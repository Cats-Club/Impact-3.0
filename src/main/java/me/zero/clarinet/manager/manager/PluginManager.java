package me.zero.clarinet.manager.manager;

import me.zero.clarinet.manager.Manager;
import me.zero.clarinet.plugin.Plugin;

public class PluginManager extends Manager<Plugin> {
	
	public PluginManager() {
		super("Plugin");
	}
	
	@Override
	public void load() {
		
	}

    @Override
    public void save() {

    }
}
