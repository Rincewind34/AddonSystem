package de.securebit.defaults.commands;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.command.Argument;
import de.securebit.api.command.BasicCommand;
import de.securebit.api.setup.ConsolSetup;
import de.securebit.api.setup.Setup;
import de.securebit.api.setup.SetupManager;
import de.securebit.plugin.CraftCore;

public class ArgumentSetup extends Argument {
	
	private static Map<String, BasicCommand> cmds;
	
	public ArgumentSetup(JavaPlugin manager) {
		super(manager);
		ArgumentSetup.cmds = new HashMap<String, BasicCommand>();
	}

	@Override
	public String getSyntax() {
		return "/bw setup <init|create|delete> <setupname>";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.setup.start";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 3) {
			if (args[1].equals("init")) {
				if (!SetupManager.containsSetup(args[2])) {
					String name = args[2];
					ConsolSetup setup = new ConsolSetup(name);
					CommandSetup cmdSetup = new CommandSetup(name, setup, (CraftCore) super.getPlugin());
					cmdSetup.create();
					SetupManager.initSetup(setup);
					ArgumentSetup.cmds.put(name, cmdSetup);
					sender.sendMessage("§aDas Setup wurde gestartet.");
					return true;
				} else {
					sender.sendMessage("§cDer Name ist schon vergeben!");
					return true;
				}
			} else if (args[1].equals("create")) {
				if (SetupManager.containsSetup(args[2])) {
					String name = args[2];
					String path = "plugins/Core";
					File file = new File(path + File.separator + name + Setup.ENDING);
					
					if (!file.exists()) {
						if (SetupManager.getSetup(name).isCreatable()) {
							SetupManager.createSetup(name, path);
							ArgumentSetup.cmds.get(name).unregister();
							ArgumentSetup.cmds.remove(name);
							sender.sendMessage("§aDas Setup wurde erstellt.");
							return true;
						} else {
							sender.sendMessage("§cDas Setup konnte nicht beendet werden, da es unvollständig ist!");
							return true;
						}
					} else {
						sender.sendMessage("§cDer File '" + path + File.separator + name + Setup.ENDING + "' existiert schon!");
						return true;
					}
				} else {
					sender.sendMessage("§cEs gibt kein Setup mit dem Namen!");
					return true;
				}
			} else if (args[1].equals("delete")) {
				if (SetupManager.containsSetup(args[2])) {
					String name = args[2];
					SetupManager.deleteSetup(name);
					ArgumentSetup.cmds.get(name).unregister();
					ArgumentSetup.cmds.remove(name);
					sender.sendMessage("§aDas Setup wurde geloescht.");
					return true;
				} else {
					sender.sendMessage("§cEs gibt kein Setup mit dem Namen!");
					return true;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
