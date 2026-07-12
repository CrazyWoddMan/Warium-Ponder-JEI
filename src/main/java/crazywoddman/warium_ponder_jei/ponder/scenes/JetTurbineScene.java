package crazywoddman.warium_ponder_jei.ponder.scenes;

import java.util.stream.Stream;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.warium_additions.WariumAdditionsAccessor;
import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModFluids;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class JetTurbineScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{
            CrustyChunksModItems.JET_TURBINE,
            CrustyChunksModItems.JET_COMPRESSOR,
            CrustyChunksModItems.JET_GEARBOX,
            CrustyChunksModItems.JET_EXHAUST,
            CrustyChunksModItems.AFTER_BURNER
        };
    }

    @Override
    protected String getID(SceneHelper builder) {
        return "turbine";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Jet Turbine";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.setBlock(CrustyChunksModBlocks.JET_COMPRESSOR.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), 2, 1, 3)
            .show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.JET_TURBINE.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), 2, 1, 2)
            .show(Direction.SOUTH);

        helper.idle(5);

        var exhaust = helper.setBlock(CrustyChunksModBlocks.JET_GEARBOX, 2, 1, 1)
            .show(Direction.SOUTH);

        helper.idle(10);

        helper.setBlock(CrustyChunksModBlocks.FUEL_TANK, 0, 1, 2).show(Direction.DOWN);

        helper.idle(20);

        helper.showTextAt("%s is needed to provide the fuel",
            80, 0.5, 1.5, 2.5,
            getName(CrustyChunksModItems.FUEL_TANK)
        ).attachKeyFrame();

        helper.idle(90);

        helper.showTextAt("Right click with %s to fill fuel tank with %s",
            100,
            0.5, 1.5, 2.5,
            getName(CrustyChunksModItems.KEROSENE_BUCKET),
            getName(CrustyChunksModFluids.KEROSENE.get())
        ).attachKeyFrame();

        helper.showControls(CrustyChunksModItems.KEROSENE_BUCKET, Pointing.RIGHT, 100, 1, 1.5, 2)
            .rightClick();

        helper.idle(110);

        helper.showTextAt("Use %s to connect fuel tank to the engine",
            100, 0.5, 1.5, 2.5,
            getName(CrustyChunksModItems.FUEL_HOSE)
        ).attachKeyFrame();

        helper.idle(20);

        helper.showControls(CrustyChunksModItems.FUEL_HOSE, Pointing.DOWN, 30, 0.5, 2, 2.5)
            .rightClick();

        helper.idle(40);

        helper.showControls(CrustyChunksModItems.FUEL_HOSE, Pointing.DOWN, 30, 2.5, 2, 2.5)
            .rightClick();

        helper.showLine(PonderPalette.BLUE, 60, 0.5, 1.5, 2.5, 2.5, 1.5, 2.5);

        helper.idle(65);

        helper.setBlock(Blocks.LEVER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 1, 1, 2)
            .show(Direction.EAST);

        helper.idle(20);

        helper.toggleRedstone(1, 1, 2);

        helper.idle(20);

        helper.showTextAt(
            "Jet Turbine generates %d units of Kinetic Power",
            80,
            2.5, 1.5, 1,
            WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getTurbinePower() : 51
        );

        if (!WariumPonderJei.WARIUM_VS)
            return;

        helper.builder.addKeyframe();
        
        helper.idle(100);

        Item[] exhausts = Stream
            .of("vtol_exhaust", "jet_exhaust")
            .map(s -> ForgeRegistries.ITEMS.getValue(ResourceLocation.fromNamespaceAndPath("valkyrien_warium", s)))
            .toArray(Item[]::new);

        helper.showTextAt(
            "You can replace %s with %s or %s to give your VS ship some thrust",
            100,
            2.5, 1.5, 1.5,
            getName(CrustyChunksModItems.JET_GEARBOX),
            getName(exhausts[0]),
            getName(exhausts[1])
        ).attachKeyFrame();

        helper.idle(30);

        for(Item item : exhausts) {
            helper.hideSection(Direction.EAST, exhaust);

            helper.idle(15);

            exhaust = helper.setBlock(
                ((BlockItem)item).getBlock(),
                2, 1, 1
            ).show(Direction.EAST);

            helper.idle(30);
        }

        helper.setBlock(CrustyChunksModBlocks.AFTER_BURNER, 2, 1, 0).show(Direction.SOUTH);

        helper.idle(15);

        helper.showTextAt(
            "Place %s to give %s additional thrust",
            100,
            2.5, 1.5, 0,
            getName(CrustyChunksModItems.AFTER_BURNER), getName(exhausts[1])
        );
    }
}