package me.zero.clarinet.mod.misc.anticheat.check;

import me.zero.clarinet.mod.misc.anticheat.data.CheckData;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class Check<T extends CheckData> {

    private final Class<T> type;

    private String id;

    @SuppressWarnings("unchecked")
    public Check(String id) {
        this.id = id;

        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            this.type = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            this.type = (Class<T>) CheckData.class;
        }
    }

    public String getID() {
        return this.id;
    }

    public abstract boolean check(T data);

    public Class<T> getType() {
        return this.type;
    }
}
