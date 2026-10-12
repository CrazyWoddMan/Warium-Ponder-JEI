package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.Optional;

import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class PaintToolRecipe extends AbstractRecipe {
    public final Ingredient ingredient;
    public final BlockItem clear;
    private final String result;

    public PaintToolRecipe(ResourceLocation id, Ingredient ingredient, BlockItem clear, String result) {
        super(id);
        this.ingredient = ingredient;
        this.result = result;
        this.clear = clear;
    }

    @Override
    protected RecipeRegistry<?> getRegistry() {
        return WPJrecipes.PAINT_TOOL;
    }

    public Optional<BlockItem> getResult(Item current, String color) {
        return color.equals("remove")
        ? (current == this.clear ? Optional.empty() : Optional.of(this.clear))
        : Optional.ofNullable((BlockItem)ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(this.result.formatted(color))))
                  .filter(item -> item != current);
    }

    public static class Serializer implements RecipeSerializer<PaintToolRecipe> {

        @Override
        public PaintToolRecipe fromJson(ResourceLocation id, JsonObject json) {
            return new PaintToolRecipe(
                id,
                Ingredient.fromJson(json.get("paintable")),
                (BlockItem)GsonHelper.getAsItem(json, "clear"),
                GsonHelper.getAsString(json, "result")
            );
        }

        @SuppressWarnings("deprecation")
        @Override
        public void toNetwork(FriendlyByteBuf buffer, PaintToolRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeId(BuiltInRegistries.ITEM, recipe.clear);
            buffer.writeUtf(recipe.result);
        }

        @SuppressWarnings("deprecation")
        @Override
        public PaintToolRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new PaintToolRecipe(
                id,
                Ingredient.fromNetwork(buffer),
                (BlockItem)buffer.readById(BuiltInRegistries.ITEM),
                buffer.readUtf()
            );
        }
    }
}
