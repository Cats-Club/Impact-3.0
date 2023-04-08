package me.zero.clarinet.util.entity;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.Impact;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public abstract class EntityFilter {
	
	public final boolean isValidEntity(Entity e) {
		if (e instanceof EntityLivingBase && !(e instanceof EntityPlayerSP)) {
			EntityLivingBase ent = (EntityLivingBase) e;
			if (!ent.isInvisible() || this.invisibles()) {
				if (!ent.isPlayerSleeping() || this.sleeping()) {
					if (Minecraft.getMinecraft().player.canEntityBeSeen(ent) || this.walls()) {
						if (!onSameTeam(Minecraft.getMinecraft().player, ent) || this.teammates()) {
							if (customCheck(ent)) {
								if (!isFriend(ent) || friends()) {
									if (isPlayer(e)) {
										if (players()) {
											return true;
										}
									}
									if (isAnimal(e)) {
										if (animals()) {
											return true;
										}
									}
									if (isHostile(e)) {
										if (hostiles()) {
											return true;
										}
									}
									if (isPassive(e)) {
										if (passives()) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public final List<Entity> filter(List<Entity> list) {
		List<Entity> filtered = new ArrayList<>();
		for (Entity e : list) {
			if (isValidEntity(e)) {
				filtered.add(e);
			}
		}
		return filtered;
	}
	
	public boolean customCheck(Entity ent) {
		return true;
	}
	
	public abstract boolean walls();
	
	public abstract boolean sleeping();
	
	public abstract boolean invisibles();
	
	public abstract boolean teammates();
	
	public abstract boolean friends();
	
	public abstract boolean players();
	
	public abstract boolean animals();
	
	public abstract boolean hostiles();
	
	public abstract boolean passives();
	
	public static boolean isPlayer(Entity e) {
		boolean c1 = e instanceof EntityPlayer;
		return c1;
	}
	
	public static boolean isAnimal(Entity e) {
		boolean c1 = e instanceof EntityAnimal;
		boolean c2 = e instanceof EntitySquid;
		boolean c3 = e instanceof EntityBat;
		return c1 || c2 || c3;
	}
	
	public static boolean isHostile(Entity e) {
		boolean c1 = e instanceof EntityMob;
		boolean c2 = e instanceof EntitySlime;
		return c1 || c2;
	}
	
	public static boolean isPassive(Entity e) {
		boolean c1 = e instanceof EntityVillager;
		return c1;
	}
	
	public static boolean onSameTeam(EntityLivingBase e1, EntityLivingBase e2) {
		if (!(e1 instanceof EntityPlayer) || !(e2 instanceof EntityPlayer)) {
			return false;
		}
		return e1.isOnSameTeam(e2);
	}
	
	public static boolean isFriend(EntityLivingBase e) {
		if (!(e instanceof EntityPlayer)) {
			return false;
		}
		return Impact.getInstance().getFriendManager().isFriend(e.getName());
	}
}
