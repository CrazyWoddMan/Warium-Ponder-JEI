package crazywoddman.warium_ponder_jei.config;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class WPJconfig {
    public static final List<ConfigValueHolder> VALUES = new ArrayList<>();
    public static final ForgeConfigSpec SPEC;

    public static final IntValue MACHINES_POWER;
    public static final IntValue MIN_HEAT;

    static {
        Builder builder = new Builder();

        MACHINES_POWER = builder
        .comment("Minimal KineticPower required for assembly machines to function")
        .defineInRange("machinesMinPower", 30, 0, Integer.MAX_VALUE);
        MIN_HEAT = builder
        .comment("Heat amount needed for resource processors that require heat to function")
        .defineInRange("heatRequirement", 200, 0, 1000);
        
        SPEC = builder.build();
    }

    public static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.SERVER, SPEC);
    }

    private static class Builder extends ForgeConfigSpec.Builder {

        @Override
        public <T> ConfigValue<T> define(List<String> path, Supplier<T> defaultSupplier, Predicate<Object> validator, Class<?> clazz) {
            ConfigValue<T> value = super.define(path, defaultSupplier, validator, clazz);
            VALUES.add(new ConfigValueHolder(value, validator));
            return value;
        }
    }
}
