package crazywoddman.warium_ponder_jei.ponder.scenes;

import java.util.function.Supplier;
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
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.RegistryObject;

public class EngineScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return Stream.of(EngineType.values()).map(t -> t.item).toArray(RegistryObject[]::new);
    }

    @Override
    protected String getID(SceneHelper builder) {
        return "engines";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Small Engines";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        EngineType engine = EngineType.of(helper.getSubject());
        helper.setBlock(engine.getBlock(), 2, 1, 2).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_TANK, 0, 1, 2).show(Direction.DOWN);

        helper.idle(20);

        helper.showTextAt("Fuel Tank Connection Port is needed to provide the fuel", 80, 0.5, 1.5, 2.5)
            .attachKeyFrame();

        helper.idle(90);

        helper.showTextAt("Right click with %s to fill Fuel Tank with %s",
            100,
            0.5, 1.5, 2.5,
            getName(engine.getFuel().getBucket()),
            getName(engine.getFuel())
        ).attachKeyFrame();

        helper.showControls(engine.getFuel().getBucket(), Pointing.RIGHT, 100, 1, 1.5, 2)
            .rightClick();

        helper.idle(110);

        helper.showTextAt("Use Fuel Hose to connect Fuel Tank to the engine", 100, 0.5, 1.5, 2.5)
            .attachKeyFrame();

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
            "%s generates %d units of Kinetic Power",
            120,
            2.5, 1.5, 2,
            engine.getName(),
            switch (engine) {
                case MEDIUM_DIESEL -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getMDEpower() : 50;
                case SMALL_DIESEL -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getSDEpower() : 35;
                case MEDIUM_PETROL -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getMPEpower() : 60;
                case SMALL_PETROL -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getSPEpower() : 40;
                case LIGHT_TURBINE -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getLTEpower() : 65;
            }
        );
    }

    public enum EngineType {
        MEDIUM_DIESEL(CrustyChunksModItems.MEDIUM_DIESEL_ENGINE, CrustyChunksModFluids.DIESEL),
        SMALL_DIESEL(CrustyChunksModItems.SMALL_DIESEL_ENGINE, CrustyChunksModFluids.DIESEL),
        MEDIUM_PETROL(CrustyChunksModItems.MEDIUM_PETROL_ENGINE, CrustyChunksModFluids.PETROLIUM),
        SMALL_PETROL(CrustyChunksModItems.SMALL_PETROL_ENGINE, CrustyChunksModFluids.PETROLIUM),
        LIGHT_TURBINE(CrustyChunksModItems.LIGHT_TURBINE_ENGINE, CrustyChunksModFluids.KEROSENE);

        final Supplier<Item> item;
        private final Supplier<? extends Fluid> fuel;

        EngineType(Supplier<Item> item, Supplier<? extends Fluid> fuel) {
            this.item = item;
            this.fuel = fuel;
        }

        static EngineType of(Item item) {
            for (EngineType type : EngineType.values())
                if (type.item.get() == item)
                    return type;

            throw new IllegalStateException("Unknown Engine Type: " + AbstractScene.getName(item));
        }

        String getName() {
            return AbstractScene.getName(this.item);
        }

        Block getBlock() {
            return ((BlockItem)this.item.get()).getBlock();
        }

        Fluid getFuel() {
            return this.fuel.get();
        }
    }
}