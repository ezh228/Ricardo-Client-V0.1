package ru.terrarXD.clickgui.screen;

import ru.terrarXD.Client;
import ru.terrarXD.clickgui.Comp;
import ru.terrarXD.shit.utils.AnimationUtils;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 19:55
 */
public class Screen extends Comp {

    AnimationUtils anim = new AnimationUtils(0, 1, 0.1f);

    public Screen(){

    }

    @Override
    public void init() {
        super.init();
        anim.setAnim(0);
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
    }

    @Override
    public float getWidth() {
        return Client.clickGuiScreen.WIDTH-100-40;
    }

    @Override
    public float getHeight() {
        return Client.clickGuiScreen.HEIGHT-45;
    }
}
