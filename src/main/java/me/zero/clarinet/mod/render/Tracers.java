package me.zero.clarinet.mod.render;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class Tracers extends Mod {
	
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible");
	
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping");
	
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);
	
	private BooleanValue rbf = new BooleanValue(this, "Rainbow Friends", "friends", true);
	
	private BooleanValue rbd = new BooleanValue(this, "Rainbow Damage", "damage");
	
	private NumberValue r = new NumberValue(this, "Red", "red", 209D, 0D, 255D, 1D);
	
	private NumberValue g = new NumberValue(this, "Green", "green", 255D, 0D, 255D, 1D);
	
	private NumberValue b = new NumberValue(this, "Blue", "blue", 253D, 0D, 255D, 1D);
	
	private NumberValue a = new NumberValue(this, "Alpha", "alpha", 255D, 0D, 255D, 1D);
	
	private NumberValue width = new NumberValue(this, "Line Width", "width", 1D, 0D, 3D, 0.25D);
	
	public Tracers() {
		super("Tracers", "Draws lines to entities", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		for (Object o : mc.world.getLoadedEntityList()) {
			if (o instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase) o;
				if (isCorrectEntity(entity)) {
					float[] colors = getESPColorF(entity);
					float red = colors[0];
					float green = colors[1];
					float blue = colors[2];
					float alpha = colors[3];
					GL11.glLineWidth(Math.max(width.getValue().floatValue(), 0.01F));
					GL11.glColor4f(red, green, blue, alpha);
					RenderUtils.drawTracer(entity, event.partialTicks);
				}
			}
		}
	}
	
	public boolean isCorrectEntity(Object o) {
		if (!(o instanceof Entity)) {
			return false;
		}
		Entity ent = (Entity) o;
		if (ent == mc.player) {
			return false;
		}
		if (ent.isInvisible() && !invisible.getValue()) {
			return false;
		}
		if (o instanceof EntityPlayer) {
			if (players.getValue()) {
				EntityPlayer player = (EntityPlayer) o;
				if (player.isPlayerSleeping() && !sleeping.getValue()) {
					return false;
				}
				return true;
			}
		} else if (o instanceof EntityMob) {
			if (mobs.getValue()) {
				return true;
			}
		} else if (o instanceof EntityAnimal) {
			if (animals.getValue()) {
				return true;
			}
		}
		return false;
	}
	
	public int getESPColorH(Entity ent) {
		float[] color = getESPColorF(ent);
		return new Color(color[0], color[1], color[2], color[3]).getRGB();
	}
	
	public float[] getESPColorF(Entity ent) {
		float red = r.getValue().floatValue() / 255F;
		float green = g.getValue().floatValue() / 255F;
		float blue = b.getValue().floatValue() / 255F;
		if (ent instanceof EntityLivingBase) {
			EntityLivingBase elb = (EntityLivingBase) ent;
			if (elb instanceof EntityPlayer) {
				if (Impact.getInstance().getFriendManager().isFriend(elb.getName())) {
					if (rbf.getValue() || (rbd.getValue() && elb.hurtTime > 0.0F)) {
						float[] color = ColorUtils.getRainbowF();
						red = color[0];
						blue = color[1];
						green = color[2];
					} else {
						int color = mc.fontRenderer.getColorCode('b');
						red = (color >> 16 & 0xFF) / 255.0F;
						green = (color >> 8 & 0xFF) / 255.0F;
						blue = (color & 0xFF) / 255.0F;
					}
				}
			}
		}
		return new float[] { red, green, blue, 1.0F };
	}
}
