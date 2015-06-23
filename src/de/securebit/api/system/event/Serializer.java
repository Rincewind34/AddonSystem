package de.securebit.api.system.event;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import de.securebit.api.exceptions.SerializerException;

public abstract class Serializer {
	
	public abstract JsonObject serialize(Object obj);
	
	public abstract Object unserialize(JsonObject json);
	
	
	private static final Map<Class<?>, Serializer> serializer = new HashMap<Class<?>, Serializer>();
	
	public static final void add(Class<?> key, Serializer s) {
		if(Serializer.serializer.containsKey(key)) throw new SerializerException("Unable to register Serializer! The key already exists!");
		else Serializer.serializer.put(key, s);
	}
	
	public static final Serializer get(Class<?> key) {
		return Serializer.serializer.get(key);
	}
	
	public static final boolean contains(Class<?> key) {
		return Serializer.serializer.containsKey(key);
	}
	
}
