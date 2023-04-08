package me.zero.clarinet.ui.click.classic.theme.themes;

import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.Theme;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.util.text.TextFormatting;

public class WeepCraftTheme extends Theme {
	
	@Override
	public void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame) {
		frame.setHeight(mc.fontRenderer.FONT_HEIGHT + 2);
		frame.setWidth(115);
		
		int totalHeight = frame.getHeight() + 2;
		if (frame.open) {
			for (Element e : frame.getElements()) {
				totalHeight += e.getHeight() + 1;
			}
		}
		
		RenderUtils.rectangleBordered(frame.getX(), frame.getY(), frame.getX() + frame.getWidth(), frame.getY() + frame.getHeight(), 0x30FFFFFF, 0x70000000);
		if (frame.open) {
			RenderUtils.rectangleBordered(frame.getX(), frame.getY() + 12, frame.getX() + frame.getWidth(), frame.getY() + totalHeight, 0x30FFFFFF, 0x70000000);
		}
		mc.fontRenderer.drawString(TextFormatting.YELLOW + frame.getTitle(), frame.getX() + 3, frame.getY() + 2, 0xFFFFFFFF);
		RenderUtils.rectangleBordered(frame.getX() + frame.getWidth() - 10, frame.getY() + 1, frame.getX() + frame.getWidth() - 1, frame.getY() + frame.getHeight() - 1, 0x30FFFFFF, 0x70000000);
		
		int elementY = frame.getY() + frame.getHeight() + 4;
		for (Element e : frame.getElements()) {
			e.setX(frame.getX() + 1);
			e.setY(elementY);
			elementY += e.getHeight() + 1;
		}
		
	}
	
	@Override
	public void drawButton(int mouseX, int mouseY, float partialTicks, Button button) {
		button.setHeight(mc.fontRenderer.FONT_HEIGHT + 2);
		button.setWidth(86);
		if (!button.isInside(mouseX, mouseY)) {
			mc.fontRenderer.drawStringWithShadow(button.isEnabled() ? button.getTitle() : TextFormatting.DARK_RED + button.getTitle(), button.getX() + 2, button.getY(), button.isEnabled() ? 0xFF00FF00 : 0xFFFFFFFF);
		} else {
			mc.fontRenderer.drawStringWithShadow(TextFormatting.YELLOW + button.getTitle(), button.getX() + 2, button.getY() + .25F, 0xFFFFFFFF);
		}
		
		if (button.getElements().size() > 0) {
			if (button.isOpen()) {
				int renderHeight = 0;
				int renderWidth = 0;
				for (Element e : button.getElements()) {
					renderHeight += e.getHeight() + 2;
					if (e.getWidth() > renderWidth) {
						renderWidth = e.getWidth();
					}
				}
				// RenderUtils.drawBorderedRect(frame.getX(), frame.getY(),
				// frame.getX() + frame.getWidth(), frame.getY() + totalHeight,
				// 0xFF000000, 0x60000000);
				renderHeight -= 1;
				RenderUtils.rectangleBordered(button.getX() + button.getWidth() + 4, button.getY() - 2, button.getX() + button.getWidth() + renderWidth + 20, button.getY() + renderHeight + 1, 0x30FFFFFF, 0x70000000);
			}
		}
	}
	
	@Override
	public void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider) {
		// RenderUtils.drawBorderedRect(frame.getX(), frame.getY(),
		// frame.getX() + frame.getWidth(), frame.getY() + frame.getHeight(),
		// 0x30FFFFFF, 0x70000000);
		RenderUtils.rectangleBordered(slider.getX(), slider.getY(), slider.getX() + (float) slider.getSliderWidth(), slider.getY() + slider.getHeight(), 0x30FFFFFF, 0x70000000);
		mc.fontRenderer.drawStringWithShadow(TextFormatting.DARK_RED + slider.getTitle() + TextFormatting.YELLOW + " (" + slider.getValue() + ")", slider.getX() + 2, slider.getY() + 3, -1);
	}
	
	@Override
	public void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box) {
		box.setWidth(86);
		box.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		RenderUtils.rectangleBordered(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0x30FFFFFF, 0x80000000);
		mc.fontRenderer.drawString(box.isEnabled() ? TextFormatting.YELLOW + box.getTitle() : TextFormatting.WHITE + box.getTitle(), box.getX() + 5 + box.getHeight(), box.getY() + 3, -1);
	}
	
	@Override
	public void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame) {
		if (mouseButton == 1 && frame.isInside(mouseX, mouseY)) {
			frame.open = !frame.open;
		}
	}
}
