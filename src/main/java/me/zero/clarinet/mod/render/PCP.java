package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;

public class PCP extends Mod {
	
	public PCP() {
		super("PCP", "Everyone's favorite drug!", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
	}
}
