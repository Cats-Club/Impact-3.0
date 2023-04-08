package me.zero.clarinet.ui.screen;

import java.io.IOException;
import java.util.Arrays;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiMacroAdd extends GuiScreen {
	
	public GuiScreen callback;
	public GuiTextField macroBind;
	public GuiTextField commandName;
	public String title = "Add Macro";
	public int bind = 0;
	
	public GuiMacroAdd(GuiScreen callback) {
		this.callback = callback;
	}
	
	@Override
	public void updateScreen() {
		macroBind.updateCursorCounter();
		commandName.updateCursorCounter();
	}
	
	@Override
	protected void keyTyped(char c, int i) {
		if (c == '\t') {
			if (macroBind.isFocused()) {
				commandName.setFocused(true);
				macroBind.setFocused(false);
			} else if (commandName.isFocused()) {
				macroBind.setFocused(true);
				commandName.setFocused(false);
			}
		}
		if (macroBind.isFocused()) {
			macroBind.setText(Keyboard.getKeyName(i));
			bind = i;
		} else if (commandName.isFocused()) {
			commandName.textboxKeyTyped(c, i);
		}
	}
	
	@Override
	public void mouseClicked(int i, int j, int k) {
		try {
			super.mouseClicked(i, j, k);
		} catch (IOException e) {
			e.printStackTrace();
		}
		macroBind.mouseClicked(i, j, k);
		commandName.mouseClicked(i, j, k);
	}
	
	@Override
	public void initGui() {
		buttonList.add(new GuiButton(1, this.width / 2 - 100, 76 + 48 + 24 + 24, "Back"));
		buttonList.add(new GuiButton(2, this.width / 2 - 100, 76 + 48 + 24, "Add Macro"));
		macroBind = new GuiTextField(3, fontRenderer, this.width / 2 - 100, 76, 200, 20);
		commandName = new GuiTextField(3, fontRenderer, this.width / 2 - 100, 112, 200, 20);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			mc.displayGuiScreen(callback);
		} else if (button.id == 2) {
			if (bind != 0) {
				Impact.getInstance().getMacroManager().addMacro(bind, commandName.getText().split(";"));
				mc.displayGuiScreen(callback);
			}
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, title, this.width / 2, 20, 0xffffffff);
		drawString(fontRenderer, "Keybind", width / 2 - 100, 63, 0xa0a0a0);
		drawString(fontRenderer, "Message (For commands, start with \"/\")", width / 2 - 100, 63 + 37, 0xa0a0a0);
		macroBind.drawTextBox();
		commandName.drawTextBox();
		super.drawScreen(i, j, f);
	}
}
