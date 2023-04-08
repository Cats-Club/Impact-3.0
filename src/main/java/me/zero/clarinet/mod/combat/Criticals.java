package me.zero.clarinet.mod.combat;

import org.lwjgl.input.Keyboard;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.event.player.EventAttackEntity;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.BlockUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.CPacketPlayer;

public class Criticals extends Mod {
	
	public Criticals() {
		super("Criticals", "Always get a Critical Hit on your Target", Keyboard.KEY_NONE, Category.COMBAT);
	}
	
	@EventTarget
	public void onAttackEntity(EventAttackEntity event) {
		if (event.type == EventType.PRE) {
			if (event.entity instanceof EntityLivingBase) {
                crit();
			}
		}
	}

	public void crit() {
        if (mc.player.isInWater() || mc.player.isInsideOfMaterial(Material.LAVA) || !mc.player.onGround || BlockUtils.isOnLiquid()) {
            return;
        }
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0624D, mc.player.posZ, true));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.11E-4D, mc.player.posZ, false));
        mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
    }
}
