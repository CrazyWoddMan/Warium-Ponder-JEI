package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.function.Supplier;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class DurationalAssemblyRecipe extends MultiResultRecipe<ChancedItem> implements IDurationalRecipe<ChancedItem> {
    public final int processtime;

    public DurationalAssemblyRecipe(
        ResourceLocation id,
        Supplier<? extends Serializer<?>> serializer,
        Supplier<? extends RecipeType<?>> type,
        Ingredient ingredient,
        int processtime,
        ChancedItem... results
    ) {
        super(id, serializer, type, ingredient, results);
        this.processtime = processtime;
    }

    @Override
    public int getProcessTime() {
        return this.processtime;
    }

    @Override
    public ChancedItem[] getResults() {
        return this.result;
    }

    public static abstract class Serializer<T extends DurationalAssemblyRecipe> implements RecipeSerializer<T> {
        protected abstract T recipeof(ResourceLocation id, Ingredient ingredient, int passes, ChancedItem... results);
        protected abstract String getName();

        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            try {
                JsonArray resultsArr = GsonHelper.getAsJsonArray(json, "results");
                int size = resultsArr.size();

                if (size > 4 || size < 1)
                    throw new IllegalStateException(getName() + " recipe %s results exceed the 1-4 range: ".formatted(id.toString()) + resultsArr.size());

                ChancedItem[] results = new ChancedItem[size];

                for (int i = 0; i < size; i++)
                    results[i] = ChancedItem.fromJson(resultsArr.get(i).getAsJsonObject(), id);
                
                return recipeof(
                    id,
                    Ingredient.fromJson(json.get("ingredient")),
                    Math.max(0, GsonHelper.getAsInt(json, "processtime")),
                    results
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse " + getName() + " recipe [" + id + "]", e);
            }
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int processtime = buffer.readVarInt();
            ChancedItem[] results = new ChancedItem[buffer.readByte()];

            for (int i = 0; i < results.length; i++)
                results[i] = ChancedItem.fromNetwork(buffer);
            
            return recipeof(id, ingredient, processtime, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, T recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            buffer.writeVarInt(recipe.processtime);
            buffer.writeByte(recipe.result.length);

            for (ChancedItem result : recipe.result)
                result.toNetwork(buffer);
        }
    }
}