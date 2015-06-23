package de.securebit.defaults.commands;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.Addon;
import de.securebit.api.Core;
import de.securebit.api.InfoLayout;
import de.securebit.api.command.Argument;
import de.securebit.api.system.event.AddonConnection;
import de.securebit.plugin.CraftCore;

public class ArgumentInfo extends Argument {

	public ArgumentInfo(JavaPlugin manager) {
		super(manager);
	}

	@Override
	public String getSyntax() {
		return "/bw info [addonname]";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.info";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}

	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			InfoLayout layout = new InfoLayout("Info");
			JavaPlugin plugin = super.getPlugin();
			
			layout.newCategory("General");
			layout.addComent("Version: " + plugin.getDescription().getVersion() , false);
			layout.addComent("Authors: " + plugin.getDescription().getAuthors().toString(), false);
			layout.addComent("Addoncount: " + Integer.toString(Core.getAddons().size()), false);
			layout.addInfo("Game", Core.getGame().isRunning());
			layout.newCategory("Addons");
			for (String addon : Core.getAddons()) {
				layout.addComent(addon, false);
			}
			layout.newBarrier();
			layout.send(sender);
			
			return true;
		} else if (args.length == 2) {
			if (CraftCore.getAddons().contains(args[1])) {
				Addon addon = this.getAddon(args[1]);
				InfoLayout layout = new InfoLayout("Info");
				
				layout.newCategory("General");
				layout.addInfo("Name", addon.getName(), true);
				layout.addInfo("Version", addon.getVersion(), true);
				layout.addInfo("Type", addon.getType().toString(), true);
				layout.addInfo("Author(s)", addon.getAuthors().toString(), true);
				layout.addInfo("Config", addon.getConfig().getName(), true);
				layout.newCategory("Connections");
				for(AddonConnection con : CraftCore.getConnectionManager().getConnections(addon)) {
					layout.addInfo(con.getName(), con.getSender(), con.isOpen());
				}
				layout.newBarrier();
				layout.send(sender);
				
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private Addon getAddon(String name) {
		try {
			Method method = CraftCore.class.getDeclaredMethod("getAddon", new Class[] { String.class });
			method.setAccessible(true);
			return (Addon) method.invoke(null, new Object[] { name });
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
}
