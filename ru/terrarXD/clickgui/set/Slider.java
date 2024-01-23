package ru.terrarXD.clickgui.set;

import it.unimi.dsi.fastutil.floats.FloatSet;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.Setting;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.MathUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 23:00 of 20.04.2023
 */
public class Slider extends Set{
    boolean draging = false;
    public Slider(FloatSetting setting) {
        super(setting);
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        int color3 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.6d).getRGB());

        FloatSetting setting = (FloatSetting) getSetting();
        if (draging && !Mouse.isButtonDown(0)){
            draging = false;
        }

        Fonts.main_18.drawString(setting.getName(), x+5, y+getHeight()/2-Fonts.main_18.getHeight()/2, getColor(isHover(x,y, x+getWidth(), y+getHeight(), mouseX, mouseY) ? -1 : new Color(200, 200, 200).getRGB()));
        float wid = getWidth()-( Fonts.main_18.getStringWidth(setting.getName())+15+Fonts.main_18.getStringWidth(" "+setting.getVal()));
        float posX = x+Fonts.main_18.getStringWidth(setting.getName())+10;
        Fonts.main_16.drawString(setting.getVal()+"", posX+wid+5, y+getHeight()/2-Fonts.main_16.getHeight()/2,getColor( isHover(x,y, x+getWidth(), y+getHeight(), mouseX, mouseY) ? -1 : new Color(200, 200, 200).getRGB()));

        if(draging){
            setting.setVal((float) MathUtils.round((float) ((double)(mouseX - posX) * (setting.getMax() - setting.getMin()) / (double)(wid) + setting.getMin()), setting.getPercent()));
            if (setting.getVal() > setting.getMax()) {
                setting.setVal(setting.getMax());
            } else if (setting.getVal() < setting.getMin()) {
                setting.setVal(setting.getMin());
            }
        }

        double optionValue = MathUtils.round((float) setting.getVal(), 0.01f);
        double renderPerc = (double)(wid) / (setting.getMax() - setting.getMin());
        double barWidth = renderPerc * optionValue - renderPerc * setting.getMin();
        RenderUtils.drawRect(posX, y+getHeight()/2-1, posX+wid, y+getHeight()/2+1, getColor(new Color(40, 40, 40).getRGB()));

        RenderUtils.drawGradientRect(posX, y+getHeight()/2-1, (float) (posX+barWidth), y+getHeight()/2+1, true, ColorUtils.swapAlpha(Client.clickGuiScreen.getColor(), 100), ColorUtils.swapAlpha(Client.clickGuiScreen.getColor(), 50));
        RenderUtils.drawCircle((float) ( posX+barWidth), y+getHeight()/2, 3, color3);
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY) && button == 0){
            draging = true;
        }
    }


    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public float getHeight() {
        return 17;
    }
}
