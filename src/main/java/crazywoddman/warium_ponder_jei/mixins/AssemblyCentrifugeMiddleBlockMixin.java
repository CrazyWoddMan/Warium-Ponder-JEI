package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.AssemblyCentrifugeMiddleBlock;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.config.WPJconfig;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import crazywoddman.warium_ponder_jei.utils.WPJutil;
import crazywoddman.warium_ponder_jei.utils.WPJutil.BlockSoundPlayer;

@Mixin(AssemblyCentrifugeMiddleBlock.class)
public class AssemblyCentrifugeMiddleBlockMixin {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/AssemblyCentrifugeTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/level/block/state/BlockState;)V",
            remap = false
        ),
        remap = true
    )
    private void wpj$execute(LevelAccessor world, double x, double y, double z, BlockState state, BlockState bs, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getBlockState(pos.above()).is(CrustyChunksModBlocks.ASSEMBLY_CENTRIFUGE_TOP.get())
            || !level.getBlockState(pos.below(2)).is(CrustyChunksModBlocks.GIANT_COIL.get())
        ) return;

        BlockState bottom = level.getBlockState(pos.below());

        if (!bottom.is(CrustyChunksModBlocks.ASSEMBLY_CENTRIFUGE_BOTTOM.get()))
            return;

        WPJutil.tryAssembly(level, pos, true, facing -> WPJutil.checkKinetic(level, pos.below(), bottom.getValue(HorizontalDirectionalBlock.FACING).getOpposite()) >= WPJconfig.MACHINES_POWER.get(), (blockEntity, input, output) -> {
            CompoundTag data = blockEntity.getPersistentData();
            int progress = data.getInt("progress");

            if ((input.isEmpty() || !WPJutil.findRecipe(level, WPJrecipes.CENTRIFUGE, input).map(recipe -> {
                if (!WPJutil.readItemSlots(output, recipe.result))
                    return true;

                int passes = recipe.processtime * 4;
                BlockSoundPlayer player = new BlockSoundPlayer(level, pos);

                if (progress >= passes) {
                    data.remove("progress");
                    data.remove("ProgressFraction");
                    WPJutil.writeItemSlots(output, input, random, recipe.result);
                    level.sendParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        x + 0.5,
                        y + 0.5,
                        z + 0.5,
                        10,
                        0.6, 0.6, 0.6,
                        0.1
                    );
                    player.playSound(SoundEvents.CONDUIT_ACTIVATE, 20, 3);
                    player.playSound(SoundEvents.CONDUIT_DEACTIVATE, 20, 3);
                } else {
                    int next = progress + 1;
                    data.putInt("progress", next);
                    data.putDouble("ProgressFraction", (double)next / passes);
                    player.playSound(SoundEvents.CONDUIT_AMBIENT, 10, 1 + (float)progress / passes * 3);
                    level.sendParticles(
                        CrustyChunksModParticleTypes.SPARKS.get(),
                        x + 0.5,
                        y + 0.9,
                        z + 0.5,
                        5,
                        0.1, 0.1, 0.1,
                        0.5F
                    );
                }

                level.sendBlockUpdated(pos, state, state, Block.UPDATE_NONE);
                return true;
            }).orElse(false)) && progress != 0) {
                data.remove("progress");
                data.remove("ProgressFraction");
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_NONE);
            }
        });
    }
}
