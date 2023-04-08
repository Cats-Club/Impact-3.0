package me.zero.clarinet.mod.movement;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.network.EventPacketSend;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.util.BlockUtils;
import me.zero.values.types.MultiValue;
import net.minecraft.network.play.client.CPacketPlayer;
import org.lwjgl.input.Mouse;

public class Jesus extends Mod {
	
	private MultiValue<String> mode = new MultiValue<String>(this, "Mode", "mode", "Solid", new String[] { "Solid", "Dolphin" });

	private boolean unset = false;
	
	public boolean isJesus;
	
	public Jesus() {
		super("Jesus", "Walk on water", Keyboard.KEY_J, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.suffix = mode.getValue();
		if (mode.getValue().equalsIgnoreCase("Dolphin")) {
			if (mc.player.isInWater()) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
				unset = false;
			} else {
                boolean held;
                int bind = mc.gameSettings.keyBindJump.getKeyCode();
                if (bind <= -100) {
                    held = Mouse.isButtonDown(bind + 100);
                } else {
                    held = Keyboard.isKeyDown(bind);
                }
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), held);
            }
		}
	}
	
	@EventTarget
	public void onPacketSend(EventPacketSend event) {
		if (mode.getValue().equalsIgnoreCase("Solid")) {
			double posX = mc.player.posX;
			double posY = mc.player.posY;
			double posZ = mc.player.posZ;
			if (!mc.player.isInWater() && mc.player.onGround) {
				String collide = BlockUtils.liquidCollision();
				if (collide.equalsIgnoreCase("positive")) {
					mc.player.setPosition(posX - 1.0E-10D, posY, posZ - 1.0E-10D);
				} else if (collide.equalsIgnoreCase("negative")) {
					mc.player.setPosition(posX + 1.0E-10D, posY, posZ + 1.0E-10D);
				}
			}
			if (BlockUtils.isOnLiquid() && !BlockUtils.getBlockUnderPlayer2(mc.player, -1.0E-5D).getMaterial(null).isLiquid()) {
				isJesus = true;
				if (mc.gameSettings.keyBindSneak.isKeyDown()) {
					mc.player.setPosition(posX, posY - 0.1D, posZ);
				}
				float yaw = mc.player.rotationYaw;
				float pitch = mc.player.rotationPitch;
				if (Impact.getInstance().getModManager().get(Aura.class).isToggled() && Impact.getInstance().getModManager().get(Aura.class).attacking) {
					float[] rotations = Impact.getInstance().getModManager().get(Aura.class).currentAngles;
					yaw = rotations[0];
					pitch = rotations[1];
				}
				if (event.getPacket() instanceof CPacketPlayer) {
					if (mc.player.ticksExisted % 2 == 0) {
						if (!mc.player.collidedHorizontally && mc.player.posY % 1.0D == 0.0D) {
							event.setPacket(new CPacketPlayer.PositionRotation(posX, mc.player.posY - 0.01D, posZ, yaw, pitch, true));
						}
					} else if (!mc.player.collidedHorizontally) {
						event.setPacket(new CPacketPlayer.PositionRotation(posX, mc.player.posY + 0.01D, posZ, yaw, pitch, true));
					}
				}
			} else {
				if (BlockUtils.getBlockUnderPlayer2(mc.player, -0.1D).getMaterial(null).isLiquid() || mc.player.fallDistance >= 3.0F) {
					isJesus = false;
				}
				if (BlockUtils.getBlockUnderPlayer2(mc.player, -0.01D).getMaterial(null).isLiquid()) {
					if (mc.gameSettings.keyBindSneak.isKeyDown()) {
						return;
					}
					mc.player.motionY = 0.075D;
				}
			}
		} else {
			isJesus = false;
		}
	}
	
	@Override
	public void onDisable() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
		isJesus = false;
	}
}
