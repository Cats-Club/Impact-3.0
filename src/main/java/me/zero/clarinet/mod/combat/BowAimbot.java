package me.zero.clarinet.mod.combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.misc.AntiBot;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.entity.EntityFilter;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;

public class BowAimbot extends Mod {
	
	private ArrayList<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
	
	private EntityLivingBase currentTarget;
	
	private float velocity;
	
	private BowAimbotEntityFilter filter;
	
	private BooleanValue teammates = new BooleanValue(this, "Teammates", "teammates");
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible");
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping");
	private BooleanValue friends = new BooleanValue(this, "Friends", "friends");
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);
	
	public NumberValue fov = new NumberValue(this, "FOV", "fov", 360D, 45D, 360D, 1D);
	
	public BowAimbot() {
		super("BowAimbot", "Aims at the specified targets with a bow", Keyboard.KEY_NONE, Category.COMBAT);
		filter = new BowAimbotEntityFilter();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!(mc.player.inventory.getCurrentItem() != null && mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.gameSettings.keyBindUseItem.isKeyDown())) {
			return;
		}
		entities = CombatUtils.getCloseEntities(1000, fov.getValue().intValue(), filter);
		entities.sort((target1, target2) -> {
			double distance1 = target1.getDistance(mc.player);
			double distance2 = target2.getDistance(mc.player);
			return distance1 < distance2 ? -1 : distance1 == distance2 ? 0 : 1;
		});
		if (entities.size() > 0) {
			currentTarget = entities.get(0);
			if (currentTarget != null || currentTarget.isDead || !filter.isValidEntity(currentTarget)) {
				if (currentTarget != null) {
					entities.remove(currentTarget);
				}
				if (entities.size() > 0) {
					currentTarget = entities.get(0);
				} else {
					return;
				}
			}
			int bowCharge = mc.player.getItemInUseMaxCount();
			velocity = bowCharge / 20;
			velocity = (velocity * velocity + velocity * 2) / 3;
			if (velocity < 0.1) {
				if (currentTarget instanceof EntityLivingBase) {
					float[] angles = RotationUtils.getRotations(currentTarget);
					mc.player.rotationYaw = angles[0];
					mc.player.rotationPitch = angles[1];
				}
				return;
			}
			if (velocity > 1) {
				velocity = 1;
			}
			double predictX = (currentTarget.posX - currentTarget.prevPosX) * 5;
			double predictY = (currentTarget.posY - currentTarget.prevPosY) * 5;
			double predictZ = (currentTarget.posZ - currentTarget.prevPosZ) * 5;
			double posX = currentTarget.posX + predictX - mc.player.posX;
			double posY = currentTarget.posY + predictY + currentTarget.getEyeHeight() - 0.15 - mc.player.posY - mc.player.getEyeHeight();
			double posZ = currentTarget.posZ + predictZ - mc.player.posZ;
			float yaw = (float) (Math.atan2(posZ, posX) * 180 / Math.PI) - 90;
			double y2 = Math.sqrt(posX * posX + posZ * posZ);
			float g = 0.006F;
			float tmp = (float) (velocity * velocity * velocity * velocity - g * (g * (y2 * y2) + 2 * posY * (velocity * velocity)));
			float pitch = (float) -Math.toDegrees(Math.atan((velocity * velocity - Math.sqrt(tmp)) / (g * y2)));
			mc.player.rotationYaw = yaw;
			mc.player.rotationPitch = pitch;
		}
	}
	
	public class BowAimbotEntityFilter extends EntityFilter {
		
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
