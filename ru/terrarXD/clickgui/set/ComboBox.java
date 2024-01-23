package ru.terrarXD.clickgui.set;

import ru.terrarXD.Client;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 13:57 of 30.05.2023
 */
public class ComboBox extends Set{
    ArrayList<Mode> modes = new ArrayList<>();
    AnimationUtils anim;
    public ComboBox(ModeSetting setting) {
        super(setting);
        anim = new AnimationUtils(0, 0, 0.1f);
        for (String s : setting.getModes()){
            modes.add(new Mode(s));
        }
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        ModeSetting setting = (ModeSetting) getSetting();

        int color2 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.7d).getRGB());
        int color3 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.6d).getRGB());
        Fonts.main_18.drawString(setting.getName(), x+5, y+getHeight()/2-Fonts.main_18.getHeight()/2, getColor(isHover(x,y, x+getWidth(), y+getHeight(), mouseX, mouseY) ? -1 : new Color(200, 200, 200).getRGB()));
        int max = 1;
        float size = 0;
        float nowSize = 0;
        boolean sizable = true;
        for (Mode mode : modes){
            if (Fonts.main_16.getStringWidth(mode.name) > max){
                max = Fonts.main_16.getStringWidth(mode.name);
            }
            size +=Fonts.main_16.getStringWidth(mode.name)+5;
            if (mode.name.equals(((ModeSetting)setting).getVal())){
                sizable = false;
            }
            if (sizable){
                nowSize +=Fonts.main_16.getStringWidth(mode.name)+5;
            }

        }
        float posX = x+getWidth()-5-7-7-max;
        Fonts.main_16.drawCenteredString("<", posX+7/2, y+getHeight()/2-Fonts.main_16.getHeight()/2, new Color(200, 200, 200).getRGB());
        Fonts.main_16.drawCenteredString(">", posX+7+max+7/2, y+getHeight()/2-Fonts.main_16.getHeight()/2, new Color(200, 200, 200).getRGB());

        anim.to = nowSize;
        float yz = 0;
        float pp =posX+7+5+max/2-(modes.size()*4)+4;
        for (Mode mode : modes){
            yz+=mode.render(posX+7-anim.getAnim()+yz+(max-Fonts.main_16.getStringWidth(setting.getVal()))/2, y+getHeight()/2-Fonts.main_16.getHeight()/2, mouseX, mouseY, x, y)+5;
            RenderUtils.drawCircle(pp, y+getHeight()/2+Fonts.main_16.getHeight()/2+2, 1.5f, mode.name.equals(setting.getVal()) ? Client.clickGuiScreen.getColor() : new Color(200, 200, 200).getRGB());
            pp+=4;
        }



    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        ModeSetting setting = (ModeSetting) getSetting();
        if (button == 0){
            int max = 1;
            for (Mode mode : modes){
                if (Fonts.main_16.getStringWidth(mode.name) > max){
                    max = Fonts.main_16.getStringWidth(mode.name);
                }
            }
            float posX = x+getWidth()-5-7-7-max;
            if (isHover(posX, y, posX+7, y+getHeight(), mouseX, mouseY)){
                if (setting.getModes().indexOf(setting.getVal()) != 0){
                    setting.setVal(setting.getModes().get(setting.getModes().indexOf(setting.getVal())-1));
                }

            }
            if (isHover(posX+7+max, y, posX+7+max+7, y+getHeight(), mouseX, mouseY)){
                if (setting.getModes().indexOf(setting.getVal()) != setting.getModes().size()-1){
                    setting.setVal(setting.getModes().get(setting.getModes().indexOf(setting.getVal())+1));
                }
            }
        }
    }


    @Override
    public float getHeight() {
        return 15;
    }

    @Override
    public void reset() {
        super.reset();
        anim.setAnim(0);
    }

    class Mode{
        String name;
        AnimationUtils animAlpha;
        public Mode(String name){
            this.name = name;
            animAlpha = new AnimationUtils(0, 0, 0.1f);
        }

        public float render(float x, float y, float mouseX, float mouseY, float globX, float globY){
            animAlpha.to = name.equals(((ModeSetting)setting).getVal()) ? 255 : 0;
            if (animAlpha.getAnim() >= 50){
                Fonts.main_16.drawString(name, (double) x, y, ColorUtils.swapAlpha(getColor(isHover(globX,globY, globX+getWidth(), globY+getHeight(), mouseX, mouseY) ? -1 : new Color(200, 200, 200).getRGB()), (int) animAlpha.getAnim()));

            }
            return Fonts.main_16.getStringWidth(name);
        }

    }
}
