package me.zero.clarinet.event.game;

import me.zero.clarinet.event.api.events.Event;

public class EventKey implements Event {
	
	public final int key;
	
	public EventKey(int key) {
		this.key = key;
	}
}
