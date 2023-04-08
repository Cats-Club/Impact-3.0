package me.zero.clarinet.ui.click.para.components;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;

public class LKeybinder extends Component {
	
	private Mod mod;
	
	private boolean binding;
	
	public LKeybinder(Mod mod, int width, int height) {
		this.mod = mod;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		boolean hover = this.mouseInside(mouseX, mouseY);
		int[] enabledGradient = new int[] { -14540254, -14540254, RenderUtils.blend(-14540254, -16777216, 0.95f), RenderUtils.blend(-14540254, -16777216, 0.95f) };
		int[] disabledGradient = new int[] { -13421773 };
		int[] outlineGradient = new int[] { RenderUtils.blend(-15658735, -16777216, 0.95f), RenderUtils.blend(-15658735, -16777216, 0.95f), -15658735, -15658735 };
		RenderUtils.rectangleBorderedGradient(x, y, x + width, y + height, binding ? enabledGradient : disabledGradient, outlineGradient, 1F);
		RenderUtils.rectangle(x + 2.0f, y + 1.0f, x + width - 2.0f, y + 2.0f, binding ? 536870912 : 553648127);
		if (mouseInside(mouseX, mouseY)) {
			RenderUtils.rectangle(x, y, x + width, y + height, new Color(0, 0, 0, 50).getRGB());
		}
		font.drawString("Keybind", x + 6, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
		String s = binding ? ". . ." : Keyboard.getKeyName(mod.getKeybind());
		font.drawString(s, x + width - font.getStringWidth(s) - 4, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.mouseInside(mouseX, mouseY)) {
			if (mouseButton == 0) {
				this.binding = true;
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		if (binding) {
			if (keyCode == Keyboard.KEY_DELETE) {
				mod.setKeybind(0);
			} else {
				mod.setKeybind(keyCode);
			}
			binding = false;
		}
	}
}
