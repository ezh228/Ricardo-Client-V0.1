package ru.terrarXD.clickgui;

import ru.terrarXD.Client;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 18:21
 */
public class Panel extends Comp {
    String name;
    String icon;
    AnimationUtils anim = new AnimationUtils(0, 1, 0.1f);
    AnimationUtils animSdvig = new AnimationUtils(0, 1, 0.1f);
    public Panel(String name){
        this.name = name;
        if (name.equals("Combat")){
            icon="D";
        }else if (name.equals("Player")){
            icon="B";
        }else if (name.equals("Render")){
            icon="C";
        }else if (name.equals("Movement")){
            icon="A";
        }else if (name.equals("Hud")){
            icon="M";
        }
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);


        int he = 18;
        x+=anim.getAnim()*18f;



        if (anim.getAnim() > 0.1f){
            int colorMain1 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.1d).getRGB();
            int colorMain2 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.3d).getRGB();
            colorMain1 = ColorUtils.swapAlpha(colorMain1, (int) (anim.getAnim()*255f));
            colorMain2 = ColorUtils.swapAlpha(colorMain2, (int) (anim.getAnim()*255f));
            RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x+animSdvig.getAnim()*3-3, y, x+getWidth()+animSdvig.getAnim()*3, y+he, 5, 0.5f, colorMain2, colorMain1, colorMain1, colorMain2, false, true ,true);
            RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x+animSdvig.getAnim()*3, y, x+getWidth()+animSdvig.getAnim()*3, y+he, 5, 1f, new Color(29, 29, 29, (int) ( anim.getAnim()*255f)).getRGB(), new Color(29, 29, 29, (int) ( anim.getAnim()*255f)).getRGB(), new Color(29, 29, 29, (int) ( anim.getAnim()*255f)).getRGB(), new Color(29, 29, 29, (int) ( anim.getAnim()*255f)).getRGB(), false, true, true);
            Fonts.main_18.drawString(">", x+getWidth()+animSdvig.getAnim()*3-Fonts.main_18.getStringWidth(">")-3, y+he/2-Fonts.main_18.getHeight()/2, new Color(255, 255, 255, (int) ( anim.getAnim()*255f)).getRGB());
        }

        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY)){
            animSdvig.to = 1f;
        }else {
            animSdvig.to = 0f;
        }
        if (Client.clickGuiScreen.currentPanel != null && Client.clickGuiScreen.currentPanel.equals(this)){
           anim.to = 1f;
        }else {
            anim.to = 0f;
        }
        Fonts.icons_18.drawString(icon, x+animSdvig.getAnim()*3+2, y+he/2-Fonts.icons_18.getHeight()/2, -1);
        Fonts.main_15.drawString(name, x+Fonts.icons_18.getStringWidth(icon)+5+animSdvig.getAnim()*3, y+he/2-Fonts.main_15.getHeight()/2, -1);


    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY) && button == 0){
            Client.clickGuiScreen.setCurrentPanel(this);
        }
    }

    @Override
    public float getHeight() {
        int he = 18;
        return he;
    }

    @Override
    public float getWidth() {
        return 80;
    }
}
