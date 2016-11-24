package resource;

import java.util.Locale;

import constants.LanguageConstants;
import joe.classes.language.AbstractMessages;
import joe.classes.language.Message;

public class GameMessages extends AbstractMessages {	
	public Message Title;
	
	public Message MainMenu_Start;
	public Message MainMenu_Help;
	public Message MainMenu_Exit;
	
	public GameMessages(Locale locale) {
		super(locale);
	}
	
	@Override
	public void setLocale(Locale locale) {
		super.setLocale(locale);
		super.setBundle(locale, "Resources/Language", "messages");
		
		Title = setMessage(Title, LanguageConstants.title, "");
		MainMenu_Start = setMessage(MainMenu_Start, LanguageConstants.MainMenu_Start, "");
		MainMenu_Help = setMessage(MainMenu_Help, LanguageConstants.MainMenu_Help, "");
		MainMenu_Exit = setMessage(MainMenu_Exit, LanguageConstants.MainMenu_Exit, "");
	}
}
