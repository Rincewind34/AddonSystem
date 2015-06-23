package de.securebit.defaults.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.command.Argument;
import de.securebit.api.io.AddonConfig;

public class ArgumentSetupLinkConfig extends Argument {

	private CommandSetup cmd;
	
	public ArgumentSetupLinkConfig(JavaPlugin manager, CommandSetup cmd) {
		super(manager);
		this.cmd = cmd;
	}

	@Override
	public String getSyntax() {
		return cmd.getName()  + " linkconfig <addonname> <configname>";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.setup.linkconfig";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 3) {
			if (this.cmd.getAddons().containsKey(args[1])) {
				File file = new File(AddonConfig.getPath(args[1]) + File.separator + args[2] + AddonConfig.ENDING);
				if (file.exists() && file.isFile()) {
					this.cmd.getSetup().linkConfig(this.cmd.getAddons().get(args[1]), args[2]);
					sender.sendMessage("§aDie Config wurde erfolgreich gelinkt.");
					return true;
				} else {
					sender.sendMessage("§cDie Config '" + args[2] + "' konnte nicht gefunden werden!");
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
	
}
