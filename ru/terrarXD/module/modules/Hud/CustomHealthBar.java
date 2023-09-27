package ru.terrarXD.module.modules.Hud;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.HudModule;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventRender3D;

/**
 * @author zTerrarxd_
 * @since 23:53 of 14.06.2023
 */
public class CustomHealthBar extends HudModule {
    public CustomHealthBar() {
        super("CustomHealthBar", Category.Hud, 150, 50);
    }
    @EventTarget
    public void onRender2D(EventRender2D event){

    }

}
