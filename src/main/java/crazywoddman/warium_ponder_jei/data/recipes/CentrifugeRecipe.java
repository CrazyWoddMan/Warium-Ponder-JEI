package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import java.util.function.Supplier;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public class CentrifugeRecipe extends DurationalAssemblyRecipe {

    public CentrifugeRecipe(ResourceLocation id, Ingredient ingredient, int processtime, ChancedItem... results) {
        super(id, WariumpPonderJeiRecipes.CENTRIFUGE_SERIALIZER, WariumpPonderJeiRecipes.CENTRIFUGE_TYPE, ingredient, processtime, results);
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

    @SafeVarargs
    protected static CentrifugeRecipe of(Supplier<Item> ingredient, int processtime, ChancedItem... results) {
        return new CentrifugeRecipe(
            null,
            Ingredient.of(ingredient.get()),
            processtime,
            results
        );
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<CentrifugeRecipe> STATIC_RECIPES = List.of(
        of(
            CrustyChunksModItems.URANIUM_NEUTRAL_DUST,
            200,
            new ChancedItem(CrustyChunksModItems.URANIUM_ENRICHED_TINY_DUST),
            new ChancedItem(CrustyChunksModItems.URANIUM_DEPLETED_DUST, 80),
            new ChancedItem(CrustyChunksModItems.LEAD_DUST, 5)
        ),
        of(
            CrustyChunksModItems.LITHIUM_DUST,
            200,
            new ChancedItem(CrustyChunksModItems.ENRICHED_LITHIUM_NUGGET)
        ),
        of(
            CrustyChunksModItems.ALUMINATE_DUST,
            1,
            new ChancedItem( CrustyChunksModItems.FILTERED_ALUMINATE_DUST),
            new ChancedItem(Items.RED_SAND, 25)
        ),
        of(
            CrustyChunksModItems.FILTERED_PYROCHLORE_DUST,
            20,
            new ChancedItem(CrustyChunksModItems.NIOBIUM_TINY_DUST),
            new ChancedItem(CrustyChunksModItems.NIOBIUM_TINY_DUST, 25),
            new ChancedItem(CrustyChunksModItems.SULFUR, 10),
            new ChancedItem(Items.QUARTZ, 10)
        )
    );
}