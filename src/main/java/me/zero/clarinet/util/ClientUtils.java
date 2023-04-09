package me.zero.clarinet.util;

import me.zero.clarinet.event.player.EventMove;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;

public final class ClientUtils implements Helper {

	public static final String prefix = "§f[§9Impact§f]§7";
	
	public static void breakNCP() {
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX + mc.player.motionX, /*13370000001E9*/ mc.player.posY - 1337, mc.player.posZ + mc.player.motionZ, true));
	}

	public static void swap(int slot, int hotbar) {
        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, hotbar, ClickType.SWAP, mc.player);
    }
	
	public static boolean isUnderBlock() {
		if (mc.player == null) {
			return false;
		}
		for (int x = MathHelper.floor(mc.player.getEntityBoundingBox().minX); x < MathHelper.floor(mc.player.getEntityBoundingBox().maxX) + 1; x++) {
			for (int z = MathHelper.floor(mc.player.getEntityBoundingBox().minZ); z < MathHelper.floor(mc.player.getEntityBoundingBox().maxZ) + 1; z++) {
				Block block = mc.world.getBlockState(new BlockPos(x, mc.player.posY + 2, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir)) && (block.isCollidable())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor(mc.player.getEntityBoundingBox().minX); x < MathHelper.floor(mc.player.getEntityBoundingBox().maxX) + 1; x++) {
			for (int y = MathHelper.floor(mc.player.getEntityBoundingBox().minY); y < MathHelper.floor(mc.player.getEntityBoundingBox().maxY) + 1; y++) {
				for (int z = MathHelper.floor(mc.player.getEntityBoundingBox().minZ); z < MathHelper.floor(mc.player.getEntityBoundingBox().maxZ) + 1; z++) {
					Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
					if (block != null && !(block instanceof BlockAir)) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.world.getBlockState(new BlockPos(x, y, z)), mc.world, new BlockPos(x, y, z));
						if (block instanceof BlockHopper) {
							boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
						}
						if (boundingBox != null) {
							if (mc.player.getEntityBoundingBox().intersects(boundingBox)) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isUnderBlock(Vec3d offset) {
		int y = (int) Math.round(mc.player.posY + 2 + offset.y);
		for (int x = MathHelper.floor(mc.player.getEntityBoundingBox().minX + offset.x); x < MathHelper.floor(mc.player.getEntityBoundingBox().maxX + offset.x) + 1; x++) {
			for (int z = MathHelper.floor(mc.player.getEntityBoundingBox().minZ + offset.z); z < MathHelper.floor(mc.player.getEntityBoundingBox().maxZ + offset.z) + 1; z++) {
				Block block = Minecraft.getMinecraft().world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if ((block != null) && (!(block instanceof BlockAir))) {
					if (block.isCollidable()) return true;
				}
			}
		}
		return false;
	}
	
	public static void blinkToPos(double[] startPos, BlockPos endPos, double slack) {
		double curX = startPos[0];
		double curY = startPos[1];
		double curZ = startPos[2];
		double endX = (double) endPos.getX() + 0.5;
		double endY = (double) endPos.getY() + 1.0;
		double endZ = (double) endPos.getZ() + 0.5;
		double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
		int count = 0;
		while (distance > slack) {
			double offset;
			distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
			if (count > 120) break;
			boolean next = false;
			double diffX = curX - endX;
			double diffY = curY - endY;
			double diffZ = curZ - endZ;
			double d2 = offset = (count & 1) == 0 ? 0.4 : 0.1;
			if (diffX < 0.0) {
				curX = Math.abs(diffX) > offset ? (curX += offset) : (curX += Math.abs(diffX));
			}
			if (diffX > 0.0) {
				curX = Math.abs(diffX) > offset ? (curX -= offset) : (curX -= Math.abs(diffX));
			}
			if (diffY < 0.0) {
				curY = Math.abs(diffY) > 0.25 ? (curY += 0.25) : (curY += Math.abs(diffY));
			}
			if (diffY > 0.0) {
				curY = Math.abs(diffY) > 0.25 ? (curY -= 0.25) : (curY -= Math.abs(diffY));
			}
			if (diffZ < 0.0) {
				curZ = Math.abs(diffZ) > offset ? (curZ += offset) : (curZ += Math.abs(diffZ));
			}
			if (diffZ > 0.0) {
				curZ = Math.abs(diffZ) > offset ? (curZ -= offset) : (curZ -= Math.abs(diffZ));
			}
            mc.player.connection.sendPacket(new CPacketPlayer.Position(curX, curY, curZ, true));
			++count;
		}
	}
	
	public static double getDistanceToGround() {
		double h = 1.0D;
		AxisAlignedBB box = mc.player.getEntityBoundingBox().expand(0.0625D, 0.0625D, 0.0625D);
		double distance = 0.0D;
		for (distance = 0.0D; distance < mc.player.posY; distance += h) {
			AxisAlignedBB nextBox = box.offset(0.0D, -distance, 0.0D);
			if (mc.world.checkBlockCollision(nextBox)) {
				if (h < 0.0625D) {
					break;
				}
				distance -= h;
				h /= 2.0D;
			}
		}
		return Math.max(distance, 0);
	}
	
	public static double getBaseMoveSpeed() {
		double baseSpeed = 0.2873D;
		if (mc.player.isPotionActive(Potion.getPotionById(1))) {
			int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
			baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
		}
		return baseSpeed;
	}
	
	public static double getBaseMoveSpeedNoBoost() {
		return 0.2873D;
	}
	
	public static int getLatency(EntityPlayer player) {
		if (player != null) {
			NetworkPlayerInfo npi = mc.getConnection().getPlayerInfo(player.getUniqueID());
			if (npi != null) {
				int latency = npi.getResponseTime();
				latency = MathUtils.clampInteger(latency, -1, 999);
				return latency;
			}
		}
		return -1;
	}
	
	public static void setMoveSpeed(double speed) {
		double forward = mc.player.moveForward;
		double strafe = mc.player.moveStrafing;
		float yaw = mc.player.rotationYaw;
		if (forward == 0.0D && strafe == 0.0D) {
            mc.player.motionX = (0.0D);
            mc.player.motionZ = (0.0D);
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}
            mc.player.motionX = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            mc.player.motionZ = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
		}
	}
	
	public static void setMoveSpeed(EventMove event, double speed) {
		double forward = mc.player.moveForward;
		double strafe = mc.player.moveStrafing;
		float yaw = mc.player.rotationYaw;
		if (forward == 0.0D && strafe == 0.0D) {
			event.x = (0.0D);
			event.z = (0.0D);
		} else {
			if (forward != 0.0D) {
				if (strafe > 0.0D) {
					yaw += (forward > 0.0D ? -45 : 45);
				} else if (strafe < 0.0D) {
					yaw += (forward > 0.0D ? 45 : -45);
				}
				strafe = 0.0D;
				if (forward > 0.0D) {
					forward = 1.0D;
				} else if (forward < 0.0D) {
					forward = -1.0D;
				}
			}
			event.x = (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
			event.z = (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
		}
	}
	
	public static void message(String message) {
		if (mc.player != null) {
            mc.ingameGUI.getChatGUI().printChatMessage(new TextComponentString(prefix + " " + message));
		}
	}
	
	public static void error(String message) {
		message("§c" + message);
	}

	public static void log(String log) {
		System.out.println("[Impact] " + log);
	}
	
	public static ServerData getCurrentServer() {
		return mc.getCurrentServerData();
	}
}
