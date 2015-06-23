package de.securebit.api.setup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.securebit.api.AddonType;

public class DefaultSetup {
	
//	private Map<String, AddonType> addons;
	
	public DefaultSetup(Map<String, AddonType> addons) {
//		this.addons = addons;
	}
	
	public void init() {
		
	}
	
	public void create(String name, String path) {
		File file = new File(path + File.separator + name + Setup.ENDING);
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
			
			Gson gson = new Gson();
			JsonObject root = new JsonObject();
			JsonArray addons = new JsonArray();
			
			{
				JsonObject nexus = new JsonObject();
				nexus.addProperty("name", "nexus");
				nexus.addProperty("config", "cfg1");
				addons.add(nexus);
				
				JsonObject shop = new JsonObject();
				shop.addProperty("name", "shop");
				shop.addProperty("config", "cfg1");
				addons.add(shop);
			}
			
			JsonArray connections = new JsonArray();
			
			{
				JsonObject con = new JsonObject();
				con.addProperty("name", "con1");
				con.addProperty("sender", "nexus");
				con.addProperty("receiver", "shop");
				con.addProperty("open", true);
				connections.add(con);
			}
			
			root.add("addons", addons);
			root.add("connections", connections);
			
			String json = gson.toJson(root);
			byte[] string = json.getBytes("UTF-8");
			ByteArrayInputStream inStream = new ByteArrayInputStream(string, 0, string.length);
			ByteStreams.copy(inStream, new FileOutputStream(file));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
