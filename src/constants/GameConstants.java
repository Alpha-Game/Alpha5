package constants;

import java.awt.Color;
import java.util.Locale;

public class GameConstants {
	public static final int StartWindowWidth;
	public static final int StartWindowHeight;
	public static final int DefaultWidth;
	public static final int DefaultHeight;
	public static final double DefaultFPS;
	public static final double DefaultCPS;
	public static final boolean StartMaximized;
	public static final boolean StartMinimized;
	public static final Locale StartLanguage;
	public static final Color BackgroundColor;
	
	static {
		StartWindowWidth = 1920;
		StartWindowHeight = 1080;
		DefaultWidth = 320;
		DefaultHeight = 240;
		DefaultFPS = 60;
		DefaultCPS = 240;
		
		StartMaximized = false;
		StartMinimized = false;
		StartLanguage = new Locale("en-US");
		BackgroundColor = Color.BLACK;
	}
}
