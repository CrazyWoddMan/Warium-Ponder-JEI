package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import java.util.function.Supplier;

import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class BreederReactorRecipe extends SimpleRecipe<Item> implements IDurationalRecipe<Item> {
    public final int processtime;

    public BreederReactorRecipe(ResourceLocation id, Ingredient ingredient, int processtime, Item result) {
        super(id, WariumpPonderJeiRecipes.BREEDER_REACTOR_SERIALIZER, WariumpPonderJeiRecipes.BREEDER_REACTOR_TYPE, result, ingredient);
        this.processtime = processtime;
    }

    @Override
    public int getProcessTime() {
        return this.processtime;
    }

    @Override
    public Item[] getResults() {
        return new Item[]{this.result};
    }

    public static class Serializer implements RecipeSerializer<BreederReactorRecipe> {

        @Override
        public BreederReactorRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                return new BreederReactorRecipe(
                    id,
                    Ingredient.fromJson(json.get("ingredient")),
                    GsonHelper.getAsInt(json, "processtime"),
                    GsonHelper.getAsItem(json, "result")
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse breeder reactor recipe [" + id + "]", e);
            }
        }

        @Override
        public BreederReactorRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new BreederReactorRecipe(
                id,
                Ingredient.fromNetwork(buffer),
                buffer.readVarInt(),
                buffer.readItem().getItem()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BreederReactorRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            buffer.writeVarInt(recipe.processtime);
            buffer.writeItem(new ItemStack(recipe.result));
        }
    }

    protected static BreederReactorRecipe of(Supplier<Item> ingredient, Supplier<Item> result) {
        return new BreederReactorRecipe(
            null,
            Ingredient.of(ingredient.get()),
            400,
            result.get()
        );
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<BreederReactorRecipe> STATIC_RECIPES = List.of(
        of(CrustyChunksModItems.URANIUM_ENRICHED_DUST, CrustyChunksModItems.PLUTONIUM_NUGGET),
        of(CrustyChunksModItems.ENRICHED_LITHIUM_INGOT, CrustyChunksModItems.TINY_LITHIUM_DEUTERIDE)
    );
}