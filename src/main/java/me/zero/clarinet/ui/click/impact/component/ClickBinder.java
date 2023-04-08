package me.zero.clarinet.ui.click.impact.component;

import java.awt.Point;

import me.zero.clarinet.mod.Mod;

public class ClickBinder extends ClickComponent {
	
	private Mod mod;
	
	private boolean binding;
	
	public ClickBinder(Mod mod) {
		this.mod = mod;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		
	}
	
	@Override
	public void onUpdate(int mouseX, int mouseY) {
		
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		Point mouse = new Point(mouseX, mouseY);
		if (getCollisionBox().contains(mouse)) {
			binding = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (binding) {
			mod.setKeybind(keyCode);
			binding = false;
		}
	}
}
