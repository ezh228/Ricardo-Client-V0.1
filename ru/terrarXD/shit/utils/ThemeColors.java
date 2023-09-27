package ru.terrarXD.shit.utils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 15:29 of 04.02.2023
 */
public class ThemeColors {
    public static int color = new Color(255, 255, 255).getRGB();
    public static int color2 = new Color(225, 232, 240).getRGB();


    public static int getColor(int step){
        return ColorUtils.TwoColoreffect(new Color(180, 76, 57), new Color(236, 100, 76), Math.abs(System.currentTimeMillis() / 50L) / 100.0 + 6.0F * (step * 2.55) / 90).getRGB();

    }
}
