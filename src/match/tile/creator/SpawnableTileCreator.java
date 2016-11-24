package match.tile.creator;

import java.util.Map;
import java.util.Set;

import match.tile.manager.SpawnableTileManager;
import constants.TileConstants;
import joe.game.manager.Setting;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.tile.ITileManager;

public class SpawnableTileCreator extends AbstractTileCreator {
	public SpawnableTileCreator() {
		super();
	}
	
	@Override
	public Set<Setting> getSettings() {
		Set<Setting> settings = super.getSettings();
		settings.add(new Setting(TileConstants.Settings_SpawnLocation, TileConstants.Language_SpawnLocation, Boolean.class));
		return settings;
	}
	
	
	@Override
	protected ITileManager createObject(IMatchManager match, Map<String, Object> settings) {
		return new SpawnableTileManager(match, settings);
	}

}
