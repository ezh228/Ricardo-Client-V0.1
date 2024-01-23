package ru.terrarXD.clickgui.set;

import ru.terrarXD.Client;
import ru.terrarXD.clickgui.Comp;
import ru.terrarXD.shit.settings.Setting;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.ColorUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 16:16 of 20.04.2023
 */
public class Set extends Comp {
    AnimationUtils animAlph;

    Setting setting;
    public Set(Setting setting){
        this.setting = setting;
        animAlph = new AnimationUtils(0, 255, 0.1f);
    }

    public Setting getSetting() {
        return setting;
    }

    public int getColor(int color){
        return ColorUtils.swapAlpha(color, (int) ((new Color(color).getAlpha() * animAlph.getAnim())/255));
    }


    @Override
    public void reset() {
        super.reset();
        animAlph.setAnim(0);
    }

    @Override
    public float getWidth() {
        return (Client.clickGuiScreen.sizeX-120-15)/2-10;
    }
}
