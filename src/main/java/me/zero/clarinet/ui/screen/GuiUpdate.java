package me.zero.clarinet.ui.screen;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.io.OnlineFileReader;
import me.zero.clarinet.util.ClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiUpdate extends GuiScreen {
	
	public static VersionStatus status = VersionStatus.OKAY;
	
	private static String latestVersion;
	
	private GuiScreen callback;
	
	public GuiUpdate(GuiScreen callback) {
		this.callback = callback;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, "Download Update (Recommended)"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 - 10 + 24, "Skip this version (Not Recommended)"));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		String s1 = "An Update for §9" + Impact.getInstance().getName() + "§f has been found!";
		mc.fontRenderer.drawStringWithShadow(s1, width / 2 - mc.fontRenderer.getStringWidth(s1) / 2, this.height / 2 - 50, 0xFFFFFFFF);
		String s2 = "You are running §9" + Impact.getInstance().getBuild() + "§f and §9" + latestVersion + "§f is the latest version!";
		mc.fontRenderer.drawStringWithShadow(s2, width / 2 - mc.fontRenderer.getStringWidth(s2) / 2, this.height / 2 - 35, 0xFFFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 0) {
			if (Desktop.isDesktopSupported()) {
				// Desktop.getDesktop().browse(new URI("http://sh.st/TUNYE"));
				// We don't want to update - Doogie13
			}
		} else if (button.id == 1) {
			mc.displayGuiScreen(callback);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {}
	
	public static void checkForUpdate() {
		try {
			ClientUtils.log("[Update] Checking For Updates");
			OnlineFileReader ofr = new OnlineFileReader(Impact.getURL("latestversion"));
			ofr.read();
			if (ofr.hasFirst()) {
				latestVersion = ofr.getFirst();
				if (latestVersion.startsWith("v")) {
					status = VersionStatus.NEW;
				} else {
					try {
						double curr = Impact.getInstance().getBuild();
						double latest = Double.valueOf(latestVersion);
						if (curr > latest) {
							status = VersionStatus.NEW;
						} else if (curr < latest) {
							status = VersionStatus.UPDATE;
						}
					} catch (Exception e) {
					}
				}
				if (status == VersionStatus.UPDATE) {
					ClientUtils.log("[Update] An Update has been found! (" + latestVersion + ")");
				} else {
					ClientUtils.log("[Update] No Update found!");
				}
			}
		} catch (Exception e) {
		}
	}
	
	public enum VersionStatus {
		OKAY, NEW, UPDATE;
	}
}
