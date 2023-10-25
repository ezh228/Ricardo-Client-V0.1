package ru.terrarXD.module.modules.Hud;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.utils.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 13:59 of 17.06.2023
 */
public class Notifications extends Module {
    ArrayList<Notif> notifs=  new ArrayList<>();
    public Notifications() {
        super("Notifications", Category.Hud);
    }

    @EventTarget
    public void onRender2D(EventRender2D event){
        int x = GuiScreen.width-10;
        int y = GuiScreen.height-10;
        for (int i = 0; i < notifs.size(); i++) {
            Notif notif = notifs.get(i);
            if (notif.delete()){
                notifs.remove(i);
            }else {
                notif.render(x, y);
                y-=18+4;
            }
        }
    }

    public void displayNotif(Notif notif){
        notifs.add(notif);
    }


    public static class Notif{

        String name;
        String text;

        AnimationUtils anim;
        TimerUtils timer;
        public Notif(String name, String text){
            this.name = name;
            this.text=  text;
            anim = new AnimationUtils(0, 1, 0.01f);
            timer = new TimerUtils();

        }

        public void render(float x, float y){
            int colorMain = ColorUtils.swapAlpha(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.9d).getRGB(), (int) (anim.getAnim()*254f));
            //int colorCategorys = ColorUtils.swapAlpha(ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.95d).getRGB(), (int) (anim.getAnim()*255f));
            x +=  ((1f - anim.getAnim()) * 10f);
            anim.speed = 0.05f;
            int height = 18;
            int width = Fonts.main_16.getStringWidth(text)+2*4;
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, 0);
            GL11.glRotatef((90- (anim.getAnim()*90f)), 0, 0, 1);
            GL11.glTranslated(-x, -y, 0);
            RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x-width, y-height, x, y, 5, 0, colorMain, colorMain, colorMain, colorMain, false, true, false);
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x-width, y-height, x, y, 5, 0, colorMain, colorMain, colorMain, colorMain, false, true, false);
            StencilUtil.readStencilBuffer(1);
            RenderUtils.drawRect(x-width, y-1, x-width+(timer.getMc()/2000f*width), y, Client.getColor());
            StencilUtil.uninitStencilBuffer();

            Fonts.main_18.drawString(name, x-width+2, y-height+2, ColorUtils.swapAlpha(-1, (int) (anim.getAnim()*254f)));
            Fonts.main_15.drawString(text, x-width+2, y-height+2+Fonts.main_18.getHeight()+1, ColorUtils.swapAlpha(-1, (int) (anim.getAnim()*254f)));

            GL11.glPopMatrix();
            if (timer.hasReached(2000)){
                anim.to = 0;
                if (anim.getAnim() == 0){
                    
                }
            }
        }
        
        public boolean delete(){
            if (timer.hasReached(2000)){
                if (anim.getAnim() == 0){
                    return true;
                }
            }
            return false;
        }

    }
}
