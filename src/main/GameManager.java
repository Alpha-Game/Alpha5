package main;

import resource.GameMessages;

public class GameManager extends joe.game.manager.GameManager {
	public GameManager(Game game) {
		super(game);
	}
	
	public GameState getGameState() {
		return ((GameState)super.getGameState());
	}
	
	public GameMessages getLanguage() {
		return getGameState().getLanguage();
	}
}
