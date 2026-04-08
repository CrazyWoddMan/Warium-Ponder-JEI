package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.AssemblyDepotBlock;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.AssemblySounds;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(AssemblyDepotBlock.class)
public class AssemblyMachineRecipesProvider {

    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/AssemblyDepotTickProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDDLnet/minecraft/world/level/block/state/BlockState;)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z, BlockState state, BlockState bs, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos above = pos.above();
        BlockState machine = level.getBlockState(above);

        if (!machine.is(CrustyChunksModBlocks.ASSEMBLY_MACHINE.get()))
            return;

        WariumPonderJeiUtil.tryAssembly(level, pos, false, f -> true, (blockEntity, input, output) -> {
            BlockPos kineticSource = above.relative(machine.getValue(HorizontalDirectionalBlock.FACING).getOpposite());

            if (!level.getBlockState(kineticSource).is(WariumpjTags.Blocks.KINETIC_OUTPUT_FRONT))
                return;

            double kineticPower = level.getBlockEntity(kineticSource).getPersistentData().getDouble("KineticPower");

            if (kineticPower <= 0)
                return;

            level.getBlockEntity(pos.above()).getCapability(ForgeCapabilities.ITEM_HANDLER).map(cap -> cap.getStackInSlot(0)).filter(item -> !item.isEmpty()).ifPresent(process ->
                WariumPonderJeiUtil
                .findRecipe(level, WariumpPonderJeiRecipes.ASSEMBLY_TYPE, input, process)
                .ifPresent(recipe -> {
                    int slot = 0;
                    int count;

                    for (;;) {
                        ItemStack stack = output.getStackInSlot(slot);
                        count = stack.getCount();

                        if (!stack.isEmpty() && (!stack.is(recipe.result)) || !(stack.getMaxStackSize() > count)) {
                            if (slot < 3) slot++;
                            else return;
                        } else break;
                    }

                    CompoundTag data = blockEntity.getPersistentData();
                    double work = data.getDouble("work") + kineticPower / 9;
                    boolean completed = work >= recipe.runs;

                    if (completed) {
                        data.remove("work");
                        input.shrink(1);
                        output.setStackInSlot(slot,  new ItemStack(recipe.result, count + 1));
                    }
                    else data.putDouble("work", work);

                    if (process.hurt(1, random, null)) {
                        process.shrink(1);
                        level.playSound(null, pos, SoundEvents.ITEM_BREAK, SoundSource.BLOCKS, 1, 1);
                    }

                    level.sendParticles(
                        CrustyChunksModParticleTypes.SPARKS.get(),
                        x + 0.5,
                        y + 0.9,
                        z + 0.5,
                        5,
                        0.1, 0.1, 0.1,
                        0.5
                    );
                    AssemblySounds.get(process.getItem()).ifPresent(sound ->
                        level.playSound(null, pos, sound, SoundSource.BLOCKS, 2, completed ? 0.8f : 0.1f + recipe.runs / 10f)
                    );
                })
            );
        });
    }
}
