package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class FuelTankScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{
            CrustyChunksModItems.FUEL_TANK,
            CrustyChunksModItems.FUEL_TANK_MODULE,
            CrustyChunksModItems.FUEL_TANK_INPUT
        };
    }

    @Override
    protected String getID(SceneHelper helper) {
        return "fuel_tanks";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Fuel Tanks";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.setBlock(CrustyChunksModBlocks.FUEL_TANK, 3, 1, 2).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_TANK_MODULE, 2, 1, 2).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_TANK_INPUT, 1, 1, 2).show(Direction.DOWN);

        helper.idle(20);

        helper.showTextAt("Fuel tanks share fuel when placed next to each other", 80, 2.5, 1.5, 2.5)
            .attachKeyFrame();

        helper.idle(5);

        helper.showOutline(PonderPalette.BLUE, 75, 3, 1, 2, 1, 1, 2);

        helper.idle(95);

        helper.showTextAt("%s transfers all it's fuel to surrounding tanks",
            80, 1.5, 2, 2.5,
            getName(CrustyChunksModItems.FUEL_TANK_INPUT)
        ).attachKeyFrame();

        helper.idle(100);

        helper.showTextAt("%s evenly distributes fuel between other tanks (except %s)",
            80, 2.5, 2, 2.5,
            getName(CrustyChunksModItems.FUEL_TANK_MODULE),
            getName(CrustyChunksModItems.FUEL_TANK_INPUT)
        ).attachKeyFrame();

        helper.idle(100);

        helper.showTextAt("%s transfers fuel to a connected consumer",
            120, 3.5, 2, 2.5,
            getName(CrustyChunksModItems.FUEL_TANK_MODULE),
            getName(CrustyChunksModItems.FUEL_TANK_INPUT)
        ).attachKeyFrame();

        helper.idle(20);

        helper.setBlock(CrustyChunksModBlocks.MEDIUM_DIESEL_ENGINE, 4, 1, 0).show(Direction.DOWN);

        helper.idle(20);

        helper.showControls(CrustyChunksModItems.FUEL_HOSE, Pointing.DOWN, 20, 4.5, 2, 0.5);

        helper.idle(35);

        helper.showControls(CrustyChunksModItems.FUEL_HOSE, Pointing.DOWN, 20, 3.5, 2, 2.5);
        helper.showLine(PonderPalette.BLUE, 45, 4.5, 1.5, 0.5, 3.5, 1.5, 2.5);
    }
}
