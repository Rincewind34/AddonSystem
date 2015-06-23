package de.securebit.api.gamestates;

public interface GameStateManager {
	
	/**
	 * Switches to the next gamestate
	 * @return The new gamestate or NULL, if the end of all gamestates is reached
	 */
	GameState next();
	
	GameState getCurrent();
	
	GameState getState(String s);
	
}
