package de.securebit.api.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import de.securebit.api.exceptions.InvalidArgumentException;

public class CommandManager {
	
	public static void processMessage(String rawMessage, String[] keys, String[] replacement, CommandSender sender) {
		if(keys.length != replacement.length) {
			try {
				throw new InvalidArgumentException();
			} catch (InvalidArgumentException e) {
				e.printStackTrace();
			}
			return;
		}
		
		for(int i = 0; i < keys.length; i++) {
			rawMessage = rawMessage.replace(keys[i], replacement[i]);
		}
		
		ChatColor.translateAlternateColorCodes('&', rawMessage);
		
		sender.sendMessage(rawMessage);
	}
	
}