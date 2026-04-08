package crazywoddman.warium_ponder_jei.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.mcreator.crustychunks.init.CrustyChunksModItems;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.CentrifugeRecipe;

public class CentrifugeCategory extends StructuredRecipeCategory<CentrifugeRecipe> {
    public static final RecipeType<CentrifugeRecipe> TYPE = new RecipeType<>(
        WariumpPonderJeiRecipes.CENTRIFUGE_TYPE.getId(),
        CentrifugeRecipe.class
    );

    public CentrifugeCategory(IGuiHelper guiHelper) {
        super(
            guiHelper,
            TYPE,
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_MIDDLE,
            guiHelper.createBlankDrawable(124, 80),
            1.7f,
            40, 55
        );
        addBlock(CrustyChunksModItems.GIANT_COIL, 0, 0, 0);
        addBlock(CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_BOTTOM, 0, 1, 0, tooltip -> tooltip
            .add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip.kinetic.required")
            .withStyle(ChatFormatting.GOLD))
        );
        addBlock(CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_MIDDLE, 0, 2, 0);
        addBlock(CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_TOP, 0, 3, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_INPUT, -1, 2, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_OUTPUT, 1, 2, 0);
    }

    @Override
    public RecipeType<CentrifugeRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable(WariumPonderJei.MODID + ".jei.category.centrifuge");
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CentrifugeRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(10, 26)
        .addIngredients(recipe.ingredients[0])
        .setStandardSlotBackground();

        int[][] slots = {{80, 26}, {98, 26}, {80, 44}, {98, 44}};
        buildItemOutputs(builder, slots, recipe.result);
    }

    @Override
    public void draw(CentrifugeRecipe recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, slots, graphics, mouseX, mouseY);
        String processtime =  recipe.processtime + "s";
        graphics.drawString(
            Minecraft.getInstance().font,
            processtime,
            124 - processtime.length() * 6,
            72,
            0x8B8B8B,
            false
        );
    }
}
