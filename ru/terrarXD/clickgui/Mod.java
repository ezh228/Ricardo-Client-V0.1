package ru.terrarXD.clickgui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import ru.terrarXD.Client;
import ru.terrarXD.clickgui.sets.*;
import ru.terrarXD.module.Module;
import ru.terrarXD.shit.fonts.Fonts;
import ru.terrarXD.shit.settings.*;
import ru.terrarXD.shit.utils.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @date 06.11.2023 20:15
 */
public class Mod extends Comp {
    boolean binding = false;

    ArrayList<Particle2D> particle2DS = new ArrayList<>();

    Module module;
    int key = 0;
    AnimationUtils anim = new AnimationUtils(0, 0, 0.1f);
    AnimationUtils animButton = new AnimationUtils(0, 0, 0.1f);
    AnimationUtils animBind = new AnimationUtils(0, 0, 0.01f);

    ArrayList<Set> sets = new ArrayList<>();
    public Mod(Module module){
        this.module = module;
        for (Setting setting : module.getSettings()){
            if (setting instanceof BooleanSetting){
                sets.add(new CheckBox((BooleanSetting) setting));
            }
            if (setting instanceof FloatSetting){
                sets.add(new Slider((FloatSetting) setting));
            }
            if (setting instanceof ModeSetting){
                sets.add(new ComboBox((ModeSetting) setting));
            }
            if (setting instanceof ColorSetting){
                sets.add(new ColorPiker((ColorSetting) setting));
            }
        }
        key = module.getKey();
    }

    public Module getModule() {
        return module;
    }

    public void bind(){
        if (Keyboard.getEventKey() == Keyboard.KEY_DELETE){
            module.setKey(0);
        }else {
            module.setKey(Keyboard.getEventKey());
        }
        binding = false;
    }

    @Override
    public void drawScreen(float x, float y, float mouseX, float mouseY) {
        super.drawScreen(x, y, mouseX, mouseY);

        if (binding){
            if (Keyboard.isKeyDown(Keyboard.getEventKey())){
                if (key != Keyboard.getEventKey()){
                    animBind.setAnim(0);
                    animBind.to = 0;
                }else if (animBind.getAnim() == 1){
                    bind();
                }else if (key == Keyboard.getEventKey()){
                    animBind.to = 1f;
                }
                key = Keyboard.getEventKey();
            }else {
                animBind.to = 0f;

            }
        }
        if (module.isEnabled()){
            animButton.to = 1;
        }else {
            animButton.to = 0;
        }

        if (isHover(x, y, x+getWidth(), y+18+3, mouseX, mouseY)){
            anim.to = 1;
        }else {
            anim.to = 0;
        }
        int colorMain1 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.1d).getRGB();
        int colorMain2 = ColorUtils.TwoColoreffect(new Color(29, 29, 29), new Color(Client.getColor()), 0.3d).getRGB();
        int color = new Color(29, 29, 29).getRGB();
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y, x+getWidth(), y+getHeight(), 5, 1, colorMain1, colorMain1, colorMain2, colorMain2, false, true, true);
        RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x, y+3, x+getWidth(), y+getHeight(), 5, 1, color, color, color, color, false, true, true);
        Fonts.main_15.drawString(module.getName(), x+6+anim.getAnim()*3, y+3+18/2-Fonts.main_15.getHeight()/2, -1);
        //Button
        int pos = (int) (getWidth()-25);


        if (binding){
            Fonts.main_12.drawCenteredString(""+Keyboard.getKeyName(key),x+pos+10, y+3+18/2-Fonts.main_12.getHeight()/2, -1);

            RenderUtils.drawCircle2D(x+pos+10, y+3+18/2, 5, (int) (animBind.getAnim()*360f), colorMain2, animBind.getAnim()*3f);
            int i = (int) (animBind.getAnim()*360f);
            float partX = (float) ((double)x + Math.sin((double)i * Math.PI / 180.0D) * (double)5)+pos+10;
            float partY = (float) ((double)y + Math.cos((double)i * Math.PI / 180.0D) * (double)5)+3+18/2;
            particle2DS.add(new Particle2D(partX, partY, animBind.getAnim(), 10));
            
        }else {
            if (animButton.getAnim() != animButton.to){
                particle2DS.add(new Particle2D(x+pos+5+animButton.getAnim()*10f, y+3+18f/2f, 4));
            }
            int gg = 1;
            if (module.isEnabled()){
                RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x+pos, y+3+4, x+pos+20, y+3f+18-4, 5, 1, colorMain1, colorMain2, colorMain2, colorMain1, false, true ,true);
                int radius = 4;
                if ((1f - animButton.getAnim()) >= 0.01f){
                    Fonts.icons_12.drawCenteredString("I", x+pos+5+9, y+gg+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(-1, (int) (255f*(1f - animButton.getAnim()))));
                }
                if ((animButton.getAnim()) >= 0.01f) {
                    Fonts.icons_12.drawCenteredString("H", x+pos+5, y+gg+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(-1, (int) (255f*animButton.getAnim())));
                }
                RenderUtils.drawCircle(x+pos+5+animButton.getAnim()*10f, y+3+18f/2f, radius, -1);
            }else {
                RenderUtils.drawRoundedFullGradientShadowFullGradientRoundedFullGradientRectWithBloomBool(x+pos, y+3+4, x+pos+20, y+3f+18-4, 5, 1, -1, -1, -1, -1, false, true ,true);
                int radius = 4;
                if ((1f - animButton.getAnim()) >= 0.01f){
                    Fonts.icons_12.drawCenteredString("I", x+pos+5+9, y+gg+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(colorMain1, (int) (255f*(1f - animButton.getAnim()))));
                }
                if ((animButton.getAnim()) >= 0.01f) {
                    Fonts.icons_12.drawCenteredString("H", x+pos+5, y+gg+3+18f/2f-Fonts.icons_12.getHeight()/2, ColorUtils.swapAlpha(colorMain2, (int) (255f*animButton.getAnim())));
                }
                RenderUtils.drawCircle(x+pos+5+animButton.getAnim()*10f, y+3+18f/2f, radius, colorMain2);
            }
        }
        for (int i = 0; i < particle2DS.size(); i++) {
            if (particle2DS.get(i).toRemove()){
                particle2DS.remove(i);
            }else {
                particle2DS.get(i).drawAndMovement();
            }
        }

        if (sets.size()>0){
            RenderUtils.drawRect(x+5, y+20, x+getWidth()-25-2, y+21, new Color(0, 0, 0, 50).getRGB());
        }
        int posY = 18;
        for (Set set : sets){
            if(set.getSetting().isVisable()){
                set.drawScreen(x, y+posY, mouseX, mouseY);
                posY+=set.getHeight();
            }

        }
    }

    @Override
    public void mouseClicked(float x, float y, float mouseX, float mouseY, int button) {
        super.mouseClicked(x, y, mouseX, mouseY, button);
        if (isHover(x, y, x+getWidth(), y+18+3, mouseX, mouseY) && button == 0){
            System.out.println(module.getName());

            module.toggle();

        }
        if (isHover(x, y, x+getWidth(), y+18+3, mouseX, mouseY) && button == 2){
            binding = !binding;

        }
        int posY = 18;
        for (Set set : sets){
            if(set.getSetting().isVisable()){
                set.mouseClicked(x, y+posY, mouseX, mouseY, button);
                posY+=set.getHeight();
            }

        }

    }

    @Override
    public float getHeight() {
        int he = 18+3;
        int posY = 18+3+3;
        for (Set set : sets){
            if(set.getSetting().isVisable()){
                posY+=set.getHeight();

            }

        }
        return posY;
    }
    public float getStaticHeight() {
        int he = 18+3;
        int posY = 18+3+3;
        for (Set set : sets){
            posY+=set.getHeight();



        }
        return posY;
    }

    @Override
    public float getWidth() {
        return (Client.clickGuiScreen.WIDTH-100-40-10)/2;
    }
}
