package match.layer;

import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import joe.classes.geometry2D.Vector2D;
import joe.game.manager.Setting;
import joe.game.platformer.layer.ILayerManager;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.match.IMatchObjectCreator;
import constants.LayerConstants;

public class LayerCreator implements IMatchObjectCreator {
	protected Map<String, Object> fSettings;
	
	public LayerCreator() {
		fSettings = new HashMap<String, Object>();
	}
	
	@Override
	public Set<Setting> getSettings() {		
		Set<Setting> settings = new LinkedHashSet<Setting>();
		settings.add(new Setting(LayerConstants.Settings_Identifier, LayerConstants.Language_Identifier, String.class));
		settings.add(new Setting(LayerConstants.Settings_LayerSize, LayerConstants.Language_LayerSize, Dimension2D.class));
		settings.add(new Setting(LayerConstants.Settings_BackgroundSprite, LayerConstants.Language_BackgroundSprite, String.class));
		settings.add(new Setting(LayerConstants.Settings_BaseGravityMultiplier, LayerConstants.Language_BaseGravityMultiplier, Vector2D.class));
		settings.add(new Setting(LayerConstants.Settings_BaseSpeedMultiplier, LayerConstants.Language_BaseSpeedMultiplier, Double.class));
		
		return settings;
	}
	
	@Override
	public void addSetting(String identifier, Object setting) {
		fSettings.put(identifier, setting);
	}
	
	protected ILayerManager createObject(IMatchManager match, Map<String, Object> settings) {
		return new LayerManager(match, settings);
	}
	
	@Override
	public ILayerManager createObject(IMatchManager match) {
		ILayerManager object = createObject(match, fSettings);
		fSettings.clear();
		
		match.addObject(object);
		return object;
	}
}
