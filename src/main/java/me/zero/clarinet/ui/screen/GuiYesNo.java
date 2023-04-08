package me.zero.clarinet.ui.screen;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiYesNo extends GuiScreen {
	
	public GuiScreen callback;
	public String title;
	
	public GuiYesNo(GuiScreen callback, String title) {
		this.callback = callback;
		this.title = title;
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(1, this.width / 2 - 100, 76 + 48, "Yes"));
		buttonList.add(new GuiButton(2, this.width / 2 - 100, 76 + 48 + 24, "No"));
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, title, this.width / 2, 76 + 24, 0xffffffff);
		super.drawScreen(i, j, f);
	}
}
