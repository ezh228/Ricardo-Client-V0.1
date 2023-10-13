package ru.terrarXD.shit.utils;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class GlichUtils {
    AnimationUtils anim;
    TimerUtils timer;
    float posY;

    boolean doGlich = false;
    long time;
    public GlichUtils(){
        anim = new AnimationUtils(0, 0, 0.5f, Type.HARP);
        timer = new TimerUtils();
        this.time = 5000;
    }

    public GlichUtils(long time){
        anim = new AnimationUtils(0, 0, 0.5f, Type.HARP);
        timer = new TimerUtils();
        this.time = time;
    }


    public void render(ArrayList<RenderInterface> renderInterfaces, float posYY, float height, float maxHight){
        if (timer.hasReached(time)){
            doGlich = true;
            timer.reset();
            posY = MathUtils.randomInt((int) posYY, (int) (posYY + height-maxHight));
            anim.to = MathUtils.randomInt(-10, 10);
        }
        if (anim.getAnim() == anim.to){
            anim.to = 0;
        }
        if (anim.getAnim() == 0 && anim.to == 0){
            doGlich = false;
        }
        if (!doGlich){
            for (RenderInterface renderInterface : renderInterfaces){
                renderInterface.render();
            }
        }else{
            for (RenderInterface renderInterface : renderInterfaces){
                renderInterface.render();
            }
            StencilUtil.initStencilToWrite();
            RenderUtils.drawRect(0, posY, 1000, posY+maxHight, -1);
            StencilUtil.readStencilBuffer(1);
            GL11.glPushMatrix();
            for (RenderInterface renderInterface : renderInterfaces){
                GL11.glTranslated(anim.getAnim(), 0, 0);
                renderInterface.render();
            }
            GL11.glPopMatrix();
            StencilUtil.uninitStencilBuffer();
        }

    }

}


