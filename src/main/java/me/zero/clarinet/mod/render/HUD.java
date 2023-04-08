package me.zero.clarinet.mod.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zero.clarinet.event.api.types.Priority;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.user.UserManager;
import me.zero.clarinet.util.TPSWatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventRender2D;
import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.ui.font.GlobalFontRenderer;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.misc.StringUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;

public class HUD extends Mod {

	private HashMap<Mod, Float> extendedAmount = new HashMap<>();
	
	private int y;
	
	private MultiValue<String> mode = new MultiValue<>(this, "Mode", "mode", "Flare", new String[] { "Flare", "Direkt" });

	private BooleanValue watermark = new BooleanValue(this, "WaterMark", "watermark", true);
	public BooleanValue tabgui = new BooleanValue(this, "Tab Gui", "tabgui", true);
	private BooleanValue arraylist = new BooleanValue(this, "ArrayList", "arraylist", true);
	private BooleanValue effects = new BooleanValue(this, "Potion Effects", "effects", true);
	private BooleanValue coords = new BooleanValue(this, "Coordinates", "coords");
    private BooleanValue nether = new BooleanValue(this, "Nether Coords", "nether");
	private BooleanValue info = new BooleanValue(this, "Info", "info");
	private BooleanValue entities = new BooleanValue(this, "Entity Info", "entities");

    private FontRenderer font = new GlobalFontRenderer(FontManager.urwgothic_hud);

	public HUD() {
		super("Hud", "Displays an ingame overlay, showing information", Keyboard.KEY_NONE, Category.RENDER, false);
		this.toggle();
	}
	
	@EventTarget(Priority.LOWEST)
	public void onRender2D(EventRender2D event) {
		
		ScaledResolution sr = new ScaledResolution(mc);

		if (!mc.gameSettings.showDebugInfo) {
			float speedRatio = (144.0F / Minecraft.getDebugFPS());
			float restore = -3F;
			int y = 2;

			// Doogie13 - Optifine stuff
			// if (mc.gameSettings.ofShowFps) {
			// 	y += font.FONT_HEIGHT + 1;
			// }

			if (watermark.getValue()) {
				String s1 = TextFormatting.BLUE + Impact.getInstance().getName();
				String s2 = String.valueOf(Impact.getInstance().getBuild());
                String s3 = "Pepsi Edition";

                int watermark = 2;

				font.drawStringWithShadow(s1, watermark, y, 0xFFFFFFFF);
                watermark += font.getStringWidth(s1) + 4;

				font.drawStringWithShadow(s2, watermark, y, 0xFFFFFFFF);
                watermark += font.getStringWidth(s2) + 4;

                if (UserManager.pepsi.isUser(((IMinecraft) mc).getSession().getPlayerID())) {
                    font.drawStringWithShadow(s3, watermark, y, 0xFFFFFFFF);
                }

				y += font.FONT_HEIGHT + 3;
			}

			if (tabgui.getValue()) {
				Impact.getInstance().getTabGui().drawGui(font, 2, y - 1);
				y += Impact.getInstance().getTabGui().getHeight() + 2;
			}

			TextFormatting c1 = TextFormatting.BLUE;
			TextFormatting c2 = TextFormatting.WHITE;

			if (info.getValue()) {
				font.drawStringWithShadow(c1 + "FPS: " + c2 + Minecraft.getDebugFPS(), 2, y, 0xFFFFFFFF);
				y += font.FONT_HEIGHT + 1;
				if (mc.player != null) {
					font.drawStringWithShadow(c1 + "Ping: " + c2 + ClientUtils.getLatency(mc.player), 2, y, 0xFFFFFFFF);
					y += font.FONT_HEIGHT + 1;
				}
                font.drawStringWithShadow(c1 + "TPS: " + c2 + MathUtils.roundToPlace(TPSWatcher.INSTANCE.getTPS(), 2), 2, y, 0xFFFFFFFF);
                y += font.FONT_HEIGHT + 1;
			}

			if (entities.getValue()) {
				Map<String, Integer> entities = new HashMap<>();
				for (Entity e : mc.world.loadedEntityList) {
					if (!(e instanceof EntityPlayerSP)) {
						String name = e.getName();
						if (e instanceof EntityItem) {
							name = "Item";
						}
						if (e instanceof EntityPlayer) {
							name = "Player";
						}
						entities.put(name, (entities.get(name) != null ? entities.get(name) + 1 : 1));
					}
				}
				for (String ent : entities.keySet()) {
					font.drawStringWithShadow(c1 + "(" + c2 + entities.get(ent) + c1 + ") " + c2 + ent, 2, y, 0xFFFFFFFF);
					y += font.FONT_HEIGHT + 1;
				}
			}

			this.y = y;
			int cornerY = sr.getScaledHeight() - font.FONT_HEIGHT - 4;
			cornerY -= (mc.currentScreen instanceof GuiChat ? 14 : 0);

			if (effects.getValue()) {
				for (PotionEffect effect : mc.player.getActivePotionEffects()) {
					int color = effect.getPotion().getLiquidColor();
					String name = getPotionString(effect);
					font.drawStringWithShadow(name, sr.getScaledWidth() - font.getStringWidth(name) - 1, cornerY, color);
					cornerY -= font.FONT_HEIGHT + 1;
				}
			}

			if (coords.getValue()) {
                double xCoord = MathUtils.roundToPlace(mc.player.posX, 1);
                double yCoord = MathUtils.roundToPlace(mc.player.posY, 1);
                double zCoord = MathUtils.roundToPlace(mc.player.posZ, 1);

                if (nether.getValue()) {
                    if (mc.world.getBiome(new BlockPos(mc.player)).getBiomeName().equalsIgnoreCase("Hell")) {
                        xCoord *= 8;
                        zCoord *= 8;
                    }
                }

				String s1 = c1 + "Z: " + c2 + zCoord;
				font.drawStringWithShadow(s1, sr.getScaledWidth() - font.getStringWidth(s1) - 4, cornerY, 0xFFFFFFFF);
				cornerY -= font.FONT_HEIGHT + 1;

				String s2 = c1 + "Y: " + c2 + yCoord;
				font.drawStringWithShadow(s2, sr.getScaledWidth() - font.getStringWidth(s2) - 4, cornerY, 0xFFFFFFFF);
				cornerY -= font.FONT_HEIGHT + 1;

				String s3 = c1 + "X: " + c2 + xCoord;
				font.drawStringWithShadow(s3, sr.getScaledWidth() - font.getStringWidth(s3) - 4, cornerY, 0xFFFFFFFF);
			}

			if (!arraylist.getValue()) {
				return;
			}
			List<Mod> mods = new ArrayList<>();
            Impact.getInstance().getModManager().getMods().stream().filter(Mod::doesDisplay).forEach(mod -> {
                extendedAmount.putIfAbsent(mod, restore);
                if (mod.isToggled() || extendedAmount.get(mod) > restore) {
                    mods.add(mod);
                }
            });
			mods.sort((mod1, mod2) -> {
				double size1 = font.getStringWidth(mod1.getDisplayName());
				double size2 = font.getStringWidth(mod2.getDisplayName());
				return size1 < size2 ? 1 : size1 == size2 ? 0 : -1;
			});
			Collection<PotionEffect> collection = mc.player.getActivePotionEffects();
			int offsetY = !collection.isEmpty() ? 28 : 2;
			for (PotionEffect pe : collection) {
				if (pe.getPotion().isBadEffect()) {
					offsetY += 26;
					break;
				}
			}
            boolean ysmooth = mode.getValue().contains("Flare");
			for (Mod module : mods) {
				float openingTarget = font.getStringWidth(module.getDisplayName());
				float target = module.isToggled() ? openingTarget : restore;
				float newAmount = extendedAmount.get(module);

                newAmount += 1.5 * speedRatio * (module.isToggled() ? 1 : -1);

				newAmount = module.isToggled() ? Math.min(target, newAmount) : Math.max(target, newAmount);

				if (!module.isToggled() && newAmount < 0) {
					newAmount = restore;
				}
				if (module.isToggled() && target - newAmount < 1) {
					newAmount = target;
				}

				float percent = newAmount / openingTarget;
				extendedAmount.put(module, newAmount);

				font.drawStringWithShadow(module.getDisplayName(), sr.getScaledWidth() - extendedAmount.get(module) - 4, offsetY, module.getColor());
				if (ysmooth) {
					offsetY += (font.FONT_HEIGHT + 1) * percent;
				} else {
					offsetY += font.FONT_HEIGHT + 1;
				}
			}
		}
	}
	
	public int getY() {
		return y;
	}
	
	private String getPotionString(PotionEffect effect) {
		Potion potion = effect.getPotion();
		String name = I18n.format(potion.getName());
		name += " " + StringUtils.toRoman(effect.getAmplifier() + 1);
		name += " §7(§f" + Potion.getPotionDurationString(effect, 1.0F) + "§7)";
		return name;
	}
}
