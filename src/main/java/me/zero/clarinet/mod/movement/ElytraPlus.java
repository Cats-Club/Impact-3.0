package me.zero.clarinet.mod.movement;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.TimerUtil;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.math.MathHelper;

public class ElytraPlus extends Mod {
	
	private TimerUtil timer = new TimerUtil();
	
	private BooleanValue betterfly = new BooleanValue(this, "BetterFly", "betterfly", true);
	
	private BooleanValue takeoff = new BooleanValue(this, "Easy Takeoff", "takeoff", true);
	
	private NumberValue speed = new NumberValue(this, "Base Speed", "speed", 4D, 0D, 6D, 0.25D);
	
	private NumberValue up = new NumberValue(this, "Up Speed", "up", 1D, 0D, 3D, 0.25D);
	
	private NumberValue down = new NumberValue(this, "Down Speed", "down", 1D, 0D, 3D, 0.25D);
	
	public ElytraPlus() {
		super("Elytra+", "Makes it easier to fly with an elytra", Keyboard.KEY_NONE, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		ItemStack chest = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		if (chest == null || chest.getItem() != Items.ELYTRA) {
			return;
		}
		if (mc.player.isElytraFlying()) {
			if (betterfly.getValue()) {
				double speed = ClientUtils.getBaseMoveSpeed() * this.speed.getValue().doubleValue();
				double forward = mc.player.moveForward;
				double strafe = mc.player.moveStrafing;
				float yaw = mc.player.rotationYaw;
				float pitch = mc.player.rotationPitch;
				if ((forward == 0.0D) && (strafe == 0.0D)) {
					mc.player.motionX = 0;
					mc.player.motionY = 0;
					mc.player.motionZ = 0;
				} else {
					if (forward != 0.0D) {
						if (strafe > 0.0D) {
							yaw += (forward > 0.0D ? -45 : 45);
						} else if (strafe < 0.0D) {
							yaw += (forward > 0.0D ? 45 : -45);
						}
						strafe = 0.0D;
						if (forward > 0.0D) {
							forward = 1.0D;
						} else if (forward < 0.0D) {
							forward = -1.0D;
						}
					}
					double strafeX = Math.sin(Math.toRadians(yaw + 90.0F));
					double strafeZ = Math.cos(Math.toRadians(yaw + 90.0F));
					yaw = (float) Math.toRadians(yaw);
					pitch = (float) Math.toRadians(pitch);
					double dirX = -Math.sin(yaw) * Math.cos(pitch);
					double dirZ = Math.cos(yaw) * Math.cos(pitch);
					double dirY = -MathHelper.sin(pitch);
					mc.player.motionX = (forward * speed * dirX) + (strafe * speed * strafeX);
					mc.player.motionZ = (forward * speed * dirZ) - (strafe * speed * strafeZ);
					mc.player.motionY = (forward * speed * dirY);
				}
				if (mc.gameSettings.keyBindJump.isKeyDown()) {
					mc.player.motionY += up.getValue().doubleValue();
				} else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					mc.player.motionY -= down.getValue().doubleValue();
				}
			}
		} else if (takeoff.getValue() && mc.gameSettings.keyBindJump.isKeyDown() && !ItemElytra.isUsable(chest)) {
			if (timer.delay(1000)) {
				mc.player.setJumping(false);
				mc.player.setSprinting(true);
				mc.player.jump();
				timer.reset();
			}
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
		}
	}
}
