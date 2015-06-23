package de.securebit.api.gamestates;

import java.util.ArrayList;
import java.util.List;

public class BasicGameStateManager implements GameStateManager {

	private int pointer;
	private List<GameState> states;

	public BasicGameStateManager() {
		this.states = new ArrayList<GameState>();
		this.pointer = -1;
		this.initStates();
	}

	@Override
	public GameState next() {
		if (this.pointer > -1) {
			this.states.get(this.pointer).execLeaveListener();
		}

		this.pointer++;

		if (this.pointer < this.states.size()) {
			this.states.get(this.pointer).execEnterListener();
			return this.states.get(this.pointer);
		}
		return null;
	}

	@Override
	public GameState getCurrent() {
		return (this.pointer > -1 && this.pointer < this.states.size()) ? this.states.get(this.pointer) : null;
	}

	@Override
	public GameState getState(String s) {
		for (GameState state : this.states) if(state.getName().equals(s)) return state;
		return null;
	}
	
	public List<GameState> getGameStates() {
		return this.states;
	}
	
	protected void initStates() {
		this.states.add(new BasicGameState(GameStates.GS_LOBBY));
		this.states.add(new BasicGameState(GameStates.GS_WARMUP));
		this.states.add(new BasicGameState(GameStates.GS_GRACE));
		this.states.add(new BasicGameState(GameStates.GS_INGAME));
		this.states.add(new BasicGameState(GameStates.GS_END));
		this.states.add(new BasicGameState(GameStates.GS_RESTART));
	}

	public static final class GameStates {

		public static final String GS_LOBBY = "gs_lobby";
		public static final String GS_WARMUP = "gs_warmup";
		public static final String GS_GRACE = "gs_grace";
		public static final String GS_INGAME = "gs_ingame";
		public static final String GS_END = "gs_end";
		public static final String GS_RESTART = "gs_restart";

	}

}
