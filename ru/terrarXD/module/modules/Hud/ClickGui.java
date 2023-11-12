package ru.terrarXD.module.modules.Hud;

import org.lwjgl.input.Keyboard;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.RPC;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.ColorSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.ModeSetting;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 19:06 of 19.04.2023
 */
public class ClickGui extends Module {
    public ColorSetting color;
    public BooleanSetting blur;
    public FloatSetting blurradius;
    public BooleanSetting anime;
    public ModeSetting animee;
    public FloatSetting animeSize;
    public FloatSetting animeX;
    public FloatSetting animeY;

    public ClickGui() {
        super("ClickGui", Category.Hud);
        setKey(Keyboard.KEY_RSHIFT);
        add(color = new ColorSetting("Color", new Color(5, 134, 105)));
        add(blur = new BooleanSetting("Blur", true));
        add(blurradius = (FloatSetting) new FloatSetting("Blur-Radius",1, 25, 10, 1f).setVisible(()->blur.getVal()));

        add(anime = new BooleanSetting("Anime", true));

        ArrayList<String> modes = new ArrayList<>();
        modes.add("SkyLine");
        modes.add("Miku");
        modes.add("Aqua");
        add(animee = (ModeSetting) new ModeSetting("IMG", modes, "SkyLine").setVisible(()->anime.getVal()));
        add(animeSize = (FloatSetting) new FloatSetting("Anime-Size", 0.1f, 3, 3, 0.1f).setVisible(()->anime.getVal()));
        add(animeX = (FloatSetting) new FloatSetting("Anime-X", -200, 200, 0, 0.1f).setVisible(()->anime.getVal()));
        add(animeY = (FloatSetting) new FloatSetting("Anime-Y", -200, 200, 0, 0.1f).setVisible(()->anime.getVal()));

    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(Client.clickGuiScreen);
        setEnabled(false);
    }
}
