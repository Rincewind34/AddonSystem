package de.securebit.defaults.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

public class Listeners {
	
	private List<ListenerDefault> listeners;
	
	private JavaPlugin plugin;
	
	public Listeners(JavaPlugin plugin) {
		this.listeners = new ArrayList<ListenerDefault>();
		this.plugin = plugin;
	}
	
	public void register() {
		for(ListenerDefault listener : this.listeners) {
			this.plugin.getServer().getPluginManager().registerEvents(listener, this.plugin);
		}
	}
	
	public void addListener(ListenerDefault listener) {
		this.listeners.add(listener);
	}
	
	public ListenerDefault getListener(Class<? extends ListenerDefault> clazz) {
		for(ListenerDefault listener : this.listeners) {
			if(listener.getClass().equals(clazz)) {
				return listener;
			}
		}
		
		return null;
	}
	
	public void cancelAll(boolean toCancel) {
		for(ListenerDefault listener : this.listeners) {
			listener.cancel = toCancel;
		}
	}
	
}
