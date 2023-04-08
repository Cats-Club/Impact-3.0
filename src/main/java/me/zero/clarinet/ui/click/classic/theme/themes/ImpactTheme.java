package me.zero.clarinet.ui.click.classic.theme.themes;

import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.Theme;
import me.zero.clarinet.util.render.RenderUtils;

public class ImpactTheme extends Theme {
	
	public boolean insideOpener;
	
	@Override
	public void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame) {
		frame.setWidth(110);
		frame.setHeight(15);
		
		int totalHeight = frame.getHeight();
		if (frame.open) {
			for (Element e : frame.getElements()) {
				totalHeight += e.getHeight() + 2;
			}
		}
		
		RenderUtils.rectangleBordered(frame.getX(), frame.getY(), frame.getX() + frame.getWidth(), frame.getY() + totalHeight, 0xFF5B5B5B, 0xFF242424);
		if (frame.open) {
			RenderUtils.rectangleBordered(frame.getX() + 97, frame.getY() + 2, frame.getX() + frame.getWidth() - 2, frame.getY() + frame.getHeight() - 2, 0xFF5B5B5B, 0xFF242424);
		} else {
			RenderUtils.rectangleBorderedGradient(frame.getX() + 97, frame.getY() + 2, frame.getX() + frame.getWidth() - 2, frame.getY() + frame.getHeight() - 2, 0xFF000000, 0xFF6E6E6E, 0xFF494949);
		}
		FontManager.urwgothic_hud.drawString(frame.getTitle(), frame.getX() + 2, frame.getY() + 3, 0xFFFFFFFF);
		
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
		button.setWidth(106);
		if (!button.isEnabled()) {
			RenderUtils.rectangleBordered(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0xFF000000, button.isInside(mouseX, mouseY) ? 0xFF4D4D4D : 0xFF5F5F5F);
		} else {
			RenderUtils.rectangleBorderedGradient(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0xFF000000, button.isInside(mouseX, mouseY) ? 0xFF701D1D : 0xFFA91B1B, button.isInside(mouseX, mouseY) ? 0xFFA51E1E : 0xFF941717);
		}
		if (button.getTitle().split(":").length == 2) {
			FontManager.urwgothic_hud.drawStringWithShadow(button.getTitle().split(":")[0], button.getX() + 2, button.getY() + 2, 0xFFFFFFFF);
			FontManager.urwgothic_hud.drawStringWithShadow(button.getTitle().split(":")[1], button.getX() + button.getWidth() - 2 - FontManager.urwgothic_hud.getStringWidth(button.getTitle().split(":")[1]), button.getY() + 2, 0xFFFFFFFF);
		} else {
			FontManager.urwgothic_hud.drawStringWithShadow(button.getTitle(), button.getX() + (button.getWidth() / 2) - (FontManager.urwgothic_hud.getStringWidth(button.getTitle()) / 2), button.getY() + 2, 0xFFFFFFFF);
			if (button.getElements().size() > 0) {
				String ext = button.isOpen() ? "<" : ">";
				FontManager.urwgothic_hud.drawStringWithShadow(ext, button.getX() + button.getWidth() - FontManager.urwgothic_hud.getStringWidth(ext) - 2, button.getY() + 2, 0xFFFFFFFF);
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
				// RenderUtils.rectangleBordered(frame.getX(), frame.getY(),
				// frame.getX() + frame.getWidth(), frame.getY() + totalHeight,
				// 0xFF5B5B5B, 0xFF242424);
				RenderUtils.rectangleBordered(button.getX() + button.getWidth() + 4, button.getY() - 2, button.getX() + button.getWidth() + renderWidth + 8, button.getY() + renderHeight + 1, 0xFF5B5B5B, 0xFF242424);
			}
		}
	}
	
	@Override
	public void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider) {
		slider.setHeight(13);
		slider.setWidth(106);
		RenderUtils.rectangleBordered(slider.getX(), slider.getY(), slider.getX() + slider.getWidth(), slider.getY() + slider.getHeight(), 0xFF000000, 0xFF5F5F5F);
		RenderUtils.rectangleGradient(slider.getX() + 1, slider.getY() + 1, slider.getX() + (float) slider.getSliderWidth() - 1, slider.getY() + slider.getHeight() - 1, 0xFFA91B1B, 0xFF941717);
		FontManager.urwgothic_hud.drawString(slider.getTitle() + " (" + slider.getValue() + ")", slider.getX() + 2, slider.getY() + 2, -1);
	}
	
	@Override
	public void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box) {
		box.setWidth(106);
		box.setHeight(13);
		if (!box.isEnabled()) {
			RenderUtils.rectangleBordered(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0xFF000000, 0xFF5F5F5F);
		} else {
			RenderUtils.rectangleBordered(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0xFF000000, 0xFF5F5F5F);
			RenderUtils.rectangleGradient(box.getX() + 1, box.getY() + 1, box.getX() + box.getHeight() - 1, box.getY() + box.getHeight() - 1, 0xFFA91B1B, 0xFF941717);
		}
		FontManager.urwgothic_hud.drawString(box.getTitle(), box.getX() + 5 + box.getHeight(), box.getY() + 2, -1);
	}
	
	@Override
	public void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame) {
		// RenderUtils.rectangleBorderedGradient(frame.getX() + 97,
		// frame.getY() + 2, frame.getX() + frame.getWidth() - 2, frame.getY() +
		// frame.getHeight() - 2, 0xFF000000, 0xFF6E6E6E, 0xFF494949);
		if (mouseButton == 0 && mouseX >= frame.getX() + 97 && mouseX <= frame.getX() + frame.getWidth() - 2 && mouseY >= frame.getY() + 2 && mouseY <= frame.getY() + frame.getHeight() - 2) {
			frame.open = !frame.open;
		}
	}
}
