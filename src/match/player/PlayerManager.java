package match.player;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import match.AbstractMatchObjectManager;
import match.player.attack.MeleeAttackManager;
import constants.PlayerConstants;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.layout.image.sprite.IAnimatedSprite;
import joe.game.layout.image.sprite.ISprite;
import joe.game.platformer.effect.IEffectStateType;
import joe.game.platformer.layer.ILayerManager;
import joe.game.platformer.match.ICollidableObject;
import joe.game.platformer.match.IMatchManager;
import joe.game.platformer.match.IMoveableObject;
import joe.game.platformer.physics.PhysicsCalculator;
import joe.game.platformer.player.Controls;
import joe.game.platformer.player.IPlayerManager;
import joe.game.platformer.tile.ISpawnableTile;
import joe.input.IControllerAction;

public class PlayerManager extends AbstractMatchObjectManager implements IPlayerManager {
	private PlayerControls fControls;
	private PlayerSpriteController fSprites;
	
	private PlayerState fState;
	
	private long fTotalDrawTime;
	
	private Set<ISpawnableTile> fSpawnPoints;
	private Random fRandom;
	
	public PlayerManager(IMatchManager matchManager, Map<String, Object> settings) {
		super(matchManager, Values.getStringValue(settings.get(PlayerConstants.Settings_Identifier), null));		
		fRandom = new Random();
		
		fControls = new PlayerControls(this);
		fControls.setCommand(PlayerConstants.Settings_Controls_MoveUp, Values.getValue(settings.get(PlayerConstants.Settings_Controls_MoveUp), null, Controls.class));
		fControls.setCommand(PlayerConstants.Settings_Controls_MoveDown, Values.getValue(settings.get(PlayerConstants.Settings_Controls_MoveDown), null, Controls.class));
		fControls.setCommand(PlayerConstants.Settings_Controls_MoveLeft, Values.getValue(settings.get(PlayerConstants.Settings_Controls_MoveLeft), null, Controls.class));
		fControls.setCommand(PlayerConstants.Settings_Controls_MoveRight, Values.getValue(settings.get(PlayerConstants.Settings_Controls_MoveRight), null, Controls.class));
		fControls.setCommand(PlayerConstants.Settings_Controls_Attack1, Values.getValue(settings.get(PlayerConstants.Settings_Controls_Attack1), null, Controls.class));
		fControls.setCommand(PlayerConstants.Settings_Controls_Attack2, Values.getValue(settings.get(PlayerConstants.Settings_Controls_Attack2), null, Controls.class));
		fControls.setCommand(PlayerConstants.Settings_Controls_Jump, Values.getValue(settings.get(PlayerConstants.Settings_Controls_Jump), null, Controls.class));
		
		fSprites = new PlayerSpriteController(this);
		fSprites.setSprite(PlayerConstants.Settings_Sprites_Stand, getSprite(Values.getStringValue(settings.get(PlayerConstants.Settings_Sprites_Stand), null)));
		fSprites.setSprite(PlayerConstants.Settings_Sprites_Walk, getSprite(Values.getStringValue(settings.get(PlayerConstants.Settings_Sprites_Walk), null)));
		fSprites.setSprite(PlayerConstants.Settings_Sprites_Run, getSprite(Values.getStringValue(settings.get(PlayerConstants.Settings_Sprites_Run), null)));
		fSprites.setSprite(PlayerConstants.Settings_Sprites_Climb, getSprite(Values.getStringValue(settings.get(PlayerConstants.Settings_Sprites_Climb), null)));
		fSprites.setSprite(PlayerConstants.Settings_Sprites_Fall, getSprite(Values.getStringValue(settings.get(PlayerConstants.Settings_Sprites_Fall), null)));
		fSprites.setSprite(PlayerConstants.Settings_Sprites_Jump, getSprite(Values.getStringValue(settings.get(PlayerConstants.Settings_Sprites_Jump), null)));
		
		fSpawnPoints = new LinkedHashSet<ISpawnableTile>();
		
		fState = new PlayerState(this, settings);
		fTotalDrawTime = 0;
	}
	
	protected boolean validSpawnPoint(ISpawnableTile tile) {
		return getState().getCurrentRectangle().getWidth() <= tile.getSpawnLocation().getWidth() 
				&& getState().getCurrentRectangle().getHeight() <= tile.getSpawnLocation().getHeight();
	}
	
	protected ISpawnableTile getSpawnPoint() {
		Iterator<ISpawnableTile> iter = fSpawnPoints.iterator();
		for(int spawnPoint = fRandom.nextInt(fSpawnPoints.size()); spawnPoint > 0; spawnPoint++) {
			iter.next();
		}
		
		ISpawnableTile start = iter.next();
		if (validSpawnPoint(start)) {
			return start;
		} else {
			ISpawnableTile next = null;
			while(iter.hasNext()) {
				next = iter.next();
				if (validSpawnPoint(next)) {
					return next;
				}
			}
			
			iter = fSpawnPoints.iterator();
			while(iter.hasNext()) {
				next = iter.next();
				if (validSpawnPoint(next)) {
					return next;
				} else if (next == start) {
					break;
				}
			}
		}
		return null;
	}
	
	protected IControllerAction getControllerFromManager(String actionIdentifier) {
		return fControls.getCommand(actionIdentifier);
	}
	
	protected ISprite getSpriteFromManager(String spriteIdentifier) {
		return fSprites.getSprite(spriteIdentifier);
	}
	
	protected PlayerState getState() {
		return fState;
	}
	
	@Override
	public Rectangle2D getLastCollisionRectangle() {
		return getState().getLastRectangle();
	}
	
	@Override
	public Rectangle2D getCollisionRectangle() {
		return getState().getCurrentRectangle();
	}
	
	@Override
	public boolean setValue(Object callingObject, IEffectStateType type, double value) {
		return getState().setValue(type, value);
	}
	
	protected void respawn() {
		ISpawnableTile spawnPoint = getSpawnPoint();
		if (spawnPoint != null) {
			setLayer(spawnPoint.getLayer());
			getState().setValue(PlayerEffectStateType.X_Left, spawnPoint.getSpawnLocation().getMinX());
			getState().setValue(PlayerEffectStateType.Y_Top, spawnPoint.getSpawnLocation().getMinY());
			getState().setValue(PlayerEffectStateType.Velocity_Y, 0.0);
			getState().setValue(PlayerEffectStateType.Velocity_X, 0.0);
			getState().setValue(PlayerEffectStateType.Health, getState().getValue(PlayerEffectStateType.MaxHealth));
		}
	}
	
	protected boolean canRespawn() {
		return true;
	}
	
	protected boolean isDead() {
		return getLayer() == null; 
	}
	
	protected void checkControls() {
		if (getControllerFromManager(PlayerConstants.Settings_Controls_MoveUp).getLastValue() > 0.0 && getState().getValue(PlayerEffectStateType.Gravity_Y) >= 0.0) {
			getState().setValue(PlayerEffectStateType.Velocity_Y_Desired, -80.0);
		} else if (getControllerFromManager(PlayerConstants.Settings_Controls_MoveDown).getLastValue() > 0.0 && getState().getValue(PlayerEffectStateType.Gravity_Y) <= 0.0) {
			getState().setValue(PlayerEffectStateType.Velocity_Y_Desired, 80.0);
		} else {
			getState().setValue(PlayerEffectStateType.Velocity_Y_Desired, 0.0);
		}
		
		if (getControllerFromManager(PlayerConstants.Settings_Controls_MoveLeft).getLastValue() > 0) {
			getState().setValue(PlayerEffectStateType.Velocity_X_Desired, -80.0);
		} else if (getControllerFromManager(PlayerConstants.Settings_Controls_MoveRight).getLastValue() > 0) {
			getState().setValue(PlayerEffectStateType.Velocity_X_Desired, 80.0);
		} else {
			getState().setValue(PlayerEffectStateType.Velocity_X_Desired, 0.0);
		}
		
		if (getControllerFromManager(PlayerConstants.Settings_Controls_Attack1).getLastValue() > 0) {
			new MeleeAttackManager(null).onAttack(this);
		}
	}
	
	protected boolean justDied() {
		return getState().getValue(PlayerEffectStateType.Health) <= 0.0;
	}
	
	protected void onDeath() {
		setLayer(null);
	}
	
	@Override
	public void update(long time) {
		if (isDead()) {
			if (canRespawn()) {
				respawn();
			}
		} else if (justDied()) {
			onDeath();
		} else {
			checkControls();
		}
		
		getState().update(time);
	}
	
	protected void drawSprite(ISprite sprite, Graphics2D g, Rectangle2D rect, long time) {
		if (sprite instanceof IAnimatedSprite) {
			((IAnimatedSprite)sprite).draw(g, getState().getCurrentRectangle(), time);
		} else {
			sprite.draw(g, getState().getCurrentRectangle());
		}
	}

	@Override
	public void draw(Graphics2D g, long time) {
		fTotalDrawTime += time;
		if (getState().getValue(PlayerEffectStateType.Velocity_X_Desired) == 0.0) {
			drawSprite(getSpriteFromManager(PlayerConstants.Settings_Sprites_Stand), g, getState().getCurrentRectangle(), fTotalDrawTime);
		} else if (getState().getValue(PlayerEffectStateType.Velocity_X_Desired) < 0.0) {
			getSpriteFromManager(PlayerConstants.Settings_Sprites_Stand).flipX(true);
			drawSprite(getSpriteFromManager(PlayerConstants.Settings_Sprites_Run).flipX(true), g, getState().getCurrentRectangle(), fTotalDrawTime);
		} else if (getState().getValue(PlayerEffectStateType.Velocity_X_Desired) > 0.0) {
			getSpriteFromManager(PlayerConstants.Settings_Sprites_Stand).flipX(false);
			drawSprite(getSpriteFromManager(PlayerConstants.Settings_Sprites_Run).flipX(false), g, getState().getCurrentRectangle(), fTotalDrawTime);
		}
	}

	@Override
	public boolean canCollide(ICollidableObject object) {
		if (object instanceof IPlayerManager) {
			return false;
		}
		return true;
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
		
		return PhysicsCalculator.isCollision(getLastCollisionRectangle(), getCollisionRectangle(), objectLastRectangle, objectRectangle);
	}

	@Override
	public ILayerManager getLayer() {
		return getState().getLayer();
	}

	@Override
	public void setLayer(ILayerManager layer) {
		ILayerManager lastLayer = getLayer();
		if (lastLayer != layer) {
			getState().setLayer(layer);
			if (lastLayer != null) {
				lastLayer.removeObject(this);
			}
			if (layer != null && layer.getPlayer(getIdentifier()) == null) {
				layer.addObject(this);
			}
		}
	}

	@Override
	public void addSpawnPoint(ISpawnableTile spawnPoint) {
		fSpawnPoints.add(spawnPoint);
	}

	@Override
	public void removeSpawnPoint(ISpawnableTile spawnPoint) {
		fSpawnPoints.remove(spawnPoint);
	}
}
