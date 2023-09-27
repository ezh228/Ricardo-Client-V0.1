package ru.terrarXD.module.modules.Player;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.settings.BooleanSetting;

/**
 * @author zTerrarxd_
 * @since 14:40 of 17.06.2023
 */
public class StreamerMode extends Module {
    public BooleanSetting nickProtect;
    public BooleanSetting friends;
    public StreamerMode() {
        super("StreamerMode", Category.Player);
        add(nickProtect = new BooleanSetting("Nick-Protect", true));
        add(friends = new BooleanSetting("Friends-Protect", false));
    }

}
