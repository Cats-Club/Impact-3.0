package me.zero.clarinet.mod.movement;

import java.util.Arrays;
import java.util.List;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.TimerUtil;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ScaffoldWalk extends Mod {
	
	private BlockData blockData = null;
	private TimerUtil time = new TimerUtil();
	private List<Block> blacklist;
	
	public ScaffoldWalk() {
		super("ScaffoldWalk", "Places blocks under you", 0, Category.MOVEMENT);
		this.blacklist = Arrays.asList(Blocks.AIR, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA);
	}
	
	@Override
	public void onEnable() {
		this.blockData = null;
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
	}
	
	@EventTarget
	private void onMotionUpdate(EventMotionUpdate event) {
		if (mc.player.getHeldItem(EnumHand.MAIN_HAND) != null && mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
			boolean keyDown;
			if (mc.gameSettings.keyBindJump.getKeyCode() < 0) {
				keyDown = Mouse.isButtonDown(mc.gameSettings.keyBindJump.getKeyCode() + 100);
			} else {
				keyDown = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
			}
			if ((keyDown) && (mc.inGameHasFocus) && (mc.player.onGround)) {
				//mc.player.setEntityBoundingBox(mc.player.getEntityBoundingBox().offset(0.0D, 1.2D, 0.0D));
			}
		} else {
			int block = findBlocks(36, 45);
			if (block != -1) {
				mc.player.connection.sendPacket(new CPacketHeldItemChange(block - 36));
				mc.player.inventory.currentItem = (block - 36);
			}
		}
		this.blockData = null;
		if ((mc.player.getHeldItem(EnumHand.MAIN_HAND) != null) && (!mc.player.isSneaking()) && ((mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBlock))) {
			BlockPos blockBelow = new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ);
			if (mc.world.getBlockState(blockBelow).getBlock() == Blocks.AIR) {
				this.blockData = getBlockData(blockBelow);
				if (this.blockData != null) {
					float[] angles = RotationUtils.getRotations(blockData.position);
					event.yaw = angles[0];
					event.pitch = angles[1];
				}
			}
		}
		if (this.blockData == null) {
			return;
		}
		if (!this.time.delay(75L)) {
			return;
		}
		((IMinecraft) mc).setRightClickDelayTimer(0);
		if (EnumActionResult.SUCCESS == mc.playerController.processRightClickBlock(mc.player, mc.world, this.blockData.position, this.blockData.face, new Vec3d(this.blockData.position.getX(), this.blockData.position.getY(), this.blockData.position.getZ()), EnumHand.MAIN_HAND)) {
			mc.player.swingArm(EnumHand.MAIN_HAND);
		}
	}
	
	private int findBlocks(int startSlot, int endSlot) {
		for (int i = startSlot; i < endSlot; i++) {
			ItemStack stack = mc.player.inventoryContainer.getSlot(i).getStack();
			if ((stack != null) && ((stack.getItem() instanceof ItemBlock))) {
				return i;
			}
		}
		return -1;
	}
	
	public BlockData getBlockData(BlockPos pos) {
		if (!this.blacklist.contains(mc.world.getBlockState(pos.add(0, -1, 0)).getBlock())) {
			return new BlockData(pos.add(0, -1, 0), EnumFacing.UP);
		}
		if (!this.blacklist.contains(mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(-1, 0, 0), EnumFacing.EAST);
		}
		if (!this.blacklist.contains(mc.world.getBlockState(pos.add(1, 0, 0)).getBlock())) {
			return new BlockData(pos.add(1, 0, 0), EnumFacing.WEST);
		}
		if (!this.blacklist.contains(mc.world.getBlockState(pos.add(0, 0, -1)).getBlock())) {
			return new BlockData(pos.add(0, 0, -1), EnumFacing.SOUTH);
		}
		if (!this.blacklist.contains(mc.world.getBlockState(pos.add(0, 0, 1)).getBlock())) {
			return new BlockData(pos.add(0, 0, 1), EnumFacing.NORTH);
		}
		return null;
	}
	
	private class BlockData {
		
		public BlockPos position;
		public EnumFacing face;
		
		public BlockData(BlockPos position, EnumFacing face) {
			this.position = position;
			this.face = face;
		}
	}
}
