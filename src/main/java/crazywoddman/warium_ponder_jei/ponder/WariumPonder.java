package crazywoddman.warium_ponder_jei.ponder;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

import crazywoddman.warium_ponder_jei.WariumPonderJei;
import crazywoddman.warium_ponder_jei.ponder.scenes.*;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.createmod.ponder.foundation.PonderIndex;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public class WariumPonder implements PonderPlugin {
    public static void register() {
        PonderIndex.addPlugin(new WariumPonder());
    }

    @Override
    public String getModId() {
        return WariumPonderJei.MODID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> registrationHelper) {
        RegistrationHelper helper = new RegistrationHelper(registrationHelper.withKeyFunction(RegistryObject::getId));
        helper.addStoryBoard("5x5", MortarScene::scene, CrustyChunksModItems.MORTAR);
        helper.addStoryBoard("9x9", LargeEngineScene::scene, CrustyChunksModItems.DRIVE_SHAFT, CrustyChunksModItems.ENGINE_CYLLINDER, CrustyChunksModItems.LARGE_ENGINE_SMOKESTACK);
        helper.addStoryBoard("5x5", HardpointScene::scene, CrustyChunksModItems.EMPTY_MISSILE_HARDPOINT);
        helper.addStoryBoard("7x7", ReactorScenes::reactorInterface, CrustyChunksModItems.POWER_REACTOR_INTERFACE, CrustyChunksModItems.BREEDER_REACTOR_INTERFACE);
    }

    @FunctionalInterface
    private interface AdvancedStoryBoard {
        void program(SceneBuilder scene, SceneBuildingUtil util, SceneExtras extras, RegistryObject<Item> item);
    }

    private static class RegistrationHelper {
        private final PonderSceneRegistrationHelper<RegistryObject<Item>> helper;

        private RegistrationHelper(PonderSceneRegistrationHelper<RegistryObject<Item>> helper) {
            this.helper = helper;
        }

        @SuppressWarnings("unchecked")
        @SafeVarargs
        private final void addStoryBoard(String schematicPath, AdvancedStoryBoard storyBoard, RegistryObject<Item>... items) {
            for (RegistryObject<Item> item : items)
                helper.forComponents(item).addStoryBoard(schematicPath, (scene, util) -> storyBoard.program(scene, util, new SceneExtras(scene, util), item));
        }
    }

    public static class SceneExtras {
        private final SceneBuilder scene;
        private final SceneBuildingUtil util;

        private SceneExtras(SceneBuilder scene, SceneBuildingUtil util) {
            this.scene = scene;
            this.util = util;
        }

        public ElementLink<EntityElement> createEntity(EntityType<?> entityType, String compound) {
            return scene.world().createEntity(level -> {
                Entity entity = entityType.create(level);
                CompoundTag tag;
                
                try {
                    tag = TagParser.parseTag(compound);
                    entity.load(tag);
                } catch (CommandSyntaxException e) {
                    e.printStackTrace();
                }

                return entity;
            });
        }

        public ElementLink<WorldSectionElement> showSection(int x, int y, int z, Direction fadeInDirection) {
            return scene.world().showIndependentSection(sel(x, y, z), fadeInDirection);
        }

        public ElementLink<WorldSectionElement> showSection(int x, int y, int z, int x2, int y2, int z2, Direction fadeInDirection) {
            return scene.world().showIndependentSection(sel(x, y, z, x2, y2, z2), fadeInDirection);
        }

        public ElementLink<WorldSectionElement> showSection(int x, int y, int z) {
            return scene.world().showIndependentSectionImmediately(sel(x, y, z));
        }

        public ElementLink<WorldSectionElement> showSection(int x, int y, int z, int x2, int y2, int z2) {
            return scene.world().showIndependentSectionImmediately(sel(x, y, z, x2, y2, z2));
        }

        public void moveSection(ElementLink<WorldSectionElement> link, double x, double y, double z, int duration) {
            scene.world().moveSection(link, new Vec3(x, y, z), duration);
        }

        public void setBlock(int x, int y, int z, Supplier<Block> block) {
            setBlock(x, y, z, block, UnaryOperator.identity());
        }

        public void setBlock(int x, int y, int z, Supplier<Block> block, UnaryOperator<BlockState> state) {
            scene.world().setBlock(new BlockPos(x, y, z), state.apply(block.get().defaultBlockState()), false);
        }

        public void showOutline(String COLOR, int x, int y, int z, int duration) {
            scene.overlay().showOutline(PonderPalette.valueOf(COLOR), null, sel(x, y, z), duration);
        }

        public void showOutline(PonderPalette color, int x, int y, int z, int x2, int y2, int z2, int duration) {
            scene.overlay().showOutline(color, null, sel(x, y, z, x2, y2, z2), duration);
        }

        public void showLine(PonderPalette color, double x, double y, double z, double x2, double y2, double z2, int duration) {
            scene.overlay().showLine(color, new Vec3(x, y, z), new Vec3(x, y, z), 645);
        }

        public void emitParticles(double x, double y, double z, SimpleParticleType particle, double motionX, double motionY, double motionZ) {
            scene.effects().emitParticles(
                new Vec3(x, y, z),
                scene.effects().simpleParticleEmitter(particle, new Vec3(motionX, motionY, motionZ)),
                1, 1
            );
        }

        public Selection sel(BlockPos pos) {
            return util.select().position(pos);
        }

        public Selection sel(int x, int y, int z) {
            return util.select().position(x, y, z);
        }

        public Selection sel(int x, int y, int z, int x2, int y2, int z2) {
            return util.select().fromTo(x, y, z, x2, y2, z2);
        }
    }
}
