package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.CountableIngredient;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class FoundryRecipe extends SimpleRecipe<Item> {
    public final int count;

    public FoundryRecipe(ResourceLocation id, Ingredient template, CountableIngredient ingredient, Item result) {
        super(id, WariumpPonderJeiRecipes.FOUNDRY_SERIALIZER, WariumpPonderJeiRecipes.FOUNDRY_TYPE, result, template, ingredient.asIngredient());
        this.count = ingredient.getCount();
    }

    public static class Serializer implements RecipeSerializer<FoundryRecipe> {

        @Override
        public FoundryRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                return new FoundryRecipe(
                    id,
                    Ingredient.fromJson(json.get("template")),
                    CountableIngredient.fromJson(json.get("ingredient")),
                    GsonHelper.getAsItem(json, "result")
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse foundry recipe [" + id + "]", e);
            }
        }

        @Override
        public FoundryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new FoundryRecipe(
                id,
                Ingredient.fromNetwork(buffer),
                CountableIngredient.fromNetwork(buffer),
                buffer.readItem().getItem()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FoundryRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            recipe.ingredients[1].toNetwork(buffer);
            buffer.writeItem(new ItemStack(recipe.result));
        }
    }

    protected static FoundryRecipe of(Supplier<Item> template, Supplier<Item> input, int amount, Supplier<Item> output) {
        return new FoundryRecipe(
            null,
            Ingredient.of(template.get()),
            new CountableIngredient(Ingredient.of(input.get()), amount),
            output.get()
        );
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<FoundryRecipe> STATIC_RECIPES = List.of(
        of(
            CrustyChunksModItems.COMPONENT_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 1,
            CrustyChunksModItems.CAST_COMPONENT
        ),
        of(
            CrustyChunksModItems.CYLINDER_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 1,
            CrustyChunksModItems.STEEL_CYLINDER
        ),
        of(
            CrustyChunksModItems.SMALL_PROJECTILE_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.LEAD_NUGGET, 2,
            CrustyChunksModItems.SMALL_PROJECTILE
        ),
        of(
            CrustyChunksModItems.MEDIUM_PROJECTILE_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.LEAD_NUGGET, 4,
            CrustyChunksModItems.MEDIUM_PROJECTILE
        ),
        of(
            CrustyChunksModItems.LARGE_PROJECTILE_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.LEAD_INGOT, 6,
            CrustyChunksModItems.LARGE_PROJECTILE
        ),
        of(
            CrustyChunksModItems.EXTRA_LARGE_PROJECTILE_TEMPLATE,
            CrustyChunksModItems.LEAD_INGOT, 2,
            CrustyChunksModItems.EXTRA_LARGE_PROJECTILE
        ),
        of(
            CrustyChunksModItems.HUGE_PROJECTILE_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.LEAD_INGOT, 3,
            CrustyChunksModItems.HUGE_PROJECTILE
        ),
        of(
            CrustyChunksModItems.SMALL_BARREL_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 1,
            CrustyChunksModItems.SMALL_UNBORED_BARREL
        ),
        of(
            CrustyChunksModItems.MEDIUM_BARREL_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 3,
            CrustyChunksModItems.MEDIUM_UNBORED_BARREL
        ),
        of(
            CrustyChunksModItems.LARGE_BARREL_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 4,
            CrustyChunksModItems.LARGE_UNBORED_BARREL
        ),
        of(
            CrustyChunksModItems.HUGE_BARREL_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 6,
            CrustyChunksModItems.HUGE_UNBORED_BARREL
        ),
        of(
            CrustyChunksModItems.SMALL_CANNON_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 6,
            CrustyChunksModItems.SMALL_UNBORED_CANNON_BARREL
        ),
        of(
            CrustyChunksModItems.MEDIUM_CANNON_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 8,
            CrustyChunksModItems.MEDIUM_UNBORED_CANNON_BARREL
        ),
        of(
            CrustyChunksModItems.LARGE_CANNON_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 8,
            CrustyChunksModItems.LARGE_UNBORED_CANNON_BARREL
        ),
        of(
            CrustyChunksModItems.HUGE_CANNON_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.STEEL_INGOT, 9,
            CrustyChunksModItems.HUGE_UNBORED_CANNON_BARREL
        ),
        of(
            CrustyChunksModItems.COMPONENT_FOUNDRY_TEMPLATE,
            CrustyChunksModItems.PLUTONIUM_INGOT, 4,
            CrustyChunksModItems.PLUTONIUM_CORE
        )
    );
}