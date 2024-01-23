package ru.terrarXD.module.modules.Player;

import net.minecraft.network.play.server.SPacketTitle;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventPacketReceive;
import ru.terrarXD.shit.utils.Utils;

/**
 * @author zTerrarxd_
 * @date 22.01.2024 22:49
 */
public class HitSound extends Module {
    public HitSound() {
        super("HitSound", Category.Player);
    }

    @EventTarget
    public void onPacket(EventPacketReceive eventPacket) {
        if (eventPacket.getPacket() instanceof SPacketTitle) {
            SPacketTitle title = (SPacketTitle) eventPacket.getPacket();
            String m = title.getMessage().getUnformattedText();
            StringBuilder builder = new StringBuilder();
            char[] buffer = m.toCharArray();
            for (int i = 0; i < buffer.length; i++) {
                if (buffer[i] == '§') {
                    i++;
                } else {
                    builder.append(buffer[i]);
                }
            }
            if (m.contains("HP)")) {
                Utils.playSound("hit.wav");
            }
        }
    }
}
