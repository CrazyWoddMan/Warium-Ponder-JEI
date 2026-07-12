package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModEntities;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class BombScene extends AbstractScene {
    private final BombType type;

    public BombScene(BombType type) {
        this.type = type;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return switch (this.type) {
            case KINETIC -> new RegistryObject[]{
                CrustyChunksModItems.ORDINANCE_KINETIC_HEAD
            };
            case FISSION -> new RegistryObject[]{
                CrustyChunksModItems.ORDINANCE_FISSION_INITIATOR_HEAD,
                CrustyChunksModItems.ORDINANCE_INLINE_FISSION_WARHEAD
            };
            case FUSION -> new RegistryObject[]{
                CrustyChunksModItems.ORDINANCE_FISSION_INITIATOR_HEAD,
                CrustyChunksModItems.ORDINANCE_INLINE_FUSION_WARHEAD_STAGE_1,
                CrustyChunksModItems.ORDINANCE_INLINE_FUSION_WARHEAD_STAGE_2
            };
        };
    }

    @Override
    protected String getID(SceneHelper helper) {
        return this.type.name().toLowerCase() + "_bomb";
    }

    @Override
    protected String getName(SceneHelper helper) {
        return switch (this.type) {
            case KINETIC -> "Kinetic";
            case FISSION -> "Fission";
            case FUSION -> "Fusion";
        } + " Bomb";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_FINS, 2, 1, 4)
            .show(Direction.DOWN);

        helper.idle(5);

        helper.setBlock(CrustyChunksModBlocks.ORDINANCE_CORE, 2, 1, 3)
            .show(Direction.SOUTH);

        helper.idle(5);

        helper.setBlock(type.inline, 2, 1, 2)
            .show(Direction.SOUTH);

        helper.idle(5);

        switch (this.type) {
            case KINETIC -> helper.setBlock(CrustyChunksModBlocks.ORDINANCE_KINETIC_HEAD, 2, 1, 1)
                .show(Direction.SOUTH);
            case FISSION -> helper.setBlock(CrustyChunksModBlocks.ORDINANCE_FISSION_INITIATOR_HEAD, 2, 1, 1)
                .show(Direction.SOUTH);
            case FUSION -> {
                helper.setBlock(CrustyChunksModBlocks.ORDINANCE_INLINE_FUSION_WARHEAD_STAGE_2, 2, 1, 1)
                    .show(Direction.SOUTH);

                helper.idle(5);

                helper.setBlock(CrustyChunksModBlocks.ORDINANCE_FISSION_INITIATOR_HEAD, 2, 1, 0)
                    .show(Direction.SOUTH);
            }
        }

        helper.idle(20);
        
        if (this.type != BombType.KINETIC) {
            helper.showTextAt("%s requires 8 %s and 1 %s",
                100, 2.5, 2, 2.5,
                getName(switch (type) {
                    case FISSION -> CrustyChunksModItems.ORDINANCE_INLINE_FISSION_WARHEAD;
                    case FUSION -> CrustyChunksModItems.ORDINANCE_INLINE_FUSION_WARHEAD_STAGE_1;
                    default -> null;
                }),
                getName(CrustyChunksModItems.IMPLOSION_LENS),
                getName(CrustyChunksModItems.FUSION_CORE)
            ).attachKeyFrame();

            helper.idle(20);

            helper.showControls(CrustyChunksModItems.IMPLOSION_LENS, Pointing.DOWN, 30, 2.5, 2, 2.5);

            helper.idle(40);

            helper.showControls(CrustyChunksModItems.FISSION_CORE, Pointing.DOWN, 30, 2.5, 2, 2.5);
            
            helper.idle(60);
        }

        helper.showTextAt("Only fins can be used", 40, 2.5, 2, 4.5)
            .attachKeyFrame();

        helper.idle(60);

        helper.setBlock(CrustyChunksModBlocks.STEEL_PLATING, 2, 2, 3).show(Direction.DOWN);

        helper.idle(20);

        helper.setBlock(Blocks.STONE_BUTTON.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 1, 2, 3)
        .show(Direction.EAST);

        helper.idle(20);

        helper.builder.world().hideSection(helper.select(0, 0, 0, 4, 0, 4), Direction.DOWN);

        helper.idle(20);

        helper.showTextAt("Send redstone signal next to the %s to activate the bomb",
            80, 2, 2.5, 3.5,
            getName(CrustyChunksModItems.ORDINANCE_CORE)
        ).attachKeyFrame();

        helper.idle(20);
 
        helper.toggleRedstone(1, 2, 3);

        helper.idle(5);

        helper.removeBlocks(0, 0, 0, 4, 1, 4);
        helper.createEntity(
            switch (this.type) {
                case KINETIC -> CrustyChunksModEntities.BUNKER_BUSTER_PROJECTILE;
                case FISSION -> CrustyChunksModEntities.NUCLEAR_BOMB_PROJECTILE;
                case FUSION -> CrustyChunksModEntities.ORDINANCE_FUSION_BOMB_PROJECTILE;
            },
            "{Motion: [0.0d, 0.0d, -0.3d], Rotation: [180.0f, 0.0f], Pos: [2.5d, 1.5d, 2.5d]}"
        );

        helper.idle(15);

        helper.toggleRedstone(1, 2, 3);
    }

    public enum BombType {
        KINETIC(CrustyChunksModBlocks.ORDINANCE_INLINE_WARHEAD),
        FISSION(CrustyChunksModBlocks.ORDINANCE_INLINE_FISSION_WARHEAD),
        FUSION(CrustyChunksModBlocks.ORDINANCE_INLINE_FUSION_WARHEAD_STAGE_1);

        final RegistryObject<Block> inline;

        private BombType(RegistryObject<Block> inline) {
            this.inline = inline;
        }
    }
}
