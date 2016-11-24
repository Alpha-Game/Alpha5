package match.tile.creator;

import java.util.Map;
import java.util.Set;

import match.tile.manager.DrawableTileManager;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.manager.Setting;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.tile.ITileManager;
import constants.TileConstants;

public class DrawableTileCreator extends AbstractTileCreator {
	public DrawableTileCreator() {
		super();
	}
	
	@Override
	public Set<Setting> getSettings() {
		Set<Setting> settings = super.getSettings();
		settings.add(new Setting(TileConstants.Settings_SpriteLocation, TileConstants.Language_SpriteLocation, String.class));
		settings.add(new Setting(TileConstants.Settings_DrawLocation, TileConstants.Language_DrawLocation, Rectangle2D.class));
		settings.add(new Setting(TileConstants.Settings_IsBackground, TileConstants.Language_IsBackground, Boolean.class));
		return settings;
	}
	
	@Override
	protected ITileManager createObject(IMatchManager match, Map<String, Object> settings) {
		return new DrawableTileManager(match, fSettings);
	}
}
