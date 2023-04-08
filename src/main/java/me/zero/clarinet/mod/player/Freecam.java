package me.zero.clarinet.mod.player;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.network.EventPacketSend;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.event.render.EventRenderHands;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.NumberValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.CPacketPlayer;

public class Freecam extends Mod {
	
	private EntityOtherPlayerMP fakePlayer = null;
	private double oldX;
	private double oldY;
	private double oldZ;
	private double onGround;
	
	public NumberValue speed = new NumberValue(this, "Speed", "speed", 1D, 1D, 5D, 0.5D);
	
	public Freecam() {
		super("Freecam", "Fly out of your body, like spectator mode", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		if (mc.player == null) {
			
		} else {
			oldX = mc.player.posX;
			oldY = mc.player.posY;
			oldZ = mc.player.posZ;
			fakePlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());

			fakePlayer.inventory.copyInventory(mc.player.inventory);
			fakePlayer.setHealth(mc.player.getHealth());
			fakePlayer.setScore(mc.player.getScore());

			fakePlayer.copyLocationAndAnglesFrom(mc.player);
			fakePlayer.rotationYawHead = mc.player.rotationYawHead;
			mc.world.addEntityToWorld(-420, fakePlayer);
		}
	}
	
	@EventTarget
	public void onRenderHand(EventRenderHands event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onPacketSend(EventPacketSend event) {
		if (event.getPacket() instanceof CPacketPlayer) {
			event.setCancelled(true);
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		mc.player.motionX = 0;
		mc.player.motionY = 0;
		mc.player.motionZ = 0;
		mc.player.jumpMovementFactor = speed.getValue().floatValue() / 5.0F;
		if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY += speed.getValue().floatValue() / 4.0F;
		if (mc.gameSettings.keyBindSneak.isKeyDown()) mc.player.motionY -= speed.getValue().floatValue() / 4.0F;
	}
	
	@Override
	public void onDisable() {
		mc.player.setPositionAndRotation(oldX, oldY, oldZ, mc.player.rotationYaw, mc.player.rotationPitch);
		mc.world.removeEntityFromWorld(-420);
		fakePlayer = null;
		mc.renderGlobal.loadRenderers();
	}
}
