package me.zero.clarinet.mod.combat.aura;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.TimerUtil;
import net.minecraft.entity.EntityLivingBase;

public class AuraSwitch extends AuraMode {
	
	private boolean setupTick;
	private boolean switchingTargets;
	private List<EntityLivingBase> targets;
	private int index;
	private TimerUtil timer;
	public static TimerUtil potTimer = new TimerUtil();
	
	public AuraSwitch(Aura aura) {
		super(aura);
		this.targets = new ArrayList<EntityLivingBase>();
		this.timer = new TimerUtil();
	}
	
	@Override
	public void onPreUpdate(EventMotionUpdate event) {
		if (timer.delay(300.0F) || targets.get(index) == null || !targets.get(index).isEntityAlive() || mc.player.getDistance(targets.get(index)) > aura.range.getValue().doubleValue()) {
			targets = CombatUtils.getCloseEntities(aura.range, aura.fov, aura.filter);
            targets = aura.sort(targets);
			aura.attacking = targets.size() > 0;
		}
		if (index >= this.targets.size()) {
			index = 0;
		}
		if (targets.size() > 0) {
			EntityLivingBase target = this.targets.get(this.index);
			if (target != null) {
				float[] angles = RotationUtils.getRotations(target);
				event.yaw = aura.currentAngles[0] = angles[0];
				event.pitch = aura.currentAngles[1] = angles[1];
			}
			if (setupTick) {
				if (timer.delay(aura.switchTimer)) {
					incrementIndex();
					switchingTargets = true;
					timer.reset();
				}
			} else {
				if (mc.player.fallDistance > 0.0f && mc.player.fallDistance < 0.66) {
					event.onGround = true;
				}
			}
		}
		setupTick = !this.setupTick;
	}
	
	@Override
	public void onPostUpdate(EventMotionUpdate event) {
		stopBlock();
		if (setupTick && targets.size() > 0 && targets.get(index) != null && targets.size() > 0) {
			EntityLivingBase target = targets.get(index);
			for (int i = 0; i < 1; ++i) {
				attackEntity(target);
			}
		}
		startBlock();
	}
	
	private void incrementIndex() {
		++this.index;
		if (this.index >= this.targets.size()) {
			this.index = 0;
		}
	}
}
