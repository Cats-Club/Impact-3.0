package me.zero.clarinet.ui.click.impact.component;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ClickGui;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.RenderUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class ClickFrame extends ClickSlotComponent {

    private boolean drag;

    private int dragX, dragY;

    private int renderHeight;

    public float targetScrollAmount, scrollAmount = 0.0F;

    public float extend = 0.0F;

    public final int maxHeight = 750;

    public ClickFrame(String title) {
        this.title = title;

        ClickGui.FrameState state = Impact.getInstance().getModManager().get(ClickGui.class).getState(this);
        if (state != null) {
            this.x = state.x;
            this.y = state.y;
            this.open = state.open;
        }
    }

    @Override
    public void drawElement(int mouseX, int mouseY, float partialTicks) {
        int dark = 30;

        updateScroll(mouseX, mouseY, maxHeight, renderHeight);
        int cut = (int) Math.min(maxHeight, extend);

        RenderUtils.rectangleBordered(x, y, x + width, y + cut, 1, ColorUtils.getColorCode('9'), new Color(dark, dark, dark, 200).getRGB());
        font.drawStringWithShadow(title, x + (width / 2F) - (font.getStringWidth(title) / 2F), y + 9, 0xFFFFFFFF);

        if (open || extend > height) {
            for (ClickComponent comp : components) {
                RenderUtils.startScissor(x, y + height, x + width, y + cut);
                comp.drawElement(mouseX, mouseY, partialTicks);
                RenderUtils.endScissor();
            }
        }
    }

    @Override
    public void onUpdate(int mouseX, int mouseY) {
        this.width = 220;
        this.height = 35;

        if (drag) {
            x = mouseX - dragX;
            y = mouseY - dragY;
        }

        float change = 1F;
        extend += open ? change : -change;
        extend = Math.max(height, Math.min(renderHeight, extend));

        resizeComponents();
        if (open || extend > height) {
            for (ClickComponent comp : components) {
                if (mouseY > y && (mouseY <= y + Math.min(maxHeight, extend))) {
                    comp.onUpdate(mouseX, mouseY);
                } else {
                    comp.onUpdate(-1, -1);
                }
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isInside(mouseX, mouseY)) {
            if (mouseButton == 0) {
                drag = true;
                dragX = mouseX - x;
                dragY = mouseY - y;
            } else {
                open = !open;
            }
        }
        if (open) {
            for (ClickComponent comp : components) {
                if (mouseY > y && (mouseY <= y + Math.min(maxHeight, extend))) {
                    comp.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        drag = false;
        if (open) {
            for (ClickComponent comp : components) {
                if (mouseY > y && (mouseY <= y + Math.min(maxHeight, extend))) {
                    comp.mouseReleased(mouseX, mouseY, state);
                }
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {}

    public boolean didCollide(int mouseX, int mouseY) {
        return (mouseX >= x && mouseX <= x + width) && (mouseY >= y && mouseY <= y + (open ? renderHeight : height));
    }

    private void resizeComponents() {
        int spacing = 0;
        int elementY = y + height + spacing;
        for (ClickComponent comp : components) {
            comp.setX(x + width / 2 - comp.getWidth() / 2);
            comp.setY(elementY + (int) scrollAmount);
            elementY += comp.getHeight() + spacing;
        }
        renderHeight = elementY - y + 3;
    }

    private void updateScroll(int mouseX, int mouseY, int maxHeight, int rHeight) {
        if (this.isInside(mouseX, mouseY, x, y, width, rHeight)) {
            if (Mouse.hasWheel()) {
                int dw = Mouse.getDWheel();
                int amt = 40;
                if (dw <= -120) {
                    targetScrollAmount -= amt;
                } else if (dw >= 120) {
                    targetScrollAmount += amt;
                }
            }
        }
        targetScrollAmount = Math.max(maxHeight - rHeight, targetScrollAmount);
        targetScrollAmount = Math.min(0, targetScrollAmount);
        if (rHeight < maxHeight) {
            targetScrollAmount = 0;
        }
        float targetDist = Math.abs(targetScrollAmount - scrollAmount);
        float targetChangeAmount = targetDist / 10.0F * 144.0F / mc.getDebugFPS() + 0.25F;
        if (scrollAmount < targetScrollAmount) {
            scrollAmount += targetChangeAmount;
            if (scrollAmount > targetScrollAmount) {
                scrollAmount = targetScrollAmount;
            }
        } else if (scrollAmount > targetScrollAmount) {
            scrollAmount -= targetChangeAmount;
            if (scrollAmount < targetScrollAmount) {
                scrollAmount = targetScrollAmount;
            }
        }
    }
}
