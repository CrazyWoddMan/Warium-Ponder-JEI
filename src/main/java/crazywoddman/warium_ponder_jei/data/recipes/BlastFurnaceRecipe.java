package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BlastFurnaceRecipe extends SimpleRecipe<Item> {

    public BlastFurnaceRecipe(ResourceLocation id, Ingredient[] ingredients, Item result) {
        super(id, WariumpPonderJeiRecipes.BLAST_FURNACE_SERIALIZER, WariumpPonderJeiRecipes.BLAST_FURNACE_TYPE, result, ingredients);
    }

    public static class Serializer implements RecipeSerializer<BlastFurnaceRecipe> {

        @Override
        public BlastFurnaceRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                JsonArray ingArr = GsonHelper.getAsJsonArray(json, "ingredients");
                int size = ingArr.size();

                if (size != 2)
                    throw new IllegalStateException("alloy furnace recipe %s must have 2 results but instead has ".formatted(id.toString()) + ingArr.size());

                Ingredient[] ingredients = new Ingredient[size];

                for (int i = 0; i < size; i++)
                    ingredients[i] = Ingredient.fromJson(ingArr.get(i));

                return new BlastFurnaceRecipe(
                    id,
                    ingredients,
                    GsonHelper.getAsItem(json, "result")
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse alloy furnace recipe [" + id + "]", e);
            }
        }

        @Override
        public BlastFurnaceRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new BlastFurnaceRecipe(
                id,
                new Ingredient[]{
                    Ingredient.fromNetwork(buffer),
                    Ingredient.fromNetwork(buffer)
                },
                buffer.readItem().getItem()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BlastFurnaceRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            recipe.ingredients[1].toNetwork(buffer);
            buffer.writeItem(new ItemStack(recipe.result));
        }
    }

    private static BlastFurnaceRecipe of(Ingredient ingredient1, Ingredient ingredient2, Supplier<Item> result) {
        return new BlastFurnaceRecipe(
            null,
            new Ingredient[]{
                ingredient1,
                ingredient2
            },
            result.get()
        );
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<BlastFurnaceRecipe> STATIC_RECIPES = List.of(
        of(
            Ingredient.of(WariumpjTags.Items.IRON_DUST),
            Ingredient.of(WariumpjTags.Items.IRON_DUST),
            CrustyChunksModItems.STEEL_INGOT
        ),
        of(
            Ingredient.of(WariumpjTags.Items.ZINC_DUST),
            Ingredient.of(WariumpjTags.Items.COPPER_DUST),
            CrustyChunksModItems.BRASS_INGOT
        ),
        of(
            Ingredient.of(CrustyChunksModItems.COMPRESSED_ADVANCED_MIXTURE.get()),
            Ingredient.of(CrustyChunksModItems.COMPRESSED_ADVANCED_MIXTURE.get()),
            CrustyChunksModItems.ADVANCED_ALLOY_INGOT
        )
    );
}