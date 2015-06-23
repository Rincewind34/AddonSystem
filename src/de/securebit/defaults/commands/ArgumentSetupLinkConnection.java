package de.securebit.defaults.commands;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.AddonType;
import de.securebit.api.command.Argument;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.AddonYMLExtractor;

public class ArgumentSetupLinkConnection extends Argument {

	private CommandSetup cmd;
	
	public ArgumentSetupLinkConnection(JavaPlugin manager, CommandSetup cmd) {
		super(manager);
		this.cmd = cmd;
	}

	@Override
	public String getSyntax() {
		return cmd.getName()  + " linkcon <addonname> <conname> <sendername>";
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
		if (args.length == 4) {
			if (this.cmd.getAddons().containsKey(args[1])) {
				AddonYMLExtractor ex = new AddonYMLExtractor(this.getFile(this.cmd.getAddons().get(args[1])));
				AddonYMLExtractor exs = new AddonYMLExtractor(this.getFile(this.cmd.getAddons().get(args[3])));
				
				if (ex.getConnections().contains(args[2])) {
					if (this.cmd.getAddons().containsKey(args[3])) {
						if (exs.getAddonType() == AddonType.SPECIAL || exs.getAddonType() == ex.getConnectionType(args[2])) {
							this.cmd.getSetup().linkConnection(args[2], this.cmd.getAddons().get(args[1]), this.cmd.getAddons().get(args[3]));
							sender.sendMessage("§aDie Connection wurde erfolgreich gelinkt.");
							return true;
						} else {
							sender.sendMessage("§cDer Sender '" + args[3] + "' hat nicht den richtigen AddonType!");
							return true;
						}
					} else {
						sender.sendMessage("§cDas Addon '" + args[3] + "' ist nicht im Setup!");
						return true;
					}
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
	
}
