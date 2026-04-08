package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.BauxiteDigesterBlock;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.mcreator.crustychunks.init.CrustyChunksModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(BauxiteDigesterBlock.class)
public class DigesterRecipesProvider {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/BauxiteDigesterScriptProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/level/block/state/BlockState;)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z, BlockState state, BlockState bs, ServerLevel level, BlockPos pos, RandomSource random) {
        WariumPonderJeiUtil.tryAssembly(level, pos, false, (blockEntity, input, output) ->
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(cap -> cap.getStackInSlot(0)).filter(stack -> !stack.isEmpty()).ifPresent(catalyst ->
                WariumPonderJeiUtil
                .findRecipe(level, WariumpPonderJeiRecipes.DIGESTER_TYPE, input, catalyst)
                .filter(r -> WariumPonderJeiUtil.processAssembly(output, input, random, r.result))
                .ifPresent(recipe -> {
                    if (recipe.consumeChance == 100 || random.nextInt(100) < recipe.consumeChance)
                        catalyst.shrink(1);
                    level.sendParticles(
                        CrustyChunksModParticleTypes.SPARKS.get(),
                        x + 0.5,
                        y + 0.9,
                        z + 0.5,
                        5,
                        0.1, 0.1, 0.1,
                        0.5
                    );
                    for (SoundEvent sound : new SoundEvent[]{SoundEvents.GRINDSTONE_USE, CrustyChunksModSounds.MOTOR.get()})
                        level.playSound(
                        null,
                        pos,
                        sound,
                        SoundSource.BLOCKS,
                        2.0f,
                        Mth.nextFloat(random, 0.2f, 0.4f)
                    );
                })
            )
        );
    }
}
