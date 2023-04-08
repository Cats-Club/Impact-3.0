package me.zero.clarinet.mod;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.event.api.EventManager;

import me.zero.clarinet.Impact;
import me.zero.clarinet.event.game.EventModToggle;
import me.zero.clarinet.user.UserManager;
import me.zero.clarinet.util.Helper;
import me.zero.clarinet.util.render.ColorUtils;
import me.zero.values.types.MultiValue;
import net.minecraft.util.text.TextFormatting;

public class Mod implements Comparable<Mod>, Helper {

	private Category category;
	protected final String name;
	protected String suffix;
	private final String description;
	private boolean toggled;
	private boolean displays;
	private int key;
	private int color;
	
	private MultiValue<String> modeValue;
	
	private ModMode mode = null;
	
	private List<ModMode> modes = null;
	
	public Mod(String name, String description, int key, Category category) {
		this(name, description, key, category, true);
	}
	
	public Mod(String name, String description, int key, Category category, boolean displays) {
		this.name = name;
		this.suffix = "";
		this.description = description;
		this.key = key;
		this.category = category;
		this.displays = displays;
		this.color = ColorUtils.getModColor();
	}
	
	@Override
	public final int compareTo(Mod m) {
		String str1 = getName();
		String str2 = m.getName();
		int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
		if (res == 0) {
			res = str1.compareTo(str2);
		}
		return res;
	}
	
	protected final void setModes(ModMode... modes) {
		if (this.modes == null) {
			this.modes = new ArrayList<>();
		}
		this.modes.clear();
		String[] modeNames = new String[modes.length];
		for (int i = 0; i < modes.length; i++) {
			ModMode mode = modes[i];
			modeNames[i] = mode.getName();
			if (!doesUseModes()) setMode(mode);
			this.modes.add(mode);
		}
		modeValue = new MultiValue<String>(this, "Mode", "mode", modeNames) {
			@Override
			public String setValue(String value) {
				for (ModMode mode : Mod.this.modes) {
					if (value.equalsIgnoreCase(mode.getName())) {
						Mod.this.setMode(mode);
						return super.setValue(value);
					}
				}
				return this.getValue();
			}
		};
	}

    private final boolean doesUseModes() {
		return (modes != null && modes.size() > 0);
	}

    protected final void setMode(ModMode mode) {
		this.suffix = mode.getName();
		
		if (this.mode != null) {
			this.mode.onDisable();
			EventManager.unregister(this.mode);
		}
		
		this.mode = mode;
		
		if (toggled) {
			mode.onEnable();
			EventManager.register(mode);
		}
	}

	public ModMode getMode() {
        return this.mode;
    }
	
	public Mod[] getConflictingMods() {
		return new Mod[0];
	}
	
	public final void toggle() {
		this.toggled = !this.toggled;
		
		EventModToggle event = new EventModToggle(this, toggled);
		EventManager.call(event);
		if (event.isCancelled()) {
			this.toggled = !this.toggled;
			return;
		}
		
		if (toggled) {
			this.color = ColorUtils.getModColor();
			
			EventManager.register(this);
			this.onEnable();
			
			if (mode != null && doesUseModes()) {
				EventManager.register(mode);
				mode.onEnable();
			}
		} else {
			this.onDisable();
			EventManager.unregister(this);
			
			if (mode != null && doesUseModes()) {
				mode.onDisable();
				EventManager.unregister(mode);
			}
		}
	}
	
	public final int getKeybind() {
		return this.key;
	}
	
	public final void setKeybind(int key) {
		this.key = key;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final String getDisplayName() {
		if (this.suffix == null || this.suffix.length() == 0) {
			return this.name;
		}
		String suffix = this.suffix;
		if (this.doesUseModes()) {
			suffix = mode.getName();
		}
		return this.name + " " + TextFormatting.GRAY + "[" + suffix + "]";
	}
	
	public final Category getCategory() {
		return this.category;
	}
	
	public final boolean isToggled() {
		if (Impact.getInstance().isDisabled()) {
			return false;
		}
		return this.toggled;
	}
	
	public final void setDisplays(boolean displays) {
		this.displays = displays;
	}
	
	public final boolean doesDisplay() {
		return this.displays;
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final int getColor() {
		return this.color;
	}
	
	public final boolean canUseMod() {
		return (!(this instanceof PremiumMod) || UserManager.isPremium());
	}
	
	public void onEnable() {}
	
	public void onDisable() {}
}