package me.zero.clarinet.event.player;

import me.zero.clarinet.event.api.events.Event;

public class EventSprint implements Event {
	
	public boolean sprinting;
	
	public EventSprint(boolean sprinting) {
		this.sprinting = sprinting;
	}
}
