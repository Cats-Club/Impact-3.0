package me.zero.clarinet.ui.click.impact.component;

import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.MultiValue;

import java.awt.*;

public class ClickCombobox extends ClickComponent {

    private MultiValue<?> value;

    private float hoverTime = 0F;

    public ClickCombobox(MultiValue<?> value) {
        this.value = value;
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        float textColor = Math.min(0.7F + (hoverTime * 0.3F), 1F);

        RenderUtils.rectangle(x, y, x + width, y + height, new Color(0, 0, 0, hoverTime * 0.1F).getRGB());

        font.drawStringWithShadow(value.getName(), x + 30, y + 9, new Color(textColor, textColor, textColor, 1.0F).getRGB());
        String text = String.valueOf(this.value.getValue());
        font.drawStringWithShadow(text, x + width - 6 - font.getStringWidth(text), y + 9, 0xFFFFFFFF);
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {
        this.width = 220 - 4;
        this.height = 35;

        float change = 0.008F;
        hoverTime += this.isInside(mouseX, mouseY) ? change : -change;
        hoverTime = Math.max(0F, Math.min(1F, hoverTime));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                value.next();
            } else if (mouseButton == 1) {
                value.last();
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}
}

