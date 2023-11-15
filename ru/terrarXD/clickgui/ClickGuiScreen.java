package ru.terrarXD.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.world.BossInfo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.clickgui.screen.Screen;
import ru.terrarXD.clickgui.screen.ScreenMods;
import ru.terrarXD.clickgui.screen.ScreenSearch;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.modules.Hud.ClickGui;
import ru.terrarXD.module.modules.Hud.Notifications;
import ru.terrarXD.shit.config.ConfigGuiIMG;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.utils.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 17:41
 */
public class ClickGuiScreen extends GuiScreen {
    int posX = 100;
    int posY = 100;
    boolean drag =  false;
    float dragX;
    float dragY;
    public ru.terrarXD.shit.utils.TextField textField;
    AnimationUtils animAnime;
    AnimationUtils animDark = new AnimationUtils(0, 0, 0.1f);


    public int WIDTH;
    public int HEIGHT;
    ArrayList<Panel> panels = new ArrayList<>();
    ArrayList<Screen> screens = new ArrayList<>();

    public Screen cuuentScreen;

    public Panel currentPanel;

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Client.configManager.save();

    }

    public ClickGuiScreen(){
        for (Category category : Category.values()){
            panels.add(new Panel(category.name()));
        }
        for (Category category : Category.values()){
            screens.add(new ScreenMods(category));

        }
        animAnime = new AnimationUtils(-500, 0, 0.5f);
        screens.add(new ScreenSearch());
    }

    @Override
    public void initGui() {
        super.initGui();
        textField = new ru.terrarXD.shit.utils.TextField(1337, Fonts.main_18, width/2-100, height/2-10, 200, 20);
        textField.setText("Поиск...");
        if (width < 960){
            WIDTH = (int) (960/2.1f);
            HEIGHT = 960 * 9 / 16;
        }else {
            WIDTH = (int) (width/2.1f);
            HEIGHT = WIDTH * 9 / 16;
        }

        animAnime.setAnim(500);
        animDark.setAnim(0f);
        animDark.to = 1f;

    }

    public void setCurrentPanel(Panel panel){
        textField.setFocused(false);
        textField.setText("Поиск...");
        currentPanel = panel;
        for (Category category : Category.values()){
            if (category.name().equals(panel.name)){
                for (Screen screen : screens){
                    if (screen instanceof ScreenMods &&  ((ScreenMods) screen).getCategory() == category){
                        cuuentScreen = screen;
                        cuuentScreen.init();
                        return;
                    }
                }
            }
        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (!textField.getText().equals("Поиск...")){
            for (Screen screen : screens){
                if (screen instanceof ScreenSearch){
                    cuuentScreen = screen;
                }
            }
        }

        ClickGui module = (ClickGui) Client.moduleManager.getModule("ClickGui");
        if (drag){
            if (!Mouse.isButtonDown(0)){
                drag = false;
            }
            posX = (int) (mouseX-dragX);
            posY = (int) (mouseY-dragY);
        }
        if (module.blur.getVal()){
            GaussianBlur.renderBlur((int)module.blurradius.getVal(), ()->RenderUtils.drawRect(0, 0, width, height, -1));
        }
        if (module.darkness.getVal()){
            RenderUtils.drawRect(0, 0, width, height, ColorUtils.swapAlpha(Color.BLACK.getRGB(), (int) (100*animDark.getAnim())));
        }
        if (module.anime.getVal()){
            //RenderUtils.drawRect(0, 0, width, height, -1);

            String val = module.animee.getVal();
            ConfigGuiIMG.IMGGUI img = null;
            for (ConfigGuiIMG.IMGGUI imggui : Client.configManager.configGuiIMG.imgguis){
                if (imggui.getName().replace(".png", "").equals(val)){
                    img = imggui;
                }
            }

            BufferedImage bimg = img.getBufferedImage();


            int width1          = bimg.getWidth();
            int height1         = bimg.getHeight();
            float size = module.animeSize.getVal();
            if (width > height){
                int w = (int) (100*size);
                int h = (int) ((100*size)*width1/height1);
                RenderUtils.drawImage(img.getResourceLocation(), (int) (width-w+module.animeX.getVal()+animAnime.getAnim()), (int) (height-h+module.animeY.getVal()), w, h, -1);

            }else {
                int h = (int) (100*size);
                int w = (int) ((100*size)*height1/width1);
                RenderUtils.drawImage(img.getResourceLocation(), (int) (width-w+module.animeX.getVal()+animAnime.getAnim()), (int) (height-h+module.animeY.getVal()), w, h, -1);

            }
        }

        int colorMain1 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.9d).getRGB();
        int colorMain2 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.85d).getRGB();
        int colorMain3 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.8d).getRGB();
        int r = 3;
        //RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX-r, posY-r, posX+ WIDTH+r, posY+HEIGHT+r, 10,1f, -1, -1, -1, -1, true, true, true);
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX, posY, posX+ WIDTH, posY+HEIGHT, 10, 1f, colorMain1, colorMain2, colorMain3, colorMain2, false, true, true);
        RenderUtils.drawImage("logo_new", posX+WIDTH-100, posY+HEIGHT-100, 100, 100, new Color(29, 29, 29, 20).getRGB());
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(posX, posY, 100, 1000);
        int color = new Color(29, 29, 29, 50).getRGB();
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX+3, posY+3, posX+ WIDTH-3, posY+HEIGHT-3, 10, 1f, color, color, color, color, false, true, true);
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        //Panels
        Fonts.main_25.drawCenteredString(Client.NAME_FULL, posX+100/2, posY+60/2-Fonts.main_25.getHeight()/2, -1);
        int pos = 100/2+ Fonts.main_25.getStringWidth(Client.NAME_FULL)/2;
        Fonts.main_12.drawString(Client.VERSION, posX+pos-Fonts.main_12.getStringWidth(Client.VERSION), posY+60/2+Fonts.main_25.getHeight()/2+2, -1);
        if (cuuentScreen != null){
            cuuentScreen.drawScreen(posX+100+20, posY+40, mouseX, mouseY);
        }else {
            Fonts.main_25.drawCenteredString("Всеми знакомая легенда", posX+120+(WIDTH-120)/2, posY+(HEIGHT/2)-Fonts.main_25.getHeight()+3, -1);
            Fonts.main_25.drawCenteredString("в новом облике", posX+120+(WIDTH-120)/2, posY+(HEIGHT/2), -1);
        }
        int y = 60;
        for (Panel panel : panels){
            panel.drawScreen(posX+10, posY+y, mouseX, mouseY);
            y+=panel.getHeight()+2;
        }
        textField.xPosition = posX+100+(WIDTH-100)/2+5+3;
        textField.yPosition = posY+10+15/2-Fonts.main_18.getHeight()/2;
        textField.height = 15;

        textField.width = (Client.clickGuiScreen.WIDTH-100-40-10)/2-(textField.height*2)-20+2;
        textField.setMaxStringLength(60);
        textField.setEnableBackgroundDrawing(false);
        int c = new Color(29, 29, 29).getRGB();

        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(textField.xPosition-3, textField.yPosition-4, textField.xPosition+textField.getWidth()-3, textField.yPosition+textField.height-4, 5, 1f, c, c, c, c, false, true, true);
        textField.drawTextBox();
        Fonts.icons_18.drawString("G", textField.xPosition+textField.getWidth()-3-Fonts.icons_18.getStringWidth("G")-5, textField.yPosition-4+textField.height/2-Fonts.icons_18.getHeight()/2+1, -1);

        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(textField.xPosition+textField.getWidth()-3+10-3, textField.yPosition-4,textField.xPosition+textField.getWidth()-3+10+textField.height-1, textField.yPosition+textField.height-4, 5, 1f, c, c, c, c, false, true, true);
        RenderUtils.drawImage("discord", textField.xPosition+textField.getWidth()-3+10, textField.yPosition-2,textField.height-4, textField.height-4, -1);

        int add = 25;
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(textField.xPosition+textField.getWidth()-3+10-3+add, textField.yPosition-4,textField.xPosition+textField.getWidth()-3+10+textField.height+add-1, textField.yPosition+textField.height-4, 5, 1f, c, c, c, c, false, true, true);
        RenderUtils.drawImage("telegram", textField.xPosition+textField.getWidth()-3+10+add, textField.yPosition-2,textField.height-4, textField.height-4, -1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
        if (keyCode == Keyboard.KEY_RETURN) {

        }
    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (cuuentScreen != null){
            cuuentScreen.mouseClicked(posX+100+20, posY+40, mouseX, mouseY, mouseButton);
        }
        int y = 60;
        for (Panel panel : panels){
            panel.mouseClicked(posX+10, posY+y, mouseX, mouseY, mouseButton);
            y+=panel.getHeight()+2;
        }
        if (isHover(posX, posY, posX+100, posY+60, mouseX, mouseY) && mouseButton == 0){
            drag = true;
            dragX = mouseX-posX;
            dragY = mouseY-posY;
        }
        if (isHover(textField.xPosition-3, textField.yPosition-4, textField.xPosition+textField.getWidth()-3, textField.yPosition+textField.height-4, mouseX, mouseY) && mouseButton == 0){
            textField.setFocused(true);
            textField.setText("");
        }else {

        }
        int add = 25;
        if (isHover(textField.xPosition+textField.getWidth()-3+10-3, textField.yPosition-4,textField.xPosition+textField.getWidth()-3+10+textField.height-1, textField.yPosition+textField.height-4, mouseX, mouseY)){
            Utils.openWebpage("https://discord.gg/b5KwppSgM7");
        }
        if (isHover(textField.xPosition+textField.getWidth()-3+10-3+add, textField.yPosition-4,textField.xPosition+textField.getWidth()-3+10+textField.height+add-1, textField.yPosition+textField.height-4, mouseX, mouseY)){
            Utils.openWebpage("https://t.me/ricardo_client_offical");
        }

    }
    public boolean isHover(float x, float y, float x2, float y2, float mouseX, float mouseY){
        return mouseX>x && mouseX < x2 && mouseY>y && mouseY<y2;
    }
}
