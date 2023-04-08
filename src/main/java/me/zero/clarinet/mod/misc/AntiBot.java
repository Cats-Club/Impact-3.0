package me.zero.clarinet.mod.misc;

import me.zero.clarinet.mixin.Fields;
import org.lwjgl.input.Keyboard;

import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.MathUtils;
import me.zero.values.types.BooleanValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class AntiBot extends Mod {
	
	public AntiBot() {
		super("AntiBot", "Prevents the player from attacking GWEN, Watchdog, and AAC entities", Keyboard.KEY_NONE, Category.MISC);
	}
	
	public boolean isNPC(EntityPlayer player) {
        if (player == null) return false;

		if (player instanceof EntityPlayerSP) return false;

		boolean watchdog = player.ticksExisted <= 130;
		boolean gwen = !Fields.didSwingEntityPlayer;

		return gwen || watchdog;
	}
}
