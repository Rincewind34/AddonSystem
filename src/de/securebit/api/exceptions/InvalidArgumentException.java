package de.securebit.api.exceptions;

@SuppressWarnings("serial")
public class InvalidArgumentException extends Exception {
	
	public InvalidArgumentException() {
		super("The methode 'processMessage' only performes a simlar length of arguments.");
	}
	
}
