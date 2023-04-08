package me.zero.clarinet.ui.click.para.components;

import me.zero.clarinet.util.Helper;
import org.lwjgl.input.Mouse;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.render.ClickGui;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.Minecraft;

public class LFrame extends LSlotComponent implements Helper {
	
	private boolean drag;
	
	private int dragX, dragY;
	
	private float targetScrollAmount = 0.0F;
	
	private float scrollAmount = 0.0F;
	
	private int renderHeight;
	
	private int maxHeight = 750;
	
	public LFrame(String title, int x, int y, int width, int height) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

        ClickGui.FrameState state = Impact.getInstance().getModManager().get(ClickGui.class).getState(this);
        if (state != null) {
            this.x = state.x;
            this.y = state.y;
            this.open = state.open;
        }
	}
	
	@Override
	public void drawElement(int mouseX, int mouseY, float partialTicks) {
		if (drag) {
			x = mouseX - dragX;
			y = mouseY - dragY;
		}
		if (Impact.getInstance().getModManager().get(ClickGui.class).snap.getValue()) {
			double increment = 10;
			x = (int) ((double) Math.round(x * (1.0 / increment)) / (1.0 / increment));
			y = (int) ((double) Math.round(y * (1.0 / increment)) / (1.0 / increment));
		}
		int[] fillGradient = new int[] { -14540254, -14540254, RenderUtils.blend(-14540254, -16777216, 0.95f), RenderUtils.blend(-14540254, -16777216, 0.95f) };
		int[] outlineGradient = new int[] { RenderUtils.blend(-15658735, -16777216, 0.95f), RenderUtils.blend(-15658735, -16777216, 0.95f), -15658735, -15658735 };
		
		renderHeight = height;
		int spacing = 3;
		for (Component comp : components) {
			renderHeight += comp.getHeight() + spacing;
		}
		renderHeight += 10;

		if (open && components.size() > 0) {
			RenderUtils.rectangleBorderedGradient(x, y, x + width, y + Math.min(renderHeight, maxHeight), fillGradient, outlineGradient, 1.0f);
		} else {
			RenderUtils.rectangleBorderedGradient(x, y, x + width, y + height, fillGradient, outlineGradient, 1.0f);
		}

		renderScroll(maxHeight, renderHeight);
		font.drawStringWithShadow(title, x + width / 2 - font.getStringWidth(title) / 2, y + height / 2 - font.FONT_HEIGHT / 2, 0xFFFFFFFF);
		if (open) {
			resizeComponents();
			updateScroll(mouseX, mouseY, maxHeight, renderHeight);
			RenderUtils.startScissor(x, y + height, mc.displayWidth, y + maxHeight - 5);
			for (Component comp : components) {
				comp.drawElement(mouseX, mouseY, partialTicks);
			}
			RenderUtils.endScissor();
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (this.mouseInside(mouseX, mouseY)) {
			if (mouseButton == 0) {
				drag = true;
				dragX = mouseX - x;
				dragY = mouseY - y;
			} else {
				open = !open;
			}
		}
		if (open) {
			if (y + height - mouseY > 0) {
				return;
			}
			if(y + Math.min(renderHeight, maxHeight) - mouseY < 0){
				return;
			}
			for (Component comp : components) {
				comp.mouseClicked(mouseX, mouseY, mouseButton);
			}
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		drag = false;
		if (open) {
			for (Component comp : components) {
				comp.mouseReleased(mouseX, mouseY, state);
			}
		}
	}
	
	@Override
	public void keyTyped(char typedChar, int keyCode) {
		for (Component comp : components) {
			comp.keyTyped(typedChar, keyCode);
		}
	}
	
	public boolean didCollide(int mouseX, int mouseY) {
		if (this.isOpen()) {
			return this.mouseInside(mouseX, mouseY, x, y, width, renderHeight);
		} else {
			return this.mouseInside(mouseX, mouseY);
		}
	}
	
	private void resizeComponents() {
		int spacing = 3;
		int elementY = y + height + spacing;
		for (Component comp : components) {
			comp.setX(x + width / 2 - comp.getWidth() / 2);
			comp.setY(elementY + (int) scrollAmount);
			elementY += comp.getHeight() + spacing;
		}
	}
	
	private void updateScroll(int mouseX, int mouseY, int maxHeight, int rHeight) {
		if (this.mouseInside(mouseX, mouseY, x, y, width, rHeight)) {
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
	
	private void renderScroll(int maxHeight, int rHeight) {
		if (this.open && maxHeight < rHeight) {
			float s = -scrollAmount;
			float ext = (float) rHeight - (float) maxHeight;
			float ext2 = s / ext;
			float perc = (float) (maxHeight + 4) / (float) rHeight;
			float scrollHeight = (float) maxHeight * perc;
			float offset = ext2 * ((float) maxHeight - scrollHeight) + 5;
			RenderUtils.rectangle(x + width - 9, y + height + offset, x + width - 6, y + offset + scrollHeight - 15, 1090519039);
		}
	}
}
