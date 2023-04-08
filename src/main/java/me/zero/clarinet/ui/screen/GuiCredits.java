package me.zero.clarinet.ui.screen;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiCredits extends GuiScreen {
	
	public GuiScreen callback;
	
	public String title = "Impact Credits";
	
	public final String[][] credits = {
            { "Zero/Brady", "Main Developer" },
            { "Whispers", "Developer" },
            { "Dinavi", "Developer" },
            { "DoubleParallax", "Made original \"Para\" Gui" },
            { "TheCyberBrick", "Stencil util file" },
            { "Alerithe", "Base for Boost Speed" },
            { "Ed Gray", "S/M-P Icons (Noun Project)" },
            { "Housin Aziz", "Settings Icon (Noun Project)" },
            { "Joe Mortell", "Language Icon (Noun Project)" },
            { "Creative Stall", "Crosshair Icon (Noun Project)" },
            { "abhishek rana", "Exit Icon (Noun Project)" } };
	
	public GuiCredits(GuiScreen callback) {
		this.mc = Minecraft.getMinecraft();
		this.callback = callback;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 6 + 168, "Done"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			mc.displayGuiScreen(callback);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(mc.fontRenderer, title, this.width / 2, 20, 0xffffffff);
		int y = 40;
		int gap = 150;
		for (String[] s : credits) {
			drawString(mc.fontRenderer, s[0], this.width / 2 - gap, y, 0xffffffff);
			drawString(mc.fontRenderer, s[1], this.width / 2 + gap - mc.fontRenderer.getStringWidth(s[1]), y, 0xffffffff);
			y += mc.fontRenderer.FONT_HEIGHT + 1;
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
