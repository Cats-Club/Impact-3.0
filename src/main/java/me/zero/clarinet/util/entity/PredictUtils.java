package me.zero.clarinet.util.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class PredictUtils {
	
	private static Vec3d lerp(Vec3d pos, Vec3d prev, float time) {
		double x = pos.x + ((pos.x - prev.x) * time);
		double y = pos.y + ((pos.y - prev.y) * time);
		double z = pos.z + ((pos.z - prev.z) * time);
		return new Vec3d(x, y, z);
	}
	
	public static Vec3d predictPos(Entity entity, float time) {
		return lerp(new Vec3d(entity.posX, entity.posY, entity.posZ), new Vec3d(entity.prevPosX, entity.prevPosY, entity.prevPosZ), time);
	}
}