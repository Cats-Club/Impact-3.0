package me.zero.clarinet.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.zero.clarinet.util.ClientUtils;

public abstract class Manager<T> {

    private static final List<Manager<?>> managers = new ArrayList<>();

	private List<T> data = new ArrayList<T>();
	
	public Manager(String name) {
		ClientUtils.log("[Init] Loading " + name + " Manager");
        managers.add(this);
	}
	
	public abstract void load();

    public abstract void save();

    public final void reset(){
        this.data.clear();
    }

	protected final void addData(T data) {
		this.data.add(data);
	}
	
	protected final void addData(T... data) {
		this.data.addAll(Arrays.asList(data));
	}
	
	protected final void addData(List<T> data) {
		this.data.addAll(data);
	}
	
	protected final void removeData(T data) {
		this.data.remove(data);
	}
	
	public final <I extends T> I get(Class<I> clazz) {
		for (T data : getData()) {
			if (data.getClass().equals(clazz)) {
				return (I) data;
			}
		}
		return null;
	}
	
	protected final List<T> getData() {
		return this.data;
	}

	public static void saveAll() {
        managers.forEach(Manager::save);
    }
}
