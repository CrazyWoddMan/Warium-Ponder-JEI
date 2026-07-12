package crazywoddman.warium_ponder_jei.ponder.scenes;

import java.util.function.Supplier;

import crazywoddman.warium_ponder_jei.data.WariumpPonderJeiRecipes;
import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.RegistryObject;

public class AssemblyScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{
            CrustyChunksModItems.ASSEMBLY_DEPOT,
            CrustyChunksModItems.ASSEMBLY_MACHINE,
            CrustyChunksModItems.ASSEMBLY_CRUSHER,
            CrustyChunksModItems.ASSEMBLY_FURNACE,
            CrustyChunksModItems.ASSEMBLY_CIRCUIT_FABRICATOR,
            CrustyChunksModItems.ASSEMBLY_MECHANICAL_FABRICATOR,
            CrustyChunksModItems.BAUXITE_DIGESTER
        };
    }

    @Override
    protected String getID(SceneHelper helper) {
        return AssemblyType.of(helper.getSubject()).name().toLowerCase();
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Assembly";
    }

    @SuppressWarnings({ "unchecked", "incomplete-switch" })
    @Override
    protected void showScene(SceneHelper helper) {
        AssemblyType type = AssemblyType.of(helper.getSubject());
        int line = type == AssemblyType.FURNACE ? 2 : 1;
        ElementLink<WorldSectionElement> firebox = null;

        switch (type) {
            case MACHINE -> {
                helper.setBlock(CrustyChunksModBlocks.ASSEMBLY_DEPOT.get(), 2, 1, 2)
                    .show(Direction.DOWN);

                helper.idle(5);

                helper.setBlock(type.getBlock().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 2, 2, 2)
                    .show(Direction.DOWN);
            }
            case FURNACE -> {
                firebox = helper.setBlock(CrustyChunksModBlocks.FIREBOX.get(), 2, 1, 2)
                    .show(Direction.DOWN);

                helper.idle(3);

                helper.setBlock(type.getBlock(), 2, 2, 2)
                    .show(Direction.DOWN);

                helper.idle(3);

                for (int i = 0; i < 3; i++) {
                    helper.setBlock(CrustyChunksModBlocks.BLAST_FUNNEL, 2, 3 + i, 2).show(Direction.DOWN);
                    helper.idle(3);
                }
            }
            default -> helper.setBlock(type.getBlock(), 2, 1, 2).show(Direction.DOWN);
        }

        helper.idle(10);

        helper.setBlock(CrustyChunksModBlocks.PRODUCTION_OUTPUT, 2, line, 1).show(Direction.SOUTH);
        helper.setBlock(CrustyChunksModBlocks.PRODUCTION_INPUT.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH), 2, line, 3)
            .show(Direction.NORTH);

        helper.idle(20);

        if (type == AssemblyType.FURNACE) {
            helper.showTextAt("Active firebox is required for %s to function", 80, 2, 1.5, 2.5, type.getName())
                
                .attachKeyFrame();

            helper.idle(30);

            for (RegistryObject<Block> block : new RegistryObject[]{
                CrustyChunksModBlocks.OIL_FIREBOX,
                CrustyChunksModBlocks.ELECTRIC_FIREBOX
            }) {
                helper.hideSection(Direction.SOUTH, firebox);

                helper.idle(15);

                firebox = helper.setBlock(block, 2, 1, 2).show(Direction.SOUTH);

                helper.idle(10);
            }

            helper.idle(20);
        } else {
            int powerY = type == AssemblyType.MACHINE ? 2 : 1;
            helper.showTextAt("%s requires %d untis of Kinetic Power to work",
                30 + EngineScene.EngineType.values().length * 25,
                2, powerY + 0.5, 2.5,
                type.getName(),
                WariumpPonderJeiRecipes.MACHINES_KINETIC_REQUIRE.getAsInt()
            ).attachKeyFrame();

            helper.idle(30);

            for (var engine : EngineScene.EngineType.values()) {
                var link = helper.setBlock(engine.getBlock().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 1, powerY, 2)
                    .show(Direction.SOUTH);

                helper.idle(10);

                helper.hideSection(Direction.SOUTH, link);

                helper.idle(15);
            }

            helper.idle(5);

            var crank = helper.setBlock(CrustyChunksModBlocks.MANUAL_CRANK.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST), 1, powerY, 2)
                .show(Direction.EAST);

            helper.idle(10);

            helper.showTextAt("Note that %s doesn't generate enough Kinetic Power",
                60, 2, powerY + 0.5, 2.5,
                getName(CrustyChunksModItems.MANUAL_CRANK)
            ).attachKeyFrame();

            helper.idle(60);

            helper.hideSection(Direction.WEST, crank);

            helper.idle(20);

            switch (type) {
                case CRUSHER -> {
                    helper.showTextAt("It also requires a crushing wheel", 70, 2.5, 2, 2.5)
                        .attachKeyFrame();
                    
                    helper.idle(20);

                    for (RegistryObject<Item> item : new RegistryObject[]{CrustyChunksModItems.STEEL_CRUSHING_WHEEL, CrustyChunksModItems.IRONGEAR}) {
                        helper.showControls(item, Pointing.DOWN, 20, 2.5, 2, 2.5);
                        helper.idle(35);
                    }
                }
                case DIGESTER -> {
                    helper.showTextAt("It also requires a catalyst", 60, 2.5, 2, 2.5)
                        .attachKeyFrame();
                    
                    helper.idle(10);

                    helper.showControls(CrustyChunksModItems.NITRATE, Pointing.DOWN, 50, 2.5, 2, 2.5);
                    helper.idle(70);
                }
                case MACHINE -> {
                    helper.showTextAt("It also requires a processor", 120, 2.5, 2.5, 2.5)
                        .attachKeyFrame();
                    
                    helper.idle(20);

                    for (RegistryObject<Item> item : new RegistryObject[]{
                        CrustyChunksModItems.MECHANICAL_BORE,
                        CrustyChunksModItems.MECHANICAL_PRESS,
                        CrustyChunksModItems.MECHANICAL_EXTRUDER,
                        CrustyChunksModItems.MECHANICAL_SHEAR
                    }) {
                        helper.showControls(item, Pointing.DOWN, 10, 2.5, 3, 2.5);
                        helper.idle(25);
                    }

                    helper.idle(20);
                }
            }
        }

        helper.showTextAt("Put item to process into %s",
            60, 2.5, line + 0.5, 4,
            getName(CrustyChunksModItems.PRODUCTION_INPUT)
        ).attachKeyFrame();

        helper.idle(80);

        helper.showTextAt("Processed item will be outputted into %s",
            100, 2.5, line + 0.5, 1.5,
            getName(CrustyChunksModItems.PRODUCTION_OUTPUT)
        );
    }

    private enum AssemblyType {
        MACHINE(CrustyChunksModItems.ASSEMBLY_MACHINE),
        CRUSHER(CrustyChunksModItems.ASSEMBLY_CRUSHER),
        FURNACE(CrustyChunksModItems.ASSEMBLY_FURNACE),
        CIRCUIT_FABRICATOR(CrustyChunksModItems.ASSEMBLY_CIRCUIT_FABRICATOR),
        MECHANICAL_FABRICATOR(CrustyChunksModItems.ASSEMBLY_MECHANICAL_FABRICATOR),
        DIGESTER(CrustyChunksModItems.BAUXITE_DIGESTER);

        private final Supplier<Item> item;

        AssemblyType(Supplier<Item> item) {
            this.item = item;
        }

        static AssemblyType of(Item item) {
            for (AssemblyType type : AssemblyType.values())
                if (type.item.get() == item)
                    return type;

            if (item == CrustyChunksModItems.ASSEMBLY_DEPOT.get())
                return MACHINE;
            
            throw new IllegalStateException("Unknown Assembly Type: " + AbstractScene.getName(item));
        }

        String getName() {
            return AbstractScene.getName(this.item);
        }

        Block getBlock() {
            return ((BlockItem)this.item.get()).getBlock();
        }
    }
}