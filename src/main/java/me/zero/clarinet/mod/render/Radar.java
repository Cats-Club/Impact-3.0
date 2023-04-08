package me.zero.clarinet.mod.render;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventRender2D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class Radar extends Mod {
	
	private MultiValue<String> mode = new MultiValue<String>(this, "Mode", "mode", "Static", new String[] { "Static", "Dynamic" });
	
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible");
	
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping");
	
	private BooleanValue players = new BooleanValue(this, "Players", "players", true);
	
	private BooleanValue mobs = new BooleanValue(this, "Mobs", "mobs", true);
	
	private BooleanValue animals = new BooleanValue(this, "Animals", "animals", true);
	
	private BooleanValue skins = new BooleanValue(this, "Skins", "skins", true);
	
	private NumberValue alpha = new NumberValue(this, "Radar Opacity", "opacity", 75D, 25D, 100D, 1D);
	
	private NumberValue size = new NumberValue(this, "Size", "size", 100D, 75D, 125D, 1D);
	
	private NumberValue blipSize = new NumberValue(this, "Blip Size", "blip", 1D, 0.1D, 2D, 0.05D);
	
	public Radar() {
		super("Radar", "Displays a radar that shows where entities are", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget
	public void onRender(EventRender2D event) {
		this.suffix = mode.getValue();
		if (!mc.gameSettings.showDebugInfo) {
			ScaledResolution sr = new ScaledResolution(mc);
			int x = 2;
			int y = Impact.getInstance().getModManager().get(HUD.class).getY() + 2;
			int radius = (int) (size.getValue().intValue() * 0.425);
			int centerX = x + (size.getValue().intValue() / 2);
			int centerY = y + (size.getValue().intValue() / 2);
			
			// RenderUtils.rectangleBordered(x, y, x + size, y + size, 1,
			// 0xFF000000, 0xFF1F1F1F);
			int realOpacity = (int) (alpha.getValue().floatValue() * 2.55);
			RenderUtils.rectangleBordered(x, y, x + size.getValue().intValue(), y + size.getValue().intValue(), 1, RGBtoHEX(0, 0, 0, realOpacity), this.RGBtoHEX(31, 31, 31, realOpacity));
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glBlendFunc(770, 771);
			GL11.glEnable(GL11.GL_BLEND);
			
			// Entities
			for (Entity e : mc.world.loadedEntityList) {
				if (isCorrectEntity(e)) {
					double diffX = mc.player.posX - e.posX;
					double diffZ = mc.player.posZ - e.posZ;
					double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
					if (dist <= (size.getValue().intValue() / 2.0) - (size.getValue().intValue() / 50.0)) {
						
						GL11.glPushMatrix();
						
						boolean check = (e instanceof EntityPlayer && skins.getValue());
						double posX = mc.player.posX - e.posX + x + size.getValue().intValue() / 2;
						double posY = mc.player.posZ - e.posZ + y + size.getValue().intValue() / 2;
						
						GL11.glTranslated(centerX, centerY, 0);
						GL11.glRotatef(!mode.getValue().equalsIgnoreCase("static") ? -mc.player.rotationYaw : -180, 0, 0, 1);
						GL11.glTranslated(-centerX, -centerY, 0);
						
						double headScale = 12;
						if (check) {
							posX += (size.getValue().intValue() / (headScale * 2));
							posY += (size.getValue().intValue() / (headScale * 4));
						}
						
						GL11.glTranslated(posX, posY, 0);
						GL11.glRotatef((!mode.getValue().equalsIgnoreCase("Static") ? mc.player.rotationYaw + 180 : 0) + 180, 0, 0, 1);
						GL11.glTranslated(-posX, -posY, 0);
						
						if (check) {
							GL11.glEnable(GL11.GL_TEXTURE_2D);
							RenderUtils.drawUserFace(e.getName(), posX, posY, size.getValue().intValue() / headScale, size.getValue().intValue() / headScale);
							GL11.glDisable(GL11.GL_TEXTURE_2D);
						} else {
							if (e instanceof EntityPlayer) {
								GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
							} else if (e instanceof EntityMob) {
								GL11.glColor4f(1.0F, 0.5F, 0.5F, 1.0F);
							} else if (e instanceof EntityAnimal) {
								GL11.glColor4f(0.5F, 1.0F, 0.5F, 1.0F);
							} else {
								GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
							}
							GL11.glBegin(GL11.GL_POLYGON);
							{
								GL11.glVertex2d(posX, posY + blipSize.getValue().floatValue());
								GL11.glVertex2d(posX + blipSize.getValue().floatValue(), posY);
								GL11.glVertex2d(posX, posY - blipSize.getValue().floatValue());
								GL11.glVertex2d(posX - blipSize.getValue().floatValue(), posY);
							}
							GL11.glEnd();
						}
						GL11.glPopMatrix();
					}
				}
			}
			
			// The Player
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPushMatrix();
			GL11.glTranslatef(centerX, centerY, 0);
			GL11.glRotatef(mode.getValue().equalsIgnoreCase("static") ? mc.player.rotationYaw : 180, 0, 0, 1);
			GL11.glTranslatef(-centerX, -centerY, 0);
			GL11.glBegin(GL11.GL_POLYGON);
			{
				GL11.glVertex2d(centerX, centerY + 3);
				GL11.glVertex2d(centerX + 1.5, centerY - 3);
				GL11.glVertex2d(centerX - 1.5, centerY - 3);
			}
			GL11.glEnd();
			GL11.glPopMatrix();
			
			// North, South, East and West
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			Object[][] memes = { { "N", -90.0 }, { "S", 90.0 }, { "E", 0.0 }, { "W", 180.0 } };
			for (Object[] meme : memes) {
				if (meme.length < 2) {
					return;
				}
				if (meme[0] instanceof String) {
					if (meme[1] instanceof Double) {
						String s = (String) meme[0];
						Double i = (Double) meme[1];
						if (!mode.getValue().equalsIgnoreCase("Static")) {
							i -= mc.player.rotationYaw;
							i -= 180;
						}
						mc.fontRenderer.drawStringWithShadow(s, (float) (centerX + (radius * Math.cos(Math.toRadians(i)))), (float) (centerY + (radius * Math.sin(Math.toRadians(i)))) - mc.fontRenderer.FONT_HEIGHT / 2, 0xFFFFFFFF);
					}
				}
			}
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
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
	
	public int RGBtoHEX(final int r, final int g, final int b, final int alpha) {
		return (alpha << 24) + (r << 16) + (g << 8) + b;
	}
}