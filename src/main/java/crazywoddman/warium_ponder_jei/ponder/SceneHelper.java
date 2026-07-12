package crazywoddman.warium_ponder_jei.ponder;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.joml.Vector3f;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.element.InputElementBuilder;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SceneHelper {
    public final SceneBuilder builder;
    public final SceneBuildingUtil util;

    public SceneHelper(SceneBuilder scene, SceneBuildingUtil util) {
        this.builder = scene;
        this.util = util;
    }

    public Item getSubject() {
        return ForgeRegistries.ITEMS.getValue(builder.getScene().getLocation());
    }

    public void idle(int ticks) {
        this.builder.idle(ticks);
    }

    public ElementLink<EntityElement> createEntity(EntityType<?> entity, String compound) {
        return this.builder.world().createEntity(level -> {
            Entity ent = entity.create(level);
            
            try {
                ent.load(TagParser.parseTag(compound));
            } catch (CommandSyntaxException e) {
                LogUtils.getLogger().error("Failed to apply CompoundTag to Entity", e);
            }

            return ent;
        });
    }

    public ElementLink<EntityElement> createEntity(RegistryObject<? extends EntityType<?>> entity, String compound) {
        return createEntity(entity.get(), compound);
    }

    public ElementLink<WorldSectionElement> showSection(Direction fade, int x, int y, int z) {
        return this.builder.world().showIndependentSection(select(x, y, z), fade);
    }

    public ElementLink<WorldSectionElement> showSection(Direction fade, int x, int y, int z, int x2, int y2, int z2) {
        return this.builder.world().showIndependentSection(select(x, y, z, x2, y2, z2), fade);
    }

    public ElementLink<WorldSectionElement> showSection(int x, int y, int z) {
        return this.builder.world().showIndependentSectionImmediately(select(x, y, z));
    }

    public ElementLink<WorldSectionElement> showSection(int x, int y, int z, int x2, int y2, int z2) {
        return this.builder.world().showIndependentSectionImmediately(select(x, y, z, x2, y2, z2));
    }

    @SafeVarargs
    public final void hideSection(Direction fade, ElementLink<WorldSectionElement>... links) {
        for (var link : links)
            this.builder.world().hideIndependentSection(link, fade);
    }

    @SafeVarargs
    public final void moveSection(int duration, double x, double y, double z, ElementLink<WorldSectionElement>... links) {
        for (var link : links)
            this.builder.world().moveSection(link, new Vec3(x, y, z), duration);
    }

    @SafeVarargs
    public final void moveSection(double x, double y, double z, ElementLink<WorldSectionElement>... links) {
        moveSection(0, x, y, z, links);
    }

    public Section setBlock(BlockState block, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        this.builder.world().setBlock(pos, block, false);
        return new Section(select(pos));
    }

    public Section setBlock(Block block, int x, int y, int z) {
        return setBlock(block.defaultBlockState(), x, y, z);
    }

    public Section setBlock(Supplier<? extends Block> block, int x, int y, int z) {
        return setBlock(block.get(), x, y, z);
    }

    public void removeBlock(int x, int y, int z) {
        setBlock(Blocks.AIR, x, y, z);
    }

    public void removeBlocks(int x, int y, int z, int x2, int y2, int z2) {
        this.builder.world().setBlocks(select(x, y, z, x2, y2, z2), Blocks.AIR.defaultBlockState(), false);
    }

    public void modifyBlock(int x, int y, int z, UnaryOperator<BlockState> operation) {
        this.builder.world().modifyBlock(new BlockPos(x, y, z), operation, false);
    }

    public void showOutline(PonderPalette color, int duration, int x, int y, int z) {
        this.builder.overlay().showOutline(color, null, select(x, y, z), duration);
    }

    public void showOutline(PonderPalette color, int duration, int x, int y, int z, int x2, int y2, int z2) {
        this.builder.overlay().showOutline(color, null, select(x, y, z, x2, y2, z2), duration); 
    }

    public void showLine(PonderPalette color, int duration, double x, double y, double z, double x2, double y2, double z2) {
        this.builder.overlay().showLine(color, new Vec3(x, y, z), new Vec3(x2, y2, z2), duration);
    }

    public void emitParticles(SimpleParticleType particle, double x, double y, double z, double motionX, double motionY, double motionZ) {
        this.builder.effects().emitParticles(
            new Vec3(x, y, z),
            this.builder.effects().simpleParticleEmitter(particle, new Vec3(motionX, motionY, motionZ)),
            1, 1
        );
    }

    public InputElementBuilder showControls(Pointing pointing, int duration, double x, double y, double z) {
        return this.builder.overlay().showControls(new Vec3(x, y, z), pointing, duration);
    }

    public InputElementBuilder showControls(Item item, Pointing pointing, int duration, double x, double y, double z) {
        return showControls(pointing, duration, x, y, z).withItem(new ItemStack(item));
    }

    public InputElementBuilder showControls(Supplier<Item> item, Pointing pointing, int duration, double x, double y, double z) {
        return showControls(item.get(), pointing, duration, x, y, z);
    }

    public TextElementBuilder showText(String text, int duration, Object... args) {
        return this.builder.overlay().showText(duration).text(text, args);
    }

    public TextElementBuilder showTextAt(String text, int duration, double x, double y, double z, Object... args) {
        return showText(text, duration, args).pointAt(new Vec3(x, y, z)).placeNearTarget();
    }

    public void emitParticles(Supplier<SimpleParticleType> particle, double x, double y, double z, double motionX, double motionY, double motionZ) {
        emitParticles(particle.get(), x, y, z, motionX, motionY, motionZ);
    }

    public void toggleRedstone(int x, int y, int z) {
        modifyBlock(x, y, z, state -> state.getOptionalValue(BlockStateProperties.POWERED).map(powered -> {
            if (!powered) {
                for (int i = 0; i < 5; i++) {
                    this.builder.getScene().getWorld().addParticle(
                        new DustParticleOptions(new Vector3f(1, 0, 0), 1),
                        x + Math.random(),
                        y + Math.random(),
                        z + Math.random(),
                        0, 0, 0
                    );
                }
            }

            return state.setValue(BlockStateProperties.POWERED, !powered);
        }).orElse(state));
    }

    public Selection select(BlockPos pos) {
        return this.util.select().position(pos);
    }

    public Selection select(int x, int y, int z) {
        return this.util.select().position(x, y, z);
    }

    public Selection select(int x, int y, int z, int x2, int y2, int z2) {
        return this.util.select().fromTo(x, y, z, x2, y2, z2);
    }

    public class Section {
        public final Selection selection;

        public Section(Selection selection) {
            this.selection = selection;
        }

        public ElementLink<WorldSectionElement> show() {
            return SceneHelper.this.builder.world().showIndependentSectionImmediately(this.selection);
        }

        public ElementLink<WorldSectionElement> show(Direction fade) {
            return SceneHelper.this.builder.world().showIndependentSection(this.selection, fade);
        }
    }
}
