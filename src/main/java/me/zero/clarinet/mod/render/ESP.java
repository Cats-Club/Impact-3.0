package me.zero.clarinet.mod.render;

import java.awt.Color;

import me.zero.clarinet.mixin.Fields;
import me.zero.clarinet.mixin.mixins.minecraft.client.IMinecraft;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.IEntityRenderer;
import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRenderManager;
import org.lwjgl.input.Keyboard;
import static org.lwjgl.opengl.GL11.*;

import me.zero.clarinet.event.api.EventTarget;
import me.zero.clarinet.event.api.types.Priority;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.MCStencil;
import me.zero.clarinet.util.render.RenderUtils;
import me.zero.clarinet.util.render.Stencil;
import me.zero.clarinet.util.render.shader.ESPShader;
import me.zero.clarinet.util.render.shader.ShaderHelper;
import me.zero.clarinet.util.render.shader.ShaderProgram;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.MultiValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends Mod {

    private MultiValue<String> mode = new MultiValue<>(this, "Mode", "mode", "Box", new String[] { "Box", "Outline", "Spectral" });
	
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
	private NumberValue width = new NumberValue(this, "Outline Width", "width", 2D, 0D, 3D, 0.25D);

	private Framebuffer shaderBuffer;
	private ESPShader shaderOutline;
	
	public ESP() {
		super("ESP", "Reveals entity location", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget(Priority.LOWEST)
	public void onRender(EventRender3D event) {
		this.suffix = mode.getValue();
        if (mode.getValue().equalsIgnoreCase("Box")) {
            mc.world.getLoadedEntityList().stream().filter(e -> e instanceof EntityLivingBase).forEach(e -> {
                EntityLivingBase entity = (EntityLivingBase) e;
                if (isCorrectEntity(entity)) {
                    float[] colors = getESPColorF(entity);
                    float red = colors[0];
                    float green = colors[1];
                    float blue = colors[2];
                    RenderUtils.drawEntityESP(entity, red, green, blue, event.partialTicks);
                }
            });
		} else if (mode.getValue().equalsIgnoreCase("Outline")) {
            for (Entity entity : mc.world.loadedEntityList) {
                if (isCorrectEntity(entity)) {
                    glPushMatrix();
                    double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks;
                    double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks;
                    double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks;
                    glTranslated(posX - ((IRenderManager) mc.getRenderManager()).getRenderPosX(), (posY - Math.pow(10, 5)) - ((IRenderManager) mc.getRenderManager()).getRenderPosY(), posZ - ((IRenderManager) mc.getRenderManager()).getRenderPosZ());
                    Render<Entity> entityRender = mc.getRenderManager().getEntityRenderObject(entity);
                    if (entityRender != null) {
                        if (entity instanceof EntityLivingBase) {
                            GlStateManager.disableLighting();
							Fields.renderLayersRenderLivingBase = false;
                            entityRender.doRender(entity, 0.0, 0.0, 0.0, 0.0f, ((IMinecraft) mc).getTimer().renderPartialTicks);
							Fields.renderLayersRenderLivingBase = true;
                            GlStateManager.enableLighting();
                        }
                    }
                    glPopMatrix();
                }
            }
			Color color = new Color(255, 255, 255);
			ColorUtils.setColor(color);
			MCStencil.checkSetupFBO();
			int list = glGenLists(1);
			Stencil.getInstance().startLayer();
			RenderUtils.pre();
			Stencil.getInstance().setBuffer(true);
			glNewList(list, GL_COMPILE);
			glLineWidth(width.getValue().floatValue());
			for (Entity entity : mc.world.loadedEntityList) {
				if (isCorrectEntity(entity)) {
					Render<Entity> render = mc.getRenderManager().getEntityRenderObject(entity);
					if (render == null) {
						continue;
					}
					RenderUtils.pre();
					double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ((IMinecraft) mc).getTimer().renderPartialTicks;
					double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ((IMinecraft) mc).getTimer().renderPartialTicks;
					double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ((IMinecraft) mc).getTimer().renderPartialTicks;
					glTranslated(posX - ((IRenderManager) mc.getRenderManager()).getRenderPosX(), posY - ((IRenderManager) mc.getRenderManager()).getRenderPosY(), posZ - ((IRenderManager) mc.getRenderManager()).getRenderPosZ());
					float[] colors = getESPColorF(entity);
					glColor4f(colors[0], colors[1], colors[2], colors[3]);
					Fields.renderLayersRenderLivingBase = false;
					glDisable(GL_BLEND);
					render.setRenderOutlines(true);
					render.doRender(entity, 0.0, 0.0, 0.0, 0.0F, ((IMinecraft) mc).getTimer().renderPartialTicks);
					render.setRenderOutlines(false);
					glEnable(GL_BLEND);
					Fields.renderLayersRenderLivingBase = true;
					RenderUtils.post();
				}
			}
			glEndList();
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			glCallList(list);
			Stencil.getInstance().setBuffer(false);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			glCallList(list);
			Stencil.getInstance().cropInside();
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			glCallList(list);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			Stencil.getInstance().stopLayer();
			glDeleteLists(list, 1);
			RenderUtils.post();
		} else if (mode.getValue().equalsIgnoreCase("Shader")) {
			ScaledResolution sr = new ScaledResolution(mc);
			
			if (shaderBuffer == null) {
				shaderBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
			} else if ((shaderBuffer.framebufferWidth != mc.displayWidth) || (shaderBuffer.framebufferHeight != mc.displayHeight)) {
				shaderBuffer.unbindFramebuffer();
				shaderBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, false);
				if (shaderOutline != null) {
					shaderOutline.delete();
					shaderOutline = new ESPShader("shader/fragment.glsl", "shader/vertex.glsl", shaderBuffer);
				}
			}
			if (shaderOutline == null) {
				shaderOutline = new ESPShader("shader/fragment.glsl", "shader/vertex.glsl", shaderBuffer);
			}
			
			RenderUtils.pre();

			((IEntityRenderer) mc.entityRenderer).callSetupCameraTransform(((IMinecraft) mc).getTimer().renderPartialTicks, 0);
			glMatrixMode(GL_MODELVIEW);
			RenderHelper.enableStandardItemLighting();
			this.shaderBuffer.bindFramebuffer(false);
			
			glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
			glClear(16640);

            mc.entityRenderer.disableLightmap();
            RenderHelper.disableStandardItemLighting();

            mc.world.loadedEntityList.stream().filter(this::isCorrectEntity).forEach(ent -> {
                glEnable(GL_TEXTURE_2D);
                double[] iterpolation = ShaderHelper.interpolate(ent);
                double x = iterpolation[0];
                double y = iterpolation[1];
                double z = iterpolation[2];
                glPushMatrix();
                Render render = mc.getRenderManager().getEntityRenderObject(ent);
                if (render != null) {
					Fields.renderEnchantsLayerArmorBase = false;
                    Fields.renderEnchantsRenderItem = false;
                    Fields.renderTagsRender = false;
                    render.doRender(ent, x, y, z, 0.0f, ((IMinecraft) mc).getTimer().renderPartialTicks);
                    Fields.renderTagsRender = true;
                    Fields.renderEnchantsRenderItem = true;
                    Fields.renderEnchantsLayerArmorBase = true;
                }
                glDisable(GL_TEXTURE_2D);
                glPopMatrix();
            });

            mc.entityRenderer.setupOverlayRendering();

			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			this.shaderOutline.update();
			mc.getFramebuffer().bindFramebuffer(true);

			glScaled(1.0 / ShaderProgram.superScale, 1.0 / ShaderProgram.superScale, 1.0 / ShaderProgram.superScale);
			glColor4f(r.getValue().floatValue() / 255F, g.getValue().floatValue() / 255F, b.getValue().floatValue() / 255F, 1.0F);
			glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, this.shaderOutline.getTextureID());
			glBegin(GL_LINE_BIT);
			glTexCoord2d(0.0D, 1.0D);
			glVertex2d(0.0D, 0.0D);
			glTexCoord2d(0.0D, 0.0D);
			glVertex2d(0.0D, sr.getScaledHeight() * 2);
			glTexCoord2d(1.0D, 0.0D);
			glVertex2d(sr.getScaledWidth() * 2, sr.getScaledHeight() * 2);
			glTexCoord2d(1.0D, 0.0D);
			glVertex2d(sr.getScaledWidth() * 2, sr.getScaledHeight() * 2);
			glTexCoord2d(1.0D, 1.0D);
			glVertex2d(sr.getScaledWidth() * 2, 0.0D);
			glTexCoord2d(0.0D, 1.0D);
			glVertex2d(0.0D, 0.0D);
			glEnd();
			glScaled(ShaderProgram.superScale, ShaderProgram.superScale, ShaderProgram.superScale);
			
			glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			mc.getFramebuffer().bindFramebuffer(false);

            RenderHelper.enableStandardItemLighting();
			
			RenderUtils.post();
		}
	}
	
	public boolean isSpectral() {
		return mode.getValue().equalsIgnoreCase("Spectral");
	}
	
	public boolean isCorrectEntity(Object o) {
		if (!(o instanceof Entity)) return false;

		Entity ent = (Entity) o;

		if (ent == mc.player) return false;

		if (ent.isInvisible() && !invisible.getValue()) return false;

		if (o instanceof EntityPlayer) {
			if (players.getValue()) {
				EntityPlayer player = (EntityPlayer) o;
                return !(player.isPlayerSleeping() && !sleeping.getValue());
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
	
	private float[] getESPColorF(Entity ent) {
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
