package me.zero.clarinet.mod.minigame;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.MultiValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSword;

public class Murder extends Mod {
	
	private EntityPlayer murderer;
	
	private MultiValue<String> mode = new MultiValue<String>(this, "Mode", "mode", "PlayMCM", new String[] { "PlayMCM", "MCPZ" });
	
	public Murder() {
		super("Murder", "Murder Mod", Keyboard.KEY_NONE, Category.MINIGAME);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = mode.getValue();
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer && !(e instanceof EntityPlayerSP)) {
				EntityPlayer player = (EntityPlayer) e;
				if (player.isInvisible()) {
					continue;
				}
				if (mode.getValue().equalsIgnoreCase("PlayMCM")) {
					if (murderer == null || !mc.world.loadedEntityList.contains(murderer)) {
						if (player.getHeldItemMainhand() != null && isKnife(player.getHeldItemMainhand().getItem())) {
							murderer = player;
						}
					} else {
						if (murderer.getDisplayName().getFormattedText().startsWith("§8")) {
							murderer = null;
						}
					}
				} else if (mode.getValue().equalsIgnoreCase("MCPZ")) {
					if (player.getHeldItemMainhand() != null && isKnife(player.getHeldItemMainhand().getItem())) {
						murderer = player;
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer && !(e instanceof EntityPlayerSP)) {
				EntityPlayer player = (EntityPlayer) e;
				if (player.isInvisible()) {
					continue;
				}
				if (player.isPlayerSleeping()) {
					continue;
				}
				float[] color = new float[3];
				color[0] = 1.0F;
				color[1] = 1.0F;
				color[2] = 1.0F;
				if (murderer != null && player.getName().equalsIgnoreCase(murderer.getName())) {
					color[0] = 0.8F;
					color[1] = 0.0F;
					color[2] = 0.0F;
				}
				RenderUtils.drawEntityESP(player, color[0], color[1], color[2], event.partialTicks);
			} else if (e instanceof EntityItem) {
				EntityItem ie = (EntityItem) e;
				if (ie.getItem() == null || ie.getItem().getItem() == null) {
					continue;
				}
				float[] color = getItemColor(ie.getItem().getItem());
				if (color[3] != 0.0F) {
					double xPos = (ie.lastTickPosX + (ie.posX - ie.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
					double yPos = (ie.lastTickPosY + (ie.posY - ie.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
					double zPos = (ie.lastTickPosZ + (ie.posZ - ie.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
					RenderUtils.renderEntityItem(ie, color[0], color[1], color[2], xPos, yPos, zPos, true);
				}
			}
		}
	}
	
	private boolean isKnife(Item item) {
		if (mode.getValue().equalsIgnoreCase("PlayMCM")) {
			return item instanceof ItemSword || Item.getIdFromItem(item) == 381;
		} else if (mode.getValue().equalsIgnoreCase("MCPZ")) {
			return item instanceof ItemSword;
		}
		return false;
	}
	
	private float[] getItemColor(Item item) {
		float[] evidence = { 0F, 1F, 0F, 1F };
		;
		float[] gun = { 0F, 0F, 1F, 1F };
		float[] none = { 0F, 0F, 0F, 0F };
		if (mode.getValue().equalsIgnoreCase("PlayMCM")) {
			if (Item.getIdFromItem(item) == 265) {
				return evidence;
			} else if (Item.getIdFromItem(item) == 290) {
				return gun;
			}
		} else if (mode.getValue().equalsIgnoreCase("MCPZ")) {
			if (Item.getIdFromItem(item) == 388) {
				return evidence;
			} else if (item instanceof ItemHoe) {
				return gun;
			}
		}
		return none;
	}
}
