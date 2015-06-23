package de.securebit.api.countdown;

import org.bukkit.Bukkit;

public abstract class MessageCountdown extends AbstractCountdown {

	protected String message;
	protected String replaceString;
	
	public MessageCountdown(String message, String replaceWithSeconds, int seconds) {
		super(seconds);
		this.message = message;
		this.replaceString = replaceWithSeconds;
	}

	@Override
	public void run() {
		if (this.isAnnounceTime()) {
			Bukkit.broadcastMessage(this.message.replace(this.replaceString, Integer.toString(this.seconds)));
		}
	}

	protected boolean isAnnounceTime() {
		return this.seconds % 120 == 0
				|| (this.seconds <= 90 && this.seconds % 15 == 0)
				|| (this.seconds == 10 || this.seconds <= 5);
	}
}
