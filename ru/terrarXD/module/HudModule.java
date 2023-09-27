package ru.terrarXD.module;

/**
 * @author zTerrarxd_
 * @since 13:17 of 17.04.2023
 */
public class HudModule extends Module{
    float posX;
    float posY;
    float sizeX;
    float sizeY;

    public HudModule(String name, Category category, float sizeX, float sizeY) {
        super(name, category);
        this.sizeX =sizeX;
        this.sizeY = sizeY;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getSizeX() {
        return sizeX;
    }

    public float getSizeY() {
        return sizeY;
    }

    public void setSizeX(float sizeX) {
        this.sizeX = sizeX;
    }

    public void setSizeY(float sizeY) {
        this.sizeY = sizeY;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }
}
