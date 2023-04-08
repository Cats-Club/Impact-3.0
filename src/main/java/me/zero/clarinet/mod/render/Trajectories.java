package me.zero.clarinet.mod.render;

import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import net.minecraft.init.Items;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class Trajectories extends Mod {
	
	public Trajectories() {
		super("Trajectories", "Shows where your items will land", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget
	public void renderTrajectories(EventRender3D event) {
		EntityPlayerSP player = mc.player;
		
		ItemStack stack = player.getHeldItemMainhand();
		if (stack == null) return;
		
		Item item = stack.getItem();
		if (!(item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl || (item instanceof ItemPotion && item.equals(Items.SPLASH_POTION)))) return;
		
		boolean usingBow = stack.getItem() instanceof ItemBow;
		double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks() - MathHelper.cos((float) Math.toRadians(player.rotationYaw)) * 0.16F;
		double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks() + player.getEyeHeight() - 0.1;
		double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks() - MathHelper.sin((float) Math.toRadians(player.rotationYaw)) * 0.16F;
		float arrowMotionFactor = usingBow ? 1F : 0.4F;
		float yaw = (float) Math.toRadians(player.rotationYaw);
		float pitch = (float) Math.toRadians(player.rotationPitch);
		float arrowMotionX = -MathHelper.sin(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
		float arrowMotionY = -MathHelper.sin(pitch) * arrowMotionFactor;
		float arrowMotionZ = MathHelper.cos(yaw) * MathHelper.cos(pitch) * arrowMotionFactor;
		double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
		arrowMotionX /= arrowMotion;
		arrowMotionY /= arrowMotion;
		arrowMotionZ /= arrowMotion;
		if (usingBow) {
			float bowPower = (72000 - player.getItemInUseCount()) / 20F;
			bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;
			
			if (bowPower > 1F) bowPower = 1F;
			
			if (bowPower <= 0.1F) bowPower = 1F;
			
			bowPower *= 3F;
			arrowMotionX *= bowPower;
			arrowMotionY *= bowPower;
			arrowMotionZ *= bowPower;
		} else {
			arrowMotionX *= 1.5D;
			arrowMotionY *= 1.5D;
			arrowMotionZ *= 1.5D;
		}
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glDisable(3553);
		GL11.glDisable(2929);
		GL11.glEnable(GL13.GL_MULTISAMPLE);
		GL11.glDepthMask(false);
		GL11.glLineWidth(1.8F);
		IRenderManager renderManager = (IRenderManager) mc.getRenderManager();
		double gravity = usingBow ? 0.05D : item instanceof ItemPotion ? 0.4D : 0.03D;
		Vec3d playerVector = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		float red = (1F - (float) Math.sin((float) ((System.currentTimeMillis() / 4) % 1000L) / 1000L * Math.PI * 2)) / 2F;
		float green = (1F - (float) Math.sin((float) (((System.currentTimeMillis() / 4) + 333L) % 1000L) / 1000L * Math.PI * 2)) / 2F;
		float blue = (1F - (float) Math.sin((float) (((System.currentTimeMillis() / 4) + 666L) % 1000L) / 1000L * Math.PI * 2)) / 2F;
		GL11.glColor3d(red, green, blue);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		RayTraceResult result = null;
		for (int i = 0; i < 1000; i++) {
			GL11.glVertex3d(arrowPosX - renderManager.getRenderPosX(), arrowPosY - renderManager.getRenderPosY(), arrowPosZ - renderManager.getRenderPosZ());
			arrowPosX += arrowMotionX;
			arrowPosY += arrowMotionY;
			arrowPosZ += arrowMotionZ;
			arrowMotionX *= 0.99D;
			arrowMotionY *= 0.99D;
			arrowMotionZ *= 0.99D;
			arrowMotionY -= gravity;
			
			result = mc.world.rayTraceBlocks(playerVector, new Vec3d(arrowPosX, arrowPosY, arrowPosZ));
			
			if (result != null) {
				break;
			}
		}
		
		double renderX = arrowPosX - renderManager.getRenderPosX();
		double renderY = arrowPosY - renderManager.getRenderPosY();
		double renderZ = arrowPosZ - renderManager.getRenderPosZ();
		
		if (result != null) {
			if (result.sideHit != null) {
				switch (result.sideHit) {
					case WEST:
					case EAST:
						GL11.glVertex3d(renderX, renderY, renderZ);
						GL11.glVertex3d(renderX, renderY - 0.55D, renderZ);
						GL11.glVertex3d(renderX, renderY, renderZ - 0.55D);
						GL11.glVertex3d(renderX, renderY, renderZ + 0.55D);
						GL11.glVertex3d(renderX, renderY + 0.55D, renderZ);
						GL11.glVertex3d(renderX, renderY - 0.55D, renderZ);
						GL11.glVertex3d(renderX, renderY, renderZ + 0.55D);
						GL11.glVertex3d(renderX, renderY, renderZ - 0.55D);
						GL11.glVertex3d(renderX, renderY + 0.55D, renderZ);
						break;
					case NORTH:
					case SOUTH:
						GL11.glVertex3d(renderX, renderY, renderZ);
						GL11.glVertex3d(renderX - 0.55D, renderY, renderZ);
						GL11.glVertex3d(renderX, renderY - 0.55D, renderZ);
						GL11.glVertex3d(renderX, renderY + 0.55D, renderZ);
						GL11.glVertex3d(renderX + 0.55D, renderY, renderZ);
						GL11.glVertex3d(renderX - 0.55D, renderY, renderZ);
						GL11.glVertex3d(renderX, renderY + 0.55D, renderZ);
						GL11.glVertex3d(renderX, renderY - 0.55D, renderZ);
						GL11.glVertex3d(renderX + 0.55D, renderY, renderZ);
						break;
					case DOWN:
					case UP:
						GL11.glVertex3d(renderX, renderY, renderZ);
						GL11.glVertex3d(renderX - 0.55D, renderY, renderZ);
						GL11.glVertex3d(renderX, renderY, renderZ - 0.55D);
						GL11.glVertex3d(renderX, renderY, renderZ + 0.55D);
						GL11.glVertex3d(renderX + 0.55D, renderY, renderZ);
						GL11.glVertex3d(renderX - 0.55D, renderY, renderZ);
						GL11.glVertex3d(renderX, renderY, renderZ + 0.55D);
						GL11.glVertex3d(renderX, renderY, renderZ - 0.55D);
						GL11.glVertex3d(renderX + 0.55D, renderY, renderZ);
						break;
				}
			}
		}
		GL11.glEnd();
		GL11.glDisable(3042);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(GL13.GL_MULTISAMPLE);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glPopMatrix();
	}
}
