package crazywoddman.warium_ponder_jei.util;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.apache.logging.log4j.util.TriConsumer;

import crazywoddman.warium_ponder_jei.data.ChancedItem;
import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.data.WariumpjTags;
import crazywoddman.warium_ponder_jei.data.recipes.SimpleRecipe;
import net.mcreator.crustychunks.block.AssemblyCrusherBlock;
import net.mcreator.crustychunks.block.entity.ProductionInputBlockEntity;
import net.mcreator.crustychunks.block.entity.ProductionOutputBlockEntity;
import net.mcreator.crustychunks.init.CrustyChunksModBlockEntities;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class WariumPonderJeiUtil {

    /**
     * @return current time in ticks
     */
    public static int ticks() {
        return (int)(System.currentTimeMillis() / 50);
    }

    /**
     * @return number from <b>size</b> ranged cycle that changes every 20 ticks
     */
    public static int ticker(int size) {
        return ((ticks() / 20) % size);
    }

    /**
     * @return element from <b>elements</b> ranged cycle that changes every 20 ticks
     */
    @SafeVarargs
    public static final <T> T ticker(T... elements) {
        return elements[ticker(elements.length)];
    }

    public static Optional<BlockEntity> heatedBlockProcedure(LevelAccessor level, BlockPos pos, int decrease) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        
        if (blockEntity != null) {
            CompoundTag data = blockEntity.getPersistentData();
            int heat = data.getInt("Heat");
            int require = WariumpPonderJeiRecipes.HEAT_REQUIRE.getAsInt();

            if (heat > 0)
                data.putInt("Heat", heat > (require + 100) ? heat - 8 * decrease : heat - decrease);

            if (heat >= require)
                return Optional.of(blockEntity);
        }

        return Optional.empty();
    }

    public static double checkKinetic(BlockEntity blockEntity, Direction consuming) {
        if (blockEntity != null) {
            BlockState state = blockEntity.getBlockState();

            return (
                state.is(WariumpjTags.Blocks.KINETIC_OUTPUT_FRONT)
                && (state.hasProperty(HorizontalDirectionalBlock.FACING) ? state.getValue(HorizontalDirectionalBlock.FACING) : state.getValue(DirectionalBlock.FACING)) == consuming
            ) ? blockEntity.getPersistentData().getDouble("KineticPower") : 0;
        }

        return 0;
    }

    public static double checkKinetic(Level level, BlockPos pos, Direction... sides) {
        double kineticPower = 0;

        for (Direction side : sides)
            kineticPower += checkKinetic(level.getBlockEntity(pos.relative(side)), side.getOpposite());

        return kineticPower;
    }

    public static Optional<IItemHandlerModifiable> getItemHandler(BlockEntity blockEntity) {
        return blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).map(cap -> (IItemHandlerModifiable)cap);
    }

    public static void tryAssembly(ServerLevel level, BlockPos pos, boolean allowEmptyInput, Predicate<Direction> kineticPredicate, TriConsumer<BlockEntity, ItemStack, IItemHandlerModifiable> procedure) {
        Direction facing = level.getBlockState(pos).getValue(AssemblyCrusherBlock.FACING);
        BlockEntity output = level.getBlockEntity(pos.relative(facing));

        if (!(output instanceof ProductionOutputBlockEntity))
            return;
        
        BlockEntity input = level.getBlockEntity(pos.relative(facing.getOpposite()));
        
        if (!(input instanceof ProductionInputBlockEntity))
            return;

        if (kineticPredicate.test(facing))
            input.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(inputCap -> {
                ItemStack stack = inputCap.getStackInSlot(0);

                if (!allowEmptyInput && stack.isEmpty())
                    return;

                getItemHandler(output).ifPresent(outputCap ->
                    procedure.accept(
                        level.getBlockEntity(pos),
                        stack,
                        outputCap
                    )
                );
            });
    }

    public static void tryAssembly(ServerLevel level, BlockPos pos, boolean allowEmptyInput, TriConsumer<BlockEntity, ItemStack, IItemHandlerModifiable> procedure) {
        tryAssembly(level, pos, allowEmptyInput, facing -> checkKinetic(level, pos, facing.getClockWise(), facing.getCounterClockWise()) >= WariumpPonderJeiRecipes.MACHINES_KINETIC_REQUIRE.getAsInt(), procedure);
    }

    public static <C extends Container, T extends Recipe<C>> Optional<T> findRecipe(Level level, Supplier<RecipeType<T>> recipeType, Predicate<T> tester) {
        for (T recipe : level.getRecipeManager().getAllRecipesFor(recipeType.get()))
            if (tester.test(recipe))
                return Optional.of(recipe);
        
        return Optional.empty();
    }

    public static <T extends SimpleRecipe<?>> Optional<T> findRecipe(Level level, Supplier<RecipeType<T>> recipeType, ItemStack... inputs) {
        return findRecipe(level, recipeType, recipe -> recipe.test(inputs));
    }

    @SafeVarargs
    public static <T extends ItemLike> boolean readItemSlots(IItemHandler output, T... results) {
        for (int i = 0; i < results.length; i++) {
            ItemStack stack = output.getStackInSlot(i);

            if (!stack.isEmpty() && (!stack.is(results[i].asItem()) || !(stack.getMaxStackSize() > stack.getCount())))
                return false;
        }

        return true;
    }

    @SafeVarargs
    public static <T extends ItemLike> boolean writeItemSlots(IItemHandlerModifiable output, ItemStack input, Predicate<T> predicate, T... results) {
        input.shrink(1);

        for (int i = 0; i < results.length; i++)
            if (predicate.test(results[i]))
                output.setStackInSlot(i,  new ItemStack(results[i].asItem(), output.getStackInSlot(i).getCount() + 1));
        
        return true;
    }

    public static boolean writeItemSlots(IItemHandlerModifiable output, ItemStack input, RandomSource random, ChancedItem... results) {
        return writeItemSlots(output, input, item -> item.chance() == 100 || random.nextInt(100) < item.chance(), results);
    }

    @SafeVarargs
    public static <T extends ItemLike> boolean processAssembly(IItemHandlerModifiable output, ItemStack input, Predicate<T> predicate, T... results) {
        return readItemSlots(output, results) && writeItemSlots(output, input, predicate, results);
    }

    public static boolean processAssembly(IItemHandlerModifiable output, ItemStack input, RandomSource random, ChancedItem... results) {
        return processAssembly(output, input, item -> item.chance() == 100 || random.nextInt(100) < item.chance(), results);
    }

    public static record BlockSoundPlayer(ServerLevel level, BlockPos pos) {
        public void playSound(SoundEvent sound, float volume, float pitch) {
            this.level.playSound(
                null,
                this.pos,
                sound,
                SoundSource.BLOCKS,
                volume,
                pitch
            );
        }
    }

    public static final Direction[] HORIZONTAL_DIRECTIONS = {Direction.EAST, Direction.WEST, Direction.SOUTH, Direction.NORTH};

    public static boolean verifyReactor(Level level, BlockPos main) {
        BlockChecker validator = new BlockChecker(level);
        BlockPos below = main.below();

        if (!validator.check(below, CrustyChunksModBlocks.BREEDER_REACTOR_CORE) || !validator.check(main.below(2), CrustyChunksModBlocks.BREEDER_REACTOR_PORT))
            return false;

        for (Direction side : HORIZONTAL_DIRECTIONS)
            for (BlockPos pos : new BlockPos[]{main, below})
                if (!level.getBlockEntity(pos.relative(side, 2), CrustyChunksModBlockEntities.REACTION_CHAMBER.get()).map(be -> be.getPersistentData().getBoolean("Ready")).orElse(false))
                    return false;
        
        return true;
    }

    private static record BlockChecker(Level level) {
        private boolean check(BlockPos pos, Supplier<Block> block) {
            return this.level.getBlockState(pos).is(block.get());
        }
    }
}
