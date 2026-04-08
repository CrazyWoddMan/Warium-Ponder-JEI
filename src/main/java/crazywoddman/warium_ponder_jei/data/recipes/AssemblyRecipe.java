package crazywoddman.warium_ponder_jei.data.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

public class AssemblyRecipe extends SimpleRecipe<Item> {
    public final int runs;

    public AssemblyRecipe(ResourceLocation id, Ingredient item, Ingredient process, Item result, int runs) {
        super(id, WariumpPonderJeiRecipes.ASSEMBLY_SERIALIZER, WariumpPonderJeiRecipes.ASSEMBLY_TYPE, result, item, process);
        this.runs = runs;
    }

    public static class Serializer implements RecipeSerializer<AssemblyRecipe> {

        @Override
        public AssemblyRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                return new AssemblyRecipe(
                    id,
                    parseIngredient(json, "item"),
                    parseIngredient(json, "process"),
                    GsonHelper.getAsItem(json, "result"),
                    GsonHelper.getAsInt(json, "runs", 1)
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse assembly recipe [" + id + "]", e);
            }
        }

        private Ingredient parseIngredient(JsonObject json, String name) {
            JsonElement element = json.get(name);

            if (element.isJsonPrimitive()) {
                ResourceLocation key = ResourceLocation.tryParse(element.getAsString());
                return ForgeRegistries.ITEMS.containsKey(key)
                ? Ingredient.of(ForgeRegistries.ITEMS.getValue(key))
                : Ingredient.of(ItemTags.create(key));
            }
            
            return Ingredient.fromJson(element);
        }

        @Override
        public AssemblyRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new AssemblyRecipe(
                id,
                Ingredient.fromNetwork(buffer),
                Ingredient.fromNetwork(buffer),
                buffer.readItem().getItem(),
                buffer.readInt()
            );
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AssemblyRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            recipe.ingredients[1].toNetwork(buffer);
            buffer.writeItem(new ItemStack(recipe.result));
            buffer.writeInt(recipe.runs);
        }
    }
}