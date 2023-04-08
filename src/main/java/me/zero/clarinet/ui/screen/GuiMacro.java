package me.zero.clarinet.ui.screen;

import java.io.IOException;
import java.util.Arrays;

import me.zero.clarinet.manager.manager.FriendManager;
import me.zero.clarinet.manager.manager.MacroManager;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiMacro extends GuiScreen {
	
	private List slot;
	
	public GuiScreen callback;
	public String title = "Macros";
    private int slotHeight = 28;
	
	public GuiMacro(GuiScreen callback) {
        this.mc = Minecraft.getMinecraft();
		this.callback = callback;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 154, this.height - 52, 150, 20, "Add Macro"));
		this.buttonList.add(new GuiButton(2, this.width / 2 + 4, this.height - 52, 150, 20, "Reset Macros"));
		this.buttonList.add(new GuiButton(3, this.width / 2 - 154, this.height - 28, 150, 20, "Delete Macro"));
		this.buttonList.add(new GuiButton(4, this.width / 2 + 4, this.height - 28, 150, 20, "Back"));
		this.slot = new List(this.mc, this);
		this.slot.registerScrollButtons(7, 8);
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.slot.handleMouseInput();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button.id == 1) {
			mc.displayGuiScreen(new GuiMacroAdd(this));
		} else if (button.id == 2) {
			GuiYesNo check = new GuiYesNo(this, "Are you sure you want to clear the macro list?") {
				@Override
				protected void actionPerformed(GuiButton button) {
					if (button.id == 1) {
						Impact.getInstance().getMacroManager().reset();
						mc.displayGuiScreen(callback);
					} else if (button.id == 2) {
						mc.displayGuiScreen(callback);
					}
				}
			};
			mc.displayGuiScreen(check);
		} else if (button.id == 3) {
			if (slot.selected != -1) {
				Impact.getInstance().getMacroManager().getMacros().remove(slot.selected);
                slot.selected = -1;
			}
		} else if (button.id == 4) {
			mc.displayGuiScreen(callback);
		}
	}
	
	@Override
	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, title, this.width / 2, 20, 0xffffffff);

        this.slot.drawScreen(i, j, f);
		super.drawScreen(i, j, f);
	}

    private class List extends GuiSlot {

        private int selected = -1;
        private GuiMacro screen;

		public List(Minecraft mcIn, GuiMacro screen) {
			super(mcIn, GuiMacro.this.width, GuiMacro.this.height, 32, GuiMacro.this.height - 65 + 4, screen.slotHeight);
            this.screen = screen;
		}

        @Override
        protected int getContentHeight() {
            return this.getSize() * slotHeight;
        }

        @Override
		protected int getSize() {
			return Impact.getInstance().getMacroManager().getMacros().size();
		}
		
		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
            selected = slotIndex;
		}
		
		@Override
		protected boolean isSelected(int slotIndex) {
			return slotIndex == selected;
		}
		
		@Override
		protected void drawBackground() {
			screen.drawDefaultBackground();
		}
		
		@Override
		protected void drawSlot(int slotIndex, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks) {
			MacroManager.Macro macro = Impact.getInstance().getMacroManager().getMacros().get(slotIndex);
            String macroKey = Keyboard.getKeyName(macro.getKey());
            String messages = Arrays.toString(macro.getMessages());
            mc.fontRenderer.drawString("Key: §7" + macroKey, insideLeft + 2, yPos + 3, 0xFFFFFFFF);
            mc.fontRenderer.drawString("Messages: §7" + messages, insideLeft + 2, yPos + slotHeight - mc.fontRenderer.FONT_HEIGHT - 5, 0xFFFFFFFF);
		}
	}
}
