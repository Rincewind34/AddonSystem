package de.securebit.api;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import de.securebit.api.command.Argument;
import de.securebit.api.io.Config;
import de.securebit.api.system.shell.Shell;
import de.securebit.plugin.CraftCore;

public class Core {
	
	public static ConnectionManager getConnectionManager() {
		return CraftCore.getConnectionManager();
	}
	
	public static BedWars getGame() {
		return CraftCore.getGame();
	}
	
	public static void registerArgument(String name, Argument arg) {
		CraftCore.registerArgument(name, arg);;
	}
	
	public static void output(String sender, String msg) {
		CraftCore.output(sender, msg);;
	}
	
	public static void debug(String msg) {
		Core.getShell().log("Core", "Debug", msg);
		Bukkit.getConsoleSender().sendMessage("§c[Core] [Debug] " + msg);
	}
	
	public static void registerListener(Listener listener) {
		CraftCore.registerListener(listener);
	}
	
	public static List<String> getAddons() {
		return CraftCore.getAddons();
	}
	
	public static Config getCfg() {
		return CraftCore.getCfg();
	}
	
	public static Shell getShell() {
		return CraftCore.getShell();
	}

	
}
