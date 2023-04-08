package me.zero.clarinet.ui.screen.override.screen;

import java.io.IOException;

import me.zero.clarinet.ui.screen.component.GuiButtonWithImage;
import net.mcleaks.GuiRedeemToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class OverrideGuiMultiplayer extends GuiMultiplayer {
	
	public OverrideGuiMultiplayer(GuiScreen parentScreen) {
		super(parentScreen);
	}
	
	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButtonWithImage(50, 4, 4, 20, 20, new ResourceLocation("impact/mcleaksbutton.png")));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 50) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiRedeemToken(false));
			return;
		}
		super.actionPerformed(button);
	}
}
