package ru.terrarXD.clickgui.set;

import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.ColorSetting;
import ru.terrarXD.shit.settings.Setting;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 15:53 of 21.04.2023
 */
public class ColorPiker extends Set{
    AnimationUtils anim;
    public ColorPiker(ColorSetting setting) {
        super(setting);
        anim =new AnimationUtils(0, 0, 0.1f);
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        float wid = getWidth()-( Fonts.main_18.getStringWidth(setting.getName())+15);
        float posX = x+Fonts.main_18.getStringWidth(setting.getName())+10;
        ColorSetting setting = (ColorSetting) getSetting();
        if (setting.getColor().getRGB() == new Color(247, 50, 56).getRGB()){
            anim.to=wid/7-(wid/7/2);
        }else if (setting.getColor().getRGB() == new Color(242, 99, 33).getRGB()){
            anim.to=wid/7*2-(wid/7/2);
        }else if (setting.getColor().getRGB() == new Color(252, 179, 22).getRGB()){
            anim.to=wid/7*3-(wid/7/2);
        }else if (setting.getColor().getRGB() == new Color(5, 134, 105).getRGB()){
            anim.to=wid/7*4-(wid/7/2);
        }else if (setting.getColor().getRGB() == new Color(47, 122, 229).getRGB()){
            anim.to=wid/7*5-(wid/7/2);
        }else if (setting.getColor().getRGB() == new Color(126, 84, 217).getRGB()){
            anim.to=wid/7*6-(wid/7/2);
        }else if (setting.getColor().getRGB() == new Color(232, 96, 152).getRGB()){
            anim.to=wid/7*7-(wid/7/2);
        }
        Fonts.main_18.drawString(setting.getName(), x+5, y+getHeight()/2-Fonts.main_18.getHeight()/2, getColor(isHover(x,y, x+getWidth(), y+getHeight(), mouseX, mouseY) ? -1 : new Color(200, 200, 200).getRGB()));

        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f, getColor(new Color(247, 50, 56).getRGB()));
        posX+=wid/7;
        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f, getColor(new Color(242, 99, 33).getRGB()));
        posX+=wid/7;
        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f,getColor( new Color(252, 179, 22).getRGB()));
        posX+=wid/7;
        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f, getColor(new Color(5, 134, 105).getRGB()));
        posX+=wid/7;
        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f,getColor(new Color(47, 122, 229).getRGB()));
        posX+=wid/7;
        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f, getColor(new Color(126, 84, 217).getRGB()));
        posX+=wid/7;
        RenderUtils.drawRect(posX, y+getHeight()/2-0.5f, posX+wid/7-1, y+getHeight()/2+0.5f, getColor(new Color(232, 96, 152).getRGB()));
        posX = x+Fonts.main_18.getStringWidth(setting.getName())+10;

        RenderUtils.drawCircle(posX+anim.getAnim(), y+getHeight()/2, 2.5f,getColor( ((ColorSetting)(setting)).getColor().getRGB()));
    }



    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        float wid = getWidth()-( Fonts.main_18.getStringWidth(setting.getName())+15);
        float posX = x+Fonts.main_18.getStringWidth(setting.getName())+10;
        ColorSetting setting = (ColorSetting) getSetting();

        if (button == 0){
            for (int i = 0; i < 7; i+=1) {
                if (isHover(posX+(i*(wid/7)), y, posX+(i*(wid/7))+wid/7, y+getHeight(), mouseX, mouseY)){
                    switch (i){
                        case 0:
                            setting.setColor(new Color(247, 50, 56));
                            break;
                        case 1:
                            setting.setColor(new Color(242, 99, 33));
                            break;
                        case 2:
                            setting.setColor(new Color(252, 179, 22));
                            break;
                        case 3:
                            setting.setColor(new Color(5, 134, 105));
                            break;
                        case 4:
                            setting.setColor(new Color(47, 122, 229));
                            break;
                        case 5:
                            setting.setColor(new Color(126, 84, 217));
                            break;
                        case 6:
                            setting.setColor(new Color(232, 96, 152));
                            break;
                    }
                }
            }
        }

    }

    @Override
    public float getHeight() {
        return 17;
    }
}
