package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.block.entity.RefineryTowerBlockEntity;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;

@Restriction(conflict = @Condition("warium_additions"))
@Mixin(RefineryTowerBlockEntity.class)
public class RefineryTowerBlockEntityMixin {

    @Shadow(remap = false)
    private FluidTank fluidTank;

    @Inject(
        method = "<init>",
        at = @At("RETURN"),
        remap = false
    )
    private void wadditions$acceptAnyFluid(BlockPos position, BlockState state, CallbackInfo callback) {
        fluidTank.setValidator(stack -> true);
    }
}
