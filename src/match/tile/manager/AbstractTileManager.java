package match.tile.manager;

import java.awt.Graphics2D;
import java.util.Map;

import match.AbstractMatchObjectManager;
import match.AbstractMatchObjectState;
import constants.TileConstants;
import joe.classes.bundle.Values;
import joe.game.twodimension.platformer.layer.ILayerManager;
import joe.game.twodimension.platformer.match.IMatchManager;
import joe.game.twodimension.platformer.tiles.ITileManager;

public abstract class AbstractTileManager extends AbstractMatchObjectManager implements ITileManager {
	private ILayerManager fLayer;

	public AbstractTileManager(IMatchManager matchManager, Map<String, Object> settings) {
		super(matchManager, Values.getStringValue(settings.get(TileConstants.Settings_Identifier), null));
		fLayer= null;
	}
	
	@Override
	protected AbstractMatchObjectState getState() {
		return null;
	}
	
	@Override
	public void setLayer(ILayerManager layer) {
		ILayerManager lastLayer = getLayer();
		if (lastLayer != layer) {
			fLayer = layer;
			if (lastLayer != null) {
				lastLayer.removeObjects(this);
			}
			if (layer != null && layer.getPlayer(getIdentifier()) != null) {
				layer.addObject(this);
			}
		}
	}

	@Override
	public ILayerManager getLayer() {
		return fLayer;
	}
}
