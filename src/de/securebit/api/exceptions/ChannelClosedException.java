package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class ChannelClosedException extends NullPointerException {
	
	public ChannelClosedException(String cause) {
		super(cause);
	}
	
}
