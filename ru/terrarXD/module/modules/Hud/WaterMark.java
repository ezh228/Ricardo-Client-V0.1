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
    BooleanSetting shadow;
    public WaterMark() {
        super("WaterMark", Category.Hud, 100, 25);
        setPosX(10);
        setPosY(10);
        setEnabled(true);
        add(shadow = new BooleanSetting("Shadow", false));
    }



    @EventTarget
    public void onRender2D(EventRender2D event){
        String text= Client.NAME_FULL+" "+Client.VERSION;
        setSizeX(Fonts.main_18.getStringWidth(text)+4);
        float x = getPosX();
        float y = getPosY();

        renderRect();
        if (shadow.getVal()){
            BloomUtil.renderBlur(()->renderRect());
        }
        Fonts.main_18.drawString(Client.NAME_FULL.substring(0,1), x+2, y+7.5f-(Fonts.main_18.getHeight()/2), Client.getColor());

        Fonts.main_18.drawString(Client.NAME_FULL.substring(1, Client.NAME_FULL.length())+" "+Client.VERSION, x+8, y+7.5f-(Fonts.main_18.getHeight()/2), -1);
        Fonts.main_18.drawString("by zTerrarxd_", x+2, y+10+7.5f-(Fonts.main_18.getHeight()/2), -1);

    }

    public void renderRect(){
        float x = getPosX();
        float y = getPosY();
        int colorMain = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.92d).getRGB();
        RenderUtils.drawRoundedRect(x, y, x+getSizeX(), y+15, 5, colorMain);
        RenderUtils.drawRoundedRect(x, y, x+ Fonts.main_18.getStringWidth("by zTerrarxd_")+5, y+25, 5, colorMain);
    }
}
