package ru.terrarXD.shit.event.events;

import ru.terrarXD.shit.event.Event;
/**
 * @author zTerrarxd_
 * @since 0:18 of 30.05.2022
 */
public class EventRender2D extends Event {
    float partialTicks;
    public EventRender2D(float partialTicks){
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
