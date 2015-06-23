package de.securebit.api.system.event;

import java.util.ArrayList;
import java.util.List;

import de.securebit.api.exceptions.ConnectionClosedException;

public final class AddonConnection {
	
	private String name;
	private String sender;
	private String receiver;
	
	private List<ReceiveListener> listeners;
	
	private boolean isOpen;
	
	public AddonConnection(String name, String sender, String receiver, boolean isOpen) {
		this.name = name;
		this.sender = sender;
		this.receiver = receiver;
		this.listeners = new ArrayList<ReceiveListener>();
		this.isOpen = isOpen;
	}
	
	public String getName() {
		return this.name;
	}

	public String getSender() {
		return this.sender;
	}
	
	public String getReceiver() {
		return this.receiver;
	}
	
	public void addListener(ReceiveListener l) {
		if(!this.isOpen) throw new ConnectionClosedException("The connection '" + this.name + "' from '" + this.receiver + "' is closed by the setup!");
		this.listeners.add(l);
	}
	
	public List<ReceiveListener> getListeners() {
		return this.listeners;
	}
	
	public boolean isOpen() {
		return this.isOpen;
	}

	public interface ReceiveListener {
		
		void onReceive(Listener listener);
		
	}
	
}
