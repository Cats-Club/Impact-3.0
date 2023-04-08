package me.zero.clarinet.util;

import me.zero.values.types.NumberValue;

public final class TimerUtil {
	
	private long prevMS = -1L;
	
	public final void reset() {
		prevMS = System.currentTimeMillis();
	}
	
	public final long getDiff() {
		return getTime() - prevMS;
	}
	
	public final boolean delay(long milliseconds) {
		return getTime() >= prevMS + milliseconds;
	}
	
	public final boolean delay(NumberValue milliseconds) {
		return delay(milliseconds.getValue().intValue());
	}
	
	public final boolean delay(float milliSec) {
		return (float) (getTime() - this.prevMS) >= milliSec;
	}
	
	public final boolean speed(float speed) {
		speed = Math.max(0, speed);
		return getTime() >= prevMS + (long) (1000 / speed);
	}
	
	public final boolean speed(NumberValue speed) {
		return this.speed(speed.getValue().floatValue());
	}

    private long getTime() {
		return System.currentTimeMillis();
	}
}
