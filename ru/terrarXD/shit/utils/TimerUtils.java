package ru.terrarXD.shit.utils;

/**
 * @author zTerrarxd_
 * @since 14:06 of 31.05.2022
 */
public class TimerUtils {
    long mc;
    public TimerUtils(){
        mc = System.currentTimeMillis();
    }

    public void reset(){
        mc = System.currentTimeMillis();
    }

    public long getMc(){
        return System.currentTimeMillis() - mc;
    }

    public boolean hasReached(long time){
        return System.currentTimeMillis() - mc > time;
    }
}
