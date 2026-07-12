package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModEntities;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class HardpointScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{CrustyChunksModItems.EMPTY_MISSILE_HARDPOINT};
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Medium Rocket Hardpoint";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        var hardpoint = helper.setBlock(CrustyChunksModBlocks.EMPTY_MISSILE_HARDPOINT, 2, 1, 2).show(Direction.DOWN);

        helper.idle(15);

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 2, 3, 2).show(Direction.DOWN);
        
        helper.idle(10);
        
        helper.moveSection(5, 0, 1, 0, hardpoint);

        helper.idle(15);

        helper.builder.addKeyframe();
        helper.showControls(CrustyChunksModItems.FIRE_SPEAR_ROCKET, Pointing.RIGHT, 20, 3, 2.8, 2).rightClick();

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.FIRE_SPEAR_MISSILE_HARDPOINT, 2, 1, 2);

        helper.idle(20);

        helper.setBlock(Blocks.STONE_BUTTON.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 1, 3, 2)
            .show(Direction.EAST);

        helper.idle(20);

        helper.toggleRedstone(1, 3, 2);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.EMPTY_MISSILE_HARDPOINT, 2, 1, 2);
        helper.createEntity(
            CrustyChunksModEntities.FIRE_SPEAR_ROCKET_PROJECTILE,
            "{Motion: [0.0d, 0.0d, -4.0d], Rotation: [180.0f, 0.0f], Pos: [2.5d, 2.5d, 2.5d]}"
        );

        helper.idle(15);
        
        helper.toggleRedstone(1, 3, 2);
    }
}
