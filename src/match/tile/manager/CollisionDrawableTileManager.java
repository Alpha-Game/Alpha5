package match.tile.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import constants.TileConstants;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.platformer.effect.IEffect;
import joe.game.platformer.effect.IEffectable;
import joe.game.platformer.layer.ILayerManager;
import joe.game.platformer.match.ICollidableObject;
import joe.game.platformer.match.ICollisionDetector;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.match.IMoveableObject;
import joe.game.platformer.physics.PhysicsCalculator;
import joe.game.platformer.player.IPlayerManager;
import joe.game.platformer.tile.ITileManager;
import match.effect.Effect;
import match.effect.EffectOperatorType;
import match.player.PlayerEffectStateType;

public class CollisionDrawableTileManager extends DrawableTileManager implements ICollidableObject, ICollisionDetector {
	private Rectangle2D fCollisionRectangle;
	private Map<ICollidableObject, Collection<IEffect>> fPlayerEffectMap;
	
	public CollisionDrawableTileManager(IMatchManager previousManager, Map<String, Object> settings) {
		super(previousManager, settings);
		fPlayerEffectMap = new HashMap<ICollidableObject, Collection<IEffect>>();
		fCollisionRectangle = Values.getValue(settings.get(TileConstants.Settings_CollisionLocation), null, Rectangle2D.class);
		if (fCollisionRectangle == null) {
			fCollisionRectangle = getDrawPosition();
		}
		fCollisionRectangle = fCollisionRectangle.clone();
	}
	
	@Override
	public Rectangle2D getCollisionRectangle() {
		return fCollisionRectangle;
	}
	
	protected void onNewTopPentration(Rectangle2D platformRectangle, ICollidableObject object) {
		IPlayerManager player = (IPlayerManager)object;
		if (player.getValue(PlayerEffectStateType.Velocity_Y) > 0.0) {
			player.setValue(this, PlayerEffectStateType.Velocity_Y, 0.0);
			if (player.getValue(PlayerEffectStateType.Y_Bottom) > platformRectangle.getMinY()) {
				player.setValue(this, PlayerEffectStateType.Y_Bottom, platformRectangle.getMinY());
			}
		}
	}
	
	protected void onNewBottomPentration(Rectangle2D platformRectangle, ICollidableObject object) {
		IPlayerManager player = (IPlayerManager)object;
		if (player.getValue(PlayerEffectStateType.Velocity_Y) < 0.0) {
			player.setValue(this, PlayerEffectStateType.Velocity_Y, 0.0);
			if (player.getValue(PlayerEffectStateType.Y_Top) < platformRectangle.getMaxY()) {
				player.setValue(this, PlayerEffectStateType.Y_Top, platformRectangle.getMaxY());
			}
		}
	}
	
	protected void onNewLeftPentration(Rectangle2D platformRectangle, ICollidableObject object) {
		IPlayerManager player = (IPlayerManager)object;
		if (player.getValue(PlayerEffectStateType.Velocity_X) > 0.0) {
			player.setValue(this, PlayerEffectStateType.Velocity_X, 0.0);
			if (player.getValue(PlayerEffectStateType.X_Right) > platformRectangle.getMinX()) {
				player.setValue(this, PlayerEffectStateType.X_Right, platformRectangle.getMinX());
			}
		}
	}
	
	protected void onNewRightPentration(Rectangle2D platformRectangle, ICollidableObject object) {
		IPlayerManager player = (IPlayerManager)object;
		if (player.getValue(PlayerEffectStateType.Velocity_X) < 0.0) {
			player.setValue(this, PlayerEffectStateType.Velocity_X, 0.0);
			if (player.getValue(PlayerEffectStateType.X_Left) < platformRectangle.getMaxX()) {
				player.setValue(this, PlayerEffectStateType.X_Left, platformRectangle.getMaxX());
			}
		}
	}
	
	protected void onCollisionEffect(ICollidableObject object) {
		if (object instanceof IEffectable) {
			Rectangle2D platformRectangle = getCollisionRectangle();
			Rectangle2D playerRectangle = object.getCollisionRectangle();
			Rectangle2D playerPreviousRectangle;
			if (object instanceof IMoveableObject) {
				playerPreviousRectangle = ((IMoveableObject)object).getLastCollisionRectangle();
			} else {
				playerPreviousRectangle = playerRectangle;
			}
			
			// Add physic properties of walking on the tile
			double gravityY = ((IEffectable)object).getValue(PlayerEffectStateType.Gravity_Y);
			double gravityX = ((IEffectable)object).getValue(PlayerEffectStateType.Gravity_X);
			if (!Double.isNaN(gravityY) 
					&& ((PhysicsCalculator.isTopPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isTopPentration(platformRectangle, playerPreviousRectangle) && gravityY > 0.0)
					|| (PhysicsCalculator.isBottomPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isBottomPentration(platformRectangle, playerPreviousRectangle) && gravityY < 0.0))) {
				// Add tile effects
				if (fPlayerEffectMap.containsKey(object)) {
					IEffect firstEffect = fPlayerEffectMap.get(object).iterator().next();
					if (!(firstEffect.getStateType().equals(PlayerEffectStateType.Acceleration_X) || firstEffect.getStateType().equals(PlayerEffectStateType.Decceleration_X))) {
						for (IEffect effect : fPlayerEffectMap.remove(object)) {
							((IEffectable)object).removeEffect(this, effect);
						}
					}
				}
				
				if (!fPlayerEffectMap.containsKey(object)) {
					Set<IEffect> effects = new HashSet<IEffect>();
					IEffect effect = new Effect(PlayerEffectStateType.Acceleration_X, EffectOperatorType.Multiplcation, 10.0, null);
					effects.add(effect);
					
					effect = new Effect(PlayerEffectStateType.Decceleration_X, EffectOperatorType.Multiplcation, 10.0, null);
					effects.add(effect);
					fPlayerEffectMap.put(object, effects);
					((IEffectable)object).addBaseEffect(this, effect);
				} 
			} else if (!Double.isNaN(gravityX) 
					&& ((PhysicsCalculator.isLeftPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isLeftPentration(platformRectangle, playerPreviousRectangle) && gravityX > 0.0)
					|| (PhysicsCalculator.isRightPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isRightPentration(platformRectangle, playerPreviousRectangle) && gravityX < 0.0))) {
				// Add tile effects
				if (fPlayerEffectMap.containsKey(object)) {
					IEffect firstEffect = fPlayerEffectMap.get(object).iterator().next();
					if (!(firstEffect.getStateType().equals(PlayerEffectStateType.Acceleration_Y) || firstEffect.getStateType().equals(PlayerEffectStateType.Decceleration_Y))) {
						for (IEffect effect : fPlayerEffectMap.remove(object)) {
							((IEffectable)object).removeEffect(this, effect);
						}
					}
				}
				
				if (!fPlayerEffectMap.containsKey(object)) {
					Set<IEffect> effects = new HashSet<IEffect>();
					IEffect effect = new Effect(PlayerEffectStateType.Acceleration_Y, EffectOperatorType.Multiplcation, 10.0, null);
					effects.add(effect);
					fPlayerEffectMap.put(object, effects);
					
					effect = new Effect(PlayerEffectStateType.Decceleration_Y, EffectOperatorType.Multiplcation, 10.0, null);
					effects.add(effect);
					fPlayerEffectMap.put(object, effects);
					((IEffectable)object).addBaseEffect(this, effect);
				}
			} else {
				// Remove effects
				Collection<IEffect> effects = fPlayerEffectMap.remove(object);
				if (effects != null) {
					for (IEffect effect : effects) {
						((IEffectable)object).removeEffect(this, effect);
					}
				}
			}
		}
	}
	
	@Override
	public void update(long time) {
		// Do nothing
	}
	

	@Override
	public void detectCollision() {
		ILayerManager layer = getLayer();
		for(IPlayerManager player : layer.getPlayers()) {
			if (canCollide(player) && player.canCollide(this)) {
				if (isCollision(player) && player.isCollision(this)) {
					onCollision(player);
				} else {
					onNoCollision(player);
				}
			}
		}
	}

	@Override
	public boolean isCollision(ICollidableObject object) {
		Rectangle2D objectRectangle = object.getCollisionRectangle();
		Rectangle2D objectLastRectangle;
		if (object instanceof IMoveableObject) {
			objectLastRectangle = ((IMoveableObject)object).getLastCollisionRectangle();
		} else {
			objectLastRectangle = objectRectangle;
		}
		
		return PhysicsCalculator.isCollision(getCollisionRectangle(), getCollisionRectangle(), objectLastRectangle, objectRectangle);
	}

	protected boolean onCollision(ICollidableObject object) {
		onCollisionEffect(object);
		
		if (object instanceof IPlayerManager) {
			Rectangle2D platformRectangle = getCollisionRectangle();
			Rectangle2D playerRectangle = object.getCollisionRectangle();
			Rectangle2D playerPreviousRectangle;
			if (object instanceof IMoveableObject) {
				playerPreviousRectangle = ((IMoveableObject)object).getLastCollisionRectangle();
			} else {
				playerPreviousRectangle = playerRectangle;
			}
			
			// Make adjustments for tile collision
			if (PhysicsCalculator.isTopPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isTopPentration(platformRectangle, playerPreviousRectangle)) {
				onNewTopPentration(platformRectangle, object);
			} else if (PhysicsCalculator.isBottomPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isBottomPentration(platformRectangle, playerPreviousRectangle)) {
				onNewBottomPentration(platformRectangle, object);
			} else if (PhysicsCalculator.isLeftPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isLeftPentration(platformRectangle, playerPreviousRectangle)) {
				onNewLeftPentration(platformRectangle, object);
			} else if (PhysicsCalculator.isRightPentration(platformRectangle, playerRectangle) && !PhysicsCalculator.isRightPentration(platformRectangle, playerPreviousRectangle)) {
				onNewRightPentration(platformRectangle, object);
			}
			return true;
		}
		
		return false;
	}

	protected boolean onNoCollision(ICollidableObject object) {
		if (object instanceof IEffectable) {
			Collection<IEffect> effects = fPlayerEffectMap.remove(object);
			if (effects != null) {
				for (IEffect effect : effects) {
					((IEffectable)object).removeEffect(this, effect);
				}
			}
		}
		return true;
	}

	@Override
	public boolean canCollide(ICollidableObject object) {
		if (object instanceof ITileManager) {
			return false;
		}
		return true;
	}
}
