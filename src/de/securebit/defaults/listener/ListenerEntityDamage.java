package de.securebit.defaults.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class ListenerEntityDamage extends ListenerDefault {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(!super.ignore.contains(e.getEntity())) e.setCancelled(super.cancel);
	}
	
}
