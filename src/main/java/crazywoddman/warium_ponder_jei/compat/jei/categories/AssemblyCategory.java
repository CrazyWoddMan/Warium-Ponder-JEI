package crazywoddman.warium_ponder_jei.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.mcreator.crustychunks.init.CrustyChunksModItems;

import java.util.LinkedHashSet;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.recipes.AssemblyRecipe;

public class AssemblyCategory extends StructuredRecipeCategory<AssemblyRecipe> {
    public static final RecipeType<AssemblyRecipe> TYPE = RecipeType.create("crusty_chunks", "assembly", AssemblyRecipe.class);

    public AssemblyCategory(IGuiHelper guiHelper) {
        super(
            guiHelper,
            TYPE,
            CrustyChunksModItems.ASSEMBLY_MACHINE,
            guiHelper.createBlankDrawable(106, 66),
            1.7f,
            31, 14,
            new LinkedHashSet<>()
        );
        addBlock(CrustyChunksModItems.PRODUCTION_INPUT, -1, 0, 0);
        addBlock(CrustyChunksModItems.ASSEMBLY_DEPOT, 0, 0, 0);
        addBlock(CrustyChunksModItems.ASSEMBLY_MACHINE, 0, 1, 0, tooltip -> tooltip
            .add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip.kinetic.required")
            .withStyle(ChatFormatting.GOLD))
        );
        addBlock(CrustyChunksModItems.PRODUCTION_OUTPUT, 1, 0, 0);
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AssemblyRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(1, 13)
        .addIngredients(recipe.ingredients[0])
        .setStandardSlotBackground();

        builder
        .addInputSlot(33, 49)
        .addIngredients(recipe.ingredients[1])
        .setStandardSlotBackground();

        int[][] slots = {{71, 13}, {89, 13}, {71, 31}, {89, 31}};
        buildItemOutputs(builder, slots, recipe.result);
    }
}