package me.zero.clarinet.plugin;

import me.zero.clarinet.mod.Mod;
import org.lwjgl.input.Keyboard;

public class PluginMod extends Mod {

    public PluginMod(Plugin plugin) {
        super(plugin.getName(), plugin.getDescription(), Keyboard.KEY_NONE, null);
    }
}
