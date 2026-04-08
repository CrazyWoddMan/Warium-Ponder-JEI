package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class SimpleRecipe<T> extends AbstractRecipe {
    public final Ingredient[] ingredients;
    public final T result;

    public SimpleRecipe(ResourceLocation id, Supplier<? extends RecipeSerializer<?>> serializer, Supplier<? extends RecipeType<?>> type, T result, Ingredient... ingredients) {
        super(id, serializer, type);
        this.ingredients = ingredients;
        this.result = result;
    }

    public boolean test(ItemStack... ingredients) {
        for (int i = 0; i < this.ingredients.length && i < ingredients.length; i++)
            if (!this.ingredients[i].test(ingredients[i]))
                return false;

        return true;
    }
}