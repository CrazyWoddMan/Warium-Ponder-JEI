package crazywoddman.warium_ponder_jei.data;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.data.recipes.*;

public class WPJrecipes {
    private static final RecipeRegister WARIUM_RECIPES = new RecipeRegister("crusty_chunks");
    public static final RecipeRegistry<AssemblyRecipe> ASSEMBLY = WARIUM_RECIPES.register("assembly", AssemblyRecipe.Serializer::new);

    private static final RecipeRegister RECIPES = new RecipeRegister(WariumPonderJei.MODID);
    public static final RecipeRegistry<BlastFurnaceRecipe> BLAST_FURNACE = RECIPES.register("alloy_furnace", BlastFurnaceRecipe.Serializer::new);
    public static final RecipeRegistry<FoundryRecipe> FOUNDRY = RECIPES.register("foundry", FoundryRecipe.Serializer::new);
    public static final RecipeRegistry<MineralGrinderRecipe> MINERAL_GRINDER = RECIPES.register("mineral_grinder", MineralGrinderRecipe.Serializer::new);
    public static final RecipeRegistry<RefineryRecipe> REFINERY = RECIPES.register("refinery", RefineryRecipe.Serializer::new);
    public static final RecipeRegistry<CrusherRecipe> CRUSHER = RECIPES.register("crusher", CrusherRecipe.Serializer::new);
    public static final RecipeRegistry<DigesterRecipe> DIGESTER = RECIPES.register("chemical_digester", DigesterRecipe.Serializer::new);
    public static final RecipeRegistry<MechanicalFabricatorRecipe> MECHANICAL_FABRICATOR = RECIPES.register("mechanical_fabricator", MechanicalFabricatorRecipe.Serializer::new);
    public static final RecipeRegistry<CircuitFabricatorRecipe> CIRCUIT_FABRICATOR = RECIPES.register("circuit_fabricator", CircuitFabricatorRecipe.Serializer::new);
    public static final RecipeRegistry<CentrifugeRecipe> CENTRIFUGE = RECIPES.register("centrifuge", CentrifugeRecipe.Serializer::new);
    public static final RecipeRegistry<BreederReactorRecipe> BREEDER_REACTOR = RECIPES.register("breeder_reactor", BreederReactorRecipe.Serializer::new);
    public static final RecipeRegistry<PaintToolRecipe> PAINT_TOOL = RECIPES.register("paint_tool", PaintToolRecipe.Serializer::new);

    public static void register(IEventBus bus) {
        WARIUM_RECIPES.register(bus);
        RECIPES.register(bus);
    }

    private static class RecipeRegister {
        final DeferredRegister<RecipeType<?>> types;
        final DeferredRegister<RecipeSerializer<?>> serializers;

        RecipeRegister(String namespace) {
            this.types = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, namespace);
            this.serializers = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, namespace);
        }

        private void register(IEventBus bus) {
            this.types.register(bus);
            this.serializers.register(bus);
        }

        private <T extends Recipe<Container>> RecipeRegistry<T> register(String id, Supplier<? extends RecipeSerializer<T>> serializer) {
            return new RecipeRegistry<>(
                this.types.register(id, () -> new RecipeType<T>(){}),
                this.serializers.register(id, serializer)
            );
        }
    }
}
