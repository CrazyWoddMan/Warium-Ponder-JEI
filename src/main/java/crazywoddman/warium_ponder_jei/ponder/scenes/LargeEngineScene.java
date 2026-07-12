package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.warium_additions.WariumAdditionsAccessor;
import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModFluids;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.mcreator.crustychunks.init.CrustyChunksModParticleTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class LargeEngineScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{CrustyChunksModItems.DRIVE_SHAFT, CrustyChunksModItems.ENGINE_CYLLINDER, CrustyChunksModItems.LARGE_ENGINE_SMOKESTACK};
    }

    @Override
    protected int getSize() {
        return 9;
    }

    @Override
    protected String getID(SceneHelper builder) {
        return "large_engine";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Large Engine";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.builder.scaleSceneView(0.9f);
        helper.builder.setSceneOffsetY(-1);
        helper.setBlock(CrustyChunksModBlocks.DRIVE_SHAFT, 4, 1, 4).show(Direction.DOWN);
        
        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ENGINE_CYLLINDER, 4, 2, 4).show(Direction.DOWN);
        
        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.LARGE_ENGINE_SMOKESTACK, 4, 3, 4).show(Direction.DOWN);

        helper.idle(20);

        helper.setBlock(CrustyChunksModBlocks.FUEL_TANK, 1, 1, 4).show(Direction.DOWN);

        helper.idle(20);

        helper.showTextAt("%s is needed to provide the fuel",
            80, 1.5, 1.5, 4.5,
            getName(CrustyChunksModItems.FUEL_TANK)
        ).attachKeyFrame();

        helper.idle(90);

        helper.showTextAt("Right click with %s to fill fuel tank with %s",
            100, 1.5, 1.5, 4.5,
            getName(CrustyChunksModItems.DIESEL_BUCKET),
            getName(CrustyChunksModFluids.DIESEL.get())
        ).attachKeyFrame();

        helper.showControls(CrustyChunksModItems.DIESEL_BUCKET, Pointing.RIGHT, 100, 2, 1.5, 4)
            .rightClick();

        helper.idle(110);

        helper.showTextAt("Use %s to connect fuel tank to the engine",
            100, 1.5, 1.5, 4.5,
            getName(CrustyChunksModItems.FUEL_HOSE)
        ).attachKeyFrame();

        helper.idle(20);

        helper.showControls(CrustyChunksModItems.FUEL_HOSE, Pointing.RIGHT, 30, 5, 2.5, 4)
            .rightClick();

        helper.idle(40);

        helper.showControls(CrustyChunksModItems.FUEL_HOSE, Pointing.DOWN, 30, 1.8, 1.5, 5)
            .rightClick();
        helper.showLine(PonderPalette.BLUE, 60, 1.5, 1.5, 4.5, 4.5, 2.5, 4.5);

        helper.idle(65);

        helper.setBlock(Blocks.LEVER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 3, 2, 4)
            .show(Direction.EAST);

        helper.idle(20);

        helper.toggleRedstone(3, 2, 4);

        helper.idle(20);

        helper.emitParticles(CrustyChunksModParticleTypes.SMOKE, 4.5, 4.5, 4.5, 0, 0, 0);

        helper.idle(20);

        helper.showTextAt(
            "One Large Engine section generates %d units of Kinetic Power",
            120,
            4.5, 1.5, 4,
            WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getLEpower() : 50
        ).attachKeyFrame();

        helper.idle(130);

        helper.showTextAt("However...", 30, 4.5, 2.5, 4.5).attachKeyFrame();

        helper.idle(40);

        for (int i = 1; i <= 3; i++) {
            helper.moveSection(0, 0, -i, helper.showSection(Direction.SOUTH, 4, 1, 4, 4, 3, 4));
            helper.moveSection(0, 0, i, helper.showSection(Direction.NORTH, 4, 1, 4, 4, 3, 4));
            helper.idle(5);
        }

        helper.moveSection(0, 0, 4, helper.showSection(Direction.NORTH, 4, 1, 4, 4, 3, 4));

        helper.idle(20);

        helper.showTextAt("Up to 8 sections can be stacked to to increase the power", 80, 4.5, 2.5, 4.5)
            .attachKeyFrame();

        helper.idle(90);

        for (int i = 4; i >= -3; i--) {
            if (i == 0) continue;
            helper.moveSection(0, 0, i, helper.showSection(Direction.EAST, 3, 2, 4));
            helper.idle(3);
        }

        for (int i = 4; i >= -3; i--) {
            if (i == 0) continue;
            helper.moveSection(0, 0, i, helper.showSection(Direction.DOWN, 1, 1, 4));
            helper.idle(3);
        }

        helper.idle(17);

        helper.showTextAt("Each section requires individual redstone signal and %s",
            80, 3.8, 1.5, 1.5,
            getName(CrustyChunksModItems.FUEL_TANK)
        );
    }
}