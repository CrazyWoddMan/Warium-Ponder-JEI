package crazywoddman.warium_ponder_jei.data;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;

public class WPJtags {
    public static class Items {
        public static final TagKey<Item>
        CRUSHING_WHEELS = create("crushing_wheels"),

        RAW_LEAD = rawMaterial("lead"),
        RAW_ZINC = rawMaterial("zinc"),
        RAW_NICKEL = rawMaterial("nickel"),
        RAW_BERYLLIUM = rawMaterial("beryllium"),
        RAW_URANIUM = rawMaterial("uranium"),
        RAW_LITHIUM = rawMaterial("lithium"),

        IRON_DUST = forge("dusts/iron"),
        ZINC_DUST = forge("dusts/zinc"),
        COPPER_DUST = forge("dusts/copper");

        public static ITag<Item> get(TagKey<Item> tagkey) {
            return ForgeRegistries.ITEMS.tags().getTag(tagkey);
        }

        private static TagKey<Item> create(String namespace, String path) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, path));
        }

        private static TagKey<Item> create(String path) {
            return create(WariumPonderJei.MODID, path);
        }

        private static TagKey<Item> forge(String name) {
            return create("forge", name);
        }

        private static TagKey<Item> rawMaterial(String name) {
            return forge("raw_materials/" + name);
        }
    }

    public static class Blocks {
        public static final TagKey<Block>
        KINETIC_OUTPUT_FRONT = create("kineticoutputfront"),
        FUEL_ROD = create("fuelrod");

        public static ITag<Block> get(TagKey<Block> tagkey) {
            return ForgeRegistries.BLOCKS.tags().getTag(tagkey);
        }

        private static TagKey<Block> create(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("crusty_chunks", path));
        }
    }
}
