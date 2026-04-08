package crazywoddman.warium_ponder_jei.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.network.chat.Component;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.RefineryRecipe;

public class RefineryCategory extends StructuredRecipeCategory<RefineryRecipe> {
    public static final RecipeType<RefineryRecipe> TYPE = new RecipeType<>(WariumpPonderJeiRecipes.REFINERY_TYPE.getId(), RefineryRecipe.class);

    public RefineryCategory(IGuiHelper guiHelper) {
        super(
            guiHelper,
            TYPE,
            CrustyChunksModItems.REFINERY,
            guiHelper.createBlankDrawable(73, 161),
            2.1f,
            30, 111
        );
    }
    
    @SuppressWarnings("removal")
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RefineryRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(6, 102)
        .setStandardSlotBackground()
        .addIngredients(recipe.ingredients[0]);
        
        for (int i = 0; i < recipe.result.length; i++) {
            int amount =  recipe.result[i].getAmount();
            builder
            .addOutputSlot(6, 84 - i * 18)
            .addFluidStack(recipe.result[i].getFluid(), amount)
            .addTooltipCallback((view, tooltip) -> {
                tooltip.add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip.collect_with_bucket").withStyle(ChatFormatting.GOLD));
                tooltip.add(2, Component.literal(amount + " mB").withStyle(ChatFormatting.GRAY));
            });
        }
    }

    @Override
    public void draw(RefineryRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        this.structure.clear();
        firebox();
        addBlock(CrustyChunksModItems.REFINERY, 0, 1, 0);

        for (int i = 0; i < recipe.result.length; i++)
            addBlock(CrustyChunksModItems.REFINERY_TOWER, 0, i + 2, 0);

        super.draw(recipe, slots, graphics, mouseX, mouseY);
    }
}
