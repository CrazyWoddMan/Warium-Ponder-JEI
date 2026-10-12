package crazywoddman.warium_ponder_jei.data.recipes;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class MechanicalFabricatorRecipe extends DurationalAssemblyRecipe {

    public MechanicalFabricatorRecipe(ResourceLocation id, Ingredient input, int processtime, ChancedItem... results) {
        super(id, input, processtime, results);
    }

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.MECHANICAL_FABRICATOR;
    }

    public static class Serializer extends DurationalAssemblyRecipe.Serializer<MechanicalFabricatorRecipe> {
        
        @Override
        protected MechanicalFabricatorRecipe recipeof(ResourceLocation id, Ingredient input, int processtime, ChancedItem... results) {
            return new MechanicalFabricatorRecipe(id, input, processtime, results);
        }

        @Override
        protected String getName() {
            return "mechanical fabricator";
        }
    }
}