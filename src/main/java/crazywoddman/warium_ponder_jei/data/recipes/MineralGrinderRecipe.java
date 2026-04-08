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

public class MineralGrinderRecipe extends MultiResultRecipe<ChancedItem> {

    public MineralGrinderRecipe(ResourceLocation id, Ingredient ingredient, ChancedItem... results) {
        super(id, WariumpPonderJeiRecipes.MINERAL_GRINDER_SERIALIZER, WariumpPonderJeiRecipes.MINERAL_GRINDER_TYPE, ingredient, results);
    }

    public static class Serializer implements RecipeSerializer<MineralGrinderRecipe> {

        @Override
        public MineralGrinderRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                JsonArray resultsArr = GsonHelper.getAsJsonArray(json, "results");
                int size = resultsArr.size();

                if (size > 2 || size < 1)
                    throw new IllegalStateException("mineral grinder recipe %s results exceed the 1-2 range: ".formatted(id.toString()) + resultsArr.size());

                ChancedItem[] results = new ChancedItem[size];

                for (int i = 0; i < size; i++)
                    results[i] = ChancedItem.fromJson(resultsArr.get(i).getAsJsonObject(), id);
                
                return new MineralGrinderRecipe(
                    id,
                    Ingredient.fromJson(json.get("ingredient")),
                    results
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse mineral grinder recipe [" + id + "]", e);
            }
        }

        @Override
        public MineralGrinderRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ChancedItem[] results = new ChancedItem[buffer.readByte()];

            for (int i = 0; i < results.length; i++)
                results[i] = ChancedItem.fromNetwork(buffer);

            return new MineralGrinderRecipe(
                id,
                ingredient,
                results
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, MineralGrinderRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            buffer.writeByte(recipe.result.length);

            for (ChancedItem result : recipe.result)
                result.toNetwork(buffer);
        }
    }

    private static MineralGrinderRecipe of(Supplier<Item> ingredient, Supplier<Item> result1, Supplier<Item> result2) {
        return new MineralGrinderRecipe(
            null,
            Ingredient.of(ingredient.get()),
            new ChancedItem(result1),
            new ChancedItem(result2, 25)
        );
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<MineralGrinderRecipe> STATIC_RECIPES = List.of(
            of(
                CrustyChunksModItems.RAW_URANIUM,
                CrustyChunksModItems.URANIUM_NEUTRAL_DUST,
                CrustyChunksModItems.URANIUM_DEPLETED_TINY_DUST
            ),
            of(
                CrustyChunksModItems.RAW_ZINC,
                CrustyChunksModItems.ZINC_DUST,
                CrustyChunksModItems.ZINC_DUST
            ),
            of(
                CrustyChunksModItems.RAW_LEAD,
                CrustyChunksModItems.LEAD_DUST,
                CrustyChunksModItems.LEAD_DUST
            ),
            of(
                CrustyChunksModItems.RAW_NICKEL,
                CrustyChunksModItems.NICKEL_DUST,
                CrustyChunksModItems.NICKEL_DUST
            ),
            of(
                CrustyChunksModItems.RAW_BERYLLIUM,
                CrustyChunksModItems.BERYLLIUM_DUST,
                CrustyChunksModItems.BERYLLIUM_DUST
            ),
            of(
                CrustyChunksModItems.RAW_LITHIUM,
                CrustyChunksModItems.LITHIUM_DUST,
                CrustyChunksModItems.LITHIUM_DUST
            ),
            of(
                CrustyChunksModItems.PYROCHLORE,
                CrustyChunksModItems.PYROCHLORE_DUST,
                CrustyChunksModItems.PYROCHLORE_DUST
            ),
            of(
                () -> Items.RAW_GOLD,
                CrustyChunksModItems.GOLD_DUST,
                CrustyChunksModItems.GOLD_DUST
            ),
            of(
                () -> Items.RAW_IRON,
                CrustyChunksModItems.IRON_DUST,
                CrustyChunksModItems.IRON_DUST
            ),
            of(
                () -> Items.RAW_COPPER,
                CrustyChunksModItems.COPPER_DUST,
                CrustyChunksModItems.COPPER_DUST
            ),
            of(
                () -> Items.COBBLESTONE,
                () -> Items.GRAVEL,
                () -> Items.FLINT
            ),
            of(
                () -> Items.GRAVEL,
                () -> Items.SAND,
                () -> Items.FLINT
            ),
            of(
                CrustyChunksModItems.BAUXITE,
                CrustyChunksModItems.BAUXITE_DUST,
                () -> Items.SAND
            )
    );
}