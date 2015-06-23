package de.securebit.api.countdown;

public interface CountdownManager {

	void register(String name, Countdown countdown);
	void start(Countdown countdown);
	void stop(Countdown countdown, boolean callEnd);
	
	boolean isRunning(Countdown countdown);
	
	Countdown get(String name);
	
}
