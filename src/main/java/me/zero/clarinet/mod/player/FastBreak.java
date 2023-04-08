package me.zero.clarinet.mod.player;

import me.zero.clarinet.mixin.mixins.minecraft.client.multiplayer.IPlayerControllerMP;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.BlockUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class FastBreak extends Mod {
	
	private boolean isMining = false;
	
	public FastBreak() {
		super("FastBreak", "Break blocks faster", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (mc.player.capabilities.isCreativeMode) {
			this.suffix = "Creative";
			return;
		} else {
			if (mc.player.isSpectator()) {
				this.suffix = "None";
			} else {
				this.suffix = "Survival";
			}
		}
		ItemStack heldItem = mc.player.getHeldItem(EnumHand.MAIN_HAND);
		BlockPos var1 = null;
		isMining = false;
		float mineSpeed = 0.0f;
		if (mc.objectMouseOver != null) {
			if (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && mc.objectMouseOver.getBlockPos() != null) {
				var1 = mc.objectMouseOver.getBlockPos();
			}
		}
		if (mc.playerController.getIsHittingBlock()) {
			isMining = true;
		}
		Float getBlock = Float.valueOf(BlockUtils.getBlock(var1).getBlockHardness(BlockUtils.getBlockState(var1), mc.world, var1));
		float blockDMG = heldItem.getItem().getDestroySpeed(heldItem, BlockUtils.getBlockState(var1));
		if (BlockUtils.getBlock(var1).getBlockHardness(BlockUtils.getBlockState(var1), mc.world, var1) > 4.0f) {
			mineSpeed = 0.005f;
		} else if (getBlock.floatValue() >= 3.0f) {
			mineSpeed = 0.2f;
			if (blockDMG <= 2.0f) {
				mineSpeed = 0.0f;
			} else if (blockDMG == 4.0f) {
				mineSpeed = 0.02f;
			} else if (blockDMG == 6.0f) {
				mineSpeed = 0.05f;
			}
		} else if ((double) getBlock.floatValue() >= 1.5) {
			mineSpeed = 0.1f;
			if (blockDMG <= 2.0f) {
				mineSpeed = 0.0f;
			} else if (blockDMG == 4.0f) {
				mineSpeed = 0.02f;
			} else if (blockDMG == 6.0f) {
				mineSpeed = 0.05f;
			}
		} else if (getBlock.floatValue() >= 1.0f) {
			mineSpeed = 0.5f;
			if (blockDMG <= 2.0f) {
				mineSpeed = 0.0f;
			} else if (blockDMG == 4.0f) {
				mineSpeed = 0.4f;
			}
		}
		((IPlayerControllerMP) mc.playerController).setCurBlockDamageMP(((IPlayerControllerMP) mc.playerController).getCurBlockDamageMP() + mineSpeed);
	}
	
	@Override
	public void onDisable() {
		((IPlayerControllerMP) mc.playerController).setCurBlockDamageMP(0);
	}
}
