package me.zero.clarinet.ui.screen.override.screen;

import java.io.IOException;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mixin.mixins.minecraft.client.gui.IGuiDisconnected;
import me.zero.clarinet.mod.misc.AutoReconnect;
import me.zero.clarinet.util.ServerHook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiMessageDialog;

public class OverrideGuiDisconnected extends GuiDisconnected {
	
	private int autoReconnectTimer;
	
	public OverrideGuiDisconnected(GuiDisconnected gui) {
		super(((IGuiDisconnected) gui).getParentScreen(), ((IGuiDisconnected) gui).getReason(), ((IGuiDisconnected) gui).getMessage());
	}
	
	@Override
	public void initGui() {
		super.initGui();
		autoReconnectTimer = 100;
		this.buttonList.add(new GuiButton(20, this.width / 2 - 100, this.height / 2 + ((IGuiDisconnected) this).getTextHeight() / 2 + this.fontRenderer.FONT_HEIGHT + 25, "Reconnect"));
		this.buttonList.add(new GuiButton(21, this.width / 2 - 100, this.height / 2 + ((IGuiDisconnected) this).getTextHeight() / 2 + this.fontRenderer.FONT_HEIGHT + 25 + 24, "Reconnect"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 20) {
			Impact.getInstance().getModManager().get(AutoReconnect.class).toggle();
		}
		if (button.id == 21) {
			ServerHook.reconnectToLastServer(this);
		}
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
		}
	}
	
	@Override
	public void updateScreen() {
		if (Impact.getInstance().getModManager().get(AutoReconnect.class).isToggled()) {
			if (this.autoReconnectTimer > 0) {
				for (GuiButton b : buttonList) {
					if (b.id == 20) {
						b.displayString = "AutoReconnect " + TextFormatting.GREEN + "ON" + TextFormatting.WHITE + " (" + (autoReconnectTimer / 20 + 1) + ")";
					}
				}
				this.autoReconnectTimer--;
			} else {
				ServerHook.reconnectToLastServer(this);
			}
		} else {
			autoReconnectTimer = 100;
			for (GuiButton b : buttonList) {
				if (b.id == 20) {
					b.displayString = "AutoReconnect " + TextFormatting.RED + "OFF";
				}
			}
		}
	}
}
