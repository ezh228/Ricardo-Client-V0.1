package ru.terrarXD.shit.utils;

/**
 * @author zTerrarxd_
 * @since 21:35 of 02.02.2023
 */
import static org.lwjgl.opengl.GL11.GL_GREATER;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;


public class BloomUtil implements Utility {
    static Minecraft mc = Minecraft.getMinecraft();
    public static ShaderUtility gaussianBloom = new ShaderUtility("sutil/bloom.frag");

    public static Framebuffer framebuffer = ShaderUtility.createFrameBuffer(new Framebuffer(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth(), new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight(), true));

    public static void update(Framebuffer framebuffer) {
        if (framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            framebuffer.createBindFramebuffer(mc.displayWidth, mc.displayHeight);
        }
    }


    static Framebuffer framebuffer1 = ShaderUtility.createFrameBuffer(new Framebuffer(1, 1, false));
    public static void renderBlur(List<Runnable> run) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.forEach(Runnable::run);
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, 10, 1, ColorUtils.getColor(0,0,0,255), 2, true);

    }
    public static void renderShadow(List<Runnable> run,int color,int radius,int offset,float des,boolean fill) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.forEach(Runnable::run);
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, radius, offset, color, des, fill);

    }
    public static void renderShadow(Runnable run,int color,int radius,int offset,float des,boolean fill) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.run();
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, radius, offset, color, des, fill);

    }
    public static void renderBlur(Runnable run) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.run();
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, 10, 1, ColorUtils.getColor(0,0,0,255), 2, true);
    }

    public static void renderBlur(Runnable run, int radius, int offset, int color, boolean fill) {
        update(framebuffer1);
        framebuffer1.framebufferClear();
        framebuffer1.bindFramebuffer(true);
        run.run();
        framebuffer1.unbindFramebuffer();

        renderBlur(framebuffer1.framebufferTexture, radius, 1, color, offset, fill);
    }

    public static void renderBlur(Runnable run, int radius, int offset, int color, boolean fill, Framebuffer framebuffer) {
        update(framebuffer);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        run.run();
        framebuffer.unbindFramebuffer();

        renderBlur(framebuffer.framebufferTexture, radius, 1, color, offset, fill);
    }

    public static void renderBlur(int sourceTexture, int radius, int offset, int c, float des, boolean fill) {
        framebuffer = ShaderUtility.createFrameBuffer(BloomUtil.framebuffer);
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);

        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 1; i <= radius; i++) {
            weightBuffer.put(ShaderUtility.calculateGaussianValue(i, radius / 2f));
        }
        weightBuffer.rewind();

        setAlphaLimit(0.0F);

        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        gaussianBloom.init();
        setupUniforms(radius, 1, 0, weightBuffer, c, des, fill);
        ShaderUtility.bindTexture(sourceTexture);
        ShaderUtility.drawQuads();
        gaussianBloom.unload();
        framebuffer.unbindFramebuffer();


        mc.getFramebuffer().bindFramebuffer(true);
        gaussianBloom.init();
        setupUniforms(radius, 0, 1, weightBuffer, c, des, fill);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE16);
        ShaderUtility.bindTexture(sourceTexture);
        GlStateManager.setActiveTexture(GL13.GL_TEXTURE0);
        ShaderUtility.bindTexture(framebuffer.framebufferTexture);
        ShaderUtility.drawQuads();
        gaussianBloom.unload();

        GlStateManager.alphaFunc(516, 0f);
        GlStateManager.enableAlpha();

        GlStateManager.bindTexture(0);
        GlStateManager.popMatrix();
    }

    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }

    public static void setupUniforms(int radius, int directionX, int directionY, FloatBuffer weights, int c, float des, boolean fill) {
        gaussianBloom.setUniformi("avoidTexture", fill ? 1 : 0);
        gaussianBloom.setUniformi("inTexture", 0);
        gaussianBloom.setUniformi("textureToCheck", 16);
        gaussianBloom.setUniformf("radius", radius);
        gaussianBloom.setUniformf("exposure", des);
        gaussianBloom.setUniformf("color", ColorUtils.getGLRedFromColor(c), ColorUtils.getGLGreenFromColor(c),ColorUtils.getGLBlueFromColor(c));
        gaussianBloom.setUniformf("texelSize", 1.0F / (float) mc.displayWidth, 1.0F / (float) mc.displayHeight);
        gaussianBloom.setUniformf("direction", directionX, directionY);

        GL20.glUniform1(gaussianBloom.getUniform("weights"), weights);
    }
}