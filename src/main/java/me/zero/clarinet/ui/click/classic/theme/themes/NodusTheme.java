package me.zero.clarinet.ui.click.classic.theme.themes;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.Theme;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.clarinet.util.render.RenderUtils;

public class NodusTheme extends Theme {
	
	@Override
	public void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame) {
		frame.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		frame.setWidth(130);
		int renderHeight = frame.getHeight() + 3;
		for (Element e : frame.getElements()) {
			renderHeight += e.getHeight() + 2;
		}
		renderHeight -= 1;
		if (!frame.open || frame.getElements().size() == 0) {
			drawTopNodusRect(frame.getX() - 3.0F, frame.getY() - 3.0F, frame.getX() + frame.getWidth() - 3, frame.getY() + mc.fontRenderer.FONT_HEIGHT + 5);
		} else {
			drawNodusRect(frame.getX() - 3.0F, frame.getY() + mc.fontRenderer.FONT_HEIGHT + 5.0F, frame.getX() + frame.getWidth() - 3, frame.getY() + renderHeight + 3);
			drawTopNodusRect(frame.getX() - 3.0F, frame.getY() - 3.0F, frame.getX() + frame.getWidth() - 3, frame.getY() + mc.fontRenderer.FONT_HEIGHT + 5);
		}
		int offset = frame.getWidth() - 2;
		drawSmallNodusRect(frame.getX() + offset - mc.fontRenderer.FONT_HEIGHT - 6, frame.getY() + 1.0F, frame.getX() + offset - 5, frame.getY() + mc.fontRenderer.FONT_HEIGHT + 2);
		if (frame.open) {
			drawSmallNodusRect(frame.getX() + offset - mc.fontRenderer.FONT_HEIGHT - 6, frame.getY() + 1.0F, frame.getX() + offset - 5, frame.getY() + mc.fontRenderer.FONT_HEIGHT + 2);
		}
		mc.fontRenderer.drawStringWithShadow(frame.getTitle(), frame.getX() + 2, frame.getY() + 2, -11141291);
		mc.fontRenderer.drawStringWithShadow("(" + frame.getElements().size() + ")", frame.getX() + 2 + mc.fontRenderer.getStringWidth(frame.getTitle() + " "), frame.getY() + 2, -1);
		int elementY = frame.getY() + frame.getHeight() + 3;
		for (Element e : frame.getElements()) {
			e.setX(frame.getX() + 2);
			e.setY(elementY + 1);
			elementY += e.getHeight() + 2;
		}
	}
	
	@Override
	public void drawButton(int mouseX, int mouseY, float partialTicks, Button button) {
		button.setWidth(button.getParent().getWidth() - 4);
		button.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
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
				drawTopNodusRect(button.getX() + button.getWidth(), button.getY() - 5, button.getX() + button.getWidth() + renderWidth + 14, button.getY() + renderHeight + 2);
			}
			String thing = button.isOpen() ? "<" : ">";
			mc.fontRenderer.drawStringWithShadow(thing, button.getX() + button.getWidth() - 6 - mc.fontRenderer.getStringWidth(thing), button.getY() + 2, -1);
		}
		if (button.getTitle().split(":").length == 2) {
			mc.fontRenderer.drawStringWithShadow(button.getTitle().split(":")[0] + " (" + button.getTitle().split(":")[1] + ")", button.getX() + 2, button.getY() + 0.5F, 0xFFFFFFFF);
		} else {
			mc.fontRenderer.drawStringWithShadow(button.getTitle(), button.getX() + 2, button.getY() + 2, -1);
			if (button.isInside(mouseX, mouseY) || button.isEnabled()) {
				mc.fontRenderer.drawStringWithShadow(button.getTitle(), button.getX() + 2, button.getY() + 2, -11141291);
			}
		}
	}
	
	@Override
	public void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider) {
		slider.setWidth(slider.getParent().getWidth() - 4);
		slider.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		drawSmallNodusRect(slider.getX() - 2, slider.getY() - 1, slider.getX() + (float) slider.getSliderWidth(), slider.getY() + slider.getHeight() - 1);
		mc.fontRenderer.drawStringWithShadow(slider.getTitle() + " (" + slider.getValue() + ")", slider.getX() + 2, slider.getY() + 2, -1);
	}
	
	@Override
	public void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box) {
		box.setWidth(box.getParent().getWidth() - 4);
		box.setHeight(mc.fontRenderer.FONT_HEIGHT + 4);
		mc.fontRenderer.drawStringWithShadow(box.getTitle(), box.getX() + 4 + box.getHeight(), box.getY() + 2, -1);
		boolean inside = (mouseX >= box.getX() && mouseX <= box.getX() + box.getHeight()) && (mouseY >= box.getY() && mouseY <= box.getY() + box.getHeight());
		drawSmallNodusRect(box.getX(), box.getY() - 1, box.getX() + (float) box.getHeight(), box.getY() + box.getHeight() - 1);
		boolean mcfont = false;
		if (mcfont) {
			if (box.isEnabled()) {
				mc.fontRenderer.drawStringWithShadow("x", box.getX() + 4, box.getY() + 1, -1);
			}
		} else {
			if (box.isEnabled()) {
				Color c = new Color(-11141291);
				float r = c.getRed() / 255.0F;
				float g = c.getGreen() / 255.0F;
				float b = c.getBlue() / 255.0F;
				RenderUtils.pre();
				GL11.glLineWidth(2.5F);
				GL11.glColor4f(r, g, b, 1);
				GL11.glTranslatef(-0.5F, 0, 0);
				GL11.glBegin(GL11.GL_LINE_STRIP);
				GL11.glVertex2f(box.getX() + box.getHeight() - 3, box.getY() + 2);
				GL11.glVertex2f(box.getX() + box.getHeight() - 7, box.getY() + 9);
				GL11.glVertex2f(box.getX() + box.getHeight() - 9, box.getY() + 6);
				GL11.glEnd();
				RenderUtils.post();
			}
		}
	}
	
	@Override
	public void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame) {
		int offset = frame.getWidth() - 2;
		if (mouseButton == 0) {
			int boundX1 = frame.getX() + offset - mc.fontRenderer.FONT_HEIGHT - 6;
			int boundX2 = frame.getX() + offset - 5;
			int boundY1 = (int) (frame.getY() + 1.0F);
			int boundY2 = frame.getY() + mc.fontRenderer.FONT_HEIGHT + 2;
			if ((mouseX >= boundX1 && mouseX <= boundX2) && (mouseY >= boundY1 && mouseY <= boundY2)) {
				frame.open = !frame.open;
			}
		}
	}
	
	public static void drawNodusButton(float par1, float par2, float par3, float par4, boolean isHighlighted) {
		RenderUtils.rectangle(par1, par2, par3, par4, 1627389951);
		RenderUtils.rectangle(par1 + 1.0F, par2 + 1.0F, par3 - 1.0F, par4 - 1.0F, isHighlighted ? 1627389951 : -1728053248);
	}
	
	public static void drawNodusRect(float par1, float par2, float par3, float par4) {
		RenderUtils.rectangle(par1, par2, par3, par4, 553648127);
		RenderUtils.rectangle(par1 + 2.0F, par2, par3 - 2.0F, par4 - 2.0F, Integer.MIN_VALUE);
	}
	
	public static void drawNodusConsole(float par1, float par2, float par3, float par4) {
		RenderUtils.rectangle(par1 - 2.0F, par2 - 2.0F, par3 + 2.0F, par4 + 2.0F, 553648127);
		RenderUtils.rectangle(par1, par2, par3, par4, Integer.MIN_VALUE);
	}
	
	public static void drawTopNodusRect(float par1, float par2, float par3, float par4) {
		RenderUtils.rectangle(par1, par2, par3, par4, 553648127);
		RenderUtils.rectangle(par1 + 2.0F, par2 + 2.0F, par3 - 2.0F, par4 - 2.0F, -1728053248);
	}
	
	public static void drawNodusTabRect(float par1, float par2, float par3, float par4) {
		RenderUtils.rectangle(par1 - 2.0F, par2 - 2.0F, par3 + 2.0F, par4 + 2.0F, 553648127);
		RenderUtils.rectangle(par1, par2, par3, par4, -1728053248);
	}
	
	public static void drawSmallNodusRect(float par1, float par2, float par3, float par4) {
		RenderUtils.rectangle(par1, par2, par3, par4, 553648127);
		RenderUtils.rectangle(par1 + 1.0F, par2 + 1.0F, par3 - 1.0F, par4 - 1.0F, Integer.MIN_VALUE);
	}
	
	public static void drawSmallNodusButton(float par1, float par2, float par3, float par4, int fillColor) {
		RenderUtils.rectangle(par1 - 1.0F, par2 - 1.0F, par3 + 1.0F, par4 + 1.0F, 553648127);
		RenderUtils.rectangle(par1, par2, par3, par4, fillColor);
	}
	
	public static void drawNodusNametag(int par1, int par2, int par3, int par4, int fillColor) {
		RenderUtils.rectangle(par1, par2, par3, par4, 553648127);
		RenderUtils.rectangle(par1 + 2, par2 + 2, par3 - 2, par4 - 2, fillColor);
	}
}
