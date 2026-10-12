package crazywoddman.warium_ponder_jei.mixins;

import net.mcreator.crustychunks.procedures.PaintToolFireProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import crazywoddman.warium_ponder_jei.data.WPJrecipes;
import crazywoddman.warium_ponder_jei.data.recipes.PaintToolRecipe;

@Mixin(PaintToolFireProcedure.class)
public class PaintToolFireProcedureMixin {
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Inject(
        method = "execute",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void wpj$execute(LevelAccessor world, double x, double y, double z, ItemStack paintTool, CallbackInfo ci) {
        ci.cancel();
        Level level = (Level)world;

        if (level.isClientSide)
            return;

        CompoundTag tag = paintTool.getTag();

        if (tag == null)
            return;

        int fluid = tag.getInt("Fluid");

        if (fluid < 1)
            return;
        
        String color = tag.getString("Color");

        if (color.isEmpty() || color.equals("none"))
            return;
        
        BlockPos pos = BlockPos.containing(x, y, z);
        BlockState state = level.getBlockState(pos);
        Item item = state.getBlock().asItem();
        ItemStack stack = item.getDefaultInstance();
        
        for (PaintToolRecipe recipe : WPJrecipes.PAINT_TOOL.get(level)) {
            if (recipe.ingredient.test(stack)) {
                recipe.getResult(item, color).ifPresent(result -> {
                    if (fluid < 2) {
                        tag.remove("Color");
                        tag.remove("Fluid");
                        tag.remove("Loaded");
                    } else {
                        tag.putInt("Fluid", fluid - 1);
                    }

                    BlockState newState = result.getBlock().defaultBlockState();

                    for (Property property : state.getProperties())
                        newState = newState.trySetValue(property, state.getValue(property));

                    BlockEntity blockEntity = level.getBlockEntity(pos);
                    level.setBlockAndUpdate(pos, newState);

                    if (blockEntity != null) {
                        BlockEntity newBlockEntity = level.getBlockEntity(pos);

                        if (newBlockEntity != null) {
                            newBlockEntity.load(blockEntity.saveWithFullMetadata());
                            newBlockEntity.setChanged();
                            level.sendBlockUpdated(pos, newState, newState, Block.UPDATE_ALL);
                        }
                    }

                    level.playSound(
                        null,
                        pos,
                        SoundEvents.PUFFER_FISH_BLOW_OUT,
                        SoundSource.BLOCKS,
                        1,
                        Mth.nextFloat(RandomSource.create(), 0.7f, 1)
                    );
                });
                
                return;
            }
        }
    }
}
