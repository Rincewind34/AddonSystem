package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class GameStateManagerUnloadedException extends NullPointerException {

	public GameStateManagerUnloadedException(String cause) {
		super(cause);
	}
}
