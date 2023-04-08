package me.zero.clarinet.ui.click.classic.elements;

import java.util.ArrayList;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ClickGui;

public class Button extends Element {
	
	private String title;
	
	private boolean enabled = false;
	
	private boolean open = false;
	
	private ArrayList<Element> elements = new ArrayList<Element>();
	
	public Button(String title, Element parent) {
		this.title = title;
		this.parent = parent;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		this.onUpdate();
        Impact.getInstance().getModManager().get(ClickGui.class).classicClick.getThemeManager().getCurrentTheme().drawButton(mouseX, mouseY, partialTicks, this);
		int elementY = this.getY();
		for (Element e : this.getElements()) {
			e.setX(this.getX() + this.getWidth() + 6);
			e.setY(elementY);
			e.setWidth(this.getWidth());
			elementY += e.getHeight() + 2;
		}
		if (open) {
			for (Element element : elements) {
				element.drawElement(mouseX, mouseY, partialTicks);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0 && isInside(mouseX, mouseY)) {
			this.onToggle();
		} else if (mouseButton == 1 && isInside(mouseX, mouseY)) {
			this.open = !this.open;
		}
		if (open) {
			for (Element element : elements) {
				element.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (open) {
			for (Element element : elements) {
				element.mouseReleased(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	public void onUpdate() {
	}
	
	public void onToggle() {
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	public boolean isOpen() {
		return this.open;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void addElement(Element e) {
		elements.add(e);
	}
	
	public ArrayList<Element> getElements() {
		return this.elements;
	}
}
