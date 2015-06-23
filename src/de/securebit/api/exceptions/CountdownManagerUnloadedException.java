package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class CountdownManagerUnloadedException extends NullPointerException {

	public CountdownManagerUnloadedException(String cause) {
		super(cause);
	}
}
