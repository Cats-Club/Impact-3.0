package me.zero.clarinet.alt;

import java.io.IOException;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.MojangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.Session;

public class GuiDirectLogin extends GuiScreen {
	
	public GuiScreen parent;
	public GuiTextField usernameBox;
	public PasswordField passwordBox;
	public GuiTextField sessionBox;
	public static Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fontRenderer = mc.fontRenderer;
	
	public GuiDirectLogin(GuiScreen paramScreen) {
		this.parent = paramScreen;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96 + 12, "Login"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 36, "Back"));
		usernameBox = new GuiTextField(3, fontRenderer, width / 2 - 100, 76 - 25, 200, 20);
		passwordBox = new PasswordField(fontRenderer, width / 2 - 100, 116 - 25, 200, 20);
		sessionBox = new GuiTextField(4, fontRenderer, width / 2 - 100, 156 - 25, 200, 20);
		usernameBox.setMaxStringLength(200);
		passwordBox.setMaxStringLength(128);
		sessionBox.setMaxStringLength(257);
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	@Override
	public void updateScreen() {
		usernameBox.updateCursorCounter();
		passwordBox.updateCursorCounter();
		sessionBox.updateCursorCounter();
	}
	
	@Override
	public void mouseClicked(int x, int y, int b) {
		usernameBox.mouseClicked(x, y, b);
		passwordBox.mouseClicked(x, y, b);
		sessionBox.mouseClicked(x, y, b);
		try {
			super.mouseClicked(x, y, b);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			if (!usernameBox.getText().trim().isEmpty() && !passwordBox.getText().trim().isEmpty()) {
				try {
					MojangUtils.login(usernameBox.getText(), passwordBox.getText());
                    GuiAltList.dispErrorString = "Email: \2477" + usernameBox.getText();
				} catch (Exception error) {
					GuiAltList.dispErrorString = "".concat("\247cBad Login \2477(").concat(usernameBox.getText()).concat(")");
				}
			} else if (!sessionBox.getText().trim().isEmpty()) {
				MojangUtils.stealSession(sessionBox.getText());
				GuiAltList.dispErrorString = "";
			} else if (!usernameBox.getText().trim().isEmpty()) {
				((IMinecraft) mc).setSession(new Session(usernameBox.getText().trim(), "-", "0", "Mojang"));
				GuiAltList.dispErrorString = "";
			}
			mc.displayGuiScreen(parent);
		} else if (button.id == 2) {
			mc.displayGuiScreen(parent);
		}
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		usernameBox.textboxKeyTyped(c, i);
		passwordBox.textboxKeyTyped(c, i);
		sessionBox.textboxKeyTyped(c, i);
		if (c == '\t') {
			if (usernameBox.isFocused()) {
				usernameBox.setFocused(false);
				passwordBox.setFocused(true);
				sessionBox.setFocused(false);
			} else if (passwordBox.isFocused()) {
				usernameBox.setFocused(false);
				passwordBox.setFocused(false);
				sessionBox.setFocused(true);
			} else if (sessionBox.isFocused()) {
				usernameBox.setFocused(true);
				passwordBox.setFocused(false);
				sessionBox.setFocused(false);
			}
		}
		if (c == '\r') {
			actionPerformed((GuiButton) buttonList.get(0));
		}
	}
	
	@Override
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
		drawString(fontRenderer, "Username", width / 2 - 100, 63 - 25, 0xA0A0A0);
		drawString(fontRenderer, "\2474*", width / 2 - 106, 63 - 25, 0xA0A0A0);
		drawString(fontRenderer, "Password", width / 2 - 100, 104 - 25, 0xA0A0A0);
		drawString(fontRenderer, "Session ID (Advanced)", width / 2 - 100, 144 - 25, 0xA0A0A0);
		usernameBox.drawTextBox();
		passwordBox.drawTextBox();
		sessionBox.drawTextBox();
		super.drawScreen(x, y, f);
	}
}
