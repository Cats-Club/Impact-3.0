package me.zero.clarinet.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public final class BlockUtils implements Helper {

	public static float getBlockDistance(float xDiff, float yDiff, float zDiff) {
		return MathHelper.sqrt((xDiff - 0.5F) * (xDiff - 0.5F) + (yDiff - 0.5F) * (yDiff - 0.5F) + (zDiff - 0.5F) * (zDiff - 0.5F));
	}
	
	public static IBlockState getBlockState(BlockPos pos) {
		return mc.world.getBlockState(pos);
	}
	
	public static Block getBlock(int x, int y, int z) {
		BlockPos bp = new BlockPos(x, y, z);
		return getBlock(bp);
	}
	
	public static Block getBlock(double x, double y, double z) {
		BlockPos bp = new BlockPos(x, y, z);
		return getBlock(bp);
	}
	
	public static Block getBlock(BlockPos bp) {
		return mc.world == null ? null : mc.world.getBlockState(bp).getBlock();
	}
	
	public static Block getBlock(double offset) {
		return getBlock(mc.player.getEntityBoundingBox().offset(0.0D, offset, 0.0D));
	}
	
	public static Block getBlock(AxisAlignedBB bb) {
		int y = (int) bb.minY;
		for (int x = MathHelper.floor(bb.minX); x < MathHelper.floor(bb.maxX) + 1; x++) {
			for (int z = MathHelper.floor(bb.minZ); z < MathHelper.floor(bb.maxZ) + 1; z++) {
				Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null) {
					return block;
				}
			}
		}
		return null;
	}
	
	public static Block getBlockAtPosC(EntityPlayer inPlayer, double x, double y, double z) {
		return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
	}
	
	public static boolean isOnLiquid() {
		boolean onLiquid = false;
		if (getBlockAtPosC(mc.player, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial(null).isLiquid() && getBlockAtPosC(mc.player, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial(null).isLiquid()) {
			onLiquid = true;
		}
		return onLiquid;
	}
	
	public static boolean isInsideBlock() {
		for (int x = MathHelper.floor(mc.player.getEntityBoundingBox().minX); x < MathHelper.floor(mc.player.getEntityBoundingBox().maxX) + 1; x++) {
			for (int y = MathHelper.floor(mc.player.getEntityBoundingBox().minY); y < MathHelper.floor(mc.player.getEntityBoundingBox().maxY) + 1; y++) {
				for (int z = MathHelper.floor(mc.player.getEntityBoundingBox().minZ); z < MathHelper.floor(mc.player.getEntityBoundingBox().maxZ) + 1; z++) {
					Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
					if ((block != null) && (!(block instanceof BlockAir))) {
						AxisAlignedBB boundingBox = block.getCollisionBoundingBox(mc.world.getBlockState(new BlockPos(x, y, z)), mc.world, new BlockPos(x, y, z));
						if ((block instanceof BlockHopper)) {
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
	
	public static boolean isOnLiquid(AxisAlignedBB boundingBox) {
		boundingBox = boundingBox.expand(-0.01, 0.0, -0.01);
		boundingBox = boundingBox.offset(0.0, -0.01, 0.0);
		boolean onLiquid = false;
		int y2 = (int) boundingBox.minY;
		int x2 = MathHelper.floor(boundingBox.minX);
		while (x2 < MathHelper.floor(boundingBox.maxX + 1.0)) {
			int z2 = MathHelper.floor(boundingBox.minZ);
			while (z2 < MathHelper.floor(boundingBox.maxZ + 1.0)) {
				Block block = getBlock(new BlockPos(x2, y2, z2));
				if (block != Blocks.AIR) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}
					onLiquid = true;
				}
				++z2;
			}
			++x2;
		}
		return onLiquid;
	}
	
	public static Block getBlockUnderPlayer2(EntityPlayer inPlayer, double height) {
		return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
	}
	
	public static Block getBlockUnderEntity(EntityLivingBase inPlayer, double height) {
		return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
	}
	
	public static String liquidCollision() {
		String colission = "";
		if (getBlockAtPosC(mc.player, 0.3100000023841858D, 0.0D, 0.3100000023841858D).getMaterial(null).isLiquid()) {
			colission = "Positive";
		}
		if (getBlockAtPosC(mc.player, -0.3100000023841858D, 0.0D, -0.3100000023841858D).getMaterial(null).isLiquid()) {
			colission = "Negative";
		}
		return colission;
	}
}
