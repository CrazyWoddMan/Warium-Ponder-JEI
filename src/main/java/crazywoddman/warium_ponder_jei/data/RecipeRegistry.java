package crazywoddman.warium_ponder_jei.data;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class RecipeRegistry<T extends Recipe<Container>> {
    private final RegistryObject<RecipeType<T>> type;
    private final RegistryObject<RecipeSerializer<T>> serializer;
    
    public RecipeRegistry(RegistryObject<RecipeType<T>> type, RegistryObject<RecipeSerializer<T>> serializer) {
        this.type = type;
        this.serializer = serializer;
    }
    
    public RecipeType<T> type() {
        return this.type.get();
    }

    public RecipeSerializer<T> serializer() {
        return this.serializer.get();
    }

    public ResourceLocation getId() {
        return this.type.getId();
    }

    public List<T> get(Level level) {
        return level.getRecipeManager().getAllRecipesFor(this.type());
    }
}