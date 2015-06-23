package de.securebit.defaults.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class ListenerDefault implements Listener {
	
	public List<Player> ignore = new ArrayList<Player>();
	
	public boolean cancel = true;
	
}
