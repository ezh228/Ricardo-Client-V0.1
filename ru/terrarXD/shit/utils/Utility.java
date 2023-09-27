package ru.terrarXD.shit.utils;

/**
 * @author zTerrarxd_
 * @since 21:34 of 02.02.2023
 */
import java.util.ArrayList;
import java.util.List;



public interface Utility {



    List<Runnable> NORMAL_BLUR_RUNNABLES = new ArrayList<>();
    List<Runnable> NORMAL_SHADOW_BLACK = new ArrayList<>();

    static void render2DRunnables() {

        GaussianBlur.renderBlur(15, NORMAL_BLUR_RUNNABLES);
        BloomUtil.renderBlur(NORMAL_SHADOW_BLACK);
        clearRunnables();

    }

    static void clearRunnables() {
        NORMAL_BLUR_RUNNABLES.clear();
        NORMAL_SHADOW_BLACK.clear();
    }


}
