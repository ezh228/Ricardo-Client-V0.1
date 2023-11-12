package ru.terrarXD.clickgui.sets;

import ru.terrarXD.Client;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.Setting;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 22:16
 */
public class CheckBox extends Set{
    AnimationUtils animButton = new AnimationUtils(0, 0, 0.1f);

    public CheckBox(BooleanSetting setting) {
        super(setting);
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        //int alpha =
        BooleanSetting setting = (BooleanSetting) getSetting();
        if (setting.getVal()){
            animButton.to = 1;
        }else {
            animButton.to = 0;
        }
        int colorMain1 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.1d).getRGB();
        int colorMain2 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.3d).getRGB();
        int pos = (int) (getWidth()-25);
        if (setting.getVal()){
            RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x+pos, y+3+4, x+pos+20, y+3f+18-4, 5, 1, colorMain1, colorMain2, colorMain2, colorMain1, false, true ,true);
            int radius = 4;
            if ((1f - animButton.getAnim()) >= 0.01f){
                Fonts.icons_12.drawCenteredString("I", x+pos+5+9, y+1+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(-1, (int) (255f*(1f - animButton.getAnim()))));
            }
            if ((animButton.getAnim()) >= 0.01f) {
                Fonts.icons_12.drawCenteredString("H", x+pos+5, y+1+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(-1, (int) (255f*animButton.getAnim())));
            }
            RenderUtils.drawCircle(x+pos+5+animButton.getAnim()*10f, y+3+18f/2f, radius, -1);
        }else {
            RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x+pos, y+3+4, x+pos+20, y+3f+18-4, 5, 1, -1, -1, -1, -1, false, true ,true);
            int radius = 4;
            if ((1f - animButton.getAnim()) >= 0.01f){
                Fonts.icons_12.drawCenteredString("I", x+pos+5+9, y+1+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(colorMain1, (int) (255f*(1f - animButton.getAnim()))));
            }
            if ((animButton.getAnim()) >= 0.01f) {
                Fonts.icons_12.drawCenteredString("H", x+pos+5, y+3+1+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(colorMain2, (int) (255f*animButton.getAnim())));
            }
            RenderUtils.drawCircle(x+pos+5+animButton.getAnim()*10f, y+3+18f/2f, radius, colorMain2);
        }
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        BooleanSetting setting = (BooleanSetting) getSetting();
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY) && button == 0){
            setting.setVal(!setting.getVal());

        }
    }

    @Override
    public float getHeight() {
        return 15;
    }
}
