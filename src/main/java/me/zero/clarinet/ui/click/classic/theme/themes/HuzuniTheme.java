package me.zero.clarinet.ui.click.classic.theme.themes;

import java.awt.Font;

import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.Theme;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.util.text.TextFormatting;

public class HuzuniTheme extends Theme {
	
	public CFontRenderer font = new CFontRenderer(new Font("Verdana", Font.PLAIN, 18), true, false);
	
	public boolean isInsideOpener;
	
	@Override
	public void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame) {
		frame.setHeight(15);
		frame.setWidth(125);
		
		int totalHeight = frame.getHeight();
		if (frame.open) {
			for (Element e : frame.getElements()) {
				totalHeight += e.getHeight() + 2;
			}
		}
		
		RenderUtils.rectangle(frame.getX(), frame.getY(), frame.getX() + frame.getWidth(), frame.getY() + totalHeight, 0xFF000000);
		font.drawString(frame.getTitle(), frame.getX() + 2, frame.getY() + 2.5F, 0xFFFFFFFF);
		
		RenderUtils.rectangle(frame.getX() + 112, frame.getY() + 2, frame.getX() + frame.getWidth() - 2, frame.getY() + frame.getHeight() - 2, frame.open ? 0x9500A7FF : 0xFF373737);
		
		int elementY = frame.getY() + frame.getHeight();
		for (Element e : frame.getElements()) {
			e.setX(frame.getX() + 2);
			e.setY(elementY);
			elementY += e.getHeight() + 2;
		}
	}
	
	@Override
	public void drawButton(int mouseX, int mouseY, float partialTicks, Button button) {
		button.setWidth(121);
		button.setHeight(14);
		if (!button.isInside(mouseX, mouseY)) {
			RenderUtils.rectangle(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), button.isEnabled() ? 0x9500A7FF : 0xFF454545);
		} else {
			RenderUtils.rectangle(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), button.isEnabled() ? 0xFF00A7FF : 0xFF575757);
		}
		if (button.getTitle().split(":").length == 2) {
			font.drawStringWithShadow(button.getTitle().split(":")[0], button.getX() + 2, button.getY() + 2.5F, 0xFFFFFFFF);
			font.drawStringWithShadow(button.getTitle().split(":")[1], button.getX() + button.getWidth() - 2 - font.getStringWidth(button.getTitle().split(":")[1]), button.getY() + 2.5F, 0xFFFFFFFF);
		} else {
			font.drawStringWithShadow(button.getTitle(), button.getX() + (button.getWidth() / 2) - (font.getStringWidth(button.getTitle()) / 2), button.getY() + 2.5, 0xFFFFFFFF);
			if (button.getElements().size() > 0) {
				String ext = button.isOpen() ? "<" : ">";
				font.drawStringWithShadow(ext, button.getX() + button.getWidth() - font.getStringWidth(ext) - 2, button.getY() + 2.5, 0xFFFFFFFF);
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
				RenderUtils.rectangle(button.getX() + button.getWidth() + 4, button.getY() - 2, button.getX() + button.getWidth() + renderWidth + 8, button.getY() + renderHeight + 1, 0xFF000000);
			}
		}
	}
	
	@Override
	public void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider) {
		slider.setWidth(121);
		slider.setHeight(14);
		RenderUtils.rectangle(slider.getX(), slider.getY(), slider.getX() + (float) slider.getWidth(), slider.getY() + slider.getHeight(), 0xFF454545);
		RenderUtils.rectangle(slider.getX(), slider.getY(), slider.getX() + (float) slider.getSliderWidth(), slider.getY() + slider.getHeight(), 0x9500A7FF);
		font.drawStringWithShadow(slider.getTitle() + TextFormatting.WHITE + " (" + slider.getValue() + ")", slider.getX() + 2, slider.getY() + 2.5, -1);
	}
	
	@Override
	public void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box) {
		box.setWidth(121);
		box.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		if (!box.isEnabled()) {
			RenderUtils.rectangle(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0xFF454545);
		} else {
			RenderUtils.rectangle(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0x9500A7FF);
		}
		font.drawString(TextFormatting.WHITE + box.getTitle(), box.getX() + 5 + box.getHeight(), box.getY() + 2, -1);
	}
	
	@Override
	public void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame) {
		// RenderUtils.drawRect(frame.getX() + 112, frame.getY() + 2,
		// frame.getX() + frame.getWidth() - 2, frame.getY() + frame.getHeight()
		// - 2, frame.open ? 0x9500A7FF : 0xFF373737);
		if (mouseButton == 0 && mouseX >= frame.getX() + 112 && mouseX <= frame.getX() + frame.getWidth() - 2 && mouseY >= frame.getY() + 2 && mouseY <= frame.getY() + frame.getHeight() - 2) {
			frame.open = !frame.open;
		}
	}
}
