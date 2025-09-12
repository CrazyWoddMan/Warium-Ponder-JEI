package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public record OilRefineryRecipes(ItemStack input, FluidStack output1, FluidStack output2, FluidStack output3, FluidStack output4) {

    public ItemStack getInput() {
        return input;
    }

    public FluidStack getOutput1() {
        return output1;
    }

    public FluidStack getOutput2() {
        return output2;
    }

    public FluidStack getOutput3() {
        return output3;
    }

    public FluidStack getOutput4() {
        return output4;
    }

    public static final List<OilRefineryRecipes> RECIPES = List.of(
        new OilRefineryRecipes(
            WariumPonderUtil.getItem("shale_oil"),
            WariumPonderUtil.getFluid("oil", 1000),
            WariumPonderUtil.getFluid("diesel", 1000),
            WariumPonderUtil.getFluid("kerosene", 1000),
            WariumPonderUtil.getFluid("petrolium", 1000)
        )
    );
}