package ru.terrarXD.clickgui.sets;

import ru.terrarXD.Client;
import ru.terrarXD.clickgui.Comp;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.Setting;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.GaussianBlur;
import ru.terrarXD.shit.utils.RenderUtils;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 22:04
 */
public class Set extends Comp {
    AnimationUtils anim = new AnimationUtils(0, 0, 0.1f);
    Setting setting;
    public Set(Setting setting){
        this.setting = setting;
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {



        super.drawScreen(x, y, mouseX, mouseY);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY)){
            anim.to =1;
        }else {
            anim.to = 0;
        }
        Fonts.main_15.drawString(setting.getName(), x+6+anim.getAnim()*3, y+3+getHeight()/2-Fonts.main_15.getHeight()/2, -1);

    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
    }

    public Setting getSetting() {
        return setting;
    }

    @Override
    public float getWidth() {
        return (Client.clickGuiScreen.WIDTH-100-40-10)/2;
    }
}
