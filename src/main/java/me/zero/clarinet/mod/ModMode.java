package me.zero.clarinet.mod;

import me.zero.clarinet.util.Helper;

public class ModMode<T extends Mod> implements Helper {

	protected final T parent;
	
	private final String name;
	
	public ModMode(T parent, String name) {
		this.parent = parent;
		this.name = name;
	}
	
	public void onEnable() {}
	
	public void onDisable() {}
	
	public T getParent() {
		return this.parent;
	}
	
	public String getName() {
		return this.name;
	}
}