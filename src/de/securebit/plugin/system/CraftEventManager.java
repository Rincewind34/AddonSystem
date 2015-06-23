package de.securebit.plugin.system;

import java.util.Map;

import de.securebit.api.Addon;
import de.securebit.api.system.event.Event;
import de.securebit.api.system.event.EventManager;
import de.securebit.plugin.CraftCore;

public class CraftEventManager implements EventManager {
	
	private Addon sender;
	
	private Map<String, Event> events;
	
	public CraftEventManager(Addon sender) {
		this.sender = sender;
	}
	
	@Override
	public void fireEvent(Event event) {
		CraftCore.getConnectionManager().send(this.sender, event.getName(), event.getPayload());
	}

	@Override
	public void saveEvent(Event event) {
		this.events.put(event.getName(), event);
	}

	@Override
	public Event loadEvent(String name) {
		return this.events.get(name);
	}

}
