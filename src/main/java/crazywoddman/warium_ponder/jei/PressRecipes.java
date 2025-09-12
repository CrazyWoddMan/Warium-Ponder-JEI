package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public record PressRecipes(Ingredient input, ItemStack output) {

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }
    
    public static final List<PressRecipes> RECIPES = List.of(
        new PressRecipes(
            WariumPonderUtil.getTag("forge", "ingots/steel"),
            WariumPonderUtil.getItem("steelplate")
        ),
        new PressRecipes(
            Ingredient.of(WariumPonderUtil.getItem("steelplate")),
            WariumPonderUtil.getItem("bent_component")
        ),
        new PressRecipes(
            Ingredient.of(WariumPonderUtil.getItem("bent_component")),
            WariumPonderUtil.getItem("steel_tube")
        ),
        new PressRecipes(
            Ingredient.of(Items.COPPER_INGOT),
            WariumPonderUtil.getItem("copper_plate")
        ),
        new PressRecipes(
            WariumPonderUtil.getTag("forge", "ingots/brass"),
            WariumPonderUtil.getItem("brass_plate")
        ),
        new PressRecipes(
            WariumPonderUtil.getTag("forge", "ingots/aluminum"),
            WariumPonderUtil.getItem("aluminum_plate")
        )
    );
}