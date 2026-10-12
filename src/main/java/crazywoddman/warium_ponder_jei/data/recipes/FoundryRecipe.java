package crazywoddman.warium_ponder_jei.data.recipes;

import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.CountableIngredient;
import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
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
        super(id, result, template, ingredient.ingredient);
        this.count = ingredient.count;
    }

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.FOUNDRY;
    }

    public static class Serializer implements RecipeSerializer<FoundryRecipe> {

        @Override
        public FoundryRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new FoundryRecipe(
                id,
                Ingredient.fromJson(json.get("template")),
                CountableIngredient.fromJson(json.get("ingredient")),
                GsonHelper.getAsItem(json, "result")
            );
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
            new CountableIngredient(recipe.ingredients[1], recipe.count).toNetwork(buffer);
            buffer.writeItem(new ItemStack(recipe.result));
        }
    }
}