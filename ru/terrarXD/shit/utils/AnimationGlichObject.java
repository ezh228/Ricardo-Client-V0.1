package ru.terrarXD.shit.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author zTerrarxd_
 * @since 18:25 of 03.12.2022
 */
public class AnimationGlichObject {
    ArrayList<AnimObject> animObjects = new ArrayList<>();
    public AnimationGlichObject(){
        animObjects = new ArrayList<>();
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        for (int x = 0; x < sr.getScaledWidth(); x+=0) {
            int wid= (int) MathUtils.getRandomInRange(50, 25);
            for (int y = 0; y < sr.getScaledHeight(); y+=0) {

                int hed= (int) MathUtils.getRandomInRange(50, 25);

                animObjects.add(new AnimObject(x, y,wid,hed));

                y+=hed;
            }
            x+=wid;
        }
        System.out.println("size " + animObjects.size());
    }
    public boolean ready = false;
    public void render(InitAnim initAnim){

        if (!ready){
            boolean g = true;
            for(AnimObject animObject : animObjects){
                animObject.render(initAnim);
                if (!animObject.ready()){
                    g = false;
                }
            }
            if (g){
                ready = true;
            }
        }else {

            initAnim.render();
        }

    }



    public class AnimObject{
        AnimationUtils animX;
        AnimationUtils animY;
        int x;
        int y;
        int x2;
        int y2;

        boolean alph = false;
        public AnimObject(int x, int y, int x2, int y2){
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            animX = new AnimationUtils(getNeedPos(x, 0), 0,  MathUtils.getRandomInRange(0.08f, 0.01f));
            animY = new AnimationUtils(getNeedPos(y, 1), 0,  MathUtils.getRandomInRange(0.08f, 0.01f));
            this.x2 = x2;
            this.y2 = y2;
            this.x = x;
            this.y = y;

        }

        int getNeedPos(int pos, int id){
            ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
            if (id == 0){
                boolean left = pos < sr.getScaledWidth() / 2;
                if (left){
                    return (int) (-MathUtils.getRandomInRange(400, 50));
                }else {
                    return (int) (+ MathUtils.getRandomInRange(400, 50));
                }

            }else {
                boolean up = pos < sr.getScaledHeight() / 2;
                if (up){
                    return (int) (-MathUtils.getRandomInRange(400, 50));
                }else {
                    return (int) (+ MathUtils.getRandomInRange(400, 50));
                }
            }

        }

        public void render(InitAnim initAnim){
            if (MathUtils.getRandomInRange(200, 1) > 190){
                alph = true;
            }
            GL11.glPushMatrix();
            GL11.glTranslated(animX.getAnim(), animY.getAnim(), 0);
            //StencilUtil.initStencilToWrite();
            //RenderUtils.drawRect(x, y,  x + x2,y + y2, -1);
            //StencilUtil.readStencilBuffer(1);
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            RenderUtils.scissor(x + animX.getAnim(), y+animY.getAnim(),  x2,y2);
            initAnim.render();
            if (alph){
                RenderUtils.drawRect(0, 0, GuiScreen.width, GuiScreen.height, new Color(0, 0, 0, 50).getRGB());
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);

            //StencilUtil.uninitStencilBuffer();
            GL11.glPopMatrix();
            alph = false;

        }

        public boolean ready(){

            return Math.abs(animX.getAnim() - animX.to) < 1f && Math.abs(animY.getAnim() - animY.to) < 1f;
        }
    }

    public interface InitAnim{
        void render();
    }
}
