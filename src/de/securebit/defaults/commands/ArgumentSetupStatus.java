package de.securebit.defaults.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.InfoLayout;
import de.securebit.api.command.Argument;
import de.securebit.api.setup.ConsolSetup;

public class ArgumentSetupStatus extends Argument {

	private CommandSetup cmd;
	
	public ArgumentSetupStatus(JavaPlugin manager, CommandSetup cmd) {
		super(manager);
		this.cmd = cmd;
	}

	@Override
	public String getSyntax() {
		return cmd.getName()  + " status";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.setup.status";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			ConsolSetup setup = this.cmd.getSetup();
			InfoLayout layout = new InfoLayout("Setup");
			
			layout.newCategory("General");
			layout.addInfo("Configs", setup.checkConfigs());
			layout.addInfo("Connections", setup.checkConnections());
			layout.addInfo("Finished", setup.isCreatable());
			layout.newCategory("Addons");
			for(String addon : setup.getAddonsToLoad()) {
				layout.addElement(addon, setup.checkAddonConfig(addon) && setup.checkAddonCons(addon));
			}
			layout.newBarrier();
			layout.send(sender);
			
			return true;
		} else {
			return false;
		}
	}
	
}

