package de.securebit.api;

import java.io.File;
import java.util.List;

import de.securebit.api.io.AddonConfig;
import de.securebit.api.system.event.EventManager;
import de.securebit.plugin.system.CraftEventManager;

public abstract class Addon {
	
	private String name;
	private String description;
	private String version;
	private List<String> authors;
	private AddonType type;
	private AddonConfig config;
	private File file;
	
	private EventManager eventManager = new CraftEventManager(this);
	
	public final String getName() {
		return this.name;
	}
	
	public final String getDescription() {
		return this.description;
	}
	
	public final String getVersion() {
		return this.version;
	}
	
	public final List<String> getAuthors() {
		return this.authors;
	}
	
	public final AddonType getType() {
		return type;
	}
	
	public final File getFile() {
		return file;
	}
	
	public void onEnable() {}
	
	public void onDisable() {}
	
	public void onGameReady() {}
	
	public void onBroadcastReceive(String subject, String payload) {}
	
	public void onChannelOpen() {}
	
	public AddonConfig onConfigCreate(AddonConfig config) {
		return config;
	}
	
	@SuppressWarnings("unused")
	private void init(String name, String description, String version, AddonType type, List<String> authors, AddonConfig config, File file) {
		this.name = name;
		this.description = description;
		this.version = version;
		this.type = type;
		this.authors = authors;
		this.config = config;
		this.file = file;
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public String getDataFolder() {
		return "plugins" + 
				File.separator + "Core" + 
				File.separator + "addons" + 
				File.separator + name;
	}
	
	public AddonConfig getConfig() {
		return config;
	}
	
}
