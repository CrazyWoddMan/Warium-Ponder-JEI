package crazywoddman.warium_ponder.jei;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fluids.FluidStack;


public class WariumPonderUtil {

    public static ItemStack getItem(String id) {
        return new ItemStack(
            ForgeRegistries.ITEMS.getValue(
                ResourceLocation.fromNamespaceAndPath("crusty_chunks", id)
            )
        );
    }

    public static FluidStack getFluid(String id, int amount) {
        return new FluidStack(
            ForgeRegistries.FLUIDS.getValue(ResourceLocation.fromNamespaceAndPath("crusty_chunks", id)),
            amount
        );
    }

    public static Ingredient getTag(String namespace, String id) {
        return Ingredient.of(
            ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, id))
        );
    }
}