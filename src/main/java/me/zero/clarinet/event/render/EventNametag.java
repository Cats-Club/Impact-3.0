package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.callables.EventCancellable;

import net.minecraft.entity.Entity;

public class EventNametag extends EventCancellable {
	
	public final Entity entity;
	private String renderName;
	
	public EventNametag(Entity entity, String renderName) {
		this.entity = entity;
		this.renderName = renderName;
	}
	
	public void setRenderName(String renderName) {
		this.renderName = renderName;
	}
	
	public String getRenderName() {
		return this.renderName;
	}
	
	public String getEntityName() {
		return this.entity.getName();
	}
}
