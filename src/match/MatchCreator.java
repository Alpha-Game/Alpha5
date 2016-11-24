package match;

import main.GameManager;
import joe.classes.geometry2D.Vector2D;
import joe.game.base.implementation.settings.AbstractCreator;
import joe.game.base.implementation.settings.SettingType;
import joe.game.twodimension.platformer.match.IMatchCreator;
import constants.MatchConstants;

public class MatchCreator extends AbstractCreator<GameManager, MatchManager> implements IMatchCreator<GameManager, MatchManager> {
	public MatchCreator() {
		super();
		fSettingTypes.put(MatchConstants.Settings_Identifier, new SettingType<String>(MatchConstants.Settings_Identifier, MatchConstants.Language_Identifier, String.class));
		fSettingTypes.put(MatchConstants.Settings_BaseGravity, new SettingType<Vector2D>(MatchConstants.Settings_BaseGravity, MatchConstants.Language_BaseGravity, Vector2D.class));
		fSettingTypes.put(MatchConstants.Settings_BaseSpeed, new SettingType<Double>(MatchConstants.Settings_BaseSpeed, MatchConstants.Language_BaseSpeed, Double.class));
	}

	@Override
	public MatchManager create(GameManager callingObject) {
		return new MatchManager(callingObject, getSettings());
	}
}
