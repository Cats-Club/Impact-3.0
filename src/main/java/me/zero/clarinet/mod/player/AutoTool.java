package me.zero.clarinet.mod.player;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventLeftClick;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class AutoTool extends Mod {
	
	private boolean state;
	private int oldSlot;
	
	public AutoTool() {
		super("AutoTool", "Find the best tool for the job!", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!mc.gameSettings.keyBindAttack.isKeyDown() && state) {
			state = false;
			mc.player.inventory.currentItem = oldSlot;
		} else if (state && mc.objectMouseOver != null && mc.objectMouseOver.getBlockPos() != null && mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock().getMaterial(mc.world.getBlockState(mc.objectMouseOver.getBlockPos())) != Material.AIR) {
			setSlot(mc.objectMouseOver.getBlockPos());
		}
	}
	
	@EventTarget
	public void LeftClick(EventLeftClick event) {
		if (mc.objectMouseOver == null || mc.objectMouseOver.getBlockPos() == null) {
			return;
		}
		IBlockState blockState = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
		if (blockState.getBlock().getMaterial(blockState) != Material.AIR) {
			state = true;
			oldSlot = mc.player.inventory.currentItem;
			setSlot(mc.objectMouseOver.getBlockPos());
		}
	}
	
	public static void setSlot(BlockPos blockPos) {
		float bestSpeed = 1F;
		int bestSlot = -1;
		IBlockState state = mc.world.getBlockState(blockPos);
		Block block = state.getBlock();
		for (int i = 0; i < 9; i++) {
			ItemStack item = mc.player.inventory.getStackInSlot(i);
			if (item == null) {
				continue;
			}
			float speed = item.getDestroySpeed(state);
			if (speed > bestSpeed) {
				bestSpeed = speed;
				bestSlot = i;
			}
		}
		if (bestSlot != -1) {
			mc.player.inventory.currentItem = bestSlot;
		}
	}
}
