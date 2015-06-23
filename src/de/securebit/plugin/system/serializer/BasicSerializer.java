package de.securebit.plugin.system.serializer;

import com.google.gson.JsonObject;

import de.securebit.api.system.event.Serializer;

public class BasicSerializer {
	
	public BasicSerializer() {
		Serializer.add(String.class, new StringSerializer());
		Serializer.add(Character.class, new CharacterSerializer());
		Serializer.add(Boolean.class, new BooleanSerializer());
		Serializer.add(Byte.class, new ByteSerializer());
		Serializer.add(Short.class, new ShortSerializer());
		Serializer.add(Integer.class, new IntegerSerializer());
		Serializer.add(Long.class, new LongSerializer());
		
		Serializer.add(char.class, new CharacterSerializer());
		Serializer.add(boolean.class, new BooleanSerializer());
		Serializer.add(byte.class, new ByteSerializer());
		Serializer.add(short.class, new ShortSerializer());
		Serializer.add(int.class, new IntegerSerializer());
		Serializer.add(long.class, new LongSerializer());
	}
	
	public static final class StringSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (String) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsString();
		}
		
	}
	
	public static final class IntegerSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (Integer) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsInt();
		}
		
	}
	
	public static final class BooleanSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (Boolean) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsBoolean();
		}
		
	}
	
	public static final class ShortSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (Short) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsShort();
		}
		
	}
	
	public static final class LongSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (Long) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsLong();
		}
		
	}
	
	public static final class CharacterSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (Character) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsCharacter();
		}
		
	}
	
	public static final class ByteSerializer extends Serializer {

		@Override
		public JsonObject serialize(Object obj) {
			JsonObject json = new JsonObject();
			json.addProperty("value", (Byte) obj);
			return json;
		}

		@Override
		public Object unserialize(JsonObject json) {
			return json.get("value").getAsByte();
		}
		
	}
}
