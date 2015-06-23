package de.securebit.api.io;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import de.securebit.plugin.CraftCore;

public final class Config {

	private String fileName;
	private File file;
	private FileConfiguration config;
	
	public Config(CraftCore core) {
		this(core, "config.yml");
	}
	
	protected Config(CraftCore instance, String fileName) {
		this.fileName = fileName;
		this.file = new File(instance.getDataFolder(), this.fileName);
		this.load();
	}
	
	public void load() {
		this.config = YamlConfiguration.loadConfiguration(this.file);
	}
	
	public FileConfiguration instance() {
		return this.config;
	}
	
	public void save() {
		try {
			this.config.options().copyDefaults(true);
			this.config.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
