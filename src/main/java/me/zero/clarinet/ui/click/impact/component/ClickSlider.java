package me.zero.clarinet.ui.click.impact.component;

import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.NumberValue;

import java.awt.*;

public class ClickSlider extends ClickComponent {
	
	private NumberValue value;

    private boolean drag = false;

    private float hoverTime = 0F;
	
	protected ClickSlider(String title, NumberValue value) {
		this.title = title;
		this.value = value;
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
        float textColor = Math.min(0.7F + (hoverTime * 0.3F), 1F);
        font.drawStringWithShadow(title, x + 30, y + 9, new Color(textColor, textColor, textColor, 1.0F).getRGB());

        double value = this.value.getValue().doubleValue();
        double min = this.value.getMin().doubleValue();
        double max = this.value.getMax().doubleValue();
        double sliderWidth = (this.width - 30) * (value - min) / (max - min);

        RenderUtils.rectangle(x, y, x + width, y + height, new Color(0, 0, 0, hoverTime * 0.1F).getRGB());
        RenderUtils.rectangle(x + 30, y + height - 2, x + 30 + sliderWidth, y + height - 1, 0xFFC8C8C8);

        String text = String.valueOf(this.value.getValue());
        RenderUtils.startScissor(x + width - (hoverTime * (font.getStringWidth(text) + 10)), y, x + width, y + height);
        font.drawStringWithShadow(text, x + width - 6 - font.getStringWidth(text), y + 9, 0xFFFFFFFF);
        RenderUtils.endScissor();
	}
	
	@Override
	public void onUpdate(int mouseX, int mouseY) {
        this.width = 220 - 4;
        this.height = 35;

        float change = 0.008F;
        hoverTime += this.isInside(mouseX, mouseY) ? change : -change;
        hoverTime = Math.max(0F, Math.min(1F, hoverTime));

        if (this.drag) {
            double diff = Math.min(width, Math.max(0, mouseX - x));

            if (diff == 0) {
                this.value.setValue(this.value.getMin());
                return;
            }

            double min = this.value.getMin().doubleValue();
            double max = this.value.getMax().doubleValue();

            this.value.setValue(MathUtils.roundToPlace((((diff - 30) / (width - 30)) * (max - min)) + min, 2));
        }
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.drag = !this.drag;
            }
        }
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {}
}
