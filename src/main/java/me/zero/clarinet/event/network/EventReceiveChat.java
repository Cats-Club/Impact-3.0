package me.zero.clarinet.event.network;

import me.zero.clarinet.event.api.events.Event;

import net.minecraft.util.text.ITextComponent;

public class EventReceiveChat implements Event {
	
	public ITextComponent chatComponent;
	
	public EventReceiveChat(ITextComponent chatComponent) {
		this.chatComponent = chatComponent;
	}
}
