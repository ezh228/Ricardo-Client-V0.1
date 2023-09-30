package ru.terrarXD.shit.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BossInfo;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;

import java.awt.*;
import java.util.ArrayList;

import static net.minecraft.client.renderer.GlStateManager.resetColor;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ZERO;

/**
 * @author zTerrarxd_
 * @since 13:30 of 29.10.2022
 */
public class RenderUtils {
    private static final Frustum frustrum = new Frustum();

    public static void drawThemeRect(float x,float y,float x2,float y2){
        int colorFon = new Color(24, 24, 24, 25).getRGB();

        GaussianBlur.renderBlur(20, ()->RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, 5, 0, colorFon, colorFon, colorFon, colorFon, true ,true, false));
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, 5, 0, colorFon, colorFon, colorFon, colorFon, true ,true, false);
        //RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, 5, 0, colorFon, colorFon, colorFon, colorFon, false ,true, false);


    }

    public static void renderEntityFilledBoundingBox(final Entity entity, final Color color, final float alpha){
        final RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        final Vec3d entityPos = Utils.interpolateEntity(entity);
        final AxisAlignedBB bb = new AxisAlignedBB(entityPos.xCoord - entity.width / 2.0f, entityPos.yCoord, entityPos.zCoord - entity.width / 2.0f, entityPos.xCoord + entity.width / 2.0f, entityPos.yCoord + entity.height, entityPos.zCoord + entity.width / 2.0f).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.renderFilledBox(bb, color.getGreen(), color.getRed(), color.getBlue(), alpha);
    }


    public static void renderEntityFilledBoundingBox(final Entity entity, final float red, final float green, final float blue, final float alpha) {
        final RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        final Vec3d entityPos = Utils.interpolateEntity(entity);
        final AxisAlignedBB bb = new AxisAlignedBB(entityPos.xCoord - entity.width / 2.0f, entityPos.yCoord, entityPos.zCoord - entity.width / 2.0f, entityPos.xCoord + entity.width / 2.0f, entityPos.yCoord + entity.height, entityPos.zCoord + entity.width / 2.0f).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.renderFilledBox(bb, red, green, blue, alpha);
    }

    public static void renderEntityBoundingBox(final Entity entity, final Color color, final float alpha){
        final RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        final Vec3d entityPos = Utils.interpolateEntity(entity);
        final AxisAlignedBB bb = new AxisAlignedBB(entityPos.xCoord - entity.width / 2.0f, entityPos.yCoord, entityPos.zCoord - entity.width / 2.0f, entityPos.xCoord + entity.width / 2.0f, entityPos.yCoord + entity.height, entityPos.zCoord + entity.width / 2.0f).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.drawSelectionBoundingBox(bb, color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }


    public static void renderEntityBoundingBox(final Entity entity, final float red, final float green, final float blue, final float alpha) {
        final RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        final Vec3d entityPos = Utils.interpolateEntity(entity);
        final AxisAlignedBB bb = new AxisAlignedBB(entityPos.xCoord - entity.width / 2.0f, entityPos.yCoord, entityPos.zCoord - entity.width / 2.0f, entityPos.xCoord + entity.width / 2.0f, entityPos.yCoord + entity.height, entityPos.zCoord + entity.width / 2.0f).offset(-rm.viewerPosX, -rm.viewerPosY, -rm.viewerPosZ);
        RenderGlobal.drawSelectionBoundingBox(bb, red, green, blue, alpha);
    }







    public static double interpolate(double current, double old, double scale) {
        return old + (current - old) * scale;
    }
    public static boolean isInViewFrustrum(Entity entity) {
        return isInViewFrustrum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
    private static boolean isInViewFrustrum(AxisAlignedBB bb) {
        Entity current = Minecraft.getMinecraft().getRenderViewEntity();
        frustrum.setPosition(current.posX, current.posY, current.posZ);
        return frustrum.isBoundingBoxInFrustum(bb);
    }


    public static void drawCircle3D(double posX, double posY, double posZ, float radius, int color) {

            double[] position = {
                    posX - Minecraft.getMinecraft().getRenderManager().viewerPosX,
                    posY - Minecraft.getMinecraft().getRenderManager().viewerPosY,
                    posZ - Minecraft.getMinecraft().getRenderManager().viewerPosZ
            };
            GL11.glPushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableCull();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.disableDepth();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(4);
            GL11.glPushMatrix();

            GlStateManager.translate(position[0], position[1], position[2]);

            GL11.glBegin(2);
            RenderUtils.glColor(color);

            for(int i = 0; i <= 360; i++) {
                GL11.glVertex3d(Math.sin(i * Math.PI / 180) * radius, 0, Math.cos(i * Math.PI / 180) * radius);
            }
            GL11.glEnd();
            /*
            GL11.glBegin(2);
            for(int i = 0; i <= 360; i++) {
                int[] colors = Client.instance.getClientColors((float) (i * 1.1f), 100);
                RenderUtils.glColor(colors[0]);
                GL11.glVertex3d(Math.sin(i * Math.PI / 180) * .3f * anim.getAnim2(), 0, Math.cos(i * Math.PI / 180) * .3f * anim.getAnim2());
            }
            GL11.glEnd();

             */

            GL11.glPopMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.enableDepth();
            GlStateManager.enableTexture2D();
            GlStateManager.enableCull();
            GlStateManager.disableBlend();
            GL11.glPopMatrix();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderUtils.glColor(-1);
            GL11.glLineWidth(1);

    }

    public static void drawCircle(float x, float y, float radius, int color)
    {
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        boolean flag = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean flag1 = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
        boolean flag2 = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);

        if (!flag)
        {
            GL11.glEnable(GL11.GL_BLEND);
        }

        if (!flag1)
        {
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
        }

        if (flag2)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
        }
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);


        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_POLYGON);

        for (int i = 0; i <= 360; ++i)
        {
            GL11.glVertex2d((double)x + Math.sin((double)i * Math.PI / 180.0D) * (double)radius, (double)y + Math.cos((double)i * Math.PI / 180.0D) * (double)radius);
        }

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);

        if (flag2)
        {
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (!flag1)
        {
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }

        if (!flag)
        {
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
    public static void scissor(double x, double y, double width, double height) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);
        final double scale = sr.getScaleFactor();
        double finalHeight = height * scale;
        double finalY = (sr.getScaledHeight() - y) * scale;
        double finalX = x * scale;
        double finalWidth = width * scale;
        GL11.glScissor((int) finalX, (int) (finalY - finalHeight), (int) finalWidth, (int) finalHeight);
    }
    public static void customScaledObject2D(float oXpos,float oYpos,float oWidth,float oHeight,float oScale) {
        GL11.glTranslated(oWidth/2,oHeight/2,1);
        GL11.glTranslated(-oXpos * oScale + oXpos + oWidth/2 * -oScale,-oYpos * oScale + oYpos + oHeight/2 * -oScale,1);
        GL11.glScaled(oScale,oScale,0);
    }

    public static void drawCroneShadow(final double x, final double y, final int startAngle, final int endAngle,final float radius,final float shadowSize, int color,int endColor, boolean bloom) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(5);
        for (int i = startAngle; i <= endAngle; i+=6) {
            ColorUtils.glColor(color);
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180) * radius, y + Math.cos(i * Math.PI / 180) * radius);
            ColorUtils.glColor(endColor);
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180) * (radius+shadowSize), y + Math.cos(i * Math.PI / 180) * (radius+shadowSize));
        }
        GL11.glEnd();
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(GL11.GL_FLAT);
    }

    public static void drawRoundedRect(float x,float y,float x2,float y2,float round, int color){
        drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, round, 1, color ,color, color, color, false, true, false);
    }


    public static void customScaledObject2D(float oXpos,float oYpos,float oWidth,float oHeight,float oScale, float oScale2) {
        GL11.glTranslated(oWidth/2,oHeight/2,1);
        GL11.glTranslated(-oXpos * oScale + oXpos + oWidth/2 * -oScale,-oYpos * oScale + oYpos + oHeight/2 * -oScale2,1);
        GL11.glScaled(oScale,oScale2,0);
    }
    public static void drawRoundedFullGradientShadow(float x,float y,float x2,float y2,float round,float shadowSize,int color,int color2,int color3,int color4,boolean bloom) {
        x+=round;x2-=round;y+=round;y2-=round;
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE,GlStateManager.DestFactor.ZERO);

        //left round
        drawCroneShadow(x,y,-180,-90,round,shadowSize,color,bloom ? 0 : ColorUtils.swapAlpha(color,0),bloom);
        //up rect
        drawFullGradientRectPro(x, y - round - shadowSize, x2, y - round, color, color2, bloom ? 0 : ColorUtils.swapAlpha(color2,0), bloom ? 0 : ColorUtils.swapAlpha(color,0), bloom);

        //right round
        drawCroneShadow(x2,y,90,180,round,shadowSize,color2,bloom ? 0 : ColorUtils.swapAlpha(color2,0),bloom);
        //right rect
        drawFullGradientRectPro(x2 + round, y, x2 + round + shadowSize, y2, color3, bloom ? 0 : ColorUtils.swapAlpha(color3,0), bloom ? 0 : ColorUtils.swapAlpha(color2,0), color2, bloom);

        //right down round
        drawCroneShadow(x2,y2,0,90,round,shadowSize,color3,bloom ? 0 : ColorUtils.swapAlpha(color3,0),bloom);
        //down rect
        drawFullGradientRectPro(x, y2 + round, x2, y2 + round + shadowSize, bloom ? 0 : ColorUtils.swapAlpha(color4,0), bloom ? 0 : ColorUtils.swapAlpha(color3,0), color3, color4, bloom);

        //left down round
        drawCroneShadow(x,y2,-90,0,round,shadowSize,color4,bloom ? 0 : ColorUtils.swapAlpha(color4,0),bloom);
        //left rect
        drawFullGradientRectPro(x - round - shadowSize, y, x - round, y2, bloom ? 0 : ColorUtils.swapAlpha(color4,0), color4, color, bloom ? 0 : ColorUtils.swapAlpha(color,0), bloom);

        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,GlStateManager.DestFactor.ZERO);

    }




    public static void drawFullGradientRectPro(float x, float y, float x2, float y2, int color, int color2, int color3, int color4,boolean blend) {

        GlStateManager.enableBlend();

        if (blend) {
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        GlStateManager.disableTexture2D();
        GL11.glShadeModel(GL_SMOOTH);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBegin(GL11.GL_QUADS);
        {
            ColorUtils.glColor(color);
            GL11.glVertex2f(x, y2);
            ColorUtils.glColor(color2);
            GL11.glVertex2f(x2, y2);
            ColorUtils.glColor(color3);
            GL11.glVertex2f(x2, y);
            ColorUtils.glColor(color4);
            GL11.glVertex2f(x, y);
        }
        GL11.glEnd();
        if (blend) {
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        }
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glShadeModel(GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        resetColor();
    }


    public static void drawImage(ResourceLocation image, int x, int y, int width, int height, int color) {
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        ColorUtils.glColor(color);

        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }
    public static void drawImage(String name, int x, int y, int width, int height, int color) {
        ResourceLocation image = new ResourceLocation("client/img/" + name + ".png");
        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);
        ColorUtils.glColor(color);

        Minecraft.getMinecraft().getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);

        GlStateManager.disableBlend();
        GlStateManager.enableDepth();
    }

    public static void drawShadowRect(float x, float y, float x2, float y2, int color, boolean bloom){
        drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, 0, 5, color ,color, color, color, bloom, true, true);
    }

    public static void drawGradientRect(final double startX, final double startY, final double endX, final double endY, final boolean sideways, final int startColor, final int endColor)
    {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(GL_SMOOTH);

        ColorUtils.glColor(startColor);

        glBegin(GL_QUADS);

        if (sideways) {
            glVertex2d(startX, startY);
            glVertex2d(startX, endY);
            ColorUtils.glColor(endColor);
            glVertex2d(endX, endY);
            glVertex2d(endX, startY);
        } else {
            glVertex2d(startX, startY);
            ColorUtils.glColor(endColor);
            glVertex2d(startX, endY);
            glVertex2d(endX, endY);
            ColorUtils.glColor(startColor);
            glVertex2d(endX, startY);
        }

        glEnd();

        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(GL_FLAT);
    }

    public static void drawRect(final float startX, final float startY, final float endX, final float endY, final int color)
    {
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, startY);
        GL11.glVertex2d(endX, endY);
        GL11.glVertex2d(startX, endY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopAttrib();
        glPopMatrix();
    }
    public static void blockEspFrame(BlockPos blockPos, double red, double green, double blue) {
        double d = blockPos.getX();
        Minecraft.getMinecraft().getRenderManager();
        double x = d - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        double d2 = blockPos.getY();
        Minecraft.getMinecraft().getRenderManager();
        double y = d2 - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        double d3 = blockPos.getZ();
        Minecraft.getMinecraft().getRenderManager();
        double z = d3 - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(1.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4d(red, green, blue, 1);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glColor4f(1, 1, 1, 1);
    }

    public static void drawSelectionBoundingBox(AxisAlignedBB boundingBox) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }
    public static void glColor(final int hex)
    {
        final float alpha = (hex >> 24 & 0xFF) / 255F;
        final float red = (hex >> 16 & 0xFF) / 255F;
        final float green = (hex >> 8 & 0xFF) / 255F;
        final float blue = (hex & 0xFF) / 255F;

        GlStateManager.color(red, green, blue, alpha);
    }
    public static void renderItem(ItemStack itemStack, float x, float y) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        //Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }
    public static void renderItemOver(ItemStack itemStack, float x, float y) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }

    public static void drawRect(final double startX, final double startY, final double endX, final double endY, final int color)
    {
        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        glPushMatrix();
        GL11.glPushAttrib(1048575);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(startX, startY);
        GL11.glVertex2d(endX, startY);
        GL11.glVertex2d(endX, endY);
        GL11.glVertex2d(startX, endY);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopAttrib();
        glPopMatrix();
    }

    public static void drawShadowRect(float x, float y, float x2, float y2, float shadow, int color, boolean bloom, boolean rect){
        drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, 0, shadow, color ,color, color, color, bloom, rect, true);
    }

    public static void drawShadowRect(float x, float y, float x2, float y2, float shadow, int color, boolean bloom){
        drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x2, y2, 0, shadow, color ,color, color, color, bloom, true, true);
    }
    public static void drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(float x,float y,float x2,float y2,float round,float shadowSize,int color,int color2,int color3,int color4,boolean bloom,boolean rect,boolean shadow) {
        if (shadow)
            drawRoundedFullGradientShadow(x,y,x2,y2,round,shadowSize,color,color2,color3,color4,bloom);
        if (rect)
            drawRoundedFullGradientRectPro(x, y, x2, y2, round*2, color, color2,color3,color4, bloom);
    }

    public static void drawRoundedFullGradientRectPro(float x, float y, float x2, float y2, float round, int color,int color2,int color3,int color4,boolean bloom) {
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
        drawFullGradientRectPro(x+round/2, y+round/2, x2-round/2, y2 - round/2, color4,color3,color2,color,bloom);
        drawPolygonParts(x+round/2, y+round/2, round/2, 0, color, color,bloom);
        drawPolygonParts(x + round/2, y2 - round/2, round/2, 5, color4, color4,bloom);
        drawPolygonParts(x2 - round/2, y + round/2, round/2, 7, color2, color2,bloom);
        drawPolygonParts(x2 - round/2, y2 - round/2, round/2, 6, color3, color3,bloom);
        drawBloomedFullGradientRect(x,y+round/2,x+round/2,y2-round/2,color4,color4,color,color,bloom);
        drawBloomedFullGradientRect(x+round/2,y,x2-round/2,y+round/2,color,color2,color2,color,bloom);
        drawBloomedFullGradientRect(x2-round/2,y+round/2,x2,y2-round/2,color3,color3,color2,color2,bloom);
        drawBloomedFullGradientRect(x+round/2,y2-round/2,x2-round/2,y2,color4,color3,color3,color4,bloom);
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
    }

    public static void drawPolygonParts(double x, double y, float radius, int part, int color, int endcolor,boolean bloom) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha1 = (float) (endcolor >> 24 & 255) / 255.0F;
        float red1 = (float) (endcolor >> 16 & 255) / 255.0F;
        float green1 = (float) (endcolor >> 8 & 255) / 255.0F;
        float blue1 = (float) (endcolor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        if (bloom) {
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
        } else {
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
        }
        GlStateManager.shadeModel( 7425 );
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0).color(red, green, blue, alpha).endVertex();
        final double TWICE_PI = Math.PI * 2;
        for (int i = part * 90; i <= part * 90 + 90; i++) {
            double angle = (TWICE_PI * i / 360) + Math.toRadians(180);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0)
                    .color(red1, green1, blue1, alpha1).endVertex();
        }
        tessellator.draw();
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawBloomedFullGradientRect(float xpos,float ypos,float x2pos,float y2pos,int color1,int color2,int color3,int color4,boolean bloom) {
        float x = xpos;
        float y = ypos;
        float w = x2pos - xpos;
        float h = y2pos - ypos;
        int colorid1 = color1;
        int colorid2 = color2;
        int colorid3 = color3;
        int colorid4 = color4;

        RenderUtils.drawFullGradientRectPro(x,y,x + w,y + h, color1,color2,color3,color4,bloom);

    }

    public static void drawPolygonParts(double x, double y, int radius, int part, int color, int endcolor) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        float alpha1 = (float) (endcolor >> 24 & 255) / 255.0F;
        float red1 = (float) (endcolor >> 16 & 255) / 255.0F;
        float green1 = (float) (endcolor >> 8 & 255) / 255.0F;
        float blue1 = (float) (endcolor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        // GlStateManager.shadeModel( 7425 );
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0).color(red, green, blue, alpha).endVertex();
        final double TWICE_PI = Math.PI * 2;
        for (int i = part * 90; i <= part * 90 + 90; i++) {
            double angle = (TWICE_PI * i / 360) + Math.toRadians(180);
            bufferbuilder.pos(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0)
                    .color(red1, green1, blue1, alpha1).endVertex();
        }
        tessellator.draw();
        // GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void rroneShadow(final double x, final double y, final int startAngle, final int endAngle,final float radius,final float shadowSize, int color,int endColor, boolean bloom) {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GL11.glBegin(5);
        for (int i = startAngle; i <= endAngle; i+=6) {
            ColorUtils.glColor(color);
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180) * radius, y + Math.cos(i * Math.PI / 180) * radius);
            ColorUtils.glColor(endColor);
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180) * (radius+shadowSize), y + Math.cos(i * Math.PI / 180) * (radius+shadowSize));
        }
        GL11.glEnd();
        if (bloom)
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(GL11.GL_FLAT);
    }


    public static void drawPolygon(ArrayList<Vec2f> pos, boolean colorIn, final int color){
        if (colorIn){
            float f = (float)(color >> 24 & 255) / 255.0F;
            float f1 = (float)(color >> 16 & 255) / 255.0F;
            float f2 = (float)(color >> 8 & 255) / 255.0F;
            float f3 = (float)(color & 255) / 255.0F;
            glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(f1, f2, f3, f);
            glBegin(GL_POLYGON);
            for(Vec2f vec2f : pos){
                glVertex2f(vec2f.x, vec2f.y);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopAttrib();
            glPopMatrix();
        }else {
            float f = (float)(color >> 24 & 255) / 255.0F;
            float f1 = (float)(color >> 16 & 255) / 255.0F;
            float f2 = (float)(color >> 8 & 255) / 255.0F;
            float f3 = (float)(color & 255) / 255.0F;
            glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(f1, f2, f3, f);
            glLineWidth(2);
            glBegin(3);
            for(Vec2f vec2f : pos){
                glVertex2f(vec2f.x, vec2f.y);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopAttrib();
            glPopMatrix();
        }
        GL11.glColor4f(1, 1, 1, 1);

    }


    public static void drawPolygonWithShadow(ArrayList<Vec2f> pos, boolean colorIn, final int color){
        float shadow = 2;
        if (colorIn){
            float f = (float)(color >> 24 & 255) / 255.0F;
            float f1 = (float)(color >> 16 & 255) / 255.0F;
            float f2 = (float)(color >> 8 & 255) / 255.0F;
            float f3 = (float)(color & 255) / 255.0F;
            glPushMatrix();
            GL11.glPushAttrib(1048575);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(f1, f2, f3, f);
            glBegin(GL_POLYGON);
            for(Vec2f vec2f : pos){
                glVertex2f(vec2f.x, vec2f.y);
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopAttrib();
            glPopMatrix();
        }else {
            float f = (float)(color >> 24 & 255) / 255.0F;
            float f1 = (float)(color >> 16 & 255) / 255.0F;
            float f2 = (float)(color >> 8 & 255) / 255.0F;
            float f3 = (float)(color & 255) / 255.0F;
            glPushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.shadeModel(GL_SMOOTH);
            GL11.glColor4f(f1, f2, f3, f);
            glLineWidth(2);
            glBegin(3);
            for(Vec2f vec2f : pos){
                GL11.glColor4f(f1, f2, f3, f);
                glVertex2f(vec2f.x, vec2f.y);

            }
            GL11.glEnd();
            GlStateManager.disableBlend();
            GlStateManager.enableTexture2D();
            GlStateManager.enableAlpha();
            GlStateManager.shadeModel(GL_FLAT);
            glPopMatrix();
        }
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glColor4f(1, 1, 1, 1);

    }

}
