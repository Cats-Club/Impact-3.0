package me.zero.clarinet.util.misc;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class StringUtils {
	
	private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();
	
	static {
		map.put(1000, "M");
		map.put(900, "CM");
		map.put(500, "D");
		map.put(400, "CD");
		map.put(100, "C");
		map.put(90, "XC");
		map.put(50, "L");
		map.put(40, "XL");
		map.put(10, "X");
		map.put(9, "IX");
		map.put(5, "V");
		map.put(4, "IV");
		map.put(1, "I");
	}
	
	public static boolean isSimilar(String s1, String s2) {
		if (s1.equalsIgnoreCase(s2)) {
			return true;
		}
		return false;
	}
	
	public static String fromHex(String hex) {
		try {
			byte[] bytes = Hex.decodeHex(hex.toCharArray());
			return new String(bytes, "UTF-8");
		} catch (DecoderException | UnsupportedEncodingException e) {
			return "null";
		}
	}
	
	public static String capitalize(String str) {
		if (str.length() > 0) {
			String newString = str.toUpperCase().substring(0, 1);
			if (str.length() > 1) {
				newString += str.substring(1, str.length());
			}
			return newString;
		} else {
			return str;
		}
	}
	
	public final static String toRoman(int number) {
		int l = map.floorKey(number);
		if (number == l) {
			return map.get(number);
		}
		return map.get(l) + toRoman(number - l);
	}
}
