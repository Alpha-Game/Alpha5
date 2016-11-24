package menu.main;

import java.awt.Graphics2D;
import java.util.HashMap;

import constants.MainMenuConstants;
import main.GameManager;
import main.Manager;
import match.MatchManager;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.layout.IDrawable;
import joe.game.layout.image.sprite.ISprite;
import joe.game.layout.implementation.LayoutCalculator;
import joe.game.layout.implementation.menu.AbstractMenuItem;
import joe.game.layout.implementation.menu.Menu;
import joe.game.layout.implementation.message.MessageDrawer;
import joe.game.manager.IManager;
import joe.input.ControllerAction;

public class MainMenuManager extends Manager {	
	private ISprite Background;
	private MessageDrawer Title;
	private Menu Menu;
	
	private MainMenuState State;
	private ControllerAction UpAction;
	private ControllerAction DownAction;
	private ControllerAction EnterAction;
	
	public MainMenuManager(GameManager manager) {
		super(manager);
		
		State = new MainMenuState();
		
		try {
			Background = getManager().getSprite("Resources/Sprites:sprites?titleBackground");
			Title = new MessageDrawer(getManager().getLanguage().Title, MainMenuConstants.TitleFont, MainMenuConstants.TitleColor);
			Menu = new Menu(getMenuChoices(), MainMenuConstants.MenuItemPosition);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		UpAction = getManager().getController(null, "Key: Up");
		DownAction = getManager().getController(null, "Key: Down");
		EnterAction = getManager().getController(null, "Key: Return");
	}
	
	protected void reset() {
		UpAction.reset();
		DownAction.reset();
		EnterAction.reset();
	}
	
	public void setPreviousManager(IManager previousManager) {
		super.setPreviousManager(previousManager);
		reset();
	}
	
	public void update() {
		int selection = State.getCurrentSelection();
		int upDepressions = (int) ((UpAction.getUseCount() / 2) + (UpAction.getUseCount() > 0 ? UpAction.getLastValue() : 0));
		int downDepressions = (int) ((DownAction.getUseCount() / 2) + (DownAction.getUseCount() > 0 ? DownAction.getLastValue() : 0));
		selection= (int) ((selection + downDepressions - upDepressions) % getMenuChoices().length);
		if (selection < 0) {
			selection += getMenuChoices().length;
		}
		State.setCurrentSelection(selection);
		
		if (EnterAction.getUseCount() > 0) {
			select(State.getCurrentSelection());
		}
		reset();
	}
	
	public void draw(Graphics2D g) {
		Rectangle2D gameRectangle = LayoutCalculator.createRectangle(0, 0, getManager().getGameState().getGameWidth(), getManager().getGameState().getGameHeight());
		
		Menu.clearSelected();
		Menu.addSelected(State.getCurrentSelection());
		
		Background.draw(g, gameRectangle); // draw background
		Title.draw(g, LayoutCalculator.getInnerRectangle(LayoutCalculator.getMarginRectangle(gameRectangle, 0, 0, MainMenuConstants.TitleTopMargin, gameRectangle.getMaxY() - MainMenuConstants.MenuTopMargin), Title.getDimension(), MainMenuConstants.TitlePosition, 0, 0));
		Menu.draw(g, LayoutCalculator.getInnerRectangle(LayoutCalculator.getMarginRectangle(gameRectangle, 0, 0, MainMenuConstants.MenuTopMargin, 0), Menu.getDimension(), MainMenuConstants.MenuPosition, 0, 0));
	}
	
	private AbstractMenuItem[] menuItems;
	
	private AbstractMenuItem[] getMenuChoices() {
		if (menuItems == null) {
			menuItems = new AbstractMenuItem[MainMenuConstants.MenuItems.length];
			for (int i = 0; i < menuItems.length; i++) {
				menuItems[i] = new AbstractMenuItem(new MessageDrawer(getManager().getLanguage().getMessage(MainMenuConstants.MenuItems[i]), MainMenuConstants.MenuFont, MainMenuConstants.UnselectedColor), 0, 0, 0, MainMenuConstants.MenuItemTopMargin) {
					@Override
					public void setSelectedState(IDrawable item, boolean isSelected) {
						if (isSelected) {
							((MessageDrawer)item).setColor(MainMenuConstants.SelectedColor);
						} else {
							((MessageDrawer)item).setColor(MainMenuConstants.UnselectedColor);
						}
					}
				};
			}
		}
		return menuItems;
	}
	
	private void select(int choice) {
		switch (choice) {
			case 0: // Start
				getManager().setManager(new MatchManager(getManager(), new HashMap<String, Object>()));
				break;
			case 1: // Help
				break;
			case 2: // Exit
				System.exit(0);
				break;
		}
	}
}










