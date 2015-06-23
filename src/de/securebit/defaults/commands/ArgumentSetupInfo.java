package de.securebit.defaults.commands;

import java.io.File;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.InfoLayout;
import de.securebit.api.command.Argument;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.AddonYMLExtractor;
import de.securebit.api.setup.ConsolSetup;
import de.securebit.api.system.event.AddonConnection;

public class ArgumentSetupInfo extends Argument {

	private CommandSetup cmd;
	
	public ArgumentSetupInfo(JavaPlugin manager, CommandSetup cmd) {
		super(manager);
		this.cmd = cmd;
	}

	@Override
	public String getSyntax() {
		return cmd.getName()  + " info <addon>";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.setup.info";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2) {
			ConsolSetup setup = this.cmd.getSetup();
			if (setup.getAddonsToLoad().contains(this.cmd.getAddons().get(args[1]))) {
				InfoLayout layout = new InfoLayout("Setup");
				String addon = args[1];
				String file = this.cmd.getAddons().get(addon);
				AddonYMLExtractor ex = new AddonYMLExtractor(new File(AddonLoader.ADDON_DIR + File.separator + file));
				List<AddonConnection> cons = setup.getCons(addon);
				
				layout.newCategory("General");
				layout.addComent("Name: " + addon, false);
				layout.addComent("File: " + file, false);
				layout.addInfo("Config", setup.getConfigsToUse().get(file), setup.getConfigsToUse().get(file) != null);
				layout.addComent("Total Conns: " + Integer.toString(ex.getConnections().size()), false);
				layout.addInfo("Configured Conns", Integer.toString(cons.size()), cons.size() == ex.getConnections().size());
				layout.newCategory("Connections");
				for (String con : ex.getConnections()) {
					layout.addElement(con, this.isInside(con, cons));
					
					if (this.isInside(con, cons)) {
						layout.addComent("- Sender: " + this.getByName(con, cons).getSender(), false);
						layout.addComent("- Open: " + this.getByName(con, cons).isOpen(), false);
					} else {
						layout.addComent("- Type: " + ex.getConnectionType(con), false);
					}
				}
				layout.newBarrier();
				layout.send(sender);
				
				return true;
			} else {
				sender.sendMessage("§cNo addon with the name '" + args[1] + "' found!");
				return true;
			}
		} else {
			return false;
		}
	}
	
	private boolean isInside(String conname, List<AddonConnection> cons) {
		return this.getByName(conname, cons) != null;
	}
	
	private AddonConnection getByName(String conname, List<AddonConnection> cons) {
		for (AddonConnection con : cons) {
			if (con.getName().equals(conname)) return con;
		}
		
		return null;
	}
	
}

