package ru.terrarXD.shit.event.events;

import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import ru.terrarXD.shit.event.Event;

/**
 * @author zTerrarxd_
 * @date 17.10.2023 18:27
 */
public class EventTransformEatFirstPerson extends Event {
    EnumHandSide hand;
    public EventTransformEatFirstPerson(EnumHandSide hand){
        this.hand = hand;
    }

    public EnumHandSide getHand() {
        return hand;
    }
}
