package me.zero.clarinet.util.render;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;

public class MCStencil {
	
	public static void checkSetupFBO() {
		Framebuffer fbo = Minecraft.getMinecraft().getFramebuffer();
		if (fbo != null) {
			if (fbo.depthBuffer > -1) {
				setupFBO(fbo);
				fbo.depthBuffer = -1;
			}
		}
	}
	
	public static void setupFBO(Framebuffer fbo) {
		EXTFramebufferObject.glDeleteRenderbuffersEXT(fbo.depthBuffer);
		int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindRenderbufferEXT(36161, stencil_depth_buffer_ID);
		EXTFramebufferObject.glRenderbufferStorageEXT(36161, 34041, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36128, 36161, stencil_depth_buffer_ID);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(36160, 36096, 36161, stencil_depth_buffer_ID);
	}
	
	public static void renderOne(float[] colors, float width) {
		checkSetupFBO();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glLineWidth(width);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		GL11.glClear(1024);
		GL11.glClearStencil(15);
		GL11.glColor4f(colors[0], colors[1], colors[2], colors[3]);
		GL11.glStencilFunc(GL11.GL_NEVER, 1, 15);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
	}
	
	public static void renderTwo() {
		GL11.glStencilFunc(GL11.GL_NEVER, 0, 0xFF);
		GL11.glStencilOp(GL11.GL_REPLACE, GL11.GL_REPLACE, GL11.GL_REPLACE);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
	}
	
	public static void renderThree() {
		GL11.glStencilFunc(GL11.GL_EQUAL, 1, 0xFF);
		GL11.glStencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
	}
	
	public static void renderFour() {
		GL11.glDepthMask(false);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_POLYGON_OFFSET_LINE);
		GL11.glPolygonOffset(1.0F, -2000000.0F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
	}
	
	public static void renderFive() {
		GL11.glPolygonOffset(1.0F, 2000000.0F);
		GL11.glDisable(GL11.GL_POLYGON_OFFSET_LINE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_STENCIL_TEST);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glPopAttrib();
	}
}
