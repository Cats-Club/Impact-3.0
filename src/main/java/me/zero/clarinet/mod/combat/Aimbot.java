package me.zero.clarinet.mod.combat;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventMotionUpdate;
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

public class Aimbot extends Mod {

	private AimbotEntityFilter filter;
	
	private BooleanValue teammates = new BooleanValue(this, "Teammates", "teammates");
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible");
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping");
	private BooleanValue friends = new BooleanValue(this, "Friends", "friends");
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);
	
	private NumberValue range = new NumberValue(this, "Range", "range", 4.25D, 4D, 6D, 0.25D);
	private NumberValue fov = new NumberValue(this, "FOV", "fov", 360D, 45D, 360D, 1D);
	
	public Aimbot() {
		super("Aimbot", "Aims at entities around you", Keyboard.KEY_NONE, Category.COMBAT);
		filter = new AimbotEntityFilter();
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
        ArrayList<EntityLivingBase> entities = CombatUtils.getCloseEntities(range.getValue().doubleValue(), fov.getValue().intValue(), filter);
		entities.sort((target1, target2) -> {
			double distance1 = target1.getDistance(mc.player);
			double distance2 = target2.getDistance(mc.player);
			return distance1 < distance2 ? -1 : distance1 == distance2 ? 0 : 1;
		});
		if (entities.size() > 0) {
			float[] angles = RotationUtils.getRotations(entities.get(0));
			mc.player.rotationYaw = angles[0];
			mc.player.rotationPitch = angles[1];
		}
	}
	
	private class AimbotEntityFilter extends EntityFilter {
		
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
