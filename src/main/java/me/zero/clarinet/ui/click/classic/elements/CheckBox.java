package me.zero.clarinet.ui.click.classic.elements;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ClickGui;

public class CheckBox extends Element {
	
	private String title;
	
	private boolean enabled = false;
	
	public CheckBox(String title, boolean enabled, Element parent) {
		this.parent = parent;
		this.title = title;
		this.enabled = enabled;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		this.onUpdate();
        Impact.getInstance().getModManager().get(ClickGui.class).classicClick.getThemeManager().getCurrentTheme().drawCheckbox(mouseX, mouseY, partialTicks, this);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if ((mouseX >= x && mouseX <= x + height) && (mouseY >= y && mouseY <= y + height)) {
			this.enabled = !this.enabled;
			this.onToggle();
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void onUpdate() {
	}
	
	public void onToggle() {
	}
}
