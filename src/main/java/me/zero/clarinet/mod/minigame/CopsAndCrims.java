package me.zero.clarinet.mod.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.entity.MoverType;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;
import me.zero.clarinet.event.api.types.Priority;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.network.EventPacketSend;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.RotationUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;

public class CopsAndCrims extends Mod {
	
	public int ticks;
	public int lookDelay;
	private EntityPlayer target;
	public int buffer = 10;
	private Map<EntityPlayer, List<Vec3d>> playerPositions = new HashMap<EntityPlayer, List<Vec3d>>();
	
	public NumberValue delay = new NumberValue(this, "Delay", "delay", 7D, 0D, 35D, 1D);
	
	private BooleanValue noSpread = new BooleanValue(this, "NoSpread", "noSpread", true);
	private BooleanValue rcs = new BooleanValue(this, "Recoil", "rcs");
	private BooleanValue silent = new BooleanValue(this, "Silent", "silent", true);
	private BooleanValue autoShoot = new BooleanValue(this, "AutoShoot", "autoShoot", true);
	
	private MultiValue<String> bone = new MultiValue<String>(this, "Bone", "bone", "Head", new String[] { "Head", "Neck", "Chest", "Jimmies", "Legs", "Feet" });
	
	public NumberValue rcsHorizontal = new NumberValue(this, "Horizontal Recoil", "hRecoil", 0.1D, 0.1D, 2.0D, 0.1D);
	public NumberValue rcsVertical = new NumberValue(this, "Verticle Recoil", "vRecoil", 0.5D, 0.1D, 2.0D, 0.1D);
	
	public NumberValue fov = new NumberValue(this, "FOV", "fov", 360D, 45D, 360D, 1D);
	
	public CopsAndCrims() {
		super("CopsAndCrims", "Hack for Cops and Crims", Keyboard.KEY_NONE, Category.MINIGAME);
	}
	
	@EventTarget
	public void sendPacket(EventPacketSend event) {
		if (this.isToggled() && rcs.getValue() && event.getPacket() instanceof CPacketPlayerTryUseItem) {
			++this.ticks;
		}
	}
	
	@EventTarget(Priority.LOWEST)
	public void preTick(EventMotionUpdate event) {
		if (event.type != EventType.PRE) {
			return;
		}
		double targetWeight = Double.NEGATIVE_INFINITY;
		this.target = null;
		for (EntityPlayer p : mc.world.playerEntities) {
			if (p.equals(mc.player) || Impact.getInstance().getFriendManager().isFriend(p.getName()) || p.ticksExisted < 40 || p.isInvisible() || !mc.player.canEntityBeSeen(p) || isOnSameTeam(p, mc.player) || !RotationUtils.isVisibleFOV(p, (Entity) mc.player, fov.getValue().floatValue())) continue;
			if (this.target == null) {
				this.target = p;
				targetWeight = this.getTargetWeight(p);
				continue;
			}
			if (this.getTargetWeight(p) <= targetWeight) continue;
			this.target = p;
			targetWeight = this.getTargetWeight(p);
		}
		for (EntityPlayer player2 : this.playerPositions.keySet()) {
			if (mc.world.playerEntities.contains(player2)) continue;
			this.playerPositions.remove(player2);
		}
		for (EntityPlayer player2 : mc.world.playerEntities) {
			this.playerPositions.putIfAbsent(player2, new ArrayList());
			List<Vec3d> previousPositions = this.playerPositions.get(player2);
			previousPositions.add(new Vec3d(player2.posX, player2.posY, player2.posZ));
			if (previousPositions.size() <= this.buffer) continue;
			int i = 0;
			for (Vec3d position : new ArrayList<Vec3d>(previousPositions)) {
				if (i < previousPositions.size() - this.buffer) {
					previousPositions.remove(previousPositions.get(i));
				}
				++i;
			}
		}
		if (this.target != null) {
			if (rcs.getValue() && this.ticks >= 30) {
				this.ticks = 0;
			}
			++this.lookDelay;
			Entity simulated = this.predictPlayerMovement(this.target);
			float offset = 0.0f;
			if (bone.getValue().equalsIgnoreCase("Head")) {
				offset = -0.2f;
			}
			if (bone.getValue().equalsIgnoreCase("Neck")) {
				offset = 0.1f;
			}
			if (bone.getValue().equalsIgnoreCase("Chest")) {
				offset = 0.4f;
			}
			if (bone.getValue().equalsIgnoreCase("Jimmies")) {
				offset = 0.85f;
			}
			if (bone.getValue().equalsIgnoreCase("Legs")) {
				offset = 1.0f;
			}
			if (bone.getValue().equalsIgnoreCase("Feet")) {
				offset = 1.5f;
			}
			float[] rotations = this.getPlayerRotations(mc.player, simulated.posX, simulated.posY + (double) this.target.getEyeHeight() - (double) offset, simulated.posZ);
			if (rcs.getValue()) {
				event.yaw = rotations[0];
				event.pitch = rotations[1] + rcsVertical.getValue().floatValue() * (float) this.ticks;
				if (this.ticks >= 10) {
					event.yaw = rotations[0] - rcsHorizontal.getValue().floatValue() * (float) this.ticks;
				}
				if (this.ticks >= 20) {
					event.yaw = rotations[0] + rcsHorizontal.getValue().floatValue() * (float) this.ticks;
				}
			} else {
				event.yaw = rotations[0];
				event.pitch = rotations[1];
			}
			if (!silent.getValue()) {
				mc.player.rotationYaw = event.yaw;
				mc.player.rotationPitch = event.pitch;
			}
			if ((float) this.lookDelay >= delay.getValue().floatValue()) {
				if (noSpread.getValue()) {
					mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
				}
				if (autoShoot.getValue()) {
					mc.playerController.processRightClick(mc.player, mc.world, EnumHand.MAIN_HAND);
				}
				if (noSpread.getValue()) {
					mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
				}
				this.lookDelay = 0;
			}
		} else {
			--this.ticks;
			if (this.ticks <= 0) {
				this.ticks = 0;
			}
		}
	}
	
	public double getTargetWeight(EntityPlayer p) {
		double weight = -mc.player.getDistance(p);
		if (p.lastTickPosX == p.posX && p.lastTickPosY == p.posY && p.lastTickPosZ == p.posZ) {
			weight += 200.0;
		}
		return weight -= (double) (p.getDistance(mc.player) / 5.0f);
	}
	
	private Entity predictPlayerMovement(EntityPlayer target) {
		int pingTicks = (int) Math.ceil((double) ClientUtils.getLatency(target) / 50.0);
		return this.predictPlayerLocation(target, pingTicks);
	}
	
	public Entity predictPlayerLocation(EntityPlayer player, int ticks) {
		List<Vec3d> previousPositions;
		if (this.playerPositions.containsKey(player) && (previousPositions = this.playerPositions.get(player)).size() > 1) {
			Vec3d origin = previousPositions.get(0);
			ArrayList<Vec3d> deltas = new ArrayList<Vec3d>();
			Vec3d previous = origin;
			for (Vec3d position : previousPositions) {
				deltas.add(new Vec3d(position.x - previous.x, position.y - previous.y, position.z - previous.z));
				previous = position;
			}
			double x = 0.0;
			double y = 0.0;
			double z = 0.0;
			for (Vec3d delta : deltas) {
				x += delta.x;
				y += delta.y;
				z += delta.z;
			}
			x /= (double) deltas.size();
			y /= (double) deltas.size();
			z /= (double) deltas.size();
			EntityOtherPlayerMP simulated = new EntityOtherPlayerMP(mc.world, player.getGameProfile());
			simulated.noClip = false;
			simulated.setPosition(player.posX, player.posY, player.posZ);
			int i = 0;
			while (i < ticks) {
				simulated.move(MoverType.PLAYER, x, y, z);
				++i;
			}
			return simulated;
		}
		return player;
	}
	
	public static boolean isOnSameTeam(EntityPlayer e, EntityPlayer e2) {
		if (e.getDisplayName().getFormattedText().contains("§" + getTeamFromName(e)) && e2.getDisplayName().getFormattedText().contains("§" + getTeamFromName(e))) {
			return true;
		}
		return false;
	}
	
	public static String getTeamFromName(Entity e) {
		Matcher m = Pattern.compile("§(.).*§r").matcher(e.getDisplayName().getFormattedText());
		if (m.find()) {
			return m.group(1);
		}
		return "f";
	}
	
	private final float[] getPlayerRotations(Entity player, double x, double y, double z) {
		double deltaX = x - player.posX;
		double deltaY = y - player.posY - (double) player.getEyeHeight() - 0.1;
		double deltaZ = z - player.posZ;
		double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(-Math.atan(deltaX / deltaZ)));
		double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		yawToEntity = wrapAngleTo180((float) yawToEntity);
		pitchToEntity = wrapAngleTo180((float) pitchToEntity);
		return new float[] { (float) yawToEntity, (float) pitchToEntity };
	}
	
	private static float wrapAngleTo180(float angle) {
		while ((angle %= 360.0f) >= 180.0f) {
			angle -= 360.0f;
		}
		while (angle < -180.0f) {
			angle += 360.0f;
		}
		return angle;
	}
}
