package ru.terrarXD.module.modules.Hud;

import com.sun.jna.platform.unix.X11;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import ru.terrarXD.Client;
import ru.terrarXD.module.BindType;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.HudModule;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.BloomUtil;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;

/**
 * @author zTerrarxd_
 * @since 13:14 of 17.04.2023
 */
public class WaterMark extends HudModule {
    public WaterMark() {
        super("WaterMark", Category.Hud, 100, 25);
        setPosX(10);
        setPosY(10);
        setEnabled(true);
    }



    @EventTarget
    public void onRender2D(EventRender2D event){
        /*
        if (mc.player.onGround){
            mc.player.jump();
        }

         */
        setSizeY(17);
        String text= Client.NAME_FULL+" "+Client.VERSION;
        if (!Client.actual){
            text+=" old";
        }
        setSizeX(Fonts.main_18.getStringWidth(text)+4);
        float x = getPosX();
        float y = getPosY();
        int colorMain1 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.1d).getRGB();
        int colorMain2 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.3d).getRGB();
        int color = new Color(29, 29, 29).getRGB();
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x+getSizeX(), y+getSizeY(), 5, 1f, colorMain2, colorMain1, colorMain2, colorMain1, false, true, true);
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y+3, x+getSizeX(), y+getSizeY(), 5, 1f, color, color, color, color, false, true, true);
        Fonts.main_18.drawString(text, x+2, y+7, -1);
    }


}
