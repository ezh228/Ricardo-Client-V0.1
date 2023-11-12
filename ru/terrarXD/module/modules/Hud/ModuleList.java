package ru.terrarXD.module.modules.Hud;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.module.Category;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.event.EventTarget;
import ru.terrarXD.shit.event.events.EventRender2D;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.BooleanSetting;
import ru.terrarXD.shit.settings.FloatSetting;
import ru.terrarXD.shit.utils.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author zTerrarxd_
 * @since 0:01 of 21.04.2023
 */
public class ModuleList extends Module {
    FloatSetting offsetX;
    FloatSetting offsetY;




    ArrayList<Mod> mods = new ArrayList<>();

    public ModuleList() {
        super("ArrayList", Category.Hud);
        add(offsetX = new FloatSetting("X-Offset", 0, 10, 0, 0.1f));
        add(offsetY = new FloatSetting("Y-Offset", 0, 10, 0, 0.1f));
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mods.clear();
        for (Module module : Client.moduleManager.modules){
            mods.add(new Mod(module));
        }
        mods.sort(new Comparator<Mod>() {
            @Override
            public int compare(Mod o1, Mod o2) {
                return Fonts.main_18.getStringWidth(o2.module.getName())-Fonts.main_18.getStringWidth(o1.module.getName());
            }
        });
    }

    public void doSMT(){
        float x = GuiScreen.width-offsetX.getVal();
        float y = offsetY.getVal();
        for (Mod mod : mods){
            mod.render(x, y);
            y+=mod.getHeight();
        }
    }

    @EventTarget
    public void onRender2D(EventRender2D event){

        float x = GuiScreen.width-offsetX.getVal();
        float y = offsetY.getVal();

        int colorMain1 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.1d).getRGB();
        int colorMain2 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.9d).getRGB();
        StencilUtil.initStencilToWrite();
        int mah_weight = 0;

        float old_h = 0;
        for (Mod mod : mods){
            old_h = mod.renderObv(x, y, old_h);
            y+=mod.getHeight();
            if (Fonts.main_18.getStringWidth(mod.module.getName())+4 > mah_weight){
                mah_weight = Fonts.main_18.getStringWidth(mod.module.getName())+4;
            }
        }
        StencilUtil.readStencilBuffer(1);
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x-mah_weight-2, 0, x+2, y+2, 0, 0, colorMain1,colorMain1, colorMain2, colorMain2, false ,true ,false);
        StencilUtil.uninitStencilBuffer();

        y = offsetY.getVal();

        for (Mod mod : mods){
            mod.render(x, y);
            y+=mod.getHeight();

        }



    }


    class Mod{
        AnimationUtils anim;
        Module module;
        public Mod(Module module){
            this.module = module;
            anim = new AnimationUtils(0, 0, 0.1f);
        }

        public void render(float x, float y){
            if (module.isEnabled()){
                anim.to = 1;
            }else {
                anim.to=0;
            }
            if (anim.getAnim()>0.8f){
                //GL11.glPushMatrix();
                //RenderUtils.customScaledObject2D(x- (Fonts.main_18.getStringWidth(module.getName())+4)*3, y-15, (Fonts.main_18.getStringWidth(module.getName())+4)*2, 15, anim.getAnim());
                int c = new Color(29, 29, 29, (int)(255*anim.getAnim())).getRGB();
                RenderUtils.drawRect(x- Fonts.main_18.getStringWidth(module.getName())-4, y, x, y+15, new Color(29, 29, 29, (int)(255*anim.getAnim())).getRGB());
                Fonts.main_18.drawString(module.getName(), x- Fonts.main_18.getStringWidth(module.getName())-2, y+15/2-(Fonts.main_18.getHeight()/2), ColorUtils.swapAlpha(Client.getColor(), (int) (255*anim.getAnim())));

               // GL11.glPopMatrix();
            }
        }

        public float renderObv(float x, float y, float old_h){
            if (module.isEnabled()){
                anim.to = 1;
            }else {
                anim.to=0;
            }
            if (anim.getAnim()>0.8f){
                //GL11.glPushMatrix();
                //RenderUtils.customScaledObject2D(x- (Fonts.main_18.getStringWidth(module.getName())+4)*3, y-15, (Fonts.main_18.getStringWidth(module.getName())+4)*2, 15, anim.getAnim());
                RenderUtils.drawRect(x- Fonts.main_18.getStringWidth(module.getName())-4-0.5f, y-0.5f, x+0.5f, y+15+0.5f, -1);


                //GL11.glPopMatrix();
            }
            return Fonts.main_18.getStringWidth(module.getName());
        }



        public float getHeight(){
            return 15 * anim.getAnim();
        }




    }
}
