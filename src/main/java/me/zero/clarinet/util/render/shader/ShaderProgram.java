package me.zero.clarinet.util.render.shader;

import java.nio.ByteBuffer;

import me.zero.clarinet.util.Helper;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.util.io.InternalFileReader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Framebuffer;

public abstract class ShaderProgram implements Helper {

	public static final double superScale = 2D;
	
	private final String fragment;
	private final String vertex;
    final int targetTextureID;
	protected final int textureWidth;
	protected final int textureHeight;
	protected final int renderWidth;
	protected final int renderHeight;
	private int fboTextureID = -1;
    int fboID = -1;
    int renderBufferID = -1;
	private int vertexShaderID = -1;
	private int fragmentShaderID = -1;
    int shaderProgramID = -1;

	ShaderProgram(String fragment, String vertex, Framebuffer fbo) {
		this(fragment, vertex, fbo.framebufferTexture, mc.displayWidth, mc.displayHeight, new ScaledResolution(mc).getScaledWidth(), new ScaledResolution(mc).getScaledHeight());
	}

	private ShaderProgram(String fragment, String vertex, int targetTextureID, int textureWidth, int textureHeight, int renderWidth, int renderHeight) {
		this.fragment = fragment;
		this.vertex = vertex;
		this.targetTextureID = targetTextureID;
		this.textureWidth = textureWidth;
		this.textureHeight = textureHeight;
		this.renderWidth = (int) (renderWidth * superScale);
		this.renderHeight = (int) (renderHeight * superScale);
		generateFBO();
		initShaders();
	}
	
	public abstract ShaderProgram update();
	
	private void generateFBO() {
		this.fboID = EXTFramebufferObject.glGenFramebuffersEXT();
		this.fboTextureID = GL11.glGenTextures();
		this.renderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fboTextureID);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, 9729.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, 9729.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10496.0F);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10496.0F);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.fboTextureID);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, this.textureWidth, this.textureHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, this.fboID);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, this.fboTextureID, 0);
		
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, this.renderBufferID);
		EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, ARBFramebufferObject.GL_DEPTH_STENCIL, this.textureWidth, this.textureHeight);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, this.renderBufferID);
		
		checkFBO();
	}
	
	protected void draw() {
		GL11.glScaled(1.0 / superScale, 1.0 / superScale, 1.0 / superScale);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(4);
		GL11.glTexCoord2d(0.0D, 1.0D);
		GL11.glVertex2d(0.0D, 0.0D);
		GL11.glTexCoord2d(0.0D, 0.0D);
		GL11.glVertex2d(0.0D, this.renderHeight);
		GL11.glTexCoord2d(1.0D, 0.0D);
		GL11.glVertex2d(this.renderWidth, this.renderHeight);
		GL11.glTexCoord2d(1.0D, 0.0D);
		GL11.glVertex2d(this.renderWidth, this.renderHeight);
		GL11.glTexCoord2d(1.0D, 1.0D);
		GL11.glVertex2d(this.renderWidth, 0.0D);
		GL11.glTexCoord2d(0.0D, 1.0D);
		GL11.glVertex2d(0.0D, 0.0D);
		GL11.glEnd();
		GL11.glScaled(superScale, superScale, superScale);
	}
	
	public int getTextureID() {
		return this.fboTextureID;
	}
	
	public void delete() {
		if (this.renderBufferID > -1) {
			EXTFramebufferObject.glDeleteRenderbuffersEXT(this.renderBufferID);
		}
		if (this.fboID > -1) {
			EXTFramebufferObject.glDeleteFramebuffersEXT(this.fboID);
		}
		if (this.fboTextureID > -1) {
			GL11.glDeleteTextures(this.fboTextureID);
		}
	}
	
	private void checkFBO() {
		int error = EXTFramebufferObject.glCheckFramebufferStatusEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT);
		switch (error) {
			case 36053:
				return;
			case 36054:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT");
			case 36055:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT");
			case 36056:
			default:
				throw new RuntimeException("glCheckFramebufferStatusEXT returned unknown status:" + error);
			case 36057:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT");
			case 36058:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT");
			case 36059:
				throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT");
		}
	}
	
	private void initShaders() {
		if (shaderProgramID == -1) {
			shaderProgramID = ARBShaderObjects.glCreateProgramObjectARB();
			try {
				if (vertexShaderID == -1) {
					String vertexShaderCode = InternalFileReader.readFile(vertex);
					vertexShaderID = ShaderHelper.createShader(vertexShaderCode, ARBVertexShader.GL_VERTEX_SHADER_ARB);
				}
				if (fragmentShaderID == -1) {
					String fragmentShaderCode = InternalFileReader.readFile(fragment);
					fragmentShaderID = ShaderHelper.createShader(fragmentShaderCode, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
				}
			} catch (Exception ex) {
				shaderProgramID = -1;
				vertexShaderID = -1;
				fragmentShaderID = -1;
				ex.printStackTrace();
			}
			if (shaderProgramID != -1) {
				ARBShaderObjects.glAttachObjectARB(shaderProgramID, vertexShaderID);
				ARBShaderObjects.glAttachObjectARB(shaderProgramID, fragmentShaderID);
				ARBShaderObjects.glLinkProgramARB(shaderProgramID);
				if (ARBShaderObjects.glGetObjectParameteriARB(shaderProgramID, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == 0) {
					System.err.println(ShaderHelper.getLogInfo(shaderProgramID));
					return;
				}
				ARBShaderObjects.glValidateProgramARB(shaderProgramID);
				if (ARBShaderObjects.glGetObjectParameteriARB(shaderProgramID, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == 0) {
					System.err.println(ShaderHelper.getLogInfo(shaderProgramID));
					return;
				}
				ARBShaderObjects.glUseProgramObjectARB(0);
			}
		}
	}
	
	protected int getUniformLocation(String name) {
		return ARBShaderObjects.glGetUniformLocationARB(shaderProgramID, name);
	}
}
