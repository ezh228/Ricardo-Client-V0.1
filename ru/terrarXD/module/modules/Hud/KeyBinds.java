package ru.terrarXD.module.modules.Hud;

import net.minecraft.client.gui.GuiChat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.module.HudModule;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.event.events.EventUpdate;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;
import ru.terrarXD.shit.utils.StencilUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author zTerrarxd_
 * @since 13:07 of 15.06.2023
 */
public class KeyBinds extends HudModule {
    ArrayList<Mod> mods = new ArrayList<>();
    AnimationUtils alpha = new AnimationUtils(0, 0, 0.1f);
    AnimationUtils size;
    public KeyBinds() {
        super("KeyBinds", Category.Hud, 120, 20);
        size = new AnimationUtils(0, getSizeY(), 0.1f);
    }


    @Override
    public void onEnable() {
        super.onEnable();
        mods.clear();
        size.speed = 0.3f;
        if (Client.moduleManager != null){
            for (Module module : Client.moduleManager.modules){
                if (module.getKey() != 0){
                    mods.add(new Mod(module));
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event){
        if (mc.player.ticksExisted % 10 == 0){
            for (Module module : Client.moduleManager.modules){
                if (module.getKey() != 0){
                    boolean b = false;
                    for (Mod mod : mods){
                        if (mod.getModule() == module){
                            b = true;
                        }
                    }
                    if (!b){
                        mods.add(new Mod(module));
                    }
                }
            }
            mods.sort(new Comparator<Mod>() {
                @Override
                public int compare(Mod o1, Mod o2) {
                    return Fonts.main_16.getStringWidth(o2.module.getName()) - Fonts.main_16.getStringWidth(o1.module.getName());
                }
            });
        }
    }


    @EventTarget
    public void onRender2D(EventRender2D event){
        if (mc.currentScreen instanceof GuiChat){
            //return;
        }
        //setSizeY(15);
        StencilUtil.initStencilToWrite();
        RenderUtils.drawRoundedRect(getPosX(), getPosY(), getPosX() + getSizeX(), getPosY() + getSizeY(), 5, -1);

        StencilUtil.readStencilBuffer(1);
        setSizeX(100);
        int step = 1;
        int colorMain = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.92d).getRGB();

        RenderUtils.drawRoundedRect(getPosX(), getPosY(), getPosX() + getSizeX(), getPosY() + getSizeY(), 5, colorMain);
        RenderUtils.drawRect(getPosX() + 3, getPosY() + 15-2, getPosX() + getSizeX() - 3, getPosY() + 14, ColorUtils.swapAlpha(Client.getColor(), (int) alpha.getAnim()));

        Fonts.main_16.drawCenteredString(getName(), getPosX() + getSizeX() / 2, getPosY() + 15/2 - Fonts.main_16.getHeight() / 2, Client.getColor());
        int i = 15;
        for (Mod mod : mods){
            mod.render((int) getPosX(), (int) (getPosY() + i), step);
            step++;
            i+=mod.getHeight();
        }
        if (i>15){
            alpha.to = 255;
        }else {
            alpha.to = 0;
        }
        size.to = i;

        setSizeY((int) size.getAnim());
        StencilUtil.uninitStencilBuffer();

    }



    public class Mod{
        public Module module;
        AnimationUtils anim;
        public Mod(Module module){
            this.module = module;
            anim = new AnimationUtils(1.1f, 1, 0.1f);
        }

        public void render(int posX, int posY, int step){
            float scale = anim.getAnim();
            int alpha = (int) ((1 - ((scale - 1) * 10)) * 255);
            if (!(mc.currentScreen instanceof GuiChat)){
                anim.to = module.isEnabled() ? 1 : 1.1f;

            }
            if ( 1.1f - scale < 0.01f){
                return;
            }
            GL11.glPushMatrix();
            RenderUtils.customScaledObject2D(posX, posY, getSizeX(), 10, scale);
            Fonts.main_16.drawCenteredString(module.getName(), posX + getSizeX() / 2, posY + 10 / 2 - Fonts.main_16.getHeight() / 2, ColorUtils.swapAlpha(-1, alpha));
            Fonts.main_16.drawString("["+Keyboard.getKeyName(module.getKey())+"]", posX + getSizeX()-3-Fonts.main_16.getStringWidth("["+Keyboard.getKeyName(module.getKey())+"]"), posY + 10 / 2 - Fonts.main_16.getHeight() / 2, ColorUtils.swapAlpha(-1, alpha));

            GL11.glPopMatrix();
        }

        public Module getModule() {
            return module;
        }

        public float getHeight(){
            if ( 1.1f - anim.getAnim() < 0.01f){
                return 0;
            }
            return anim.getAnim() * 10;
        }
    }
}