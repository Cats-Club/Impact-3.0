package me.zero.values;

import java.util.ArrayList;
import java.util.List;

import me.zero.clarinet.manager.Manager;
import me.zero.values.types.Value;

public class ValueManager extends Manager<Value<?>> {
	
	public static final ValueManager INSTANCE = new ValueManager();
	
	private ValueManager() {
		super("value");
	}
	
	@Override
	public void load() {}

    @Override
    public void save() {}

    /**
	 * Registers the values that belong to the specified Object
	 */
	public void register(Value<?> value) {
		this.addData(value);
	}
	
	/**
	 * Returns all the values that belong to the specified Object
	 */
	public List<Value<?>> getValues(Object object) {
		List<Value<?>> values = new ArrayList<>();
		for (Value value : this.getValues()) {
			if (value.getParent().equals(object)) {
				values.add(value);
			}
		}
		return values;
	}
	
	/**
	 * Returns all registered Values
	 */
	public List<Value<?>> getValues() {
		return this.getData();
	}
}
