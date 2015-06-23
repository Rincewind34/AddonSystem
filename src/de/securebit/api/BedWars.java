package de.securebit.api;

import de.securebit.api.countdown.CountdownManager;
import de.securebit.api.gamestates.GameStateManager;

public interface BedWars {

	public abstract GameStateManager getGamestateManager();

	public abstract CountdownManager getCountdownManager();
	
	public abstract void setGamestateManager(GameStateManager gamestateManager);

	public abstract void setCountdownManager(CountdownManager countdownManager);

	public abstract void setRunning(boolean runing);
	
	public abstract boolean isRunning();

}