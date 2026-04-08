package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.BlastFurnaceBlock;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(BlastFurnaceBlock.class)
public class BlastFurnaceRecipesProvider {
    private static final int INPUT_TOP_SLOT = 0;
    private static final int INPUT_BOTTOM_SLOT = 1;
    private static final int RESULT_SLOT = 2;

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/BlastFurnaceUpdateTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDD)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        WariumPonderJeiUtil.heatedBlockProcedure(level, pos, 1).ifPresent(blockEntity -> {
            for (int i = 1; i <= 3; i++)
                if (!level.getBlockState(pos.above(i)).is(CrustyChunksModBlocks.BLAST_FUNNEL.get()))
                    return;
            
            WariumPonderJeiUtil.getItemHandler(blockEntity).ifPresent(handler -> {
                ItemStack inputTop = handler.getStackInSlot(INPUT_TOP_SLOT);

                if (inputTop.isEmpty())
                    return;

                ItemStack inputBottom = handler.getStackInSlot(INPUT_BOTTOM_SLOT);
                
                if (inputBottom.isEmpty())
                    return;

                ItemStack output = handler.getStackInSlot(RESULT_SLOT);
                int count = output.getCount();

                if (count >= output.getMaxStackSize())
                    return;

                WariumPonderJeiUtil
                .findRecipe(
                    level,
                    WariumpPonderJeiRecipes.BLAST_FURNACE_TYPE,
                    r -> (r.ingredients[0].test(inputTop) && r.ingredients[1].test(inputBottom)) || (r.ingredients[0].test(inputBottom) && r.ingredients[1].test(inputTop))
                )
                .filter(recipe -> output.isEmpty() || output.is(recipe.result))
                .ifPresent(recipe -> {
                    CompoundTag data = blockEntity.getPersistentData();
                    int progress = data.getInt("Progress");

                    if (progress >= 4) {
                        data.remove("Progress");
                        inputTop.shrink(1);
                        inputBottom.shrink(1);
                        handler.setStackInSlot(RESULT_SLOT, new ItemStack(recipe.result, count + 1));
                    }
                    else data.putInt("Progress", progress + 1);

                    level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS);
                    BlockPos top = pos.above(3);

                    for (;;) if (!level.getBlockState(top = top.above()).is(CrustyChunksModBlocks.BLAST_FUNNEL.get()))
                        break;

                    level.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        x + 0.5,
                        top.getY() + 3,
                        z + 0.5,
                        5,
                        0,
                        3,
                        0,
                        0.01
                    );
                });
            });
        });
    }
}
