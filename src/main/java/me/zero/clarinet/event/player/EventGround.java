package me.zero.clarinet.event.player;

import me.zero.clarinet.event.api.events.Event;

public class EventGround implements Event {

    public boolean onGround;

    public EventGround(boolean onGround) {
        this.onGround = onGround;
    }
}
