package crazywoddman.warium_ponder_jei.ponder.scenes;

import java.util.stream.Stream;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModEntities;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class WarheadScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return Stream.of(WarheadType.values()).map(t -> t.item).toArray(RegistryObject[]::new);
    }

    @Override
    protected String getName(SceneHelper helper) {
        return switch (WarheadType.of(helper.getSubject())) {
            case HEAVY -> "Heavy";
            case INCENDIARY -> "Fire";
            case CLUSTER -> "Cluster";
        } + " Warhead";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        WarheadType type = WarheadType.of(helper.getSubject());
        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_THRUSTER.get().defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 3, 1, 3)
            .show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_CORE.get().defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 3, 2, 3)
            .show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(type.getBlock().defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 3, 3, 3)
            .show(Direction.DOWN);

        helper.idle(10);

        var fins = helper.setBlock(CrustyChunksModBlocks.ORDINANCE_FINS, 1, 1, 3).show(Direction.DOWN);

        helper.idle(5);

        var core = helper.setBlock(CrustyChunksModBlocks.ORDINANCE_CORE, 1, 1, 2).show(Direction.SOUTH);

        helper.idle(5);

        var nose = helper.setBlock(type.getBlock(), 1, 1, 1).show(Direction.SOUTH);

        helper.idle(20);

        helper.showTextAt("Thruster for missile", 30, 3.5, 1.5, 3)
            .attachKeyFrame();

        helper.idle(40);

        helper.showTextAt("Fins for bomb", 30, 1.5, 2, 3.5)
            .attachKeyFrame();

        helper.idle(50);

        if (type == WarheadType.HEAVY) {
            helper.moveSection(15, 0, 0, -1, nose);

            helper.idle(20);

            helper.moveSection(0, -1, 0,
                helper.setBlock(CrustyChunksModBlocks.ORDINANCE_INLINE_WARHEAD, 1, 2, 1).show(Direction.DOWN)
            );

            helper.idle(20);

            helper.showTextAt("Bomb power can be further increased with 1 or 2 sections of %s",
                100, 1.5, 1.5, 1.5,
                getName(CrustyChunksModItems.ORDINANCE_INLINE_WARHEAD)
            ).attachKeyFrame();

            helper.idle(40);

            helper.moveSection(15, 0, 0, 1, core, fins);

            helper.idle(20);

            helper.moveSection(0, -1, 0,
                helper.setBlock(CrustyChunksModBlocks.ORDINANCE_INLINE_WARHEAD, 1, 2, 2).show(Direction.DOWN)
            );

            helper.idle(60);
        }

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 2, 1, 3).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 2, 2, 3).show(Direction.DOWN);

        helper.idle(20);

        helper.setBlock(Blocks.STONE_BUTTON, 2, 2, 2).show(Direction.SOUTH);

        helper.idle(20);

        helper.showTextAt("Send redstone signal next to the %s to activate the bomb/missile",
            80, 2.5, 2.5, 3,
            getName(CrustyChunksModItems.ORDINANCE_CORE)
        ).attachKeyFrame();

        helper.idle(20);
 
        helper.toggleRedstone(2, 2, 2);

        helper.idle(5);

        helper.removeBlocks(3, 1, 3, 3, 3, 3);
        helper.createEntity(
            switch (type) {
                case HEAVY -> CrustyChunksModEntities.LARGE_ROCKET;
                case INCENDIARY -> CrustyChunksModEntities.INCINDIARY_ROCKET_PROJECTILE;
                case CLUSTER -> CrustyChunksModEntities.CLUSTER_ROCKET;
            },
            "{Motion: [0.0d, 4.0d, -0.3d], Rotation: [0.0f, 90.0f], Pos: [3.5d, 5.0d, 3.5d]}"
        );

        helper.idle(15);

        helper.toggleRedstone(2, 2, 2);
    }

    private enum WarheadType {
        HEAVY(CrustyChunksModItems.ORDINANCE_HEAVY_WARHEAD),
        INCENDIARY(CrustyChunksModItems.ORDINANCE_INCENDIARY_WARHEAD),
        CLUSTER(CrustyChunksModItems.ORDINANCE_CLUSTER_WARHEAD);

        final RegistryObject<Item> item;

        private WarheadType(RegistryObject<Item> item) {
            this.item = item;
        }

        static WarheadType of(Item item) {
            for (WarheadType type : WarheadType.values())
                if (type.item.get() == item)
                    return type;

            throw new IllegalStateException("Unknown Warhead Type: " + AbstractScene.getName(item));
        }

        Block getBlock() {
            return ((BlockItem)this.item.get()).getBlock();
        }
    }
}
