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

public class WariumpjTags {
    public static class Items {
        public static final TagKey<Item> CRUSHING_WHEELS = create("crushing_wheels");

        public static final TagKey<Item> RAW_LEAD = rawMaterial("lead");
        public static final TagKey<Item> RAW_ZINC = rawMaterial("zinc");
        public static final TagKey<Item> RAW_NICKEL = rawMaterial("nickel");
        public static final TagKey<Item> RAW_BERYLLIUM = rawMaterial("beryllium");
        public static final TagKey<Item> RAW_URANIUM = rawMaterial("uranium");
        public static final TagKey<Item> RAW_LITHIUM = rawMaterial("lithium");

        public static final TagKey<Item> IRON_DUST = forge("dusts/iron");
        public static final TagKey<Item> ZINC_DUST = forge("dusts/zinc");
        public static final TagKey<Item> COPPER_DUST = forge("dusts/copper");

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
        public static final TagKey<Block> KINETIC_OUTPUT_FRONT = create("kineticoutputfront");
        public static final TagKey<Block> FUEL_ROD = create("fuelrod");

        public static ITag<Block> get(TagKey<Block> tagkey) {
            return ForgeRegistries.BLOCKS.tags().getTag(tagkey);
        }

        private static TagKey<Block> create(String path) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("crusty_chunks", path));
        }
    }
}
