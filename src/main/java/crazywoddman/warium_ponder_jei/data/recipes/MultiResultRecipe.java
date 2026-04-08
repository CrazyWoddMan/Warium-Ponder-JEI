package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class MultiResultRecipe<T> extends SimpleRecipe<T[]> {

    @SafeVarargs
    public MultiResultRecipe(ResourceLocation id, Supplier<? extends RecipeSerializer<?>> serializer, Supplier<? extends RecipeType<?>> type, Ingredient[] ingredients, T... results) {
        super(id, serializer, type, results, ingredients);
    }

    @SafeVarargs
    public MultiResultRecipe(ResourceLocation id, Supplier<? extends RecipeSerializer<?>> serializer, Supplier<? extends RecipeType<?>> type, Ingredient ingredient, T... results) {
        this(id, serializer, type, new Ingredient[]{ingredient}, results);
    }
}