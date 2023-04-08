package me.zero.clarinet.ui.click.classic.theme.themes;

import java.awt.Color;
import java.awt.Font;

import org.lwjgl.opengl.GL11;

import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.Theme;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.util.text.TextFormatting;

public class NodusXITheme extends Theme {
	
	public CFontRenderer font = new CFontRenderer(new Font("Verdana", Font.BOLD, 17), true, false);
	
	@Override
	public void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame) {
		frame.setHeight(15);
		frame.setWidth(113);
		
		int totalHeight = frame.getHeight();
		if (frame.open) {
			for (Element e : frame.getElements()) {
				totalHeight += e.getHeight() + 2;
			}
		}
		
		RenderUtils.rectangleBordered(frame.getX(), frame.getY(), frame.getX() + frame.getWidth(), frame.getY() + totalHeight, 0xFF000000, 0x60000000);
		font.drawStringWithShadow(frame.getTitle() + " " + TextFormatting.WHITE + "(" + frame.getElements().size() + ")", frame.getX() + 3, frame.getY() + 3, -11141291);
		
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
		button.setWidth(109);
		RenderUtils.rectangleBordered(button.getX(), button.getY(), button.getX() + button.getWidth(), button.getY() + button.getHeight(), 0xFF000000, 0x80000000);
		if (button.getTitle().split(":").length == 2) {
			font.drawStringWithShadow(button.getTitle().split(":")[0], button.getX() + 2, button.getY() + 2.5F, 0xFFFFFFFF);
			font.drawStringWithShadow(button.getTitle().split(":")[1], button.getX() + button.getWidth() - 2 - font.getStringWidth(button.getTitle().split(":")[1]), button.getY() + 2.5F, 0xFFFFFFFF);
		} else {
			if (!button.isInside(mouseX, mouseY)) {
				font.drawStringWithShadow(button.getTitle(), button.getX() + 2, button.getY() + 2, button.isEnabled() ? -11141291 : 0xFFFFFFFF);
			} else {
				font.drawStringWithShadow(TextFormatting.GREEN + button.getTitle(), button.getX() + 2, button.getY() + 2.50F, 0xFFFFFFFF);
			}
			if (button.getElements().size() > 0) {
				String ext = button.isOpen() ? "<" : ">";
				font.drawStringWithShadow(ext, button.getX() + button.getWidth() - font.getStringWidth(ext) - 2, button.getY() + 2, 0xFFFFFFFF);
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
				RenderUtils.rectangleBordered(button.getX() + button.getWidth() + 4, button.getY() - 2, button.getX() + button.getWidth() + renderWidth + 8, button.getY() + renderHeight + 1, 0xFF000000, 0x60000000);
			}
		}
	}
	
	@Override
	public void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider) {
		RenderUtils.rectangleBordered(slider.getX(), slider.getY(), slider.getX() + (float) slider.getSliderWidth(), slider.getY() + slider.getHeight(), 0xFF000000, 0x80000000);
		font.drawString(TextFormatting.GREEN + slider.getTitle() + TextFormatting.WHITE + " (" + slider.getValue() + ")", slider.getX() + 2, slider.getY() + 2, -1);
	}
	
	@Override
	public void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box) {
		box.setWidth(109);
		box.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		RenderUtils.rectangleBordered(box.getX(), box.getY(), box.getX() + box.getHeight(), box.getY() + box.getHeight(), 0xFF000000, 0x80000000);
		font.drawString(box.isEnabled() ? TextFormatting.GREEN + box.getTitle() : TextFormatting.WHITE + box.getTitle(), box.getX() + 5 + box.getHeight(), box.getY() + 2.5F, -1);
		if (box.isEnabled()) {
			Color c = new Color(-11141291);
			float r = c.getRed() / 255.0F;
			float g = c.getGreen() / 255.0F;
			float b = c.getBlue() / 255.0F;
			RenderUtils.pre();
			GL11.glLineWidth(2.5F);
			GL11.glColor4f(r, g, b, 1);
			GL11.glTranslatef(-0.5F, 1, 0);
			GL11.glBegin(GL11.GL_LINE_STRIP);
			GL11.glVertex2f(box.getX() + box.getHeight() - 3, box.getY() + 2);
			GL11.glVertex2f(box.getX() + box.getHeight() - 7, box.getY() + 9);
			GL11.glVertex2f(box.getX() + box.getHeight() - 9, box.getY() + 6);
			GL11.glEnd();
			RenderUtils.post();
		}
	}
	
	@Override
	public void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame) {
		if (mouseButton == 1 && frame.isInside(mouseX, mouseY)) {
			frame.open = !frame.open;
		}
	}
}
