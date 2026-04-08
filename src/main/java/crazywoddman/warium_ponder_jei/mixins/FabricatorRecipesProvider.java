package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.AssemblyCircuitFabricatorBlock;
import net.mcreator.crustychunks.block.AssemblyMechanicalFabricatorBlock;
import net.mcreator.crustychunks.block.entity.AssemblyMechanicalFabricatorBlockEntity;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.mcreator.crustychunks.init.CrustyChunksModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.DurationalAssemblyRecipe;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil.BlockSoundPlayer;

@Mixin({AssemblyMechanicalFabricatorBlock.class, AssemblyCircuitFabricatorBlock.class})
public class FabricatorRecipesProvider {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/BasicFabricatorScriptProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/level/block/state/BlockState;)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z, BlockState state, BlockState bs, ServerLevel level, BlockPos pos, RandomSource random) {
        WariumPonderJeiUtil.tryAssembly(level, pos, true, (blockEntity, input, output) -> {
            CompoundTag data = blockEntity.getPersistentData();
            int progress = data.getInt("progress");
            boolean isMechanical = blockEntity instanceof AssemblyMechanicalFabricatorBlockEntity;
            RecipeType<? extends DurationalAssemblyRecipe> type = (isMechanical ? WariumpPonderJeiRecipes.MECHANICAL_FABRICATOR_TYPE : WariumpPonderJeiRecipes.CIRCUIT_FABRICATOR_TYPE).get();

            if ((input.isEmpty() || !WariumPonderJeiUtil.findRecipe(level, () -> type, input).map(recipe -> {
                if (!WariumPonderJeiUtil.readItemSlots(output, recipe.result))
                    return true;

                BlockSoundPlayer player = new BlockSoundPlayer(level, pos);
                int passes = recipe.processtime * 4;

                if (progress >= passes) {
                    data.remove("progress");
                    data.remove("ProgressFraction");
                    WariumPonderJeiUtil.writeItemSlots(output, input, random, recipe.result);
                    level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, x + 0.5, y + 0.5, z + 0.5, 10, 0.6, 0.6, 0.6, 0.1);

                    for (SoundEvent sound : new SoundEvent[]{SoundEvents.CONDUIT_ACTIVATE, SoundEvents.CONDUIT_DEACTIVATE})
                        player.playSound(sound, 1, 3);

                    player.playSound(CrustyChunksModSounds.MEGAMECHSTEP.get(), 3, 3);
                } else {
                    int next = progress + 1;
                    data.putInt("progress", next);
                    data.putDouble("ProgressFraction", (double)next / passes);

                    if (isMechanical) {
                        level.sendParticles(CrustyChunksModParticleTypes.SPARKS.get(), x + 0.5, y + 0.9, z + 0.5, 2, 0.1, 0.1, 0.1, 0.5);
                        player.playSound(CrustyChunksModSounds.FABRICATOR.get(), 3, 1 + (float)progress / passes * 3);
                        if (random.nextInt(10) == 0)
                            player.playSound(SoundEvents.ANVIL_PLACE, 3, Mth.nextFloat(random, 0.4f, 0.7f));
                        else if (random.nextInt(10) == 0) {
                            player.playSound(CrustyChunksModSounds.MOTOR.get(), 3, Mth.nextFloat(random, 0.8f, 1.3f));
                            player.playSound(CrustyChunksModSounds.MECHSTEP.get(), 4, 1.3f);
                        } else if (random.nextInt(10) == 0)
                            player.playSound(CrustyChunksModSounds.SPARKS.get(), 3, Mth.nextFloat(random, 0.8f, 1.3f));
                    } else {
                        if (random.nextInt(10) == 0)
                            player.playSound(CrustyChunksModSounds.SPARKS.get(), 3, Mth.nextFloat(random, 0.8f, 1.3f));
                        else if (random.nextInt(10) == 0)
                            player.playSound(CrustyChunksModSounds.DRYFIRE.get(), 3, Mth.nextFloat(random, 0.8f, 1.3f));
                        else if (random.nextInt(10) == 0)
                            player.playSound(CrustyChunksModSounds.FABRICATOR.get(), 3, Mth.nextFloat(random, 1.9f, 2));
                    }
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
