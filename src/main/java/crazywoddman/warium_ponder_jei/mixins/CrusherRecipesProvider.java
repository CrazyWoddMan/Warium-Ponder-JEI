package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.AssemblyCrusherBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(AssemblyCrusherBlock.class)
public class CrusherRecipesProvider {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/AssemblyCrusherTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/level/block/state/BlockState;)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z, BlockState state, BlockState bs, ServerLevel level, BlockPos pos, RandomSource random) {
        WariumPonderJeiUtil.tryAssembly(level, pos, false, (blockEntity, input, output) ->
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(cap -> cap.getStackInSlot(0)).filter(s -> s.is(WariumpjTags.Items.CRUSHING_WHEELS)).ifPresent(crushingWheel ->
                WariumPonderJeiUtil
                .findRecipe(level, WariumpPonderJeiRecipes.CRUSHER_TYPE, input)
                .filter(r -> WariumPonderJeiUtil.processAssembly(output, input, random, r.result))
                .ifPresent(recipe -> {
                    if (crushingWheel.hurt(1, random, null)) {
                        crushingWheel.shrink(1);
                        level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                    }
                    level.levelEvent(
                        LevelEvent.PARTICLES_DESTROY_BLOCK,
                        pos,
                        Block.getId((input.getItem() instanceof BlockItem blockItem ? blockItem.getBlock() : Blocks.COBBLESTONE).defaultBlockState())
                    );
                    level.playSound(null, pos, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 0.9f, 1.1f);
                })
            )
        );
    }
}
