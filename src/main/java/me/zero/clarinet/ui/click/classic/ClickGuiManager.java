package me.zero.clarinet.ui.click.classic;

import java.util.ArrayList;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.ui.click.classic.elements.Button;
import me.zero.clarinet.ui.click.classic.elements.Element;
import me.zero.clarinet.ui.click.classic.elements.Frame;
import me.zero.clarinet.ui.click.classic.elements.Slider;
import me.zero.clarinet.ui.click.classic.theme.ThemeManager;
import me.zero.clarinet.ui.click.classic.theme.ThemeManager.ETheme;
import me.zero.values.ValueManager;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.Value;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiManager extends GuiScreen {
	
	public ArrayList<Frame> frames = new ArrayList<Frame>();
	
	public ThemeManager themeManager;
	
	public ClickGuiManager() {
		themeManager = new ThemeManager();
		this.setupFrames();
	}
	
	public void setupFrames() {
		int y = 10;
		for (Category cat : Category.values()) {
			ArrayList<Mod> mods = Impact.getInstance().getModManager().getMods(cat);
			Frame frame = new Frame(cat.getName(), 10, y);
			for (Mod mod : mods) {
				Button button = new Button(mod.getName(), frame) {
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
					Element component = null;
					if (value instanceof MultiValue) {
						MultiValue multiValue = (MultiValue) value;
						component = new Button("", button) {
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
						component = new Slider(clampedValue.getName(), clampedValue.getMin().doubleValue(), clampedValue.getMax().doubleValue(), clampedValue.getValue().doubleValue(), clampedValue.getIncrement().doubleValue(), button) {
							@Override
							public void onValueSet(double newValue) {
								clampedValue.setValue(newValue);
							}
						};
					} else if (value instanceof BooleanValue) {
						BooleanValue booleanValue = (BooleanValue) value;
						component = new Button(booleanValue.getName(), button) {
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
						button.addElement(component);
					}
				}
				frame.addElement(button);
			}
			y += 20;
			frames.add(frame);
		}
		final Frame themeFrame = new Frame("Theme", 10, frames.get(frames.size() - 1).getY() + 20);
		for (final ETheme theme : themeManager.getThemes()) {
			themeFrame.addElement(new Button(theme.getName(), themeFrame) {
				@Override
				public void onToggle() {
					themeManager.setTheme(theme.getTheme());
					Impact.getInstance().getConfigManager().setValue("theme", themeManager.getCurrentThemeName());
				}
			});
		}
		frames.add(themeFrame);
		String theme = Impact.getInstance().getConfigManager().getValue("theme");
		if (theme != null) {
			for (ETheme th : themeManager.getThemes()) {
				if (th.getName().equalsIgnoreCase(theme)) {
					themeManager.setTheme(th.getTheme());
				}
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		for (Frame frame : frames) {
			frame.drawElement(mouseX, mouseY, partialTicks);
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		Frame newTop = null;
		for (Frame frame : frames) {
			frame.mouseClicked(mouseX, mouseY, mouseButton);
			if (frame.isInside(mouseX, mouseY)) {
				newTop = frame;
			}
		}
		if (newTop != null) {
			ArrayList<Frame> newList = new ArrayList<Frame>();
			for (Frame frame : frames) {
				if (frame != newTop) {
					newList.add(frame);
				}
			}
			newList.add(newTop);
			frames = newList;
		}
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
		for (Frame frame : frames) {
			frame.mouseReleased(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	public ThemeManager getThemeManager() {
		return themeManager;
	}
}
