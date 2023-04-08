package me.zero.clarinet.alt;

import me.zero.clarinet.Impact;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;

public class SlotAlt extends GuiSlot {
	
	private GuiAltList aList;
	
	private int selected;
	
	public SlotAlt(Minecraft theMinecraft, GuiAltList aList) {
		super(theMinecraft, aList.width, aList.height, 32, aList.height - 59, Impact.getInstance().getAltManager().slotHeight);
		this.aList = aList;
		this.selected = 0;
	}
	
	@Override
	protected int getContentHeight() {
		return this.getSize() * Impact.getInstance().getAltManager().slotHeight;
	}
	
	@Override
	protected int getSize() {
		return Impact.getInstance().getAltManager().getAltSize();
	}
	
	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4) {
		this.selected = var1;
	}
	
	@Override
	protected boolean isSelected(int var1) {
		return this.selected == var1;
	}
	
	protected int getSelected() {
		return this.selected;
	}
	
	@Override
	protected void drawBackground() {
		aList.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int selectedIndex, int x, int y, int var4, int var6, int var7, float partialTicks) {
		try {
			Alt theAlt = Impact.getInstance().getAltManager().getAlt(selectedIndex);
			aList.drawString(aList.getLocalFontRenderer(), theAlt.getUsername(), x, y + 1, 0xFFFFFF);
			if (theAlt.isPremium()) {
				aList.drawString(aList.getLocalFontRenderer(), Impact.getInstance().getAltManager().makePassChar(theAlt.getPassword()), x, y + 12, 0x808080);
			} else {
				aList.drawString(aList.getLocalFontRenderer(), "N/A", x, y + 12, 0x808080);
			}
		} catch (AccountManagementException error) {
			error.printStackTrace();
		}
	}
}
