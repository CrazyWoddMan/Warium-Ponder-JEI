package crazywoddman.warium_ponder_jei.events;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.config.WPJconfigScreen;
import crazywoddman.warium_ponder_jei.ponder.WariumPonder;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = WariumPonderJei.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class WPJclientEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        WariumPonder.register();
        WPJconfigScreen.register();
    }
}