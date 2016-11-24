package match.layer;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

import constants.LayerConstants;
import constants.TileConstants;
import match.AbstractMatchObjectManager;
import match.player.PlayerEffectStateType;
import match.tile.creator.CollisionDrawableTileCreator;
import match.tile.creator.SpawnableTileCreator;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Dimension2D;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.layout.IDrawable;
import joe.game.layout.implementation.LayoutCalculator;
import joe.game.layout.implementation.Position;
import joe.game.layout.implementation.image.ImageDrawer;
import joe.game.platformer.layer.ILayerManager;
import joe.game.platformer.layer.ILayerObject;
import joe.game.platformer.match.ICollisionDetector;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.physics.CollisionType;
import joe.game.platformer.physics.PhysicsCalculator;
import joe.game.platformer.player.IPlayerManager;
import joe.game.platformer.tile.ISpawnableTile;
import joe.game.platformer.tile.ITileManager;

public class LayerManager extends AbstractMatchObjectManager implements ILayerManager {
	private IDrawable fBackground;
	private Map<String, IPlayerManager> fPlayers;
	private Map<String, ITileManager> fTiles;
	private Map<String, ITileManager> fBackgroundTiles;
	private Map<String, ITileManager> fForegroundTiles;
	private Map<String, ISpawnableTile> fSpawnableTiles;
	private Map<String, ICollisionDetector> fCollisionDetectors;
	
	private boolean fisActive;
	private Dimension2D fLayerSize;
	private LayerState fState;
	

	public LayerManager(IMatchManager matchManager, Map<String, Object> settings) {
		super(matchManager, Values.getStringValue(settings.get(LayerConstants.Settings_Identifier), null));
		fPlayers = new LinkedHashMap<String, IPlayerManager>();
		fTiles = new LinkedHashMap<String, ITileManager>();
		fBackgroundTiles = new LinkedHashMap<String, ITileManager>();
		fForegroundTiles = new LinkedHashMap<String, ITileManager>();
		fSpawnableTiles = new LinkedHashMap<String, ISpawnableTile>();
		fCollisionDetectors = new LinkedHashMap<String, ICollisionDetector>();
		
		fisActive = false;
		fLayerSize = Values.getValue(settings.get(LayerConstants.Settings_LayerSize), null, Dimension2D.class);
		fBackground = getSprite(Values.getStringValue(settings.get(LayerConstants.Settings_BackgroundSprite), null));
		
		fState = new LayerState(this, settings);
		
		// Create Spawners
		createSpawner(LayoutCalculator.createRectangle(45, 49.99, 30, 30));
		createSpawner(LayoutCalculator.createRectangle(215, 169.99, 30, 30));
		
		
		// Create Tiles
		createTile(LayoutCalculator.createRectangle(-29, 0, 30, 540));
		createTile(LayoutCalculator.createRectangle(0, -29, 960, 30));
		createTile(LayoutCalculator.createRectangle(959, 0, 30, 540));
		createTile(LayoutCalculator.createRectangle(0, 539, 960, 30));
		createTile(LayoutCalculator.createRectangle(30, 80, 60, 30));
		createTile(LayoutCalculator.createRectangle(200, 200, 60, 30));
	}
	
	static int counter = 1;
	
	private void createSpawner(Rectangle2D rectangle) {
		SpawnableTileCreator spawner = new SpawnableTileCreator();
		spawner.addSetting(TileConstants.Settings_Identifier, "Spawner " + counter++);
		spawner.addSetting(TileConstants.Settings_SpawnLocation, rectangle);
		addObject(spawner.createObject(getMatchManager()));
	}
	
	private void createTile(Rectangle2D rectangle) {
		CollisionDrawableTileCreator tile = new CollisionDrawableTileCreator();
		tile.addSetting(TileConstants.Settings_Identifier, "Tile " + counter++);
		tile.addSetting(TileConstants.Settings_SpriteLocation, "Resources/Sprites:sprites?basicPlatform");
		tile.addSetting(TileConstants.Settings_DrawLocation, rectangle);
		tile.addSetting(TileConstants.Settings_CollisionLocation, rectangle);
		tile.addSetting(TileConstants.Settings_IsBackground, false);
		addObject(tile.createObject(getMatchManager()));
	}
	
	protected Dimension2D getLayerDimension() {
		return fLayerSize;
	}
	
	@Override
	protected LayerState getState() {
		return fState;
	}

	@Override
	public IPlayerManager getPlayer(String playerIdentifier) {
		return fPlayers.get(playerIdentifier);
	}

	@Override
	public Iterable<IPlayerManager> getPlayers() {
		return fPlayers.values();
	}

	@Override
	public ITileManager getTile(String tileIdentifier) {
		return fTiles.get(tileIdentifier);
	}

	@Override
	public Iterable<ITileManager> getTiles() {
		return fTiles.values();
	}
	
	protected void calculatePlayers(long time, int playerStart) {
		int currentPlayer = 0;
		try {
			for (IPlayerManager player : fPlayers.values()) {
				if (playerStart <= currentPlayer) {
					player.update(time);
				}
				currentPlayer++;
			}
		} catch (Throwable t) {
			calculatePlayers(time, currentPlayer);
		}
	}

	@Override
	public void update(long time) {
		fState.update(time);
	
		calculatePlayers(time, 0);
		
		for (ITileManager tile : fTiles.values()) {
			tile.update(time);
		}
	}
	
	protected void onOutsideOfLayer(ILayerObject object, Position position) {
		if (object instanceof IPlayerManager) {
			IPlayerManager player = (IPlayerManager)object;
			
			Dimension2D layerBounds = getLayerDimension();
			if (position.isBottom()) {
				double bottom = player.getValue(PlayerEffectStateType.Y_Top) % layerBounds.getHeight();
				player.setValue(this, PlayerEffectStateType.Y_Bottom, bottom < 0.0 ? bottom + layerBounds.getHeight() : bottom);
			} else if (position.isTop()) {
				double top = player.getValue(PlayerEffectStateType.Y_Bottom) % layerBounds.getHeight();
				player.setValue(this, PlayerEffectStateType.Y_Top, top < 0.0 ? top + layerBounds.getHeight() : top);
			}
			
			if (position.isRight()) {
				double right = player.getValue(PlayerEffectStateType.X_Left) % layerBounds.getWidth();
				player.setValue(this, PlayerEffectStateType.X_Right, right < 0.0 ? right + layerBounds.getWidth() : right);
			} else if (position.isLeft()) {
				double left = player.getValue(PlayerEffectStateType.X_Right) % layerBounds.getWidth();
				player.setValue(this, PlayerEffectStateType.X_Left, left < 0.0 ? left + layerBounds.getWidth() : left);
			}
		}
	}
	
	protected void onEdgeOfLayer(ILayerObject object, CollisionType position) {
		// Do nothing
		if (object instanceof IPlayerManager) {
			//IPlayerManager player = (IPlayerManager)object;
		}
	}
	
	@Override
	public void detectCollision() {
		// Detect if players are outside of layer bounds after collision updates
		Dimension2D layerBounds = getLayerDimension();
		for (IPlayerManager player : fPlayers.values()) {
			Rectangle2D playerBounds = player.getCollisionRectangle();
			
			if (!PhysicsCalculator.isCollision(LayoutCalculator.createRectangle(0, 0, layerBounds.getWidth(), layerBounds.getHeight()), playerBounds)) {
				onOutsideOfLayer(player, Position.create(player.getValue(PlayerEffectStateType.Y_Bottom) < 0.0, player.getValue(PlayerEffectStateType.Y_Top) > layerBounds.getHeight(), player.getValue(PlayerEffectStateType.X_Right) < 0.0, player.getValue(PlayerEffectStateType.X_Left) > layerBounds.getWidth()));
			}
		}
		
		for (ICollisionDetector detector : fCollisionDetectors.values()) {
			detector.detectCollision();
		}
		
		// Detect if players are outside of layer bounds after collision updates
		layerBounds = getLayerDimension();
		for (IPlayerManager player : fPlayers.values()) {
			Rectangle2D playerBounds = player.getCollisionRectangle();
			
			if (!PhysicsCalculator.isCollision(LayoutCalculator.createRectangle(0, 0, layerBounds.getWidth(), layerBounds.getHeight()), playerBounds)) {
				onOutsideOfLayer(player, Position.create(player.getValue(PlayerEffectStateType.Y_Bottom) < 0.0, player.getValue(PlayerEffectStateType.Y_Top) > layerBounds.getHeight(), player.getValue(PlayerEffectStateType.X_Right) < 0.0, player.getValue(PlayerEffectStateType.X_Left) > layerBounds.getWidth()));
			}
		}
	}

	@Override
	public void draw(Graphics2D g, long time) {
		Rectangle2D gameRectangle = getManager().getGameRectangle();
		Dimension2D layerBounds = getLayerDimension();
		BufferedImage newImage = new BufferedImage((int)layerBounds.getWidth(), (int)layerBounds.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = newImage.createGraphics();
		
		if (fBackground != null) {
			fBackground.draw(g2, LayoutCalculator.createRectangle(0, 0, layerBounds.getWidth(), layerBounds.getHeight()));
		}
		
		for (ITileManager tile : fBackgroundTiles.values()) {
			tile.draw(g2, time);
		}
		
		for (IPlayerManager player : fPlayers.values()) {
			player.draw(g2, time);
		}
		
		for (ITileManager tile : fForegroundTiles.values()) {
			tile.draw(g2, time);
		}
		
		 g2.dispose();
		 ImageDrawer.draw(g, newImage, gameRectangle.getMinX(), gameRectangle.getMinY(), gameRectangle.getWidth(), gameRectangle.getHeight());
	}

	@Override
	public void addObject(ILayerObject object) {
		if (object instanceof IPlayerManager) {
			fPlayers.put(object.getIdentifier(), (IPlayerManager)object);
		} 
		if (object instanceof ITileManager) {
			ITileManager tile = (ITileManager)object;
			fTiles.put(tile.getIdentifier(), tile);
			if (tile.isBackgroundTile()) {
				fBackgroundTiles.put(tile.getIdentifier(), tile);
			} else {
				fForegroundTiles.put(tile.getIdentifier(), tile);
			}
			if (tile instanceof ISpawnableTile) {
				fSpawnableTiles.put(tile.getIdentifier(), (ISpawnableTile)tile);
			}
		}
		if (object instanceof ICollisionDetector) {
			fCollisionDetectors.put(object.getIdentifier(), (ICollisionDetector)object);
		}
		
		// Force to this layer
		if (object.getLayer() != this) {
			object.setLayer(this);
		}
	}

	@Override
	public void removeObject(ILayerObject object) {
		if (object instanceof IPlayerManager) {
			fPlayers.remove(object.getIdentifier());
		} 
		if (object instanceof ITileManager) {
			fTiles.remove(object.getIdentifier());
			fBackgroundTiles.remove(object.getIdentifier());
			fForegroundTiles.remove(object.getIdentifier());
			fSpawnableTiles.remove(object.getIdentifier());
		}
		if (object instanceof ICollisionDetector) {
			fCollisionDetectors.remove(object.getIdentifier());
		}
	}

	@Override
	public void setIsActive(boolean isActive) {
		if (fisActive != isActive) {
			fisActive = isActive;
			getMatchManager().setLayerActive(this, isActive);
		}
	}

	@Override
	public boolean isActive() {
		return fisActive;
	}
}
