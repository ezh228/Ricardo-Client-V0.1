package ru.terrarXD.shit.utils;

/**
 * @author zTerrarxd_
 * @since 21:35 of 02.02.2023
 */
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL20.glUniform1;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;


public class GaussianBlur {
    static Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtility blurShader = new ShaderUtility("sutil/gaussian.frag");

    public static Framebuffer framebuffer = new Framebuffer(1, 1, false);


    public static void setupUniforms(float dir1, float dir2, float radius) {
        blurShader.setUniformi("textureIn", 0);
        blurShader.setUniformf("texelSize", 1.0F / (float) mc.displayWidth, 1.0F / (float) mc.displayHeight);
        blurShader.setUniformf("direction", dir1, dir2);
        blurShader.setUniformf("radius", radius);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; i++) {
            weightBuffer.put(ShaderUtility.calculateGaussianValue(i, radius / 2));
        }

        weightBuffer.rewind();
        glUniform1(blurShader.getUniform("weights"), weightBuffer);
    }


    static Framebuffer framebuffer1 = ShaderUtility.createFrameBuffer(new Framebuffer(1, 1, false));
    private static Framebuffer inputFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    private static Framebuffer outputFramebuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
    public static void drawBlur(float radius, Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }

    public static void update(Framebuffer framebuffer) {
        if (framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }
    }

    public static void renderBlur(float radius, List<Runnable> run) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.forEach(Runnable::run);
        framebuffer1.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);

        if (framebuffer1 != null) {
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            mc.getFramebuffer().bindFramebuffer(true);
            ShaderUtility.bindTexture(framebuffer1.framebufferTexture);
            drawBlur(radius, ShaderUtility::drawQuads);
            mc.getFramebuffer().bindFramebuffer(false);


            GlStateManager.disableAlpha();
            GL11.glPopMatrix();

        }

    }

    public static void renderBlur(float radius, Runnable run) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.run();
        framebuffer1.unbindFramebuffer();
        mc.getFramebuffer().bindFramebuffer(true);

        if (framebuffer1 != null) {
            GL11.glPushMatrix();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.0f);
            GlStateManager.enableBlend();
            OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

            mc.getFramebuffer().bindFramebuffer(true);
            ShaderUtility.bindTexture(framebuffer1.framebufferTexture);
            drawBlur(radius, ShaderUtility::drawQuads);
            mc.getFramebuffer().bindFramebuffer(false);


            GlStateManager.disableAlpha();
            GL11.glPopMatrix();

        }

    }

    public static void renderBlur(float radius) {
        mc.getFramebuffer().bindFramebuffer(true);
        GlStateManager.enableBlend();
        GL11.glDisable(GL_DEPTH_TEST);
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);


        update(framebuffer);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        blurShader.init();
        setupUniforms(1, 0, radius);

        ShaderUtility.bindTexture(mc.getFramebuffer().framebufferTexture);

        ShaderUtility.drawQuads();
        framebuffer.unbindFramebuffer();
        blurShader.unload();

        mc.getFramebuffer().bindFramebuffer(true);
        blurShader.init();
        setupUniforms(0, 1, radius);

        ShaderUtility.bindTexture(framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        blurShader.unload();

        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
        GL11.glEnable(GL_DEPTH_TEST);
    }


}
