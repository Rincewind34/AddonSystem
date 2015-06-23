package de.securebit.api;

import java.util.List;

import de.securebit.api.system.event.AddonConnection;

public interface ConnectionManager {

	public abstract AddonConnection getConnection(Addon receiver, String name);

	public abstract List<AddonConnection> getConnections(Addon receiver);

	public abstract List<AddonConnection> getConnections();

	public abstract void addConnection(AddonConnection connection);

	public abstract void broadcast(String subject, String payload, AddonType... receiveTypes);

	public abstract void send(Addon sender, String subject, String payload);

}