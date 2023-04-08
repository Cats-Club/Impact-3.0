package me.zero.clarinet.ui.click.classic.theme.themes;

import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.Theme;
import me.zero.clarinet.util.render.RenderUtils;

public class ProximityTheme extends Theme {
	
	@Override
	public void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame) {
		frame.setWidth(123);
		frame.setHeight(15);
		
		int totalHeight = frame.getHeight();
		if (frame.open) {
			for (Element e : frame.getElements()) {
				totalHeight += e.getHeight() + 2;
			}
		}
		
		RenderUtils.rectangleBorderedGradient(frame.getX(), frame.getY(), frame.getX() + frame.getWidth(), frame.getY() + totalHeight, 0xFF000000, 0xAF333333, 0xAF000000);
		RenderUtils.rectangleBorderedGradient(frame.getX() + 110, frame.getY() + 2, frame.getX() + frame.getWidth() - 2, frame.getY() + frame.getHeight() - 2, 0xFF000000, 0xAF333333, 0xAF000000);
		
		FontManager.font_proximity.drawString(frame.getTitle(), frame.getX() + 3, frame.getY() + 2, 0xFFFFFFFF);
		FontManager.font_proximity.drawString(frame.open ? "-" : "+", frame.open ? frame.getX() + 114 : frame.getX() + 113, frame.getY() + 2, 0xFFFFFFFF);
		
		int elementY = frame.getY() + frame.getHeight();
		for (Element e : frame.getElements()) {
			e.setX(frame.getX() + 2);
			e.setY(elementY);
			elementY += e.getHeight() + 2;
		}
	}
	
	@Override
	public void drawButton(int mouseX, int mouseY, float partialTicks, Button button) {
		button.setHeight(13);
		button.setWidth(button.getParent().getWidth() - 4);
		if (button.isInside(mouseX, mouseY)) {
			RenderUtils.rectangleBorderedGradient(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0xFF000000, button.isEnabled() ? 0xAF0000FF : 0xFF101010, button.isEnabled() ? 0xAF0099FF : 0xFF262626);
		} else {
			RenderUtils.rectangleBorderedGradient(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0xFF000000, button.isEnabled() ? 0xAF0099FF : 0xFF262626, button.isEnabled() ? 0xAF0000FF : 0xFF101010);
		}
		if (button.getTitle().split(":").length == 2) {
			FontManager.font_proximity.drawStringWithShadow(button.getTitle().split(":")[0], button.getX() + 2, button.getY() + 0.5F, 0xFFFFFFFF);
			FontManager.font_proximity.drawStringWithShadow(button.getTitle().split(":")[1], button.getX() + button.getWidth() - 2 - FontManager.font_proximity.getStringWidth(button.getTitle().split(":")[1]), button.getY() + 0.5F, 0xFFFFFFFF);
		} else {
			FontManager.font_proximity.drawStringWithShadow(button.getTitle(), button.getX() + (button.getWidth() / 2) - (FontManager.font_proximity.getStringWidth(button.getTitle()) / 2), button.getY() + 1, 0xFFFFFFFF);
			if (button.getElements().size() > 0) {
				String ext = button.isOpen() ? "<" : ">";
				FontManager.font_proximity.drawStringWithShadow(ext, button.getX() + button.getWidth() - FontManager.font_proximity.getStringWidth(ext) - 2, button.getY() + 2.5, 0xFFFFFFFF);
			}
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
				renderHeight -= 1;
				RenderUtils.rectangleBorderedGradient(button.getX() + button.getWidth() + 4, button.getY() - 2, button.getX() + button.getWidth() + renderWidth + 13, button.getY() + renderHeight + 1, 0xFF000000, 0xAF333333, 0xAF000000);
			}
		}
	}
	
	@Override
	public void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider) {
		slider.setHeight(13);
		slider.setWidth(slider.getParent().getWidth() - 4);
		RenderUtils.rectangleBorderedGradient(slider.getX(), slider.getY(), slider.getX() + slider.getWidth() + 5, slider.getY() + slider.getHeight(), 0xFF000000, 0xAF333333, 0xAF000000);
		RenderUtils.rectangleBorderedGradient(slider.getX() + 1, slider.getY() + 1, slider.getX() + (float) slider.getSliderWidth(), slider.getY() + slider.getHeight() - 1, 0xFF000000, 0xAF0099FF, 0xAF0000FF);
		FontManager.font_proximity.drawString(slider.getTitle() + " (" + slider.getValue() + ")", slider.getX() + 2, slider.getY() + 3, -1);
	}
	
	@Override
	public void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box) {
		box.setWidth(box.getParent().getWidth() - 4);
		box.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		if (!box.isEnabled()) {
			RenderUtils.rectangleBorderedGradient(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0xFF000000, 0xAF333333, 0xAF000000);
		} else {
			RenderUtils.rectangleBorderedGradient(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0xFF000000, 0xAF333333, 0xAF000000);
			RenderUtils.rectangleBorderedGradient(box.getX() + 1, box.getY() + 1, box.getX() + box.getHeight() - 1, box.getY() + box.getHeight() - 1, 0xFF000000, 0xAF0099FF, 0xAF0000FF);
		}
		FontManager.font_proximity.drawString(box.getTitle(), box.getX() + 5 + box.getHeight(), box.getY() + 2, -1);
	}
	
	@Override
	public void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame) {
		if (mouseButton == 0 && mouseX >= frame.getX() + 110 && mouseX <= frame.getX() + frame.getWidth() - 2 && mouseY >= frame.getY() + 2 && mouseY <= frame.getY() + frame.getHeight() - 2) {
			frame.open = !frame.open;
		}
	}
}
