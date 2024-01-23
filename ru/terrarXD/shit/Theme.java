package ru.terrarXD.shit;

import ru.terrarXD.Client;
import ru.terrarXD.shit.utils.ColorUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @date 24.11.2023 0:39
 */
public enum Theme {

    Dark(new Color(28, 32, 34).getRGB(), new Color(44, 45, 49).getRGB()), Light(new Color(255, 255, 255).getRGB(), new Color(231, 231, 231).getRGB());

    int color;
    int color2;
    Theme(int color, int color2){
        this.color = color;
        this.color2 = color2;
    }

    public int getColor() {

        return color;
    }

    public int getColor2() {

        return color2;
    }
}
