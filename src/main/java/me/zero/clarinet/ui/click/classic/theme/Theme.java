package me.zero.clarinet.ui.click.classic.theme;

import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.CheckBox;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import net.minecraft.client.Minecraft;

public abstract class Theme {
	
	protected static final Minecraft mc = Minecraft.getMinecraft();
	
	public abstract void drawFrame(int mouseX, int mouseY, float partialTicks, Frame frame);
	
	public abstract void drawButton(int mouseX, int mouseY, float partialTicks, Button button);
	
	public abstract void drawSlider(int mouseX, int mouseY, float partialTicks, Slider slider);
	
	public abstract void drawCheckbox(int mouseX, int mouseY, float partialTicks, CheckBox box);
	
	public abstract void frameClicked(int mouseX, int mouseY, int mouseButton, Frame frame);
}