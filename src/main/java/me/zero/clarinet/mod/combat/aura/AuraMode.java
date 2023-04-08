package me.zero.clarinet.mod.combat.aura;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public abstract class AuraMode {
	
	protected static final Minecraft mc = Minecraft.getMinecraft();
	
	public static AuraMode NULL = new Null();
	
	protected Aura aura;
	
	public AuraMode(Aura aura) {
		this.aura = aura;
	}
	
	public abstract void onPreUpdate(EventMotionUpdate event);
	
	public abstract void onPostUpdate(EventMotionUpdate event);
	
	protected static void attackEntity(EntityLivingBase en) {
		Aura aura = Impact.getInstance().getModManager().get(Aura.class);
		double percent = (aura.hmr.getValue().doubleValue() / 1.0) * 100.0;
		boolean hit = MathUtils.chance((int) percent);
		if (hit) {
			if (!(aura.getAuraMode() instanceof AuraSwitch)) {
				mc.playerController.attackEntity(mc.player, en);
				mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
			}
			mc.playerController.attackEntity(mc.player, en);
		}
		if (!aura.noswing.getValue()) {
			mc.player.swingArm(EnumHand.MAIN_HAND);
		} else {
			mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		}
	}
	
	protected static void startBlock() {
		if (!canBlock()) return;
		if (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
			if (mc.player.getHeldItemOffhand() != null) {
				if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
					mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
				}
			} else {
				mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
			}
		}
	}
	
	protected static void stopBlock() {
		if (!canBlock()) return;
		mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(mc.player), EnumFacing.getFacingFromVector((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ)));
	}
	
	private static boolean canBlock() {
		return Impact.getInstance().getModManager().get(Aura.class).attacking && Impact.getInstance().getModManager().get(Aura.class).autoblock.getValue() && (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword);
	}
	
	private static class Null extends AuraMode {
		
		public Null() {
			super(null);
		}
		
		@Override
		public void onPreUpdate(EventMotionUpdate event) {}
		
		@Override
		public void onPostUpdate(EventMotionUpdate event) {}
	}
}
