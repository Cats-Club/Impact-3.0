package me.zero.clarinet.alt;

import java.io.IOException;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.util.MojangUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.util.Session;

public class GuiAltList extends GuiScreen implements GuiYesNoCallback {
	
	private GuiScreen parentScreen;
	public static String dispErrorString = "";
	public boolean deleteMenuOpen = false;
	public static Minecraft mc = Minecraft.getMinecraft();
	public FontRenderer fontRenderer = mc.fontRenderer;
	
	public GuiAltList(GuiScreen screen) {
		this.parentScreen = screen;
	}
	
	public void onGuiClosed() {
		Impact.getInstance().getAltManager().save();
		super.onGuiClosed();
	}
	
	private SlotAlt tSlot;
	
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(1, width / 2 + 51, height - 26, 66, 20, "Add"));
		
		GuiButton random = new GuiButton(6, width / 2 + 51, height - 48, 66, 20, "Random");
		random.enabled = false;
		buttonList.add(random);
		
		buttonList.add(new GuiButton(3, width / 2 - 33 - 70, height - 26, 66, 20, "Remove"));
		buttonList.add(new GuiButton(4, width / 2 - 33 - 70, height - 48, 66, 20, "Back"));
		buttonList.add(new GuiButton(5, width / 2 - 33, height - 48, 80, 20, "Direct Login"));
		buttonList.add(new GuiButton(2, width / 2 - 33, height - 26, 80, 20, "Login"));
		
		tSlot = new SlotAlt(this.mc, this);
		// tSlot.registerScrollButtons(buttonList, 7, 8);
	}
	
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		this.tSlot.handleMouseInput();
	}

	@Override
	public void confirmClicked(boolean flag, int id) {
		super.confirmClicked(flag, id);
		if (deleteMenuOpen) {
			deleteMenuOpen = false;
			if (flag) {
				Impact.getInstance().getAltManager().removeAlt(id);
				Impact.getInstance().getAltManager().save();
			}
			mc.displayGuiScreen(this);
		}
	}
	
	public void actionPerformed(GuiButton button) {
		try {
			super.actionPerformed(button);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (button.id == 1) {
			GuiAltAdd gaa = new GuiAltAdd(this);
			mc.displayGuiScreen(gaa);
		}
		if (button.id == 2) {
			try {
				Alt a1 = Impact.getInstance().getAltManager().getAlt(tSlot.getSelected());
				if (a1.isPremium()) {
					try {
                        GuiAltList.dispErrorString = "Email: \2477" + a1.getUsername();
						MojangUtils.login(a1.getUsername(), a1.getPassword());
					} catch (Exception error) {
						dispErrorString = "".concat("\247cBad Login \2477(").concat(a1.getUsername()).concat(")");
					}
				} else {
					((IMinecraft) this.mc).setSession(new Session(a1.getUsername(), "-", "0", Session.Type.LEGACY.toString()));
					dispErrorString = "";
				}
			} catch (Exception e) {
			}
		}
		if (button.id == 3) {
			try {
				String s1 = "Are you sure you want this account to be deleted? " + "\"" + Impact.getInstance().getAltManager().getAlt(tSlot.getSelected()).getUsername() + "\"" + "?";
				String s3 = "Delete";
				String s4 = "Cancel";
				GuiYesNo guiyesno = new GuiYesNo(this, s1, "", s3, s4, tSlot.getSelected());
				deleteMenuOpen = true;
				mc.displayGuiScreen(guiyesno);
			} catch (Exception e) {
			}
		}
		if (button.id == 4) {
			mc.displayGuiScreen(parentScreen);
		}
		if (button.id == 5) {
			GuiDirectLogin gdl = new GuiDirectLogin(this);
			mc.displayGuiScreen(gdl);
		}
	}
	
	public void updateScreen() {
		super.updateScreen();
	}
	
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		tSlot.drawScreen(mouseX, mouseY, partialTicks);
		String d1 = "Alts: \2477" + Impact.getInstance().getAltManager().getAltSize();
		fontRenderer.drawStringWithShadow(d1, (this.width / 2) - (fontRenderer.getStringWidth(d1) / 2), 3, 0xFFFFFF);
		fontRenderer.drawStringWithShadow("Username: \2477" + mc.getSession().getUsername(), 3, 3, 0xFFFFFF);
		fontRenderer.drawStringWithShadow(dispErrorString, 3, 13, 0xFFFFFF);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	public FontRenderer getLocalFontRenderer() {
		return this.fontRenderer;
	}
}
