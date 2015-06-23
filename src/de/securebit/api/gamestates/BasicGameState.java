package de.securebit.api.gamestates;

import java.util.ArrayList;
import java.util.List;

public class BasicGameState implements GameState {

	private List<GameStateEnterListener> enterListeners;
	private List<GameStateLeaveListener> leaveListeners;
	
	private String name;
	
	public BasicGameState(String name) {
		this.enterListeners = new ArrayList<GameState.GameStateEnterListener>();
		this.leaveListeners = new ArrayList<GameState.GameStateLeaveListener>();
		
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void addEnterListener(GameStateEnterListener listener) {
		this.enterListeners.add(listener);
	}

	@Override
	public void addLeaveListener(GameStateLeaveListener listener) {
		this.leaveListeners.add(listener);
	}
	
	@Override
	public void execLeaveListener() {
		for (GameStateLeaveListener listener : this.leaveListeners) {
			listener.onLeave();
		}
	}
	
	@Override
	public void execEnterListener() {
		for (GameStateEnterListener listener : this.enterListeners) {
			listener.onEnter();
		}
	}

}
