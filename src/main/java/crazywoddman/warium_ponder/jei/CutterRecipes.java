package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record CutterRecipes(ItemStack input, ItemStack output) {

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }
    
    public static final List<CutterRecipes> RECIPES = List.of(
        new CutterRecipes(
            WariumPonderUtil.getItem("cast_component"),
            WariumPonderUtil.getItem("steel_gear")
        ),
        new CutterRecipes(
            WariumPonderUtil.getItem("bent_component"),
            WariumPonderUtil.getItem("cut_component")
        ),
        new CutterRecipes(
            WariumPonderUtil.getItem("copper_plate"),
            WariumPonderUtil.getItem("copper_wire")
        ),
        new CutterRecipes(
            WariumPonderUtil.getItem("steelplate"),
            WariumPonderUtil.getItem("steel_wire")
        )
    );
}