package match.player;

import java.awt.geom.Dimension2D;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import joe.classes.geometry2D.Vector2D;
import joe.game.manager.Setting;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.match.IMatchObjectCreator;
import joe.game.platformer.player.Controls;
import joe.game.platformer.player.IPlayerManager;
import constants.PlayerConstants;

public class PlayerCreator implements IMatchObjectCreator {
	protected Map<String, Object> fSettings;
	
	public PlayerCreator() {
		fSettings = new HashMap<String, Object>();
	}
	
	@Override
	public Set<Setting> getSettings() {
		Set<Setting> settings = new LinkedHashSet<Setting>();
		settings.add(new Setting(PlayerConstants.Settings_Identifier, PlayerConstants.Language_Identifier, String.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseDimensions, PlayerConstants.Language_BaseDimensions, Dimension2D.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseGravityMultiplier, PlayerConstants.Language_BaseGravityMultiplier, Vector2D.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseMass, PlayerConstants.Language_BaseMass, Double.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseSpeedMultiplier, PlayerConstants.Language_BaseSpeedMultiplier, Double.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseMaxVelocity, PlayerConstants.Language_BaseMaxVelocity, Vector2D.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseAcceleration, PlayerConstants.Language_BaseAcceleration, Vector2D.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseDecceleration, PlayerConstants.Language_BaseDecceleration, Vector2D.class));
		settings.add(new Setting(PlayerConstants.Settings_BaseMaxHealth, PlayerConstants.Language_BaseMaxHealth, Double.class));
		
		// Controls
		settings.add(new Setting(PlayerConstants.Settings_Controls_MoveUp, PlayerConstants.Language_Controls_MoveUp, Controls.class));
		settings.add(new Setting(PlayerConstants.Settings_Controls_MoveDown, PlayerConstants.Language_Controls_MoveDown, Controls.class));
		settings.add(new Setting(PlayerConstants.Settings_Controls_MoveLeft, PlayerConstants.Language_Controls_MoveLeft, Controls.class));
		settings.add(new Setting(PlayerConstants.Settings_Controls_MoveRight, PlayerConstants.Language_Controls_MoveRight, Controls.class));
		settings.add(new Setting(PlayerConstants.Settings_Controls_Attack1, PlayerConstants.Language_Controls_Attack1, Controls.class));
		settings.add(new Setting(PlayerConstants.Settings_Controls_Attack2, PlayerConstants.Language_Controls_Attack2, Controls.class));
		settings.add(new Setting(PlayerConstants.Settings_Controls_Jump, PlayerConstants.Language_Controls_Jump, Controls.class));
		
		// Sprites
		settings.add(new Setting(PlayerConstants.Settings_Sprites_Stand, PlayerConstants.Language_Sprites_Stand, String.class));
		settings.add(new Setting(PlayerConstants.Settings_Sprites_Walk, PlayerConstants.Language_Sprites_Walk, String.class));
		settings.add(new Setting(PlayerConstants.Settings_Sprites_Run, PlayerConstants.Language_Sprites_Run, String.class));
		settings.add(new Setting(PlayerConstants.Settings_Sprites_Climb, PlayerConstants.Language_Sprites_Climb, String.class));
		settings.add(new Setting(PlayerConstants.Settings_Sprites_Fall, PlayerConstants.Language_Sprites_Fall, String.class));
		settings.add(new Setting(PlayerConstants.Settings_Sprites_Jump, PlayerConstants.Language_Sprites_Jump, String.class));
		return settings;
	}
	
	@Override
	public void addSetting(String identifier, Object setting) {
		fSettings.put(identifier, setting);
	}
	
	protected IPlayerManager createObject(IMatchManager match, Map<String, Object> settings) {
		return new PlayerManager(match, settings);
	}
	
	@Override
	public IPlayerManager createObject(IMatchManager match) {
		IPlayerManager object = createObject(match, fSettings);
		fSettings.clear();
		
		match.addObject(object);
		return object;
	}
}
