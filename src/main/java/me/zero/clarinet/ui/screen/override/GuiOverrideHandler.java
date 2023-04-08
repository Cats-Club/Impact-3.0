package me.zero.clarinet.ui.screen.override;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mixin.mixins.minecraft.client.gui.IGuiChat;
import me.zero.clarinet.mixin.mixins.minecraft.client.gui.inventory.IGuiChest;
import me.zero.clarinet.ui.screen.metro.MetroMenu;
import me.zero.clarinet.ui.screen.override.screen.*;
import me.zero.clarinet.util.Helper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiChest;

public class GuiOverrideHandler implements Helper {
	
	public static boolean metro = false;
	
	public static GuiScreen handle(GuiScreen screen) {
		if (!Impact.getInstance().isDisabled()) {
			if (screen instanceof GuiIngameMenu) {
				screen = new OverrideGuiIngameMenu();
			}
			if (screen instanceof GuiMainMenu || (screen == null && Minecraft.getMinecraft().world == null)) {
				if (metro) {
					screen = new MetroMenu();
				} else {
					screen = new OverrideGuiMainMenu();
				}
			}
			if (screen instanceof GuiDisconnected) {
				screen = new OverrideGuiDisconnected((GuiDisconnected) screen);
			}
			if (screen instanceof GuiChat && !(screen instanceof GuiSleepMP)) {
				screen = new OverrideGuiChat(((IGuiChat) screen).getDefaultInputFieldText());
			}
			if (screen instanceof GuiMultiplayer) {
				screen = new OverrideGuiMultiplayer(new GuiMainMenu());
			}
			if (screen instanceof GuiChest) {
				IGuiChest chest = (IGuiChest) screen;
				screen = new OverrideGuiChest(chest.getUpperChestInventory(), chest.getLowerChestInventory());
			}
		}
		return screen;
	}
}
