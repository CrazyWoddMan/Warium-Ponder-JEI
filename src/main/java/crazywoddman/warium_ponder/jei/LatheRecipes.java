package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record LatheRecipes(ItemStack input, ItemStack output) {

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static final List<LatheRecipes> RECIPES = List.of(
        new LatheRecipes(
            WariumPonderUtil.getItem("cast_component"),
            WariumPonderUtil.getItem("bored_component")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("steel_cylinder"),
            WariumPonderUtil.getItem("steel_tube")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("brass_ingot"),
            WariumPonderUtil.getItem("brass_fitting")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("large_projectile"),
            WariumPonderUtil.getItem("hollowed_large_projectile")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("extra_large_projectile"),
            WariumPonderUtil.getItem("hollowed_extra_large_projectile")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("huge_projectile"),
            WariumPonderUtil.getItem("hollowed_huge_projectile")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("small_unbored_barrel"),
            WariumPonderUtil.getItem("small_bored_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("medium_unbored_barrel"),
            WariumPonderUtil.getItem("medium_bored_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("large_unbored_barrel"),
            WariumPonderUtil.getItem("large_bored_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("huge_unbored_barrel"),
            WariumPonderUtil.getItem("huge_bored_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("small_unbored_cannon_barrel"),
            WariumPonderUtil.getItem("autocannon_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("medium_unbored_cannon_barrel"),
            WariumPonderUtil.getItem("autocannon_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("large_unbored_cannon_barrel"),
            WariumPonderUtil.getItem("battle_cannon_barrel")
        ),
        new LatheRecipes(
            WariumPonderUtil.getItem("huge_unbored_cannon_barrel"),
            WariumPonderUtil.getItem("artillery_barrel")
        )
    );
}