package ru.terrarXD.shit.event.events;

import net.minecraft.network.Packet;
import ru.terrarXD.shit.event.Event;
/**
 * @author zTerrarxd_
 * @since 13:02 of 01.08.2022
 */
public class EventPacketSend
        extends Event {
    Packet packet;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
