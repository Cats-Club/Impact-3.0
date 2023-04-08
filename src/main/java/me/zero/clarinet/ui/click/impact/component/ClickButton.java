package me.zero.clarinet.ui.click.impact.component;

import me.zero.clarinet.util.render.RenderUtils;
import static org.lwjgl.opengl.GL11.*;

import java.awt.*;

public class ClickButton extends ClickSlotComponent {

    private float hoverTime, enabledTime, extend = 0F;

    private boolean enabled = false;

    private int renderHeight;

    protected ClickButton(String title) {
        this.title = title;
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        float textColor = Math.min(enabledTime + 0.5F + (hoverTime * 0.3F), 1F);
        font.drawStringWithShadow(title, x + 30, y + 9, new Color(textColor, textColor, textColor, 1.0F).getRGB());

        if (components.size() > 0) {

            float rotationAmt = ((extend - height) / (renderHeight - height));
            int centerX = x + 15;
            int centerY = y + 15;

            //Setup
            glPushMatrix();
            glDisable(GL_TEXTURE_2D);
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (rotationAmt > 0.0F) RenderUtils.rotateZ(rotationAmt * 90F, centerX, centerY, 0);

            //Line Smoothing
            glLineWidth(1.0F);
            glEnable(GL_LINE_SMOOTH);
            glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);

            //Draw the Arrow
            glBegin(GL_LINE_STRIP);
            glVertex2f(centerX - 3.0F, centerY + 5.0F);
            glVertex2f(centerX + 3.0F, centerY);
            glVertex2f(centerX - 3.0F, centerY - 5.0F);
            glEnd();

            //Finish
            glPopMatrix();
        }

        RenderUtils.rectangle(x, y, x + width, y + height, new Color(0, 0, 0, hoverTime * 0.1F).getRGB());
        RenderUtils.rectangle(x + 15, y + height, x + 16, y + extend, 0xFFC8C8C8);

        float padding = 6;
        float size = 38;
        int color = RenderUtils.blend(0xFF5BC94F, 0xFFC23030, enabledTime);

        ClickFrame frame = (ClickFrame) this.getParent();

        RenderUtils.startScissor(x + width - (hoverTime * size), y, x + width, y + height);
        RenderUtils.rectangle(x + width - size, y + padding, x + width, y + height - padding, color);
        String text = this.enabled ? "ON" : "OFF";
        font.drawStringWithShadow(text, x + width - 6 - font.getStringWidth(text), y + 9, 0xFFFFFFFF);
        RenderUtils.endScissor();

        if (open || extend > height) {
            float trimX = frame.x;
            float trimY = frame.y + frame.height;
            float trimX1 = frame.x + frame.width;
            float trimY1 = frame.y + Math.min(frame.extend, frame.maxHeight);

            trimY1 = Math.min(this.y + this.extend, trimY1);

            for (ClickComponent comp : components) {
                RenderUtils.startScissor(trimX, trimY, trimX1, trimY1);
                comp.drawElement(mouseX, mouseY, partialTicks);
                RenderUtils.endScissor();
            }
        }
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {
        this.width = 220 - 4;
        this.height = 35;

        float change = 0.008F;
        hoverTime += this.isInside(mouseX, mouseY) ? change : -change;
        hoverTime = Math.max(0F, Math.min(1F, hoverTime));

        change = 0.005F;
        enabledTime += this.enabled ? change : -change;
        enabledTime = Math.max(0F, Math.min(1F, enabledTime));

        change = 1F;
        extend += open ? change : -change;
        extend = Math.max(height, Math.min(renderHeight, extend));

        resizeComponents();
        if (open || extend > height) {
            if (mouseY <= y + extend || this.open) {
                for (ClickComponent comp : components) {
                    comp.onUpdate(mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                this.enabled = !this.enabled;
                this.onToggle();
            } else {
                this.open = !this.open;
            }
        }
        if (open) {
            if (mouseY <= y + extend) {
                for (ClickComponent comp : components) {
                    comp.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (open) {
            for (ClickComponent comp : components) {
                comp.mouseReleased(mouseX, mouseY, state);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (open) {
            for (ClickComponent comp : components) {
                comp.keyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public int getHeight() {
        return (open || extend > height) ? (int)(extend) : this.height;
    }

    public void onToggle() {}

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void resizeComponents() {
        int spacing = 0;
        int elementY = y + height + spacing;
        for (ClickComponent comp : components) {
            comp.setX(x + width / 2 - comp.getWidth() / 2);
            comp.setY(elementY);
            elementY += comp.getHeight() + spacing;
        }
        renderHeight = elementY - y;
    }
}
