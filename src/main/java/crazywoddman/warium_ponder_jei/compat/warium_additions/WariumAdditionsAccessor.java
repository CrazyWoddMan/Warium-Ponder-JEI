package crazywoddman.warium_ponder_jei.compat.warium_additions;

import crazywoddman.warium_additions.config.Config;

public class WariumAdditionsAccessor {
    public static int getMachinesMinKinetic() {
        return Config.SERVER.machinesMinPower.get();
    }

    public static int getHeatRequire() {
        return Config.SERVER.heatRequirement.get();
    }

    public static double getKineticToFe() {
        return Config.SERVER.kineticToFeRate.get();
    }
}
