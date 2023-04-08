package me.zero.clarinet.ui.screen.metro;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;

public class MetroPanel {
	
	protected ArrayList<MetroButton> buttons = new ArrayList<MetroButton>();
	
	protected final int maxSize = 4;
	
	protected float panelX;
	
	protected float panelY;
	
	public MetroPanel() {
		this(0, 0);
	}
	
	public MetroPanel(float panelX, float panelY) {
		this.panelX = panelX;
		this.panelY = panelY;
	}
	
	public void draw(int mouseX, int mouseY) {
		for (MetroButton button : buttons) {
			button.drawButton(Minecraft.getMinecraft(), mouseX, mouseY, Minecraft.getMinecraft().getRenderPartialTicks());
		}
	}
	
	public void setPosition(float panelX, float panelY) {
		this.panelX = panelX;
		this.panelY = panelY;
	}
	
	public void addButton(MetroButton button) {
		this.buttons.add(button);
	}
	
	public void addButtons(ArrayList<MetroButton> buttons) {
		this.buttons.addAll(buttons);
	}
	
	public int getSize() {
		return maxSize;
	}
	
	public ArrayList<MetroButton> getButtons() {
		return this.buttons;
	}
}
