package me.zero.clarinet.mod.world;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.block.Block;

public class Xray extends Mod {
	
	public Xray() {
		super("XRay", "Allows you to see ores through blocks!", Keyboard.KEY_X, Category.WORLD);
		// 14, 15, 16, 21, 56, 73, 129
	}
	
	@Override
	public void onEnable() {
		mc.renderGlobal.loadRenderers();
	}
	
	@Override
	public void onDisable() {
		mc.renderGlobal.loadRenderers();
	}
	
	public boolean isXrayBlock(Block block) {
		for (Integer id : Impact.getInstance().getXrayManager().getBlocks()) {
			if (Block.getIdFromBlock(block) == id) {
				return true;
			}
		}
		return false;
	}
}
