package me.zero.clarinet.util.render;

import java.awt.Color;

import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.IEntityRenderer;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import me.zero.clarinet.util.Helper;
import net.minecraft.client.renderer.BufferBuilder;
import org.lwjgl.opengl.Display;

import me.zero.clarinet.Impact;
import me.zero.clarinet.manager.manager.FontManager;
import me.zero.clarinet.ui.font.CFontRenderer;
import me.zero.clarinet.util.MathUtils;
import net.mcleaks.MCLeaks;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtils implements Helper {
	
	public static void scissor(int x, int y, int width, int height) {
		ScaledResolution sr = new ScaledResolution(mc);
		float displayScaleX = (float) mc.displayWidth / (float) sr.getScaledWidth();
		float displayScaleY = (float) mc.displayHeight / (float) sr.getScaledHeight();
		y += height;
		y = sr.getScaledHeight() - height - y;
		width = Math.max(width, 1);
		glScissor((int) (x * displayScaleX), (int) (y * displayScaleY), (int) (width * displayScaleX), (int) (height * displayScaleY));
	}

    public static void startScissor(float x1, float y1, float x2, float y2) {
        startScissor((int) x1, (int) y1, (int) x2, (int) y2);
    }
	
	public static void startScissor(int x1, int y1, int x2, int y2) {
		if (y1 > y2) {
			int temp = y2;
			y2 = y1;
			y1 = temp;
		}
		glEnable(GL_SCISSOR_TEST);
		glScissor(x1, Display.getHeight() - y2, x2 - x1, y2 - y1);
	}
	
	public static void endScissor() {
		glDisable(GL_SCISSOR_TEST);
	}
	
	public static void rotateX(double angle, double x, double y, double z) {
		glTranslated(x, y, z);
		glRotated(angle, 1, 0, 0);
		glTranslated(-x, -y, -z);
	}
	
	public static void rotateY(double angle, double x, double y, double z) {
		glTranslated(x, y, z);
		glRotated(angle, 0, 1, 0);
		glTranslated(-x, -y, -z);
	}
	
	public static void rotateZ(double angle, double x, double y, double z) {
		glTranslated(x, y, z);
		glRotated(angle, 0, 0, 1);
		glTranslated(-x, -y, -z);
	}
	
	public static void rectangle(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double var5 = left;
			left = right;
			right = var5;
		}
		if (top < bottom) {
			double var5 = top;
			top = bottom;
			bottom = var5;
		}
		float var11 = (color >> 24 & 0xFF) / 255.0F;
		float var6 = (color >> 16 & 0xFF) / 255.0F;
		float var7 = (color >> 8 & 0xFF) / 255.0F;
		float var8 = (color & 0xFF) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		worldRenderer.pos(left, bottom, 0.0D).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(right, bottom, 0.0D).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(right, top, 0.0D).color(var6, var7, var8, var11).endVertex();
		worldRenderer.pos(left, top, 0.0D).color(var6, var7, var8, var11).endVertex();
		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
	
	public static void rectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int borderColor, Side side) {
		rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
		if (!side.equals(Side.Top)) {
			rectangle(x + width, y, x1 - width, y + width, borderColor);
		}
		if (!side.equals(Side.Left)) {
			rectangle(x, y, x + width, y1, borderColor);
		}
		if (!side.equals(Side.Right)) {
			rectangle(x1 - width, y, x1, y1, borderColor);
		}
		if (!side.equals(Side.Bottom)) {
			rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
		}
	}
	
	public static void rectangleBordered(double x, double y, double x1, double y1, int borderColor, int internalColor) {
		rectangleBordered(x, y, x1, y1, 0.5, borderColor, internalColor);
	}
	
	public static void rectangleBordered(double x, double y, double x1, double y1, double width, int borderColor, int internalColor) {
		rectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
		rectangle(x + width, y, x1 - width, y + width, borderColor);
		rectangle(x, y, x + width, y1, borderColor);
		rectangle(x1 - width, y, x1, y1, borderColor);
		rectangle(x + width, y1 - width, x1 - width, y1, borderColor);
	}
	
	public static void rectangleGradient(double x1, double y1, double x2, double y2, int c1, int c2) {
		rectangleGradient(x1, y1, x2, y2, new int[] { c1, c2 });
	}
	
	private static void rectangleGradient(double x1, double y1, double x2, double y2, int[] color) {
		float[] r = new float[color.length];
		float[] g = new float[color.length];
		float[] b = new float[color.length];
		float[] a = new float[color.length];
		for (int i = 0; i < color.length; i++) {
			r[i] = ((color[i] >> 16 & 0xFF) / 255.0F);
			g[i] = ((color[i] >> 8 & 0xFF) / 255.0F);
			b[i] = ((color[i] & 0xFF) / 255.0F);
			a[i] = ((color[i] >> 24 & 0xFF) / 255.0F);
		}
		GlStateManager.disableTexture2D();
		GlStateManager.disableBlend();
		GlStateManager.disableAlpha();
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GlStateManager.blendFunc(770, 771);
		GlStateManager.shadeModel(7425);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldRenderer = tessellator.getBuffer();
		worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		if (color.length == 1) {
			worldRenderer.pos(x2, y1, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
			worldRenderer.pos(x1, y1, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
			worldRenderer.pos(x1, y2, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
			worldRenderer.pos(x2, y2, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
		} else if (color.length == 2 || color.length == 3) {
			worldRenderer.pos(x2, y1, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
			worldRenderer.pos(x1, y1, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
			worldRenderer.pos(x1, y2, 0.0D).color(r[1], g[1], b[1], a[1]).endVertex();
			worldRenderer.pos(x2, y2, 0.0D).color(r[1], g[1], b[1], a[1]).endVertex();
		} else if (color.length >= 4) {
			worldRenderer.pos(x2, y1, 0.0D).color(r[0], g[0], b[0], a[0]).endVertex();
			worldRenderer.pos(x1, y1, 0.0D).color(r[1], g[1], b[1], a[1]).endVertex();
			worldRenderer.pos(x1, y2, 0.0D).color(r[2], g[2], b[2], a[2]).endVertex();
			worldRenderer.pos(x2, y2, 0.0D).color(r[3], g[3], b[3], a[3]).endVertex();
		}
		tessellator.draw();
		GlStateManager.shadeModel(7424);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
	}
	
	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int border, int c1, int c2, int width) {
		rectangleBorderedGradient(x1, y1, x2, y2, new int[] { c1, c2 }, new int[] { border }, width);
	}
	
	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int border, int c1, int c2) {
		rectangleBorderedGradient(x1, y1, x2, y2, new int[] { c1, c2 }, new int[] { border }, 0.5);
	}
	
	public static void rectangleOutlinedGradient(double x1, double y1, double x2, double y2, int[] color, double width) {
		rectangleGradient(x1, y1, x2, y1 + width, color);
		rectangleGradient(x1, y2 - width, x2, y2, color);
		rectangleGradient(x1, y1 + width, x1 + width, y2 - width, color);
		rectangleGradient(x2 - width, y1 + width, x2, y2 - width, color);
	}
	
	public static void rectangleBorderedGradient(double x1, double y1, double x2, double y2, int[] fill, int[] outline, double width) {
		rectangleOutlinedGradient(x1, y1, x2, y2, outline, width);
		rectangleGradient(x1 + width, y1 + width, x2 - width, y2 - width, fill);
	}
	
	public static float[] blendF(int color1, int color2, float perc) {
		Color x = new Color(color1);
		Color y = new Color(color2);
		float inverse_blending = 1.0F - perc;
		float red = x.getRed() * perc + y.getRed() * inverse_blending;
		float green = x.getGreen() * perc + y.getGreen() * inverse_blending;
		float blue = x.getBlue() * perc + y.getBlue() * inverse_blending;
		return new float[] { red / 255.0F, green / 255.0F, blue / 255.0F };
	}
	
	public static enum Side {
		Top, Right, Bottom, Left, None;
	}
	
	public static void pre() {
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(770, 771);
		glDisable(GL_TEXTURE_2D);
		glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
		glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
		glDepthMask(false);
	}
	
	public static void post() {
		glDepthMask(true);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_LINE_SMOOTH);
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glPopMatrix();
		glColor4f(1, 1, 1, 1);
	}
	
	public static int blend(int color1, int color2, float perc) {
		Color x = new Color(color1);
		Color y = new Color(color2);
		float inverse_blending = 1.0F - perc;
		float red = x.getRed() * perc + y.getRed() * inverse_blending;
		float green = x.getGreen() * perc + y.getGreen() * inverse_blending;
		float blue = x.getBlue() * perc + y.getBlue() * inverse_blending;
		Color blended;
		try {
			blended = new Color(red / 255.0F, green / 255.0F, blue / 255.0F);
		} catch (Exception e) {
			blended = new Color(-1);
		}
		return blended.getRGB();
	}
	
	public static void drawUserInfoBox(GuiScreen screen, int x, int y) {
		String name = mc.getSession().getUsername();
		if (MCLeaks.isAltActive()) {
			name = MCLeaks.getMCName();
		}
		String credits = "Coded by ";
		String[] names = Impact.getInstance().getClientAuthors();
		
		for (int i = 0; i < names.length; i++) {
			String meme = ", ";
			if (i == names.length - 2) {
				meme = " and ";
			} else if (i == names.length - 1) {
				meme = "";
			}
			credits += "§c" + names[i] + "§f" + meme;
		}
		
		String loggedIn = "Logged in as §b" + name + (MCLeaks.isAltActive() ? " §f[MCLeaks]" : "");
		int w = 32;
		int h = 32;
		int dy = y + 5;
		int dx = x + 5;
		drawUserFace(name, dx - 4, dy - 4, w, h);
		dy -= 2;
		CFontRenderer fontrenderer = FontManager.urwgothic_hud;
		fontrenderer.drawString(Impact.getInstance().getName() + " " + Impact.getInstance().getBuild(), w + dx, dy, -1);
		fontrenderer.drawString(credits, w + dx, dy + 10, -1);
		fontrenderer.drawString(loggedIn, w + dx, dy + 20, -1);
	}
	
	public static void drawUserFace(String name, double x, double y, double w, double h) {
		try {
			AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name).loadTexture(mc.getResourceManager());
			mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(name));
			Tessellator var3 = Tessellator.getInstance();
			BufferBuilder var4 = var3.getBuffer();
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(770, 771);
			double fw = 32;
			double fh = 32;
			double u = 32;
			double v = 32;
			var4.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			var4.pos((double) x + 0, (double) y + h, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + fh) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var4.pos((double) x + w, (double) y + h, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + fh) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var4.pos((double) x + w, (double) y + 0, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + 0) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var4.pos((double) x + 0, (double) y + 0, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + 0) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var3.draw();
			fw = 32;
			fh = 32;
			u = 160;
			v = 32;
			var4.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
			var4.pos((double) x + 0, (double) y + h, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + fh) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var4.pos((double) x + w, (double) y + h, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + fh) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var4.pos((double) x + w, (double) y + 0, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + 0) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var4.pos((double) x + 0, (double) y + 0, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + 0) * 0.00390625F).color(255, 255, 255, 255).endVertex();
			var3.draw();
			GlStateManager.disableBlend();
		} catch (Exception e) {
		}
	}
	
	public static void drawBoundingBox(AxisAlignedBB aa, float red, float green, float blue, float alpha) {
		glColor4f(red, green, blue, alpha);
		glBegin(GL_QUADS);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glEnd();
		glBegin(GL_QUADS);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glEnd();
	}
	
	public static void drawOutlinedBoundingBox(AxisAlignedBB aa, float red, float green, float blue, float alpha) {
		glColor4f(red, green, blue, alpha);
		glBegin(GL_LINE_STRIP);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glEnd();
		glBegin(GL_LINE_LOOP);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glEnd();
		glBegin(GL_LINES);
		glVertex3d(aa.minX, aa.minY, aa.minZ);
		glVertex3d(aa.minX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.minZ);
		glVertex3d(aa.maxX, aa.maxY, aa.minZ);
		glVertex3d(aa.maxX, aa.minY, aa.maxZ);
		glVertex3d(aa.maxX, aa.maxY, aa.maxZ);
		glVertex3d(aa.minX, aa.minY, aa.maxZ);
		glVertex3d(aa.minX, aa.maxY, aa.maxZ);
		glEnd();
	}
	
	public static void drawOutlinedBlockESP(AxisAlignedBB bb, float red, float green, float blue, float alpha, float lineWidth) {
		pre();
		glLineWidth(lineWidth);
		drawOutlinedBoundingBox(bb, red, green, blue, alpha);
		post();
	}
	
	public static void drawSolidBlockESP(AxisAlignedBB bb, float red, float green, float blue, float alpha) {
		pre();
		drawBoundingBox(bb, red, green, blue, alpha);
		post();
	}
	
	public static void drawEntityESP(EntityLivingBase entity, float red, float green, float blue, float partialTicks) {
		double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
		double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
		double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
		AxisAlignedBB bb = new AxisAlignedBB(x - (entity.width / 2), y, z - (entity.width / 2), x + (entity.width / 2), y + entity.height, z + (entity.width / 2));
		glPushMatrix();
		glLineWidth(0.25F);
		if (entity.isDead || entity.getHealth() <= 0) {
			float var19 = MathUtils.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
			float var11 = MathUtils.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
			float var12 = var11 - var19;
			float var14;
			if (entity.isRiding() && entity.getRidingEntity() instanceof EntityLivingBase) {
				EntityLivingBase var20 = (EntityLivingBase) entity.getRidingEntity();
				var19 = MathUtils.interpolateRotation(var20.prevRenderYawOffset, var20.renderYawOffset, partialTicks);
				var12 = var11 - var19;
				var14 = MathHelper.wrapDegrees(var12);
				var14 = Math.max(-85.0F, var14);
				var14 = Math.min(85.0F, var14);
				var19 = var11 - var14;
				if (var14 * var14 > 2500.0F) {
					var19 += var14 * 0.2F;
				}
			}
			rotateY(180.0F - var19, x, y, z);
			if (entity.deathTime > 0) {
				float var6 = ((float) entity.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
				var6 = MathHelper.sqrt(var6);
				if (var6 > 1.0F) {
					var6 = 1.0F;
				}
				rotateZ(var6 * 90.0F, x, y, z);
			}
		}
		rotateY(-(entity.rotationYaw), x, y, z);
		RenderUtils.drawEntityESP(x, y, z, (entity.width / 2), entity.height, red, green, blue, 0.25F);
		glPopMatrix();
	}
	
	public static void drawTracer(Entity entity, float partialTicks) {
		double x = (entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
		double y = (entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
		double z = (entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
		pre();
		glLoadIdentity();
		((IEntityRenderer) mc.entityRenderer).callOrientCamera(partialTicks);
		Vec3d eyes = new Vec3d(0, 0, 1).rotatePitch(-(float) Math.toRadians(mc.player.rotationPitch)).rotateYaw(-(float) Math.toRadians(mc.player.rotationYaw));
		glBegin(GL_LINE_STRIP);
		glVertex3d(eyes.x, mc.player.getEyeHeight() + eyes.y, eyes.z);
		glVertex3d(x, y, z);
		glVertex3d(x, y + entity.getEyeHeight(), z);
		glEnd();
		post();
	}
	
	public static void drawEntityESP(double x, double y, double z, double width, double height, float red, float green, float blue, float alpha) {
		pre();
		AxisAlignedBB aa = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
		drawBoundingBox(aa, red, green, blue, alpha);
		glLineWidth(1.0F);
		drawOutlinedBoundingBox(aa, red, green, blue, 1.0F);
		post();
	}
	
	public static void renderEntityItem(EntityItem item, float red, float green, float blue, double x, double y, double z, boolean filled) {
		pre();
		AxisAlignedBB aa = new AxisAlignedBB(x - (item.width * .4) - 0.05, y - (item.height * .4), z - (item.width * .4) - 0.05, x + (item.width * .8) - 0.05, y + (item.height * .8), z + (item.width * .8) - 0.05);
		drawOutlinedBoundingBox(aa, red, green, blue, 1.0F);
		if (filled) {
			drawBoundingBox(aa, red, green, blue, 0.25F);
		}
		post();
	}
}
