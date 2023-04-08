package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.Event;

import me.zero.clarinet.util.Helper;

public class EventRenderBrightness implements Event, Helper {
	
	public float brightness;
	
	public EventRenderBrightness() {
		brightness = mc.gameSettings.gammaSetting;
	}
}
