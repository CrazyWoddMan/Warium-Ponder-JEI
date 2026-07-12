package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class CentrifugeScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_TOP,
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_MIDDLE,
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_BOTTOM,
            CrustyChunksModItems.GIANT_COIL
        };
    }

    @Override
    protected String getID(SceneHelper helper) {
        return "centrifuge";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Assembly Centrifuge";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.setBlock(CrustyChunksModBlocks.GIANT_COIL, 2, 1, 2).show(Direction.DOWN);

        helper.idle(3);

        helper.setBlock(CrustyChunksModBlocks.ASSEMBLY_CENTRIFUGE_BOTTOM.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2, 2, 2)
            .show(Direction.DOWN);

        helper.idle(3);

        helper.setBlock(CrustyChunksModBlocks.ASSEMBLY_CENTRIFUGE_MIDDLE, 2, 3, 2).show(Direction.DOWN);

        helper.idle(3);

        helper.setBlock(CrustyChunksModBlocks.ASSEMBLY_CENTRIFUGE_TOP, 2, 4, 2).show(Direction.DOWN);

        helper.idle(10);

        helper.setBlock(CrustyChunksModBlocks.PRODUCTION_OUTPUT, 2, 3, 1).show(Direction.SOUTH);
        helper.setBlock(CrustyChunksModBlocks.PRODUCTION_INPUT.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), 2, 3, 3)
            .show(Direction.NORTH);

        helper.idle(20);

        helper.showTextAt("Centrifuge requires %d untis of Kinetic Power to work",
            30 + EngineScene.EngineType.values().length * 25,
            2, 2.5, 2.5,
            WariumpPonderJeiRecipes.MACHINES_KINETIC_REQUIRE.getAsInt()
        ).attachKeyFrame();

        helper.idle(30);

        for (var engine : EngineScene.EngineType.values()) {
            var link = helper.setBlock(engine.getBlock().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 1, 2, 2)
                .show(Direction.SOUTH);

            helper.idle(10);

            helper.hideSection(Direction.SOUTH, link);

            helper.idle(15);
        }

        helper.idle(5);

        var crank = helper.setBlock(CrustyChunksModBlocks.MANUAL_CRANK.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 1, 2, 2)
            .show(Direction.EAST);

        helper.idle(10);

        helper.showTextAt("Note that %s doesn't generate enough Kinetic Power",
            60, 2, 2.5, 2.5,
            getName(CrustyChunksModItems.MANUAL_CRANK)
        ).attachKeyFrame();

        helper.idle(60);

        helper.hideSection(Direction.WEST, crank);

        helper.idle(20);

        helper.showTextAt("Put item to process into %s",
            60, 2.5, 3.5, 4,
            getName(CrustyChunksModItems.PRODUCTION_INPUT)
        ).attachKeyFrame();

        helper.idle(80);

        helper.showTextAt("Processed item will be outputted into %s",
            100, 2.5, 3.5, 1.5,
            getName(CrustyChunksModItems.PRODUCTION_OUTPUT)
        );
    }
}