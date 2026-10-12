package crazywoddman.warium_ponder_jei.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BlastFurnaceRecipe extends SimpleRecipe<Item> {

    public BlastFurnaceRecipe(ResourceLocation id, Ingredient[] ingredients, Item result) {
        super(id, result, ingredients);
    }

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.BLAST_FURNACE;
    }

    public static class Serializer implements RecipeSerializer<BlastFurnaceRecipe> {

        @Override
        public BlastFurnaceRecipe fromJson(ResourceLocation id, JsonObject json) {
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
}