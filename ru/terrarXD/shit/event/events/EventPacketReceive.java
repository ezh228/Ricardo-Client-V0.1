package ru.terrarXD.shit.event.events;

import net.minecraft.network.Packet;
import ru.terrarXD.shit.event.Event;
/**
 * @author zTerrarxd_
 * @since 23:37 of 16.06.2022
 */
public class EventPacketReceive extends Event {
    Packet packet;

    public EventPacketReceive(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}
