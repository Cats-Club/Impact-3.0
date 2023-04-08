package me.zero.clarinet.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.util.Helper;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.game.EventKey;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.render.HUD;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.ValueManager;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import me.zero.values.types.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.TextFormatting;

public class TabGui implements Helper {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	private int selectedIndex, selectedModIndex, selectedOptionIndex = 0;
	
	private float tabHeight;
	
	private float selectedCategoryPos, selectedCategoryTargetPos = 0.0F;
	
	private float selectedModPos, selectedModTargetPos = 0.0F;
	
	private float selectedOptionPos, selectedOptionTargetPos = 0.0F;
	
	private State state = State.CATEGORY;
	
	private boolean optionSetting = false;
	
	public void drawGui(FontRenderer font, int x, int y) {
		glPushMatrix();

        Color cSelect1 = new Color(ColorUtils.getColorCode('9'));
        int cSelect = new Color(cSelect1.getRed(), cSelect1.getGreen(), cSelect1.getBlue(), 200).getRGB();
        int border = 0xFF0A0A0A;
		int color = 20;
		int cBackground = new Color(color, color, color, 175).getRGB();
		float selectedPadding = 0.5F;
		float maximumPenileExtension = 1.420F;
		float boxSize = font.FONT_HEIGHT + 2;

		selectedCategoryTargetPos = (boxSize * selectedIndex);
		float catDist = Math.abs(selectedCategoryTargetPos - selectedCategoryPos);
		selectedCategoryPos = smooth(selectedCategoryPos, selectedCategoryTargetPos, 10.0F);
		float catWidth = 0.0F;
		for (Category c : Category.values()) {
			int width = font.getStringWidth(c.getName());
			if (width > catWidth) {
				catWidth = width;
			}
		}
		catWidth = 65;
		float catX = x;
		float catY = y;
		RenderUtils.rectangle(catX, catY, catX + catWidth, catY + (boxSize * Category.values().length), cBackground);
		RenderUtils.rectangle(catX + selectedPadding, catY + selectedCategoryPos + selectedPadding, catX + catWidth - selectedPadding, catY + selectedCategoryPos + boxSize - selectedPadding, cSelect);
		float catTextX = catX + 4F;
		float catTextY = catY + 1F;
		int catIndex = 0;
		for (Category c : Category.values()) {
			float offset = 0F;
			if (catIndex == selectedIndex) {
				offset = boxSize / catDist;
				offset = Math.min(offset, maximumPenileExtension);
			}
			font.drawStringWithShadow(c.getName(), catTextX + offset, catTextY, -1);
			catTextY += boxSize;
			catIndex++;
		}
		if (state != State.CATEGORY) {
			selectedModTargetPos = (boxSize * selectedModIndex);
			float modDist = Math.abs(selectedModTargetPos - selectedModPos);
			selectedModPos = smooth(selectedModPos, selectedModTargetPos, 10.0F);
			float modWidth = 0.0F;
			ArrayList<Mod> mods = Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]);
			for (Mod m : mods) {
				int width = font.getStringWidth(m.getName());
				if (width > modWidth) {
					modWidth = width;
				}
			}
            modWidth += 15;
			float modX = catX + catWidth + 2;
			float modY = catY;
			RenderUtils.rectangle(modX, modY, modX + modWidth, modY + (boxSize * mods.size()), cBackground);
			RenderUtils.rectangle(modX + selectedPadding, modY + selectedModPos + selectedPadding, modX + modWidth - selectedPadding, modY + selectedModPos + boxSize - selectedPadding, cSelect);
			for (Mod m : mods) {
                float yPlus = (boxSize * mods.indexOf(m));
				if (m.isToggled()) {
                    RenderUtils.rectangle(modX + selectedPadding, modY + yPlus + selectedPadding, modX + 2F - selectedPadding, modY + yPlus + boxSize - selectedPadding, cSelect);
                }
                if (ValueManager.INSTANCE.getValues(m).size() > 0) {
                    RenderUtils.rectangle(modX + selectedPadding - 2F + modWidth, modY + yPlus + selectedPadding, modX - selectedPadding + modWidth, modY + yPlus + boxSize - selectedPadding, cSelect);
                }
			}
			float modTextX = modX + 4F;
			float modTextY = modY + 1F;
			int modIndex = 0;
			for (Mod m : mods) {
				float offset = 0F;
				if (modIndex == selectedModIndex) {
                    offset = boxSize / modDist;
                    offset = Math.min(offset, maximumPenileExtension);
                }
				font.drawStringWithShadow((m.isToggled() ? TextFormatting.WHITE : TextFormatting.GRAY) + m.getName(), modTextX + offset, modTextY, -1);
				modTextY += boxSize;
				modIndex++;
			}
			if (state == State.OPTION) {
				selectedOptionTargetPos = (boxSize * selectedOptionIndex);
				float optionDist = Math.abs(selectedOptionTargetPos - selectedOptionPos);
				selectedOptionPos = smooth(selectedOptionPos, selectedOptionTargetPos, 10.0F);
				float optionWidth = 0.0F;
				List<Value<?>> values = ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex));
				for (Value<?> v : values) {
					int width = font.getStringWidth(v.getName());
					if (width > optionWidth) {
						optionWidth = width;
					}
				}
				optionWidth = 110;
				float optionX = catX + catWidth + 2 + modWidth + 2;
				float optionY = catY;
				RenderUtils.rectangle(optionX, optionY, optionX + optionWidth, optionY + (boxSize * values.size()), cBackground);
				RenderUtils.rectangle(optionX + selectedPadding, optionY + selectedOptionPos + selectedPadding, optionX + optionWidth - selectedPadding, optionY + selectedOptionPos + boxSize - selectedPadding, cSelect);
				float optionTextX = optionX + 4F;
				float optionTextY = optionY + 1F;
				int optionIndex = 0;
				for (Value<?> v : values) {
					float offset = 0F;
					if (optionIndex == selectedOptionIndex) {
						offset = boxSize / optionDist;
						offset = Math.min(offset, maximumPenileExtension);
					}
					if (v instanceof MultiValue) {
						MultiValue mv = (MultiValue) v;
						font.drawStringWithShadow(v.getName(), optionTextX + offset, optionTextY, -1);
						font.drawStringWithShadow(mv.getValue().toString(), optionTextX + optionWidth - selectedPadding - font.getStringWidth(mv.getValue().toString()) - 6, optionTextY, -1);
					} else if (v instanceof BooleanValue) {
						BooleanValue bv = (BooleanValue) v;
						font.drawStringWithShadow((bv.getValue() ? TextFormatting.WHITE : TextFormatting.GRAY) + v.getName(), optionTextX + offset, optionTextY, -1);
						if (bv.getValue()) {
							float yPlus = (boxSize * values.indexOf(v));
							RenderUtils.rectangle(optionX + selectedPadding, optionY + yPlus + selectedPadding, optionX + 2F - selectedPadding, optionY + yPlus + boxSize - selectedPadding, cSelect);
						}
					} else if (v instanceof NumberValue) {
						NumberValue nv = (NumberValue) v;
						boolean setting = optionSetting && values.indexOf(v) == selectedOptionIndex;
						font.drawStringWithShadow((setting ? TextFormatting.WHITE : TextFormatting.GRAY) + v.getName(), optionTextX + offset, optionTextY, -1);
						String valStr = (setting ? TextFormatting.WHITE : TextFormatting.GRAY) + nv.getValue().toString();
						font.drawStringWithShadow(valStr, optionTextX + optionWidth - selectedPadding - font.getStringWidth(valStr) - 6, optionTextY, -1);
						
						float sliderWidth = (optionWidth) * (nv.getValue().floatValue() - nv.getMin().floatValue()) / (nv.getMax().floatValue() - nv.getMin().floatValue());
						float yPlus = (boxSize * values.indexOf(v));
						RenderUtils.rectangle(optionX, optionY + yPlus + boxSize - 1F, optionX + sliderWidth, optionY + yPlus + boxSize, cSelect);
					}
					optionTextY += boxSize;
					optionIndex++;
				}
			} else {
				selectedOptionTargetPos = 0;
				selectedOptionIndex = 0;
				selectedOptionPos = 0;
			}
		} else {
			selectedModTargetPos = 0;
			selectedModIndex = 0;
			selectedModPos = 0;
		}
		tabHeight = (boxSize * Category.values().length);
		glPopMatrix();
	}
	
	public float smooth(float pos, float target, float smooth) {
		return smooth(pos, target, Math.abs(target - pos), smooth);
	}
	
	public float smooth(float pos, float target, float dist, float smooth) {
        smooth *= 4;
		float speedRatio = 144.0F / mc.getDebugFPS();
		float catChangeAmount = dist / smooth * speedRatio + 1.0F;
		if (pos < target) {
			pos += catChangeAmount;
			if (pos > target) {
				pos = target;
			}
		} else if (pos > target) {
			pos -= catChangeAmount;
			if (pos < target) {
				pos = target;
			}
		}
		return pos;
	}
	
	@EventTarget
	public void onKeyPress(EventKey event) {
		int key = event.key;
		if (!Impact.getInstance().getModManager().get(HUD.class).tabgui.getValue() || mc.gameSettings.showDebugInfo) {
			return;
		}
		switch (key) {
			case Keyboard.KEY_UP:
				handleUp();
				break;
			case Keyboard.KEY_DOWN:
				handleDown();
				break;
			case Keyboard.KEY_LEFT:
				handleLeft();
				break;
			case Keyboard.KEY_RIGHT:
				handleRight();
				break;
			case Keyboard.KEY_RETURN:
				handleEnter();
				break;
		}
	}
	
	private void handleEnter() {
		if (state == State.MOD) {
			Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex).toggle();
		} else if (state == State.OPTION) {
			Value val = ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex)).get(selectedOptionIndex);
			if (val instanceof BooleanValue) {
				((BooleanValue) val).setValue(!((BooleanValue) val).getValue());
			} else if (val instanceof MultiValue) {
				((MultiValue) val).next();
			} else if (val instanceof NumberValue) {
				optionSetting = !optionSetting;
			}
		}
	}
	
	private void handleRight() {
		if (state == State.CATEGORY) {
			state = State.MOD;
		} else if (state == State.MOD) {
			if (ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex)).size() > 0) {
				state = State.OPTION;
			}
		} else if (optionSetting) {
			Value val = ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex)).get(selectedOptionIndex);
			if (val instanceof NumberValue) {
				NumberValue nv = (NumberValue) val;
				nv.setValue(nv.getValue().doubleValue() + nv.getIncrement().doubleValue());
			}
		}
	}
	
	private void handleLeft() {
		if (state == State.OPTION && !optionSetting) {
			state = State.MOD;
		} else if (state == State.MOD) {
			state = State.CATEGORY;
		} else if (optionSetting) {
			Value val = ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex)).get(selectedOptionIndex);
			if (val instanceof NumberValue) {
				NumberValue nv = (NumberValue) val;
				nv.setValue(nv.getValue().doubleValue() - nv.getIncrement().doubleValue());
			}
		}
	}
	
	private void handleDown() {
		if (state == State.CATEGORY) {
			selectedIndex++;
			if (selectedIndex > Category.values().length - 1) {
				selectedIndex = 0;
			}
		} else if (state == State.MOD) {
			int max = Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).size() - 1;
			selectedModIndex++;
			if (selectedModIndex > max) {
				selectedModIndex = 0;
			}
		} else if (state == State.OPTION && !optionSetting) {
			int max = ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex)).size() - 1;
			selectedOptionIndex++;
			if (selectedOptionIndex > max) {
				selectedOptionIndex = 0;
			}
		}
	}
	
	private void handleUp() {
		if (state == State.CATEGORY) {
			selectedIndex--;
			if (selectedIndex < 0) {
				selectedIndex = Category.values().length - 1;
			}
		} else if (state == State.MOD) {
			int max = Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).size() - 1;
			selectedModIndex--;
			if (selectedModIndex < 0) {
				selectedModIndex = max;
			}
		} else if (state == State.OPTION && !optionSetting) {
			int max = ValueManager.INSTANCE.getValues(Impact.getInstance().getModManager().getMods(Category.values()[selectedIndex]).get(selectedModIndex)).size() - 1;
			selectedOptionIndex--;
			if (selectedOptionIndex < 0) {
				selectedOptionIndex = max;
			}
		}
	}
	
	public float getHeight() {
		return tabHeight;
	}
	
	private enum State {
		CATEGORY, MOD, OPTION;
	}
}
