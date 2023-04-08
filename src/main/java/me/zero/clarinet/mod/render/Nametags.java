package me.zero.clarinet.mod.render;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.event.render.EventRender2D;
import me.zero.clarinet.user.UserManager;
import me.zero.clarinet.util.DimensionConverter;
import me.zero.clarinet.util.misc.MiscUtils;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.clarinet.util.render.RenderUtils;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import me.zero.clarinet.event.api.EventTarget;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.render.EventNametag;
import me.zero.clarinet.event.render.EventRender3D;
import me.zero.clarinet.mod.Category;
import me.zero.clarinet.mod.Mod;
import me.zero.clarinet.util.misc.StringUtils;
import me.zero.values.types.BooleanValue;
import me.zero.values.types.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.GameType;

public class Nametags extends Mod {
	
	private BooleanValue health = new BooleanValue(this, "Health", "health");
	
	private BooleanValue armor = new BooleanValue(this, "Armor", "armor", true);
	
	private BooleanValue gamemode = new BooleanValue(this, "Gamemode", "gamemode", true);
	
	private BooleanValue priority = new BooleanValue(this, "Priority", "priority", true);
	
	private BooleanValue sleeping = new BooleanValue(this, "Sleeping", "sleeping", true);
	
	private BooleanValue invisible = new BooleanValue(this, "Invisibles", "invisible", true);
	
	private NumberValue scale = new NumberValue(this, "Scale", "scale", 1.0D, 0.1D, 2D, 0.1D);

    private ResourceLocation pepsi = null;
	
	public Nametags() {
		super("Nametags", "Makes Nametags Bigger and shows player information", Keyboard.KEY_NONE, Category.RENDER);

        pepsi = MiscUtils.getImageFromURL("http://i.imgur.com/GXS8xyQ.png");
    }

	@EventTarget
    public void onRender2D(EventRender2D event) {
        GlStateManager.pushMatrix();
        DimensionConverter.screenScale();
        List<EntityPlayer> entities = new ArrayList<>();
        for (Entity e : mc.world.loadedEntityList) {
            if (canRenderName(e)) {
                entities.add((EntityPlayer) e);
            }
        }
        if (priority.getValue()) {
            entities.sort((target1, target2) -> {
                double distance1 = target1.getDistance(mc.player);
                double distance2 = target2.getDistance(mc.player);
                return distance1 < distance2 ? 1 : distance1 == distance2 ? 0 : -1;
            });
        }
        for (EntityPlayer player : entities) {

            GlStateManager.pushMatrix();
            Vec3d pos = null;

            try {
                pos = DimensionConverter.getEntityPosition(player);
            } catch(Exception e) {}

            if (pos == null || pos.z < 0.0D || pos.z >= 1.0D){
                GlStateManager.popMatrix();
                continue;
            }

            GL11.glTranslated(pos.x, pos.y, 0.0D);
            DimensionConverter.renderScale(this.scale.getValue().doubleValue());

            String str = player.getDisplayName().getFormattedText();
            if (Impact.getInstance().getFriendManager().isFriend(player.getName())) {
                str = str.replace(Impact.getInstance().getFriendManager().getAliasName(player.getName()), "§b" + Impact.getInstance().getFriendManager().getAliasName(player.getName()));
            }
            String health = ((int) player.getHealth() / 2.0) + " \u2764";

            if (gamemode.getValue()) {
                NetworkPlayerInfo info = mc.getConnection().getPlayerInfo(player.getUniqueID());
                if (info != null) {
                    GameType gameType = info.getGameType();
                    str = TextFormatting.DARK_GRAY + "[" + TextFormatting.GRAY + StringUtils.capitalize(gameType.getName()) + TextFormatting.DARK_GRAY + "] " + TextFormatting.RESET + str;
                }
            }

            boolean pepsi = UserManager.pepsi.isUser(player.getUniqueID().toString());
            int pepsi_offset = pepsi ? 10 : 0;
            int stringWidth = (mc.fontRenderer.getStringWidth(str + (this.health.getValue() ? " " + health : "")) + pepsi_offset) / 2;

            RenderUtils.rectangleBordered(-stringWidth - 2, -1, stringWidth + 2, 9, 0.25D, 0xFF000000, new Color(0, 0, 0, 120).getRGB());
            mc.fontRenderer.drawStringWithShadow(str, -stringWidth + pepsi_offset, 0.0F, -1);

            if (this.pepsi != null && pepsi) {
                GlStateManager.pushMatrix();
                drawTexturedModalRect(-stringWidth - 1, 0, 8, 8);
                GlStateManager.popMatrix();
            }

            if (this.health.getValue()) {
                mc.fontRenderer.drawStringWithShadow(String.valueOf(health), stringWidth - mc.fontRenderer.getStringWidth(String.valueOf(health)), 0.0F, ColorUtils.getHealthRGBI(player));
            }

            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            if (this.armor.getValue()) {
                final List<ItemStack> itemsToRender = new ArrayList<>();
                for (int i = player.inventory.armorInventory.size() - 1; i >= 0; i--) {
                    ItemStack stack = player.inventory.armorInventory.get(i);
                    if (stack != null) {
                        itemsToRender.add(stack);
                    }
                }
                ItemStack off = player.getHeldItem(EnumHand.OFF_HAND);
                if (off != null) {
                    itemsToRender.add(off);
                }
                ItemStack main = player.getHeldItem(EnumHand.MAIN_HAND);
                if (main != null) {
                    itemsToRender.add(main);
                }
                int x = -(itemsToRender.size() * 8);
                for (ItemStack stack : itemsToRender) {
                    if (stack == null) {
                        continue;
                    }
                    renderItemStack(stack, x, -19);
                    x += 16;
                }
            }
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
        }
        GlStateManager.popMatrix();
    }

	@EventTarget
	public void onRender(EventRender3D event) {
	}
	
	private void renderItemStack(ItemStack stack, int x, int y) {
		GL11.glPushMatrix();
		GL11.glDepthMask(true);
		GlStateManager.clear(256);
		GlStateManager.disableLighting();
		mc.getRenderItem().zLevel = -150.0F;
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
		mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
		mc.getRenderItem().renderItemOverlays(mc.fontRenderer, stack, x, y);
		mc.getRenderItem().zLevel = 0.0F;
		RenderHelper.disableStandardItemLighting();
		GlStateManager.enableAlpha();
		GlStateManager.disableBlend();
		GlStateManager.disableLighting();
		double scale = 0.55D;
		GlStateManager.scale(scale, scale, scale);
		GlStateManager.disableDepth();
		drawitemStackEnchants(stack, x, y, scale);
		GlStateManager.enableDepth();
		double rescale = 1.0F / scale;
		GlStateManager.scale(rescale, rescale, rescale);
		GlStateManager.enableLighting();
		GL11.glPopMatrix();
	}
	
	public static void drawitemStackEnchants(ItemStack stack, int x, int y, double scale) {
		y -= 5;
		x += 2;
		x *= 1.0F / scale;
		y *= 1.0F / scale;
		int originalX = x;
		NBTTagList enchants = stack.getEnchantmentTagList();
		if (enchants != null) {
			int ency = 0;
			for (int index = 0; index < enchants.tagCount(); index++) {
				short id = enchants.getCompoundTagAt(index).getShort("id");
				short level = enchants.getCompoundTagAt(index).getShort("lvl");
				if (Enchantment.getEnchantmentByID(id) != null) {
					Enchantment enc = Enchantment.getEnchantmentByID(id);
					String encName = net.minecraft.util.StringUtils.stripControlCodes(enc.getTranslatedName(level)).substring(0, 2).toLowerCase();
					mc.fontRenderer.drawStringWithShadow(String.valueOf(encName), x, y + ency, -5592406);
					mc.fontRenderer.drawStringWithShadow(TextFormatting.AQUA + "" + level, x + 15, y + ency, -5592406);
					ency -= mc.fontRenderer.FONT_HEIGHT;
				}
			}
		}
		/**
		if (stack.hasDisplayName()) {
			float scale2 = 0.35F;
			GlStateManager.pushMatrix();
			GlStateManager.scale(scale2, scale2, scale2);
			x *= 1.0 / scale2;
			y *= 1.0 / scale2;
			float offset = 37F * (1.0F / scale2);
			mc.fontRenderer.drawStringWithShadow(stack.getDisplayName(), x, y + offset, -1);
			GlStateManager.popMatrix();
		}
         */
	}
	
	@EventTarget
	public void nametagEvent(EventNametag event) {
		if (canRenderName(event.entity)) {
			event.setCancelled(true);
		}
	}
	
	public boolean canRenderName(Entity entity) {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!(player instanceof EntityPlayerSP)) {
				if (!player.isInvisible() || invisible.getValue()) {
					if (!player.isPlayerSleeping() || sleeping.getValue()) {
						return true;
					}
				}
			}
		}
		return false;
	}

    private void drawTexturedModalRect(int x, int y, int w, int h){
        mc.getTextureManager().bindTexture(pepsi);
        Tessellator var3 = Tessellator.getInstance();
        BufferBuilder var4 = var3.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        double fw = w * 32;
        double fh = h * 32;
        double u = 0;
        double v = 0;
        var4.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        var4.pos((double) x + 0, (double) y + h, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + fh) * 0.00390625F).color(255, 255, 255, 255).endVertex();
        var4.pos((double) x + w, (double) y + h, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + fh) * 0.00390625F).color(255, 255, 255, 255).endVertex();
        var4.pos((double) x + w, (double) y + 0, 0).tex((float) (u + fw) * 0.00390625F, (float) (v + 0) * 0.00390625F).color(255, 255, 255, 255).endVertex();
        var4.pos((double) x + 0, (double) y + 0, 0).tex((float) (u + 0) * 0.00390625F, (float) (v + 0) * 0.00390625F).color(255, 255, 255, 255).endVertex();
        var3.draw();
        GlStateManager.disableBlend();
    }
}
