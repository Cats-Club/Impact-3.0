package me.zero.clarinet.ui.screen.override.screen;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.io.OnlineFileReader;
import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.ui.screen.GuiImpact;
import me.zero.clarinet.ui.screen.GuiUpdate;
import me.zero.clarinet.ui.screen.GuiUpdate.VersionStatus;
import me.zero.clarinet.ui.screen.component.ImpactButton;
import me.zero.clarinet.util.Callback;
import me.zero.clarinet.util.misc.MiscUtils;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.util.ResourceLocation;

public class OverrideGuiMainMenu extends GuiScreen {
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation("impact/background.png");
	
	private static final ResourceLocation SINGLEPLAYER = new ResourceLocation("impact/single.png");
	
	private static final ResourceLocation MULTIPLAYER = new ResourceLocation("impact/multi.png");
	
	private static final ResourceLocation SETTINGS = new ResourceLocation("impact/settings.png");
	
	private static final ResourceLocation LANGUAGE = new ResourceLocation("impact/language.png");
	
	private static final ResourceLocation IMPACT = new ResourceLocation("impact/crosshair.png");
	
	private static final ResourceLocation QUIT = new ResourceLocation("impact/quit.png");
	
	private static ResourceLocation BACKGROUND_1 = null;
	
	private static boolean versionCheck = false;
	
	public static int background_mode = 0;
	
	private static String motd = "";
	
	private GuiButton realmsButton;
	
	private long init;
	
	@Override
	public void initGui() {
		int j = this.height / 4 + 80;
		this.buttonList.add(new ImpactButton(1, this.width / 2 - 102 - 202, j + 72 - 50, 98, 70, I18n.format("menu.singleplayer")));
		this.buttonList.add(new ImpactButton(2, this.width / 2 - 102 - 100, j + 72 - 50, 98, 70, I18n.format("menu.multiplayer")));
		this.realmsButton = new ImpactButton(14, 4, this.height - 24, 98, 20, I18n.format("menu.online"));
		this.buttonList.add(realmsButton);
		this.buttonList.add(new ImpactButton(5, this.width / 2 + 2, j + 72 - 50, 98, 70, StringUtils.remove(I18n.format("options.language"), '.')));
		this.buttonList.add(new ImpactButton(420, this.width / 2 + 2 + 102, j + 72 - 50, 98, 70, Impact.getInstance().getName()));
		this.buttonList.add(new ImpactButton(0, this.width / 2 - 100, j + 72 - 50, 98, 70, StringUtils.remove(I18n.format("menu.options"), '.')));
		this.buttonList.add(new ImpactButton(4, this.width / 2 + 2 + 102 + 102, j + 72 - 50, 98, 70, I18n.format("menu.quit")));
		init = System.currentTimeMillis();
		if (BACKGROUND_1 == null && background_mode == 1) {
			String url = "http://i.imgur.com/C7x2ire.jpg";
			BACKGROUND_1 = MiscUtils.getImageFromURL(url);
		}
		OnlineFileReader.getStringNewThread(new Callback<String>() {
			@Override
			public void done(String callback) {
				motd = callback;
			}
		}, Impact.getURL("motd"));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}
		if (button.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiWorldSelection(this));
		}
		if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		if (button.id == 14 && this.realmsButton.visible) {
			this.switchToRealms();
		}
		if (button.id == 4) {
			this.mc.shutdown();
		}
		if (button.id == 420) {
			mc.displayGuiScreen(new GuiImpact(this));
			return;
		}
		super.actionPerformed(button);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		CFontRenderer fontRenderer = FontManager.urwgothic_hud;
		if ((System.currentTimeMillis() - init >= 500) && !versionCheck) {
			if (GuiUpdate.status == VersionStatus.UPDATE) {
				mc.displayGuiScreen(new GuiUpdate(this));
				versionCheck = true;
			}
		}
		int i = 274;
		drawBackground(new ScaledResolution(mc));
		int width = 300;
		int border = 10;

		RenderUtils.rectangleBordered(this.width / 2 - width - border, this.height / 4 + 5 + border, this.width / 2 + width + border, this.height / 4 + 200 - border, 1, 0xFFFFFFFF, 0x80000000);

		String name = Impact.getInstance().getName();
		FontManager.urwgothic_title.drawCenteredString(name, (this.width / 2), (this.height / 4 + 40 + FontManager.urwgothic_title.getHeight() / 2), 0xFFFFFFFF);
		String build = String.valueOf(Impact.getInstance().getBuild());
		fontRenderer.drawString(build, (this.width / 2) + (FontManager.urwgothic_title.getStringWidth(name) / 2), (this.height / 4 + 40 + FontManager.urwgothic_title.getHeight() / 2), 0xFFFFFFFF);
		fontRenderer.drawCenteredString(motd, this.width / 2, (this.height / 4 + 58 + FontManager.urwgothic_title.getHeight()), -1);
		RenderUtils.drawUserInfoBox(this, 4, 4);

		String var1 = "Minecraft 1.10";
		String var2 = "Copyright Mojang AB";
		String var3 = "Do not distribute!";
		fontRenderer.drawString(var1, this.width - fontRenderer.getStringWidth(var1) - 4, 4, -1);
		fontRenderer.drawString(var2, this.width - fontRenderer.getStringWidth(var2) - 4, 14, -1);
		fontRenderer.drawString(var3, this.width - fontRenderer.getStringWidth(var3) - 4, 24, -1);

		GlStateManager.pushMatrix();
		double scale = 0.2;
		GlStateManager.scale(scale, scale, 1);
		renderIcons(this.height / 4 + 80, scale);
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private void renderIcons(int j, double scale) {
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		mc.getTextureManager().bindTexture(SINGLEPLAYER);
		this.drawTexturedModalRect((int) ((this.width / 2 - 102 - 202) / scale) + 118, (int) ((j + 72) / scale) - 240, 0, 0, 220, 220);
		mc.getTextureManager().bindTexture(MULTIPLAYER);
		this.drawTexturedModalRect((int) ((this.width / 2 - 202) / scale) + 119, (int) ((j + 72) / scale) - 240, 0, 0, 250, 250);
		mc.getTextureManager().bindTexture(SETTINGS);
		this.drawTexturedModalRect((int) ((this.width / 2 - 101) / scale) + 119, (int) ((j + 75) / scale) - 240, 0, 0, 250, 250);
		mc.getTextureManager().bindTexture(LANGUAGE);
		this.drawTexturedModalRect((int) ((this.width / 2) / scale) + 119, (int) ((j + 75) / scale) - 240, 0, 0, 250, 250);
		mc.getTextureManager().bindTexture(IMPACT);
		this.drawTexturedModalRect((int) ((this.width / 2 + 104) / scale) + 119, (int) ((j + 72) / scale) - 240, 0, 0, 250, 250);
		mc.getTextureManager().bindTexture(QUIT);
		this.drawTexturedModalRect((int) ((this.width / 2 + 210) / scale) + 119, (int) ((j + 75) / scale) - 240, 0, 0, 250, 250);
		GlStateManager.disableBlend();
	}
	
	private void switchToRealms() {
		RealmsBridge realmsbridge = new RealmsBridge();
		realmsbridge.switchToRealms(this);
	}
	
	private void drawBackground(ScaledResolution scaledRes) {
		GlStateManager.disableDepth();
		GlStateManager.depthMask(false);
		GlStateManager.tryBlendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.disableAlpha();
		if (background_mode == 0 || BACKGROUND_1 == null) {
			this.mc.getTextureManager().bindTexture(BACKGROUND);
		}
		if (background_mode == 1) {
			this.mc.getTextureManager().bindTexture(BACKGROUND_1);
		}
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();
		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		vertexbuffer.pos(0.0D, (double) scaledRes.getScaledHeight(), -90.0D).tex(0.0D, 1.0D).endVertex();
		vertexbuffer.pos((double) scaledRes.getScaledWidth(), (double) scaledRes.getScaledHeight(), -90.0D).tex(1.0D, 1.0D).endVertex();
		vertexbuffer.pos((double) scaledRes.getScaledWidth(), 0.0D, -90.0D).tex(1.0D, 0.0D).endVertex();
		vertexbuffer.pos(0.0D, 0.0D, -90.0D).tex(0.0D, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableDepth();
		GlStateManager.enableAlpha();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
