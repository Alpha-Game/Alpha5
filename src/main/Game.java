package main;

import constants.GameConstants;
import menu.main.MainMenuManager;
import joe.classes.language.IMessageChangeListener;
import joe.classes.language.Message;
import joe.game.window.GameWindow;
import joe.game.window.state.GameWindowState;
import joe.input.IODeviceList;
import joe.input.jinput.JInputDeviceReader;

public class Game extends GameWindow implements IMessageChangeListener {
	private static final long serialVersionUID = -4181237104392585255L;
	
	private JInputDeviceReader fjInputReader;
	
	public Game() {
		super(GameConstants.StartWindowWidth, GameConstants.StartWindowHeight, GameConstants.StartMaximized, GameConstants.StartMinimized, GameConstants.BackgroundColor);
		
		setTitle(((GameState)fState).getLanguage().Title.getMessage());
		((GameState)fState).getLanguage().Title.addListener(this);
		fManager.setManager(new MainMenuManager((GameManager)fManager));
	}
	
	protected GameWindowState createState() {
		return new GameState();
	}
	
	protected IODeviceList createDeviceList() {
		IODeviceList deviceList = new IODeviceList();
		fjInputReader = new JInputDeviceReader(deviceList);
		try {
			fjInputReader.start();
		} catch (RuntimeException e) {
			throw e;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		return deviceList;
	}
	
	protected GameManager createManager() {
		return new GameManager(this);
	}
	
	public GameState getState() {
		return (GameState)fState;
	}

	@Override
	public void onChange(Message object, String oldString) {
		if (object == ((GameState)fState).getLanguage().Title) {
			setTitle(object.getMessage());
		}
	}
}
