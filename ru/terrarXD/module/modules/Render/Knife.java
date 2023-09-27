package ru.terrarXD.module.modules.Render;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.utils.RenderUtils;

/**
 * @author zTerrarxd_
 * @since 22:11 of 23.05.2023
 */
public class Knife extends Module {

    public Knife() {
        super("Knife", Category.Render);
    }

    @EventTarget
    public void onRender2D(EventRender2D event){
        RenderUtils.drawImage("knife", 0, 0, GuiScreen.width, GuiScreen.height, -1);
    }
}
