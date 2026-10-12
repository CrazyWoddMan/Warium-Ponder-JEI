package crazywoddman.warium_ponder_jei.data.recipes;

import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class AbstractRecipe implements Recipe<Container> {
    public final ResourceLocation id;

    public AbstractRecipe(ResourceLocation id) {
        this.id = id;
    }

    protected abstract RecipeRegistry<?> getRegistry();

    @Override
    public RecipeType<?> getType() {
        return getRegistry().type();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return getRegistry().serializer();
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(Container container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}