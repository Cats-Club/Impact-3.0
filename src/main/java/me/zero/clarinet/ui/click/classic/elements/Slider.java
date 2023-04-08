package me.zero.clarinet.ui.click.classic.elements;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ClickGui;
import me.zero.clarinet.util.MathUtils;

public class Slider extends Element {
	
	private String title;
	
	private double value, min, max, increment;
	
	private double lastValue;
	
	private boolean dragging = false;
	
	private double renderWidth;
	
	public Slider(String title, double min, double max, double value, double increment, Element parent) {
		this.title = title;
		this.min = min;
		this.max = max;
		this.value = value;
		this.increment = increment;
		this.parent = parent;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		lastValue = value;
		if (dragging) {
			double diff = mouseX - x;
			diff = Math.max(0, diff);
			diff = Math.min(width, diff);
			if (diff == 0) {
				value = min;
			} else {
				value = MathUtils.roundToPlace(((diff / width) * (max - min)) + min, 2);
				if (increment == 1) {
					value = (int) value;
				}
			}
		}
		renderWidth = ((double) this.width) * (value - min) / (max - min);
		if (lastValue != value) {
			onValueSet(value);
		}
        Impact.getInstance().getModManager().get(ClickGui.class).classicClick.getThemeManager().getCurrentTheme().drawSlider(mouseX, mouseY, partialTicks, this);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.isInside(mouseX, mouseY)) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		dragging = false;
	}
	
	public double getSliderWidth() {
		return renderWidth;
	}
	
	public void onValueSet(double value) {}
	
	public String getTitle() {
		return this.title;
	}
	
	public double getValue() {
		return this.value;
	}
}
