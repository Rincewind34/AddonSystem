package de.securebit.plugin;

import de.securebit.api.BedWars;
import de.securebit.api.Core;
import de.securebit.api.countdown.BasicCountdownManager;
import de.securebit.api.countdown.CountdownManager;
import de.securebit.api.exceptions.CountdownManagerUnloadedException;
import de.securebit.api.exceptions.GameStateManagerUnloadedException;
import de.securebit.api.gamestates.BasicGameStateManager;
import de.securebit.api.gamestates.GameStateManager;

public final class CraftBedWars implements BedWars {

	private CraftCore core;
	
	private GameStateManager gamestateManager;
	private CountdownManager countdownManager;
	
	private boolean gamestateManagerCreated;
	private boolean countdownManagerCreated;
	
	public CraftBedWars(CraftCore core) {
		this.core = core;
		this.gamestateManagerCreated = false;
		this.countdownManagerCreated = false;
	}
	
	protected void createGamestates() {
		this.setGamestateManager(new BasicGameStateManager());
	}
		
	protected void createCountdowns() {
		this.setCountdownManager(new BasicCountdownManager(this.core));
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.BedWars#setGamestateManager(de.rincewind.api.gamestates.GameStateManager)
	 */
	@Override
	public void setGamestateManager(GameStateManager gamestateManager) {
		if (this.gamestateManager != null) return;
		this.gamestateManagerCreated = true;
		this.gamestateManager = gamestateManager;
		Core.getShell().log("Core", "BedWars", "A gamestatemanager was set!");
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.BedWars#getGamestateManager()
	 */
	@Override
	public GameStateManager getGamestateManager() {
		if (!this.gamestateManagerCreated) throw new GameStateManagerUnloadedException("The gamestate-manager is not loaded yet!");
		return this.gamestateManager;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.BedWars#getCountdownManager()
	 */
	@Override
	public CountdownManager getCountdownManager() {
		if (!this.countdownManagerCreated) throw new CountdownManagerUnloadedException("The countdown-manager is not initialized yet!");
		return this.countdownManager;
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.BedWars#setCountdownManager(de.rincewind.api.countdown.CountdownManager)
	 */
	@Override
	public void setCountdownManager(CountdownManager countdownManager) {
		if (this.countdownManager != null) return;
		this.countdownManagerCreated = true;
		this.countdownManager = countdownManager;
		Core.getShell().log("Core", "BedWars", "A countdownmanager was set!");
	}
	
	/* (non-Javadoc)
	 * @see de.rincewind.api.BedWars#setRunning(boolean)
	 */
	@Override
	public void setRunning(boolean runing) {
		Core.getCfg().instance().set("game.running", runing);
		Core.getCfg().save();
	}

	@Override
	public boolean isRunning() {
		return (boolean) Core.getCfg().instance().get("game.running");
	}
	
}