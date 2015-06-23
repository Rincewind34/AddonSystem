package de.securebit.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Argument {
	
	private JavaPlugin manager;
	
	public Argument(JavaPlugin manager) {
		this.manager = manager;
	}
	
	public abstract String getSyntax();
	
	public abstract String getPermission();
	
	public abstract boolean isOnlyForplayer();
	
	public abstract boolean execute(CommandSender sender, Command cmd, String label, String[] args);
	
	protected JavaPlugin getPlugin() {
		return manager;
	}
	
}
