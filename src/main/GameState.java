package main;

import java.util.Locale;

import constants.GameConstants;
import resource.GameMessages;
import joe.game.window.state.GameWindowState;

public class GameState extends GameWindowState {
	private GameMessages fLanguage; // Language of the game
	
	public GameMessages getLanguage() {
		if (fLanguage == null) {
			fLanguage = new GameMessages(Locale.getDefault());
		}		
		return fLanguage;
	}
		
	public void setLanguage(Locale language) {
		fLanguage = new GameMessages(language);
	}
	
	protected int getDefaultWidth() {
		return GameConstants.DefaultWidth;
	}
	
	protected int getDefaultHeight() {
		return GameConstants.DefaultHeight;
	}
	
	protected double getDefaultCPS() {
		return GameConstants.DefaultCPS;
	}
	
	protected double getDefaultFPS() {
		return GameConstants.DefaultFPS;
	}
}
