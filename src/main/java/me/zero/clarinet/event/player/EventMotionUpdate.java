package me.zero.clarinet.event.player;

import me.zero.clarinet.event.api.events.Event;
import me.zero.clarinet.event.api.types.EventType;

public class EventMotionUpdate implements Event {
	
	public final byte type;
	public double x;
	public double y;
	public double z;
	public boolean sprinting;
	public float yaw;
	public float pitch;
	public boolean onGround;
	public boolean sneaking;
	
	public EventMotionUpdate() {
		this.type = EventType.POST;
	}
	
	public EventMotionUpdate(double x, double y, double z, float yaw, float pitch, boolean sprinting, boolean sneaking, boolean onGround) {
		this.type = EventType.PRE;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.sprinting = sprinting;
		this.sneaking = sneaking;
		this.onGround = onGround;
	}
}
