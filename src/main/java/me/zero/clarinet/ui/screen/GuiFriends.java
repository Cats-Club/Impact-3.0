package me.zero.clarinet.ui.screen;

import java.io.IOException;

import me.zero.clarinet.Impact;
import me.zero.clarinet.manager.manager.FriendManager.Friend;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;

public class GuiFriends extends GuiScreen {
	
	private GuiScreen parent;
	
	private FriendSlot slot;
	
	private int slotHeight = 28;
	
	public GuiFriends(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		mc = Minecraft.getMinecraft();
		slot = new FriendSlot(mc, this);
	}
	
	@Override
	public void onGuiClosed() {
		Impact.getInstance().getFriendManager().save();
	}
	
	@Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		slot.handleMouseInput();
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		slot.drawScreen(mouseX, mouseY, partialTicks);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private class FriendSlot extends GuiSlot {
		
		private GuiFriends screen;
		private int selected = 0;
		
		public FriendSlot(Minecraft mcIn, GuiFriends screen) {
			super(mcIn, screen.width, screen.height, 32, screen.height - 59, screen.slotHeight);
			this.screen = screen;
		}
		
		@Override
		protected int getContentHeight() {
			return this.getSize() * slotHeight;
		}
		
		@Override
		protected int getSize() {
			return Impact.getInstance().getFriendManager().getFriends().size();
		}
		
		@Override
		protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
			selected = slotIndex;
		}
		
		@Override
		protected boolean isSelected(int slotIndex) {
			return selected == slotIndex;
		}
		
		@Override
		protected void drawBackground() {
			screen.drawDefaultBackground();
		}
		
		@Override
		protected void drawSlot(int entryID, int insideLeft, int yPos, int insideSlotHeight, int mouseXIn, int mouseYIn, float partialTicks) {
			Friend friend = Impact.getInstance().getFriendManager().getFriends().get(entryID);
			String fixed = friend.getName();
			String alias = friend.getName().equals(friend.getAlias()) ? "N/A" : friend.getAlias();
			if (fixed.length() > 1) {
				fixed = fixed.charAt(0) + "§7" + fixed.substring(1, fixed.length());
			}
			mc.fontRenderer.drawString("Name: §7" + fixed, insideLeft + 2, yPos + 3, 0xFFFFFFFF);
			mc.fontRenderer.drawString("Alias: §7" + alias, insideLeft + 2, yPos + slotHeight - mc.fontRenderer.FONT_HEIGHT - 5, 0xFFFFFFFF);
		}
	}
}
