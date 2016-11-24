package constants;

import java.awt.Color;
import java.awt.Font;

import joe.game.layout.implementation.Position;

public class MainMenuConstants {
	public static final Color TitleColor;
	public static final Font TitleFont;
	public static final Font MenuFont;
	
	public static final Integer TitleTopMargin;
	public static final Integer MenuTopMargin;
	public static final Integer MenuItemTopMargin;
	
	public static final Position TitlePosition;
	public static final Position MenuPosition;
	public static final Position MenuItemPosition;
	
	
	public static final Color SelectedColor;
	public static final Color UnselectedColor;
	
	public static final String[] MenuItems;
	
	static {
		TitleColor = new Color(128, 0, 0);
		TitleFont = new Font("Century Gothic", Font.PLAIN, 28);
		MenuFont = new Font("Arial", Font.PLAIN, 12);
		
		TitleTopMargin = 70;
		MenuTopMargin = 140;
		MenuItemTopMargin = 5;
		
		TitlePosition = Position.Top_Center;
		MenuPosition = Position.Top_Center;
		MenuItemPosition = Position.Top_Center;
		
		SelectedColor = Color.BLACK;
		UnselectedColor = Color.RED;
		
		MenuItems = new String[] { LanguageConstants.MainMenu_Start, LanguageConstants.MainMenu_Help, LanguageConstants.MainMenu_Exit };
	}
}
