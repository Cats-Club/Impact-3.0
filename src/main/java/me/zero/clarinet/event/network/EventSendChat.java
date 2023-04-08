package me.zero.clarinet.event.network;

import me.zero.clarinet.event.api.events.callables.EventCancellable;

public class EventSendChat extends EventCancellable {
	
	public String message;
	
	public EventSendChat(String message) {
		this.message = message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
}
