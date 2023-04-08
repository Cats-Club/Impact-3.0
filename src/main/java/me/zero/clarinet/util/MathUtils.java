package me.zero.clarinet.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public final class MathUtils {
	
	public static double getAverage(double[] numbers) {
		if (numbers.length <= 0) {
			return 0;
		} else {
			double total = 0;
			for (double d : numbers) {
				total += d;
			}
			return total / numbers.length;
		}
	}
	
	public static boolean chance(int percent) {
		return (new Random().nextInt(100) < percent);
	}
	
	public static int randInt(int min, int max) {
		return new Random().nextInt(max - min + 1) + min;
	}
	
	public static double roundToPlace(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public static float interpolateRotation(float par1, float par2, float par3) {
		float var4;
		
		for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F) {
			;
		}
		
		while (var4 >= 180.0F) {
			var4 -= 360.0F;
		}
		
		return par1 + par3 * var4;
	}
	
	public static int getShadowColor(int hex) {
		return (hex & 16579836) >> 2 | hex & -16777216;
	}
	
	public static float capColorF(float v) {
		v = Math.min(1, v);
		v = Math.max(0, v);
		return v;
	}
	
	public static int capColorI(int v) {
		v = Math.min(255, v);
		v = Math.max(0, v);
		return v;
	}
	
	public static double normalizeAngle(double angle) {
		return (angle + 360.0D) % 360.0D;
	}
	
	public static int clampInteger(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}
}
