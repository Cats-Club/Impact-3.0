package me.zero.clarinet.mod.combat.aura;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.TimerUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

public class AuraMulti extends AuraMode {
	
	private List<EntityLivingBase> entities = new ArrayList<EntityLivingBase>();
	
	private TimerUtil timer = new TimerUtil();
	
	public AuraMulti(Aura aura) {
		super(aura);
	}
	
	@Override
	public void onPreUpdate(EventMotionUpdate event) {
		entities = CombatUtils.getCloseEntities(aura.range.getValue().doubleValue(), aura.fov.getValue().intValue(), aura.filter);
		entities = aura.sort(entities);
		aura.attacking = entities.size() > 0;
		aura.currentAngles[0] = mc.player.rotationYaw;
		aura.currentAngles[1] = mc.player.rotationPitch;
	}
	
	@Override
	public void onPostUpdate(EventMotionUpdate event) {
		this.stopBlock();
		if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
			if (mc.player.getHeldItemOffhand() != null && mc.player.getHeldItemOffhand().getItem() != null && mc.player.getHeldItemOffhand().getItem() instanceof ItemShield && mc.player.getHeldItemMainhand().getItem() instanceof ItemSword) {
				mc.player.setActiveHand(EnumHand.OFF_HAND);
			}
		}
		if (entities.size() > 0) {
			if (timer.speed(aura.speed.getValue().floatValue() / 2F)) {
				for (EntityLivingBase entity : entities) {
					attackEntity(entity);
				}
				timer.reset();
			}
		}
		this.startBlock();
	}
}
