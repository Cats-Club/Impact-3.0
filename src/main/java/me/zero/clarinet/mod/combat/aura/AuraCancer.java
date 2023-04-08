package me.zero.clarinet.mod.combat.aura;

import java.util.List;

import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.mod.combat.Aura;
import me.zero.clarinet.util.BlockUtils;
import me.zero.clarinet.util.CombatUtils;
import me.zero.clarinet.util.TimerUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;

public class AuraCancer extends AuraMode {
	
	private TimerUtil timer = new TimerUtil();
	
	public AuraCancer(Aura aura) {
		super(aura);
	}
	
	@Override
	public void onPreUpdate(EventMotionUpdate event) {
		if (mc.world != null) {
			List<EntityLivingBase> entities = CombatUtils.getCloseEntities(100, aura.fov.getValue().intValue(), aura.filter);
			entities = aura.sort(entities);
			aura.attacking = entities.size() > 0;
			double lastX = 0;
			double lastY = 0;
			double lastZ = 0;
			if (entities.size() > 0) {
				event.yaw = 90;
			}
			for (EntityLivingBase ent : entities) {
				stopBlock();
				double x = ent.posX;
				double y = ent.posY + ent.height;
				double z = ent.posZ;
				if (BlockUtils.getBlock(new BlockPos(x, y, z)) instanceof BlockAir && BlockUtils.getBlock(new BlockPos(x, y + 1, z)) instanceof BlockAir) {
					mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(x, y, z, mc.player.rotationYaw, 90, true));
					lastX = x;
					lastY = y;
					lastZ = z;
					attackEntity(ent);
				}
				startBlock();
			}
		}
	}
	
	@Override
	public void onPostUpdate(EventMotionUpdate event) {}
}
