package de.securebit.api.io;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigYMLExtractor extends FileExtractor {
	
	public ConfigYMLExtractor(File file) {
		super(file, "config.yml");
	}

	public void cloneConfig(File file) {
		if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		try {
			super.cfg.options().copyDefaults(true);
			super.cfg.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public FileConfiguration getExtraction() {
		return super.cfg;
	}
	
}
