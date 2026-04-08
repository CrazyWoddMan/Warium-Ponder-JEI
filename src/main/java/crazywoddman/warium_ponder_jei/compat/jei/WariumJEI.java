package crazywoddman.warium_ponder_jei.compat.jei;

import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.helpers.IGuiHelper;

import java.util.List;
import java.util.function.Supplier;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.recipes.BlastFurnaceRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.BreederReactorRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.CentrifugeRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.CircuitFabricatorRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.CrusherRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.DigesterRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.FoundryRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.MechanicalFabricatorRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.MineralGrinderRecipe;
import crazywoddman.warium_ponder_jei.data.recipes.RefineryRecipe;
import crazywoddman.warium_ponder_jei.network.NetworkHandler;
import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.compat.jei.categories.*;

@JeiPlugin
public class WariumJEI implements IModPlugin {
    public static IJeiRuntime jeiRuntime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(WariumPonderJei.MODID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
            new MineralGrinderCategory(guiHelper),
            new BlastFurnaceCategory(guiHelper),
            new ThermalFurnaceCategory(guiHelper),
            new AssemblyFurnaceCategory(guiHelper),
            new FoundryCategory(guiHelper),
            new RefineryCategory(guiHelper),
            new CentrifugeCategory(guiHelper),
            new BreederReactorCategory(guiHelper),
            new CrusherCategory(guiHelper),
            new AssemblyCategory(guiHelper),
            new DigesterCategory(guiHelper),
            new FabricatorCategory<>(guiHelper, FabricatorCategory.MECHANICAL_TYPE, CrustyChunksModItems.ASSEMBLY_MECHANICAL_FABRICATOR),
            new FabricatorCategory<>(guiHelper, FabricatorCategory.CIRCUIT_TYPE, CrustyChunksModItems.ASSEMBLY_CIRCUIT_FABRICATOR)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager manager = Minecraft.getInstance().level.getRecipeManager();
        List<SmeltingRecipe> smelting = manager.getAllRecipesFor(RecipeType.SMELTING);
        RegistrationHelper helper = new RegistrationHelper(registration, manager);

        registration.addRecipes(ThermalFurnaceCategory.TYPE, smelting);
        registration.addRecipes(AssemblyFurnaceCategory.TYPE, smelting);
        registration.addRecipes(
            AssemblyCategory.TYPE,
            manager.getAllRecipesFor(WariumpPonderJeiRecipes.ASSEMBLY_TYPE.get())
        );
        helper.addRecipes(MineralGrinderCategory.TYPE, WariumpPonderJeiRecipes.MINERAL_GRINDER_TYPE, MineralGrinderRecipe.STATIC_RECIPES);
        helper.addRecipes(BlastFurnaceCategory.TYPE, WariumpPonderJeiRecipes.BLAST_FURNACE_TYPE, BlastFurnaceRecipe.STATIC_RECIPES);
        helper.addRecipes(FoundryCategory.TYPE, WariumpPonderJeiRecipes.FOUNDRY_TYPE, FoundryRecipe.STATIC_RECIPES);
        helper.addRecipes(RefineryCategory.TYPE, WariumpPonderJeiRecipes.REFINERY_TYPE, RefineryRecipe.STATIC_RECIPES);
        helper.addRecipes(CrusherCategory.TYPE, WariumpPonderJeiRecipes.CRUSHER_TYPE, CrusherRecipe.STATIC_RECIPES);
        helper.addRecipes(DigesterCategory.TYPE, WariumpPonderJeiRecipes.DIGESTER_TYPE, DigesterRecipe.STATIC_RECIPES);
        helper.addRecipes(FabricatorCategory.MECHANICAL_TYPE, WariumpPonderJeiRecipes.MECHANICAL_FABRICATOR_TYPE, MechanicalFabricatorRecipe.STATIC_RECIPES);
        helper.addRecipes(FabricatorCategory.CIRCUIT_TYPE, WariumpPonderJeiRecipes.CIRCUIT_FABRICATOR_TYPE, CircuitFabricatorRecipe.STATIC_RECIPES);
        helper.addRecipes(CentrifugeCategory.TYPE, WariumpPonderJeiRecipes.CENTRIFUGE_TYPE, CentrifugeRecipe.STATIC_RECIPES);
        helper.addRecipes(BreederReactorCategory.TYPE, WariumpPonderJeiRecipes.BREEDER_REACTOR_TYPE, BreederReactorRecipe.STATIC_RECIPES);
    }

    private static class RegistrationHelper {
        private final IRecipeRegistration registration;
        private final RecipeManager manager;

        private RegistrationHelper(IRecipeRegistration registration, RecipeManager manager) {
            this.registration = registration;
            this.manager = NetworkHandler.serverHasMod ? manager : null;
        }

        private <C extends Container, T extends Recipe<C>> void addRecipes(
            mezz.jei.api.recipe.RecipeType<T> jeiType,
            Supplier<? extends RecipeType<T>> type,
            List<T> list
        ) {
           this.registration.addRecipes(jeiType, manager == null ? list : manager.getAllRecipesFor(type.get()));
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(
            BlastFurnaceCategory.TYPE,
            CrustyChunksModItems.BLAST_FURNACE.get()
        );
        registration.addRecipeCatalysts(
            ThermalFurnaceCategory.TYPE,
            CrustyChunksModItems.THERMAL_FURNACE.get()
        );
        registration.addRecipeCatalysts(
            AssemblyFurnaceCategory.TYPE,
            CrustyChunksModItems.ASSEMBLY_FURNACE.get()
        );
        registration.addRecipeCatalysts(
            FoundryCategory.TYPE,
            CrustyChunksModItems.FOUNDRY.get()
        );
        registration.addRecipeCatalysts(
            RefineryCategory.TYPE,
            CrustyChunksModItems.REFINERY.get(),
            CrustyChunksModItems.REFINERY_TOWER.get()
        );
        registration.addRecipeCatalysts(
            CentrifugeCategory.TYPE,
            CrustyChunksModItems.GIANT_COIL.get(),
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_BOTTOM.get(),
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_MIDDLE.get(),
            CrustyChunksModItems.ASSEMBLY_CENTRIFUGE_TOP.get()
        );
        registration.addRecipeCatalysts(
            BreederReactorCategory.TYPE,
            CrustyChunksModItems.BREEDER_REACTOR_INTERFACE.get(),
            CrustyChunksModItems.BREEDER_REACTOR_CORE.get(),
            CrustyChunksModItems.BREEDER_REACTOR_PORT.get(),
            CrustyChunksModItems.REACTION_CHAMBER.get(),
            CrustyChunksModItems.CONTROL_ROD.get(),
            CrustyChunksModItems.REACTOR_CASING.get(),
            CrustyChunksModItems.EMPTY_FUEL_RODS.get(),
            CrustyChunksModItems.FUEL_ROD.get()
        );
        registration.addRecipeCatalysts(
            MineralGrinderCategory.TYPE,
            CrustyChunksModItems.MINERAL_GRINDER.get()
        );
        registration.addRecipeCatalysts(
            CrusherCategory.TYPE,
            CrustyChunksModItems.ASSEMBLY_CRUSHER.get()
        );
        registration.addRecipeCatalysts(
            AssemblyCategory.TYPE,
            CrustyChunksModItems.ASSEMBLY_MACHINE.get(),
            CrustyChunksModItems.ASSEMBLY_DEPOT.get()
        );
        registration.addRecipeCatalysts(
            DigesterCategory.TYPE,
            CrustyChunksModItems.BAUXITE_DIGESTER.get()
        );
        registration.addRecipeCatalysts(
            FabricatorCategory.MECHANICAL_TYPE,
            CrustyChunksModItems.ASSEMBLY_MECHANICAL_FABRICATOR.get()
        );
        registration.addRecipeCatalysts(
            FabricatorCategory.CIRCUIT_TYPE,
            CrustyChunksModItems.ASSEMBLY_CIRCUIT_FABRICATOR.get()
        );
    }
}
