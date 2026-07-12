package crazywoddman.warium_ponder_jei.ponder.scenes;

import java.util.stream.Stream;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
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

public class MachineGunScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return Stream.of(MachineGunType.values()).map(t -> t.item).toArray(RegistryObject[]::new);
    }

    @Override
    protected String getID(SceneHelper helper) {
        return "machine_gun";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Machine Gun";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        MachineGunType type = MachineGunType.of(helper.getSubject());
        helper.setBlock(type.getBlock(), 2, 1, 2).show(Direction.DOWN);

        helper.idle(10);

        var barrel = helper.setBlock(type == MachineGunType.MINIGUN ? CrustyChunksModBlocks.MINI_GUN_BARREL : CrustyChunksModBlocks.MACHINE_GUN_BARREL, 2, 1, 1)
            .show(Direction.SOUTH);

        helper.idle(20);

        helper.showTextAt("%s uses %s as ammo",
            80, 2.5, 1.5, 2.5,
            type.getName(),
            getName(switch (type) {
                case DEFAULT, MINIGUN -> CrustyChunksModItems.LARGE_BULLET;
                case LIGHT -> CrustyChunksModItems.BULLET;
                case HEAVY -> CrustyChunksModItems.EXTRA_LARGE_BULLET;
            })

        ).attachKeyFrame();

        helper.idle(20);

        helper.showControls(CrustyChunksModItems.MACHINE_GUN_BOX, Pointing.DOWN, 40, 2.5, 2, 2.5);

        helper.idle(60);

        helper.setBlock(Blocks.STONE_BUTTON.defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 1, 1, 2)
            .show(Direction.DOWN);

        helper.idle(20);

        helper.builder.addKeyframe();
        helper.toggleRedstone(1, 1, 2);

        helper.idle(5);

        if (type == MachineGunType.MINIGUN)
            helper.builder.world().rotateSection(barrel, 0, 0, -1620, 15);

        for (int i = 0; i < 15; i++) {
            helper.createEntity(
                CrustyChunksModEntities.BULLETFIRE_PROJECTILE,
                "{Motion: [0.0d, 0.3, -2.0d], Pos: [2.5d, 1.5d, 0.0d]}"
            );
            helper.createEntity(
                CrustyChunksModEntities.GENERICLARGE_BULLET,
                "{Motion: [0.0d, 0.3, -2.0d], Pos: [2.5d, 1.5d, 0.0d]}"
            );
            helper.idle(1);
        }

        helper.toggleRedstone(1, 1, 2);
    }

    private enum MachineGunType {
        DEFAULT(CrustyChunksModItems.MACHINE_GUN),
        LIGHT(CrustyChunksModItems.LIGHT_MACHINE_GUN),
        HEAVY(CrustyChunksModItems.HEAVY_MACHINE_GUN),
        MINIGUN(CrustyChunksModItems.MINIGUN);

        final RegistryObject<Item> item;

        private MachineGunType(RegistryObject<Item> item) {
            this.item = item;
        }

        static MachineGunType of(Item item) {
            for (MachineGunType type : MachineGunType.values())
                if (type.item.get() == item)
                    return type;

            throw new IllegalStateException("Unknown Machine Gun Type: " + AbstractScene.getName(item));
        }

        String getName() {
            return AbstractScene.getName(this.item);
        }

        Block getBlock() {
            return ((BlockItem)this.item.get()).getBlock();
        }
    }
}
