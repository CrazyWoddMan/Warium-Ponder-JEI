package crazywoddman.warium_ponder_jei.utils;

import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

public class WPJclientUtil {

    public static Optional<Player> getLocalPlayer() {
        return Optional.ofNullable(Minecraft.getInstance().player);
    }
}
