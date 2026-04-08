package crazywoddman.warium_ponder_jei.data;

import java.util.function.Supplier;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

public record ChancedItem(Item item, int chance) implements ItemLike {
    public ChancedItem(Item item) {
        this(item, 100);
    }

    public ChancedItem(Supplier<Item> item, int chance) {
        this(item.get(), chance);
    }

    public ChancedItem(Supplier<Item> item) {
        this(item, 100);
    }

    @Override
    public Item asItem() {
        return this.item;
    }

    public static ChancedItem fromJson(JsonObject json, ResourceLocation id) {
        int chance;

        if (json.has("chance")) {
            chance =  GsonHelper.getAsInt(json, "chance");
            if (chance < 1 || chance > 100)
                throw new IllegalStateException("%s results exceed the 1-100 range: ".formatted(id.toString()) + chance);
        }
        else chance = 100;

        return new ChancedItem(
            ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(GsonHelper.getAsString(json, "item"))),
            chance
        );
    }

    public static ChancedItem fromNetwork(FriendlyByteBuf buffer) {
        return new ChancedItem(
            buffer.readItem().getItem(),
            buffer.readByte()
        );
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeItem(new ItemStack(this.item));
        buffer.writeByte(this.chance);
    }
}