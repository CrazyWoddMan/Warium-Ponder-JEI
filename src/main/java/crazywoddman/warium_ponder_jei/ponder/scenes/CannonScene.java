package crazywoddman.warium_ponder_jei.ponder.scenes;

import java.util.stream.Stream;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModEntities;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class CannonScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return Stream.of(CannonType.values()).map(t -> t.item).toArray(RegistryObject[]::new);
    }

    @Override
    protected String getName(SceneHelper helper) {
        return switch (CannonType.of(helper.getSubject())) {
            case ROTARY -> "Rotary Auto";
            case LIGHT -> "Light Auto";
            case HEAVY -> "Heavy Auto";
            case BATTLE -> "Battle";
            case ARTILLERY -> "Artillery";
        } + " Cannon";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.builder.scaleSceneView(0.7f);
        CannonType type = CannonType.of(helper.getSubject());
        var cannon = helper.setBlock(type.getBlock(), 2, 1, 2).show(Direction.DOWN);

        helper.idle(10);

        @SuppressWarnings("unchecked")
        ElementLink<WorldSectionElement>[] barrels = new ElementLink[type.barrel];
        barrels[0] = helper.setBlock(
            switch (type) {
                case ROTARY -> CrustyChunksModBlocks.RAC_BARREL;
                case LIGHT, HEAVY -> CrustyChunksModBlocks.AUTOCANNON_BARREL;
                case BATTLE -> CrustyChunksModBlocks.BATTLE_CANNON_BARREL;
                case ARTILLERY -> CrustyChunksModBlocks.ARTILLERY_BARREL;
            },
            2, 1, 1
        ).show(Direction.SOUTH);

        helper.idle(20);

        helper.showTextAt("%s uses %s Shells as ammo",
            80, 2.5, 1.5, 2.5,
            type.getName(),
            switch (type) {
                case ROTARY, LIGHT -> "Small";
                case HEAVY -> "Medium";
                case BATTLE -> "Large";
                case ARTILLERY -> "Artillery";
            }
        ).attachKeyFrame();

        helper.idle(20);

        switch (type) {
            case ROTARY, LIGHT, HEAVY -> {
                helper.idle(70);

                helper.moveSection(15, 0, 1, 0, cannon, barrels[0]);
                helper.moveSection(
                    15, 0, 1, 0,
                    helper.setBlock(CrustyChunksModBlocks.AUTOCANNON_DRUM, 2, 0, 2).show()
                );
                
                helper.idle(20);

                helper.showTextAt("Put them into %s to provide cannon with ammo",
                    80, 2, 1.5, 2.5,
                    getName(CrustyChunksModItems.AUTOCANNON_DRUM)
                ).attachKeyFrame();

                helper.idle(20);

                helper.showControls(
                    type == CannonType.HEAVY ? CrustyChunksModItems.LARGE_SHELL : CrustyChunksModItems.HUGE_BULLET, 
                    Pointing.RIGHT,
                    40, 2.5, 1.5, 2
                );

                helper.idle(80);
            }
            case BATTLE, ARTILLERY -> {
                 helper.showControls(
                    switch (type) {
                        case ROTARY, LIGHT -> CrustyChunksModItems.HUGE_BULLET;
                        case HEAVY -> CrustyChunksModItems.LARGE_SHELL;
                        case BATTLE -> CrustyChunksModItems.LARGE_SHELL;
                        case ARTILLERY -> CrustyChunksModItems.ARTILLERY_SHELL;
                    }, 
                    Pointing.DOWN,
                    25, 2.5, 2, 2.5
                );

                helper.idle(35);

                if (type == CannonType.ARTILLERY)
                    helper.showControls(CrustyChunksModItems.POWDER_CHARGE, Pointing.DOWN, 25, 2.5, 2, 2.5);
                
                helper.idle(45);

                helper.moveSection(15, 0, 1, 0, cannon, barrels[0]);
                helper.moveSection(
                    15, 0, 1, 0,
                    helper.setBlock(
                        type == CannonType.BATTLE 
                        ? CrustyChunksModBlocks.AUTOLOADER.get().defaultBlockState().setValue(BlockStateProperties.FACING, Direction.UP)
                        : CrustyChunksModBlocks.ARTILLERY_AUTOLOADER.get().defaultBlockState()
                        , 2, 0, 2
                    ).show()
                );

                helper.idle(10);

                if (type == CannonType.ARTILLERY) {
                    helper.setBlock(CrustyChunksModBlocks.ARTILLERY_CHARGE_LOADER, 2, 2, 3)
                        .show(Direction.NORTH);

                    helper.idle(10);
                }

                var hopper = helper.setBlock(Blocks.HOPPER.defaultBlockState().setValue(BlockStateProperties.FACING_HOPPER, Direction.EAST), 1, 1, 2)
                    .show(Direction.EAST);

                helper.idle(20);

                helper.showTextAt("Autoloader can be used to supply ammo from hopper", 60, 2.5, 1.5, 2)
                    .attachKeyFrame();

                helper.idle(75);

                helper.hideSection(Direction.WEST, hopper);

                helper.idle(15);

                helper.showTextAt("Autoloader must be facing the cannon to function",
                    60,
                    type == CannonType.BATTLE ? 2.5 : 2,
                    type == CannonType.BATTLE ? 1.5 : 2.5,
                    type == CannonType.BATTLE ? 2 : 3.5
                ).attachKeyFrame();

                helper.idle(80);
            }   
        }

        helper.showTextAt("Barrel can be extended up to %d blocks", 80, 2.5, 2.5, 1.5, type.barrel)
            .attachKeyFrame();

        helper.idle(15);

        for (int i = 1; i < type.barrel; i++) {
            helper.moveSection(0, 1, -i, barrels[i] = helper.showSection(Direction.SOUTH, 2, 1, 1));
            helper.idle(5);
        }

        helper.idle(65 - type.barrel * 5 + 20);

        helper.setBlock(Blocks.STONE_BUTTON.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST), 1, 2, 2)
            .show(Direction.EAST);

        helper.idle(20);

        helper.builder.addKeyframe();
        helper.toggleRedstone(1, 2, 2);

        helper.idle(5);

        if (type == CannonType.ROTARY)
            for (var barrel : barrels)
            helper.builder.world().rotateSection(barrel, 0, 0, -1620, 15);

         switch (type) {
            case ROTARY, LIGHT, HEAVY -> {
                for (int i = 0; i < 15; i++) {
                     helper.createEntity(
                        CrustyChunksModEntities.BULLETFIRE_PROJECTILE,
                        "{Motion: [0.0d, 0.3, -2.0d], Pos: [2.5d, 2.5d, %fd]}".formatted(2.0 - type.barrel)
                    );
                    helper.createEntity(
                        CrustyChunksModEntities.GENERICLARGE_BULLET,
                        "{Motion: [0.0d, 0.3, -2.0d], Pos: [2.5d, 2.5d, %fd]}".formatted(2.0 - type.barrel)
                    );
                    helper.idle(1);
                }
            }
            case BATTLE, ARTILLERY -> {
                helper.createEntity(
                    type == CannonType.BATTLE ? CrustyChunksModEntities.LARGE_SOLID_PROJECTILE :  CrustyChunksModEntities.ARTILLERY_SOLID_PROJECTILE,
                    "{Motion: [0.0d, 0.3, -4.0d], Pos: [2.5d, 2.5d, %fd]}".formatted(2.0 - type.barrel)
                );
                helper.idle(15);
            }
        }

        helper.toggleRedstone(1, 2, 2);
    }

    private enum CannonType {
        ROTARY(CrustyChunksModItems.ROTARY_AUTO_CANNON, 7),
        LIGHT(CrustyChunksModItems.LIGHT_AUTOCANNON, 7),
        HEAVY(CrustyChunksModItems.AUTOCANNON, 7),
        BATTLE(CrustyChunksModItems.BATTLE_CANNON_BREECH, 12),
        ARTILLERY(CrustyChunksModItems.ARTILLERYBREECH, 12);

        final RegistryObject<Item> item;
        final int barrel;

        private CannonType(RegistryObject<Item> item, int barrel) {
            this.item = item;
            this.barrel = barrel;
        }

        static CannonType of(Item item) {
            for (CannonType type : CannonType.values())
                if (type.item.get() == item)
                    return type;

            throw new IllegalStateException("Unknown Cannon Type: " + AbstractScene.getName(item));
        }

        String getName() {
            return AbstractScene.getName(this.item);
        }

        Block getBlock() {
            return ((BlockItem)this.item.get()).getBlock();
        }
    }
}
