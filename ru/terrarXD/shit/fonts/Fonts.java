package ru.terrarXD.shit.fonts;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.io.InputStream;

public class Fonts
{
    public static float blob(String fontName, int fontSize, String text, float x, float y, int color, final boolean shadow)
    {
        CFontRenderer cf = new CFontRenderer(getFontTTF(fontName, fontSize), true, true);
        return cf.drawString(text, x, y, color, shadow);
    }//mntsb
    public static CFontRenderer main_12 = new CFontRenderer(getFontTTF("sf-ui", 12), true, true);
    public static CFontRenderer main_13 = new CFontRenderer(getFontTTF("sf-ui", 13), true, true);
    public static CFontRenderer main_14 = new CFontRenderer(getFontTTF("sf-ui", 14), true, true);
    public static CFontRenderer main_15 = new CFontRenderer(getFontTTF("sf-ui", 15), true, true);
    public static CFontRenderer main_16 = new CFontRenderer(getFontTTF("sf-ui", 16), true, true);
    public static CFontRenderer main_17 = new CFontRenderer(getFontTTF("sf-ui", 17), true, true);
    public static CFontRenderer main_18 = new CFontRenderer(getFontTTF("sf-ui", 18), true, true);
    public static CFontRenderer main_19 = new CFontRenderer(getFontTTF("sf-ui", 19), true, true);
    public static CFontRenderer main_20 = new CFontRenderer(getFontTTF("sf-ui", 20), true, true);
    public static CFontRenderer main_21 = new CFontRenderer(getFontTTF("sf-ui", 21), true, true);
    public static CFontRenderer main_22 = new CFontRenderer(getFontTTF("sf-ui", 22), true, true);
    public static CFontRenderer main_23 = new CFontRenderer(getFontTTF("sf-ui", 23), true, true);
    public static CFontRenderer main_24 = new CFontRenderer(getFontTTF("sf-ui", 24), true, true);
    public static CFontRenderer main_25 = new CFontRenderer(getFontTTF("sf-ui", 25), true, true);
    public static CFontRenderer main_36 = new CFontRenderer(getFontTTF("sf-ui", 26), true, true);



    public static Font getFontTTF(String name, int size)
    {
        Font font;

        try
        {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("client/font/" + name + ".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex)
        {
            // ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }

        return font;
    }
}
