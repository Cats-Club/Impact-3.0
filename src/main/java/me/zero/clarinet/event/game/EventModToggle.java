package me.zero.clarinet.event.game;

import me.zero.clarinet.event.api.events.callables.EventCancellable;

import me.zero.clarinet.mod.Mod;

public class EventModToggle extends EventCancellable {
	
	public final Mod mod;
	
	public final boolean state;
	
	public EventModToggle(Mod mod, boolean state) {
		this.mod = mod;
		this.state = state;
	}
}
