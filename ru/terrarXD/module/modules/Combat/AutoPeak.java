package ru.terrarXD.module.modules.Combat;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;
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
import ru.terrarXD.shit.utils.RenderUtils;

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
    FloatSetting timerPower;
    public AutoPeak() {
        super("AutoPeak", Category.Combat);
        add(timer = new BooleanSetting("Timer", true));
        add(timerPower = (FloatSetting) new FloatSetting("Timer Power", 0, 2, 1.5f, 0.1f).setVisible(()->timer.getVal()));
    }

    @EventTarget
    public void onSwing(EventSwingArm event){
        if (event.getHand() == EnumHand.MAIN_HAND){
            teleport = true;
        }
    }

    @EventTarget
    public void render3D(EventRender3D event){
        RenderUtils.drawCircle3D(pos.xCoord, pos.yCoord, pos.zCoord, 0.3f, Client.getColor());
    }


    @EventTarget
    public void onUpdate(EventUpdate event){
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
                setEnabled(false);
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
