package de.securebit.defaults.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.command.Argument;
import de.securebit.api.io.AddonLoader;

public class ArgumentSetupRemoveAddon extends Argument {

	private CommandSetup cmd;
	
	public ArgumentSetupRemoveAddon(JavaPlugin manager, CommandSetup cmd) {
		super(manager);
		this.cmd = cmd;
	}

	@Override
	public String getSyntax() {
		return cmd.getName()  + " remove <filename>";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.setup.removeaddon";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			if (AddonLoader.getFiles().contains(this.getFile(args[1]))) {
				if (this.cmd.getSetup().getAddonsToLoad().contains(args[1])) {
					this.cmd.getSetup().unregisterAddon(args[1]);
					this.cmd.getAddons().remove(args[1]);
					
					sender.sendMessage("§aDer File wurde entfernt.");
					return true;
				} else {
					sender.sendMessage("§cDer File '" + this.getFile(args[1]).getName() + "' ist nicht in dem Setup!");
					return true;
				}
			} else {
				sender.sendMessage("§cDer File '" + this.getFile(args[1]).getName() + "' ist kein Addon!");
				return true;
			}
		} else {
			return false;
		}
	}
	
	private File getFile(String addonFileName) {
		return new File(AddonLoader.ADDON_DIR + File.separator + addonFileName);
	}
	
}
