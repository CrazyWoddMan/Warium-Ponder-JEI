package crazywoddman.warium_ponder_jei.data;

import net.mcreator.crustychunks.init.CrustyChunksModBlockEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntSupplier;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.data.recipes.*;
import crazywoddman.warium_ponder_jei.util.WariumAdditionsAccessor;

public class WariumpPonderJeiRecipes {
    public static final IntSupplier MACHINES_KINETIC_REQUIRE = () -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getMachinesMinKinetic() : 30;
    public static final IntSupplier HEAT_REQUIRE = () -> WariumPonderJei.WARIUM_ADDITIONS ? WariumAdditionsAccessor.getHeatRequire() : 200;
    private static final Map<ResourceLocation, Function<Level, List<? extends IDurationalRecipe<?>>>> PROGRESSABLE = new HashMap<>();
    
    public static Optional<IDurationalRecipe<?>> getProgressable(BlockEntity blockEntity, Level level, ItemStack stack) {
        return Optional.ofNullable(PROGRESSABLE.get(ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(blockEntity.getType()))).map(f -> {
            for (IDurationalRecipe<?> recipe : f.apply(level))
                if (recipe.test(stack))
                    return recipe;
                
            return null;
        });
    }
    private static class RecipeTypeRegister {
        private final DeferredRegister<RecipeType<?>> register;

        private RecipeTypeRegister(String modid) {
            this.register = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, modid);
        }

        private void register(IEventBus bus) {
            register.register(bus);
        }

        private <T extends Recipe<?>> RegistryObject<RecipeType<T>> register(String name) {
            return this.register.register(name, () -> new RecipeType<T>(){});
        }

        private <T extends AbstractRecipe & IDurationalRecipe<?>> RegistryObject<RecipeType<T>> register(String name, RegistryObject<BlockEntityType<?>> blockEntity) {
            RecipeType<T> type = new RecipeType<T>(){};
            PROGRESSABLE.put(blockEntity.getId(), level -> level.getRecipeManager().getAllRecipesFor(type));
            return this.register.register(name, () -> type);
        }
    }

    public static void register(IEventBus bus) {
        WARIUM_SERIALIZERS.register(bus);
        WARIUM_TYPES.register(bus);
        SERIALIZERS.register(bus);
        TYPES.register(bus);
    }

    private static final DeferredRegister<RecipeSerializer<?>> WARIUM_SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, "crusty_chunks");
    private static final RecipeTypeRegister WARIUM_TYPES = new RecipeTypeRegister("crusty_chunks");

    public static final RegistryObject<AssemblyRecipe.Serializer> ASSEMBLY_SERIALIZER =
        WARIUM_SERIALIZERS.register("assembly", AssemblyRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<AssemblyRecipe>> ASSEMBLY_TYPE = WARIUM_TYPES.register("assembly");

    private static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, WariumPonderJei.MODID);
    private static final RecipeTypeRegister TYPES = new RecipeTypeRegister(WariumPonderJei.MODID);

    public static final RegistryObject<BlastFurnaceRecipe.Serializer> BLAST_FURNACE_SERIALIZER =
        SERIALIZERS.register("alloy_furnace", BlastFurnaceRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<BlastFurnaceRecipe>> BLAST_FURNACE_TYPE = TYPES.register("alloy_furnace");

    public static final RegistryObject<FoundryRecipe.Serializer> FOUNDRY_SERIALIZER =
        SERIALIZERS.register("foundry", FoundryRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<FoundryRecipe>> FOUNDRY_TYPE = TYPES.register("foundry");
    
    public static final RegistryObject<MineralGrinderRecipe.Serializer> MINERAL_GRINDER_SERIALIZER =
        SERIALIZERS.register("mineral_grinder", MineralGrinderRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<MineralGrinderRecipe>> MINERAL_GRINDER_TYPE = TYPES.register("mineral_grinder");

    public static final RegistryObject<RefineryRecipe.Serializer> REFINERY_SERIALIZER =
        SERIALIZERS.register("refinery", RefineryRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<RefineryRecipe>> REFINERY_TYPE = TYPES.register("refinery");

    public static final RegistryObject<CrusherRecipe.Serializer> CRUSHER_SERIALIZER =
        SERIALIZERS.register("crusher", CrusherRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<CrusherRecipe>> CRUSHER_TYPE = TYPES.register("crusher");

    public static final RegistryObject<DigesterRecipe.Serializer> DIGESTER_SERIALIZER =
        SERIALIZERS.register("chemical_digester", DigesterRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<DigesterRecipe>> DIGESTER_TYPE = TYPES.register("chemical_digester");

    public static final RegistryObject<MechanicalFabricatorRecipe.Serializer> MECHANICAL_FABRICATOR_SERIALIZER =
        SERIALIZERS.register("mechanical_fabricator", MechanicalFabricatorRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<MechanicalFabricatorRecipe>> MECHANICAL_FABRICATOR_TYPE =
        TYPES.register("mechanical_fabricator", CrustyChunksModBlockEntities.ASSEMBLY_MECHANICAL_FABRICATOR);

    public static final RegistryObject<CircuitFabricatorRecipe.Serializer> CIRCUIT_FABRICATOR_SERIALIZER =
        SERIALIZERS.register("circuit_fabricator", CircuitFabricatorRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<CircuitFabricatorRecipe>> CIRCUIT_FABRICATOR_TYPE =
        TYPES.register("circuit_fabricator", CrustyChunksModBlockEntities.ASSEMBLY_CIRCUIT_FABRICATOR);

    public static final RegistryObject<CentrifugeRecipe.Serializer> CENTRIFUGE_SERIALIZER =
        SERIALIZERS.register("centrifuge", CentrifugeRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<CentrifugeRecipe>> CENTRIFUGE_TYPE =
        TYPES.register("centrifuge", CrustyChunksModBlockEntities.ASSEMBLY_CENTRIFUGE_MIDDLE);

    public static final RegistryObject<BreederReactorRecipe.Serializer> BREEDER_REACTOR_SERIALIZER =
        SERIALIZERS.register("breeder_reactor", BreederReactorRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<BreederReactorRecipe>> BREEDER_REACTOR_TYPE =
        TYPES.register("breeder_reactor", CrustyChunksModBlockEntities.BREEDER_REACTOR_INTERFACE);
}
