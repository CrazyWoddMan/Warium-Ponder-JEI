package crazywoddman.warium_ponder_jei.network;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent.LoggingOut;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

@EventBusSubscriber(modid = WariumPonderJei.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static boolean serverHasMod;
    protected static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        ResourceLocation.fromNamespaceAndPath(WariumPonderJei.MODID, "main"),
        () -> PROTOCOL_VERSION,
        version -> {
            if (compare(version)) {
                serverHasMod = true;
                return true;
            }
            return acceptAbsent(version);
        },
        version -> compare(version) || acceptAbsent(version)
    );

    private static boolean compare(String version) {
        return PROTOCOL_VERSION.equals(version);
    }

    private static boolean acceptAbsent(String version) {
        return NetworkRegistry.ABSENT.version().equals(version);
    }
    
    public static void register() {}

    @SubscribeEvent
    public static final void onLoggingOut(LoggingOut event) {
        serverHasMod = false;
    }
}