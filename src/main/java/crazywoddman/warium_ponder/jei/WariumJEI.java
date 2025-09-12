package crazywoddman.warium_ponder.jei;

import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class WariumJEI implements IModPlugin {

    public static IJeiRuntime jeiRuntime;

    @Override
    public void onRuntimeAvailable(IJeiRuntime runtime) {
        jeiRuntime = runtime;
    }

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath("warium_ponder", "jei");

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
            new FoundryCategory(guiHelper),
            new LatheCategory(guiHelper),
            new OilRefineryCategory(guiHelper),
            new CentrifugeCategory(guiHelper),
            new ReactorCategory(guiHelper),
            new PressCategory(guiHelper),
            new CutterCategory(guiHelper),
            new ExtruderCategory(guiHelper),
            new CrusherCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
            MineralGrinderCategory.TYPE,
            MineralGrinderRecipes.RECIPES
        );
        registration.addRecipes(
            BlastFurnaceCategory.TYPE,
            BlastFurnaceRecipes.RECIPES
        );
        registration.addRecipes(
            ThermalFurnaceCategory.TYPE,
            ThermalFurnaceRecipes.RECIPES
        );
        registration.addRecipes(
            FoundryCategory.TYPE,
            FoundryRecipes.RECIPES
        );
        registration.addRecipes(
            LatheCategory.TYPE,
            LatheRecipes.RECIPES
        );
        registration.addRecipes(
            OilRefineryCategory.TYPE,
            OilRefineryRecipes.RECIPES
        );
        registration.addRecipes(
            CentrifugeCategory.TYPE,
            CentrifugeRecipes.RECIPES
        );
        registration.addRecipes(
            ReactorCategory.TYPE,
            ReactorRecipes.RECIPES
        );
        registration.addRecipes(
            PressCategory.TYPE,
            PressRecipes.RECIPES
        );
        registration.addRecipes(
            CutterCategory.TYPE,
            CutterRecipes.RECIPES
        );
        registration.addRecipes(
            ExtruderCategory.TYPE,
            ExtruderRecipes.RECIPES
        );
        registration.addRecipes(
            CrusherCategory.TYPE,
            MineralGrinderRecipes.RECIPES
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalysts(
            BlastFurnaceCategory.TYPE,
            WariumPonderUtil.getItem("blast_furnace"),
            WariumPonderUtil.getItem("blast_funnel"),
            WariumPonderUtil.getItem("firebox"),
            WariumPonderUtil.getItem("oil_firebox"),
            WariumPonderUtil.getItem("electric_firebox")
        );
        registration.addRecipeCatalysts(
            ThermalFurnaceCategory.TYPE,
            WariumPonderUtil.getItem("thermal_furnace"),
            WariumPonderUtil.getItem("blast_funnel"),
            WariumPonderUtil.getItem("firebox"),
            WariumPonderUtil.getItem("oil_firebox"),
            WariumPonderUtil.getItem("electric_firebox")
        );
        registration.addRecipeCatalysts(
            FoundryCategory.TYPE,
            WariumPonderUtil.getItem("foundry"),
            WariumPonderUtil.getItem("firebox"),
            WariumPonderUtil.getItem("oil_firebox"),
            WariumPonderUtil.getItem("electric_firebox")
        );
        registration.addRecipeCatalysts(
            OilRefineryCategory.TYPE,
            WariumPonderUtil.getItem("refinery_tower"),
            WariumPonderUtil.getItem("refinery"),
            WariumPonderUtil.getItem("firebox"),
            WariumPonderUtil.getItem("oil_firebox"),
            WariumPonderUtil.getItem("electric_firebox")
        );
        registration.addRecipeCatalysts(
            CentrifugeCategory.TYPE,
            WariumPonderUtil.getItem("centrifuge_top"),
            WariumPonderUtil.getItem("centrifuge_core"),
            WariumPonderUtil.getItem("centrifuge_bottom"),
            WariumPonderUtil.getItem("giant_coil")
        );
        registration.addRecipeCatalysts(
            ReactorCategory.TYPE,
            WariumPonderUtil.getItem("breeder_reactor_interface"),
            WariumPonderUtil.getItem("breeder_reactor_core"),
            WariumPonderUtil.getItem("breeder_reactor_port"),
            WariumPonderUtil.getItem("reaction_chamber"),
            WariumPonderUtil.getItem("control_rod"),
            WariumPonderUtil.getItem("reactor_casing"),
            WariumPonderUtil.getItem("empty_fuel_rods"),
            WariumPonderUtil.getItem("fuel_rod")
        );
        registration.addRecipeCatalyst(
            WariumPonderUtil.getItem("mineral_grinder"),
            MineralGrinderCategory.TYPE
        );
        registration.addRecipeCatalyst(
            WariumPonderUtil.getItem("crusher"),
            CrusherCategory.TYPE
        );
        registration.addRecipeCatalyst(
            WariumPonderUtil.getItem("lathe"),
            LatheCategory.TYPE
        );
        registration.addRecipeCatalyst(
            WariumPonderUtil.getItem("press"),
            PressCategory.TYPE
        );
        registration.addRecipeCatalyst(
            WariumPonderUtil.getItem("cutter"),
            CutterCategory.TYPE
        );
        registration.addRecipeCatalyst(
            WariumPonderUtil.getItem("extruder"),
            ExtruderCategory.TYPE
        );
    }
}