package ru.terrarXD.shit;

import org.lwjgl.input.Mouse;
import ru.terrarXD.module.HudModule;

/**
 * @author zTerrarxd_
 * @since 21:49 of 22.04.2023
 */
public class DragObject {
    public HudModule module;
    boolean draging = false;

    float dragX;
    float dragY;

    public DragObject(HudModule module){
        this.module = module;
    }

    public void update(float mouseX, float mouseY){
        if (!Mouse.isButtonDown(0)) {
            draging = false;
        }
        if (draging){
            module.setPosX(mouseX-dragX);
            module.setPosY(mouseY-dragY);
        }
    }
    public void drag(float mouseX, float mouseY, int buuton){
        // Тут должна быть шутка про drug - наркотики, но её тут нет, живите с этим

            draging = true;
            dragX=mouseX-module.getPosX();
            dragY=mouseY-module.getPosY();

    }

    public void start(){
        if (!module.isEnabled()){
            module.onEnable();

        }
    }

    public void stop(){
        if (!module.isEnabled()){
            module.onDisable();
        }

    }

    public boolean isHover(float x, float y, float x2, float y2, float mouseX, float mouseY){
        return mouseX>x && mouseX < x2 && mouseY>y && mouseY<y2;
    }
}
