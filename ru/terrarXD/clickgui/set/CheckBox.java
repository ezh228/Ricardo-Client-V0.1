package ru.terrarXD.clickgui.set;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
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
 * @since 16:19 of 20.04.2023
 */
public class CheckBox extends Set{
    AnimationUtils anim;
    public CheckBox(BooleanSetting setting) {
        super(setting);
        anim = new AnimationUtils(0, 0, 0.1f);
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        anim.to = ((BooleanSetting) setting).getVal() ? 1 : 0;
        int color2 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.7d).getRGB());
        int color3 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.6d).getRGB());
        Fonts.main_18.drawString(setting.getName(), x+5, y+getHeight()/2-Fonts.main_18.getHeight()/2, getColor(isHover(x,y, x+getWidth(), y+getHeight(), mouseX, mouseY) ? -1 : new Color(200, 200, 200).getRGB()));
        float posX = x+getWidth()-5-10;
        for (int i = 0; i < 10; i++) {
            RenderUtils.drawCircle(posX+i, y+getHeight()/2,4,getColor( ((BooleanSetting) setting).getVal() ? color2 : new Color(40, 40, 40).getRGB()));
        }
        RenderUtils.drawCircle(posX+(10*anim.getAnim()), y+getHeight()/2, 3.5f, getColor(((BooleanSetting) setting).getVal() ? color3 : new Color(60, 60, 60).getRGB()));
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY) && button == 0){
            ((BooleanSetting) setting).setVal(!((BooleanSetting) setting).getVal());
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
}
