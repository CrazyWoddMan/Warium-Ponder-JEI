package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.RefineryBlock;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.RefineryRecipe;
import crazywoddman.warium_ponder_jei.util.WariumPonderJeiUtil;

@Mixin(RefineryBlock.class)
public class RefineryRecipesProvider {
    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/mcreator/crustychunks/procedures/RefineryOnTickUpdateProcedure;execute(Lnet/minecraft/world/level/LevelAccessor;DDD)V",
            remap = false
        ),
        remap = true
    )
    private void redirectProcedureCall(LevelAccessor world, double x, double y, double z, BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getBlockState(pos.above()).is(CrustyChunksModBlocks.REFINERY_TOWER.get()))
            return;

        WariumPonderJeiUtil.heatedBlockProcedure(world, pos, 4).ifPresent(blockEntity -> {
            MutableBoolean inputIsFluid = new MutableBoolean();

            WariumPonderJeiUtil.getItemHandler(blockEntity).ifPresent(ihandler -> {
                ItemStack input = ihandler.getStackInSlot(0);
                LazyOptional<IFluidHandler> fhandler = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER);
                FluidStack fluid = fhandler.map(handler -> handler.getFluidInTank(0)).orElse(null);
                WariumPonderJeiUtil
                .findRecipe(level, WariumpPonderJeiRecipes.REFINERY_TYPE, r -> recipeMatch(r, input, fluid, inputIsFluid))
                .ifPresent(recipe -> {
                    BlockPos topTower;
                    
                    for (int i = 0;; i++) {
                        if (!level.getBlockState(pos.above(i + 2)).is(CrustyChunksModBlocks.REFINERY_TOWER.get())) {
                            if (recipe.result.length > i + 1)
                                return;
                            else {
                                topTower = pos.above(i + 2);
                                break;
                            }
                        }
                    }

                    IFluidHandler[] caps = new IFluidHandler[recipe.result.length];

                    for (int i = 0; i < recipe.result.length; i++) {
                        int index = i;
                        
                        if (!level.getBlockEntity(pos.above(i + 1)).getCapability(ForgeCapabilities.FLUID_HANDLER).map(cap ->
                            (caps[index] = cap).fill(recipe.result[index], FluidAction.SIMULATE) == recipe.result[index].getAmount()
                        ).orElse(false))
                            return;
                    }

                    for (int i = 0; i < recipe.result.length; i++)
                        caps[i].fill(recipe.result[i], FluidAction.EXECUTE);

                    if (inputIsFluid.isTrue())
                        fhandler.resolve().get().drain(1000, FluidAction.EXECUTE);
                    else {
                        if (input.getItem() instanceof BucketItem)
                            ihandler.setStackInSlot(0, new ItemStack(Items.BUCKET));
                        else input.shrink(1);
                    }

                    level.playSound(
                        null,
                        topTower,
                        SoundEvents.FIRECHARGE_USE,
                        SoundSource.BLOCKS,
                        20,
                        0.2f
                    );
                    level.sendParticles(
                        CrustyChunksModParticleTypes.RISING_FLAME.get(),
                        x + 0.5,
                        topTower.getY() + 0.5,
                        z + 0.5,
                        1,
                        0, 0, 0,
                        0.1
                    );
                });
            });
        });
    }

    private static boolean recipeMatch(RefineryRecipe recipe, ItemStack stack, @Nullable FluidStack fluid, MutableBoolean inputIsFluid) {
        if (!stack.isEmpty() && recipe.test(stack))
            return true;

        if (fluid != null)
            for (ItemStack item : recipe.ingredients[0].getItems())
                if (item.getItem() instanceof BucketItem bucket && fluid.getFluid() == bucket.getFluid() && fluid.getAmount() >= 1000) {
                    inputIsFluid.setTrue();
                    return true;
                }

        return false;
    }
}
