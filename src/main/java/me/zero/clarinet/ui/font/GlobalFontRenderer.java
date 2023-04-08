package me.zero.clarinet.ui.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class GlobalFontRenderer extends FontRenderer {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static final int SIZE = 18;
	
	private final CFontRenderer font;
	
	public GlobalFontRenderer() {
		this(new CFontRenderer(new Font("Verdana", Font.BOLD, 17), true, false));
	}
	
	public GlobalFontRenderer(CFontRenderer font) {
		super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), true);
		this.font = font;
	}

	@Override
	public int drawString(String text, int x, int y, int color) {
		return (int) this.font.drawString(text, x, y, color);
	}
	
	@Override
	public int drawStringWithShadow(String text, float x, float y, int color) {
		return (int) this.font.drawStringWithShadow(text, x, y, color);
	}
	
	@Override
	public int getStringWidth(String text) {
		return this.font.getStringWidth(text);
	}
}
