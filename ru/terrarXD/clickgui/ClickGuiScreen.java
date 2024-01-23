package ru.terrarXD.clickgui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.modules.Hud.ClickGui;
import ru.terrarXD.shit.config.ConfigGuiIMG;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.settings.ModeSetting;
import ru.terrarXD.shit.utils.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 14:47 of 19.04.2023
 */
public class ClickGuiScreen extends GuiScreen {
    public float posX=100;
    public float posY=100;
    public float sizeX=500;
    public float sizeY=300;
    public ArrayList<Panel> panels = new ArrayList<>();
    public Panel currentPanel = null;
    boolean drag=false;
    float dragX;
    float dragY;

    boolean dragSize=false;
    AnimationUtils anim;
    AnimationUtils animAnime;

    public ClickGuiScreen(){
        for (Category category : Category.values()){
            panels.add(new Panel(category));
        }
        anim = new AnimationUtils(0, 255, 1f);
        animAnime = new AnimationUtils(-500, 0, 0.5f);
    }
    public boolean canDrag = true;

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        Client.configManager.save();
    }

    @Override
    public void initGui() {
        super.initGui();
        anim.setAnim(50);
        anim.speed = 0.1f;
        for(Panel panel : panels){
            panel.reset();
        }
        animAnime.setAnim(500);


    }
    public int getColor(int color){
        return ColorUtils.swapAlpha(color, (int) ((new Color(color).getAlpha() * anim.getAnim())/255));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ClickGui module = (ClickGui) Client.moduleManager.getModule("ClickGui");
        if (module.blur.getVal()){
            GaussianBlur.renderBlur((int)module.blurradius.getVal(), ()->RenderUtils.drawRect(0, 0, width, height, -1));

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



        canDrag = true;
        if (drag){
            if (!Mouse.isButtonDown(0)){
                drag = false;
            }
            posX = mouseX-dragX;
            posY = mouseY-dragY;
        }
        if (dragSize){
            if (!Mouse.isButtonDown(0)){
                dragSize = false;
            }
            sizeX = MathUtils.clamp(mouseX-posX, 350, 1000) ;
            sizeY = MathUtils.clamp(mouseY-posY, 200, 1000) ;
        }
        GL11.glPushMatrix();
        RenderUtils.customScaledObject2D(posX, posY, width, height, 1);
        int colorMain = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(getColor()), 0.9d).getRGB());
        int colorCategorys = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(getColor()), 0.95d).getRGB());

        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX, posY, posX+sizeX, posY+sizeY, 10, 0, colorMain, colorMain, colorMain, colorMain, false, true, false);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(posX-10, posY-10, 120, sizeY+10);
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(posX, posY, posX+sizeX, posY+sizeY, 10, 0, colorCategorys, colorCategorys, colorCategorys, colorCategorys, false, true, false);

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        Fonts.main_36.drawCenteredString(Client.NAME.substring(0, 1)+"           ", posX+55, posY+15, Client.clickGuiScreen.getColor());

        Fonts.main_36.drawCenteredString("  "+Client.NAME.substring(1, Client.NAME.length()), posX+55, posY+15, getColor(-1));
        Fonts.main_12.drawString("Client " + Client.VERSION, posX+55, posY+40, getColor(-1));
        float yPanels = posY+ 50;
        for (Panel panel : panels){
            panel.drawScreen(posX, yPanels, mouseX, mouseY);
            yPanels+=panel.getHeight();
        }

        if(currentPanel == null){
            Fonts.main_25.drawCenteredString("Всеми знакомая легенда", posX+120+(sizeX-120)/2, posY+(sizeY/2)-Fonts.main_25.getHeight()+3, getColor(-1));
            Fonts.main_25.drawCenteredString("в новом облике", posX+120+(sizeX-120)/2, posY+(sizeY/2), getColor(-1));
        }else {
            currentPanel.drawScreenMods(posX+120, posY, mouseX, mouseY);
        }
        GL11.glPopMatrix();





    }



    public int getColor(){
        return getColor(Client.getColor());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        float yPanels = posY + 50;
        for (Panel panel : panels){
            panel.mouseClicked(posX, yPanels, mouseX, mouseY, mouseButton);
            yPanels+=panel.getHeight();
        }
        if (isHover(posX+sizeX-10, posY+sizeY-10, posX+sizeX, posY+sizeY, mouseX, mouseY) && mouseButton == 0){
            canDrag = false;
            dragSize=true;

        }
        if (canDrag && mouseButton == 0 && isHover(posX, posY, posX+sizeX, posY+sizeY, mouseX, mouseY)){
            drag = true;
            dragX = mouseX-posX;
            dragY = mouseY-posY;
        }
        if (currentPanel != null){
            currentPanel.mouseClickedMods(posX+120, posY, mouseX, mouseY, mouseButton);
        }

    }
    public boolean isHover(float x, float y, float x2, float y2, float mouseX, float mouseY){
        return mouseX>x && mouseX < x2 && mouseY>y && mouseY<y2;
    }
}
