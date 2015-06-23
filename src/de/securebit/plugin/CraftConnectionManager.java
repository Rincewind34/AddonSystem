package de.securebit.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.securebit.api.Addon;
import de.securebit.api.AddonType;
import de.securebit.api.ConnectionManager;
import de.securebit.api.exceptions.ChannelClosedException;
import de.securebit.api.system.event.AddonConnection;
import de.securebit.api.system.event.Listener;
import de.securebit.api.system.event.AddonConnection.ReceiveListener;

public final class CraftConnectionManager implements ConnectionManager {
	
	private final List<AddonConnection> connections;
	
	public CraftConnectionManager(List<AddonConnection> connections) {
		this.connections = connections;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.ConnectionManager#getConnection(de.rincewind.api.Addon, java.lang.String)
	 */
	@Override
	public final AddonConnection getConnection(Addon receiver, String name) {
		for (AddonConnection connection : this.getConnections(receiver)) {
			if (connection.getName().equals(name)) {
				return connection;
			}
		}
		
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.ConnectionManager#getConnections(de.rincewind.api.Addon)
	 */
	@Override
	public final List<AddonConnection> getConnections(Addon receiver) {
		List<AddonConnection> cons = new ArrayList<AddonConnection>();
		
		for (AddonConnection connection : this.connections) {
			if (receiver.getName().equals(connection.getReceiver())) {
				cons.add(connection);
			}
		}
		
		return cons;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.ConnectionManager#getConnections()
	 */
	@Override
	public final List<AddonConnection> getConnections() {
		List<AddonConnection> cons = new ArrayList<AddonConnection>();
		
		for (AddonConnection connection : this.connections) {
			cons.add(connection);
		}
		
		return cons;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.ConnectionManager#addConnection(de.rincewind.api.system.event.AddonConnection)
	 */
	@Override
	public final void addConnection(AddonConnection connection) {
		this.connections.add(connection);
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.ConnectionManager#broadcast(java.lang.String, java.lang.String, de.rincewind.api.AddonType)
	 */
	@Override
	public final void broadcast(String subject, String payload, AddonType... receiveTypes) {
		if (!CraftCore.channelPipelineOpen) throw new ChannelClosedException("The cannelpipeline is closed!");
		
		if (receiveTypes == null || (receiveTypes != null && receiveTypes.length == 0)) {
			for (Addon addon : CraftCore.addons) {
				addon.onBroadcastReceive(subject, payload);
			}
		} else {
			List<AddonType> types = Arrays.asList(receiveTypes);
			for (Addon addon : CraftCore.addons) {
				if (types.contains(addon)) {
					addon.onBroadcastReceive(subject, payload);
				}
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.ConnectionManager#send(de.rincewind.api.Addon, java.lang.String, java.lang.String)
	 */
	@Override
	public final void send(Addon sender, String subject, String payload) {
		if (!CraftCore.channelPipelineOpen) throw new ChannelClosedException("The channelpipeline is closed!");
		
		for (AddonConnection connection : this.connections) {
			if (connection.getSender().equals(sender.getName())) {
				if(!connection.isOpen()) continue;
				for (ReceiveListener listener : connection.getListeners()) {
					listener.onReceive(new Listener(subject, payload));
				}
			}
		}
	}
}