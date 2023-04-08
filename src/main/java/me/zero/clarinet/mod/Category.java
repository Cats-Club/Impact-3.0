package me.zero.clarinet.mod;

public enum Category {
	
	COMBAT("Combat", 0xFFFF0000),
	RENDER("Render", 0xFFFFA200),
	MOVEMENT("Movement", 0xFF007CFF),
	PLAYER("Player", 0xFF00FF00),
	WORLD("World", 0xFFFFFF42),
	MISC("Misc", 0xFF7C00FF),
	EXPLOIT("Exploit", 0xFF9AFF57),
	MINIGAME("Minigame", 0xFFFA73FF);
	
	private String name;
	private int color;

	Category(String name, int color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return this.name;
	}
	
	public int getColor() {
		return this.color;
	}
}
