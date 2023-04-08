package me.zero.clarinet.ui.screen.override.screen;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.ui.screen.GuiImpact;
import me.zero.clarinet.util.ServerHook;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;

public class OverrideGuiIngameMenu extends GuiIngameMenu {
	
	private static boolean hiding;
	
	@Override
	public void initGui() {
		super.initGui();
		ServerHook.setCurrentIpToSingleplayer();
		if (!hiding) {
			this.buttonList.add(new GuiButton(420, this.width / 2 - 100, this.height / 4 + 72 - 16, Impact.getInstance().getName()));
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 420) {
			mc.displayGuiScreen(new GuiImpact(this));
			return;
		}
		super.actionPerformed(button);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_LSHIFT) {
			hiding = !hiding;
			initGui();
			return;
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
        if (!hiding) {
            String text = "Press LSHIFT to hide the Impact button";
            this.drawString(this.fontRenderer, text, this.width - this.fontRenderer.getStringWidth(text) - 2, 2, 0xFFFFFFFF);
        }
	}
}
