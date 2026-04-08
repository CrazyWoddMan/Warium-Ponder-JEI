package crazywoddman.warium_ponder_jei.compat.jei.categories;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.ChatFormatting;
import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.CrusherRecipe;

public class CrusherCategory extends StructuredRecipeCategory<CrusherRecipe> {
    public static final RecipeType<CrusherRecipe> TYPE = new RecipeType<>(
        WariumpPonderJeiRecipes.CRUSHER_TYPE.getId(),
        CrusherRecipe.class
    );

    public CrusherCategory(IGuiHelper guiHelper) {
        super(
            guiHelper,
            TYPE,
            CrustyChunksModItems.ASSEMBLY_CRUSHER,
            guiHelper.createBlankDrawable(106, 57),
            1.7f,
            31, 5
        );
        addBlock(CrustyChunksModItems.ASSEMBLY_CRUSHER, 0, 0, 0, tooltip -> tooltip
            .add(1, Component.translatable(WariumPonderJei.MODID + ".tooltip.kinetic.required")
            .withStyle(ChatFormatting.GOLD))
        );
        addBlock(CrustyChunksModItems.PRODUCTION_INPUT, -1, 0, 0);
        addBlock(CrustyChunksModItems.PRODUCTION_OUTPUT, 1, 0, 0);
    }

    @Override
    public RecipeType<CrusherRecipe> getRecipeType() {
        return TYPE;
    }
    
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrusherRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(1, 4)
        .addIngredients(recipe.ingredients[0])
        .setStandardSlotBackground();

        builder
        .addInputSlot(33, 40)
        .addItemStacks(List.of(
            new ItemStack(CrustyChunksModItems.IRONGEAR.get()),
            new ItemStack(CrustyChunksModItems.STEEL_CRUSHING_WHEEL.get())
        ))
        .setStandardSlotBackground();

        int[][] slots = {{71, 4}, {89, 4}, {71, 22}, {89, 22}};
        buildItemOutputs(builder, slots, recipe.result);
    }
}
