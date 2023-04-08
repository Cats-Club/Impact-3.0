package me.zero.clarinet.mod.misc;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class Disable extends Mod {
	
	public Disable() {
		super("Disable", "Disables the client", Keyboard.KEY_NONE, Category.MISC);
	}
	
	@Override
	public void onEnable() {
		this.toggle();
		Impact.getInstance().disable();
		mc.displayGuiScreen(null);
	}
}
