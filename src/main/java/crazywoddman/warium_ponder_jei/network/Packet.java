package crazywoddman.warium_ponder_jei.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

interface Packet {
    void encode(FriendlyByteBuf buf);
    void handle(Supplier<NetworkEvent.Context> ctx);
}