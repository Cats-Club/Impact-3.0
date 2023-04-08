package me.zero.clarinet.plugin;

public abstract class Plugin {

    private String name, id, description;

    public Plugin(String name, String id, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
    }
	
	public abstract void onEnable();
	
	public abstract void onDisable();

    public final String getName() {
        return this.name;
    }

    public final String getID() {
        return this.id;
    }

    public String getDescription() {
        return description;
    }
}
