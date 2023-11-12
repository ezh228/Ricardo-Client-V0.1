package ru.terrarXD.clickgui.screen;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.clickgui.Mod;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * @author zTerrarxd_
 * @date 11.11.2023 13:05
 */
public class ScreenSearch extends Screen{
    AnimationUtils scroll = new AnimationUtils(0, 0, 0.1f);
    ArrayList<Mod> mods = new ArrayList<>();

    public ScreenSearch(){
        for (Module module : Client.moduleManager.modules){
            mods.add(new Mod(module));
        }
    }


    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        int sc = Mouse.getDWheel();
        anim.speed = 0.08f;

        if (sc>0){
            scroll.to+=7;
        }else if (sc<0){
            scroll.to-=7;
        }
        if (scroll.to > 0){
            scroll.to = 0;
        }
        int pos1 = (int) (0+scroll.getAnim());
        int pos2 = (int) (0+scroll.getAnim());
        int p1 = 0;
        int p2 = 0;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        RenderUtils.scissor(x-1, y-1, getWidth()+2, getHeight()+2);

        for (Mod mod : mods){
            if (mod.getModule().getName().toLowerCase(Locale.ROOT).contains(Client.clickGuiScreen.textField.getText().toLowerCase(Locale.ROOT))){
                if (p1 <= p2){
                    mod.drawScreen(x, y+pos1, mouseX, mouseY);
                    pos1+=mod.getHeight()+10;
                    p1+=mod.getStaticHeight()+10;
                }else {
                    mod.drawScreen(x+mod.getWidth()+10, y+pos2, mouseX, mouseY);
                    pos2+=mod.getHeight()+10;
                    p2+=mod.getStaticHeight()+10;
                }
            }

        }
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        float height = pos1;

        if (pos2 > pos1){
            height = pos2;
        }
        //RenderUtils.drawRect(0, y+height, 100, y+height+2, -1);
        height-=scroll.getAnim();

        if (height-10 < getHeight()){
            return;
        }
        //height+=40f;
        float path = (height / getHeight());

        if (y-scroll.getAnim()+getHeight()/path > y+getHeight()){
            //scroll.to = -(getHeight()/path);
        }
    }


    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        int pos1 = (int) (0+scroll.getAnim());
        int pos2 = (int) (0+scroll.getAnim());
        for (Mod mod : mods){
            if (mod.getModule().getName().toLowerCase(Locale.ROOT).contains(Client.clickGuiScreen.textField.getText().toLowerCase(Locale.ROOT))){
                if (pos1 <= pos2){
                    mod.mouseClicked(x, y+pos1, mouseX, mouseY, button);
                    pos1+=mod.getHeight()+10;
                }else {
                    mod.mouseClicked(x+mod.getWidth()+10, y+pos2, mouseX, mouseY,button);
                    pos2+=mod.getHeight()+10;
                }
            }

        }
    }
}
