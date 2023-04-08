package me.zero.clarinet.mod.combat.aura;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.TPSWatcher;
import net.minecraft.entity.EntityLivingBase;

public class AuraDelay extends AuraMode {
	
	private List<EntityLivingBase> entities = new ArrayList<>();
	
	private EntityLivingBase currentTarget;
	
	public AuraDelay(Aura aura) {
		super(aura);
	}

    @Override
	public void onPreUpdate(EventMotionUpdate event) {
		entities = CombatUtils.getCloseEntities(aura.range, aura.fov, aura.filter);
		entities = aura.sort(entities);
		aura.attacking = entities.size() > 0;
		if (entities.size() > 0) {
			currentTarget = entities.get(0);
			float[] angles = RotationUtils.getRotations(currentTarget);
			event.yaw = aura.currentAngles[0] = angles[0];
			event.pitch = aura.currentAngles[1] = angles[1];
		}
	}
	
	@Override
	public void onPostUpdate(EventMotionUpdate event) {
		this.stopBlock();
        float adjustment = -(20.0F - TPSWatcher.INSTANCE.getTPS());
		if (entities.size() > 0 && currentTarget != null) {
			if (mc.player.getCooledAttackStrength(aura.tpsSync.getValue() ? adjustment : 0F) >= 1F) {
				attackEntity(currentTarget);
			}
		}
		this.startBlock();
	}
}
