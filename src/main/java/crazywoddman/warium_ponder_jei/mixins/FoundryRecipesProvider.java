package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.FoundryBlock;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(FoundryBlock.class)
public class FoundryRecipesProvider {
    private static final int TEMPLATE_SLOT = 2;
    private static final int INPUT_SLOT = 0;
    private static final int RESULT_SLOT = 1;

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/FoundryUpdateTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDD)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z) {
        ServerLevel level = (ServerLevel)world;
        BlockPos pos = BlockPos.containing(x, y, z);
        WariumPonderJeiUtil.heatedBlockProcedure(world, pos, 1).ifPresent(blockEntity ->
            WariumPonderJeiUtil.getItemHandler(blockEntity).ifPresent(handler -> {
                ItemStack template = handler.getStackInSlot(TEMPLATE_SLOT);

                if (template.isEmpty())
                    return;

                ItemStack input = handler.getStackInSlot(INPUT_SLOT);
                
                if (input.isEmpty())
                    return;

                ItemStack output = handler.getStackInSlot(RESULT_SLOT);
                int count = output.getCount();

                if (count >= output.getMaxStackSize())
                    return;

                WariumPonderJeiUtil
                .findRecipe(level, WariumpPonderJeiRecipes.FOUNDRY_TYPE, template, input)
                .filter(recipe -> input.getCount() >= recipe.count && (output.isEmpty() || output.is(recipe.result)))
                .ifPresent(recipe -> {
                    if (recipe.result == CrustyChunksModItems.PLUTONIUM_CORE.get()) {
                        level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 20, 0.3f);
                        level.sendParticles(CrustyChunksModParticleTypes.AERIAL_SPARKS.get(), x + 0.5, y + 0.5, z + 0.5, 25, 1, 1, 1, 0.3);
                    }
                    else level.playSound(null, pos, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 20, 1);

                    input.shrink(recipe.count);
                    handler.setStackInSlot(RESULT_SLOT, new ItemStack(recipe.result, count + 1));
                });
            })
        );
    }
}
