package de.securebit.plugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import de.securebit.api.Addon;
import de.securebit.api.BedWars;
import de.securebit.api.ConnectionManager;
import de.securebit.api.Core;
import de.securebit.api.InfoLayout;
import de.securebit.api.command.Argument;
import de.securebit.api.command.BasicCommand;
import de.securebit.api.command.DefaultExecutor;
import de.securebit.api.gamestates.BasicGameStateManager.GameStates;
import de.securebit.api.gamestates.GameState;
import de.securebit.api.gamestates.GameState.GameStateEnterListener;
import de.securebit.api.gamestates.GameState.GameStateLeaveListener;
import de.securebit.api.io.AddonLoader;
import de.securebit.api.io.Config;
import de.securebit.api.system.event.AddonConnection;
import de.securebit.api.system.shell.Shell;
import de.securebit.defaults.commands.ArgumentConfig;
import de.securebit.defaults.commands.ArgumentGame;
import de.securebit.defaults.commands.ArgumentInfo;
import de.securebit.defaults.commands.ArgumentLog;
import de.securebit.defaults.commands.ArgumentSetup;
import de.securebit.defaults.listener.ListenerBlockBreak;
import de.securebit.defaults.listener.ListenerBlockPlace;
import de.securebit.defaults.listener.ListenerEntityDamage;
import de.securebit.defaults.listener.ListenerFoodLevelChange;
import de.securebit.defaults.listener.Listeners;
import de.securebit.plugin.system.CraftShell;
import de.securebit.plugin.system.serializer.ArraySerializer;
import de.securebit.plugin.system.serializer.BasicSerializer;
import de.securebit.plugin.system.serializer.ItemSerializer;

public final class CraftCore extends JavaPlugin {
	
	protected static SetupLoader setupLoader;
	protected static List<Addon> addons;
	
	protected static boolean channelPipelineOpen = false;
	
	private static BedWars game;
	private static CraftCore instance;
	
	private static Config config;
	private static AddonLoader addonLoader;
	private static ConnectionManager connectionManager;
	
	private static Listeners listeners;
	
	private static BasicCommand command;
	
	private static Shell shell;
	
	@Override
	public void onEnable() {
		CraftCore.shell = new CraftShell();
		
		CraftCore.output("", "Creating and loading config!");
		CraftCore.config = new Config(this);
		CraftCore.config.load();
		CraftCore.config.instance().addDefault("setup.name", "default");
		CraftCore.config.instance().addDefault("setup.path", this.getDataFolder().getPath());
		CraftCore.config.instance().addDefault("game.running", false);
		CraftCore.config.save();
		
		CraftCore.output("", "Creating serializer!");
		new BasicSerializer();
		new ArraySerializer();
		new ItemSerializer();
		
		CraftCore.addons = new ArrayList<Addon>();
		CraftCore.channelPipelineOpen = false;
		
		CraftCore.game = new CraftBedWars(this);
		CraftCore.setupLoader = new SetupLoader(CraftCore.config.instance().getString("setup.path"), CraftCore.config.instance().getString("setup.name"), this);
		
		CraftCore.output("", "Adding bukkit-listeners!");
		CraftCore.listeners = new Listeners(this);
		CraftCore.listeners.addListener(new ListenerBlockBreak());
		CraftCore.listeners.addListener(new ListenerBlockPlace());
		CraftCore.listeners.addListener(new ListenerEntityDamage());
		CraftCore.listeners.addListener(new ListenerFoodLevelChange());
		CraftCore.listeners.register();
		CraftCore.listeners.cancelAll(false);
		
		CraftCore.output("", "Creating bukkit-command 'bw'!");
		CraftCore.command = new BasicCommand("sg");
		CraftCore.command.setAliases("bedwars", "rush");
		CraftCore.command.setDescription("This is the default BedWars-Command");
		CraftCore.command.setKey("bw_core_maincommand");
		CraftCore.command.setDefaultExecutor(new DefaultExecutor() {
			
			@Override
			public boolean onExecute(CommandSender sender, Command cmd, String label, String[] args) {
				InfoLayout layout = new InfoLayout("Commands");
				
				layout.newBarrier();
				
				for (Argument arg : CraftCore.command.getArguments().values()) {
					layout.addComent(arg.getSyntax(), false);
				}
				
				layout.newBarrier();
				layout.send(sender);
				
				return true;
			}
		});
		CraftCore.command.registerArgument("setup", new ArgumentSetup(this));
		CraftCore.command.registerArgument("game", new ArgumentGame(this));
		CraftCore.command.registerArgument("config", new ArgumentConfig(this));
		CraftCore.command.registerArgument("log", new ArgumentLog(this));
		CraftCore.command.registerArgument("info", new ArgumentInfo(this));
		CraftCore.command.create();
		
		if (setupLoader.canLoad()) {
			CraftCore.output("", "Loading Setup (" + setupLoader.getSetupFile().getName() + ")");
			CraftCore.setupLoader.loadSetup();
			
			CraftCore.output("", "Loading addons");
			CraftCore.addonLoader = new AddonLoader(CraftCore.setupLoader.getAddons(), CraftCore.setupLoader.getConfigs());
			CraftCore.addonLoader.loadAddons();
			
			CraftCore.output("", "Initialize connections!");
			CraftCore.connectionManager = new CraftConnectionManager(setupLoader.getConnections());
			
			CraftCore.output("", "Enabling addons");
			for (Addon addon : CraftCore.addonLoader.getAddons()) {
				CraftCore.addons.add(addon);
				System.out.println("[Core] Enabling addon " + addon.getName() + " version " + addon.getVersion());
				addon.onEnable();
			}
			
			CraftCore.output("", "Opening channelpipeline!");
			CraftCore.channelPipelineOpen = true;
			
			for (Addon addon : CraftCore.addons) {
				addon.onChannelOpen();
			}
			
			if (CraftCore.game.isRunning()) {
				CraftCore.output("", "Initialize game");
				((CraftBedWars) CraftCore.game).createGamestates();
				((CraftBedWars) CraftCore.game).createCountdowns();
				
				GameState gsLobby = Core.getGame().getGamestateManager().getState(GameStates.GS_LOBBY);
				if(gsLobby != null) {
					gsLobby.addEnterListener(new GameStateEnterListener() {
						
						@Override
						public void onEnter() {
							CraftCore.listeners.cancelAll(true);
						}
					});
					
					gsLobby.addLeaveListener(new GameStateLeaveListener() {
						
						@Override
						public void onLeave() {
							CraftCore.listeners.cancelAll(false);
						}
					});
				}
				
				for (Addon addon : CraftCore.addons) {
					addon.onGameReady();
				}
				
				Core.getGame().getGamestateManager().next();
			} else {
				CraftCore.output("", "Configurationmode active!");
			}
		} else {
			CraftCore.connectionManager = new CraftConnectionManager(new ArrayList<AddonConnection>());
			
			CraftCore.output("", "No setupfile found, create it!");
		}		
		
	}
	
	@Override
	public void onDisable() {
		for (Addon addon : CraftCore.addons) {
			addon.onDisable();
		}
	}
	
	
	public static ConnectionManager getConnectionManager() {
		return CraftCore.connectionManager;
	}
	
	public static BedWars getGame() {
		return CraftCore.game;
	}
	
	public static void registerArgument(String name, Argument arg) {
		CraftCore.command.registerArgument(name, arg);
	}
	
	public static void output(String sender, String msg) {
		CraftCore.shell.log("Core", sender, msg);
		System.out.println("[Core] [Debug]: " + msg);
	}
	
	public static List<String> getAddons() {
		List<String> list = new ArrayList<String>();
		
		for(Addon addon : CraftCore.addons) list.add(addon.getName());
		
		return list;
	}
	
	public static Config getCfg() {
		return CraftCore.config;
	}
	
	public static Shell getShell() {
		return CraftCore.shell;
	}
	
	protected static Addon getAddon(String name) {
		for (Addon addon : CraftCore.addons) if (addon.getName().equals(name)) return addon;
		return null;
	}

	public static void registerListener(Listener listener) {
		Bukkit.getPluginManager().registerEvents(listener, CraftCore.instance);
	}
	
}
