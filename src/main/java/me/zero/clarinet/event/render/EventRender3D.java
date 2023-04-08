package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.Event;

public class EventRender3D implements Event {
	
	public final float partialTicks;
	
	public EventRender3D(float partialTicks) {
		this.partialTicks = partialTicks;
	}
}
