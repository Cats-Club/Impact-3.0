package me.zero.clarinet.ui.click.para.components;

import java.awt.Color;

import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.render.RenderUtils;

public class LSlider extends Component {
	
	private double value, lastValue, min, max, increment;
	
	private boolean dragging = false;
	
	public LSlider(String title, double value, double min, double max, double increment, int width, int height) {
		this.title = title;
		this.value = this.lastValue = value;
		this.min = min;
		this.max = max;
		this.increment = increment;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		double diff = Math.min(width, Math.max(0, mouseX - x));
		if (dragging) {
			if (diff == 0) {
				value = min;
			} else {
				value = MathUtils.roundToPlace(((diff / width) * (max - min)) + min, 2);
				value = (double) Math.round(value * (1.0 / increment)) / (1.0 / increment);
			}
		}
		double renderWidth = (this.width - 4.0) * (value - min) / (max - min);
		int[] enabledGradient = new int[] { -14540254, -14540254, RenderUtils.blend(-14540254, -16777216, 0.95f), RenderUtils.blend(-14540254, -16777216, 0.95f) };
		int[] disabledGradient = new int[] { -13421773 };
		int[] outlineGradient = new int[] { RenderUtils.blend(-15658735, -16777216, 0.95f), RenderUtils.blend(-15658735, -16777216, 0.95f), -15658735, -15658735 };
		RenderUtils.rectangleBorderedGradient(x, y, x + width, y + height, dragging ? enabledGradient : disabledGradient, outlineGradient, 1F);
		RenderUtils.rectangle(x + 2.0f, y + 1.0f, x + width - 2.0f, y + 2.0f, dragging ? 536870912 : 553648127);
		RenderUtils.rectangle(x + 2, y + height - 4, x + 2 + renderWidth, y + height - 2.0f, -2130706433);
		if (mouseInside(mouseX, mouseY)) {
			RenderUtils.rectangle(x, y, x + width, y + height, new Color(0, 0, 0, 50).getRGB());
		}
		font.drawString(title, x + 6, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
		String val = String.valueOf(value);
		if (increment == 1) {
			val = String.valueOf((int) value);
		}
		font.drawString(val, x + width - font.getStringWidth(val) - 4, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
		this.onUpdate();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.mouseInside(mouseX, mouseY)) {
			dragging = true;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		dragging = false;
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {}
	
	public void setValue(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return this.value;
	}
}
