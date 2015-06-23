package de.securebit.api.countdown;

public interface Countdown {

	int getRemainingSeconds();
	void setRemainingSeconds(int seconds);
	
	void run();
	void end();
}
