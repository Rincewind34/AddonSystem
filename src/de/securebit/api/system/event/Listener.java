package de.securebit.api.system.event;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Listener {
	
	private String eventName;
	private String payload;
	
	private Gson gson;
	
	private JsonObject root;
	
	public Listener(String eventName, String payload) {
		this.eventName = eventName;
		this.payload = payload;
		
		this.gson = new Gson();
		this.root = this.gson.fromJson(this.payload, JsonObject.class);
	}
	
	public Object read(String name, Serializer s) {
		JsonObject obj = this.root.get(name).getAsJsonObject();
		return s.unserialize(obj);
	}
	
	public boolean canRead(String name) {
		return this.root.has(name);
	}
	
	public String getEventName() {
		return this.eventName;
	}
	
	public String getPayload() {
		return this.payload;
	}
	
}
