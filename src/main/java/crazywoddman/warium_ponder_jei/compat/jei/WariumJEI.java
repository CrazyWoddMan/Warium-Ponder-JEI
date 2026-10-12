package crazywoddman.warium_ponder_jei.compat.jei;

import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;

import java.util.List;

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
import net.minecraft.world.item.crafting.SmeltingRecipe;
import crazywoddman.warium_ponder_jei.data.RecipeRegistry;
import crazywoddman.warium_ponder_jei.utils.WPJutil;
import crazywoddman.warium_ponder_jei.compat.jei.categories.*;

import static crazywoddman.warium_ponder_jei.data.WPJrecipes.*;

@JeiPlugin
public class WariumJEI implements IModPlugin {
    private static final ResourceLocation UID = WPJutil.location("jei");
    public static IJeiRuntime jeiRuntime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

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
        RegistrationHelper helper = new RegistrationHelper(registration, manager);
        List<SmeltingRecipe> smelting = manager.getAllRecipesFor(net.minecraft.world.item.crafting.RecipeType.SMELTING);
        registration.addRecipes(ThermalFurnaceCategory.TYPE, smelting);
        registration.addRecipes(AssemblyFurnaceCategory.TYPE, smelting);
        helper.addRecipes(AssemblyCategory.TYPE, ASSEMBLY);
        helper.addRecipes(MineralGrinderCategory.TYPE, MINERAL_GRINDER);
        helper.addRecipes(BlastFurnaceCategory.TYPE, BLAST_FURNACE);
        helper.addRecipes(FoundryCategory.TYPE, FOUNDRY);
        helper.addRecipes(RefineryCategory.TYPE, REFINERY);
        helper.addRecipes(CrusherCategory.TYPE, CRUSHER);
        helper.addRecipes(DigesterCategory.TYPE, DIGESTER);
        helper.addRecipes(FabricatorCategory.MECHANICAL_TYPE, MECHANICAL_FABRICATOR);
        helper.addRecipes(FabricatorCategory.CIRCUIT_TYPE, CIRCUIT_FABRICATOR);
        helper.addRecipes(CentrifugeCategory.TYPE, CENTRIFUGE);
        helper.addRecipes(BreederReactorCategory.TYPE, BREEDER_REACTOR);
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

    private record RegistrationHelper(IRecipeRegistration registration, RecipeManager manager) {

        <T extends Recipe<Container>> void addRecipes(RecipeType<T> type, RecipeRegistry<T> recipe) {
           this.registration.addRecipes(type, this.manager.getAllRecipesFor(recipe.type()));
        }
    }
}
