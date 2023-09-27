package ru.terrarXD.module.modules.Player;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.RPC;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;

/**
 * @author zTerrarxd_
 * @since 16:10 of 08.06.2023
 */
public class DiscordRPC extends Module {
    public DiscordRPC() {
        super("DiscordRPC", Category.Player);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        RPC.startRPC();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        RPC.stopRPC();
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        if (mc.player.ticksExisted % 10 == 0){
            RPC.update();
        }
    }
}
