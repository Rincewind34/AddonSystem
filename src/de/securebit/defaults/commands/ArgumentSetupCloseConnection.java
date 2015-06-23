package de.securebit.defaults.commands;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.command.Argument;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.AddonYMLExtractor;

public class ArgumentSetupCloseConnection extends Argument {

	private CommandSetup cmd;
	
	public ArgumentSetupCloseConnection(JavaPlugin manager, CommandSetup cmd) {
		super(manager);
		this.cmd = cmd;
	}

	@Override
	public String getSyntax() {
		return cmd.getName()  + " closecon <addonname> <conname>";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.setup.closecon";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 3) {
			if (this.cmd.getAddons().containsKey(args[1])) {
				if (this.getDeclaredCons(this.cmd.getAddons().get(args[1])).contains(args[2])) {
					this.cmd.getSetup().closeConnection(args[2], this.cmd.getAddons().get(args[1]));
					sender.sendMessage("§aDie Connection wurde geschlossen.");
					return true;
				} else {
					sender.sendMessage("§cDas Addon '" + args[1] + "' hat keine Connection '" + args[2] + "'!");
					return true;
				}
			} else {
				sender.sendMessage("§cDas Addon '" + args[1] + "' ist nicht im Setup!");
				return true;
			}
		} else {
			return false;
		}
	}
	
	private File getFile(String addonFileName) {
		return new File(AddonLoader.ADDON_DIR + File.separator + addonFileName);
	}
	
	private List<String> getDeclaredCons(String fileName) {
		return new AddonYMLExtractor(this.getFile(fileName)).getConnections();
	}
	
}
