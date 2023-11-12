package ru.terrarXD.shit.settings;



import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 15:47 of 21.04.2023
 */
public class ColorSetting extends Setting {
    Color color;
    public ColorSetting(String name, Color color) {
        super(name);
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
