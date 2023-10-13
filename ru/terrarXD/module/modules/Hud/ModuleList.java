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
import ru.terrarXD.shit.utils.AnimationUtils;
import ru.terrarXD.shit.utils.BloomUtil;
import ru.terrarXD.shit.utils.ColorUtils;
import ru.terrarXD.shit.utils.RenderUtils;

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


    BooleanSetting shadow;


    ArrayList<Mod> mods = new ArrayList<>();

    public ModuleList() {
        super("ArrayList", Category.Hud);
        add(offsetX = new FloatSetting("X-Offset", 0, 10, 0, 0.1f));
        add(offsetY = new FloatSetting("Y-Offset", 0, 10, 0, 0.1f));
        add(shadow = new BooleanSetting("Shadow", false));
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
        
        for (Mod mod : mods){
            mod.render2(x, y);
            y+=mod.getHeight();
        }
        if (shadow.getVal()){
            BloomUtil.renderBlur(()->doSMT());
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
                GL11.glPushMatrix();
                RenderUtils.customScaledObject2D(x- (Fonts.main_18.getStringWidth(module.getName())+4)*3, y-15, (Fonts.main_18.getStringWidth(module.getName())+4)*2, 15, anim.getAnim());
                RenderUtils.drawRect(x- Fonts.main_18.getStringWidth(module.getName())-4, y, x, y+15, new Color(29, 29, 29, (int)(255*anim.getAnim())).getRGB());
                GL11.glPopMatrix();
            }

        }

        public void render2(float x, float y){
            if (module.isEnabled()){
                anim.to = 1;
            }else {
                anim.to=0;
            }
            if (anim.getAnim()>0.8f){
                GL11.glPushMatrix();
                RenderUtils.customScaledObject2D(x- (Fonts.main_18.getStringWidth(module.getName())+4)*3, y-15, (Fonts.main_18.getStringWidth(module.getName())+4)*2, 15, anim.getAnim());
                RenderUtils.drawRect(x- Fonts.main_18.getStringWidth(module.getName())-4, y, x, y+15, new Color(29, 29, 29, (int)(255*anim.getAnim())).getRGB());
                Fonts.main_18.drawString(module.getName(), x- Fonts.main_18.getStringWidth(module.getName())-2, y+15/2-(Fonts.main_18.getHeight()/2), ColorUtils.swapAlpha(Client.getColor(), (int) (255*anim.getAnim())));
                GL11.glPopMatrix();
            }

        }

        public float getHeight(){
            return 15 * anim.getAnim();
        }




    }
}
