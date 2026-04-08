package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CrusherRecipe extends MultiResultRecipe<ChancedItem> {

    public CrusherRecipe(ResourceLocation id, Ingredient ingredient, ChancedItem... results) {
        super(id, WariumpPonderJeiRecipes.CRUSHER_SERIALIZER, WariumpPonderJeiRecipes.CRUSHER_TYPE, ingredient, results);
    }

    public static class Serializer implements RecipeSerializer<CrusherRecipe> {

        @Override
        public CrusherRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
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
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse crusher recipe [" + id + "]", e);
            }
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

    private static CrusherRecipe of(TagKey<Item> ingredient, ChancedItem... results) {
        return new CrusherRecipe(null, Ingredient.of(ingredient), results);
    }

    private static CrusherRecipe of(Item ingredient, ChancedItem... results) {
        return new CrusherRecipe(null, Ingredient.of(ingredient), results);
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<CrusherRecipe> STATIC_RECIPES = List.of(
        of(
            Items.COBBLESTONE,
            new ChancedItem(Items.GRAVEL, 90),
            new ChancedItem(Items.FLINT, 10),
            new ChancedItem(Items.SAND, 10),
            new ChancedItem(CrustyChunksModItems.NITRATE, 10)
        ),
        of(
            Items.GRAVEL,
            new ChancedItem(Items.SAND, 90),
            new ChancedItem(Items.FLINT, 10),
            new ChancedItem(Items.CLAY_BALL, 10),
            new ChancedItem(CrustyChunksModItems.NITRATE, 15)
        ),
        of(
            Items.RED_SAND,
            new ChancedItem(CrustyChunksModItems.CHLORINE_DUST, 10),
            new ChancedItem(Items.CLAY_BALL, 20),
            new ChancedItem(CrustyChunksModItems.IRON_DUST, 2)
        ),
        of(
            Items.SAND,
            new ChancedItem(CrustyChunksModItems.CHLORINE_DUST, 10),
            new ChancedItem(Items.CLAY_BALL, 20),
            new ChancedItem(CrustyChunksModItems.NITRATE, 5)
        ),
        of(
            Items.RAW_IRON,
            new ChancedItem(CrustyChunksModItems.IRON_DUST),
            new ChancedItem(CrustyChunksModItems.IRON_DUST, 33),
            new ChancedItem(CrustyChunksModItems.NICKEL_DUST, 10)
        ),
        of(
            Items.RAW_GOLD,
            new ChancedItem(CrustyChunksModItems.GOLD_DUST),
            new ChancedItem(CrustyChunksModItems.GOLD_DUST, 25),
            new ChancedItem(Items.GOLD_NUGGET, 8)
        ),
        of(
            Items.RAW_COPPER,
            new ChancedItem(CrustyChunksModItems.COPPER_DUST),
            new ChancedItem(CrustyChunksModItems.COPPER_DUST, 33),
            new ChancedItem(CrustyChunksModItems.SULFUR, 5)
        ),
        of(
            WariumpjTags.Items.RAW_LEAD,
            new ChancedItem(CrustyChunksModItems.LEAD_DUST),
            new ChancedItem(CrustyChunksModItems.LEAD_DUST, 33),
            new ChancedItem(CrustyChunksModItems.NICKEL_DUST, 3)
        ),
        of(
            WariumpjTags.Items.RAW_ZINC,
            new ChancedItem(CrustyChunksModItems.ZINC_DUST),
            new ChancedItem(CrustyChunksModItems.ZINC_DUST, 33),
            new ChancedItem(CrustyChunksModItems.NICKEL_DUST, 2)
        ),
        of(
            CrustyChunksModItems.BAUXITE.get(),
            new ChancedItem(CrustyChunksModItems.BAUXITE_DUST),
            new ChancedItem(Items.SAND, 50),
            new ChancedItem(CrustyChunksModItems.BAUXITE_DUST, 25)
        ),
        of(
            CrustyChunksModItems.PYROCHLORE.get(),
            new ChancedItem(CrustyChunksModItems.PYROCHLORE_DUST),
            new ChancedItem(CrustyChunksModItems.PYROCHLORE_DUST, 50),
            new ChancedItem(CrustyChunksModItems.SULFUR, 25),
            new ChancedItem(CrustyChunksModItems.NICKEL_DUST, 25)
        ),
        of(
            WariumpjTags.Items.RAW_NICKEL,
            new ChancedItem(CrustyChunksModItems.NICKEL_DUST),
            new ChancedItem(CrustyChunksModItems.NICKEL_DUST, 33),
            new ChancedItem(CrustyChunksModItems.LEAD_DUST, 10)
        ),
        of(
            WariumpjTags.Items.RAW_BERYLLIUM,
            new ChancedItem(CrustyChunksModItems.BERYLLIUM_DUST),
            new ChancedItem(CrustyChunksModItems.BERYLLIUM_DUST, 33),
            new ChancedItem(CrustyChunksModItems.LITHIUM_NUGGET, 5)
        ),
        of(
            WariumpjTags.Items.RAW_URANIUM,
            new ChancedItem(CrustyChunksModItems.URANIUM_NEUTRALTINY_DUST),
            new ChancedItem(CrustyChunksModItems.URANIUM_NEUTRALTINY_DUST, 33),
            new ChancedItem(CrustyChunksModItems.URANIUM_DEPLETED_TINY_DUST, 33),
            new ChancedItem(CrustyChunksModItems.LEAD_DUST, 15)
        ),
        of(
            WariumpjTags.Items.RAW_LITHIUM,
            new ChancedItem(CrustyChunksModItems.LITHIUM_DUST),
            new ChancedItem(CrustyChunksModItems.LITHIUM_DUST, 25),
            new ChancedItem(CrustyChunksModItems.LITHIUM_NUGGET, 15)
        ),
        of(
            Items.REDSTONE,
            new ChancedItem(CrustyChunksModItems.PHOSPHORUS_DUST, 20),
            new ChancedItem(CrustyChunksModItems.SULFUR, 10),
            new ChancedItem(CrustyChunksModItems.COPPER_DUST, 10)
        ),
        new CrusherRecipe(
            null,
            Ingredient.of(Items.DIRT, Items.COARSE_DIRT),
            new ChancedItem(Items.CLAY_BALL, 33),
            new ChancedItem( CrustyChunksModItems.NITRATE, 40),
            new ChancedItem(Items.SAND, 3),
            new ChancedItem(Items.BONE_MEAL, 2)
        )
    );
}