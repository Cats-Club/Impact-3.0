package me.zero.clarinet.mod.misc;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.game.EventMiddleClick;
import me.zero.clarinet.manager.manager.FriendManager;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

public class MCF extends Mod {
	
	public MCF() {
		super("MCF", "You can middle click friends!", 0, Category.MISC);
	}
	
	@EventTarget
	public void onMiddleClick(EventMiddleClick event) {
		if ((mc.objectMouseOver != null) && (mc.objectMouseOver.typeOfHit == RayTraceResult.Type.ENTITY)) {
			Entity entity = mc.objectMouseOver.entityHit;
			if ((entity instanceof EntityPlayer)) {
				EntityPlayer player = (EntityPlayer) entity;
				if (!Impact.getInstance().getFriendManager().isFriend(player.getName())) {
					Impact.getInstance().getFriendManager().addFriend(new FriendManager.Friend(player.getName(), player.getName()));
				} else {
					Impact.getInstance().getFriendManager().removeFriend(player.getName());
				}
			}
		}
	}
}
