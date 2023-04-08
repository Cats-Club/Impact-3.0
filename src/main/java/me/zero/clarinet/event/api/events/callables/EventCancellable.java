package me.zero.clarinet.event.api.events.callables;

import me.zero.clarinet.event.api.events.Cancellable;
import me.zero.clarinet.event.api.events.Event;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class EventCancellable implements Event, Cancellable {
	
	private boolean cancelled;
	
	protected EventCancellable() {
	}
	
	/**
	 * @see Cancellable.isCancelled
	 */
	@Override
	public boolean isCancelled() {
		return cancelled;
	}
	
	/**
	 * @see Cancellable.setCancelled
	 */
	@Override
	public void setCancelled(boolean state) {
		cancelled = state;
	}
	
}
