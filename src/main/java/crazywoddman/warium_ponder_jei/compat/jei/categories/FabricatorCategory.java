package crazywoddman.warium_ponder_jei.compat.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.function.Supplier;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.DurationalAssemblyRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.CircuitFabricatorRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.MechanicalFabricatorRecipe;

public class FabricatorCategory<T extends DurationalAssemblyRecipe> extends StructuredRecipeCategory<T> {
    public static final RecipeType<MechanicalFabricatorRecipe> MECHANICAL_TYPE = new RecipeType<>(
        WariumpPonderJeiRecipes.MECHANICAL_FABRICATOR_TYPE.getId(),
        MechanicalFabricatorRecipe.class
    );
    public static final RecipeType<CircuitFabricatorRecipe> CIRCUIT_TYPE = new RecipeType<>(
        WariumpPonderJeiRecipes.CIRCUIT_FABRICATOR_TYPE.getId(),
        CircuitFabricatorRecipe.class
    );

    public FabricatorCategory(IGuiHelper guiHelper, RecipeType<T> type, Supplier<Item> item) {
        super(
            guiHelper,
            type,
            item,
            guiHelper.createBlankDrawable(106, 53),
            1.7f,
            31, 8
        );
        addBlock(item, 0, 0, 0, tooltip -> tooltip
            .add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip.kinetic.required")
            .withStyle(ChatFormatting.GOLD))
        );
        addBlock(CrustyChunksModItems.PRODUCTION_INPUT, -1, 0, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_OUTPUT, 1, 0, 0);
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(1, 7)
        .addIngredients(recipe.ingredients[0])
        .setStandardSlotBackground();

        int[][] slots = {{71, 7}, {89, 7}, {71, 25}, {89, 25}};
        buildItemOutputs(builder, slots, recipe.result);
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView slots, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, slots, graphics, mouseX, mouseY);
        String processtime = recipe.processtime + "s";
        graphics.drawString(
            Minecraft.getInstance().font,
            processtime,
            106 - processtime.length() * 6,
            45,
            0x8B8B8B,
            false
        );
    }
}
