package me.zero.clarinet.mod.minigame;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.EnumHand;

public class QuakeCraft extends Mod {
	
	private ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
	
	private EntityLivingBase currentTarget;
	
	public QuakeCraft() {
		super("QuakeCraft", "Aura for QuakeCraft", Keyboard.KEY_NONE, Category.MINIGAME);
	}
	
	@EventTarget
	public void onPreUpdate(EventMotionUpdate event) {
		if (event.type != EventType.PRE) {
			return;
		}
		if (mc.player.getHeldItemMainhand() != null) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
				entities = CombatUtils.getCloseEntities(250, 360, null);
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = entities.get(i);
					if (!(entity instanceof EntityPlayer) || !mc.player.canEntityBeSeen(entity)) {
						entities.remove(entity);
					}
				}
				entities.sort((target1, target2) -> {
					double distance1 = target1.getDistance(mc.player);
					double distance2 = target2.getDistance(mc.player);
					return distance1 < distance2 ? -1 : distance1 == distance2 ? 0 : 1;
				});
				this.suffix = String.valueOf(entities.size());
				if (entities.size() > 0) {
					currentTarget = entities.get(0);
					float[] angles = RotationUtils.getRotations(currentTarget);
					event.yaw = angles[0];
					event.pitch = angles[1];
				}
			}
		}
	}
	
	@EventTarget
	public void onPostUpdate(EventMotionUpdate event) {
		if (event.type != EventType.POST) {
			return;
		}
		if (mc.player.getHeldItemMainhand() != null) {
			if (mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
				if (entities.size() > 0 && currentTarget != null) {
					mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
				}
			}
		}
	}
}
