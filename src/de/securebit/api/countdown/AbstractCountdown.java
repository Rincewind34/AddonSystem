package de.securebit.api.countdown;

public abstract class AbstractCountdown implements Countdown {

	protected int seconds;
	
	public AbstractCountdown(int seconds) {
		this.seconds = seconds;
	}
	
	@Override
	public int getRemainingSeconds() {
		return this.seconds;
	}

	@Override
	public void setRemainingSeconds(int seconds) {
		this.seconds = seconds;
	}
}
