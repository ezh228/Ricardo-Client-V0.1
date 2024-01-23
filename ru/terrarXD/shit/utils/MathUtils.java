package ru.terrarXD.shit.utils;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

/**
 * @author zTerrarxd_
 * @since 19:55 of 28.10.2022
 */
public class MathUtils {
    public static float harp(float val, float current, float speed) {
        float emi = ((current - val) * (speed/2)) > 0 ? Math.max((speed), Math.min(current - val, ((current - val) * (speed/2)))) : Math.max(current - val, Math.min(-(speed/2), ((current - val) * (speed/2))));
        return val + emi;
    }
    public static float getDistance(Vec3d pos, Vec3d pos2)
    {
        float f = (float)(pos.xCoord - pos2.xCoord);
        float f1 = (float)(pos.yCoord - pos2.yCoord);
        float f2 = (float)(pos.zCoord - pos2.zCoord);
        return MathHelper.sqrt(f * f + f1 * f1 + f2 * f2);
    }

    public static float lerp(float numer, float to, float speed) {
        return numer + speed * (to - numer);
    }
    private static final Random random = new Random();

    public static float getRandomInRange(float max, float min) {
        return (float)((double)min + (double)(max - min) * random.nextDouble());
    }

    public static float randomFloat(float f2, float f3) {
        if (f2 == f3 || f3 - f2 <= 0.0f) {
            return f2;
        }
        return (float)((double)f2 + (double)(f3 - f2) * Math.random());
    }
    public static int randomInt(int n2, int n3) {
        if (n2 == n3 || n3 - n2 <= 0) {
            return n2;
        }
        return n2 + random.nextInt(n3 - n2);
    }

    public static float clamp(float val, float min, float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }

    public static float round(float num, float increment) {

        float v = (float)Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();




    }
}
