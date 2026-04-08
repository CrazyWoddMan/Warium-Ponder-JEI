package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class DigesterRecipe extends MultiResultRecipe<ChancedItem> {
    public final int consumeChance;

    public DigesterRecipe(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, int consumeChance, ChancedItem... results) {
        super(id, WariumpPonderJeiRecipes.DIGESTER_SERIALIZER, WariumpPonderJeiRecipes.DIGESTER_TYPE, new Ingredient[]{ingredient, catalyst}, results);
        this.consumeChance = consumeChance;
    }

    public static class Serializer implements RecipeSerializer<DigesterRecipe> {

        @Override
        public DigesterRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                JsonObject catalyst = GsonHelper.getAsJsonObject(json, "catalyst");
                int consumeChance;

                if (catalyst.has("consume_chance")) {
                    consumeChance =  GsonHelper.getAsInt(catalyst, "consume_chance");
                    if (consumeChance < 0 || consumeChance > 100)
                        throw new IllegalStateException("digester recipe %s results exceed the 0-100 range: ".formatted(id.toString()) + consumeChance);
                }
                else consumeChance = 100;

                JsonArray resultsArr = GsonHelper.getAsJsonArray(json, "results");
                int size = resultsArr.size();

                if (size > 4 || size < 1)
                    throw new IllegalStateException("digester recipe %s results exceed the 1-4 range: ".formatted(id.toString()) + resultsArr.size());

                ChancedItem[] results = new ChancedItem[size];

                for (int i = 0; i < size; i++)
                    results[i] = ChancedItem.fromJson(resultsArr.get(i).getAsJsonObject(), id);
                
                return new DigesterRecipe(
                    id,
                    Ingredient.fromJson(json.get("ingredient")),
                    Ingredient.fromJson(json.get("catalyst")),
                    catalyst.has("consume_chance") ? GsonHelper.getAsInt(catalyst, "consume_chance") : 100,
                    results
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse digester recipe [" + id + "]", e);
            }
        }

        @Override
        public DigesterRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient input = Ingredient.fromNetwork(buffer);
            Ingredient catalyst = Ingredient.fromNetwork(buffer);
            int consumeChance = buffer.readByte();
            ChancedItem[] results = new ChancedItem[buffer.readByte()];

            for (int i = 0; i < results.length; i++)
                results[i] = ChancedItem.fromNetwork(buffer);
            
            return new DigesterRecipe(id, input, catalyst, consumeChance, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, DigesterRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            recipe.ingredients[1].toNetwork(buffer);
            buffer.writeByte(recipe.consumeChance);
            buffer.writeByte(recipe.result.length);

            for (ChancedItem result : recipe.result)
                result.toNetwork(buffer);
        }
    }

    private static DigesterRecipe of(Supplier<Item> ingredient, ChancedItem... results) {
        return new DigesterRecipe(null, Ingredient.of(ingredient.get()), Ingredient.of(CrustyChunksModItems.NITRATE.get()), 6, results);
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<DigesterRecipe> STATIC_RECIPES = List.of(
        of(
            CrustyChunksModItems.BAUXITE_DUST,
            new ChancedItem(CrustyChunksModItems.ALUMINATE_DUST, 80),
            new ChancedItem(Items.RED_SAND, 5),
            new ChancedItem(Items.SAND, 5)
        ),
        of(
            CrustyChunksModItems.PYROCHLORE_DUST,
            new ChancedItem(CrustyChunksModItems.FILTERED_PYROCHLORE_DUST, 80),
            new ChancedItem(Items.QUARTZ, 5)
        )
    );
}