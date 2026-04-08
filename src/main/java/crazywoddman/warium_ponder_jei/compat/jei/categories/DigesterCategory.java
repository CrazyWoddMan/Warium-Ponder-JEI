package crazywoddman.warium_ponder_jei.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.network.chat.Component;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.ChatFormatting;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.DigesterRecipe;

public class DigesterCategory extends StructuredRecipeCategory<DigesterRecipe> {
    public static final RecipeType<DigesterRecipe> TYPE = new RecipeType<>(
        WariumpPonderJeiRecipes.DIGESTER_TYPE.getId(),
        DigesterRecipe.class
    );

    public DigesterCategory(IGuiHelper guiHelper) {
        super(
            guiHelper,
            TYPE,
            CrustyChunksModItems.BAUXITE_DIGESTER,
            guiHelper.createBlankDrawable(106, 57),
            1.7f,
            31, 5
        );
        addBlock(CrustyChunksModItems.BAUXITE_DIGESTER, 0, 0, 0, tooltip -> tooltip
            .add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip.kinetic.required")
            .withStyle(ChatFormatting.GOLD))
        );
        addBlock(CrustyChunksModItems.PRODUCTION_INPUT, -1, 0, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_OUTPUT, 1, 0, 0);
    }

    @Override
    public RecipeType<DigesterRecipe> getRecipeType() {
        return TYPE;
    }
    
    @SuppressWarnings("removal")
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DigesterRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(1, 4)
        .addIngredients(recipe.ingredients[0])
        .setStandardSlotBackground();

        IRecipeSlotBuilder catalyst = builder.addInputSlot(33, 40).addIngredients(recipe.ingredients[1]);

        if (recipe.consumeChance < 100)
            catalyst.setStandardSlotBackground().addTooltipCallback((view, tooltip) ->
                tooltip.add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip." + (recipe.consumeChance > 0 ? "consume_chance" : "not_consumed"), recipe.consumeChance).withStyle(ChatFormatting.GOLD))
            );

        int[][] slots = {{71, 4}, {89, 4}, {71, 22}, {89, 22}};
        buildItemOutputs(builder, slots, recipe.result);
    }
}
