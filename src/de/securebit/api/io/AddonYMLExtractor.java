package de.securebit.api.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import de.securebit.api.AddonType;
import de.securebit.api.exceptions.InvalidAddonYMLException;

public class AddonYMLExtractor extends FileExtractor {
	
	public AddonYMLExtractor(File file) {
		super(file, "addon.yml");
	}

	public void checkConfig() throws InvalidAddonYMLException {
		if (super.cfg == null) throw new InvalidAddonYMLException("Could not extract the addon.yml from '" + file.getPath() + "'! Maybe the file is not an addon!");
		if (!super.cfg.contains("name")) throw new InvalidAddonYMLException("The path 'name' is not given!");
		if (!super.cfg.contains("type")) throw new InvalidAddonYMLException("The path 'type' is not given!");
		if (!super.cfg.contains("main")) throw new InvalidAddonYMLException("The path 'main' is not given!");
		
		if (super.cfg.contains("connections")) {
			ConfigurationSection section = super.cfg.getConfigurationSection("connections");
			List<String> list = new ArrayList<String>();
			
			for (String con : section.getValues(false).keySet()) {
				if (list.contains(con)) 
					throw new InvalidAddonYMLException("The connectionname '" + con + "' is already used by another connection!");
				
				if (!super.cfg.contains("connections." + con + ".type"))
					throw new InvalidAddonYMLException("The path 'connections' requieres for all connections a sender-type!");
				
				list.add(con);
			}
		}
	}
	
	public String getAddonName() {
		return (String) super.cfg.get("name");
	}
	
	public String getAddonVersion() {
		return super.cfg.isString("version") ? super.cfg.getString("version") : "0.0.1";
	}
	
	public AddonType getAddonType() {
		return AddonType.valueOf(((String) super.cfg.get("type")).toUpperCase());
	}
	
	public AddonType getConnectionType(String conName) {
		return AddonType.valueOf(((String) super.cfg.get("connections." + conName + ".type")).toUpperCase());
	}
	
	public List<String> getConnections() {
		List<String> list = new ArrayList<String>();
		
		if (super.cfg.contains("connections")) {
			ConfigurationSection section = cfg.getConfigurationSection("connections");
			for (String con : section.getValues(false).keySet()) list.add(con);
		}
		
		return list;
	}
	
	public List<String> getAuthors() {
		return (super.cfg.isList("authors")) ?  super.cfg.getStringList("authors") :
			Arrays.asList(super.cfg.isString("author") ? super.cfg.getString("author") : "Unkown");
	}
	
	public String getDescription() {
		return super.cfg.isString("description") ? super.cfg.getString("description") : "An addon for BedWars.";
	}
	
	public String getMain() {
		return (String) super.cfg.get("main");
	}
	
}
