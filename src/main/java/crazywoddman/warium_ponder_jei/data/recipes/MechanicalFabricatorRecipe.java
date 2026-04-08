package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import java.util.function.Supplier;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

public class MechanicalFabricatorRecipe extends DurationalAssemblyRecipe {

    public MechanicalFabricatorRecipe(ResourceLocation id, Ingredient input, int processtime, ChancedItem... results) {
        super(id, WariumpPonderJeiRecipes.MECHANICAL_FABRICATOR_SERIALIZER, WariumpPonderJeiRecipes.MECHANICAL_FABRICATOR_TYPE, input, processtime, results);
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

    private static MechanicalFabricatorRecipe of(Ingredient input, Supplier<Item> result) {
        return new MechanicalFabricatorRecipe(null, input, 600, new ChancedItem(result));
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<MechanicalFabricatorRecipe> STATIC_RECIPES = List.of(
        of(
            Ingredient.of(CrustyChunksModItems.ADVANCED_ALLOY_INGOT.get()),
            CrustyChunksModItems.ADVANCED_ALLOY_COMPONENT
        ),
        of(
            Ingredient.of(ItemTags.create(ResourceLocation.fromNamespaceAndPath("forge", "blocks/aluminum"))),
            CrustyChunksModItems.PRECISION_COMPONENT
        )
    );
}