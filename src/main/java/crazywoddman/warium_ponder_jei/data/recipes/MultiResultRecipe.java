package crazywoddman.warium_ponder_jei.data.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class MultiResultRecipe<T> extends SimpleRecipe<T[]> {

    @SafeVarargs
    public MultiResultRecipe(ResourceLocation id, Ingredient[] ingredients, T... results) {
        super(id, results, ingredients);
    }

    @SafeVarargs
    public MultiResultRecipe(ResourceLocation id, Ingredient ingredient, T... results) {
        this(id, new Ingredient[]{ingredient}, results);
    }
}