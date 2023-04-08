package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.Event;

public class EventRender2D implements Event {
	
	public final float partialTicks;
	
	public EventRender2D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
}
