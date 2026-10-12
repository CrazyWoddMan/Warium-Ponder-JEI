package crazywoddman.warium_ponder_jei.data.recipes;

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

public class BreederReactorRecipe extends SimpleRecipe<Item> implements IDurationalRecipe<Item> {
    public final int processtime;

    public BreederReactorRecipe(ResourceLocation id, Ingredient ingredient, int processtime, Item result) {
        super(id, result, ingredient);
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

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.BREEDER_REACTOR;
    }

    public static class Serializer implements RecipeSerializer<BreederReactorRecipe> {

        @Override
        public BreederReactorRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new BreederReactorRecipe(
                id,
                Ingredient.fromJson(json.get("ingredient")),
                GsonHelper.getAsInt(json, "processtime"),
                GsonHelper.getAsItem(json, "result")
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BreederReactorRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            buffer.writeVarInt(recipe.processtime);
            buffer.writeItem(new ItemStack(recipe.result));
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
    }
}