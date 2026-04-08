package crazywoddman.warium_ponder_jei;

import crazywoddman.warium_ponder_jei.data.AssemblySounds;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.network.NetworkHandler;
import crazywoddman.warium_ponder_jei.ponder.WariumPonder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WariumPonderJei.MODID)
public class WariumPonderJei {
    public static final String MODID = "warium_ponder_jei";

    private static final ModList MODLIST = ModList.get();

    public static final boolean WARIUM_ADDITIONS = MODLIST.isLoaded("warium_additions");
    public static final boolean CREATE = MODLIST.isLoaded("create");

    public WariumPonderJei(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        WariumpPonderJeiRecipes.register(bus);
        NetworkHandler.register();
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            WariumPonder.register();
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
        @SubscribeEvent
        public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
            AssemblySounds.register(event);
        }
    }
}