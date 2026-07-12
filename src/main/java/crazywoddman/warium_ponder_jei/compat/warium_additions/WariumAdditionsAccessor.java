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

    public static int getLEpower() {
        return Config.SERVER.largeEnginePower.get();
    }

    public static int getMDEpower() {
        return Config.SERVER.mediumDieselEnginePower.get();
    }

    public static int getSDEpower() {
        return Config.SERVER.smallDieselEnginePower.get();
    }

    public static int getMPEpower() {
        return Config.SERVER.mediumPetrolEnginePower.get();
    }

    public static int getSPEpower() {
        return Config.SERVER.smallPetrolEnginePower.get();
    }

    public static int getTurbinePower() {
        return Config.SERVER.jetTurbinePower.get();
    }

    public static int getLTEpower() {
        return 65;
    }
}
