package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModEntities;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class TorpedoScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{CrustyChunksModItems.TORPEDO_THRUSTER};
    }

    @Override
    public String getSchematic() {
        return "15x15_water";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Torpedo";
    }

    @Override
    protected boolean showBasePlate() {
        return false;
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.builder.configureBasePlate(5, 5, 5);
        helper.builder.setSceneOffsetY(-4);
        helper.builder.removeShadow();
        var plate = helper.showSection(Direction.UP, 5, 4, 5, 9, 4, 9);

        helper.idle(10);

        helper.setBlock(CrustyChunksModBlocks.TORPEDO_THRUSTER, 7, 5, 8).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_CORE, 7, 5, 7).show(Direction.SOUTH);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_HEAVY_WARHEAD, 7, 5, 6).show(Direction.SOUTH);

        helper.idle(20);

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 7, 6, 6).show(Direction.DOWN);
        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 7, 6, 7).show(Direction.DOWN);
        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 7, 6, 8).show(Direction.DOWN);

        helper.idle(20);

        helper.setBlock(Blocks.STONE_BUTTON.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 6, 6, 7)
            .show(Direction.EAST);
        
        helper.idle(20);

        helper.hideSection(Direction.DOWN, plate);
        helper.showSection(Direction.UP, 0, 0, 0, 14, 3, 14);

        helper.idle(20);

         helper.showTextAt("Send redstone signal next to the %s to activate the bomb/missile",
            80, 7, 6.5, 7.5,
            getName(CrustyChunksModItems.ORDINANCE_CORE)
        ).attachKeyFrame();

        helper.idle(20);

        helper.toggleRedstone(6, 6, 7);

        helper.idle(5);

        helper.removeBlocks(5, 4, 5, 9, 5, 9);
        helper.createEntity(CrustyChunksModEntities.TORPEDO,
            "{Motion: [0.0d, 0.0d, -0.3d], Rotation: [180.0f, 0.0f], Pos: [7.5d, 5.5d, 7.5d]}"
        );

        helper.idle(15);

        helper.toggleRedstone(6, 6, 7);
    }
}
