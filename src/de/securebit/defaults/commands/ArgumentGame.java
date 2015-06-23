package de.securebit.defaults.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.Core;
import de.securebit.api.InfoLayout;
import de.securebit.api.command.Argument;

public class ArgumentGame extends Argument {

	private static InfoLayout layout;
	
	public ArgumentGame(JavaPlugin manager) {
		super(manager);
		ArgumentGame.layout = new InfoLayout("GameInfo");
		ArgumentGame.layout.newCategory("General");
		ArgumentGame.layout.addInfo("Isrunning", Core.getGame().isRunning());
	}

	@Override
	public boolean execute(final CommandSender sender, Command cmd, String label, final String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("info")) {
				ArgumentGame.layout.newBarrier();
				ArgumentGame.layout.send(sender);
				return true;
			} else if (args[1].equalsIgnoreCase("start")) {
				if (!Core.getGame().isRunning()) {
					Core.getGame().setRunning(true);
					sender.sendMessage("§aThe game will be started after the next realod.");
					return true;
				} else {
					sender.sendMessage("§cThe game is already started!");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("stop")) {
				if (Core.getGame().isRunning()) {
					Core.getGame().setRunning(false);
					sender.sendMessage("§aThe game will be stoped after the next realod.");
					return true;
				} else {
					sender.sendMessage("§cThe game is already stoped!");
					return true;
				}
			} else if (args[1].equalsIgnoreCase("next")) {
				if (Core.getGame().isRunning()) {
					Core.getGame().getGamestateManager().next();
					sender.sendMessage("§aNext GameState.");
					return true;
				} else {
					sender.sendMessage("§cThe game is stoped!");
					return true;
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
		return "/sg game <start|stop|info|next>";
	}

	@Override
	public String getPermission() {
		return "core.sg.game";
	}

	@Override
	public boolean isOnlyForplayer() {
		return false;
	}
	
	public static InfoLayout getLayout() {
		return layout;
	}
	
}
