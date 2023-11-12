package ru.terrarXD;

import org.lwjgl.input.Keyboard;
import ru.terrarXD.clickgui.ClickGuiScreen;
import ru.terrarXD.module.BindType;
import ru.terrarXD.module.Module;
import ru.terrarXD.module.ModuleManager;
import ru.terrarXD.module.modules.Player.Proverka;
import ru.terrarXD.shit.FriendsManager;
import ru.terrarXD.shit.RPC;
import ru.terrarXD.shit.config.ConfigManager;
import ru.terrarXD.shit.event.EventManager;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventKeyBoard;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.ColorSetting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author zTerrarxd_
 * @since 15:14 of 14.04.2023
 */
public class Client {

    public static final String NAME = "Ricardo";
    public static final String NAME_FULL = "Ricardo Client";
    public static ConfigManager configManager;
    public static final String VERSION = "V0.8";
    public static ModuleManager moduleManager;
    public static EventManager eventManager;
    public static ClickGuiScreen clickGuiScreen;
    public static FriendsManager friendsManager;

    public static boolean SHADER = true;

    public static boolean actual = true;

    public Client() throws Exception {



        eventManager = new EventManager();
        eventManager.register(this);
        moduleManager = new ModuleManager();
        friendsManager = new FriendsManager();
        configManager = new ConfigManager();
        configManager.load();
        clickGuiScreen = new ClickGuiScreen();
        URL url = new URL("https://ricardoclient.netlify.app/version");
        URLConnection con = url.openConnection();
        InputStream is = con.getInputStream();
        String text ="";
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                text+=line;
            }
        }
        if (!text.contains(VERSION)){
            actual = false;
        }

    }


    public static int getColor(){

        return ((ColorSetting) moduleManager.getModule("ClickGui").getSettings().get(0)).getColor().getRGB();
        //return new Color(231, 188, 18).getRGB();

    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        for (Module module : moduleManager.modules){
            if (module.getBindType()==BindType.Hold){
                if(Keyboard.isKeyDown(module.getKey()) && !module.isEnabled()){
                    module.setEnabled(true);
                }else if (!Keyboard.isKeyDown(module.getKey()) && module.isEnabled()){
                    module.setEnabled(false);
                }
            }
        }
    }

    @EventTarget
    public void onKeyPress(EventKeyBoard event){
        if (!Proverka.proverka){
            for (Module module : moduleManager.modules){
                if (module.getKey() == event.getKey() && module.getBindType() == BindType.Toggle){
                    module.toggle();
                }
            }
        }

    }
}
