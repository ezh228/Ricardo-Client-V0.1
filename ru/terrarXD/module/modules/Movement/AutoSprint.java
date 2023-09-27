package ru.terrarXD.module.modules.Movement;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;

/**
 * @author zTerrarxd_
 * @since 17:06 of 21.04.2023
 */
public class AutoSprint extends Module {
    public AutoSprint() {
        super("AutoSprint", Category.Movement);
    }
    @EventTarget
    public void onUpdate(EventUpdate event){
        mc.gameSettings.keyBindSprint.pressed = true;
    }
}
