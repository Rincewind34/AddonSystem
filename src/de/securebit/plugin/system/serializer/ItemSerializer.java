package de.securebit.plugin.system.serializer;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.securebit.api.system.event.Serializer;

public class ItemSerializer {
	
	public ItemSerializer() {
		Serializer.add(Material.class, new MaterialSerializer());
		Serializer.add(MaterialData.class, new MaterialDataSerializer());
	}
	
	public static final class MaterialSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", ((Material) obj).toString());
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return Material.valueOf(json.get("value").getAsString());
		}
		
	}
	
	public static final class MaterialDataSerializer extends Serializer {

		@SuppressWarnings("deprecation")
		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			JsonArray array = new JsonArray();
			
			JsonObject id = new JsonObject();
			id.addProperty("id", ((MaterialData) obj).getItemTypeId());
			array.add(id);
			
			JsonObject data = new JsonObject();
			id.addProperty("data", ((MaterialData) obj).getData());
			array.add(data);
			
			json.add("value", array);
			return json;
		}

		@SuppressWarnings("deprecation")
		@Override
		public Object unserialize(JsonObject json) {
			JsonArray array = json.get("value").getAsJsonArray();
			return new MaterialData(array.get(0).getAsInt(), array.get(1).getAsByte());
		}
		
	}
	
}
