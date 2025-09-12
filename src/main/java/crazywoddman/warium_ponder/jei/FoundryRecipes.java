package crazywoddman.warium_ponder.jei;

import java.util.List;
import net.minecraft.world.item.ItemStack;

public record FoundryRecipes(ItemStack inputtop, ItemStack inputbottom, ItemStack output) {

    public ItemStack getInputTop() {
        return inputtop;
    }

    public ItemStack getInputBottom() {
        return inputbottom;
    }

    public ItemStack getOutput() {
        return output;
    }

    public static final List<FoundryRecipes> RECIPES = List.of(
        new FoundryRecipes(
            WariumPonderUtil.getItem("component_foundry_template"),
            WariumPonderUtil.getItem("steel_ingot"),
            WariumPonderUtil.getItem("cast_component")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("cylinder_foundry_template"),
            WariumPonderUtil.getItem("steel_ingot"),
            WariumPonderUtil.getItem("steel_cylinder")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("small_projectile_foundry_template"),
            WariumPonderUtil.getItem("lead_nugget").copyWithCount(3),
            WariumPonderUtil.getItem("small_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("medium_projectile_foundry_template"),
            WariumPonderUtil.getItem("lead_nugget").copyWithCount(6),
            WariumPonderUtil.getItem("medium_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("large_projectile_foundry_template"),
            WariumPonderUtil.getItem("lead_ingot"),
            WariumPonderUtil.getItem("large_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("extra_large_projectile_template"),
            WariumPonderUtil.getItem("lead_ingot").copyWithCount(3),
            WariumPonderUtil.getItem("extra_large_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("huge_projectile_foundry_template"),
            WariumPonderUtil.getItem("lead_ingot").copyWithCount(6),
            WariumPonderUtil.getItem("huge_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("small_barrel_template"),
            WariumPonderUtil.getItem("steel_ingot"),
            WariumPonderUtil.getItem("small_unbored_barrel")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("medium_barrel_template"),
            WariumPonderUtil.getItem("steel_ingot").copyWithCount(3),
            WariumPonderUtil.getItem("medium_unbored_barrel")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("large_barrel_template"),
            WariumPonderUtil.getItem("steel_ingot").copyWithCount(4),
            WariumPonderUtil.getItem("large_unbored_barrel")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("huge_barrel_foundry_template"),
            WariumPonderUtil.getItem("steel_ingot").copyWithCount(6),
            WariumPonderUtil.getItem("huge_unbored_barrel")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("small_cannon_foundry_template"),
            WariumPonderUtil.getItem("steel_ingot").copyWithCount(8),
            WariumPonderUtil.getItem("small_unbored_cannon_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("medium_cannon_foundry_template"),
            WariumPonderUtil.getItem("steel_block"),
            WariumPonderUtil.getItem("medium_unbored_cannon_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("large_cannon_foundry_template"),
            WariumPonderUtil.getItem("steel_block"),
            WariumPonderUtil.getItem("large_unbored_cannon_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("huge_cannon_foundry_template"),
            WariumPonderUtil.getItem("steel_block").copyWithCount(2),
            WariumPonderUtil.getItem("huge_unbored_cannon_projectile")
        ),
        new FoundryRecipes(
            WariumPonderUtil.getItem("component_foundry_template"),
            WariumPonderUtil.getItem("plutonium_ingot").copyWithCount(4),
            WariumPonderUtil.getItem("plutonium_core")
        )
    );
}