package de.securebit.defaults.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.securebit.api.command.Argument;
import de.securebit.api.command.BasicCommand;
import de.securebit.api.command.DefaultExecutor;
import de.securebit.api.setup.ConsolSetup;
import de.securebit.plugin.CraftCore;

public class CommandSetup extends BasicCommand implements DefaultExecutor {

	private Map<String, String> addons;
	
	private ConsolSetup setup;
	
	public CommandSetup(String name, ConsolSetup setup, CraftCore core) {
		super(name);
		this.setDefaultExecutor(this);
		this.setup = setup;
		this.addons = new HashMap<String, String>();
		
		this.registerArgument("add", new ArgumentSetupAddAddon(core, this));
		this.registerArgument("remove", new ArgumentSetupRemoveAddon(core, this));
		this.registerArgument("linkconfig", new ArgumentSetupLinkConfig(core, this));
		this.registerArgument("linkcon", new ArgumentSetupLinkConnection(core, this));
		this.registerArgument("closecon", new ArgumentSetupCloseConnection(core, this));
		this.registerArgument("status", new ArgumentSetupStatus(core, this));
		this.registerArgument("info", new ArgumentSetupInfo(core, this));
	}
	
	@Override
	public boolean onExecute(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage("§3=================================================");
		
		for(Argument arg : this.getArguments().values()) {
			sender.sendMessage("§3" + arg.getSyntax());
		}
		
		sender.sendMessage("§3=================================================");
		return true;
	}
	
	public Map<String, String> getAddons() {
		return addons;
	}
	
	public ConsolSetup getSetup() {
		return setup;
	}

}
