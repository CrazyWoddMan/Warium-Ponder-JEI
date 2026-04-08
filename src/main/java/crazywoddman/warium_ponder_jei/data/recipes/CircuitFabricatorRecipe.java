package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public class CircuitFabricatorRecipe extends DurationalAssemblyRecipe {

    public CircuitFabricatorRecipe(ResourceLocation id, Ingredient input, int processtime, ChancedItem... results) {
        super(id, WariumpPonderJeiRecipes.CIRCUIT_FABRICATOR_SERIALIZER, WariumpPonderJeiRecipes.CIRCUIT_FABRICATOR_TYPE, input, processtime, results);
    }

    public static class Serializer extends DurationalAssemblyRecipe.Serializer<CircuitFabricatorRecipe> {
        @Override
        protected CircuitFabricatorRecipe recipeof(ResourceLocation id, Ingredient input, int processtime, ChancedItem... results) {
            return new CircuitFabricatorRecipe(id, input, processtime, results);
        }

        @Override
        protected String getName() {
            return "circuit fabricator";
        }
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<CircuitFabricatorRecipe> STATIC_RECIPES = List.of(
        new CircuitFabricatorRecipe(
            null,
            Ingredient.of(CrustyChunksModItems.UNFABRICATED_TECH_COMPONENT.get()),
            600,
            new ChancedItem(CrustyChunksModItems.TECH_COMPONENT)
        )
    );
}