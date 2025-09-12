package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record CentrifugeRecipes(ItemStack input, ItemStack outputtop, ItemStack outputbottom) {

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutputTop() {
        return outputtop;
    }

    public ItemStack getOutputBottom() {
        return outputbottom;
    }

    public static final List<CentrifugeRecipes> RECIPES = List.of(
        new CentrifugeRecipes(
            WariumPonderUtil.getItem("uranium_neutral_dust"),
            WariumPonderUtil.getItem("uranium_enriched_tiny_dust"),
            WariumPonderUtil.getItem("uranium_depleted_dust")
        ),
        new CentrifugeRecipes(
            WariumPonderUtil.getItem("lithium_dust"),
            WariumPonderUtil.getItem("enriched_lithium_nugget"),
            ItemStack.EMPTY
        )
    );
}