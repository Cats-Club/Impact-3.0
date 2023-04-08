package me.zero.clarinet.util;

import java.util.ArrayList;

import me.zero.clarinet.util.entity.EntityFilter;
import me.zero.values.types.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class RotationUtils {
	
	private static final Minecraft mc = Minecraft.getMinecraft();
	
	public static float[] getRotations(double x, double y, double z) {
		Vec3d vec = new Vec3d(x, y, z);
		return getRotations(vec);
	}
	
	public static float[] getRotations(BlockPos pos) {
		Vec3d vec = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
		return getRotations(vec);
	}
	
	public static float[] getRotations(Entity entity) {
		Vec3d vec = new Vec3d(entity.posX, entity.posY + entity.height / 2, entity.posZ);
		return getRotations(vec);
	}
	
	public static float[] getRotations(Vec3d pos) {
		double diffX = pos.x - mc.player.posX;
		double diffZ = pos.z - mc.player.posZ;
		double diffY = (pos.y - 1) - mc.player.posY;
		double hdistance = (double) MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) (-(Math.atan2(diffY, hdistance) * 180.0D / Math.PI));
		return new float[] {
                mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw),
                mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)
		};
	}
	
	public static int angleDistanceTo(Entity entity) {
		float[] neededRotations = getRotations(entity);
		if (neededRotations != null) {
			float neededYaw = mc.player.rotationYaw - neededRotations[0];
			float neededPitch = mc.player.rotationPitch - neededRotations[1];
			float distanceFromMouse = MathHelper.sqrt(neededYaw * neededYaw + neededPitch * neededPitch);
			return (int) distanceFromMouse;
		}
		return -1;
	}

    public static int angleDistanceYawNoAbs(Entity entity) {
        float[] neededRotations = getRotations(entity);
        if (neededRotations != null) {
            float neededYaw = MathHelper.wrapDegrees(neededRotations[0]) - MathHelper.wrapDegrees(mc.player.rotationYaw);
            return (int) neededYaw;
        }
        return -1;
    }
	
	public final static float limitAngleChange(final float current, final float intended, final float maxChange) {
		float change = intended - current;
		if (change > maxChange) change = maxChange;
		else if (change < -maxChange) change = -maxChange;
		return current + change;
	}
	
	public static boolean isVisibleFOV(Entity e, Entity e2, float fov) {
		return (Math.abs(getRotations(e)[0] - e2.rotationYaw) % 360.0F > 180.0F ? 360.0F - Math.abs(getRotations(e)[0] - e2.rotationYaw) % 360.0F : Math.abs(getRotations(e)[0] - e2.rotationYaw) % 360.0F) <= fov;
	}
}
