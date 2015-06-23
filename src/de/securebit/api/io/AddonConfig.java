package de.securebit.api.io;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.securebit.plugin.CraftCore;

public final class AddonConfig {
	
	public static final String ENDING = ".config";
	
	private File file;
	
	private FileConfiguration cfg;
	
	private String cfgName;
	private String addonName;
	
	public AddonConfig(String addonName, String cfgName) {
		this.cfgName = cfgName;
		this.addonName = addonName;
		
		this.file = new File(AddonConfig.getPath(addonName) + File.separator + this.cfgName + AddonConfig.ENDING);
	}
	
	public boolean check() {
		return this.file.exists() && this.file.isFile();
	}
	
	public void load() {
		this.cfg = YamlConfiguration.loadConfiguration(this.file);
	}
	
	public void save() {
		try {
			this.cfg.options().copyDefaults(true);
			this.cfg.save(this.file);
		} catch (IOException ex) {
			ex.printStackTrace();
			CraftCore.output("[AddonConfig]", "The config '" + this.cfgName + "' for the addon '" + this.addonName + "' could not be saved!");
		}
	}
	
	public Object get(String path) {
		return this.cfg.get(path);
	}
	
	public String getName() {
		return this.cfgName;
	}
	
	public String getOwner() {
		return this.addonName;
	}
	
	public File getFile() {
		return file;
	}
	
	public static String getPath(String addon) {
		return "plugins" + File.separator + "Core" + File.separator + "addons" + File.separator + addon + File.separator + "configs";
	}
	
	public static File getFile(String addon, String cfgname) {
		return new File(AddonConfig.getPath(addon) + File.separator + cfgname + AddonConfig.ENDING);
	}
	
}
