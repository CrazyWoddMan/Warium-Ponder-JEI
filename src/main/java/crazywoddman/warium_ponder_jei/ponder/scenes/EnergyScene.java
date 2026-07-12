package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.warium_additions.WariumAdditionsAccessor;
import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class EnergyScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{
            CrustyChunksModItems.GENERATOR,
            CrustyChunksModItems.LARGE_ELECTRIC_MOTOR,
            CrustyChunksModItems.ENERGY_NODE
            
        };
    }

    @Override
    protected String getID(SceneHelper helper) {
        return "energy";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Energy";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.setBlock(CrustyChunksModBlocks.GENERATOR, 2, 1, 2).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.MEDIUM_DIESEL_ENGINE, 2, 1, 3).show(Direction.NORTH);

        helper.idle(15);

        helper.setBlock(Blocks.LEVER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 1, 1, 3)
            .show(Direction.EAST);

        helper.idle(20);

        helper.toggleRedstone(1, 1, 3);

        helper.idle(20);

        helper.showTextAt(
            "%s converts Kinetic Power into Forge Energy",
            60, 2.5, 1.5, 2,
            getName(CrustyChunksModItems.GENERATOR)
        ).attachKeyFrame();

        helper.idle(75);

        helper.showTextAt("The ratio is 1.0 Kinetic Power to %f Forge Energy",
            60, 2.5, 1.5, 2,
            WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getKineticToFe() : 1
        ).attachKeyFrame();

        helper.idle(80);

        var motor = helper.setBlock(CrustyChunksModBlocks.LARGE_ELECTRIC_MOTOR.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.EAST), 4, 1, 0)
            .show(Direction.DOWN);

        helper.idle(20);

        helper.setBlock(CrustyChunksModBlocks.ENERGY_NODE.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.WEST), 3, 1, 0)
            .show(Direction.EAST);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ENERGY_NODE, 2, 1, 1).show(Direction.SOUTH);

        helper.idle(20);

        helper.showControls(CrustyChunksModItems.CABLE, Pointing.DOWN, 20, 2.5, 1.5, 1.5);

        helper.idle(30);

        helper.showControls(CrustyChunksModItems.CABLE, Pointing.DOWN, 20, 3.5, 1.5, 0.5);
        helper.showLine(PonderPalette.BLACK, 1000, 2.5, 1.5, 1.5, 3.5, 1.5, 0.5);

        helper.idle(30);

        helper.showTextAt("You can then convert energy back to Kinetic Power using %s",
            80, 4.5, 2, 0.5,
            getName(CrustyChunksModItems.LARGE_ELECTRIC_MOTOR)
        ).attachKeyFrame();

        helper.idle(100);

        helper.hideSection(Direction.SOUTH, motor);

        helper.idle(15);

        helper.setBlock(CrustyChunksModBlocks.ENERGY_BATTERY, 4, 1, 0).show(Direction.SOUTH);

        helper.idle(20);

        helper.showTextAt("Or store it in %s",
            100, 4.5, 2, 0.5,
            getName(CrustyChunksModItems.ENERGY_BATTERY)
        );
    }
}
