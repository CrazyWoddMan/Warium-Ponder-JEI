package crazywoddman.warium_ponder_jei;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import crazywoddman.warium_ponder_jei.data.AssemblySounds;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.network.NetworkHandler;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WariumPonderJei.MODID)
public class WariumPonderJei {
    public static final String MODID = "warium_ponder_jei";
    public static final boolean WARIUM_ADDITIONS, CREATE;

    static {
        ModList modlist = ModList.get();
        WARIUM_ADDITIONS = modlist
            .getModContainerById("warium_additions")
            .map(container -> container.getModInfo().getVersion().compareTo(new DefaultArtifactVersion("1.1.1")) >= 0)
            .orElse(false);
        CREATE = modlist.isLoaded("create");
    }

    public WariumPonderJei(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        WariumpPonderJeiRecipes.register(bus);
        NetworkHandler.register();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
            AssemblySounds.register(event);
        }
    }
}