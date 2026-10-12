package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.BreederReactorInterfaceBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import crazywoddman.warium_ponder_jei.utils.WPJutil;
import crazywoddman.warium_ponder_jei.utils.WPJutil.BlockSoundPlayer;

@Mixin(BreederReactorInterfaceBlock.class)
public class BreederReactorInterfaceBlockMixin {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/BreederReactorTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDD)V",
            remap = false
        ),
        remap = true
    )
    private void wpj$execute(LevelAccessor world, double x, double y, double z, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!WPJutil.verifyReactor(level, pos))
            return;

        BlockEntity blockEntity = level.getBlockEntity(pos);
        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(cap -> cap.getStackInSlot(0)).ifPresent(input ->
            WPJutil.getItemHandler(level.getBlockEntity(pos.below(2))).ifPresent(output -> {
                CompoundTag data = blockEntity.getPersistentData();
                int progress = data.getInt("progress");

                if ((input.isEmpty() || !WPJutil.findRecipe(level, WPJrecipes.BREEDER_REACTOR, input).map(recipe -> {
                    if (!WPJutil.readItemSlots(output, recipe.result))
                        return true;

                    int passes = recipe.processtime * 4;
                    BlockSoundPlayer player = new BlockSoundPlayer(level, pos);

                    if (progress >= passes) {
                        data.remove("progress");
                        WPJutil.writeItemSlots(output, input, i -> true, recipe.result);
                        level.sendParticles(
                            ParticleTypes.SONIC_BOOM,
                            x + 0.5,
                            y + 0.5,
                            z + 0.5,
                            15,
                            0.6, 0.6, 0.6,
                            0.1
                        );
                        player.playSound(SoundEvents.BEACON_ACTIVATE, 20, 3);
                    } else {
                        data.putInt("progress", progress + 1);
                        level.sendParticles(
                            ParticleTypes.SOUL_FIRE_FLAME,
                            x + 0.5,
                            y + 0.5,
                            z + 0.5,
                            10,
                            0.3,
                            0.6,
                            0.3,
                            0.01
                        );
                        player.playSound(SoundEvents.BEACON_AMBIENT, 10, 1 + (float)progress / passes * 2);
                    }

                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_NONE);
                    return true;
                }).orElse(false)) && progress != 0) {
                    data.remove("progress");
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_NONE);
                }
            })
        );
    }
}
