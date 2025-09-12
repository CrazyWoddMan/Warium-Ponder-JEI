package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record BlastFurnaceRecipes(ItemStack inputtop, ItemStack inputbottom, ItemStack output) {

    public ItemStack getInputTop() {
        return inputtop;
    }

    public ItemStack getInputBottom() {
        return inputbottom;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static final List<BlastFurnaceRecipes> RECIPES = List.of(
        new BlastFurnaceRecipes(
            WariumPonderUtil.getItem("iron_dust"),
            WariumPonderUtil.getItem("iron_dust"),
            WariumPonderUtil.getItem("steel_ingot")
        ),
        new BlastFurnaceRecipes(
            WariumPonderUtil.getItem("zinc_dust"),
            WariumPonderUtil.getItem("copper_dust"),
            WariumPonderUtil.getItem("brass_ingot")
        )
    );
}