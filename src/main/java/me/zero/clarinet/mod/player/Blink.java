package me.zero.clarinet.mod.player;

import java.util.ArrayList;

import me.zero.clarinet.mixin.mixins.minecraft.entity.IEntity;
import net.minecraft.nbt.NBTTagCompound;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.network.EventPacketSend;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

import static net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG;

public class Blink extends Mod {
	
	private ArrayList<Packet> packets = new ArrayList<Packet>();
	private EntityOtherPlayerMP fakePlayer = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	private long blinkTime;
	private long lastTime;
	
	public Blink() {
		super("Blink", "Fake Lag", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		if (mc.player != null && mc.world != null) {
			lastTime = System.currentTimeMillis();
			this.oldX = mc.player.posX;
			this.oldY = mc.player.posY;
			this.oldZ = mc.player.posZ;
			this.fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());

			fakePlayer.inventory.copyInventory(mc.player.inventory);
			fakePlayer.setHealth(mc.player.getHealth());
			fakePlayer.setScore(mc.player.getScore());

			this.fakePlayer.copyLocationAndAnglesFrom(mc.player);
			this.fakePlayer.rotationYawHead = mc.player.rotationYawHead;
			mc.world.addEntityToWorld(-69, this.fakePlayer);
		} else {
			this.toggle();
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = blinkTime + "ms";
	}
	
	@Override
	public void onDisable() {
		if (mc.player != null && mc.world != null) {
			for (Packet packet : packets) {
				mc.getConnection().getNetworkManager().sendPacket(packet);
			}
			mc.world.removeEntityFromWorld(-69);
			this.fakePlayer = null;
			blinkTime = 0L;
		}
	}
	
	@EventTarget
	public void onPacketSend(EventPacketSend event) {
		if (event.getPacket() instanceof CPacketPlayer) {
			event.setCancelled(true);
			if ((mc.player.posX != mc.player.prevPosX) || (mc.player.posZ != mc.player.prevPosZ) || (mc.player.posY != mc.player.prevPosY)) {
				blinkTime += System.currentTimeMillis() - lastTime;
				if (event.getPacket() != null) {
					packets.add(event.getPacket());
				}
			}
			lastTime = System.currentTimeMillis();
		}
	}
}