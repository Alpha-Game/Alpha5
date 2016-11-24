package match.tile.manager;

import java.awt.Graphics2D;
import java.util.Map;

import constants.TileConstants;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.tile.ISpawnableTile;

public class SpawnableTileManager extends AbstractTileManager implements ISpawnableTile {
	private Rectangle2D fSpawnLocation;

	public SpawnableTileManager(IMatchManager matchManager,  Map<String, Object> settings) {
		super(matchManager, settings);
		fSpawnLocation = Values.getValue(settings.get(TileConstants.Settings_SpawnLocation), null, Rectangle2D.class);
	}
	
	@Override
	public void update(long time) {
		// Do Nothing
	}

	@Override
	public void draw(Graphics2D g, long time) {
		// Do Nothing
	}

	@Override
	public Rectangle2D getSpawnLocation() {
		return fSpawnLocation;
	}
}
