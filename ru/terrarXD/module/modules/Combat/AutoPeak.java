package ru.terrarXD.module.modules.Combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.event.events.EventSwingArm;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.RenderUtils;
import ru.terrarXD.shit.utils.Utils;

import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 17:16 of 07.06.2023
 */
public class AutoPeak extends Module {
    public Vec3d pos;
    public double motionX;
    public double motionZ;
    boolean teleport = false;


    BooleanSetting timer;
    ModeSetting mode;
    FloatSetting timerPower;
    FloatSetting radius;

    public AutoPeak() {
        super("AutoPeak", Category.Combat);
        add(timer = new BooleanSetting("Timer", true));
        add(timerPower = (FloatSetting) new FloatSetting("Timer Power", 0, 2, 1.5f, 0.1f).setVisible(()->timer.getVal()));
        ArrayList<String> modes = new ArrayList<>();
        modes.add("Radius");
        modes.add("Swing");
        add(mode = new ModeSetting("Mode", modes, "Swing"));
        add(radius = (FloatSetting) new FloatSetting("Radius", 0, 5, 1.5f, 0.1f).setVisible(()->mode.getVal().equals("Radius")));

    }

    @EventTarget
    public void onSwing(EventSwingArm event){
        if (mode.getVal().equals("Swing")){
            if (event.getHand() == EnumHand.MAIN_HAND){
                teleport = true;
            }
        }
    }




    @EventTarget
    public void render3D(EventRender3D event){
        RenderUtils.drawCircle3D(pos.xCoord, pos.yCoord, pos.zCoord, mode.getVal().equals("Radius") ? radius.getVal() : 0.3f, Client.getColor());
        /*
        for (int x = -5; x < 5; x++) {
            for (int z = -5; z < 5; z++) {
                BlockPos pos = new BlockPos(mc.player.posX+x, mc.player.posY, mc.player.posZ+z);
                BlockPos pos_up = new BlockPos(mc.player.posX+x, mc.player.posY+1, mc.player.posZ+z);
                if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR && mc.world.getBlockState(pos_up).getBlock() != Blocks.AIR){
                    RenderUtils.blockEspFrame(pos, 1, 0, 0);
                }
            }
        }

         */



    }


    @EventTarget
    public void onUpdate(EventUpdate event){

        if (mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord) > radius.getVal() && mode.getVal().equals("Radius")){
            teleport = true;
        }
        if (teleport){
            if (timer.getVal()){
                mc.timer.field_194147_b = timerPower.getVal();
            }
            double distance = mc.player.getDistance(pos.xCoord, pos.yCoord, pos.zCoord);
            if (distance != 0){
                mc.player.motionX = -(mc.player.posX - pos.xCoord) / distance * 0.3f;
                mc.player.motionZ = -(mc.player.posZ - pos.zCoord) / distance * 0.3f;
            }

            if (distance <= 0.3f){
                //setEnabled(false);
                //pos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
                teleport = false;
            }
        }

    }



    @Override
    public void onEnable() {
        super.onEnable();
        pos = new Vec3d(mc.player.posX, mc.player.posY, mc.player.posZ);
        teleport = false;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        teleport = false;
    }
}
