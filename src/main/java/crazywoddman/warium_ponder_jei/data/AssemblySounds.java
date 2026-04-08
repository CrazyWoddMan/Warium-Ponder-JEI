package crazywoddman.warium_ponder_jei.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.registries.ForgeRegistries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AssemblySounds extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new GsonBuilder().create();;
    private static final Map<Item, ResourceLocation> SOUNDS = new HashMap<>();

    public static Optional<SoundEvent> get(Item item) {
        return Optional.ofNullable(SOUNDS.get(item)).map(sound -> ForgeRegistries.SOUND_EVENTS.getValue(sound));
    }

    public static void register(AddReloadListenerEvent event) {
        event.addListener(new AssemblySounds());
    }

    private AssemblySounds() {
        super(GSON, "assembly_sounds");
    }
    

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> data, ResourceManager resourceManager, ProfilerFiller profiler) {
        SOUNDS.clear();
        
        data.forEach((id, json) -> {
            try {
                JsonObject obj = json.getAsJsonObject();
                for (ItemStack stack : Ingredient.fromJson(obj.get("ingredient")).getItems())
                    SOUNDS.put(stack.getItem(), ResourceLocation.parse(GsonHelper.getAsString(obj, "sound")));
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse assembly sound " + id, e);
            }
        });
    }
}