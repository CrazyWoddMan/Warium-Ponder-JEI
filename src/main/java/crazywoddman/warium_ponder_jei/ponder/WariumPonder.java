package crazywoddman.warium_ponder_jei.ponder;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.ponder.scenes.*;
import crazywoddman.warium_ponder_jei.ponder.scenes.BombScene.BombType;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class WariumPonder implements PonderPlugin {

    public static void register() {
        PonderIndex.addPlugin(new WariumPonder());
    }

    @Override
    public String getModId() {
        return WariumPonderJei.MODID;
    }

    @Override
    public final void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        register(helper,
            new MortarScene(),
            new LargeEngineScene(),
            new HardpointScene(),
            new EngineScene(),
            new ReactorScene(true),
            new ReactorScene(false),
            new AssemblyScene(),
            new CentrifugeScene(),
            new JetTurbineScene(),
            new FuelTankScene(),
            new EnergyScene(),
            new WarheadScene(),
            new TorpedoScene(),
            new SeekerScene(),
            new BombScene(BombType.KINETIC),
            new BombScene(BombType.FISSION),
            new BombScene(BombType.FUSION),
            new MachineGunScene(),
            new CannonScene()
        );
    }

    private static void register(PonderSceneRegistrationHelper<ResourceLocation> register, AbstractScene... scenes) {
        PonderSceneRegistrationHelper<RegistryObject<Item>> helper = register.withKeyFunction(RegistryObject::getId);
        for (AbstractScene scene : scenes)
            helper.forComponents(scene.getItems()).addStoryBoard(scene.getSchematic(), scene::showScene);
    }
}
