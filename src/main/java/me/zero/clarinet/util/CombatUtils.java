package me.zero.clarinet.util;

import java.util.ArrayList;

import me.zero.clarinet.util.entity.EntityFilter;
import me.zero.values.types.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class CombatUtils {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static ArrayList<EntityLivingBase> getCloseEntities(NumberValue range, int fov, EntityFilter filter) {
		return getCloseEntities(range.getValue().doubleValue(), fov, filter);
	}
	
	public static ArrayList<EntityLivingBase> getCloseEntities(double range, NumberValue fov, EntityFilter filter) {
		return getCloseEntities(range, fov.getValue().intValue(), filter);
	}
	
	public static ArrayList<EntityLivingBase> getCloseEntities(NumberValue range, NumberValue fov, EntityFilter filter) {
		return getCloseEntities(range.getValue().doubleValue(), fov.getValue().intValue(), filter);
	}
	
	public static ArrayList<EntityLivingBase> getCloseEntities(double range, int fov, EntityFilter filter) {
		ArrayList<EntityLivingBase> closeEntities = new ArrayList<EntityLivingBase>();
		for (Object o : Minecraft.getMinecraft().world.loadedEntityList) {
			if (isCorrectEntity(o, filter) && ((RotationUtils.angleDistanceTo((Entity) o) <= fov / 2) || (fov == 360)) && mc.player.getDistance((Entity) o) <= range) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!(o instanceof EntityPlayerSP) && !en.isDead && en.getHealth() > 0 && !en.getName().equals(mc.player.getName())) {
					closeEntities.add(en);
				}
			}
		}
		return closeEntities;
	}
	
	public static EntityLivingBase getClosestEntity(float fov, EntityFilter filter) {
		EntityLivingBase closestEntity = null;
		for (Object o : mc.world.loadedEntityList)
			if (isCorrectEntity(o, filter) && RotationUtils.angleDistanceTo((Entity) o) <= fov / 2) {
				EntityLivingBase en = (EntityLivingBase) o;
				if (!(o instanceof EntityPlayerSP) && !en.isDead && en.getHealth() > 0 && !en.getName().equals(mc.player.getName())) if (closestEntity == null || mc.player.getDistance(en) < mc.player.getDistance(closestEntity)) {
					closestEntity = en;
				}
			}
		return closestEntity;
	}
	
	public static boolean isCorrectEntity(Object o, EntityFilter filter) {
		if (!(o instanceof Entity)) {
			return false;
		}
		if (filter == null) {
			return true;
		} else {
			return filter.isValidEntity((Entity) o);
		}
	}
}
