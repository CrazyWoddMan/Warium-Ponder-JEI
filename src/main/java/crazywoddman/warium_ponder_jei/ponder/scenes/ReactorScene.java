package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class ReactorScene extends AbstractScene {
    private final boolean isBreeder;

    public ReactorScene(boolean isBreeder) {
        super();
        this.isBreeder = isBreeder;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected RegistryObject<Item>[] getItems() {
        return this.isBreeder
        ? new RegistryObject[]{
            CrustyChunksModItems.BREEDER_REACTOR_INTERFACE,
            CrustyChunksModItems.BREEDER_REACTOR_PORT,
            CrustyChunksModItems.BREEDER_REACTOR_CORE,
            CrustyChunksModItems.REACTION_CHAMBER,
            CrustyChunksModItems.CONTROL_ROD,
            CrustyChunksModItems.REACTOR_CASING
        }
        : new RegistryObject[]{
            CrustyChunksModItems.POWER_REACTOR_INTERFACE,
            CrustyChunksModItems.POWER_REACTOR_PORT,
            CrustyChunksModItems.BREEDER_REACTOR_CORE,
            CrustyChunksModItems.REACTION_CHAMBER,
            CrustyChunksModItems.CONTROL_ROD,
            CrustyChunksModItems.REACTOR_CASING
        };
    }

    @Override
    protected int getSize() {
        return 7;
    }

    @Override
    public String getSchematic() {
        return "reactor";
    }

    @Override
    protected String getID(SceneHelper builder) {
        return (this.isBreeder ? "breeder" : "power") + getSchematic();
    }

    @Override
    protected String getName(SceneHelper helper) {
        return (this.isBreeder ? "Breeder" : "Power") + " Reactor";
    }

    @Override
    protected void showScene(SceneHelper helper) {
		helper.setBlock(isBreeder ? CrustyChunksModBlocks.BREEDER_REACTOR_PORT : CrustyChunksModBlocks.POWER_REACTOR_PORT, 3, 1, 3)
            .show(Direction.DOWN);

		helper.idle(3);

		helper.showSection(Direction.DOWN, 3, 2, 3);

		helper.idle(3);

        helper.setBlock(isBreeder ? CrustyChunksModBlocks.BREEDER_REACTOR_INTERFACE : CrustyChunksModBlocks.POWER_REACTOR_INTERFACE, 3, 3, 3)
            .show(Direction.DOWN);

		helper.idle(15);

		helper.showSection(Direction.SOUTH, 3, 2, 2, 3, 3, 2);

		helper.idle(15);

		helper.showSection(Direction.EAST, 2, 2, 2, 2, 3, 2);
		helper.showSection(Direction.WEST, 4, 2, 2, 4, 3, 2);

		helper.idle(20);

		helper.showSection(Direction.SOUTH, 3, 2, 1, 3, 3, 1);

		helper.idle(15);

		helper.showSection(Direction.EAST, 2, 2, 1, 2, 3, 1);
		helper.showSection(Direction.WEST, 4, 2, 1, 4, 3, 1);

		helper.idle(20);

		helper.showSection(Direction.SOUTH, 2, 2, 0, 4, 3, 0);

		helper.idle(20);

		helper.builder.rotateCameraY(-35);

		helper.idle(20);

		var part = helper.showSection(Direction.NORTH, 2, 2, 4, 4, 3, 6);

		helper.idle(20);

		helper.showSection(Direction.WEST, 5, 2, 2, 6, 3, 4);
        helper.showSection(Direction.WEST, 4, 2, 3, 4, 3, 3);

		helper.idle(20);

		helper.showSection(Direction.EAST, 0, 2, 2, 1, 3, 4);
        helper.showSection(Direction.EAST, 2, 2, 3, 2, 3, 3);

		helper.idle(20);

		helper.showSection(Direction.DOWN, 1, 4, 3);
        helper.showSection(Direction.DOWN, 3, 4, 1);
        helper.showSection(Direction.DOWN, 5, 4, 3);
        var rods = helper.showSection(Direction.DOWN, 3, 4, 5);

        helper.idle(20);

        helper.showTextAt("Each Fuel Rods section must contain at least 1 Uranium Fuel Rod for the reactor to work", 90, 1.5, 4, 3.5)
            .attachKeyFrame();
        
        helper.idle(10);

        helper.showControls(CrustyChunksModItems.FUEL_ROD, Pointing.DOWN, 10, 1.5, 4, 3.5);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_RODS_1, 1, 4, 3);

        helper.idle(15);

        helper.showControls(CrustyChunksModItems.FUEL_ROD, Pointing.DOWN, 10, 3.5, 4, 1.5);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_RODS_1, 3, 4, 1);

        helper.idle(15);

        helper.showControls(CrustyChunksModItems.FUEL_ROD, Pointing.DOWN, 10, 5.5, 4, 3.5);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_RODS_1, 5, 4, 3);

        helper.idle(15);

        helper.showControls(CrustyChunksModItems.FUEL_ROD, Pointing.DOWN, 10, 3.5, 4, 5.5);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FUEL_RODS_1, 3, 4, 5);

        helper.idle(30);

        if (isBreeder) {
            helper.showTextAt("Material to enrich should be put into %s",
                60, 3.5, 4, 3.5,
                getName(CrustyChunksModItems.BREEDER_REACTOR_INTERFACE)
            ).attachKeyFrame();
            helper.idle(70);
        }

        helper.builder.rotateCameraY(-70);

        helper.idle(20);

        helper.hideSection(Direction.SOUTH, part, rods);

        helper.idle(20);

        helper.showTextAt(isBreeder ? "Enriched material will be outputted into %s" : "Produced energy will be accumulated in %s",
            100, 3.5, 1.5, 4,
            getName(isBreeder ? CrustyChunksModItems.BREEDER_REACTOR_PORT : CrustyChunksModItems.POWER_REACTOR_PORT)
        );
    }
}
