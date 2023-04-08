package me.zero.clarinet.ui.screen.component;

import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class ImpactButton extends GuiButton {
	
	private float hoverTime = 0;
	
	public ImpactButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
		super(buttonId, x, y, widthIn, heightIn, buttonText);
	}
	
	public ImpactButton(int buttonId, int x, int y, String buttonText) {
		super(buttonId, x, y, buttonText);
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			CFontRenderer fontrenderer = FontManager.urwgothic_hud;
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
			int max = this.width - 40;
			float target = this.hovered ? max : 0;
			float dist = Math.abs(target - hoverTime);
			float change = dist / 4.0F + 2.5F;
			if (this.enabled) {
				if (this.hovered) {
					hoverTime += change;
				} else {
					hoverTime -= change;
				}
			}
			hoverTime = Math.min(max, hoverTime);
			hoverTime = Math.max(0, hoverTime);

            float perc = hoverTime / max;

            int j = RenderUtils.blend(0xFFFFFFF, 0xFFCFCFCF, perc);
            int c2 = MathUtils.getShadowColor(j);

			RenderUtils.rectangle(this.x + (this.width / 2) - (hoverTime / 2) + 1, this.y + this.height - 1 + 1, this.x + (this.width / 2) + (hoverTime / 2) + 1, this.y + this.height + 1, c2);
			RenderUtils.rectangle(this.x + (this.width / 2) - (hoverTime / 2), this.y + this.height - 1, this.x + (this.width / 2) + (hoverTime / 2), this.y + this.height, j);
			// RenderUtils.rectangleBordered(this.xPosition, this.yPosition,
			// this.xPosition + this.width, this.yPosition + this.height,
			// 0xFF080808, 0x00000000);
			fontrenderer.drawCenteredStringWithShadow(this.displayString, this.x + this.width / 2, this.y + (this.height - 12), j);
		}
	}
}
