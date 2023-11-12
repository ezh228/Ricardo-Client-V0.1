package ru.terrarXD.module.modules.Player;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPacketReceive;

/**
 * @author zTerrarxd_
 * @date 26.10.2023 23:45
 */
public class NoServerRotation extends Module {
    public NoServerRotation() {
        super("NoServerRotation", Category.Player);
    }

    @EventTarget
    public void onPacketResive(EventPacketReceive event){
        if (event.getPacket() instanceof SPacketPlayerPosLook){
            SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.getPacket();
            packet.yaw = mc.player.rotationYaw;
            packet.pitch = mc.player.rotationPitch;
        }
    }
}
