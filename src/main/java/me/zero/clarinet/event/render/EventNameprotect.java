package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.Event;

public class EventNameprotect implements Event {
	
	private String message;
	
	public EventNameprotect(String message) {
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
