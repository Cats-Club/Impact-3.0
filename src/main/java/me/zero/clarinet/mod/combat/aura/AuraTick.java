package me.zero.clarinet.mod.combat.aura;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.mod.combat.Criticals;
import me.zero.clarinet.util.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class AuraTick extends AuraMode {

	private List<EntityLivingBase> entities = new ArrayList<>();
	
	private EntityLivingBase currentTarget;
	
	private TimerUtil timer = new TimerUtil();
	
	private int curCPS;
	
	public AuraTick(Aura aura) {
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
		stopBlock();
		if (entities.size() > 0 && currentTarget != null) {
			if (timer.delay(curCPS) || curCPS == 0) {
                ClientUtils.swap(9, mc.player.inventory.currentItem);
                attack(currentTarget, false);
                attack(currentTarget, false);
                attack(currentTarget, true);
                ClientUtils.swap(9, mc.player.inventory.currentItem);
                attack(currentTarget, false);
                attack(currentTarget, true);
				timer.reset();

                curCPS = MathUtils.randInt(475, 550);
			}
		}
		startBlock();
	}

    private void attack(Entity ent, boolean crit) {
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.player.connection.sendPacket(new CPacketUseEntity(ent));
        if (crit) {
            Impact.getInstance().getModManager().get(Criticals.class).crit();
        }
    }
}
