package ru.terrarXD.shit.event.events;

import ru.terrarXD.shit.event.Event;

/**
 * @author zTerrarxd_
 * @date 24.10.2023 18:22
 */
public class MoveEvent extends Event {
    public double motionX, motionY, motionZ;

    public MoveEvent( final double motionX, final double motionY, final double motionZ ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}
