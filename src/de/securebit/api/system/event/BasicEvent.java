package de.securebit.api.system.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.securebit.api.exceptions.SerializerException;

public class BasicEvent implements Event {
	
	private String name;
	
	private String payload;
	
	private JsonObject root;
	
	private Gson gson;
	
	public BasicEvent(String name) {
		this.name = name;
		
		this.gson = new Gson();
		this.root = new JsonObject();		
	}
	
	public void write(String property, Object value) {
		if(!Serializer.contains(value.getClass())) throw new SerializerException("No serializer found!");
		else {
			Serializer s = Serializer.get(value.getClass());
			this.root.add(property, s.serialize(value));
		}
	}
	
	public void createPayload() {
		this.payload = gson.toJson(root);
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String getPayload() {
		return payload;
	}
	
}
