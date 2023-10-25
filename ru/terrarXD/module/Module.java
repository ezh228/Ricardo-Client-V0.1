package ru.terrarXD.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.Sound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextFormatting;
import ru.terrarXD.Client;
import ru.terrarXD.module.modules.Hud.Notifications;
import ru.terrarXD.shit.settings.Setting;

import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 12:21 of 16.04.2023
 */
public class Module {
    protected Minecraft mc = Minecraft.getMinecraft();
    String name;
    Category category;
    BindType bindType;
    int key = 0;
    boolean enabled = false;
    ArrayList<Setting> settings = new ArrayList<>();
    public Module(String name, Category category){
        this.name = name;
        this.category = category;
        bindType = BindType.Toggle;
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public void add(Setting setting){
        settings.add(setting);
    }

    public String getName() {
        return name;
    }

    public int getKey() {
        return key;
    }

    public Category getCategory() {
        return category;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (isEnabled()){
            onEnable();
        }else {
            onDisable();
        }
    }

    public void setBindType(BindType bindType) {
        this.bindType = bindType;
    }

    public BindType getBindType() {
        return bindType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle(){
        setEnabled(!isEnabled());
    }

    public void onEnable(){
        if (Client.moduleManager != null){
            ((Notifications) Client.moduleManager.getModule("Notifications")).displayNotif(new Notifications.Notif(name, "был " + TextFormatting.GREEN +"включён"));

        }

        Client.eventManager.register(this);

    }

    public void onDisable(){
        if (Client.moduleManager != null){
            ((Notifications) Client.moduleManager.getModule("Notifications")).displayNotif(new Notifications.Notif(name, "был " + TextFormatting.RED +"выключен"));
        }

        Client.eventManager.unregister(this);

    }

    public void register(){
        Client.eventManager.register(this);

    }

    public void unregister(){
        Client.eventManager.unregister(this);

    }




}
