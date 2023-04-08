package me.zero.clarinet.ui.click.classic.elements;

import java.util.ArrayList;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ClickGui;

public class Frame extends Element {
	
	public String title;
	public boolean drag, open;
	public int offsetX, offsetY;
	private ArrayList<Element> elements = new ArrayList<>();
	
	public Frame(String title, int x, int y) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.open = false;

        ClickGui.FrameState state = Impact.getInstance().getModManager().get(ClickGui.class).getState(this);
        if (state != null) {
            this.x = state.x;
            this.y = state.y;
            this.open = state.open;
        }
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		drag(mouseX, mouseY);
		Impact.getInstance().getModManager().get(ClickGui.class).classicClick.getThemeManager().getCurrentTheme().drawFrame(mouseX, mouseY, partialTicks, this);
		if (open) {
			for (Element element : elements) {
				element.drawElement(mouseX, mouseY, partialTicks);
			}
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (open) {
			for (Element element : elements) {
				element.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
		if (mouseButton == 0 && isInside(mouseX, mouseY)) {
			offsetX = x - mouseX;
			offsetY = y - mouseY;
			drag = true;
			return;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			drag = false;
		}
		if (open) {
			for (Element element : elements) {
				element.mouseReleased(mouseX, mouseY, mouseButton);
			}
		}
        Impact.getInstance().getModManager().get(ClickGui.class).classicClick.getThemeManager().getCurrentTheme().frameClicked(mouseX, mouseY, mouseButton, this);
	}
	
	public void addElement(Element element) {
		elements.add(element);
	}
	
	private void drag(int mouseX, int mouseY) {
		if (!drag) return;
		x = offsetX + mouseX;
		y = offsetY + mouseY;
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
	
	public String getTitle() {
		return title;
	}
	
	public ArrayList<Element> getElements() {
		return this.elements;
	}
}
