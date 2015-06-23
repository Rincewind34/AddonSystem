package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class CountdownNotRegisteredException extends NullPointerException {

	public CountdownNotRegisteredException(String cause) {
		super(cause);
	}
}
