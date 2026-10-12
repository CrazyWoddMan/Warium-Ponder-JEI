package crazywoddman.warium_ponder_jei;

import crazywoddman.warium_ponder_jei.config.WPJconfig;
import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import crazywoddman.warium_ponder_jei.network.NetworkHandler;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(WariumPonderJei.MODID)
public class WariumPonderJei {
    public static final String MODID = "warium_ponder_jei";
    public static final boolean
    WARIUM_ADDITIONS = ModList.get().isLoaded("warium_additions"),
    CREATE = ModList.get().isLoaded("create"),
    WARIUM_VS = ModList.get().isLoaded("valkyrien_warium");

    public WariumPonderJei(FMLJavaModLoadingContext context) {
        IEventBus bus = context.getModEventBus();
        WPJrecipes.register(bus);
        NetworkHandler.register();
        WPJconfig.register(context);
    }
}