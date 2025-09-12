package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public record ExtruderRecipes(Ingredient input, ItemStack output) {

    public Ingredient getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }
    
    public static final List<ExtruderRecipes> RECIPES = List.of(
        new ExtruderRecipes(
            Ingredient.of(WariumPonderUtil.getItem("cast_component")),
            WariumPonderUtil.getItem("steel_gear")
        ),
        new ExtruderRecipes(
            WariumPonderUtil.getTag("minecraft", "planks"),
            WariumPonderUtil.getItem("wood_component")
        ),
        new ExtruderRecipes(
            Ingredient.of(WariumPonderUtil.getItem("steel_ingot")),
            WariumPonderUtil.getItem("steel_wire")
        ),
        new ExtruderRecipes(
            Ingredient.of(WariumPonderUtil.getItem("steel_wire")),
            WariumPonderUtil.getItem("steel_spring")
        ),
        new ExtruderRecipes(
            Ingredient.of(Items.COPPER_INGOT),
            WariumPonderUtil.getItem("copper_wire")
        ),
        new ExtruderRecipes(
            Ingredient.of(WariumPonderUtil.getItem("copper_wire")),
            WariumPonderUtil.getItem("copper_coil")
        )
    );
}