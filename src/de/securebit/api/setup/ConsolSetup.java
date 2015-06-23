package de.securebit.api.setup;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.ByteStreams;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.securebit.api.AddonType;
import de.securebit.api.exceptions.SetupException;
import de.securebit.api.io.AddonConfig;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.AddonYMLExtractor;
import de.securebit.api.system.event.AddonConnection;

public class ConsolSetup implements Setup {
	
	private List<String> fileNames;
	private List<AddonConnection> cons;
	
	private Map<String, String> configs;
	
	private String name;
	
	public ConsolSetup(String name) {
		this.fileNames = new ArrayList<String>();
		this.cons = new ArrayList<AddonConnection>();
		this.configs = new HashMap<String, String>();
		this.name = name;
	}
	
	@Override
	public void linkConfig(String fileName, String cfgName) {
		AddonYMLExtractor ex  = new AddonYMLExtractor(this.getFile(fileName));
		String addonName = ex.getAddonName();
		
		if (!ex.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileName) + "'!");
		if (!this.fileNames.contains(fileName)) throw new SetupException("The addon '" + addonName + "' is not registered!");
		else {
			File file = new File(AddonConfig.getPath(addonName) + File.separator + cfgName + AddonConfig.ENDING);
			if(!file.exists() || !file.isFile()) throw new SetupException("The config '" + cfgName + "' for the addon '"
					+ addonName + "' could not be found!");
			else this.configs.put(fileName, cfgName);
		}
	}
	
	@Override
	public void linkConnection(String conName, String fileName, String fileSender) {
		AddonYMLExtractor ex  = new AddonYMLExtractor(this.getFile(fileName));
		AddonYMLExtractor exs  = new AddonYMLExtractor(this.getFile(fileSender));
		String addonName = ex.getAddonName();
		
		if (!ex.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileName) + "'!");
		if (!exs.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileSender) + "'!");
		if (!this.fileNames.contains(fileName)) throw new SetupException("The addon '" + addonName + "' is not registered!");
		else {
			for (AddonConnection con : this.getCons(addonName)) {
				if (con.getName().equals(conName)) this.cons.remove(con);
			}
			
			if (!ex.getConnections().contains(conName))
				throw new SetupException("The connections '" + conName + "' from the addon '" + addonName + "' is not declared!");
			
			if (!this.fileNames.contains(fileSender)) 
				throw new SetupException("The sender '" + exs.getAddonName() + "' is not registered!");
			
			if (exs.getAddonType() != AddonType.SPECIAL && exs.getAddonType() != ex.getConnectionType(conName))
				throw new SetupException("The sender of the connection '" + conName + "' could not be an " + exs.getAddonType() + "-Addon!");
			
			this.cons.add(new AddonConnection(conName, exs.getAddonName(), addonName, true));
		}
	}
	
	@Override
	public void registerAddon(String fileName) {
		AddonYMLExtractor ex  = new AddonYMLExtractor(this.getFile(fileName));
		String addonName = ex.getAddonName();
		
		if (!ex.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileName) + "'!");
		if(!AddonLoader.getFiles().contains(this.getFile(fileName))) throw new SetupException("The addon '" + fileName + "' could not be found!");
		if(fileNames.contains(fileName)) throw new SetupException("The addon '" + addonName + "' is already registered!");
		else {
			this.fileNames.add(fileName);
		}
	}
	
	@Override
	public void closeConnection(String conName, String fileName) {
		AddonYMLExtractor ex  = new AddonYMLExtractor(this.getFile(fileName));
		String addonName = ex.getAddonName();
		
		if (!ex.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileName) + "'!");
		if (!this.fileNames.contains(fileName)) throw new SetupException("The addon '" + addonName + "' is not registered!");
		else {
			for (AddonConnection con : this.getCons(addonName)) {
				if (con.getName().equals(conName)) this.cons.remove(con);
			}
			
			if (!ex.getConnections().contains(conName))
				throw new SetupException("The connections '" + conName + "' from the addon '" + addonName + "' is not declared!");
			
			this.cons.add(new AddonConnection(conName, "NONE", addonName, false));
		}
	}
	
	@Override
	public void create(String path) {
		if (!this.isCreatable()) throw new SetupException("The setup '" + this.name + "' could not be created!");
		
		Gson gson = new Gson();
		JsonObject root = new JsonObject();
		JsonArray addons = new JsonArray();
		JsonArray connections = new JsonArray();
		
		File file = new File(path + File.separator + this.name + Setup.ENDING);
		
		if (file.exists() && file.isFile()) throw new SetupException("The setup '" + this.name + "' could not be created! The file already exists!");
		if (!file.getParentFile().exists()) file.mkdirs();
		
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			throw new SetupException("The setup '" + this.name + "' could not be created!");
		}
		
		for (String fileName : this.fileNames) {
			JsonObject obj = new JsonObject();
			obj.addProperty("name", fileName);
			obj.addProperty("config", this.configs.get(fileName));
			addons.add(obj);
		}
		
		for (AddonConnection con : this.cons) {
			JsonObject obj = new JsonObject();
			obj.addProperty("name", con.getName());
			obj.addProperty("sender", con.getSender());
			obj.addProperty("receiver", con.getReceiver());
			obj.addProperty("open", con.isOpen());
			connections.add(obj);
		}
		
		root.add("addons", addons);
		root.add("connections", connections);
		
		byte[] string = null;
		
		try {
			string = gson.toJson(root).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ByteArrayInputStream inStream = new ByteArrayInputStream(string, 0, string.length);
		
		try {
			ByteStreams.copy(inStream, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new SetupException("The setup '" + this.name + "' could not be created! The file does not exists!");
		} catch (IOException e) {
			e.printStackTrace();
			throw new SetupException("The setup '" + this.name + "' could not be created!");
		}
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean isCreatable() {
		return this.checkConfigs() && this.checkConfigs();
	}

	@Override
	public boolean checkConnections() {
		for (String fileName : this.fileNames) {
			if (!this.checkAddonCons(fileName)) return false;
		}
		
		return true;
	}

	@Override
	public boolean checkConfigs() {
		for (String fileName : this.fileNames) {
			if (!this.checkAddonConfig(fileName)) return false;
		}
		
		return true;
	}
	
	public boolean checkAddonConfig(String fileName) {
		if (!this.configs.containsKey(fileName)) return false;
		else return true;
	}
	
	public boolean checkAddonCons(String fileName) {
		AddonYMLExtractor ex = new AddonYMLExtractor(this.getFile(fileName));
		
		if (!ex.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileName) + "'!");
		
		String addon = ex.getAddonName();
		List<String> cons = new ArrayList<String>();
		List<String> declCons = new ArrayList<String>();
		
		for (AddonConnection con : this.getCons(addon)) cons.add(con.getName());
		for (String s : ex.getConnections()) declCons.add(s);
		
		for (String s : declCons) {
			if(!cons.contains(s)) return false;
		}
		
		return true;
	}
	
	public void unregisterAddon(String fileName) {
		AddonYMLExtractor ex = new AddonYMLExtractor(this.getFile(fileName));
		
		if (!ex.isExtracted()) throw new SetupException("Could not extract the addon.yml from '" + this.getFile(fileName) + "'!");
		
		if (!this.fileNames.contains(fileName)) throw new SetupException("The addon '" + ex.getAddonName() + "' is not registered!");
		else {
			this.fileNames.remove(fileName);
			if (this.configs.containsKey(fileName)) this.configs.remove(fileName);
			for (int i = 0; i < this.cons.size();) {
				if(this.cons.get(i).getReceiver().equals(ex.getAddonName())) this.cons.remove(this.cons.get(i));
				else i++;
			}
		}
	}

	public List<String> getAddonsToLoad() {
		return this.fileNames;
	}
	
	public List<AddonConnection> getConsToActivate() {
		return this.cons;
	}
	
	public Map<String, String> getConfigsToUse() {
		return this.configs;
	}
	
	private File getFile(String addonFileName) {
		return new File(AddonLoader.ADDON_DIR + File.separator + addonFileName);
	}
	
	public List<AddonConnection> getCons(String addonName) {
		List<AddonConnection> list = new ArrayList<AddonConnection>();
		
		for(AddonConnection con : this.cons) {
			if(con.getReceiver().equals(addonName)) list.add(con);
		}
		
		return list;
	}

}
