package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class SeekerScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return WariumPonderJei.WARIUM_VS
        ? new RegistryObject[]{CrustyChunksModItems.ORDINANCE_IR_SEEKER_HEAD, CrustyChunksModItems.ORDINANCE_SARH_SEEKER}
        : new RegistryObject[]{CrustyChunksModItems.ORDINANCE_IR_SEEKER_HEAD};
    }

    @Override
    protected String getName(SceneHelper helper) {
        return switch (SeekerType.of(helper.getSubject())) {
            case IR -> "IR";
            case SARH -> "SARH";
        } + " Seeker Warhead";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        SeekerType type = SeekerType.of(helper.getSubject());
        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_THRUSTER.get().defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 2, 1, 2)
            .show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_CORE.get().defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 2, 2, 2)
            .show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(type.getBlock().defaultBlockState().setValue(BlockStateProperties.ATTACH_FACE, AttachFace.FLOOR), 2, 3, 2)
            .show(Direction.DOWN);

        helper.idle(20);

        helper.showTextAt(
            switch (type) {
                case IR -> "IR Seeker missile chases other missiles or active engines on VS grids";
                case SARH -> "SARH Seeker missile chases any target the Small Radar is locked onto";
            },
            80, 2.5, 2.5, 2
        ).attachKeyFrame();
        
        helper.idle(100);

        helper.showTextAt("Only thruster can be used", 40, 2.5, 1.5, 2)
            .attachKeyFrame();

        helper.idle(60);

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 2, 1, 3).show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 2, 2, 3).show(Direction.DOWN);

        helper.idle(20);

        helper.setBlock(Blocks.STONE_BUTTON, 2, 2, 2).show(Direction.SOUTH);

        helper.idle(20);

        helper.showTextAt("Send redstone signal next to the %s to activate the missile",
            80, 2.5, 2.5, 3,
            getName(CrustyChunksModItems.ORDINANCE_CORE)
        ).attachKeyFrame();

        helper.idle(20);
 
        helper.toggleRedstone(2, 2, 2);

        helper.idle(5);

        helper.removeBlocks(3, 1, 3, 3, 3, 3);

        helper.idle(15);

        helper.toggleRedstone(2, 2, 2);
    }

    private enum SeekerType {
        IR(CrustyChunksModItems.ORDINANCE_IR_SEEKER_HEAD),
        SARH(CrustyChunksModItems.ORDINANCE_SARH_SEEKER);

        final RegistryObject<Item> item;

        private SeekerType(RegistryObject<Item> item) {
            this.item = item;
        }

        static SeekerType of(Item item) {
            for (SeekerType type : SeekerType.values())
                if (type.item.get() == item)
                    return type;

            throw new IllegalStateException("Unknown Seeker Type: " + AbstractScene.getName(item));
        }

        Block getBlock() {
            return ((BlockItem)this.item.get()).getBlock();
        }
    }
}
