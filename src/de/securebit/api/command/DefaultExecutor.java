package de.securebit.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface DefaultExecutor {
	
	boolean onExecute(CommandSender sender, Command cmd, String label, String[] args);
	
}
