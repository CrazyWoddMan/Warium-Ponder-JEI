package crazywoddman.warium_ponder_jei.data.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public interface IDurationalRecipe<T extends ItemLike> {
    int getProcessTime();
    boolean test(ItemStack... inputs);
    T[] getResults();
    
    default int getTimeLeft(int progress) {
        return getProcessTime() - progress / 4;
    }

    default int getTimeLeftover(ItemStack stack) {
        int count = stack.getCount() - 1;
        return count > 0 ? stack.getCount() * getProcessTime() : 0;
    }
}