package de.securebit.defaults.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

public class ListenerBlockPlace extends ListenerDefault {
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if(!super.ignore.contains(e.getPlayer())) e.setCancelled(super.cancel);
	}
	
}
