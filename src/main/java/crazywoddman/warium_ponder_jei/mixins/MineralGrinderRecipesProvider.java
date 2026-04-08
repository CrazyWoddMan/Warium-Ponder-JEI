package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.init.CrustyChunksModBlockEntities;
import net.mcreator.crustychunks.procedures.MineralGrinderProcessProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(MineralGrinderProcessProcedure.class)
public class MineralGrinderRecipesProvider {
    private static final int CRUSHING_WHEEL_SLOT = 2;
    private static final int INPUT_SLOT = 0;
    private static final int[] RESULT_SLOTS = {1, 3};

    @Inject(
        method = "execute",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void redirectProcedureCall(LevelAccessor world, double x, double y, double z, CallbackInfo ci) {
        ci.cancel();
        
        if (!(world instanceof ServerLevel level))
            return;

        BlockPos pos = BlockPos.containing(x, y, z);
        level.getBlockEntity(pos, CrustyChunksModBlockEntities.MINERAL_GRINDER.get()).ifPresent(blockEntity -> 
            WariumPonderJeiUtil.getItemHandler(blockEntity).ifPresent(handler -> {
                ItemStack crushingWheel = handler.getStackInSlot(CRUSHING_WHEEL_SLOT);

                if (crushingWheel.isEmpty() || !crushingWheel.is(WariumpjTags.Items.CRUSHING_WHEELS))
                    return;

                ItemStack input = handler.getStackInSlot(INPUT_SLOT);
                
                if (input.isEmpty())
                    return;

                ItemStack[] outputs = new ItemStack[2];

                for (int i = 0; i < 2; i++) {
                    outputs[i] = handler.getStackInSlot(RESULT_SLOTS[i]);

                    if (!outputs[i].isEmpty() && !(outputs[i].getMaxStackSize() > outputs[i].getCount()))
                        return;
                }

                WariumPonderJeiUtil.findRecipe(level, WariumpPonderJeiRecipes.MINERAL_GRINDER_TYPE, input).ifPresent(recipe -> {
                    for (int i = 0; i < 2; i++)
                        if (!outputs[i].isEmpty() && !outputs[i].is(recipe.result[i].asItem()))
                            return;
                    
                    input.shrink(1);
                    RandomSource random = level.getRandom();

                    if (crushingWheel.hurt(1, random, null)) {
                        crushingWheel.shrink(1);
                        level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                    }

                    for (int i = 0; i < recipe.result.length; i++) {
                        int chance = recipe.result[i].chance();
                        if (chance == 100 || random.nextInt(100) < chance)
                            handler.setStackInSlot(RESULT_SLOTS[i],  new ItemStack(recipe.result[i].asItem(), outputs[i].getCount() + 1));
                    }

                    level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(Blocks.COBBLESTONE.defaultBlockState()));
                    level.playSound(null, pos, SoundEvents.UI_STONECUTTER_TAKE_RESULT, SoundSource.BLOCKS, 1, 1);

                    return;
                });
            })
        );
    }
}
