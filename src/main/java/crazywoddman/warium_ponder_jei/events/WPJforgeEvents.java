package crazywoddman.warium_ponder_jei.events;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.data.AssemblySounds;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = WariumPonderJei.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class WPJforgeEvents {

    @SubscribeEvent
    public static void onRegisterReloadListeners(AddReloadListenerEvent event) {
        AssemblySounds.register(event);
    }
}