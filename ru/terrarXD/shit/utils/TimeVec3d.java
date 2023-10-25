package ru.terrarXD.shit.utils;

import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

/**
 * @author zTerrarxd_
 * @date 24.10.2023 18:25
 */
public class TimeVec3d extends Vec3d {
    private final long time;

    public TimeVec3d(double xIn, double yIn, double zIn, long time) {
        super(xIn, yIn, zIn);
        this.time = time;
    }

    public TimeVec3d(Vec3i vector, long time) {
        super(vector);
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
