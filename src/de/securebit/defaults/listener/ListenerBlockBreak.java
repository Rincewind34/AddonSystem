package de.securebit.defaults.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

public class ListenerBlockBreak extends ListenerDefault {
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(!super.ignore.contains(e.getPlayer())) e.setCancelled(super.cancel);
	}
	
}
