package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public record MineralGrinderRecipes(ItemStack input, ItemStack output, ItemStack chanceoutput) {

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getChanceOutput() {
        return chanceoutput;
    }

    public static final List<MineralGrinderRecipes> RECIPES = List.of(
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("raw_uranium"),
            WariumPonderUtil.getItem("uranium_neutraltiny_dust"),
            WariumPonderUtil.getItem("uranium_depleted_tiny_dust")
        ),
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("raw_zinc"),
            WariumPonderUtil.getItem("zinc_dust"),
            WariumPonderUtil.getItem("zinc_dust")
        ),
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("raw_lead"),
            WariumPonderUtil.getItem("lead_dust"),
            WariumPonderUtil.getItem("lead_dust")
        ),
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("raw_nickel"),
            WariumPonderUtil.getItem("nickel_dust"),
            WariumPonderUtil.getItem("nickel_dust")
        ),
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("raw_beryllium"),
            WariumPonderUtil.getItem("beryllium_dust"),
            WariumPonderUtil.getItem("beryllium_dust")
        ),
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("raw_lithium"),
            WariumPonderUtil.getItem("lithium_dust"),
            WariumPonderUtil.getItem("lithium_dust")
        ),
        new MineralGrinderRecipes(
            new ItemStack(Items.RAW_GOLD),
            WariumPonderUtil.getItem("gold_dust"),
            WariumPonderUtil.getItem("gold_dust")
        ),
        new MineralGrinderRecipes(
            new ItemStack(Items.RAW_IRON),
            WariumPonderUtil.getItem("iron_dust"),
            WariumPonderUtil.getItem("iron_dust")
        ),
        new MineralGrinderRecipes(
            new ItemStack(Items.RAW_COPPER),
            WariumPonderUtil.getItem("copper_dust"),
            WariumPonderUtil.getItem("copper_dust")
        ),
        new MineralGrinderRecipes(
            new ItemStack(Items.COBBLESTONE),
            new ItemStack(Items.GRAVEL),
            new ItemStack(Items.FLINT)
        ),
        new MineralGrinderRecipes(
            new ItemStack(Items.GRAVEL),
            new ItemStack(Items.SAND),
            new ItemStack(Items.FLINT)
        ),
        new MineralGrinderRecipes(
            WariumPonderUtil.getItem("bauxite"),
            WariumPonderUtil.getItem("bauxite_dust"),
            new ItemStack(Items.SAND)
        )
    );
}