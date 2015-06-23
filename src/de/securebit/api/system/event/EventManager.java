package de.securebit.api.system.event;

public interface EventManager {
	
	void fireEvent(Event event);
	
	void saveEvent(Event event);
	
	Event loadEvent(String name);
	
}
