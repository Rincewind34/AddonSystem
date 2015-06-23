package de.securebit.api.gamestates;

public interface GameState {
	
	String getName();
	
	void addEnterListener(GameStateEnterListener listener);
	
	void addLeaveListener(GameStateLeaveListener listener);
	
	void execEnterListener();
	
	void execLeaveListener();
	
	public interface GameStateEnterListener {
		
		void onEnter();
		
	}
	
	public interface GameStateLeaveListener {
		
		void onLeave();
		
	}
	
}
