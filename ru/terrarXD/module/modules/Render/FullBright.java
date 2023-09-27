package ru.terrarXD.module.modules.Render;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;

/**
 * @author zTerrarxd_
 * @since 13:39 of 25.04.2023
 */
public class FullBright extends Module {
    public FullBright() {
        super("FullBright", Category.Render);
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        mc.gameSettings.gammaSetting = 100;
    }
}
