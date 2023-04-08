package me.zero.clarinet.event.render;

import me.zero.clarinet.event.api.events.Event;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.EntityLivingBase;

public class EventEntityRender implements Event {
	
	public final byte type;
	public final Render render;
	public final EntityLivingBase entity;
	public final double x;
	public final double y;
	public final double z;
	
	public EventEntityRender(byte type, Render render, EntityLivingBase entity, double x, double y, double z) {
		this.type = type;
		this.render = render;
		this.entity = entity;
		this.x = x;
		this.y = y;
		this.z = z;
	}
}
