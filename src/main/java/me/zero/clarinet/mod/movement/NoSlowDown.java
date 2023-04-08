package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

public class NoSlowDown extends Mod {
	
	private int stage = 0;
	
	public NoSlowDown() {
		super("NoSlowDown", "Don't slow down", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onMotionUpdate(EventMotionUpdate event) {
		if (isBlocking()) {
			switch (event.type) {
				case EventType.PRE: {
					mc.player.connection.sendPacket(new CPacketPlayerDigging(Action.RELEASE_USE_ITEM, new BlockPos(mc.player), EnumFacing.getFacingFromVector((int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ)));
					break;
				}
				case EventType.POST: {
					if (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
						if (mc.player.getHeldItemOffhand() != null) {
							if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
								mc.playerController.processRightClick(mc.player, mc.world, EnumHand.OFF_HAND);
							}
						} else {
							mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
						}
					}
					break;
				}
			}
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventPacketReceive event) {
		if (event.getPacket() instanceof SPacketEntityEquipment) {
			SPacketEntityEquipment packet = (SPacketEntityEquipment) event.getPacket();
			if (packet.getEquipmentSlot().getIndex() == 1 && packet.getItemStack() == null) {
				if (mc.player.getHeldItemOffhand() != null && mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
					if (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
						if (isBlocking()) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	private boolean isBlocking() {
		return mc.gameSettings.keyBindUseItem.isKeyDown() && (mc.player.getHeldItemOffhand() != null && mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword);
	}
}
