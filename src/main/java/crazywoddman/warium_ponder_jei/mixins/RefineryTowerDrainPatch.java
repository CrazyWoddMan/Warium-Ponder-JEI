package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.procedures.FluidDrainProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crazywoddman.warium_ponder_jei.network.NetworkHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;

@Restriction(conflict = @Condition("warium_additions"))
@Mixin(FluidDrainProcedure.class)
public class RefineryTowerDrainPatch {

    @Inject(
        method = "execute",
        at = @At("HEAD"),
        remap = false,
        cancellable = true
    )
    private static void injectExecute(LevelAccessor world, double x, double y, double z, Entity entity, CallbackInfo ci) {
        if (NetworkHandler.serverHasMod)
            ci.cancel();

        if (world.isClientSide())
            return;
        
        world.getBlockEntity(BlockPos.containing(x, y, z)).getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(handler -> {
            Player player = (Player)entity;
            ItemStack newItem;
            ItemStack handStack = player.getMainHandItem();
            Item handItem = handStack.getItem();

            if (handItem.equals(Items.BUCKET) && handler.drain(1000, IFluidHandler.FluidAction.SIMULATE).getAmount() == 1000) {
                newItem = new ItemStack(handler.drain(1000, IFluidHandler.FluidAction.EXECUTE).getFluid().getBucket());
                shrinkAndPlaySound(world, x, y, z, player, handler, handStack, newItem);
            }
            else if (handItem instanceof BucketItem bucketItem) {
                Fluid fluid = bucketItem.getFluid();
                FluidStack fluidStack = new FluidStack(fluid, 1000);
                
                if (!fluid.equals(Fluids.EMPTY) && handler.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE) == 1000) {
                    handler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                    newItem = new ItemStack(Items.BUCKET);
                    shrinkAndPlaySound(world, x, y, z, player, handler, handStack, newItem);
                }
            }
        
            FluidStack fluidInTank = handler.getFluidInTank(0);
            player.displayClientMessage(Component.literal(
                fluidInTank.isEmpty()
                    ? "Empty"
                    : (fluidInTank.getDisplayName().getString() + ": " + fluidInTank.getAmount() + "/" + handler.getTankCapacity(0))),
                true
            );
        });
    }

    private static void shrinkAndPlaySound(LevelAccessor level, double x, double y, double z, Player player, IFluidHandler handler, ItemStack handStack, ItemStack newItem) {
        if (!player.isCreative())
            handStack.shrink(1);
        
        if (handStack.isEmpty())
            player.setItemInHand(InteractionHand.MAIN_HAND, newItem);
        else if (!player.addItem(newItem))
            player.spawnAtLocation(newItem, 1);

        ((Level)level).playSound(
            null,
            x, y, z,
            SoundEvents.BUCKET_FILL,
            SoundSource.PLAYERS,
            2,
            0.2f + 1f / handler.getTankCapacity(0) * handler.getFluidInTank(0).getAmount()
        );
    }
}
