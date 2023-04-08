package me.zero.clarinet.util.render.shader;

import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import me.zero.clarinet.util.Helper;
import org.lwjgl.opengl.ARBShaderObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;

public class ShaderHelper implements Helper {
	
	public static boolean isHovering(int x, int y, int x2, int y2, int mouseX, int mouseY) {
		return (mouseX >= x) && (mouseX <= x2) && (mouseY >= y) && (mouseY <= y2);
	}
	
	public static double interpolate(double now, double then) {
		return then + (now - then) * mc.getRenderPartialTicks();
	}
	
	public static double[] interpolate(Entity entity) {
		double posX = interpolate(entity.posX, entity.lastTickPosX) - ((IRenderManager) mc.getRenderManager()).getRenderPosX();
		double posY = interpolate(entity.posY, entity.lastTickPosY) - ((IRenderManager) mc.getRenderManager()).getRenderPosY();
		double posZ = interpolate(entity.posZ, entity.lastTickPosZ) - ((IRenderManager) mc.getRenderManager()).getRenderPosZ();
		return new double[] { posX, posY, posZ };
	}
	
	public static int createShader(String shaderCode, int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			if (shader == 0) {
				return 0;
			}
			ARBShaderObjects.glShaderSourceARB(shader, shaderCode);
			ARBShaderObjects.glCompileShaderARB(shader);
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == 0) {
				throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			}
			return shader;
		} catch (Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}
	
	public static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	public static double interp(double from, double to, double pct) {
		return from + (to - from) * pct;
	}
	
	public static double interpPlayerX() {
		return interp(mc.player.lastTickPosX, mc.player.posX, mc.getRenderPartialTicks());
	}
	
	public static double interpPlayerY() {
		return interp(mc.player.lastTickPosY, mc.player.posY, mc.getRenderPartialTicks());
	}
	
	public static double interpPlayerZ() {
		return interp(mc.player.lastTickPosZ, mc.player.posZ, mc.getRenderPartialTicks());
	}
}
