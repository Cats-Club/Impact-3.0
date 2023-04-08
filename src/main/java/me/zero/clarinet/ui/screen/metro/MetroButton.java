package me.zero.clarinet.ui.screen.metro;

import org.lwjgl.opengl.GL11;

import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class MetroButton extends GuiButton {
	
	private MetroButtonType type;
	
	private ResourceLocation icon;
	
	private MetroPanel parent;
	
	private int color;
	
	private float hover = 0;
	
	public MetroButton(int buttonId, int color, MetroPanel parent, MetroButtonType type, ResourceLocation icon, String buttonText) {
		super(buttonId, 0, 0, type.getSize(), type.getSize(), buttonText);
		this.parent = parent;
		this.color = color;
		this.icon = icon;
		this.type = type;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		
		CFontRenderer font = FontManager.urwgothic_hud;
		
		float x = getX();
		float y = getY();
		int width = type.getSize();
		int height = type.getSize();
		
		RenderUtils.rectangle(x, y, x + width, y + height, color);
		
		if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
			RenderUtils.rectangleBordered(x, y, x + width, y + height, 2, 0xFFFFFFFF, 0x00000000);
		}
		
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(770, 771);
		float scale = 0.25F;
		if (type == MetroButtonType.SMALL) {
			scale /= 2;
		}
		float rescale = 1.0F / scale;
		GL11.glScalef(scale, scale, scale);
		mc.getTextureManager().bindTexture(icon);
		x += 10;
		y += 5;
		x *= rescale;
		y *= rescale;
		int size = 250;
		drawTexturedModalRect(x, y, 0, 0, size, size);
		x /= rescale;
		y /= rescale;
		x -= 10;
		y -= 5;
		GL11.glPopMatrix();
		
		float stringPadding = 4;
		if (type != MetroButtonType.SMALL) {
			font.drawString(displayString, x + stringPadding, y + height - font.getHeight() - stringPadding, 0xFFFFFFFF);
		}
		
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	public MetroButtonType getType() {
		return type;
	}
	
	private float getX() {
		int x = 0;
		int total = 0;
		for (MetroButton button : parent.buttons) {
			if (button == this) {
				break;
			}
			switch (button.getType()) {
				case BIG:
					if (total == parent.maxSize) {
						x = 0;
					} else {
						x += MetroButtonType.BIG.getSize() + MetroButtonType.gap;
					}
					total++;
					break;
				case SMALL:
					// TODO: Support This
					break;
			}
		}
		return parent.panelX + x;
	}
	
	private float getY() {
		int y = 0;
		int total = 0;
		for (MetroButton button : parent.buttons) {
			if (button == this) {
				break;
			}
			switch (button.getType()) {
				case BIG:
					if (total == parent.maxSize) {
						y += MetroButtonType.BIG.getSize() + MetroButtonType.gap;
						total = 0;
					}
					total++;
					break;
				case SMALL:
					// TODO: Support This
					break;
			}
		}
		return parent.panelY + y;
	}
	
	public enum MetroButtonType {
		
		BIG(86), SMALL(42);
		
		public static final int gap = 6;
		
		private int size;
		
		private MetroButtonType(int size) {
			this.size = size;
		}
		
		public int getSize() {
			return this.size;
		}
	}
}
