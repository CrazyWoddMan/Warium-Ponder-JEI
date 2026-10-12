package crazywoddman.warium_ponder_jei.data.recipes;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class CentrifugeRecipe extends DurationalAssemblyRecipe {

    public CentrifugeRecipe(ResourceLocation id, Ingredient ingredient, int processtime, ChancedItem... results) {
        super(id, ingredient, processtime, results);
    }

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.CENTRIFUGE;
    }

    public static class Serializer extends DurationalAssemblyRecipe.Serializer<CentrifugeRecipe> {
        @Override
        protected CentrifugeRecipe recipeof(ResourceLocation id, Ingredient ingredient, int processtime, ChancedItem... results) {
            return new CentrifugeRecipe(id, ingredient, processtime, results);
        }

        @Override
        protected String getName() {
            return "centrifuge";
        }
    }
}