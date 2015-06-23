package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class CountdownAlreadyRunningException extends NullPointerException {

	public CountdownAlreadyRunningException(String cause) {
		super(cause);
	}
}
