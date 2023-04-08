package me.zero.clarinet.manager.manager;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.Impact;
import me.zero.clarinet.util.io.FileUtil;
import me.zero.clarinet.manager.Manager;

public class ConfigManager extends Manager<ConfigManager.ConfigValue> {
	
	public ConfigManager() {
		super("Config");
	}

    @Override
    public void load() {
        List<String> data = FileUtil.read(Impact.getInstance().getFile("config"));
        for (String piece : data) {
            String[] split = piece.split(":");
            if (split.length == 2) {
                String key = split[0];
                String value = split[1];
                this.setValue(new ConfigValue(key, value));
            }
        }
    }

    @Override
    public void save() {
        List<String> data = new ArrayList<>();
        for (ConfigValue value : this.getData()) {
            data.add(value.getKey() + ":" + value.getValue());
        }
        FileUtil.write(data, Impact.getInstance().getFile("config"));
    }

	public void setValue(ConfigValue configValue) {
		this.setValue(configValue.getKey(), configValue.getValue());
	}
	
	public void setValue(String key, int value) {
		this.setValue(key, String.valueOf(value));
	}
	
	public void setValue(String key, boolean value) {
		this.setValue(key, String.valueOf(value));
	}
	
	public void setValue(String key, String value) {
		if (getValue(key) != null) {
			int i = 0;
			for (ConfigValue configValue : getConfigValues()) {
				if (configValue.getKey().equalsIgnoreCase(key)) {
					this.getConfigValues().set(i, new ConfigValue(key, value));
				}
				i++;
			}
		} else {
			this.getConfigValues().add(new ConfigValue(key, value));
		}
	}
	
	public boolean getValueBoolean(String key, boolean defaultValue) {
		if (hasValue(key)) {
			return getValueBoolean(key);
		} else {
			return defaultValue;
		}
	}
	
	public boolean getValueBoolean(String key) {
		String val = getValue(key);
		return val.equalsIgnoreCase("true");
	}
	
	public boolean hasValue(String key) {
		return getValue(key) != null;
	}
	
	public String getValue(String key, String defaultValue) {
		if (hasValue(key)) {
			return getValue(key);
		} else {
			return defaultValue;
		}
	}
	
	public String getValue(String key) {
		int i = 0;
		for (ConfigValue configValue : getConfigValues()) {
			if (configValue.getKey().equalsIgnoreCase(key)) {
				return configValue.getValue();
			}
			i++;
		}
		return null;
	}
	
	public int getValueInteger(String key, int defaultValue) {
		int i = 0;
		for (ConfigValue configValue : getConfigValues()) {
			if (configValue.getKey().equalsIgnoreCase(key)) {
				try {
					return Integer.valueOf(configValue.getValue());
				} catch (Exception e) {
				}
			}
			i++;
		}
		return defaultValue;
	}
	
	public List<ConfigValue> getConfigValues() {
		return this.getData();
	}

	public static class ConfigValue {

        public String key, value;

        public ConfigValue(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return value;
        }

        public String toString() {
            return this.key + ":" + this.value;
        }
    }
}
