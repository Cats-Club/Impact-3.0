package me.zero.clarinet.mod.combat;

import java.util.ArrayList;
import java.util.Random;

import me.zero.clarinet.event.player.EventMotionUpdate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.misc.AntiBot;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.TimerUtil;
import me.zero.clarinet.util.entity.EntityFilter;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;

public class SmoothAim extends Mod {
	
	private ArrayList<EntityLivingBase> entities = new ArrayList<>();
	
	private EntityLivingBase currentTarget;
	
	private AimbotEntityFilter filter;
	
	private double rand1, rand2, rand3;
	
	private TimerUtil randSpeedTimer = new TimerUtil();
	
	private BooleanValue teammates = new BooleanValue(this, "Teammates", "teammates");
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible");
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping");
	private BooleanValue friends = new BooleanValue(this, "Friends", "friends");
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);
	private BooleanValue sidebutton = new BooleanValue(this, "SideButton", "sidebutton", false);
	
	private NumberValue range = new NumberValue(this, "Range", "range", 4.75D, 4D, 8D, 0.25D);
	private NumberValue fov = new NumberValue(this, "FOV", "fov", 360D, 45D, 360D, 1D);
	private NumberValue speed = new NumberValue(this, "Smoothness", "smooth", 2.75D, 1D, 10D, 0.25D);
	private NumberValue base = new NumberValue(this, "Base Speed", "base", 3D, 1D, 10D, 0.25D);
	private NumberValue randSpeed = new NumberValue(this, "Random Speed", "random", 250D, 0D, 1000D, 50D);
	private NumberValue width = new NumberValue(this, "Width Random", "width", 1.25D, 0D, 2D, 0.05D);
	private NumberValue height = new NumberValue(this, "Height Random", "height", 1.35D, 0D, 2D, 0.05D);
	
	public SmoothAim() {
		super("SmoothAim", "Legit aimbot", Keyboard.KEY_NONE, Category.COMBAT);
		filter = new AimbotEntityFilter();
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		entities = CombatUtils.getCloseEntities(range, fov, filter);
		entities.sort((target1, target2) -> {
			double distance1 = target1.getDistance(mc.player);
			double distance2 = target2.getDistance(mc.player);
			return distance1 < distance2 ? -1 : distance1 == distance2 ? 0 : 1;
		});
		if (randSpeedTimer.delay(randSpeed)) {
			if (currentTarget != null) {
				rand1 = currentTarget.width / width.getValue().floatValue() * new Random().nextDouble() - currentTarget.width / (width.getValue().floatValue() * 2);
				rand2 = currentTarget.width / width.getValue().floatValue() * new Random().nextDouble() - currentTarget.width / (width.getValue().floatValue() * 2);
				rand3 = currentTarget.height / height.getValue().floatValue() * new Random().nextDouble() - currentTarget.height / (height.getValue().floatValue() * 2);
			}
			randSpeedTimer.reset();
		}
		if (entities.size() > 0 && (Mouse.isButtonDown(4) || !sidebutton.getValue())) {
			currentTarget = entities.get(0);
			if (mc.currentScreen == null && currentTarget != null) {
				float pitch = getPitch(this.currentTarget);
				mc.player.rotationPitch = RotationUtils.limitAngleChange(mc.player.rotationPitch, pitch, Math.abs(mc.player.rotationPitch - pitch) / speed.getValue().floatValue() + base.getValue().floatValue());
				float yaw = getYaw(this.currentTarget);
				mc.player.rotationYaw = RotationUtils.limitAngleChange(mc.player.rotationYaw, yaw, Math.abs(mc.player.rotationYaw - yaw) / speed.getValue().floatValue() + base.getValue().floatValue());
			}
		}
	}
	
	public float getPitch(Entity entity) {
		double deltaX = entity.posX - mc.player.posX;
		double deltaZ = entity.posZ - mc.player.posZ;
		double deltaY = entity.posY + rand3 - 2.2D + entity.getEyeHeight() - mc.player.posY;
		double distanceXZ = MathHelper.sqrt(deltaX * deltaX + deltaZ * deltaZ);
		double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
		return (float) (mc.player.rotationPitch + (-MathHelper.wrapDegrees(mc.player.rotationPitch - (float) pitchToEntity) - 2.5F));
	}
	
	public float getYaw(Entity entity) {
		double deltaX = entity.posX + rand1 - mc.player.posX;
		double deltaZ = entity.posZ + rand2 - mc.player.posZ;
		double yawToEntity = 0.0D;
		double v = Math.toDegrees(Math.atan(deltaZ / deltaX));
		if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
			yawToEntity = 90.0D + v;
		} else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
			yawToEntity = -90.0D + v;
		} else {
			yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
		}
		return (float) (mc.player.rotationYaw + (MathHelper.wrapDegrees(-(mc.player.rotationYaw - (float) yawToEntity))));
	}
	
	public class AimbotEntityFilter extends EntityFilter {
		
		@Override
		public boolean customCheck(Entity ent) {
			if (ent instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) ent;
				if (Impact.getInstance().getModManager().get(AntiBot.class).isToggled()) {
					if (Impact.getInstance().getModManager().get(AntiBot.class).isNPC(player)) {
						return false;
					}
				}
			}
			return true;
		}
		
		@Override
		public boolean walls() {
			return false;
		}
		
		@Override
		public boolean sleeping() {
			return sleeping.getValue();
		}
		
		@Override
		public boolean invisibles() {
			return invisible.getValue();
		}
		
		@Override
		public boolean teammates() {
			return teammates.getValue();
		}
		
		@Override
		public boolean friends() {
			return friends.getValue();
		}
		
		@Override
		public boolean players() {
			return players.getValue();
		}
		
		@Override
		public boolean animals() {
			return animals.getValue();
		}
		
		@Override
		public boolean hostiles() {
			return mobs.getValue();
		}
		
		@Override
		public boolean passives() {
			return false;
		}
	}
}
