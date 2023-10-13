package ru.terrarXD.shit.utils;

/**
 * @author zTerrarxd_
 * @since 1:14 of 26.06.2022
 */
public class AnimationUtils {
    long mc;
    float anim;
    public float to;
    public float speed;
    Type type;
    public AnimationUtils(float anim, float to, float speed){
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        mc = System.currentTimeMillis();
        this.type = Type.HARP;
    }

    public AnimationUtils(float anim, float to, float speed, Type type){
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        mc = System.currentTimeMillis();
        this.type = type;
    }

    public static float harp(float val, float current, float speed) {
        float emi = ((current - val) * (speed/2)) > 0 ? Math.max((speed), Math.min(current - val, ((current - val) * (speed/2)))) : Math.max(current - val, Math.min(-(speed/2), ((current - val) * (speed/2))));
        return val + emi;
    }
    public float getAnim() {
        int count = (int) ((System.currentTimeMillis() - mc) / 5);
        if (count > 0){
            mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; i++) {
            if (type == Type.HARP){
                anim = harp(anim, to, speed);
            }else if (type == Type.SIMPLE){
                if (anim > to){
                    anim-=speed;
                }else if (anim < to){
                    anim+=speed;
                }
                if (Math.abs(to - anim) < speed){
                    anim = to;
                }
            }
        }
        return anim;

    }

    public void reset(){
        mc = System.currentTimeMillis();
    }


    public void setAnim(float anim) {
        this.anim = anim;
        mc = System.currentTimeMillis();
    }
}

