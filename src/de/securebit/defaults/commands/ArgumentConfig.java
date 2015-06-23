package de.securebit.defaults.commands;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.Addon;
import de.securebit.api.command.Argument;
import de.securebit.api.io.AddonConfig;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.AddonYMLExtractor;
import de.securebit.api.io.ConfigYMLExtractor;
import de.securebit.plugin.CraftCore;

public class ArgumentConfig extends Argument {

	public ArgumentConfig(JavaPlugin manager) {
		super(manager);
	}

	@Override
	public boolean execute(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (args.length == 3 || args.length == 4) {
			if (args[1].equalsIgnoreCase("load")) {
				if (args.length == 3) {
					if (CraftCore.getAddons().contains(args[2])) {
						Addon addon = this.getAddon(args[2]);
						addon.getConfig().load();
						return true;
					} else {
						sender.sendMessage("§cThe addon '" + args[2] + "' does not exists!");
						return true;
					}
				} else {
					return false;
				}
			} else if (args[1].equalsIgnoreCase("create")) {
				if (args.length == 4) {
					ConfigYMLExtractor extractor = new ConfigYMLExtractor(this.getFile(args[2]));
					AddonYMLExtractor ex = new AddonYMLExtractor(this.getFile(args[2]));
					
					extractor.cloneConfig(AddonConfig.getFile(ex.getAddonName(), args[3]));
					sender.sendMessage("§aThe config was created!");
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public String getSyntax() {
		return "/sg config <[load <addonname>]|[create <filename> <configname>]>";
	}

	@Override
	public String getPermission() {
		return "core.bedwars.config";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
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
	
	private File getFile(String addonFileName) {
		return new File(AddonLoader.ADDON_DIR + File.separator + addonFileName);
	}
	
}
