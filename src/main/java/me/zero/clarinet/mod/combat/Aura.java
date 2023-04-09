package me.zero.clarinet.mod.combat;

import java.util.List;

import me.zero.clarinet.mod.combat.aura.*;
import me.zero.clarinet.util.RotationUtils;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.network.EventPacketReceive;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.misc.AntiBot;
import me.zero.clarinet.util.entity.EntityFilter;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.server.SPacketEntityEquipment;

public class Aura extends Mod {
	
	public float[] currentAngles = new float[2];
	public boolean attacking = false;
	public AuraEntityFilter filter = new AuraEntityFilter();
	
	private AuraSingle singleMode = new AuraSingle(this);
	private AuraSwitch switchMode = new AuraSwitch(this);
	private AuraTick tickMode = new AuraTick(this);
	private AuraMulti multiMode = new AuraMulti(this);
	private AuraDelay delayMode = new AuraDelay(this);
	private AuraCancer cancerMode = new AuraCancer(this);
	
	private MultiValue<String> mode = new MultiValue<>(this, "Mode", "mode", "Delay", new String[] { "Single", "Switch", "Tick", "Multi", "Delay", "Cancer" });
	private MultiValue<String> priority = new MultiValue<>(this, "Priority", "priority", "Distance", new String[] { "Distance", "Health", "Smart" });

    private BooleanValue walls = new BooleanValue(this, "Walls", "walls");
	public BooleanValue autoblock = new BooleanValue(this, "AutoBlock", "autoblock");
    private BooleanValue lockview = new BooleanValue(this, "LockView", "LockView");
    public BooleanValue tpsSync = new BooleanValue(this, "TPS Sync", "tps");
	public BooleanValue noswing = new BooleanValue(this, "NoSwing", "noswing");
	private BooleanValue teammates = new BooleanValue(this, "Teammates", "teammates");
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible");
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping");
	private BooleanValue friends = new BooleanValue(this, "Friends", "friends");
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);

	public NumberValue speed = new NumberValue(this, "APS", "speed", 12D, 5D, 20D, 0.25D);
	public NumberValue range = new NumberValue(this, "Range", "range", 4.25D, 2D, 6D, 0.25D);
	public NumberValue fov = new NumberValue(this, "FOV", "fov", 360D, 45D, 360D, 1D);
    private NumberValue randYaw = new NumberValue(this, "Random Yaw", "randYaw", 15D, 0D, 30D, 0.25D);
    private NumberValue randPitch = new NumberValue(this, "Random Pitch", "randPitch", 15D, 0D, 30D, 0.25D);
	private NumberValue alive = new NumberValue(this, "Existed Ticks", "ticks", 0D, 0D, 120D, 1D);
	public NumberValue hmr = new NumberValue(this, "Hit Miss Ratio", "hmr", 1D, 0D, 1D, 0.05D);
	public NumberValue switchTimer = new NumberValue(this, "Switch Timer", "switch", 200D, 150D, 500D, 25D);
	
	public Aura() {
		super("Aura", "Attacks entities around you", Keyboard.KEY_R, Category.COMBAT);
	}
	
	@EventTarget
	public void onPreUpdate(EventMotionUpdate event) {
		if (event.type == EventType.PRE) {
			this.suffix = mode.getValue();
            getAuraMode().onPreUpdate(event);
			if (lockview.getValue() && attacking) {
				event.yaw += (float) ((Math.random() - 0.5) * randYaw.getValue().doubleValue());
				event.pitch += (float) ((Math.random() - 0.5) * randPitch.getValue().doubleValue());
				mc.player.rotationYaw = event.yaw;
				mc.player.rotationPitch = event.pitch;
			}
		}
	}
	
	@EventTarget
	public void onPostUpdate(EventMotionUpdate event) {
		if (event.type == EventType.POST) {
            getAuraMode().onPostUpdate(event);
		}
	}
	
	@EventTarget
	public void onReceivePacket(EventPacketReceive event) {
		if (event.getPacket() instanceof SPacketEntityEquipment) {
			SPacketEntityEquipment packet = (SPacketEntityEquipment) event.getPacket();
			if (packet.getEquipmentSlot().getIndex() == 1 && packet.getItemStack() == null) {
				if (mc.player.getHeldItemOffhand() != null && mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) {
					if (mc.player.getHeldItemMainhand() != null && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
						if (attacking && autoblock.getValue()) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	public List<EntityLivingBase> sort(List<EntityLivingBase> entities) {
		if (priority.getValue().equalsIgnoreCase("Distance")) {
			entities.sort((target1, target2) -> {
				double distance1 = target1.getDistance(mc.player);
				double distance2 = target2.getDistance(mc.player);
				return distance1 < distance2 ? -1 : distance1 == distance2 ? 0 : 1;
			});
		} else if (priority.getValue().equalsIgnoreCase("Health")) {
			entities.sort((target1, target2) -> {
				double hp1 = target1.getHealth();
				double hp2 = target2.getHealth();
				return hp1 < hp2 ? -1 : hp1 == hp2 ? 0 : 1;
			});
		} else if (priority.getValue().equalsIgnoreCase("Smart")) {
            entities.sort((target1, target2) -> {
                double distance1 = RotationUtils.angleDistanceYawNoAbs(target1);
                double distance2 = RotationUtils.angleDistanceYawNoAbs(target2);
                return distance1 < distance2 ? 1 : distance1 == distance2 ? 0 : -1;
            });
        }
        return entities;
	}
	
	public AuraMode getAuraMode() {
		if (mode.getValue().equalsIgnoreCase("Single")) {
			return singleMode;
		} else if (mode.getValue().equalsIgnoreCase("Switch")) {
			return switchMode;
		} else if (mode.getValue().equalsIgnoreCase("Tick")) {
			return tickMode;
		} else if (mode.getValue().equalsIgnoreCase("Delay")) {
			return delayMode;
		} else if (mode.getValue().equalsIgnoreCase("Multi")) {
			return multiMode;
		} else {
			return cancerMode;
		}
	}
	
	private class AuraEntityFilter extends EntityFilter {
		
		@Override
		public boolean customCheck(Entity ent) {
			if (ent.ticksExisted < alive.getValue().intValue()) {
				return false;
			}
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
			return walls.getValue();
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
