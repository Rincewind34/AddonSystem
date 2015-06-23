package de.securebit.defaults.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.command.Argument;
import de.securebit.api.system.shell.Shell;
import de.securebit.plugin.CraftCore;

public class ArgumentLog extends Argument {

	public ArgumentLog(JavaPlugin manager) {
		super(manager);
	}

	@Override
	public String getSyntax() {
		return "/bw log < [create [name]] | [clearcash] >";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.log";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 || args.length == 3) {
			if (args[1].equals("create")) {
				CraftCore.getShell().createLog(Shell.LOG_PATH, ((args.length == 2) ? CraftCore.getShell().createLogName() : args[2]));
				sender.sendMessage("§aDer Log wurde erstellt.");
				return true;
			} else if (args[1].equals("clearcash")) {
				CraftCore.getShell().clearLog();
				sender.sendMessage("§aDer Cash von dem Log wurde geleert.");
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
