package ru.terrarXD.shit.event.events;


import ru.terrarXD.shit.event.Event;

/**
 * @author zTerrarxd_
 * @since 0:13 of 30.05.2022
 */
public class EventKeyBoard extends Event {
    int key;
    public EventKeyBoard(int key){
        this.key = key;
    }

    public int getKey() {
        return key;
    }
}
