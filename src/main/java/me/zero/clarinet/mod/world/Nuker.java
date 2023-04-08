package me.zero.clarinet.mod.world;

import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.BlockUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class Nuker extends Mod {
	
	private MultiValue<String> mode = new MultiValue<String>(this, "Mode", "mode", "Normal", new String[]{ "Normal", "Flatten", "ID" });
	
	private BooleanValue esp = new BooleanValue(this, "ESP", "esp", true);
	
	private NumberValue radius = new NumberValue(this, "Radius", "radius", 4D, 2D, 6D, 1D);
	
	private NumberValue r = new NumberValue(this, "Red", "red", 255D, 0D, 255D, 1D);
	
	private NumberValue g = new NumberValue(this, "Green", "green", 255D, 0D, 255D, 1D);
	
	private NumberValue b = new NumberValue(this, "Blue", "blue", 255D, 0D, 255D, 1D);
	
	public Nuker() {
		super("Nuker", "Nukes blocks around the player!", Keyboard.KEY_N, Category.WORLD);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = mode.getValue();
		int radius = this.radius.getValue().intValue();
		for (int x = -radius; x < radius; x++) {
			for (int y = radius; y > (mode.getValue().equalsIgnoreCase("Flatten") ? -1 : -radius); y--) {
				for (int z = -radius; z < radius; z++) {
					int xPos = (int) mc.player.posX + x;
					int yPos = (int) mc.player.posY + y;
					int zPos = (int) mc.player.posZ + z;
					BlockPos bp = new BlockPos(xPos, yPos, zPos);
					Block block = BlockUtils.getBlock(xPos, yPos, zPos);
					if (isGoodBlock(block)) {
						mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.START_DESTROY_BLOCK, bp, EnumFacing.NORTH));
						mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.STOP_DESTROY_BLOCK, bp, EnumFacing.NORTH));
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		if (!esp.getValue()) return;
		int radius = this.radius.getValue().intValue();
		for (int x = -radius; x < radius; x++) {
			for (int y = radius; y > (mode.getValue().equalsIgnoreCase("Flatten") ? -1 : -radius); y--) {
				for (int z = -radius; z < radius; z++) {
					int xPos = (int) mc.player.posX + x;
					int yPos = (int) mc.player.posY + y;
					int zPos = (int) mc.player.posZ + z;
					BlockPos bp = new BlockPos(xPos, yPos, zPos);
					Block block = BlockUtils.getBlock(xPos, yPos, zPos);
					if (isGoodBlock(block)) {
						double xr = xPos - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
						double yr = yPos - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
						double zr = zPos - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
						AxisAlignedBB box = new AxisAlignedBB(xr, yr, zr, xr + 1.0D, yr + 1.0D, zr + 1.0D);
						RenderUtils.drawSolidBlockESP(box, r.getValue().floatValue() / 255.0F, g.getValue().floatValue() / 255.0F, b.getValue().floatValue() / 255.0F, 0.2F);
					}
				}
			}
		}
	}
	
	public boolean isGoodBlock(Block block) {
		if (mode.getValue().equalsIgnoreCase("ID")) {
			int id = Block.getIdFromBlock(block);
			for (Integer nBlock : Impact.getInstance().getNukerManager().getBlocks()) {
				if (nBlock == id) {
					return true;
				}
			}
			return false;
		}
		return block != Blocks.AIR;
	}
}
