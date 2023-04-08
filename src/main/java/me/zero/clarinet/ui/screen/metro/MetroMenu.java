package me.zero.clarinet.ui.screen.metro;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.Impact;
import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.ui.screen.metro.MetroButton.MetroButtonType;
import me.zero.clarinet.util.render.BlurUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class MetroMenu extends GuiScreen {
	
	private MetroPanel panel;
	
	@Override
	public void initGui() {
		super.initGui();
		panel = new MetroPanel();
		ArrayList<MetroButton> buttons = new ArrayList<MetroButton>();
		buttons.add(new MetroButton(0, 0xFF3399FF, panel, MetroButtonType.BIG, new ResourceLocation("impact/single.png"), I18n.format("menu.singleplayer")));
		buttons.add(new MetroButton(1, 0xFFFF3300, panel, MetroButtonType.BIG, new ResourceLocation("impact/multi.png"), I18n.format("menu.multiplayer")));
		buttons.add(new MetroButton(2, 0xFF54D422, panel, MetroButtonType.BIG, new ResourceLocation("impact/multi.png"), I18n.format("menu.online")));
		buttons.add(new MetroButton(3, 0xFF8813E8, panel, MetroButtonType.BIG, new ResourceLocation("impact/language.png"), StringUtils.remove(I18n.format("options.language"), '.')));
		buttons.add(new MetroButton(4, 0xFF3399FF, panel, MetroButtonType.BIG, new ResourceLocation("impact/settings.png"), StringUtils.remove(I18n.format("menu.options"), '.')));
		buttons.add(new MetroButton(4, 0xFF3399FF, panel, MetroButtonType.BIG, new ResourceLocation("impact/crosshair.png"), Impact.getInstance().getName()));
		buttons.add(new MetroButton(4, 0xFF3399FF, panel, MetroButtonType.BIG, new ResourceLocation("impact/quit.png"), I18n.format("menu.quit")));
		panel.addButtons(buttons);
		panel.setPosition(10, height / 2 - 60);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawDefaultBackground();
		
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		
		float blur = 2;
		float blurSize = 2;
		BlurUtils.blurArea(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), blur, blurSize, blurSize);
		BlurUtils.blurArea(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), blur, blurSize, -blurSize);
		GL11.glDisable(GL11.GL_LIGHTING);
		
		panel.draw(mouseX, mouseY);
		
		CFontRenderer font = FontManager.urwgothic_hud;
		float faceX = panel.panelX;
		float faceY = panel.panelY - 50;
		float faceSize = 30;
		GL11.glEnable(GL11.GL_BLEND);
		RenderUtils.drawUserFace(mc.getSession().getUsername(), faceX, faceY, faceSize, faceSize);
		RenderUtils.rectangleBordered(faceX, faceY, faceX + faceSize, faceY + faceSize, 1.5, 0xFFFFFFFF, 0x00000000);
		font.drawString("Welcome back, " + mc.getSession().getUsername(), faceX, faceY + faceSize + 7, 0xFFFFFFFF);
	}
}
