package me.zero.clarinet.event.network;

import me.zero.clarinet.event.api.events.callables.EventCancellable;

import net.minecraft.network.Packet;

public class EventPacketReceive extends EventCancellable {
	
	private Packet packet;
	
	public EventPacketReceive(Packet packet) {
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
	
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
}
