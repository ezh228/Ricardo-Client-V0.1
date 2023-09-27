package ru.terrarXD.shit;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.utils.GaussianBlur;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;
import java.io.IOException;

/**
 * @author zTerrarxd_
 * @since 14:12 of 22.06.2023
 */
public class GuiAltManager extends GuiScreen {
    GuiTextField guiTextField;

    public GuiAltManager(){
        guiTextField = new GuiTextField(1337, fontRendererObj, 500, 500, 200, 20);
    }

    @Override
    public void initGui() {
        super.initGui();
        guiTextField = new GuiTextField(1337, fontRendererObj, width/2-100, height/2-10, 200, 20);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        guiTextField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RenderUtils.drawRect(0, 0, width, height, new Color(0, 0, 0).getRGB());

        guiTextField.xPosition = width/2-100;
        guiTextField.yPosition = height/2+100;
        guiTextField.setMaxStringLength(60);
        Fonts.main_18.drawCenteredString("Напиши ник", width/2, height/2-100, -1);

        guiTextField.drawTextBox();

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        guiTextField.setFocused(true);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        guiTextField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_RETURN) {
            Minecraft.getMinecraft().getSession().username = guiTextField.getText();
            mc.displayGuiScreen(new GuiMainMenu());
        }
    }
}
