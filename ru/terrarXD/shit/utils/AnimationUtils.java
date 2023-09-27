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
    public AnimationUtils(float anim, float to, float speed){
        this.anim = anim;
        this.to = to;
        this.speed = speed;
        mc = System.currentTimeMillis();
    }

    public float getAnim() {
        int count = (int) ((System.currentTimeMillis() - mc) / 5);
        if (count > 0){
            mc = System.currentTimeMillis();
        }
        for (int i = 0; i < count; i++) {
            anim = MathUtils.harp(anim, to, speed);
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
