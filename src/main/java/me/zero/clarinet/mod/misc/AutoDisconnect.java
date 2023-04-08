package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;

public class AutoDisconnect extends Mod {
	
	private NumberValue health = new NumberValue(this, "Health", "health", 1D, 1D, 10D, 0.5D);
	
	public AutoDisconnect() {
		super("AutoDisconnect", "Automatically Disconnect from Server", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.player.getHealth() <= (health.getValue().doubleValue() * 2)) {
            mc.world.sendQuittingDisconnectingPacket();
            mc.loadWorld(null);
		}
	}
}
