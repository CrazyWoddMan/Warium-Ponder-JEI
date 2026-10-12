package crazywoddman.warium_ponder_jei.network;

import java.util.function.Function;

import crazywoddman.warium_ponder_jei.utils.WPJutil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static int packetId = 0;

    protected static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        WPJutil.location("main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );
    
    public static void register() {
        register(ConfigSyncPacket.class, ConfigSyncPacket::decode, NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T extends Packet> void register(Class<T> clazz, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction) {
        CHANNEL.messageBuilder(clazz, packetId++, direction)
            .encoder(Packet::encode)
            .decoder(decoder)
            .consumerMainThread((packet, context) -> packet.handle(context))
            .add();
    }
}