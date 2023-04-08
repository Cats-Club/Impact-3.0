package me.zero.clarinet.ui.click.para;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.manager.manager.ConfigManager;
import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.render.ClickGui;
import me.zero.clarinet.ui.click.para.components.Component;
import me.zero.clarinet.ui.click.para.components.LButton;
import me.zero.clarinet.ui.click.para.components.LFrame;
import me.zero.clarinet.ui.click.para.components.LKeybinder;
import me.zero.clarinet.ui.click.para.components.LSlider;
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
import net.minecraft.util.text.TextFormatting;

public class ParaClickGuiManager extends GuiScreen {
	
	private boolean suggestions = true;
	
	private float blur = 0F;
	
	private ArrayList<LFrame> frames = new ArrayList<LFrame>();
	
	public ParaClickGuiManager() {
		EventManager.register(this);
		
		ConfigManager cfg = Impact.getInstance().getConfigManager();
		suggestions = cfg.getValueBoolean("gui-suggestions", true);
		
		Component.font = new GlobalFontRenderer(FontManager.urwgothic);
		int y = 10;
		int width = 215;
		int height = 33;
		this.mc = Minecraft.getMinecraft();
		for (Category cat : Category.values()) {
			LFrame frame = new LFrame(cat.getName(), 10, y, width, 40);
			for (Mod mod : Impact.getInstance().getModManager().getMods(cat)) {
				LButton button = new LButton(mod.getName(), mod.getDescription(), mod.isToggled(), width - 20, height) {
					@Override
					public void onToggle() {
						mod.toggle();
					}
					
					@Override
					public void onUpdate() {
						this.setEnabled(mod.isToggled());
					}
				};
				for (Value value : ValueManager.INSTANCE.getValues(mod)) {
					Component component = null;
					if (value instanceof MultiValue) {
						MultiValue multiValue = (MultiValue) value;
						component = new LButton("", false, width - 40, height) {
							@Override
							public void onToggle() {
								multiValue.next();
							}
							
							@Override
							public void onUpdate() {
								this.setTitle(multiValue.getName() + ":" + multiValue.getValue());
							}
						};
					} else if (value instanceof NumberValue) {
						NumberValue clampedValue = (NumberValue) value;
						component = new LSlider(clampedValue.getName(), clampedValue.getValue().doubleValue(), clampedValue.getMin().doubleValue(), clampedValue.getMax().doubleValue(), clampedValue.getIncrement().doubleValue(), width - 40, height) {
							@Override
							public void onUpdate() {
								if (clampedValue.getValue().doubleValue() != this.getValue()) {
									clampedValue.setValue(this.getValue());
								}
							}
						};
					} else if (value instanceof BooleanValue) {
						BooleanValue booleanValue = (BooleanValue) value;
						component = new LButton(booleanValue.getName(), booleanValue.getValue(), width - 40, height) {
							@Override
							public void onToggle() {
								booleanValue.setValue(!booleanValue.getValue());
							}
							
							@Override
							public void onUpdate() {
								this.setEnabled(booleanValue.getValue());
							}
						};
					}
					if (component != null) {
						button.addComponent(component);
					}
				}
				button.addComponent(new LButton("Visible", mod.doesDisplay(), width - 40, height) {
					@Override
					public void onToggle() {
						mod.setDisplays(!mod.doesDisplay());
					}
					
					@Override
					public void onUpdate() {
						this.setEnabled(mod.doesDisplay());
					}
				});
				button.addComponent(new LKeybinder(mod, width - 40, height));
				frame.addComponent(button);
			}
			y += frame.getHeight() + 4;
			frames.add(frame);
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		boolean inScreen = (mc.currentScreen != null && mc.currentScreen instanceof ParaClickGuiManager);
		
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
			BlurUtils.blurArea(0, 0, mc.displayWidth, mc.displayHeight, blur, blurSize, blurSize);
			BlurUtils.blurArea(0, 0, mc.displayWidth, mc.displayHeight, blur, blurSize, -blurSize);
			GL11.glDisable(GL11.GL_LIGHTING);
		} else {
			super.drawDefaultBackground();
		}
		
		if (suggestions) {
			String[] strings = { "Press LSHIFT to disable suggestions!", "Right click buttons to view more options and keybind", "Left click on buttons to toggle them", "Use the scroll wheel to move up and down panels" };
			int y = sr.getScaledHeight() - mc.fontRenderer.FONT_HEIGHT - 2;
			for (int i = strings.length - 1; i >= 0; i--) {
				String string = TextFormatting.GRAY + strings[i];
				mc.fontRenderer.drawStringWithShadow(string, 2, y, 0xFFFFFFFF);
				y -= mc.fontRenderer.FONT_HEIGHT + 2;
			}
		}
		
		GL11.glPushMatrix();
		float scale = (float) sr.getScaleFactor() / (float) Math.pow(sr.getScaleFactor(), 2.0);
		GL11.glScalef(scale, scale, scale);
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		for (LFrame frame : frames) {
			frame.drawElement(mouseX, mouseY, partialTicks);
		}
		GL11.glPopMatrix();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		mouseX = Mouse.getX();
		mouseY = Display.getHeight() - Mouse.getY();
		LFrame newTop = null;
		for (int i = frames.size() - 1; i >= 0; i--) {
			LFrame frame = frames.get(i);
			frame.mouseClicked(mouseX, mouseY, mouseButton);
			if (frame.didCollide(mouseX, mouseY)) {
				newTop = frame;
				break;
			}
		}
		if (newTop != null) {
			ArrayList<LFrame> newList = new ArrayList<LFrame>();
			for (LFrame frame : frames) {
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
			LFrame frame = frames.get(i);
			frame.mouseReleased(mouseX, mouseY, state);
			if (frame.didCollide(mouseX, mouseY)) {
				break;
			}
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for (LFrame frame : frames) {
			frame.keyTyped(typedChar, keyCode);
		}
		if (keyCode == Keyboard.KEY_LSHIFT) {
			suggestions = !suggestions;
		}
	}
	
	@Override
	public void onGuiClosed() {
		Impact.getInstance().getConfigManager().setValue("gui-suggestions", suggestions);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public ArrayList<LFrame> getFrames() {
		return this.frames;
	}
}
