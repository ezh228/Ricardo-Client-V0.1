package ru.terrarXD.module.modules.Render;

import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Mouse;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.module.modules.Combat.newAimBot;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;

/**
 * @author zTerrarxd_
 * @since 12:28 of 20.06.2023
 */
public class Derp extends Module {
    BooleanSetting server;
    float rotation = 0;
    public Derp() {
        super("Derp", Category.Render);
        add(server = new BooleanSetting("Server", true));
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        boolean can = (Client.moduleManager.getModule("AimBot").isEnabled() ? !((newAimBot)Client.moduleManager.getModule("AimBot")).work : true);
        if (can){
            if (!(Mouse.isButtonDown(0) || Mouse.isButtonDown(1))){
                if (server.getVal()){
                    event.setRotationYaw(rotation);
                    event.setRotationPitch(45);
                }

                mc.player.rotationYawHead=rotation;
                mc.player.renderYawOffset = rotation;
                mc.player.rotationPitchHead = 45;
                rotation+=100;
                if (rotation > 180){
                    rotation=-180;
                }
            }

        }

    }
}
