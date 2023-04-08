package me.zero.clarinet.mod.movement.longjump;

import java.util.List;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.util.ITimer;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.ModMode;
import me.zero.clarinet.mod.movement.LongJump;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

public class LongJumpNoDamage extends ModMode<LongJump> {
	
	private int speedIndex;
	private int counter;
	
	public LongJumpNoDamage(LongJump parent) {
		super(parent, "NoDamage");
	}
	
	@Override
	public void onEnable() {
		if (mc.player != null) {
			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
			mc.player.onGround = true;
		} else {
			parent.toggle();
		}
	}
	
	@Override
	public void onDisable() {
		if (mc.player != null) {
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
			mc.player.onGround = false;
			mc.player.capabilities.isFlying = false;
		}
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		if (Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
			sendPositionPacket(0.0D, 2.147483647E9D, 0.0D);
		}
		float direction = mc.player.rotationYaw + (mc.player.moveForward < 0.0F ? 180 : 0) + (mc.player.moveStrafing > 0.0F ? -90.0F * (mc.player.moveForward > 0.0F ? 0.5F : mc.player.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F) - (mc.player.moveStrafing < 0.0F ? -90.0F * (mc.player.moveForward > 0.0F ? 0.5F : mc.player.moveForward < 0.0F ? -0.5F : 1.0F) : 0.0F);
		float xDir = (float) Math.cos((direction + 90.0F) * 3.141592653589793D / 180.0D);
		float zDir = (float) Math.sin((direction + 90.0F) * 3.141592653589793D / 180.0D);
		if (!mc.player.collidedVertically) {
			this.speedIndex += 1;
			if (this.mc.gameSettings.keyBindSneak.isKeyDown()) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(0.0D, 2.147483647E9D, 0.0D, false));
			}
			this.counter = 0;
			if (!mc.player.collidedVertically) {
				if (mc.player.motionY == -0.07190068807140403D) {
					mc.player.motionY *= 0.3499999940395355D;
				}
				if (mc.player.motionY == -0.10306193759436909D) {
					mc.player.motionY *= 0.550000011920929D;
				}
				if (mc.player.motionY == -0.13395038817442878D) {
					mc.player.motionY *= 0.6700000166893005D;
				}
				if (mc.player.motionY == -0.16635183030382D) {
					mc.player.motionY *= 0.6899999976158142D;
				}
				if (mc.player.motionY == -0.19088711097794803D) {
					mc.player.motionY *= 0.7099999785423279D;
				}
				if (mc.player.motionY == -0.21121925191528862D) {
					mc.player.motionY *= 0.20000000298023224D;
				}
				if (mc.player.motionY == -0.11979897632390576D) {
					mc.player.motionY *= 0.9300000071525574D;
				}
				if (mc.player.motionY == -0.18758479151225355D) {
					mc.player.motionY *= 0.7200000286102295D;
				}
				if (mc.player.motionY == -0.21075983825251726D) {
					mc.player.motionY *= 0.7599999904632568D;
				}
				if (this.getDistance(mc.player, 69.0) < 0.5) {
					if (mc.player.motionY == -0.23537393014173347D) {
						mc.player.motionY *= 0.029999999329447746D;
					}
					if (mc.player.motionY == -0.08531999505205401D) {
						mc.player.motionY *= -0.5D;
					}
					if (mc.player.motionY == -0.03659320313669756D) {
						mc.player.motionY *= -0.10000000149011612D;
					}
					if (mc.player.motionY == -0.07481386749524899D) {
						mc.player.motionY *= -0.07000000029802322D;
					}
					if (mc.player.motionY == -0.0732677700939672D) {
						mc.player.motionY *= -0.05000000074505806D;
					}
					if (mc.player.motionY == -0.07480988066790395D) {
						mc.player.motionY *= -0.03999999910593033D;
					}
					if (mc.player.motionY == -0.0784000015258789D) {
						mc.player.motionY *= 0.10000000149011612D;
					}
					if (mc.player.motionY == -0.08608320193943977D) {
						mc.player.motionY *= 0.10000000149011612D;
					}
					if (mc.player.motionY == -0.08683615560584318D) {
						mc.player.motionY *= 0.05000000074505806D;
					}
					if (mc.player.motionY == -0.08265497329678266D) {
						mc.player.motionY *= 0.05000000074505806D;
					}
					if (mc.player.motionY == -0.08245009535659828D) {
						mc.player.motionY *= 0.05000000074505806D;
					}
					if (mc.player.motionY == -0.08244005633718426D) {
						mc.player.motionY = -0.08243956442521608D;
					}
					if (mc.player.motionY == -0.08243956442521608D) {
						mc.player.motionY = -0.08244005590677261D;
					}
					if ((mc.player.motionY > -0.1D) && (mc.player.motionY < -0.08D) && (!mc.player.onGround) && (this.mc.gameSettings.keyBindForward.isKeyDown())) {
						mc.player.motionY = -9.999999747378752E-5D;
					}
				} else {
					if ((mc.player.motionY < -0.2D) && (mc.player.motionY > -0.24D)) {
						mc.player.motionY *= 0.7D;
					}
					if ((mc.player.motionY < -0.25D) && (mc.player.motionY > -0.32D)) {
						mc.player.motionY *= 0.8D;
					}
					if ((mc.player.motionY < -0.35D) && (mc.player.motionY > -0.8D)) {
						mc.player.motionY *= 0.98D;
					}
					if ((mc.player.motionY < -0.8D) && (mc.player.motionY > -1.6D)) {
						mc.player.motionY *= 0.99D;
					}
				}
			}
			((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 0.85F);
			double[] speedVals = { 0.420606D, 0.417924D, 0.415258D, 0.412609D, 0.409977D, 0.407361D, 0.404761D, 0.402178D, 0.399611D, 0.39706D, 0.394525D, 0.392D, 0.3894D, 0.38644D, 0.383655D, 0.381105D, 0.37867D, 0.37625D, 0.37384D, 0.37145D, 0.369D, 0.3666D, 0.3642D, 0.3618D, 0.35945D, 0.357D, 0.354D, 0.351D, 0.348D, 0.345D, 0.342D, 0.339D, 0.336D, 0.333D, 0.33D, 0.327D, 0.324D, 0.321D, 0.318D, 0.315D, 0.312D, 0.309D, 0.307D, 0.305D, 0.303D, 0.3D, 0.297D, 0.295D, 0.293D, 0.291D, 0.289D, 0.287D, 0.285D, 0.283D, 0.281D, 0.279D, 0.277D, 0.275D, 0.273D, 0.271D, 0.269D, 0.267D, 0.265D, 0.263D, 0.261D, 0.259D, 0.257D, 0.255D, 0.253D, 0.251D, 0.249D, 0.247D, 0.245D, 0.243D, 0.241D, 0.239D, 0.237D };
			if (this.mc.gameSettings.keyBindForward.isKeyDown()) {
				try {
					mc.player.motionX = (xDir * speedVals[(this.speedIndex - 1)] * 3.0D);
					mc.player.motionZ = (zDir * speedVals[(this.speedIndex - 1)] * 3.0D);
				} catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
				}
			} else {
				mc.player.motionX = 0.0D;
				mc.player.motionZ = 0.0D;
			}
		} else {
			if (mc.player.moveForward != 0.0F || mc.player.moveStrafing != 0.0F) {
				((ITimer) ((IMinecraft) mc).getTimer()).setTickLength(50f / 1.0F);
				this.speedIndex = 0;
				this.counter += 1;
				mc.player.motionX /= 13.0D;
				mc.player.motionZ /= 13.0D;
				if (this.counter == 1) {
					sendPositionPacket(mc.player.posX, mc.player.posY, mc.player.posZ);
					sendPositionPacket(mc.player.posX + 0.0624D, mc.player.posY, mc.player.posZ);
					sendPositionPacket(mc.player.posX, mc.player.posY + 0.419D, mc.player.posZ);
					sendPositionPacket(mc.player.posX + 0.0624D, mc.player.posY, mc.player.posZ);
					sendPositionPacket(mc.player.posX, mc.player.posY + 0.419D, mc.player.posZ);
				}
				if (this.counter > 2) {
					this.counter = 0;
					mc.player.motionX = (xDir * 0.3D);
					mc.player.motionZ = (zDir * 0.3D);
					mc.player.motionY = 0.42399999499320984D;
				}
			}
		}
	}
	
	public void sendPositionPacket(double x, double y, double z) {
		mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, mc.player.onGround));
	}
	
	private double getDistance(EntityPlayer player, double distance) {
		final List<AxisAlignedBB> v1 = player.world.getCollisionBoxes(player, player.getEntityBoundingBox().offset(0.0, -distance, 0.0));
		if (v1.isEmpty()) {
			return 0.0;
		}
		double v2 = 0.0;
		for (AxisAlignedBB v4 : v1) {
			if (v4.maxY > v2) {
				v2 = v4.maxY;
			}
		}
		return player.posY - v2;
	}
}
