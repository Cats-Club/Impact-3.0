package me.zero.clarinet.event.network;

import me.zero.clarinet.event.api.events.callables.EventCancellable;

import net.minecraft.network.Packet;

public class EventPacketSend extends EventCancellable {
	
	private Packet packet;
	
	public EventPacketSend(Packet packet) {
		this.packet = packet;
	}
	
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
}
