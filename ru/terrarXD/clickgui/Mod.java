package ru.terrarXD.clickgui;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.clickgui.set.*;
import ru.terrarXD.module.BindType;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.*;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 20:06 of 19.04.2023
 */
public class Mod extends Comp{
    ArrayList<Set> sets = new ArrayList<>();
    boolean binding = false;
    Module module;
    AnimationUtils anim;
    float sz;


    AnimationUtils animEnable;
    public Mod(Module module){
        this.module= module;
        anim = new AnimationUtils(0, 255, 0.1f);
        animEnable = new AnimationUtils(0, 0, 0.1f);
        for (Setting setting : module.getSettings()){
            if (setting instanceof BooleanSetting){
                sets.add(new CheckBox((BooleanSetting) setting));
            }
            if (setting instanceof FloatSetting){
                sets.add(new Slider((FloatSetting) setting));
            }
            if (setting instanceof ColorSetting){
                sets.add(new ColorPiker((ColorSetting) setting));
            }
            if (setting instanceof ModeSetting){
                sets.add(new ComboBox((ModeSetting) setting));
            }
        }
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);
        if (isHover(x, y, x+getWidth(), y+getHeight(), mouseX, mouseY)){
            Client.clickGuiScreen.canDrag = false;
        }
        int color = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.95d).getRGB());
        int color2 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.7d).getRGB());
        int color3 = getColor(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.clickGuiScreen.getColor()), 0.6d).getRGB());
        if (binding){
            RenderUtils.drawRoundedRect(x, y, x+getWidth(), y+getHeight(),10,color);

            if (Keyboard.isKeyDown(Keyboard.getEventKey())){
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
                    binding = false;

                } else if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)){
                    binding = false;
                    module.setKey(0);
                }else {
                    module.setKey(Keyboard.getEventKey());
                    binding = false;
                }
            }
            Fonts.main_20.drawString(BindType.Toggle.name(), x+10, y+10-(Fonts.main_20.getHeight()/2), getColor(module.getBindType() == BindType.Toggle ? -1 : new Color(200, 200, 200).getRGB()));
            Fonts.main_20.drawString(BindType.Hold.name(), x+10+5+Fonts.main_20.getStringWidth(BindType.Toggle.name()), y+10-(Fonts.main_20.getHeight()/2),getColor( module.getBindType() == BindType.Hold ? -1 : new Color(200, 200, 200).getRGB()));
            Fonts.main_20.drawString( "["+Keyboard.getKeyName(module.getKey())+"]", x+getWidth()-5-Fonts.main_20.getStringWidth("["+Keyboard.getKeyName(module.getKey())+"]"), y+10-(Fonts.main_20.getHeight()/2), getColor(-1));
        }else {
            animEnable.to = module.isEnabled() ? 1 : 0;


            RenderUtils.drawRoundedRect(x, y, x+getWidth(), y+getHeight(),10,color);
            Fonts.main_20.drawString(module.getName(), x+10, y+10-(Fonts.main_20.getHeight()/2), getColor(-1));
            float posX = x+getWidth()-10-10;
            for (int i = 0; i < 10; i++) {
                RenderUtils.drawCircle(posX+i, y+10,4, getColor(module.isEnabled() ? color2 : new Color(40, 40, 40).getRGB()));
            }
            RenderUtils.drawCircle(posX+(10*animEnable.getAnim()), y+10, 3.5f, getColor(module.isEnabled() ? color3 : new Color(60, 60, 60).getRGB()));
        }


        //GL11.glColor4f(1, 1, 1, 1);
        //Sets


        //StencilUtil.initStencilToWrite();
        //RenderUtils.drawRoundedRect(x, y+20, x+getWidth(), y+getHeight(),10,color);
        //StencilUtil.readStencilBuffer(1);
        float ySets = y+ 20;
        float size = 20;

        for (Set set : sets){
            if (set.getSetting().isVisable()){
                GL11.glColor4f(0, 0, 0, 0);

                GlStateManager.color((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, 255);


                set.drawScreen(x+5,ySets, mouseX, mouseY);
                GlStateManager.clearColor(1, 1, 1, 1);

                ySets+=set.getHeight();
                size+=set.getHeight();
            }

        }
        sz = size;




        //StencilUtil.uninitStencilBuffer();
    }
    public int getColor(int color){
        return ColorUtils.swapAlpha(color, (int) ((new Color(color).getAlpha() * anim.getAnim())/255));
    }
    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+20, mouseX, mouseY)){
            if (binding){
                if (isHover(x+10, y+10-(Fonts.main_20.getHeight()/2), x+10+Fonts.main_20.getStringWidth(BindType.Toggle.name()), y+10+(Fonts.main_20.getHeight()/2), mouseX, mouseY) && button == 0){
                    module.setBindType(BindType.Toggle);
                }
                if (isHover(x+10+5+Fonts.main_20.getStringWidth(BindType.Toggle.name()), y+10-(Fonts.main_20.getHeight()/2), x+10+5+Fonts.main_20.getStringWidth(BindType.Toggle.name())+Fonts.main_20.getStringWidth(BindType.Toggle.name()), y+10+(Fonts.main_20.getHeight()/2), mouseX, mouseY) && button == 0){
                    module.setBindType(BindType.Hold);
                }
            }else {
                if (button == 0){
                    module.toggle();

                }
            }

            if (button == 2){
                binding = true;
            }
        }
        float ySets = y+ 20;
        for (Set set : sets){
            if (set.getSetting().isVisable()) {
                set.mouseClicked(x+5,ySets, mouseX, mouseY, button);
                ySets+=set.getHeight();
            }

        }

    }

    @Override
    public float getWidth() {
            return (Client.clickGuiScreen.sizeX-120-15)/2;
    }

    @Override
    public float getHeight() {
        return sz;
    }

    @Override
    public void reset() {
        super.reset();
        animEnable.setAnim(0);
        anim.setAnim(0);
        for (Set set : sets){
            set.reset();
        }
    }
}
