package crazywoddman.warium_ponder;

import dev.latvian.mods.kubejs.client.KubeJSClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod(WariumPonder.MODID)
public class WariumPonder {
    public static final String MODID = "warium_ponder";

    public WariumPonder() {}

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(KubeJSClient::reloadClientScripts);
        }
    }
}