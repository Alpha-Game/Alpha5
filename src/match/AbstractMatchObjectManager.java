package match;

import main.GameManager;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.base.effect.IEffect;
import joe.game.base.effect.IEffectableStates;
import joe.game.base.effect.IStateType;
import joe.game.layout.image.sprite.ISprite;
import joe.game.twodimension.platformer.match.IMatchManager;
import joe.game.twodimension.platformer.match.IMatchObject;

public abstract class AbstractMatchObjectManager implements IMatchObject, IEffectableStates {
	private final String fIdentifier;
	private IMatchManager fMatchManager;
	
	protected AbstractMatchObjectManager(IMatchManager matchManager, String identifier) {
		fIdentifier = identifier;
		fMatchManager = matchManager;
	}
	
	protected ISprite getSprite(String sprite) {
		if (sprite != null) {
			return getManager().getSprite(sprite);
		}
		return null;
	}
	
	protected Rectangle2D getGameRectangle() {
		return getManager().getGameRectangle();
	}
	
	protected GameManager getManager() {
		return (GameManager)getMatchManager().getManager();
	}
	
	protected abstract AbstractMatchObjectState getState();
	
	@Override
	public IMatchManager getMatchManager() {
		return fMatchManager;
	}

	@Override
	public String getIdentifier() {
		return fIdentifier;
	}
	
	@Override
	public boolean addBaseEffect(Object callingObject, IEffect effect) {
		getState().addBaseEffect(callingObject, effect);
		return true;
	}

	@Override
	public boolean addEffect(Object callingObject, IEffect effect, double priority) {
		getState().addEffect(callingObject, effect, priority);
		return true;
	}

	@Override
	public boolean removeEffect(Object callingObject, IEffect effect) {
		getState().removeEffect(callingObject, effect);
		return true;
	}

	@Override
	public double getLastValue(IStateType type) {
		return getState().getLastValue(type);
	}

	@Override
	public double getValue(IEffectStateType type) {
		return getState().getValue(type);
	}
	
	@Override
	public void draw(long time, Graphics2D graphics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(long time) {
		// TODO Auto-generated method stub
		
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
	public boolean removeEffect(IMappable callingObject, String... effectIDs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject, IEffect... effects) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeEffect(IMappable callingObject, Collection<Object> effectIDs) {
		getState().removeEffect(callingObject, effect);
		return true;
	}
}
