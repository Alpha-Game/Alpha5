package match;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import constants.LayerConstants;
import constants.MatchConditionConstants;
import constants.MatchEventConstants;
import constants.PlayerConstants;
import constants.states.MatchEffectStateType;
import main.GameManager;
import main.Manager;
import match.event.manager.EndMatchEventManager;
import match.layer.LayerCreator;
import match.player.PlayerCreator;
import joe.classes.base.Timer;
import joe.classes.constants.GlobalConstants;
import joe.classes.controls.Control;
import joe.classes.controls.Controls;
import joe.classes.geometry2D.Dimension2D;
import joe.classes.geometry2D.Vector2D;
import joe.classes.identifier.IMappable;
import joe.game.base.effect.IEffect;
import joe.game.base.effect.IStateType;
import joe.game.base.settings.ISettingType;
import joe.game.base.statistics.IStatisticsType;
import joe.game.layout.implementation.LayoutCalculator;
import joe.game.manager.IGameManager;
import joe.game.twodimension.platformer.layer.ILayerManager;
import joe.game.twodimension.platformer.match.IMatchManager;
import joe.game.twodimension.platformer.match.IMatchObject;
import joe.game.twodimension.platformer.player.IPlayerGroup;
import joe.game.twodimension.platformer.player.IPlayerManager;
import joe.game.twodimension.platformer.tiles.ITileManager;

public class MatchManager extends Manager implements IMatchManager {
	private Map<String, IMappable> fObjects;
	private Map<String, IPlayerManager> fAllPlayers;
	private Map<String, ILayerManager> fAllLayers;
	private Map<String, ILayerManager> fActiveLayers;
	private Map<String, ITileManager> fAllTiles;
	private Set<IMatchObject> fAllEvents;
	private Timer fUpdateTimer;
	private Timer fDrawTimer;
	private long fCurrentTime;
	private long fLastTime;
	
	private MatchState fState;
	
	public MatchManager(GameManager manager, Map<ISettingType<?>, Object> settings) {
		super(manager);
		
		manager.getGameState().setGameDimensions(LayoutCalculator.createDimension(960, 540));
		
		fCurrentTime = 0;
		fLastTime = 0;
		fDrawTimer = new Timer();
		fObjects = new HashMap<String, IMappable>();
		fAllPlayers = new LinkedHashMap<String, IPlayerManager>();
		fAllLayers = new LinkedHashMap<String, ILayerManager>();
		fActiveLayers = new LinkedHashMap<String, ILayerManager>();
		fAllTiles = new HashMap<String, ITileManager>();
		fAllEvents = new LinkedHashSet<IMatchEvent>();
		fState = new MatchState();
		
		LayerCreator layorCreator = new LayerCreator();
		layorCreator.addSetting(LayerConstants.Settings_Identifier, "Layer 1");
		layorCreator.addSetting(LayerConstants.Settings_LayerSize, LayoutCalculator.createDimension(960, 540));
		layorCreator.addSetting(LayerConstants.Settings_BackgroundSprite, "Resources/Sprites:sprites?matchBackground");
		ILayerManager layer = layorCreator.createObject(this);
		layer.setIsActive(true);
		
		PlayerCreator creator = new PlayerCreator();
		creator.addSetting(PlayerConstants.Settings_Identifier, "Player 1");
		creator.addSetting(PlayerConstants.Settings_BaseDimensions, "W<30>, H<30>");
		creator.addSetting(PlayerConstants.Settings_BaseGravityMultiplier, Vector2D.fromMagnitude(1.0, 0.0));
		creator.addSetting(PlayerConstants.Settings_BaseMass, 1.0);
		creator.addSetting(PlayerConstants.Settings_BaseSpeedMultiplier, 1.0);
		creator.addSetting(PlayerConstants.Settings_BaseMaxVelocity, new Vector2D(400.0, 400.0));
		creator.addSetting(PlayerConstants.Settings_BaseAcceleration, new Vector2D(40.0, 40.0));
		creator.addSetting(PlayerConstants.Settings_BaseDecceleration, new Vector2D(40.0, 40.0));
		creator.addSetting(PlayerConstants.Settings_BaseMaxHealth, 1.0);
		
		creator.addSetting(PlayerConstants.Settings_Controls_MoveUp, new Controls(new Control(null, "Key: Up")));
		creator.addSetting(PlayerConstants.Settings_Controls_MoveDown, new Controls(new Control(null, "Key: Down")));
		creator.addSetting(PlayerConstants.Settings_Controls_MoveLeft, new Controls(new Control(null, "Key: Left")));
		creator.addSetting(PlayerConstants.Settings_Controls_MoveRight, new Controls(new Control(null, "Key: Right")));
		creator.addSetting(PlayerConstants.Settings_Controls_Attack1, new Controls(new Control(null, "Key: L")));
		
		creator.addSetting(PlayerConstants.Settings_Sprites_Stand, "Resources/Sprites:sprites?playerStand");
		creator.addSetting(PlayerConstants.Settings_Sprites_Walk, "Resources/Sprites:sprites?playerWalk");
		creator.addSetting(PlayerConstants.Settings_Sprites_Run, "Resources/Sprites:sprites?playerWalk");
		IPlayerManager player = creator.createObject(this);
		player.addSpawnPoint((ISpawnableTile)fAllTiles.get("Spawner 1"));
		
		creator = new PlayerCreator();
		creator.addSetting(PlayerConstants.Settings_Identifier, "Player 2");
		creator.addSetting(PlayerConstants.Settings_BaseDimensions, new Dimension2D(30.0, 30.0));
		creator.addSetting(PlayerConstants.Settings_BaseGravityMultiplier, null);
		creator.addSetting(PlayerConstants.Settings_BaseMass, 1.0);
		creator.addSetting(PlayerConstants.Settings_BaseSpeedMultiplier, 1.0);
		creator.addSetting(PlayerConstants.Settings_BaseMaxVelocity, new Vector2D(400.0, 400.0));
		creator.addSetting(PlayerConstants.Settings_BaseAcceleration, new Vector2D(40.0, 40.0));
		creator.addSetting(PlayerConstants.Settings_BaseDecceleration, new Vector2D(40.0, 40.0));
		creator.addSetting(PlayerConstants.Settings_BaseMaxHealth, 1.0);
		
		creator.addSetting(PlayerConstants.Settings_Controls_MoveUp, new Controls(new Control(null, "Key: W")));
		creator.addSetting(PlayerConstants.Settings_Controls_MoveDown, new Controls(new Control(null, "Key: S")));
		creator.addSetting(PlayerConstants.Settings_Controls_MoveLeft, new Controls(new Control(null, "Key: A")));
		creator.addSetting(PlayerConstants.Settings_Controls_MoveRight, new Controls(new Control(null, "Key: D")));
		creator.addSetting(PlayerConstants.Settings_Controls_Attack1, new Controls(new Control(null, "Key: E")));
		
		creator.addSetting(PlayerConstants.Settings_Sprites_Stand, "Resources/Sprites:sprites?playerStand");
		creator.addSetting(PlayerConstants.Settings_Sprites_Walk, "Resources/Sprites:sprites?playerWalk");
		creator.addSetting(PlayerConstants.Settings_Sprites_Run, "Resources/Sprites:sprites?playerWalk");
		player = creator.createObject(this);
		player.addSpawnPoint((ISpawnableTile)fAllTiles.get("Spawner 2"));
		
		Map<String, Object> settings2 = new LinkedHashMap<String, Object>();
		settings2.put(MatchConditionConstants.Settings_Identifier, "End Condition");
		settings2.put(MatchConditionConstants.Settings_Time, 600);
		IMatchCondition endCondition = new TimeConditionManager(settings2);
		settings2.clear();
		settings2.put(MatchEventConstants.Settings_Identifier, "End Event");
		settings2.put(MatchEventConstants.Settings_Condtion, endCondition);
		fAllEvents.add(new EndMatchEventManager(settings2));
	}
	
	protected MatchState getState() {
		return fState;
	}
	
	@Override
	public GameManager getManager() {
		return super.getManager();
	}
	
	@Override
	public IPlayerManager getPlayer(String playerIdentifier) {
		return fAllPlayers.get(playerIdentifier);
	}
	
	@Override
	public Iterable<IPlayerManager> getPlayers() {
		return fAllPlayers.values();
	}
	
	@Override
	public ILayerManager getLayer(String layerIdentifier) {
		return fAllLayers.get(layerIdentifier);
	}

	@Override
	public Iterable<ILayerManager> getLayers() {
		return fAllLayers.values();
	}

	@Override
	public Iterable<ILayerManager> getActiveLayers() {
		return fActiveLayers.values();
	}

	@Override
	public ITileManager getTile(String tileIdentifier) {
		return fAllTiles.get(tileIdentifier);
	}

	@Override
	public Iterable<ITileManager> getTiles() {
		return fAllTiles.values();
	}
	
	@Override
	public double getLastValue(IEffectStateType type) {
		if (MatchEffectStateType.Time.toString().equals(type.toString())) {
			return fLastTime / GlobalConstants.NANOSECONDS;
		} else {
			return getState().getLastValue(type);
		}
	}
	
	@Override
	public double getValue(IEffectStateType type) {
		if (MatchEffectStateType.Time.toString().equals(type.toString())) {
			return fCurrentTime / GlobalConstants.NANOSECONDS;
		} else {
			return getState().getValue(type);
		}
	}
	
	@Override
	public boolean addBaseEffect(Object callingObject, IEffect effect) {
		return getState().addBaseEffect(callingObject, effect);
	}
	
	@Override
	public boolean addEffect(Object callingObject, IEffect effect, double priority) {
		return getState().addEffect(callingObject, effect, priority);
	}
	
	@Override
	public boolean removeEffect(Object callingObject, IEffect effect) {
		return getState().removeEffect(callingObject, effect);
	}

	@Override
	public void update() {
		if (fUpdateTimer == null) {
			fUpdateTimer = new Timer();
		}
		long time = fUpdateTimer.lap();
		fLastTime = fCurrentTime;
		fCurrentTime += time;
		
		getState().update(time);
		
		// Update layers
		for (ILayerManager layer : fActiveLayers.values()) {
			layer.update(time);
		}
		
		// Detect collisions
		for (ILayerManager layer : fActiveLayers.values()) {
			layer.detectCollision();
		}
		
		// Update players not on an active layer
		for (IPlayerManager player : fAllPlayers.values()) {
			ILayerManager layer = player.getLayer();
			if (layer == null || !fActiveLayers.containsKey(layer.getIdentifier())) {
				player.update(time);
			}
		}
		
		for (IMatchEvent event : fAllEvents) {
			event.update(this);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		long time = fDrawTimer.lap();
		
		// Draw layers
		for (ILayerManager layer : fActiveLayers.values()) {
			layer.draw(g, time);
		}
	}
	
	@Override
	public void addObject(IMatchObject object) {
		
	}
	
	@Override
	public void removeObject(IMatchObject object) {
		if (object instanceof ILayerManager) {
			fActiveLayers.remove(object.getIdentifier());
			fAllLayers.remove(object.getIdentifier());
			for (ITileManager tile : ((ILayerManager)object).getTiles()) {
				fAllTiles.remove(tile.getIdentifier());
			}
		}
		if (object instanceof IPlayerManager) {
			fAllPlayers.remove(object.getIdentifier());
		}
	}

	@Override
	public void setLayerActive(ILayerManager layer, boolean isActive) {
		layer = fAllLayers.get(layer.getIdentifier());
		if (layer != null) {
			if (isActive) {
				fActiveLayers.put(layer.getIdentifier(), layer);
			} else {
				fActiveLayers.remove(layer);
			}
			layer.setIsActive(isActive);
		} else {
			throw new IllegalStateException("Layer must be added first before setting as Active");
		}
	}
	
	public boolean addObject(IMappable object) {
		if (object instanceof ILayerManager) {
			fAllLayers.put(object.getIdentifier(), (ILayerManager)object);
			for (ITileManager tile : ((ILayerManager)object).getTiles()) {
				fAllTiles.put(tile.getIdentifier(), tile);
			}
			if (((ILayerManager)object).isActive()) {
				fActiveLayers.put(object.getIdentifier(), (ILayerManager)object);
			}
		}
		if (object instanceof IPlayerManager) {
			fAllPlayers.put(object.getIdentifier(), (IPlayerManager)object);
		}
		fObjects.put(object.getIdentifier(), object);
		return true;
	}

	@Override
	public boolean addObject(IMappable... objects) {
		boolean areAllObjectsAdded = true;
		for (IMappable object : objects) {
			areAllObjectsAdded = areAllObjectsAdded && addObject(object);
		}
		return areAllObjectsAdded;
	}

	@Override
	public boolean addObject(Collection<IMappable> objects) {
		boolean areAllObjectsAdded = true;
		for (IMappable object : objects) {
			areAllObjectsAdded = areAllObjectsAdded && addObject(object);
		}
		return areAllObjectsAdded;
	}

	@Override
	public IMappable getObject(Object objectOrObjectIDs) {
		if (objectOrObjectIDs instanceof String) {
			return fObjects.get(objectOrObjectIDs);
		} else if (objectOrObjectIDs instanceof IMappable) {
			return fObjects.get(((IMappable) objectOrObjectIDs).getIdentifier());
		}
		return null;
	}

	@Override
	public Collection<IMappable> getObjects(Object... objectOrObjectIDs) {
		Collection<IMappable> values = new ArrayList<IMappable>(objectOrObjectIDs.length);
		for (Object object : objectOrObjectIDs) {
			values.add(getObject(object));
		}
		return values;
	}

	@Override
	public Collection<IMappable> getObjects(Collection<Object> objectOrObjectIDs) {
		Collection<IMappable> values = new ArrayList<IMappable>(objectOrObjectIDs.size());
		for (Object object : objectOrObjectIDs) {
			values.add(getObject(object));
		}
		return values;
	}

	@Override
	public IMappable removeObject(Object objectOrObjectIDs) {
		if (objectOrObjectIDs instanceof String) {
			return fObjects.remove(objectOrObjectIDs);
		} else if (objectOrObjectIDs instanceof IMappable) {
			return fObjects.remove(((IMappable) objectOrObjectIDs).getIdentifier());
		}
		return null;
	}

	@Override
	public Collection<IMappable> removeObjects(Object... objectOrObjectIDs) {
		Collection<IMappable> values = new ArrayList<IMappable>(objectOrObjectIDs.length);
		for (Object object : objectOrObjectIDs) {
			values.add(removeObject(object));
		}
		return values;
	}

	@Override
	public Collection<IMappable> removeObjects(Collection<Object> objectOrObjectIDs) {
		Collection<IMappable> values = new ArrayList<IMappable>(objectOrObjectIDs.size());
		for (Object object : objectOrObjectIDs) {
			values.add(removeObject(object));
		}
		return values;
	}

	@Override
	public IStatisticsType<?> getStatisticType(Object statisticTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStatisticsType<?>> getStatisticTypes(
			Object... statisticTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStatisticsType<?>> getStatisticTypes(
			Collection<Object> statisticTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStatisticsType<?>> getStatisticTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getStatistic(IStatisticsType<V> statisticType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getStatistic(Object statisticTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> getStatistics(Object... statisticTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> getStatistics(Collection<Object> statisticTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<IStatisticsType<?>, Object> getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addBaseEffect(IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addBaseEffect(Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addConstantEffect(double priority, IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addConstantEffect(double priority,
			Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTimedEffect(double priority, IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addTimedEffect(double priority, Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject, Object... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject,
			Collection<Object> effectIDs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <V> boolean setValue(IMappable callingObject, IStateType<V> type,
			V value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public <V> V getValue(IStateType<V> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getLastValue(IStateType<V> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addInstantEffect(IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addInstantEffect(Collection<IEffect> effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IStateType<?> getEffectType(String effectTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes(String... effectTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes(
			IStateType<?>... effectTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes(
			Collection<Object> effectTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IStateType<?>> getEffectTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISettingType<?> getSettingType(String settingTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ISettingType<?>> getSettingTypes(String... settingTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ISettingType<?>> getSettingTypes(
			ISettingType<?>... settingTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ISettingType<?>> getSettingTypes(
			Collection<Object> settingTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ISettingType<?>> getSettingTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <V> V getSetting(ISettingType<V> settingType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSetting(String settingTypeID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> getSettings(String... settingTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> getSettings(ISettingType<?>... settingTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Object> getSettings(Collection<Object> settingTypeIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<ISettingType<?>, Object> getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPlayerManager getPlayer(Object playerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPlayerManager> getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPlayerManager> getPlayers(Object... playerIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPlayerManager> getPlayers(Collection<Object> playerIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPlayerGroup getPlayerGroup(Object playerGroupID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPlayerGroup> getPlayerGroups() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPlayerGroup> getPlayerGroups(Object... playerGroupIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<IPlayerGroup> getPlayerGroups(
			Collection<Object> playerGroupIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILayerManager getLayer(Object layerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ILayerManager> getLayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ILayerManager> getLayers(Object... layerIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ILayerManager> getLayers(Collection<Object> layerIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ILayerManager getActiveLayer(Object layerID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ILayerManager> getActiveLayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ILayerManager> getActiveLayers(Object... layerIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ILayerManager> getActiveLayers(Collection<Object> layerIDs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLayerActive(double priority, ILayerManager layer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLayerInactive(ILayerManager layer) {
		// TODO Auto-generated method stub
		
	}
}
