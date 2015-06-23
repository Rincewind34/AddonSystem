package de.securebit.defaults.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class ListenerFoodLevelChange extends ListenerDefault {
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		if(!super.ignore.contains(e.getEntity())) e.setCancelled(super.cancel);
	}
	
}
