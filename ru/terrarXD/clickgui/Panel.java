package ru.terrarXD.clickgui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.utils.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 17:22 of 19.04.2023
 */
public class Panel extends Comp{
    Category category;
    AnimationUtils animSelect;
    AnimationUtils scroll;
    ArrayList<Mod> mods = new ArrayList<>();
    public Panel(Category category){
        this.category = category;
        animSelect = new AnimationUtils(0, 0, 0.1f);
        scroll = new AnimationUtils(0, 0, 0.1f);
        for (Module module : Client.moduleManager.modules){
            if (module.getCategory() == category){
                mods.add(new Mod(module));
            }
        }
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        animSelect.to = 0;
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY)){
            Client.clickGuiScreen.canDrag = false;
            animSelect.to+=70;
        }
        if (this == Client.clickGuiScreen.currentPanel){
            animSelect.to+=150;
        }else {
            for (Mod mod : mods){
                mod.reset();
            }
        }
        int colo = Client.clickGuiScreen.getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.7d).getRGB());

        RenderUtils.drawRect(x, y, x+getWidth(), y+getHeight(), ColorUtils.swapAlpha(colo, (int) animSelect.getAnim()));
        if (Client.clickGuiScreen.currentPanel == this){
            BloomUtil.renderBlur(()->Fonts.main_18.drawCenteredString(category.name(), x+getWidth()/2, y+getHeight()/2-(Fonts.main_18.getHeight()/2), Client.clickGuiScreen.getColor(Client.clickGuiScreen.currentPanel == this ? -1 : new Color(111, 107, 115).getRGB())));
        }
        Fonts.main_18.drawCenteredString(category.name(), x+getWidth()/2, y+getHeight()/2-(Fonts.main_18.getHeight()/2), Client.clickGuiScreen.getColor(Client.clickGuiScreen.currentPanel == this ? -1 : new Color(111, 107, 115).getRGB()));
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY) && button == 0){
            Client.clickGuiScreen.currentPanel = this;
        }
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void reset() {
        super.reset();
        animSelect.setAnim(0);
        for (Mod mod : mods){
            mod.reset();
        }
    }

    @Override
    public float getHeight() {
        return MathUtils.clamp((Client.clickGuiScreen.sizeY-70)/Category.values().length, 20, 30);
    }

    @Override
    public float getWidth() {
        return 110;
    }



    public void drawScreenMods(float x, float y, float mouseX, float mouseY){
        int sc = Mouse.getDWheel();
        //scroll.to = 0;
        if (sc>0){
            scroll.to+=7;
        }else if (sc<0){
            scroll.to-=7;

        }
        float yMods = y+5+scroll.getAnim();
        float yMods2 = y+5+scroll.getAnim();
        boolean left = true;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        RenderUtils.scissor(x, y+5, Client.clickGuiScreen.sizeX-120, Client.clickGuiScreen.sizeY-10);
        for (Mod mod : mods){
            mod.drawScreen(left? x : x+(Client.clickGuiScreen.sizeX-120-15)/2+5, left ? yMods : yMods2, mouseX, mouseY);
            if (left){
                yMods+=mod.getHeight()+5;
            }else {
                yMods2+=mod.getHeight()+5;
            }
            left=!left;
        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

    }
    public void mouseClickedMods(float x, float y, float mouseX, float mouseY, int button){
        float yMods = y+5+scroll.getAnim();
        float yMods2 = y+5+scroll.getAnim();
        boolean left = true;
        for (Mod mod : mods){
            mod.mouseClicked(left? x : x+(Client.clickGuiScreen.sizeX-120-15)/2+5, left ? yMods : yMods2, mouseX, mouseY, button);
            if (left){
                yMods+=mod.getHeight()+5;
            }else {
                yMods2+=mod.getHeight()+5;
            }
            left=!left;
        }
    }
}
