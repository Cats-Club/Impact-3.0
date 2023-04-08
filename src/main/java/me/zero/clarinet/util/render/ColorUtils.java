package me.zero.clarinet.util.render;

import java.awt.Color;

import me.zero.clarinet.util.ClientUtils;
import me.zero.clarinet.util.Helper;
import me.zero.clarinet.util.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;

public class ColorUtils implements Helper {
	
	public static void setColor(Color color) {
		GlStateManager.color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F);
	}
	
	public static int getRainbowH() {
		return getRainbowH(4);
	}
	
	public static float[] getRainbowF() {
		return getRainbowF(4);
	}
	
	public static float[] getRainbowF(int divisor) {
		int color = getRainbowH(4);
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;
		return new float[] { red, green, blue };
	}
	
	public static int getRainbowH(int divisor) {
		divisor = Math.max(divisor, 1);
		float r = (1F - (float) Math.sin((float) ((System.currentTimeMillis() / divisor) % 1000L) / 1000L * Math.PI * 2)) / 2F;
		float g = (1F - (float) Math.sin((float) (((System.currentTimeMillis() / divisor) + 333L) % 1000L) / 1000L * Math.PI * 2)) / 2F;
		float b = (1F - (float) Math.sin((float) (((System.currentTimeMillis() / divisor) + 666L) % 1000L) / 1000L * Math.PI * 2)) / 2F;
		return new Color(r, g, b).getRGB();
	}

    public static int getRainbowOffset(int offset) {
        float r = (1F - (float) Math.sin((float) ((System.currentTimeMillis() + offset) % 1000L) / 1000L * Math.PI * 2)) / 2F;
        float g = (1F - (float) Math.sin((float) (((System.currentTimeMillis() + offset) + 333L) % 1000L) / 1000L * Math.PI * 2)) / 2F;
        float b = (1F - (float) Math.sin((float) (((System.currentTimeMillis() + offset) + 666L) % 1000L) / 1000L * Math.PI * 2)) / 2F;
        return new Color(r, g, b).getRGB();
    }
	
	public static int getColorCode(char cl) {
		int color = mc.fontRenderer.getColorCode(cl);
		float r = ((color >> 16 & 0xFF) / 255.0F);
		float g = ((color >> 8 & 0xFF) / 255.0F);
		float b = ((color & 0xFF) / 255.0F);
		float a = 1;
		return new Color(r, g, b, a).getRGB();
	}
	
	public static int getModColor() {
		int min = 150;
		int max = 255;
		int r = MathUtils.randInt(min, max);
		int g = MathUtils.randInt(min, max);
		int b = MathUtils.randInt(min, max);
		return new Color(r, g, b, 255).getRGB();
	}

    public static int getHealthRGBI(EntityLivingBase entity) {
        return getHealthRGBC(entity).getRGB();
    }

    public static Color getHealthRGBC(EntityLivingBase entity) {
        float[] color = getHealthRGB(entity);
        return new Color(color[0], color[1], color[2], color[3]);
    }

    public static float[] getHealthRGB(EntityLivingBase entity) {
        float[] c1 = { 0.0F, 1.0F, 0.0F };
        float[] c2 = { 1.0F, 0.9F, 0.0F };
        float[] c3 = { 1.0F, 0.0F, 0.0F };
        float hp = Math.min(entity.getHealth(), entity.getMaxHealth());
        float max = entity.getMaxHealth();
        if ((hp / max) > 0.5F) {
            float perc = (hp - max / 2) / (max / 2);
            float inverse_blending = 1.0F - perc;
            float red = c1[0] * perc + c2[0] * inverse_blending;
            float green = c1[1] * perc + c2[1] * inverse_blending;
            float blue = c1[2] * perc + c2[2] * inverse_blending;
            return new float[] { red, green, blue, 1 };
        } else {
            float perc = hp / (max / 2);
            float inverse_blending = 1.0F - perc;
            float red = c2[0] * perc + c3[0] * inverse_blending;
            float green = c2[1] * perc + c3[1] * inverse_blending;
            float blue = c2[2] * perc + c3[2] * inverse_blending;
            return new float[] { red, green, blue, 1 };
        }
    }
}
