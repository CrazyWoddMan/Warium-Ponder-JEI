package crazywoddman.warium_ponder_jei.compat.jei.categories;

import crazywoddman.warium_ponder_jei.compat.jei.StructuredRecipeCategory;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import crazywoddman.warium_ponder_jei.data.recipes.MineralGrinderRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.crafting.Ingredient;

public class MineralGrinderCategory implements IRecipeCategory<MineralGrinderRecipe> {
    public static final RecipeType<MineralGrinderRecipe> TYPE = new RecipeType<>(
        WariumpPonderJeiRecipes.MINERAL_GRINDER_TYPE.getId(),
        MineralGrinderRecipe.class
    );
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable raw;
    private final IDrawable dust;

    public MineralGrinderCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.drawableBuilder(
            ResourceLocation.fromNamespaceAndPath("crusty_chunks", "textures/screens/mineral_grinder_gui.png"),
            20, 5, 120, 67
        ).setTextureSize(172, 166).build();
        this.icon = guiHelper.createDrawableItemLike(CrustyChunksModItems.MINERAL_GRINDER.get());
        this.raw = guiHelper.drawableBuilder(
            ResourceLocation.fromNamespaceAndPath("crusty_chunks", "textures/screens/rawgray.png"),
            0, 0, 16, 16
        ).setTextureSize(16, 16).build();
        this.dust = guiHelper.drawableBuilder(
            ResourceLocation.fromNamespaceAndPath("crusty_chunks", "textures/screens/graydust.png"),
            0, 0, 16, 16
        ).setTextureSize(16, 16).build();
    }

    @Override
    public RecipeType<MineralGrinderRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.crusty_chunks." + CrustyChunksModItems.MINERAL_GRINDER.getId().getPath());
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
    
    @SuppressWarnings("removal")
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MineralGrinderRecipe recipe, IFocusGroup focuses) {
        builder
        .addInputSlot(21, 21)
        .addIngredients(Ingredient.of(WariumpjTags.Items.CRUSHING_WHEELS))
        .addTooltipCallback((view, tooltip) ->
            tooltip.add(1, Component.literal("Loses 1 durability point").withStyle(ChatFormatting.GOLD))
        );

        builder
        .addInputSlot(57, 3)
        .setBackground(raw, 0, 0)
        .addIngredients(recipe.ingredients[0]);

        int[][] slots = {{57, 48}, {84, 48}};
        StructuredRecipeCategory.buildItemOutputs(builder, slots, b -> b.setBackground(dust, 0, 0), recipe.result);
    }
}
