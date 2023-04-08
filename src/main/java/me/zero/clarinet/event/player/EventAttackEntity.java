package me.zero.clarinet.event.player;

import me.zero.clarinet.event.api.events.Event;

import net.minecraft.entity.Entity;

public class EventAttackEntity implements Event {
	
	public final byte type;
	
	public final Entity entity;
	
	public EventAttackEntity(byte type, Entity entity) {
		this.type = type;
		this.entity = entity;
	}
}
