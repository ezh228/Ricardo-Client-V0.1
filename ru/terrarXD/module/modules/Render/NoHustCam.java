package ru.terrarXD.module.modules.Render;

import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender3D;

/**
 * @author zTerrarxd_
 * @since 12:22 of 20.06.2023
 */
public class NoHustCam extends Module {

    public NoHustCam() {
        super("NoHustCam", Category.Render);

    }

    @EventTarget
    public void onRender3D(EventRender3D event){
        mc.player.hurtTime = 0;
    }

}
