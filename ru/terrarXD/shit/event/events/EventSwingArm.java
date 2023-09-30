package ru.terrarXD.shit.event.events;

import net.minecraft.util.EnumHand;
import ru.terrarXD.shit.event.Event;

public class EventSwingArm extends Event {
    EnumHand hand;
    public EventSwingArm(EnumHand hand){
        this.hand = hand;
    }

    public EnumHand getHand() {
        return hand;
    }
}
