package match.tile.creator;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import constants.TileConstants;
import joe.game.manager.Setting;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.match.IMatchObjectCreator;
import joe.game.platformer.tile.ITileManager;

public abstract class AbstractTileCreator implements IMatchObjectCreator {
	protected Map<String, Object> fSettings;
	
	protected AbstractTileCreator() {
		fSettings = new HashMap<String, Object>();
	}
	
	@Override
	public Set<Setting> getSettings() {
		Set<Setting> settings = new LinkedHashSet<Setting>();
		settings.add(new Setting(TileConstants.Settings_Identifier, TileConstants.Language_Identifier, String.class));
		return settings;
	}
	
	@Override
	public void addSetting(String identifier, Object setting) {
		fSettings.put(identifier, setting);
	}
	
	protected abstract ITileManager createObject(IMatchManager match, Map<String, Object> settings);
	
	@Override
	public ITileManager createObject(IMatchManager match) {
		ITileManager object = createObject(match, fSettings);
		fSettings.clear();
		
		return object;
	}
}
