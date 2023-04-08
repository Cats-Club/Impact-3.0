package me.zero.clarinet.event;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.game.EventKey;
import me.zero.clarinet.event.game.EventModToggle;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.manager.manager.MacroManager;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.DimensionConverter;
import me.zero.clarinet.util.Helper;

public class Handler implements Helper {
	
	@EventTarget
	public void onKeyPress(EventKey event) {
		int key = event.key;
		for (Mod m : Impact.getInstance().getModManager().getMods()) {
			if (key == m.getKeybind()) {
				m.toggle();
			}
		}
		for (MacroManager.Macro macro : Impact.getInstance().getMacroManager().getMacros()) {
			if (macro.getKey() == key) {
                for (int i = 0; i < macro.getMessages().length; i++) {
                    mc.player.sendChatMessage(macro.getMessages()[i]);
                }
			}
		}
	}
	
	@EventTarget
	public void onModToggle(EventModToggle event) {
		if (event.state) {
			if (!event.mod.canUseMod()) {
				ClientUtils.error("This is a premium mod! You must donate at least $5 USD §cto receieve access! -> bit.ly/ImpactDonate");
				event.setCancelled(true);
			} else {
				for (Mod mod : event.mod.getConflictingMods()) {
					if (mod.isToggled()) {
						ClientUtils.error(mod.getName() + " has been disabled due to a confliction!");
						mod.toggle();
					}
				}
			}
		}
	}

	@EventTarget
    public void onRender3D(EventRender3D event) {
        DimensionConverter.update();
    }
}
