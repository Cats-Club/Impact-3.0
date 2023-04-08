
package me.zero.clarinet.mod.minigame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import me.zero.clarinet.util.render.ColorUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventManager;
import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.EventType;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.player.EventMotionUpdate;
import me.zero.clarinet.event.render.EventNameprotect;
import me.zero.clarinet.event.render.EventNametag;
import me.zero.clarinet.event.render.EventRender2D;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.mod.misc.AntiBot;
import me.zero.clarinet.util.MathUtils;
import me.zero.clarinet.util.RotationUtils;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

public class Minestrike extends Mod {
	
	private EntityPlayer target = null;

    private boolean isSpec = false;
	
	private String targetMessage = "";
	
	private float shootTime = 0;

	private float offsetPitch = 0;

	private final String SWAT = "b.";
	
	private final String BOMBERS = "c.";
	
	private MultiValue<String> mode = new MultiValue<String>(this, "Mode", "mode", "Silent", new String[] { "Flusha", "Silent", "Legit" });
	private MultiValue<String> bone = new MultiValue<String>(this, "Bone", "bone", "Head", new String[] { "Head", "Chest", "Legs" });
	
	private BooleanValue autoshoot = new BooleanValue(this, "AutoShoot", "autoshoot", true);
	private BooleanValue aimbot = new BooleanValue(this, "Aimbot", "aimbot", true);
	private BooleanValue recoil = new BooleanValue(this, "Recoil", "recoil");
	private BooleanValue derp = new BooleanValue(this, "Derp", "derp");
	private BooleanValue nametags = new BooleanValue(this, "Nametags", "nametags", true);
	private BooleanValue esp = new BooleanValue(this, "ESP", "esp", true);
	private BooleanValue items = new BooleanValue(this, "Item ESP", "items", true);
	private BooleanValue tracers = new BooleanValue(this, "Tracers", "tracers", true);
	private BooleanValue display = new BooleanValue(this, "Target Display", "display", true);
	
	public NumberValue fov = new NumberValue(this, "FOV", "fov", 360D, 30D, 360D, 1D);
	
	public Minestrike() {
		super("Minestrike", "Aimbot and ESP for minestrike", Keyboard.KEY_NONE, Category.MINIGAME);
		
		Gun.register();
	}
	
	@Override
	public void onEnable() {
		targetMessage = "Target | NONE";
	}
	
	@Override
	public void onDisable() {
		target = null;
	}
	
	@EventTarget
	public void onUpdate(EventMotionUpdate event) {
		if (!aimbot.getValue()) {
			target = null;
			return;
		}
		if (event.type == EventType.POST) {
			if (autoshoot.getValue() && target != null) {
				((IMinecraft) mc).callRightClickMouse();
			}
		}

		this.suffix = aimbot.getValue() ? mode.getValue() : "";

		((IMinecraft) mc).setRightClickDelayTimer(0);
		if (mc.player.getTeam() != null) {
			if (mc.player.getTeam().getName().equalsIgnoreCase("SPEC") || (!mc.player.getTeam().getName().startsWith(SWAT) && !mc.player.getTeam().getName().startsWith(BOMBERS))) {
				isSpec = true;
				target = null;
			} else {
				isSpec = false;
			}
		}

		if (isSpec) {
			target = null;
			targetMessage = "Spectating";
			return;
		}
		if (target != null) {
			if (target != mc.player && !target.isDead && !target.isInvisible() && target.canEntityBeSeen(mc.player) && target.isEntityAlive() && !target.isPlayerSleeping()) {
				float[] angles = faceEntity(target);
				if (mode.getValue().equalsIgnoreCase("Flusha") || mode.getValue().equalsIgnoreCase("Legit")) {
					mc.player.rotationYaw = angles[0];
					mc.player.rotationPitch = angles[1];
				} else if (mode.getValue().equalsIgnoreCase("Silent")) {
					if (mc.gameSettings.keyBindUseItem.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown() || autoshoot.getValue()) {
						event.yaw = angles[0];
						event.pitch = angles[1];
					}
				}
				targetMessage = target.getName();
			} else {
				target = null;
			}
		}
		if (target == null) {
			targetMessage = "No Target";
			if (derp.getValue() && !mc.gameSettings.keyBindUseItem.isKeyDown() && Gun.getGun(mc.player.getHeldItemMainhand()) != null && Gun.getGun(mc.player.getHeldItemMainhand()).isGun()) {
				event.yaw = MathUtils.randInt(-360, 360);
				event.pitch = MathUtils.randInt(-360, 360);
			}
			offsetPitch = 0;
			shootTime = 0;
			List<EntityPlayer> players = new ArrayList<>();
			for (Entity e : mc.world.loadedEntityList) {
				if (e instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) e;
					if (canRenderName(player)) {
						players.add(player);
					}
				}
			}
			players.sort((target1, target2) -> {
				double distance1 = target1.getDistance(mc.player);
				double distance2 = target2.getDistance(mc.player);
				return distance1 < distance2 ? 1 : distance1 == distance2 ? 0 : -1;
			});
			for (EntityPlayer player : players) {
				if ((player != mc.player) && (!player.isDead) && (!player.isInvisible()) && (player.canEntityBeSeen(mc.player)) && (player.isEntityAlive()) && (!player.isPlayerSleeping())) {
					if (player.getTeam() != null && mc.player.getTeam() != null) {
                        if (Impact.getInstance().getModManager().get(AntiBot.class).isNPC(player)) {
                            continue;
						}
						if (!sameTeam(player)) {
							if (RotationUtils.angleDistanceTo(player) <= fov.getValue().floatValue()) {
								target = player;
							} else {
								target = null;
							}
						}
					}
				}
			}
		}
	}
	
	private boolean sameTeam(EntityPlayer player) {
		if (player.getTeam() != null && mc.player.getTeam() != null) {
			String n1 = player.getTeam().getName();
			String n2 = mc.player.getTeam().getName();
			if ((n1.startsWith(SWAT) && n2.startsWith(SWAT)) || (n1.startsWith(BOMBERS) && n2.startsWith(BOMBERS))) {
				return true;
			}
		}
		return false;
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		for (Object o : mc.world.getLoadedEntityList()) {
			if (o instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) o;
				if ((player != mc.player) && (!player.isDead) && (!player.isInvisible()) && (player.isEntityAlive()) && (!player.isPlayerSleeping())) {
					if (player.getTeam() != null) {
						if (player.getTeam().getName().startsWith(SWAT)) {
							drawFriendly(player, event.partialTicks);
						} else if (player.getTeam().getName().startsWith(BOMBERS)) {
							drawEnemy(player, event.partialTicks);
						}
					}
				}
			} else if (o instanceof EntityItem) {
				EntityItem item = (EntityItem) o;
				double xPos = (item.lastTickPosX + (item.posX - item.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
				double yPos = (item.lastTickPosY + (item.posY - item.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
				double zPos = (item.lastTickPosZ + (item.posZ - item.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
				Float red = null;
				Float green = null;
				Float blue = null;
				if (Item.getIdFromItem(item.getItem().getItem()) == 283) {
					red = 1.0f;
					green = 0.0f;
					blue = 1.0f;
				}
				if (Item.getIdFromItem(item.getItem().getItem()) == 392) {
					red = 0.6f;
					green = 0.6f;
					blue = 0.6f;
				}
				if (Item.getIdFromItem(item.getItem().getItem()) == 319) {
					red = 1.0f;
					green = 0.4f;
					blue = 0.0f;
				}
				if (Item.getIdFromItem(item.getItem().getItem()) == 391) {
					red = 1.0f;
					green = 1.0f;
					blue = 1.0f;
				}
				if (Item.getIdFromItem(item.getItem().getItem()) == 260) {
					red = 0.0f;
					green = 0.3f;
					blue = 0.0f;
				}
				if (red != null && green != null && blue != null && items.getValue()) {
					RenderUtils.renderEntityItem(item, red, green, blue, xPos, yPos, zPos, true);
					GL11.glColor4f(red, green, blue, 1.0F);
					// RenderUtils.drawTracer(item, event.partialTicks);
				}
			}
		}
		if (!nametags.getValue()) {
			return;
		}
		List<EntityPlayer> players = new ArrayList<>();
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) e;
				if (canRenderName(player) && !player.isInvisible()) {
					players.add(player);
				}
			}
		}
		players.sort((target1, target2) -> {
			double distance1 = target1.getDistance(mc.player);
			double distance2 = target2.getDistance(mc.player);
			return distance1 < distance2 ? 1 : distance1 == distance2 ? 0 : -1;
		});
		FontRenderer fontRenderer = mc.fontRenderer;
		for (EntityPlayer player : players) {
			double renderX = (player.lastTickPosX + (player.posX - player.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
			double renderY = (player.lastTickPosY + (player.posY - player.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
			double renderZ = (player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
			float scale = player.getDistance(Minecraft.getMinecraft().player) / 8F;
			scale = (float) Math.max(1.6, scale);
			scale /= 60F;
			String name = player.getName();
			EventNameprotect event2 = new EventNameprotect(name);
			EventManager.call(event2);
			String color = TextFormatting.WHITE + "";
			if (player.getTeam().getName().startsWith(SWAT)) {
				color += TextFormatting.AQUA;
			} else if (player.getTeam().getName().startsWith(BOMBERS)) {
				color += TextFormatting.RED;
			}
			name = color + event2.getMessage();
			GlStateManager.pushMatrix();
			float yOffset = scale * mc.fontRenderer.FONT_HEIGHT + 0.3F;
			GlStateManager.translate((float) renderX + 0.0F, (float) renderY + player.height + yOffset, (float) renderZ);
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
			GlStateManager.scale(-scale, -scale, scale);
			GlStateManager.disableLighting();
			GlStateManager.depthMask(false);
			GlStateManager.disableDepth();
			String sneak = player.isSneaking() ? TextFormatting.RED + " S" : "";
			name += sneak + " " + TextFormatting.RESET + (int) (player.getHealth() / player.getMaxHealth() * 100.0) + "%";
			String gun = Gun.getName(player.getHeldItemMainhand());
			int stringWidth = fontRenderer.getStringWidth(name) / 2;
			stringWidth = Math.max(stringWidth, fontRenderer.getStringWidth(gun) / 2);
			int border = 0xFF000000;
			if (((mc.gameSettings.keyBindUseItem.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) && !mode.getValue().equalsIgnoreCase("Silent") && target != null) || (mode.getValue().equalsIgnoreCase("Flusha") && target != null) || (autoshoot.getValue() && target != null)) {
				if (player == target) {
					border = 0xFFC80000;
				}
			}
			RenderUtils.rectangleBordered(-stringWidth - 2, -12D, stringWidth + 2, 9.5D, 1, border, 0xC8000000);
			fontRenderer.drawStringWithShadow(gun, -fontRenderer.getStringWidth(gun) / 2, -10, 0xFFFFFFFF);
			fontRenderer.drawStringWithShadow(name, -fontRenderer.getStringWidth(name) / 2, 0, ColorUtils.getHealthRGBI(player));
			GlStateManager.enableDepth();
			GlStateManager.depthMask(true);
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		ScaledResolution sr = new ScaledResolution(mc);
		if (display.getValue()) {
			int y = 20;
			int border = 0xC8FFFFFF;
			if (((mc.gameSettings.keyBindUseItem.isKeyDown() || mc.gameSettings.keyBindSneak.isKeyDown()) && !mode.getValue().equalsIgnoreCase("Silent") && target != null) || (mode.getValue().equalsIgnoreCase("Flusha") && target != null) || (autoshoot.getValue() && target != null)) {
				border = 0xC8FF0000;
			}
			RenderUtils.rectangleBordered((sr.getScaledWidth() / 2) - (mc.fontRenderer.getStringWidth(targetMessage) / 2) - 3, (sr.getScaledHeight() / 2) - y, (sr.getScaledWidth() / 2) + (mc.fontRenderer.getStringWidth(targetMessage) / 2) + 2, (sr.getScaledHeight() / 2) - (y - 12), border, 0x60080808);
			mc.fontRenderer.drawString(targetMessage, (sr.getScaledWidth() - mc.fontRenderer.getStringWidth(targetMessage)) / 2, (sr.getScaledHeight() / 2) - (y - 2), new Color(200, 200, 200).getRGB());
		}
	}
	
	@EventTarget
	private void nametagEvent(EventNametag event) {
		if (canRenderName(event.entity) && nametags.getValue()) {
			event.setCancelled(true);
		}
	}
	
	private boolean canRenderName(Entity entity) {
		if (entity instanceof EntityPlayerSP) {
			return false;
		}
		if (entity.getTeam() != null) {
			if (entity.getTeam().getName().startsWith(SWAT) || entity.getTeam().getName().startsWith(BOMBERS)) {
				return true;
			}
		}
		return false;
	}
	
	private void drawFriendly(EntityPlayer player, float partialTicks) {
		float red = 85F / 255F;
		float green = 1F;
		float blue = 1F;
		GL11.glPushMatrix();
		if (esp.getValue()) {
			RenderUtils.drawEntityESP(player, red, green, blue, partialTicks);
		}
		if (tracers.getValue()) {
			GL11.glColor4f(red, green, blue, 1.0F);
			GL11.glLineWidth(1);
			RenderUtils.drawTracer(player, partialTicks);
		}
		GL11.glPopMatrix();
	}
	
	private void drawEnemy(EntityPlayer player, float partialTicks) {
		float red = 1F;
		float green = 85F / 255F;
		float blue = 85F / 255F;
		GL11.glPushMatrix();
		if (esp.getValue()) {
			RenderUtils.drawEntityESP(player, red, green, blue, partialTicks);
		}
		if (tracers.getValue()) {
			GL11.glColor4f(red, green, blue, 1.0F);
			GL11.glLineWidth(1);
			RenderUtils.drawTracer(player, partialTicks);
		}
		GL11.glPopMatrix();
	}
	
	private float[] faceEntity(EntityLivingBase e) {
		float amount = 1;
		if (bone.getValue().equalsIgnoreCase("Head")) {
			amount = e.isSneaking() ? 0.65F : 0.85F;
		} else if (bone.getValue().equalsIgnoreCase("Chest")) {
			amount = 0.5F;
		} else if (bone.getValue().equalsIgnoreCase("Legs")) {
			amount = 0.3F;
		}
		if (mode.getValue().equalsIgnoreCase("Legit")) {
			double distance = mc.player.getDistance(e);
			double multiplier = 1.9 * Math.max((distance / 4.0) + 2.0, 1.0);
			double predictedX = e.posX + ((e.posX - e.lastTickPosX) * multiplier);
			double predictedY = e.posY + ((e.posY - e.lastTickPosY) * multiplier);
			double predictedZ = e.posZ + ((e.posZ - e.lastTickPosZ) * multiplier);
			double diffX = predictedX - mc.player.posX;
			double diffY = predictedY + e.getEyeHeight() * amount - (mc.player.posY + mc.player.getEyeHeight());
			double diffZ = predictedZ - mc.player.posZ;
			double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
			float lyaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
			float lpitch = (float) (-Math.atan2(diffY, dist) * 180.0D / Math.PI);
			float[] rotations = { mc.player.rotationYaw + MathHelper.wrapDegrees(lyaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(lpitch - mc.player.rotationPitch) };
			float yaw = rotations[0];
			yaw = RotationUtils.limitAngleChange(mc.player.rotationYaw, yaw, Math.abs(mc.player.rotationYaw - yaw) / 4.0F);
			return new float[] { yaw, mc.player.rotationPitch };
		} else {
			double distance = mc.player.getDistance(e);
			double multiplier = 1.9 * Math.max((distance / 6.0) + 2.0, 1.0);
			double cx = e.posX + ((e.posX - e.lastTickPosX) * multiplier);
			double cz = e.posZ + ((e.posZ - e.lastTickPosZ) * multiplier);
			double diffX = cx - mc.player.posX;
			double diffY = e.posY + e.getEyeHeight() * amount - (mc.player.posY + mc.player.getEyeHeight());
			double diffZ = cz - mc.player.posZ;
			double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
			float lyaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
			float lpitch = (float) (-Math.atan2(diffY, dist) * 180.0D / Math.PI);
			float[] rotations = { mc.player.rotationYaw + MathHelper.wrapDegrees(lyaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(lpitch - mc.player.rotationPitch) };
			if (mc.gameSettings.keyBindUseItem.isKeyDown() || autoshoot.getValue()) {
				shootTime++;
			} else {
				shootTime = 0;
			}
			distance *= 3.5F;// 4.5F
			boolean isScoped = false;
			offsetPitch = (shootTime * 5 * (float) distance) / 190 * ((Gun.getRecoil(mc.player.getHeldItemMainhand()) * (isScoped ? 0.7F : 1.0F)));
			float yaw = rotations[0];
			offsetPitch = Math.min(offsetPitch, recoil.getValue() ? 15 : 0);
			float pitch = rotations[1] + offsetPitch - 1.0F;
			return new float[] { yaw, pitch };
		}
	}
	
	private static class Gun {
		
		private static ArrayList<Gun> guns = new ArrayList<Gun>();
		
		private static Gun NONE = new Gun("NONE");
		
		private boolean isSkin = false;
		
		private boolean isGun = true;
		
		private String name;
		
		private float recoil;
		
		private int id, subID;

        private Gun(String name) {
			this(name, 0);
		}

        private Gun(String name, int id) {
			this(name, 0, id);
			this.isGun = false;
		}

        private Gun(String name, float recoil, int id) {
			this.name = name;
			this.recoil = recoil;
			this.id = id;
		}

        private Gun(String name, float recoil, int id, int subID) {
			this(name, recoil, id);
			this.subID = subID;
			this.isSkin = true;
		}

        private Gun deriveSkin(int subID) {
			return new Gun(name, recoil, 351, subID);
		}

        private Gun deriveSkin2(int id) {
			return new Gun(name, recoil, id);
		}

        private String getName() {
			return this.name;
		}

        private float getRecoil() {
			return this.recoil;
		}

        private int getID() {
			return this.id;
		}

        private int getSubID() {
			return this.subID;
		}

        private boolean isSkin() {
			return this.isSkin;
		}

        private boolean isGun() {
			return this.isGun;
		}

        private static Gun getGun(String name) {
			for (Gun gun : guns) {
				if (gun.name.equalsIgnoreCase(name)) {
					return gun;
				}
			}
			return NONE;
		}

        private static Gun getGun(ItemStack stack) {
			if (stack != null && stack.getItem() != null) {
				Item item = stack.getItem();
				int id = Item.getIdFromItem(item);
				for (Gun gun : guns) {
					if (gun.id == id) {
						if (gun.isSkin) {
							int subID = stack.getItemDamage();
							if (subID == gun.subID) {
								return gun;
							}
						} else {
							return gun;
						}
					}
				}
			}
			return NONE;
		}

        private static float getRecoil(ItemStack stack) {
			return getGun(stack).getRecoil();
		}

        private static String getName(ItemStack stack) {
			return getGun(stack).getName();
		}

        private static void register() {
			guns.clear();
			
			// Assault Rifles
			guns.add(new Gun("FAMAS", 0.048F, 270));
			guns.add(new Gun("AK-47", 0.044F, 269));
			guns.add(new Gun("M4A4", 0.040F, 273));
			guns.add(new Gun("Galil", 0.052F, 274));
			guns.add(new Gun("AUG", 0.032F, 285));
			guns.add(new Gun("SG 553", 0.045F, 257));
			guns.add(getGun("FAMAS").deriveSkin2(337));
			guns.add(getGun("AK-47").deriveSkin(7));
			guns.add(getGun("M4A4").deriveSkin(11));
			guns.add(getGun("Galil").deriveSkin(10));
			guns.add(getGun("AUG").deriveSkin2(369));
			guns.add(getGun("SG 553").deriveSkin(5));
			
			// Pistols
			guns.add(new Gun("Glock 18", 0.048F, 291));
			guns.add(new Gun("P2000", 0.03F, 290));
			guns.add(new Gun("P250", 0.024F, 293));
			guns.add(new Gun("Desert Eagle", 0.024F, 294));// 0.096F
			guns.add(new Gun("CZ-75", 0.048F, 292));
			guns.add(getGun("Glock 18").deriveSkin(9));
			guns.add(getGun("P2000").deriveSkin(6));
			guns.add(getGun("P250").deriveSkin(3));
			guns.add(getGun("Desert Eagle").deriveSkin2(372));
			guns.add(getGun("CZ-75").deriveSkin2(336));
			
			// Snipers
			guns.add(new Gun("SSG 08", 0.036F, 278));
			guns.add(new Gun("AWP", 0.036F, 284));
			guns.add(getGun("SSG 08").deriveSkin(12));
			guns.add(getGun("AWP").deriveSkin2(289));
			
			// SMGs
			guns.add(new Gun("PP Bizon", 0.032F, 271));
			guns.add(new Gun("P90", 0.024F, 275));
			guns.add(getGun("PP Bizon").deriveSkin(4));
			guns.add(getGun("P90").deriveSkin(0));
			
			// Shotguns
			guns.add(new Gun("XM1014", 0.0F, 279));
			guns.add(new Gun("Nova", 0.0F, 286));
			guns.add(getGun("XM1014").deriveSkin2(264));
			guns.add(getGun("Nova").deriveSkin(14));
			
			// Grenades
			guns.add(new Gun("HE Grenade", 260));
			guns.add(new Gun("Smoke Grenade", 392));
			guns.add(new Gun("Fire Grenade", 319));
			guns.add(new Gun("C4 Bomb", 283));
			guns.add(new Gun("Flashbang", 391));
			guns.add(new Gun("Molotov", 320));
			
			// Misc
			guns.add(new Gun("Defusal Kit", 359));
			guns.add(new Gun("Knife", 0.0F, 258));
			guns.add(new Gun("Knife", 0.0F, 267));
			guns.add(new Gun("M9 Bayonet", 0.0F, 276));
			guns.add(new Gun("C4 Bomb", 0.0F, 151));
		}
	}
}
