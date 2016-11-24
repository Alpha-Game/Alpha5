package match.tile.creator;

import java.util.Map;
import java.util.Set;

import joe.classes.geometry2D.Rectangle2D;
import joe.game.manager.Setting;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.tile.ITileManager;
import match.tile.manager.CollisionDrawableTileManager;
import constants.TileConstants;

public class CollisionDrawableTileCreator extends DrawableTileCreator {
	public CollisionDrawableTileCreator() {
		super();
	}
	
	
	@Override
	public Set<Setting> getSettings() {
		Set<Setting> settings = super.getSettings();
		settings.add(new Setting(TileConstants.Settings_Identifier, TileConstants.Language_Identifier, String.class));
		settings.add(new Setting(TileConstants.Settings_SpriteLocation, TileConstants.Language_SpriteLocation, String.class));
		settings.add(new Setting(TileConstants.Settings_DrawLocation, TileConstants.Language_DrawLocation, Rectangle2D.class));
		settings.add(new Setting(TileConstants.Settings_IsBackground, TileConstants.Language_IsBackground, Boolean.class));
		return settings;
	}
	
	@Override
	protected ITileManager createObject(IMatchManager match, Map<String, Object> settings) {
		return new CollisionDrawableTileManager(match, fSettings);
	}
}
