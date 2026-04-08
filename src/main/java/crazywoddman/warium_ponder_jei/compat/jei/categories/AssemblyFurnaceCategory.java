package crazywoddman.warium_ponder_jei.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;

public class AssemblyFurnaceCategory extends StructuredRecipeCategory<SmeltingRecipe> {
    public static final RecipeType<SmeltingRecipe> TYPE = RecipeType.create("crusty_chunks", CrustyChunksModItems.ASSEMBLY_FURNACE.getId().getPath(), SmeltingRecipe.class);

    public AssemblyFurnaceCategory(IGuiHelper guiHelper) {
        super(
            guiHelper,
            TYPE,
            CrustyChunksModItems.ASSEMBLY_FURNACE,
            guiHelper.createBlankDrawable(114, 81),
            1.7f,
            35, 55
        );
        firebox();
        addBlock(CrustyChunksModItems.ASSEMBLY_FURNACE, 0, 1, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_INPUT, -1, 1, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_OUTPUT, 1, 1, 0);
        addBlock(CrustyChunksModItems.BLAST_FUNNEL, 0, 2, 0);
        addBlock(CrustyChunksModItems.BLAST_FUNNEL, 0, 3, 0);
        addBlock(CrustyChunksModItems.BLAST_FUNNEL, 0, 4, 0);
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(3, 40)
        .setStandardSlotBackground()
        .addIngredients(recipe.getIngredients().get(0));

        int[][] slots = {{77, 40}, {95, 40}, {77, 58}, {95, 58}};
        buildItemOutputs(builder, slots, recipe.getResultItem(getRegistryAccess()).getItem());
    }
}
