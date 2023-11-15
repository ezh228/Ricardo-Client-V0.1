package ru.terrarXD.clickgui.sets;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import ru.terrarXD.Client;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.Setting;
import ru.terrarXD.shit.utils.*;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 22:53
 */
public class Slider extends Set{
    boolean draging = false;
    AnimationUtils animSlider = new AnimationUtils(0, 0, 0.1f);
    public Slider(FloatSetting setting) {
        super(setting);
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);

        if (draging && !Mouse.isButtonDown(0)){
            draging = false;
        }
        FloatSetting setting = (FloatSetting) getSetting();
        int posX = (int) (x+ Fonts.main_15.getStringWidth(setting.getName())+6+3+3);
        int wid = (int) (x+getWidth()-30-posX);

        if(draging){

            setting.setVal((float) MathUtils.round((float) ((double)(mouseX - posX) * (setting.getMax() - setting.getMin()) / (double)(wid) + setting.getMin()), setting.getPercent()));
            if (setting.getVal() > setting.getMax()) {
                setting.setVal(setting.getMax());
            } else if (setting.getVal() < setting.getMin()) {
                setting.setVal(setting.getMin());
            }
        }

        double optionValue = MathUtils.round((float) setting.getVal(), 0.01);
        double renderPerc = (double)(wid) / (setting.getMax() - setting.getMin());
        double barWidth = renderPerc * optionValue - renderPerc * setting.getMin();
        animSlider.to = (float) barWidth;
        Fonts.main_16.drawString(setting.getVal()+"", posX+wid+5,y+3+getHeight()/2-Fonts.main_15.getHeight()/2, -1);
        y+=3.5f;
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX, y+getHeight()/2-1, posX+wid, y+getHeight()/2+1, 1, 1f, new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 50).getRGB(), new Color(0, 0, 0, 50).getRGB(), false, true, true);

        //RenderUtils.drawRect(posX, y+getHeight()/2-1, posX+wid, y+getHeight()/2+1, new Color(0, 0, 0, 50).getRGB());
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX, y+getHeight()/2-1, (float) (posX+animSlider.getAnim()), y+getHeight()/2+1, 1, 1f, ColorUtils.swapAlpha(Client.getColor(), 100), ColorUtils.swapAlpha(Client.getColor(), 255), ColorUtils.swapAlpha(Client.getColor(), 255), ColorUtils.swapAlpha(Client.getColor(), 100), false, true, true);
        //RenderUtils.drawGradientRect(posX, y+getHeight()/2-1, (float) (posX+animSlider.getAnim()), y+getHeight()/2+1, true, ColorUtils.swapAlpha(Client.getColor(), 100), ColorUtils.swapAlpha(Client.getColor(), 255));
        GlStateManager.resetColor();
        if (draging){
            particle2DS.add(new Particle2D((float) (posX+animSlider.getAnim()), y+getHeight()/2, 4));
        }
        for (int i = 0; i < particle2DS.size(); i++) {
            if (particle2DS.get(i).toRemove()){
                particle2DS.remove(i);
            }else {
                particle2DS.get(i).drawAndMovement();
            }
        }
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY) && button == 0){
            draging = true;
        }
    }

    @Override
    public float getHeight() {
        return 15;
    }
}
