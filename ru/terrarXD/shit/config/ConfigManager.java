package ru.terrarXD.shit.config;

import net.minecraft.client.Minecraft;
import org.apache.commons.io.FileUtils;
import ru.terrarXD.Client;
import ru.terrarXD.module.modules.Hud.Notifications;

import java.io.File;
import java.io.IOException;

/**
 * @author zTerrarxd_
 * @since 17:29 of 29.05.2023
 */
public class ConfigManager {
    public final File CLIENT_FOLDER = new File(Minecraft.getMinecraft().mcDataDir, "Ricardo");
    public final File SCRIPTS_FOLDER = new File(CLIENT_FOLDER, "scripts");

    public ConfigModules configModules;
    public ConfigGuiIMG configGuiIMG;

    public ConfigManager(){
        CLIENT_FOLDER.mkdirs();
        SCRIPTS_FOLDER.mkdir();
        configModules=new ConfigModules("config", CLIENT_FOLDER);
        configGuiIMG = new ConfigGuiIMG("img", CLIENT_FOLDER);

    }


    public void load(){
        configGuiIMG.load();
        configModules.load();

    }
    public void save(){
        try {
            ((Notifications)Client.moduleManager.getModule("Notifications")).displayNotif(new Notifications.Notif("CFG System", "КФГ сохранён"));
        } catch (Exception e) {
        }
        configModules.save();
    }
    public void remove(){
        try {
            FileUtils.deleteDirectory(CLIENT_FOLDER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        configGuiIMG.remove();
        configModules.remove();
    }

}
