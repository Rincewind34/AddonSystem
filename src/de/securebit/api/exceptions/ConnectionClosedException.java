package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class ConnectionClosedException extends NullPointerException {

	public ConnectionClosedException(String cause) {
		super(cause);
	}
}
