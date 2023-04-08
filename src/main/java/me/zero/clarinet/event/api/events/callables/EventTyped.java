package me.zero.clarinet.event.api.events.callables;

import me.zero.clarinet.event.api.events.Event;
import me.zero.clarinet.event.api.events.Typed;

/**
 * Abstract example implementation of the Typed interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class EventTyped implements Event, Typed {
	
	private final byte type;
	
	/**
	 * Sets the type of the event when it's called.
	 *
	 * @param eventType
	 *            The type ID of the event.
	 */
	protected EventTyped(byte eventType) {
		type = eventType;
	}
	
	/**
	 * @see Typed.getType
	 */
	@Override
	public byte getType() {
		return type;
	}
	
}
