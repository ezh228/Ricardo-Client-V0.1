package ru.terrarXD.module.modules.Player;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventFullCube;
import ru.terrarXD.shit.event.events.EventPush;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.Utils;

/**
 * @author zTerrarxd_
 * @since 1:06 of 15.06.2023
 */
public class NoClip extends Module {
    FloatSetting speed; 
    public NoClip() {
        super("NoClip", Category.Player);
        add(speed = new FloatSetting("Speed", 0.01f, 2, 0.5f, 0.01f));
    }

    @Override
    public void onEnable() {
        super.onEnable();

    }

    @EventTarget
    public void onFullCube(EventFullCube event) {
        if (mc.world != null) {
            event.setCancelled(true);
        }
    }

    @EventTarget
    public void onPush(EventPush event) {
        event.setCancelled(true);
    }

    @EventTarget
    public void onLivingUpdate(EventUpdate event) {
        if (mc.player != null) {
            mc.player.noClip = true;
            mc.player.motionY = 0;
            mc.player.onGround = false;
            mc.player.capabilities.isFlying = false;
            Utils.setSpeed(speed.getVal());
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY += 0.1;
            }
            if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY -= 0.1;
            }
            event.setCancelled(false);
        }
    }
}
