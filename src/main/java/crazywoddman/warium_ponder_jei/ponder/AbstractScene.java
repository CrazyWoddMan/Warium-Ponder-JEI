package crazywoddman.warium_ponder_jei.ponder;

import java.util.function.Supplier;

import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.RegistryObject;

public abstract class AbstractScene {

    protected int getSize() {
        return 5;
    }

    protected String getID(SceneHelper helper) {
        return helper.builder.getScene().getLocation().getPath();
    }

    protected boolean showBasePlate() {
        return true;
    }

    protected abstract String getName(SceneHelper helper);

    protected abstract RegistryObject<Item>[] getItems();

    public String getSchematic() {
        return getSize() + "x" + getSize();
    }

    public void showScene(SceneBuilder builder, SceneBuildingUtil util) {
        SceneHelper helper = new SceneHelper(builder, util);
        builder.title(getID(helper), getName(helper));
        builder.configureBasePlate(0, 0, getSize());
        
        if (showBasePlate()) {
            builder.showBasePlate();
            helper.idle(10);
        }
        
        showScene(helper);
    }

    protected abstract void showScene(SceneHelper helper);

    protected static String getName(Item item) {
        return item.getDescription().getString();
    }

    protected static String getName(Supplier<Item> item) {
        return getName(item.get());
    }

    protected static String getName(Fluid fluid) {
        return fluid.getFluidType().getDescription().getString();
    }
}
