package me.zero.clarinet.manager.manager;

import java.awt.Font;

import me.zero.clarinet.ui.font.CFontRenderer;

public class FontManager {

	public static CFontRenderer font_proximity;
    public static CFontRenderer urwgothic;
    public static CFontRenderer urwgothic_hud;
    public static CFontRenderer urwgothic_title;
	
	public static void load() {
		font_proximity = createFont("/forgotten_futurist_bold.ttf", "Forgotten Futurist Rg", Font.BOLD, 18);
        urwgothic = createFont("/urw-gothic-l-book.ttf", "URW Gothic L Book", Font.PLAIN, 30);
        urwgothic_hud = createFont("/urw-gothic-l-book.ttf", "URW Gothic L Book", Font.PLAIN, 18);
        urwgothic_title = createFont("/urw-gothic-l-book.ttf", "URW Gothic L Book", Font.PLAIN, 46);
	}
	
	private static CFontRenderer createFont(String path, String fallback, int style, int size) {
		Font f;
		try {
			f = Font.createFont(0, FontManager.class.getResourceAsStream(path)).deriveFont(style, size);
		} catch (Exception e) {
			f = new Font(fallback, style, size);
		}
		return new CFontRenderer(f, true, false);
	}
}
