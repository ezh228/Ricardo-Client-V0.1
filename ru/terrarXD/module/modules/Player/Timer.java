package ru.terrarXD.module.modules.Player;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.FloatSetting;

/**
 * @author zTerrarxd_
 * @since 16:05 of 08.06.2023
 */
public class Timer extends Module {
    FloatSetting power;
    public Timer() {
        super("Timer", Category.Player);
        add(power = new FloatSetting("Power", 0, 2, 1.5f, 0.1f));
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        mc.timer.field_194147_b = power.getVal();
    }
}
