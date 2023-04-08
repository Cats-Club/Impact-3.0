package me.zero.clarinet.ui.screen;

import java.awt.Font;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.ui.font.GlobalFontRenderer;
import me.zero.clarinet.ui.screen.override.GuiOverrideHandler;
import me.zero.clarinet.ui.screen.override.screen.OverrideGuiMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextFormatting;

public class GuiGlobalFont extends GuiScreen {
	
	public GuiScreen callback;
	
	public GuiTextField font;
	
	public String title = "Global Font";
	
	private boolean globalFont = false;
	
	public GuiGlobalFont(GuiScreen callback) {
		this.mc = Minecraft.getMinecraft();
		this.callback = callback;
	}
	
	@Override
	public void initGui() {
		int var3 = this.height / 4 + 44;
		font = new GuiTextField(3, mc.fontRenderer, this.width / 2 - 100, this.height / 6 + 48 - 6, 200, 20);
		String configfont = Impact.getInstance().getConfigManager().getValue("globalfont-font");
		if (configfont != null) {
			font.setText(configfont);
		}
		String configstyle = Impact.getInstance().getConfigManager().getValue("globalfont-style");
		if (configstyle != null) {
			try {
				Integer val = Integer.valueOf(configstyle);
				FontType.current = FontType.fromInt(val);
			} catch (Exception e) {
			}
		}
		String configgfont = Impact.getInstance().getConfigManager().getValue("globalfont");
		if (configgfont != null) {
			if (configgfont.equalsIgnoreCase("true")) {
				globalFont = true;
			} else if (configgfont.equalsIgnoreCase("false")) {
				globalFont = false;
			}
		}
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 6 + 168, "Cancel"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 6 + 144, "Apply Changes"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, this.height / 6 + 48 - 6 + 24, "Style: " + FontType.current));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 6 + 48 - 6 + 48, "Global Font: " + globalFont));
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			String prefix = "ee:";
			if (font.getText().startsWith(prefix)) {
				if (!font.getText().replaceAll(prefix, "").trim().isEmpty()) {
					String text = font.getText().split(":")[1].toLowerCase();
					switch (text) {
						case "reset" : {
							OverrideGuiMainMenu.background_mode = 0;
							GuiOverrideHandler.metro = false;
							break;
						}
						case "slayerfox" : {
							OverrideGuiMainMenu.background_mode = 1;
							break;
						}
						case "windows8experience" : {
							GuiOverrideHandler.metro = true;
							break;
						}
					}
				}
			}
			mc.displayGuiScreen(callback);
		} else if (button.id == 2) {
			if (globalFont) {
				if (!font.getText().trim().isEmpty()) {
					mc.fontRenderer = new GlobalFontRenderer(new CFontRenderer(new Font(font.getText(), FontType.current.style, GlobalFontRenderer.SIZE), true, false));
					Impact.getInstance().getConfigManager().setValue("globalfont-font", font.getText());
				}
			} else {
				mc.fontRenderer = Impact.getInstance().baseFontRenderer;
			}
			Impact.getInstance().getConfigManager().setValue("globalfont", String.valueOf(globalFont));
			Impact.getInstance().getConfigManager().setValue("globalfont-style", String.valueOf(FontType.current.style));
			mc.displayGuiScreen(callback);
		} else if (button.id == 3) {
			button.displayString = "Style: " + FontType.next();
		} else if (button.id == 4) {
			globalFont = !globalFont;
			button.displayString = "Global Font: " + globalFont;
		}
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public void updateScreen() {
		font.updateCursorCounter();
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		font.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		font.textboxKeyTyped(c, i);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		drawCenteredString(mc.fontRenderer, title, this.width / 2, 20, 0xffffffff);
		drawCenteredString(mc.fontRenderer, TextFormatting.RED + "This is a WIP feature and there will be bugs!", this.width / 2, 30, 0xffffffff);
		drawString(mc.fontRenderer, "Font Name", width / 2 - 100, this.height / 6 + 48 - 6 - 12, 0xA0A0A0);
		font.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private enum FontType {
		PLAIN(Font.PLAIN), BOLD(Font.BOLD), ITALIC(Font.ITALIC);
		
		public static FontType current = FontType.PLAIN;
		
		public final int style;
		
		private FontType(int style) {
			this.style = style;
		}
		
		public static FontType fromInt(int num) {
			for (int i = 0; i < values().length; i++) {
				if (values()[i].style == num) {
					return values()[i];
				}
			}
			return null;
		}
		
		public static FontType next() {
			int index = 0;
			for (int i = 0; i < values().length; i++) {
				if (values()[i] == current) {
					index = i;
				}
			}
			index++;
			if (index >= values().length) {
				index = 0;
			}
			return current = values()[index];
		}
	}
}
