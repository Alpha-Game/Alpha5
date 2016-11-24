package match.player;

import java.util.HashMap;
import java.util.Map;

import joe.game.layout.image.sprite.ISprite;
import joe.game.platformer.player.IPlayerManager;

public class PlayerSpriteController {
	private Map<String, ISprite> fSprites;
	
	public PlayerSpriteController(IPlayerManager playerManager) {
		fSprites = new HashMap<String, ISprite>();
	}
	
	public void setSprite(String type, ISprite sprite) {
		fSprites.put(type, sprite);
	}
	
	public ISprite getSprite(String type) {
		return fSprites.get(type);
	}
}
