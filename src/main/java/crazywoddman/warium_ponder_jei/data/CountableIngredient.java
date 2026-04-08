package crazywoddman.warium_ponder_jei.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class CountableIngredient {
    public static final CountableIngredient EMPTY = new CountableIngredient(Ingredient.EMPTY, 0);
    private final Ingredient ingredient;
    private final int count;

    public CountableIngredient(Ingredient ingredient, int count) {
        this.ingredient = ingredient;
        this.count = count;
    }

    public boolean isEmpty() {
        return ingredient.isEmpty() || count <= 0;
    }

    public boolean test(ItemStack stack) {
        return ingredient.test(stack);
    }

    public Ingredient asIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }

    public ItemStack[] getItems() {
        ItemStack[] stacks = ingredient.getItems();

        for (ItemStack stack : stacks)
            stack.setCount(count);

        return stacks;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        ingredient.toNetwork(buffer);
        buffer.writeByte(count);
    }

    public static CountableIngredient fromNetwork(FriendlyByteBuf buffer) {
        return new CountableIngredient(Ingredient.fromNetwork(buffer), buffer.readByte());
    }

    public JsonObject toJson() {
        JsonObject json = ingredient.toJson().getAsJsonObject();

        if (count > 1)
            json.addProperty("count", count);

        return json;
    }

    public static CountableIngredient fromJson(JsonElement jsonElement) {
        JsonObject json = jsonElement.getAsJsonObject();
        JsonObject ingredientJson = new JsonObject();

        for (String key : json.keySet())
            if (!key.equals("count"))
                ingredientJson.add(key, json.get(key));
        
        return new CountableIngredient(
            Ingredient.fromJson(ingredientJson),
            GsonHelper.getAsInt(json, "count", 1)
        );
    }
}