package ru.terrarXD.shit.utils;

import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;

import java.awt.*;
import java.text.NumberFormat;

/**
 * @author zTerrarxd_
 * @since 2:00 of 30.05.2022
 */
public class ColorUtils {
    public static Color TwoColoreffect(Color cl1, Color cl2, double speed)
    {
        double thing = speed / 4.0 % 1.0;
        float val = MathUtils.clamp((float)Math.sin(Math.PI * 6 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color(MathUtils.lerp((float)cl1.getRed() / 255.0f, (float)cl2.getRed() / 255.0f, val), MathUtils.lerp((float)cl1.getGreen() / 255.0f, (float)cl2.getGreen() / 255.0f, val), MathUtils.lerp((float)cl1.getBlue() / 255.0f, (float)cl2.getBlue() / 255.0f, val));
    }

    public static float[] getHSBFromColor(int hex) {
        int r = hex >> 16 & 0xFF;
        int g = hex >> 8 & 0xFF;
        int b = hex & 0xFF;
        return Color.RGBtoHSB(r, g, b, null);
    }



    public static Color getHealthColor(EntityLivingBase entityLivingBase) {
        float health = entityLivingBase.getHealth();
        float[] fractions = new float[]{0.0f, 0.15f, 0.55f, 0.7f, 0.9f};
        Color[] colors = new Color[]{new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN};
        float progress = health / entityLivingBase.getMaxHealth();
        return health >= 0.0f ? blendColors(fractions, colors, progress).brighter() : colors[0];
    }

    public static Color blendColors(float[] fractions, Color[] colors, float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        int[] indicies = getFractionIndicies(fractions, progress);
        float[] range = new float[] { fractions[indicies[0]], fractions[indicies[1]] };
        Color[] colorRange = new Color[] { colors[indicies[0]], colors[indicies[1]] };
        float max = range[1] - range[0];
        float value = progress - range[0];
        float weight = value / max;
        return blend(colorRange[0], colorRange[1], 1.0f - weight);
    }
    public static int[] getFractionIndicies(float[] fractions, float progress) {
        int startPoint;
        int[] range = new int[2];
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {
        }
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    public static Color blend(Color color1, Color color2, double ratio) {
        float r = (float) ratio;
        float ir = 1.0f - r;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        } else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        } else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        } else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color = null;
        try {
            color = new Color(red, green, blue);
        } catch (IllegalArgumentException exp) {
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
        }
        return color;
    }
    public static int glColor(int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
        return color;
    }


    public static int getColor(int red, int green, int blue) {
        return ColorUtils.getColor(red, green, blue, 255);
    }
    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    public static int getRedFromColor(int color) {
        return (color >> 16 & 0xFF);
    }
    public static int getGreenFromColor(int color) {
        return (color >> 8 & 0xFF);
    }
    public static int getBlueFromColor(int color) {
        return (color & 0xFF);
    }
    public static int getAlphaFromColor(int color) {
        return (color >> 24 & 0xFF);
    }
    public static int getGLRedFromColor(int color) {
        return (color >> 16 & 0xFF)/255;
    }
    public static float getGLGreenFromColor(int color) {
        return (color >> 8 & 0xFF)/255;
    }
    public static float getGLBlueFromColor(int color) {
        return (color & 0xFF)/255;
    }
    public static float getGLAlphaFromColor(int color) {
        return (color >> 24 & 0xFF)/255;
    }

    public static int getOverallColorFrom(int color1,int color2,float percentTo2) {

        int red1 = getRedFromColor(color1);
        int green1 = getGreenFromColor(color1);
        int blue1 = getBlueFromColor(color1);
        int alpha1 = getAlphaFromColor(color1);
        int red2 = getRedFromColor(color2);
        int green2 = getGreenFromColor(color2);
        int blue2 = getBlueFromColor(color2);
        int alpha2 = getAlphaFromColor(color2);

        int finalRed = (int) (red1*(1-percentTo2) + red2*percentTo2);
        int finalGreen = (int) (green1*(1-percentTo2) + green2*percentTo2);
        int finalBlue = (int) (blue1*(1-percentTo2) + blue2*percentTo2);
        int finalAlpha = (int) (alpha1*(1-percentTo2) + alpha2*percentTo2);

        return getColor(finalRed,finalGreen,finalBlue,finalAlpha);
    }

    public static int getOverallColorFrom(int color1,int color2) {

        int red1 = getRedFromColor(color1);
        int green1 = getGreenFromColor(color1);
        int blue1 = getBlueFromColor(color1);
        int alpha1 = getAlphaFromColor(color1);
        int red2 = getRedFromColor(color2);
        int green2 = getGreenFromColor(color2);
        int blue2 = getBlueFromColor(color2);
        int alpha2 = getAlphaFromColor(color2);

        int finalRed = (red1 + red2) / 2;
        int finalGreen = (green1 + green2) / 2;
        int finalBlue = (blue1 + blue2) / 2;
        int finalAlpha = (alpha1 + alpha2) / 2;

        return getColor(finalRed,finalGreen,finalBlue,finalAlpha);
    }

    public static int swapAlpha(int color, int alpha)
    {
        int f = (int)(color >> 16 & 255);
        int f1 = (int)(color >> 8 & 255);
        int f2 = (int)(color & 255);
        return new Color(f, f1, f2,(int) MathUtils.clamp(alpha, 0, 255)).getRGB();
    }
}
