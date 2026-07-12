package crazywoddman.warium_ponder_jei.ponder.scenes;

import crazywoddman.warium_ponder_jei.ponder.AbstractScene;
import crazywoddman.warium_ponder_jei.ponder.SceneHelper;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.mcreator.crustychunks.init.CrustyChunksModBlocks;
import net.mcreator.crustychunks.init.CrustyChunksModEntities;
import net.mcreator.crustychunks.init.CrustyChunksModItems;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class MortarScene extends AbstractScene {

    @Override
    @SuppressWarnings("unchecked")
    protected RegistryObject<Item>[] getItems() {
        return new RegistryObject[]{CrustyChunksModItems.MORTAR};
    }

    @Override
    protected String getName(SceneHelper helper) {
        return "Mortar";
    }

    @Override
    protected void showScene(SceneHelper helper) {
        helper.setBlock(CrustyChunksModBlocks.MORTAR, 2, 1, 2).show(Direction.DOWN);

        helper.idle(10);

        helper.showControls(CrustyChunksModItems.AIMER, Pointing.DOWN, 60, 2.5, 2, 2.5).rightClick();
        helper.showTextAt("Right-click with Weapon Aimer in second hand to attach it", 60, 2.5, 1.5, 2.5)
            .attachKeyFrame();
        
        helper.idle(80);
        
        helper.showControls(CrustyChunksModItems.AIMER, Pointing.DOWN, 80, 0.5, 2, 2.5).rightClick();
        helper.showTextAt("LEFT/RIGHT arrow for yaw UP/DOWN arrow for pitch", 80, -0.1, 3.7, 2.5);
        
        helper.idle(100);
        
        helper.showText("Pitch is responsible for the vertical angle of the projectile trajectory and therefore the coverage distance", 100)
            .attachKeyFrame();
        
        helper.idle(120);
        
        helper.showText("Yaw is responsible for the left/right rotation", 80)
            .attachKeyFrame();
        
        helper.idle(100);
        
        helper.showText("Pitch ranges from -21.5 (30-100 blocks)", 60)
            .attachKeyFrame();
        helper.showLine(PonderPalette.BLUE, 725, 2.5, 2.001, 2.2, 2.5, 2.59, 0.7);
        
        helper.idle(80);
        
        helper.showText("To 44.5 (60-120 blocks)", 60)
            .attachKeyFrame();
        
        helper.showLine(PonderPalette.RED, 645, 2.5, 2.0, 2.2, 2.5, 3.5, 2.175);
        
        helper.idle(80);
        
        helper.showText("Default pitch is 0 (150-210 blocks)", 80)
            .attachKeyFrame();
        
        helper.showLine(PonderPalette.GREEN, 565, 2.5, 2.002, 2.2, 2.5, 3.5, 0.7);
        
        helper.idle(100);
        
        helper.showText("Default pitch has the longest range. Both above and below 0 pitch decreases the distance", 140)
            .attachKeyFrame();
        
        helper.idle(160);
        
        helper.showText("Lower pitch results in a less accurate shot, but projectile reaches the final destination faster", 140)
            .attachKeyFrame();
        
        helper.idle(160);
        
        helper.showText("Higher pitch shot is more accurate, but projectile takes longer to reach the end point",  140)
            .attachKeyFrame();
        
        helper.idle(160);
        
        helper.builder.addKeyframe();
        
        helper.showControls(CrustyChunksModItems.MORTAR_SHELL, Pointing.DOWN, 20, 2.5, 2, 2.5).rightClick();
        
        helper.idle(20);
        
        helper.createEntity(
            CrustyChunksModEntities.MORTAR_PROJECTILE,
            "{crit: 1b, Motion: [0.0d, 3.0d, -1.5d], Pos: [2.5d, 2.1d, 2.1d]}"
        );
        
        for (int i = 0; i < 8; i++) {
            helper.emitParticles(
                ParticleTypes.CAMPFIRE_COSY_SMOKE,
                2, 3, 1.2,
                Math.random() / 50 - 0.01,
                Math.random() / 50 - 0.01,
                Math.random() / 50 - 0.01
            );
        }
    }
}
