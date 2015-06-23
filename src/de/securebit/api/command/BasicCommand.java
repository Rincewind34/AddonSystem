package de.securebit.api.command;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;

import de.securebit.api.command.DefaultExecutor;

public class BasicCommand implements CommandExecutor {
	
	private static String VERSION;
	
	static {
		VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}
	
	private HashMap<String, Argument> arguments;
	
	private String name;
	private String description;
	private String key;
	
	private String[] aliases;
	
	private DefaultExecutor executor;
	
	public BasicCommand(String name) {
		this.name = name;
		this.arguments = new HashMap<String, Argument>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase(this.name)) {
			if (args.length != 0) {

				for (String name : this.getArguments().keySet()) {
					if (args[0].equals(name)) {
						Argument arg = this.getArgument(name);
						
						if ((arg.isOnlyForplayer() && sender instanceof Player) || (!arg.isOnlyForplayer())) {
							if (arg.getPermission() == null) {
								if(!this.executeArgument(name, sender, cmd, label, args)) sender.sendMessage("§csyntax: " + arg.getSyntax());
								return true;
							} else {
								if (sender.hasPermission(arg.getPermission())) {
									if(!this.executeArgument(name, sender, cmd, label, args)) sender.sendMessage("§csyntax: " + arg.getSyntax());
									return true;
								} else {
									sender.sendMessage("§cDu hast nicht die Permission!");
									return true;
								}
							}
						} else {
							sender.sendMessage("§cDu kannst dieses Argument nicht ausführen.");
							return true;
						}
					}
				}

				sender.sendMessage("§cBitte tippe /bw fuer die Syntax ein!");
			} else {
				if (executor != null)
					return executor.onExecute(sender, cmd, label, args);
			}
		}

		return true;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setAliases(String... aliases) {
		this.aliases = aliases;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String[] getAliases() {
		return aliases;
	}
	
	public String getName() {
		return name;
	}
	
	public String getKey() {
		return key;
	}

	public void registerArgument(String name, Argument ex) {
		arguments.put(name, ex);
	}

	public HashMap<String, Argument> getArguments() {
		return arguments;
	}

	public Argument getArgument(String name) {
		return this.arguments.get(name);
	}
	
	public void setDefaultExecutor(DefaultExecutor executor) {
		this.executor = executor;
	}
	
	public void create() {
		DynCommand dynCmd = new DynCommand(name, description != null ? description : "", this, aliases != null ? aliases : new String[0]);
		try {
			Class<?> clazzCraftServer = Class.forName("org.bukkit.craftbukkit." + VERSION + ".CraftServer");
			Field field = clazzCraftServer.getDeclaredField("commandMap");
			field.setAccessible(true);
			CommandMap map = (CommandMap) field.get(Bukkit.getServer());
			map.register((key == null || key.isEmpty() || key.trim().isEmpty()) ? name : key, dynCmd);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void unregister() {
		try {
			Class<?> clazzCraftServer = Class.forName("org.bukkit.craftbukkit." + VERSION + ".CraftServer");
			Class<?> clazzCommandMap = Class.forName("org.bukkit.command.SimpleCommandMap");
			
			Field fieldMap = clazzCraftServer.getDeclaredField("commandMap");
			Field fieldCommands = clazzCommandMap.getDeclaredField("knownCommands");
			
			fieldMap.setAccessible(true);
			fieldCommands.setAccessible(true);
			
			SimpleCommandMap map = (SimpleCommandMap) fieldMap.get(Bukkit.getServer());
			Map<String, Command> commands = (Map<String, Command>) fieldCommands.get(map);
			
			commands.remove(((key == null || key.isEmpty() || key.trim().isEmpty()) ? name : key) + ":" + name);
			commands.remove(name);
			
			if (aliases != null) for (String alias : aliases) {
				commands.remove(((key == null || key.isEmpty() || key.trim().isEmpty()) ? alias : key) + ":" + name);
				commands.remove(name);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	private boolean executeArgument(String name, CommandSender sender, Command cmd, String label, String[] args) {
		for (String targetName : arguments.keySet()) {
			if (targetName.equals(name)) {
				Argument argument = arguments.get(name);
				return argument.execute(sender, cmd, label, args);
			}
		}
		
		return true;
	}
	
	private class DynCommand extends Command {
		
		private CommandExecutor exec;
		
		public DynCommand(String name, String description, CommandExecutor exec, String... aliases) {
			super(name);
			super.setDescription(description);
			super.setAliases(Arrays.asList(aliases));
			
			this.exec = exec;
		}

		@Override
		public boolean execute(CommandSender sender, String label, String[] args) {
			return this.exec.onCommand(sender, this, label, args);
		}
		
	}

}
