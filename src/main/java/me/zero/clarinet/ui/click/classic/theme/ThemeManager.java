package me.zero.clarinet.ui.click.classic.theme;

import me.zero.clarinet.ui.click.classic.theme.themes.HuzuniTheme;
import me.zero.clarinet.ui.click.classic.theme.themes.ImpactTheme;
import me.zero.clarinet.ui.click.classic.theme.themes.NodusTheme;
import me.zero.clarinet.ui.click.classic.theme.themes.NodusXITheme;
import me.zero.clarinet.ui.click.classic.theme.themes.ProximityTheme;
import me.zero.clarinet.ui.click.classic.theme.themes.WeepCraftTheme;

public class ThemeManager {
	
	private Theme theme;
	
	public ThemeManager() {
		theme = ETheme.IMPACT.getTheme();
	}
	
	public ETheme[] getThemes() {
		return ETheme.values();
	}
	
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	
	public Theme getCurrentTheme() {
		return theme;
	}
	
	public String getCurrentThemeName() {
		for (ETheme theme : ETheme.values()) {
			if (theme.getTheme() == this.theme) {
				return theme.getName();
			}
		}
		return null;
	}
	
	public enum ETheme {
		IMPACT(new ImpactTheme(), "Impact"),
        PROXIMITY(new ProximityTheme(), "Proximity"),
        NODUSXI(new NodusXITheme(), "NodusXI"),
        HUZUNI(new HuzuniTheme(), "Huzuni"),
        NODUS(new NodusTheme(), "Nodus"),
        WEEPCRAFT(new WeepCraftTheme(), "WeepCraft");
		
		private Theme renderer;
		private String name;
		
		ETheme(Theme renderer, String name) {
			this.name = name;
			this.renderer = renderer;
		}
		
		public Theme getTheme() {
			return this.renderer;
		}

		
		public String getName() {
			return this.name;
		}
	}
}
