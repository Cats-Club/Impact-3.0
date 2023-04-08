package me.zero.clarinet.util.render.shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.IEntityRenderer;
import net.minecraft.client.Minecraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.EXTFramebufferObject;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import net.minecraft.client.shader.Framebuffer;

public class ESPShader extends ShaderProgram {
	
	private int diffuseSamperUniformID, texelSizeUniformID, sampleRadiusUniformID;

	private float outlineSize = 2F;

	private int sampleRadius = 4;
	
	public ESPShader(String fragment, String vertex, Framebuffer fbo) {
		super(fragment, vertex, fbo);
		
		diffuseSamperUniformID = getUniformLocation("DiffuseSamper");
		texelSizeUniformID = getUniformLocation("TexelSize");
		sampleRadiusUniformID = getUniformLocation("SampleRadius");
	}
	
	@Override
	public ShaderProgram update() {
		if (this.fboID == -1 || this.renderBufferID == -1 || shaderProgramID == -1) {
			throw new RuntimeException("Invalid IDs!");
		}
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, this.fboID);
		
		int var9 = Math.max(Minecraft.getDebugFPS(), 30);
		glClear(16640);
		((IEntityRenderer) mc.entityRenderer).callUpdateFogColor((float) (((IEntityRenderer) mc.entityRenderer).getRenderEndNanoTime() +  1e9 / var9));

		ARBShaderObjects.glUseProgramObjectARB(shaderProgramID);
		ARBShaderObjects.glUniform1iARB(diffuseSamperUniformID, 0);
		
		glActiveTexture(GL_TEXTURE0);
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, this.targetTextureID);
		
		FloatBuffer texelSizeBuffer = BufferUtils.createFloatBuffer(2);
		texelSizeBuffer.position(0);
		texelSizeBuffer.put(1.0F / this.textureWidth * this.outlineSize);
		texelSizeBuffer.put(1.0F / this.textureHeight * this.outlineSize);
		texelSizeBuffer.flip();
		ARBShaderObjects.glUniform2ARB(texelSizeUniformID, texelSizeBuffer);
		
		IntBuffer sampleRadiusBuffer = BufferUtils.createIntBuffer(1);
		sampleRadiusBuffer.position(0);
		sampleRadiusBuffer.put(this.sampleRadius);
		sampleRadiusBuffer.flip();
		ARBShaderObjects.glUniform1ARB(sampleRadiusUniformID, sampleRadiusBuffer);
		
		this.draw();
		
		ARBShaderObjects.glUseProgramObjectARB(0);
		return this;
	}
}
