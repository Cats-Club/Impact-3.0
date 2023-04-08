package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class AutoReconnect extends Mod {
	
	public AutoReconnect() {
		super("AutoReconnect", "Automatically Reconnect to Server", Keyboard.KEY_NONE, Category.MISC);
	}
}
