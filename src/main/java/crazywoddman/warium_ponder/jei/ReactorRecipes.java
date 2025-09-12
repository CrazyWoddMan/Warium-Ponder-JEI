package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record ReactorRecipes(ItemStack input, ItemStack output) {

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static final List<ReactorRecipes> RECIPES = List.of(
        new ReactorRecipes(
            WariumPonderUtil.getItem("uranium_enriched_dust"),
            WariumPonderUtil.getItem("plutonium_nugget")
        ),
        new ReactorRecipes(
            WariumPonderUtil.getItem("enriched_lithium_ingot"),
            WariumPonderUtil.getItem("tiny_lithium_deuteride")
        )
    );
}