package ru.terrarXD.module.modules.Render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventRender3D;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.ColorSetting;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;
import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zTerrarxd_
 * @since 0:16 of 23.04.2023
 */
public class ESP extends Module {

    ModeSetting mode;

    BooleanSetting bots;
    BooleanSetting showHealth;
    BooleanSetting armor;
    BooleanSetting clientColor;
    ColorSetting customColor;

    private final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
    private final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
    public ESP() {
        super("ESP", Category.Render);
        ArrayList<String> modes = new ArrayList<>();
        modes.add("2D");
        modes.add("3D");
        add(mode = new ModeSetting("Mode", modes, "2D"));
        add(bots = new BooleanSetting("Bots", true));
        add(showHealth = new BooleanSetting("Show-Health", false));
        add(armor = new BooleanSetting("Show-Item", true));
        add(clientColor = new BooleanSetting("Client-Color", true));
        add(customColor = (ColorSetting) new ColorSetting("Color", new Color(47, 122, 229)).setVisible(()->!clientColor.getVal()));

    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        if (!mode.getVal().equals("2D")){
            return;
        }
        GL11.glPushMatrix();
        float partialTicks = mc.getRenderPartialTicks();
        int scaleFactor = ScaledResolution.getScaleFactor();
        double scaling = scaleFactor / Math.pow(scaleFactor, 2.0D);
        GL11.glScaled(scaling, scaling, scaling);
        Color onecolor = new Color(0, 0, 0);
        Color c = new Color(onecolor.getRed(), onecolor.getGreen(), onecolor.getBlue(), 255);
        int color =  c.getRGB();
        float scale = 1F;
        float upscale = 1.0F / scale;
        RenderManager renderMng = mc.getRenderManager();
        EntityRenderer entityRenderer = mc.entityRenderer;

        for (Entity entity : mc.world.loadedEntityList) {
            if (isValid(entity) && RenderUtils.isInViewFrustrum(entity) && (entity == mc.player && mc.gameSettings.thirdPersonView != 0 || entity != mc.player)) {

                double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
                double y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
                double z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
                double width = entity.width / 1.5D;
                double height = entity.height + ((entity.isSneaking() || (entity == mc.player && mc.player.isSneaking()) ? -0.3D : 0.2D));
                AxisAlignedBB aabb = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
                Vector3d[] vectors = new Vector3d[]{new Vector3d(aabb.minX, aabb.minY, aabb.minZ), new Vector3d(aabb.minX, aabb.maxY, aabb.minZ), new Vector3d(aabb.maxX, aabb.minY, aabb.minZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.minZ),
                        new Vector3d(aabb.minX, aabb.minY, aabb.maxZ), new Vector3d(aabb.minX, aabb.maxY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.minY, aabb.maxZ), new Vector3d(aabb.maxX, aabb.maxY, aabb.maxZ)};
                entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = project2D(scaleFactor, vector.x - renderMng.viewerPosX, vector.y - renderMng.viewerPosY, vector.z - renderMng.viewerPosZ);
                    if (vector != null && vector.z >= 0.0D && vector.z < 1.0D) {
                        if (position == null)
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0D);
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }

                if (position != null) {
                    entityRenderer.setupOverlayRendering();
                    double posX = position.x;
                    double posY = position.y;
                    double endPosX = position.z;
                    double endPosY = position.w;

                    //
                    int col = clientColor.getVal() ? Client.getColor() : customColor.getColor().getRGB();
                        RenderUtils.drawRect(posX, posY, posX + 1, endPosY, col);
                        RenderUtils.drawRect(posX, endPosY, endPosX, endPosY + 1, col);
                        RenderUtils.drawRect(endPosX-1, posY, endPosX, endPosY, col);
                        RenderUtils.drawRect(posX, posY, endPosX, posY + 1, col);



                    boolean living = entity instanceof EntityLivingBase;

                    EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                    float hp2 = entityLivingBase.getHealth();
                    float maxHealth = entityLivingBase.getMaxHealth();
                    double hpPercentage = (hp2 / maxHealth);
                    double hpHeight2 = (endPosY - posY) * hpPercentage;

                    if (entityLivingBase != null && hp2 > 0 && living) {
                        if (showHealth.getVal()){
                            RenderUtils.drawRect(posX - 3.5F, posY, posX - 2.0F, endPosY, color);
                            RenderUtils.drawRect(posX - 3.5F, posY - ((posY - endPosY) / maxHealth) * (maxHealth - hp2), posX - 2.0F, endPosY, ColorUtils.getHealthColor(entityLivingBase).getRGB());
                        }

                        //Item
                        if (armor.getVal()){
                            if (!(entityLivingBase.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemAir)){
                                Fonts.main_18.drawCenteredString(entityLivingBase.getHeldItem(EnumHand.MAIN_HAND).getDisplayName(), posX + (endPosX - posX) / 2, endPosY + 5 - Fonts.main_18.getHeight() / 2, -1);

                            }

                        }

                    }
                }
            }
        }
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GlStateManager.enableBlend();
        entityRenderer.setupOverlayRendering();
    }

    @EventTarget
    public void onRender3D(EventRender3D event){
        if (!mode.getVal().equals("3D")){
            return;
        }
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableDepth();
        Color col = new Color(clientColor.getVal() ? Client.getColor() : customColor.getColor().getRGB());
        for (final Entity entity : Minecraft.getMinecraft().world.loadedEntityList) {
            if (isValid(entity) && RenderUtils.isInViewFrustrum(entity) && (entity == mc.player && mc.gameSettings.thirdPersonView != 0 || entity != mc.player)) {

                if (Client.friendsManager.isFriend(entity.getName())) {
                    RenderUtils.renderEntityBoundingBox(entity, col, 1);
                    RenderUtils.renderEntityFilledBoundingBox(entity, col, 1 / 3.0f);
                }
                else {
                    RenderUtils.renderEntityBoundingBox(entity, new Color(-1), 1);
                    RenderUtils.renderEntityFilledBoundingBox(entity, new Color(-1), 1 / 3.0f);
                }
            }

        }
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
    }


    private Vector3d project2D(int scaleFactor, double x, double y, double z) {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, this.modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, this.projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, this.viewport);
        if (GLU.gluProject((float) x, (float) y, (float) z, this.modelview, this.projection, this.viewport, this.vector))
            return new Vector3d((this.vector.get(0) / scaleFactor), ((Display.getHeight() - this.vector.get(1)) / scaleFactor), this.vector.get(2));
        return null;
    }

    public boolean isValid(Entity entity){
        if (entity instanceof EntityLivingBase){
            if (entity.getEntityId() == -7777){
                return false;
            }
            if (bots.getVal() && entity instanceof EntityZombie){
                return true;
            }
            if (entity instanceof EntityPlayer && entity != mc.player){
                return true;
            }

        }
        return false;
    }


}
