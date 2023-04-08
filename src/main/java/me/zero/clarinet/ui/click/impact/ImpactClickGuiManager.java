package me.zero.clarinet.ui.click.impact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.ui.click.impact.component.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.render.ClickGui;
import me.zero.clarinet.ui.font.GlobalFontRenderer;
import me.zero.clarinet.util.render.BlurUtils;
import me.zero.values.ValueManager;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class ImpactClickGuiManager extends GuiScreen {

	private ArrayList<ClickFrame> frames = new ArrayList<>();

	private float blur = 0F;
	
	public ImpactClickGuiManager() {
		ClickComponent.font = new GlobalFontRenderer(FontManager.urwgothic);
		EventManager.register(this);
		
		this.mc = Minecraft.getMinecraft();
		for (Category cat : Category.values()) {
			ClickFrame frame = new ClickFrame(cat.getName());
			for (Mod mod : Impact.getInstance().getModManager().getMods(cat)) {
				ClickButton button = new ClickButton(mod.getName()){
                    @Override
                    public void onToggle() {
                        mod.toggle();
                    }

                    @Override
                    public void onUpdate(int mouseX, int mouseY) {
                        this.setEnabled(mod.isToggled());
                        super.onUpdate(mouseX, mouseY);
                    }
                };
				for (Value value : ValueManager.INSTANCE.getValues(mod)) {
					if (value instanceof BooleanValue) {
						BooleanValue val = (BooleanValue) value;
                        button.addComponent(new ClickCheckbox(val));
					} else if (value instanceof NumberValue) {
						NumberValue val = (NumberValue) value;
						ClickSlider component = new ClickSlider(val.getName(), val){
							@Override
							public void onUpdate(int mouseX, int mouseY) {
								super.onUpdate(mouseX, mouseY);
							}
						};
						button.addComponent(component);
					} else if (value instanceof MultiValue) {
						MultiValue val = (MultiValue) value;
						button.addComponent(new ClickCombobox(val));
					}
				}
				frame.addComponent(button);
			}
			frames.add(frame);
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		boolean inScreen = (mc.currentScreen != null && mc.currentScreen instanceof ImpactClickGuiManager);
		
		ClickGui click = Impact.getInstance().getModManager().get(ClickGui.class);
		float fade = click.blur_speed.getValue().floatValue();
		if (inScreen) {
			blur += fade;
		} else {
			blur -= fade;
		}
		blur = Math.max(blur, 0);
		blur = Math.min(blur, 10);
	}

    @Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(this.mc);

		ClickGui click = Impact.getInstance().getModManager().get(ClickGui.class);
		if (click.blur.getValue()) {
			float blurSize = (click.blur_amount.getValue().floatValue() / 5F) / (20 - blur);
			BlurUtils.blurArea(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), blur, blurSize, blurSize);
			BlurUtils.blurArea(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), blur, blurSize, -blurSize);
			GL11.glDisable(GL11.GL_LIGHTING);
		} else {
			super.drawDefaultBackground();
		}

		GL11.glPushMatrix();
		float scale = (float) sr.getScaleFactor() / (float) Math.pow(sr.getScaleFactor(), 2.0);
		GL11.glScalef(scale, scale, scale);
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		for (ClickFrame frame : frames) {
            float offset = Math.max(0.1F, 60F / mc.getDebugFPS());
            for (float i = 0; i < offset; i += 0.1F) {
                frame.onUpdate(mouseX, mouseY);
            }
			frame.drawElement(mouseX, mouseY, partialTicks);
		}
		GL11.glPopMatrix();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		ClickFrame newTop = null;
		for (int i = frames.size() - 1; i >= 0; i--) {
			ClickFrame frame = frames.get(i);
			frame.mouseClicked(mouseX, mouseY, mouseButton);
			if (frame.didCollide(mouseX, mouseY)) {
				newTop = frame;
				break;
			}
		}
		if (newTop != null) {
			ArrayList<ClickFrame> newList = new ArrayList<ClickFrame>();
			for (ClickFrame frame : frames) {
				if (frame != newTop) {
					newList.add(frame);
				}
			}
			newList.add(newTop);
			frames = newList;
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		for (int i = frames.size() - 1; i >= 0; i--) {
			ClickFrame frame = frames.get(i);
			frame.mouseReleased(mouseX, mouseY, state);
			if (frame.didCollide(mouseX, mouseY)) {
				break;
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for (ClickFrame frame : frames) {
			frame.keyTyped(typedChar, keyCode);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	public List<ClickFrame> getFrames() {
        return this.frames;
    }
}
