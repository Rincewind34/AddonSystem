package de.securebit.plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import de.securebit.api.Core;
import de.securebit.api.exceptions.InvalidAddonYMLException;
import de.securebit.api.exceptions.SetupException;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.AddonYMLExtractor;
import de.securebit.api.io.ConfigYMLExtractor;
import de.securebit.api.setup.Setup;
import de.securebit.api.system.event.AddonConnection;

public class SetupLoader {

	private final List<AddonConnection> connections;
	private final List<String> addons;
	private final Map<String, String> configs;
	
	private File setupFile;
	private Gson gson;
	
	public SetupLoader(String setupFilePath, String setupFileName, CraftCore core) {
		this.connections = new ArrayList<AddonConnection>();
		this.addons = new ArrayList<String>();
		this.configs = new HashMap<String, String>();
		
		this.setupFile = new File(setupFilePath + File.separator + setupFileName + Setup.ENDING);
		this.gson = new Gson();
	}
	
	protected List<AddonConnection> getConnections() {
		return connections;
	}
	
	public List<String> getAddons() {
		return addons;
	}
	
	public Map<String, String> getConfigs() {
		return configs;
	}
	
	public void loadSetup() {
		Core.getShell().log("Core", "SetupLoader", "Setup is loaded!");
		
		if (this.canLoad()) {
			String json = this.getContent(setupFile);
			JsonObject root = gson.fromJson(json, JsonObject.class);
			
			if(!root.has("addons"))
				throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The array 'addons' is missing!");
			if(!root.has("connections"))
				throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The array 'connections' is missing!");
			
			JsonArray addonArray = root.get("addons").getAsJsonArray();
			JsonArray conArray = root.get("connections").getAsJsonArray();
			
			for (int i = 0; i < addonArray.size(); i++) {
				try {
					JsonObject addon = (JsonObject) addonArray.get(i);
					
					if(!addon.has("name"))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The object 'name' in the '"
								+ i + "-addon' is missing!");
					if(!addon.has("config"))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The object 'config' in the '"
								+ i + "-addon' is missing!");
					
					String name = addon.get("name").getAsString();
					String config = addon.get("config").getAsString();
					
					if (this.addons.contains(name))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The addon '" + name + "' was" +
								" already loaded!");
					
					AddonYMLExtractor ex = new AddonYMLExtractor(this.getFile(name));
					ConfigYMLExtractor cx = new ConfigYMLExtractor(this.getFile(name));
					
					if (!ex.isExtracted())
						throw new InvalidAddonYMLException("Could not extract the addon.yml from '" + this.getFile(name).getPath() + "'!");
					if (!cx.isExtracted())
						throw new InvalidAddonYMLException("Could not extract the config.yml from '" + this.getFile(name).getPath() + "'!");
					ex.checkConfig();
					
					this.addons.add(name);
					this.configs.put(name, config);
					Core.getShell().log("Core", "SetupLoader", "Addon '" + this.getFile(name) + "' was registered with the config '" + config + "'!");
				} catch (Exception ex) {
					ex.printStackTrace();
					Core.getShell().log("Core", "SetupLoader", "Exception: " + ex.getMessage());
					Core.output("[SetupLoader]", "Skiping addon " + i + "!");
					continue;
				}
			}	
			
			for (int i = 0; i < conArray.size(); i++) {
				try {
					JsonObject conObj = (JsonObject) conArray.get(i);
					
					if(!conObj.has("receiver"))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The object 'receiver' in the '"
								+ i + "-connection' is missing!");
					if(!conObj.has("sender"))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The object 'sender' in the '"
								+ i + "-connection' is missing!");
					if(!conObj.has("name"))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The object 'name' in the '"
								+ i + "-connection' is missing!");
					if(!conObj.has("open"))
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The object 'open' in the '"
									+ i + "-connection' is missing!");
					
					String receiver = conObj.get("receiver").getAsString();
					String sender = conObj.get("sender").getAsString();
					String name = conObj.get("name").getAsString();
					Boolean open = conObj.get("open").getAsBoolean();
					
					List<String> addonnames = new ArrayList<String>();
					
					for (String addon : addons) {
						addonnames.add(new AddonYMLExtractor(this.getFile(addon)).getAddonName());
					}
					
					if (!addonnames.contains(receiver)) 
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The 'receiver' in the '"
								+ i + "-connection' was not loaded as addon!");
					if (!addonnames.contains(sender) && open) 
						throw new SetupException("The setupfile '" + this.setupFile.getPath() + "' could not be loaded! The 'sender' in the '"
								+ i + "-connection' was not loaded as addon!");
					
					this.connections.add(new AddonConnection(name, sender, receiver, open));
					Core.getShell().log("Core", "SetupLoader",
							"AddonConnection:[name=" + name + ",receiver=" + receiver + ",sender=" + sender + ",open=" + open + "] added!");
				} catch (Exception ex) {
					ex.printStackTrace();
					Core.getShell().log("Core", "SetupLoader", "Exception: " + ex.getMessage());
					Core.output("[SetupLoader]", "Skiping connection " + i  +"!");
					continue;
				}
			}
			
		} else {
			throw new SetupException("The file '" + this.setupFile.getName() + "' could not be loaded as setup!");
		}
		
		Core.getShell().log("Core", "SetupLoader", "Setup was loaded!");
	}
	
	public boolean canLoad() {
		return this.setupFile.exists() && this.setupFile.isFile();
	}
	
	public File getSetupFile() {
		return this.setupFile;
	}
	
	private String getContent(File f) {
		StringBuilder contentBuilder = new StringBuilder();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			String currentLine = null;
			
			while ((currentLine = reader.readLine()) != null) {
				contentBuilder.append(currentLine);
			}
			
			reader.close();
		} catch (FileNotFoundException e) {
			Core.output("[SetupLoader]", "The file '" + f.getPath() + "could not be found!");
			e.printStackTrace();
		} catch (IOException e) {
			Core.output("[SetupLoader]", "The file '" + f.getPath() + "could not be loaded!");
			e.printStackTrace();
		}
		
		return contentBuilder.toString();
	}
	
	private File getFile(String addonFileName) {
		return new File(AddonLoader.ADDON_DIR + File.separator + addonFileName);
	}
	
}