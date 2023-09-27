package ru.terrarXD.module.modules.Render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.ColorSetting;
import ru.terrarXD.shit.utils.RenderUtils;


import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 12:41 of 31.05.2022
 */
public class Tracers extends Module {
    BooleanSetting clientf;
    ColorSetting customfcolor;

    public Tracers() {
        super("Tracers", Category.Render);
        add(clientf = new BooleanSetting("Client-Friend-Color", true));
        add(customfcolor = (ColorSetting) new ColorSetting("Friend-Color", new Color(47, 122, 229)).setVisible(()->!clientf.getVal()));

    }

    @EventTarget
    public void onRender3D(EventRender3D event){
        for (int i = 0; i < mc.world.loadedEntityList.size(); i++) {
            if (mc.world.loadedEntityList.get(i) instanceof EntityPlayer && mc.world.loadedEntityList.get(i) != mc.player && mc.player.getDistanceToEntity(mc.world.loadedEntityList.get(i)) > 300){
                mc.world.removeEntity(mc.world.loadedEntityList.get(i));
            }
        }
        RenderUtils.glColor(Color.white.getRGB());

        mc.gameSettings.viewBobbing = false;
        mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 2);
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        //GL11.glEnable(3042);
        GL11.glLineWidth(0.000001f);

        for (Entity entity : mc.world.loadedEntityList) {

            if (entity != mc.player && (entity instanceof EntityPlayer) && entity.getEntityId() != -7777){
                assert (mc.getRenderViewEntity() != null);
                mc.player.getDistanceToEntity(entity);
                double d = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) - mc.getRenderManager().viewerPosX;
                double d2 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) - mc.getRenderManager().viewerPosY;
                double d3 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) - mc.getRenderManager().viewerPosZ;
                Vec3d vec3d = new Vec3d(0.0, 0.0, 1.0);
                vec3d = vec3d.rotatePitch(-((float)Math.toRadians(mc.player.rotationPitch)));
                Vec3d vec3d2 = vec3d.rotateYaw(-((float)Math.toRadians(mc.player.rotationYaw)));

                GL11.glBegin(3);
                if(Client.friendsManager.isFriend(entity.getName())){
                    RenderUtils.glColor(clientf.getVal() ? Client.getColor() : customfcolor.getColor().getRGB());
                }else {
                    RenderUtils.glColor(Color.white.getRGB());
                }

                GL11.glVertex3d(vec3d2.xCoord, (double)mc.player.getEyeHeight() + vec3d2.yCoord, vec3d2.zCoord);
                GL11.glVertex3d(d, d2 + 0, d3);
                //GL11.glVertex3d(d, d2 + ((EntityPlayer) entity).height, d3);
                GL11.glEnd();
            }


        }
        GL11.glDisable(3042);

        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glPopMatrix();
        RenderUtils.glColor(Color.white.getRGB());

    }
}
