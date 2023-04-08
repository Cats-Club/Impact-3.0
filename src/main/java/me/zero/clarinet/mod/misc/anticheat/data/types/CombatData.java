package me.zero.clarinet.mod.misc.anticheat.data.types;

import me.zero.clarinet.mod.misc.anticheat.data.CheckData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class CombatData extends CheckData {

    private EntityLivingBase target;

    public CombatData(EntityPlayer player, EntityLivingBase target) {
        super(player);
        this.target = target;
    }

    public EntityLivingBase getTarget() {
        return this.target;
    }
}
