package me.zero.clarinet.mod.render;

import java.util.Random;

import me.zero.clarinet.mixin.mixins.minecraft.client.renderer.entity.IRender;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.event.game.EventTick;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ItemPhysics extends Mod {
	
	private NumberValue rotateSpeed = new NumberValue(this, "Rotate Speed", "speed", 25.0D, 0.0D, 100.0D, 1.0D);
	
	private BooleanValue oldRotation = new BooleanValue(this, "Old Rotation", "old", false);
	
	private BooleanValue spread = new BooleanValue(this, "Item Spread", "spread", false);
	
	public long tick;
	public double rotation;
	public Random random = new Random();
	
	public ItemPhysics() {
		super("ItemPhysics", "Dropped items have physics", Keyboard.KEY_NONE, Category.RENDER);
	}
	
	@EventTarget
	public void onTick(EventTick event) {
		tick = System.nanoTime();
	}
	
	public void doRender(RenderEntityItem renderer, Entity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		rotation = (System.nanoTime() - tick) / 10000000.0D * rotateSpeed.getValue().floatValue() / 12.5F;
		if (!mc.inGameHasFocus) {
			rotation = 0.0D;
		}
		EntityItem item = (EntityItem) entity;
		
		ItemStack itemstack = item.getItem();
		int i;
		if ((itemstack != null) && (itemstack.getItem() != null)) {
			i = Item.getIdFromItem(itemstack.getItem()) + itemstack.getMetadata();
		} else {
			i = 187;
		}
		random.setSeed(i);
		boolean flag = true;
		
		GlStateManager.pushMatrix();
		double scale = 0.5;
		GlStateManager.scale(scale, scale, scale);
		x *= 1.0 / scale;
		y *= 1.0 / scale;
		z *= 1.0 / scale;
		
		renderer.bindTexture(getEntityTexture());
		renderer.getRenderManager().renderEngine.getTexture(getEntityTexture()).setBlurMipmap(false, false);
		
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.pushMatrix();
		IBakedModel ibakedmodel = mc.getRenderItem().getItemModelWithOverrides(itemstack, entity.world, (EntityLivingBase) null);
		
		boolean flag1 = ibakedmodel.isGui3d();
		boolean is3D = ibakedmodel.isGui3d();
		int j = getModelCount(itemstack);
		float f = 0.25F;
		float f1 = 0.0F;
		float f2 = ibakedmodel.getItemCameraTransforms().getTransform(ItemCameraTransforms.TransformType.GROUND).scale.y;
		
		GlStateManager.translate((float) x, (float) y, (float) z);
		
		GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
		GL11.glRotatef(item.rotationYaw, 0.0F, 0.0F, 1.0F);
		if (is3D) {
			GlStateManager.translate(0.0D, -0.2D, -0.08D);
		} else {
			GlStateManager.translate(0.0D, 0.0D, -0.04D);
		}
		if ((is3D) || (mc.getRenderManager().options != null)) {
			if (is3D) {
				if (!item.onGround) {
					item.rotationPitch = ((float) (item.rotationPitch + rotation));
				} else if (oldRotation.getValue()) {
					for (int side = 0; side < 4; side++) {
						rotation = side * 90;
						double range = 5.0D;
						if ((item.rotationPitch > rotation - range) && (item.rotationPitch < rotation + range)) {
							item.rotationPitch = ((float) rotation);
						}
					}
					if ((item.rotationPitch != 0.0F) && (item.rotationPitch != 90.0F) && (item.rotationPitch != 180.0F) && (item.rotationPitch != 270.0F)) {
						double dir1 = Math.abs(item.rotationPitch);
						double dir2 = Math.abs(item.rotationPitch - 90.0F);
						double dir3 = Math.abs(item.rotationPitch - 180.0F);
						double dir4 = Math.abs(item.rotationPitch - 270.0F);
						if ((dir1 <= dir2) && (dir1 <= dir3) && (dir1 <= dir4)) {
							if (item.rotationPitch < 0.0F) {
								item.rotationPitch = ((float) (item.rotationPitch + rotation));
							} else {
								item.rotationPitch = ((float) (item.rotationPitch - rotation));
							}
						}
						if ((dir2 < dir1) && (dir2 <= dir3) && (dir2 <= dir4)) {
							if (item.rotationPitch - 90.0F < 0.0F) {
								item.rotationPitch = ((float) (item.rotationPitch + rotation));
							} else {
								item.rotationPitch = ((float) (item.rotationPitch - rotation));
							}
						}
						if ((dir3 < dir2) && (dir3 < dir1) && (dir3 <= dir4)) {
							if (item.rotationPitch - 180.0F < 0.0F) {
								item.rotationPitch = ((float) (item.rotationPitch + rotation));
							} else {
								item.rotationPitch = ((float) (item.rotationPitch - rotation));
							}
						}
						if ((dir4 < dir2) && (dir4 < dir3) && (dir4 < dir1)) {
							if (item.rotationPitch - 270.0F < 0.0F) {
								item.rotationPitch = ((float) (item.rotationPitch + rotation));
							} else {
								item.rotationPitch = ((float) (item.rotationPitch - rotation));
							}
						}
					}
				}
			} else if ((item != null) && (!Double.isNaN(item.posX)) && (!Double.isNaN(item.posY)) && (!Double.isNaN(item.posZ)) && (item.world != null)) {
				if (item.onGround) {
					item.rotationPitch = 0.0F;
				} else {
					item.rotationPitch = ((float) (item.rotationPitch + rotation));
				}
			}
			double height = 0.2D;
			if (is3D) {
				GlStateManager.translate(0.0D, height, 0.0D);
			}
			GlStateManager.rotate(item.rotationPitch, 1.0F, 0.0F, 0.0F);
			if (is3D) {
				GlStateManager.translate(0.0D, -height, 0.0D);
			}
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		boolean renderOutlines = ((IRender) renderer).getRenderOutlines();
		if (!flag1) {
			float f3 = -0.0F * (j - 1) * 0.5F;
			float f4 = -0.0F * (j - 1) * 0.5F;
			float f5 = -0.09375F * (j - 1) * 0.5F;
			GlStateManager.translate(f3, f4, f5);
		}
		if (renderOutlines) {
			GlStateManager.enableColorMaterial();
			try {
				GlStateManager.enableOutlineMode(((IRender) renderer).callGetTeamColor(item));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (int k = 0; k < j; k++) {
			if (flag1) {
				GlStateManager.pushMatrix();
				if (k > 0) {
					float f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					float f6 = (random.nextFloat() * 2.0F - 1.0F) * 0.15F;
					GlStateManager.translate(spread.getValue() ? f7 : 0.0F, spread.getValue() ? f9 : 0.0F, f6);
				}
				mc.getRenderItem().renderItem(itemstack, ibakedmodel);
				GlStateManager.popMatrix();
			} else {
				GlStateManager.pushMatrix();
				if (k > 0) {
				}
				mc.getRenderItem().renderItem(itemstack, ibakedmodel);
				GlStateManager.popMatrix();
				GlStateManager.translate(0.0F, 0.0F, 0.05375F);
			}
		}
		if (renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}
		GlStateManager.popMatrix();
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		renderer.bindTexture(getEntityTexture());
		if (flag) {
			renderer.getRenderManager().renderEngine.getTexture(getEntityTexture()).restoreLastBlurMipmap();
		}
		GlStateManager.popMatrix();
	}
	
	public static ResourceLocation getEntityTexture() {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}
	
	public static int getModelCount(ItemStack stack) {
		int i = 1;
		if (stack.getCount() > 48) {
			i = 5;
		} else if (stack.getCount() > 32) {
			i = 4;
		} else if (stack.getCount() > 16) {
			i = 3;
		} else if (stack.getCount() > 1) {
			i = 2;
		}
		return i;
	}
}
