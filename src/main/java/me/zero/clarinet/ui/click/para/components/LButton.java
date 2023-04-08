package me.zero.clarinet.ui.click.para.components;

import java.awt.Color;

import me.zero.clarinet.util.TimerUtil;
import me.zero.clarinet.util.render.RenderUtils;

public class LButton extends LSlotComponent {
	
	private TimerUtil hoverTimer;
	
	private String description;
	
	private boolean enabled;
	
	public LButton(String title, boolean enabled, int width, int height) {
		this(title, null, enabled, width, height);
	}
	
	public LButton(String title, String description, boolean enabled, int width, int height) {
		this.title = title;
		this.description = description;
		this.enabled = enabled;
		this.width = width;
		this.height = height;
		this.hoverTimer = new TimerUtil();
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		int renderHeight = height;
		if (open && components.size() > 0) {
			int spacing = 3;
			for (Component comp : components) {
				renderHeight += comp.getHeight() + spacing;
			}
			renderHeight += 10;
		}
		boolean hover = this.mouseInside(mouseX, mouseY);
		int[] enabledGradient = new int[] { -14540254, -14540254, RenderUtils.blend(-14540254, -16777216, 0.95f), RenderUtils.blend(-14540254, -16777216, 0.95f) };
		int[] disabledGradient = new int[] { -13421773 };
		int[] outlineGradient = new int[] { RenderUtils.blend(-15658735, -16777216, 0.95f), RenderUtils.blend(-15658735, -16777216, 0.95f), -15658735, -15658735 };
		RenderUtils.rectangleBorderedGradient(x, y, x + width, y + renderHeight, enabled ? enabledGradient : disabledGradient, outlineGradient, 1F);
		RenderUtils.rectangle(x + 2.0f, y + 1.0f, x + width - 2.0f, y + 2.0f, enabled ? 536870912 : 553648127);
		if (mouseInside(mouseX, mouseY)) {
			RenderUtils.rectangle(x, y, x + width, y + renderHeight, new Color(0, 0, 0, 50).getRGB());
		} else {
			hoverTimer.reset();
		}
		if (title.split(":").length == 2) {
			String s1 = title.split(":")[0];
			String s2 = title.split(":")[1];
			font.drawStringWithShadow(s1, x + 6, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
			font.drawStringWithShadow(s2, x + width - font.getStringWidth(s2) - 4, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
		} else {
			font.drawStringWithShadow(title, x + width / 2 - font.getStringWidth(title) / 2, y + height / 2 - font.FONT_HEIGHT / 2 + (enabled ? 1 : 0), 0xFFFFFFFF);
		}
		if (description != null && hoverTimer.delay(500)) {
			float spacer = 2;
			float stringWidth = font.getStringWidth(description) + (spacer * 2);
			float height = font.FONT_HEIGHT * 2.5F;
			float x = mouseX;
			float y = mouseY - height;
			RenderUtils.rectangle(x, y, x + stringWidth + (spacer * 2), y + height, 0xCF000000);
			font.drawStringWithShadow(description, x + (spacer * 2), y + height / 2 - font.FONT_HEIGHT / 2 - 1, 0xFFFFFFFF);
		}
		resizeComponents();
		if (open) {
			for (Component comp : components) {
				comp.drawElement(mouseX, mouseY, partialTicks);
			}
		}
		this.onUpdate();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.mouseInside(mouseX, mouseY)) {
			if (mouseButton == 0) {
				this.onToggle();
			} else {
				open = !open;
			}
		}
		if (open) {
			for (Component comp : components) {
				comp.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (open) {
			for (Component comp : components) {
				comp.mouseReleased(mouseX, mouseY, state);
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		for (Component comp : components) {
			comp.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	public int getHeight() {
		if (this.open && components.size() > 0) {
			return resizeComponents() - y + 7;
		} else {
			return super.getHeight();
		}
	}
	
	private int resizeComponents() {
		int spacing = 3;
		int elementY = y + height + spacing;
		for (Component comp : components) {
			comp.setX(x + width / 2 - comp.getWidth() / 2);
			comp.setY(elementY);
			elementY += comp.getHeight() + spacing;
		}
		return elementY;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void onToggle() {
	}
}
