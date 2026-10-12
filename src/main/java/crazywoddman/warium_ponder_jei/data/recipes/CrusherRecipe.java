package crazywoddman.warium_ponder_jei.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrusherRecipe extends MultiResultRecipe<ChancedItem> {

    public CrusherRecipe(ResourceLocation id, Ingredient ingredient, ChancedItem... results) {
        super(id, ingredient, results);
    }

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.CRUSHER;
    }

    public static class Serializer implements RecipeSerializer<CrusherRecipe> {

        @Override
        public CrusherRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonArray resultsArr = GsonHelper.getAsJsonArray(json, "results");
            int size = resultsArr.size();

            if (size > 4 || size < 1)
                throw new IllegalStateException("crusher recipe %s results exceed the 1-4 range: ".formatted(id.toString()) + resultsArr.size());

            ChancedItem[] results = new ChancedItem[size];

            for (int i = 0; i < size; i++)
                results[i] = ChancedItem.fromJson(resultsArr.get(i).getAsJsonObject(), id);
            
            return new CrusherRecipe(
                id,
                Ingredient.fromJson(json.get("ingredient")),
                results
            );
        }

        @Override
        public CrusherRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            ChancedItem[] results = new ChancedItem[buffer.readByte()];

            for (int i = 0; i < results.length; i++)
                results[i] = ChancedItem.fromNetwork(buffer);
            
            return new CrusherRecipe(id, input, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            buffer.writeByte(recipe.result.length);

            for (ChancedItem result : recipe.result)
                result.toNetwork(buffer);
        }
    }
}