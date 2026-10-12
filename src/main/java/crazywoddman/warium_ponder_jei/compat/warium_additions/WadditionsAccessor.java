package crazywoddman.warium_ponder_jei.compat.warium_additions;

import crazywoddman.warium_additions.config.WadditionsConfig;

public class WadditionsAccessor {

    public static int getLEpower() {
        return WadditionsConfig.LARGE_ENGINE_POWER.get();
    }

    public static int getMDEpower() {
        return WadditionsConfig.MEDIUM_DIESEL_ENGINE_POWER.get();
    }

    public static int getSDEpower() {
        return WadditionsConfig.SMALL_DIESEL_ENGINE_POWER.get();
    }

    public static int getMPEpower() {
        return WadditionsConfig.MEDIUM_PETROL_ENGINE_POWER.get();
    }

    public static int getSPEpower() {
        return WadditionsConfig.SMALL_PETROL_ENGINE_POWER.get();
    }

    public static int getTurbinePower() {
        return WadditionsConfig.JET_TURBINE_POWER.get();
    }

    public static int getLTEpower() {
        return WadditionsConfig.LIGHT_TURBINE_ENGINE_POWER.get();
    }
}
