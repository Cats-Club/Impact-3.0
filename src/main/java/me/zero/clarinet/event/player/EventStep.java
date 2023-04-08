package me.zero.clarinet.event.player;

import me.zero.clarinet.event.api.events.Event;

import net.minecraft.entity.Entity;

public class EventStep implements Event {
	
	public double stepHeight;
	public byte type;
	public Entity entity;
	
	public EventStep(Entity entity, float stepHeight, byte type) {
		this.entity = entity;
		this.stepHeight = stepHeight;
		this.type = type;
	}
}
