package me.zero.clarinet.util.render;

import me.zero.clarinet.mixin.mixins.minecraft.client.shader.IShaderGroup;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public final class BlurUtils {
	
	private static Minecraft mc = Minecraft.getMinecraft();
	
	private static ShaderGroup blurShader;
	private static Framebuffer buffer;
	private static int lastScale;
	private static int lastScaleWidth;
	private static int lastScaleHeight;
	private static ResourceLocation shader = new ResourceLocation("shaders/post/blur.json");
	
	public static void initFboAndShader() {
		try {
			blurShader = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shader);
			blurShader.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
			buffer = ((IShaderGroup) blurShader).getFrameBuffers().get(0);
		} catch (Exception e) {
		}
	}
	
	private static void setShaderConfigs(float intensity, float blurWidth, float blurHeight) {
		((IShaderGroup) blurShader).getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
		((IShaderGroup) blurShader).getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
		((IShaderGroup) blurShader).getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
		((IShaderGroup) blurShader).getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
	}
	
	public static void blurArea(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
			initFboAndShader();
		}
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;
		
		if (OpenGlHelper.isFramebufferEnabled()) {
			
			buffer.framebufferClear();

            GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor, height * factor);

			setShaderConfigs(intensity, blurWidth, blurHeight);
			buffer.bindFramebuffer(true);
			blurShader.render(mc.getRenderPartialTicks());
			
			mc.getFramebuffer().bindFramebuffer(true);
			
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
			buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
			GlStateManager.disableBlend();
			GL11.glScalef(factor, factor, 0);
		}
	}
	
	public static void blurArea(int x, int y, int width, int height, float intensity) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
			initFboAndShader();
		}
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;
		
		buffer.framebufferClear();
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor, (height) * factor);
		
		setShaderConfigs(intensity, 1, 1);
		buffer.bindFramebuffer(true);
		blurShader.render(mc.getRenderPartialTicks());
		
		mc.getFramebuffer().bindFramebuffer(true);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
		buffer.framebufferRenderExt(mc.displayWidth, mc.displayHeight, false);
		GlStateManager.disableBlend();
		GL11.glScalef(factor, factor, 0);
		RenderHelper.enableGUIStandardItemLighting();
	}
	
	public static void blurAreaBoarder(int x, int y, int width, int height, float intensity, float blurWidth, float blurHeight) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
			initFboAndShader();
		}
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;
		
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor, height * factor);
		
		setShaderConfigs(intensity, blurWidth, blurHeight);
		buffer.bindFramebuffer(true);
		blurShader.render(mc.getRenderPartialTicks());
		
		mc.getFramebuffer().bindFramebuffer(true);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void blurAreaBoarder(int x, int y, int width, int height, float intensity) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		int factor2 = scale.getScaledWidth();
		int factor3 = scale.getScaledHeight();
		if (lastScale != factor || lastScaleWidth != factor2 || lastScaleHeight != factor3 || buffer == null || blurShader == null) {
			initFboAndShader();
		}
		lastScale = factor;
		lastScaleWidth = factor2;
		lastScaleHeight = factor3;
		
		GL11.glScissor(x * factor, (mc.displayHeight - (y * factor) - height * factor), width * factor, (height) * factor);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		setShaderConfigs(intensity, 1, 0);
		buffer.bindFramebuffer(true);
		blurShader.render(mc.getRenderPartialTicks());
		
		mc.getFramebuffer().bindFramebuffer(true);
		
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void end() {
		if (blurShader == null) return;

		((IShaderGroup) blurShader).getShaders().stream().forEach(shader -> shader.getShaderManager().endShader());
	}
}
