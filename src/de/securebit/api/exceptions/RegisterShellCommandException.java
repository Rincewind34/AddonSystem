package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class RegisterShellCommandException extends NullPointerException {
	
	public RegisterShellCommandException() {
		super("Unable to register shellcommand! The key already exists!");
	}
	
}
