package de.securebit.plugin.system.serializer;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.securebit.api.system.event.Serializer;

public class ArraySerializer {
	
	public ArraySerializer() {
		Serializer.add(String[].class, new StringSerializer());
	}
	
	public static final class StringSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject root = new JsonObject();
			JsonArray array = new JsonArray();
			String[] objArray = (String[]) obj;
			
			for(String s : objArray) {
				JsonObject json = new JsonObject();
				json.addProperty("value", s);
				array.add(json);
			}
			
			root.add("value", array);
			return root;
		}

		@Override
		public Object unserialize(JsonObject root) {
			JsonArray array = root.get("value").getAsJsonArray();
			List<String> list = new ArrayList<String>();
			
			for (int i = 0; i < array.size(); i++) list.add(array.get(i).getAsJsonObject().get("value").getAsString());
			
			String[] obj = new String[list.size()];
			for (int i = 0; i < list.size(); i++) obj[i] = list.get(i);
			return obj;
		}
		
	}

	
}
