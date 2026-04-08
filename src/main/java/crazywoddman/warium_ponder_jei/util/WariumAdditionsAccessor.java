package crazywoddman.warium_ponder_jei.util;

import crazywoddman.warium_additions.config.Config;

public class WariumAdditionsAccessor {
    public static int getMachinesMinKinetic() {
        return Config.SERVER.machinesMinPower.get();
    }

    public static int getHeatRequire() {
        return Config.SERVER.heatRequirement.get();
    }
}
