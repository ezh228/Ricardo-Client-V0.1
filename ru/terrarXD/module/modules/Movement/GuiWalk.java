package ru.terrarXD.module.modules.Movement;

import org.lwjgl.input.Keyboard;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.settings.BooleanSetting;

/**
 * @author zTerrarxd_
 * @since 21:40 of 25.04.2023
 */
public class GuiWalk extends Module {
    BooleanSetting sneak;

    public GuiWalk() {
        super("GuiWalk", Category.Movement);
        add(sneak = new BooleanSetting("Sneak", false));
    }
    @EventTarget
    public void onUpdate(EventUpdate event){
        if(mc.currentScreen != null && !(mc.currentScreen instanceof net.minecraft.client.gui.GuiChat)){
            mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
            mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode());
            mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode());
            mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode());
            mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode());
            mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());

            if(sneak.getVal()){
                mc.gameSettings.keyBindSneak.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
            }
            mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }
}
