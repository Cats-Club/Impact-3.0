package me.zero.clarinet.ui.screen;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import me.zero.clarinet.Impact;
import me.zero.clarinet.alt.GuiAltList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiImpact extends GuiScreen {
	
	public GuiScreen callback;
	public String title = "Impact Menu";
	
	public GuiImpact(GuiScreen callback) {
		this.callback = callback;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(mc.fontRenderer, title, this.width / 2, 20, 0xffffffff);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		int var3 = this.height / 4 + 44;
		
		GuiButton friends = new GuiButton(1, this.width / 2 - 100, var3 - 72, 98, 20, "Friends");
		this.buttonList.add(friends);
		
		GuiButton xray = new GuiButton(2, this.width / 2 - 100 + 102, var3 - 72, 98, 20, "Xray");
		xray.enabled = false;
		this.buttonList.add(xray);
		
		GuiButton keybind = new GuiButton(3, this.width / 2 - 100, var3 - 48, 98, 20, "Keybinds");
		keybind.enabled = false;
		this.buttonList.add(keybind);
		
		GuiButton macro = new GuiButton(4, this.width / 2 - 100 + 102, var3 - 48, 98, 20, "Macros");
		this.buttonList.add(macro);
		
		GuiButton nameprotect = new GuiButton(5, this.width / 2 - 100, var3 - 24, 98, 20, "NameProtect");
		nameprotect.enabled = false;
		this.buttonList.add(nameprotect);
		
		GuiButton alt = new GuiButton(6, this.width / 2 - 100 + 102, var3 - 24, 98, 20, "Alts");
		this.buttonList.add(alt);
		
		GuiButton done = new GuiButton(8, this.width / 2 - 100, this.height / 6 + 168, "Done");
		this.buttonList.add(done);
		
		GuiButton yt = new GuiButton(9, this.width / 2 - 100 + 102, var3, 98, 20, "YouTube Channel");
		this.buttonList.add(yt);
		
		GuiButton font = new GuiButton(10, this.width / 2 - 100, var3 + 24, 98, 20, "Global Font");
		this.buttonList.add(font);
		
		GuiButton files = new GuiButton(12, this.width / 2 - 100, var3, 98, 20, "Impact Files");
		this.buttonList.add(files);
		
		GuiButton credits = new GuiButton(13, this.width / 2 - 100 + 102, var3 + 24, 98, 20, "Credits");
		this.buttonList.add(credits);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		String url = null;
		if (button.id == 1) {
			mc.displayGuiScreen(new GuiFriends(this));
		} else if (button.id == 2) {
			// mc.displayGuiScreen(new ImpactGuiXray(this));
		} else if (button.id == 3) {
			// mc.displayGuiScreen(new ImpactGuiKeybind(this));
		} else if (button.id == 4) {
			mc.displayGuiScreen(new GuiMacro(this));
		} else if (button.id == 5) {
			// mc.displayGuiScreen(new ImpactGuiNameprotect(this));
		} else if (button.id == 6) {
			mc.displayGuiScreen(new GuiAltList(this));
		} else if (button.id == 8) {
			mc.displayGuiScreen(callback);
		} else if (button.id == 9) {
			url = "https://www.youtube.com/channel/UCtOu_E43PHsNkttHj4XucEw";
		} else if (button.id == 10) {
			mc.displayGuiScreen(new GuiGlobalFont(this));
		} else if (button.id == 12) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().open(new File("./" + Impact.getInstance().getName() + "/"));
				} catch (IOException e) {
				}
			}
		} else if (button.id == 13) {
			mc.displayGuiScreen(new GuiCredits(this));
		}
		if (url != null && Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(url));
			} catch (URISyntaxException | IOException e) {
			}
		}
	}
}
