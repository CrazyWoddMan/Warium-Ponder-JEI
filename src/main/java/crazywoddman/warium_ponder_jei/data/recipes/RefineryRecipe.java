package crazywoddman.warium_ponder_jei.data.recipes;

import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import net.mcreator.crustychunks.init.CrustyChunksModFluids;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class RefineryRecipe extends MultiResultRecipe<FluidStack> {
    
    public RefineryRecipe(ResourceLocation id, Ingredient ingredient, FluidStack... results) {
        super(id, WariumpPonderJeiRecipes.REFINERY_SERIALIZER, WariumpPonderJeiRecipes.REFINERY_TYPE, ingredient, results);
    }

    public static class Serializer implements RecipeSerializer<RefineryRecipe> {

        @Override
        public RefineryRecipe fromJson(ResourceLocation id, JsonObject json) {
            try {
                JsonArray resultsArr = GsonHelper.getAsJsonArray(json, "results");
                int size = resultsArr.size();

                if (size < 1)
                    throw new IllegalStateException("refinery recipe %s results amount is less then 1: ".formatted(id.toString()) + resultsArr.size());

                FluidStack[] results = new FluidStack[size];

                for (int i = 0; i < size; i++) {
                    JsonObject result = resultsArr.get(i).getAsJsonObject();
                    results[i] = new FluidStack(
                        ForgeRegistries.FLUIDS.getValue(ResourceLocation.parse(GsonHelper.getAsString(result, "fluid"))),
                        GsonHelper.getAsInt(result, "amount")
                    );
                }
                
                return new RefineryRecipe(
                    id,
                    Ingredient.fromJson(json.get("ingredient")),
                    results
                );
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse refinery recipe [" + id + "]", e);
            }
        }

        @Override
        public RefineryRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            FluidStack[] results = new FluidStack[buffer.readByte()];

            for (int i = 0; i < results.length; i++)
                results[i] = FluidStack.readFromPacket(buffer);
            
            return new RefineryRecipe(id, ingredient, results);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, RefineryRecipe recipe) {
            recipe.ingredients[0].toNetwork(buffer);
            buffer.writeByte(recipe.result.length);

            for (FluidStack result : recipe.result)
                result.writeToPacket(buffer);
            
        }
    }

    /**
     * Used if this mod is in <b>client-side</b> mode
     */
    public static final List<RefineryRecipe> STATIC_RECIPES = List.of(
            new RefineryRecipe(
                null,
                Ingredient.of(CrustyChunksModItems.SHALE_OIL.get()),
                new FluidStack(CrustyChunksModFluids.OIL.get(), 250),
                new FluidStack(CrustyChunksModFluids.DIESEL.get(), 250),
                new FluidStack(CrustyChunksModFluids.KEROSENE.get(), 250),
                new FluidStack(CrustyChunksModFluids.PETROLIUM.get(), 250)
            )
    );
}