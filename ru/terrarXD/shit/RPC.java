package ru.terrarXD.shit;



import ru.terrarXD.Client;
import ru.terrarXD.module.modules.Hud.ClickGui;
import ru.terrarXD.shit.discord.DiscordEventHandlers;
import ru.terrarXD.shit.discord.DiscordRPC;
import ru.terrarXD.shit.discord.DiscordRichPresence;
import ru.terrarXD.shit.settings.ColorSetting;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class RPC {
    public static String text ="";

    private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;
    public static void update(){    
        ColorSetting setting = ((ClickGui)Client.moduleManager.getModule("ClickGui")).color;
        if (setting.getColor().getRGB() == new Color(247, 50, 56).getRGB()){
            discordRichPresence.largeImageKey = "logo-red";
        }else if (setting.getColor().getRGB() == new Color(242, 99, 33).getRGB()){
            discordRichPresence.largeImageKey = "logo-orange";
        }else if (setting.getColor().getRGB() == new Color(252, 179, 22).getRGB()){
            discordRichPresence.largeImageKey = "logo-yellow";
        }else if (setting.getColor().getRGB() == new Color(5, 134, 105).getRGB()){
            discordRichPresence.largeImageKey = "logo-green";
        }else if (setting.getColor().getRGB() == new Color(47, 122, 229).getRGB()){
            discordRichPresence.largeImageKey = "logo-blue";
        }else if (setting.getColor().getRGB() == new Color(126, 84, 217).getRGB()){
            discordRichPresence.largeImageKey = "logo-purple";
        }else if (setting.getColor().getRGB() == new Color(232, 96, 152).getRGB()){
            discordRichPresence.largeImageKey = "logo-pink";
        }
        discordRichPresence.state = text;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

        String discordID = "1156591298376110110";
        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;

        discordRichPresence.details = "Return edition " + Client.VERSION;
        discordRichPresence.largeImageKey = "logo-blue";
        discordRichPresence.largeImageText = Client.NAME_FULL+" "+Client.VERSION;
        discordRichPresence.joinSecret = "JrfqTXi5CeseEvVafGGkPOFmEVbWZuhN";
        URL url = null;
        try {
            url = new URL("https://ricardoclient.netlify.app/reklama");
            URLConnection con = url.openConnection();
            InputStream is = con.getInputStream();
            text = "";
            try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    text+=line;
                }
            }
            text = text.split("#")[1];
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        discordRichPresence.state = text;
        discordRPC.Discord_UpdatePresence(discordRichPresence);


    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
}
