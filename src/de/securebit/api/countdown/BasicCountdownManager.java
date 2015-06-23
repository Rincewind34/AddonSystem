package de.securebit.api.countdown;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import de.securebit.api.exceptions.CountdownAlreadyRunningException;
import de.securebit.api.exceptions.CountdownNotRegisteredException;
import de.securebit.plugin.CraftCore;

public class BasicCountdownManager implements CountdownManager {

	private final Map<String, Countdown> countdowns;
	private final Map<Countdown, BukkitTask> running;
	private final CraftCore instance;
	
	public BasicCountdownManager(CraftCore instance) {
		this.instance = instance;
		this.countdowns = new HashMap<String, Countdown>();
		this.running = new HashMap<Countdown, BukkitTask>();
	}
	
	@Override
	public void register(String name, Countdown countdown) {
		this.countdowns.put(name, countdown);
	}

	@Override
	public void start(final Countdown countdown) {
		if (!this.countdowns.containsValue(countdown)) {
			throw new CountdownNotRegisteredException("Countdowns must be registered before starting!");
		}

		if (this.running.containsKey(countdown)) {
			throw new CountdownAlreadyRunningException("This countdown is already running!");
		}
		
		this.running.put(countdown, Bukkit.getScheduler().runTaskTimer(this.instance, new Runnable() {
			
			@Override
			public void run() {
				if (countdown.getRemainingSeconds() <= 0) {
					BasicCountdownManager.this.running.get(countdown).cancel();
					BasicCountdownManager.this.running.remove(countdown);
					countdown.end();
				} else {
					countdown.setRemainingSeconds(countdown.getRemainingSeconds() - 1);
					countdown.run();
				}
			}
		}, 0L, 20L));
	}

	@Override
	public void stop(Countdown countdown, boolean callEnd) {
		if (!this.countdowns.containsValue(countdown)) {
			throw new CountdownNotRegisteredException("Countdowns must be registered before stopping!");
		}
		
		if (this.running.containsKey(countdown)) {
			this.running.get(countdown).cancel();
			this.running.remove(countdown);
		}
	}

	@Override
	public boolean isRunning(Countdown countdown) {
		return this.running.containsKey(countdown);
	}

	@Override
	public Countdown get(String name) {
		return this.countdowns.get(name);
	}

}
