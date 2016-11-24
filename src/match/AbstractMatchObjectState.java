package match;

import java.util.Collection;
import java.util.Map;

import joe.classes.identifier.IMappable;
import joe.game.base.effect.IEffect;
import joe.game.base.effect.IEffectableStates;
import joe.game.base.effect.IStateType;
import match.effect.Effects;

public abstract class AbstractMatchObjectState implements IEffectableStates {
	private Effects fEffects;
	
	protected AbstractMatchObjectState() {
		fEffects = new Effects();
	}

	@Override
	public boolean addBaseEffect(Object callingObject, IEffect effect) {
		fEffects.addBaseEffect(effect);
		return true;
	}

	@Override
	public boolean addEffect(Object callingObject, IEffect effect, double priority) {
		fEffects.addEffect(effect, priority);
		return true;
	}

	@Override
	public boolean removeEffect(Object callingObject, IEffect effect) {
		fEffects.removeBaseEffect(effect);
		fEffects.removeEffect(effect);
		return true;
	}
	
	protected Map<String, Double> update(long time, Map<String, Double> values) {
		return fEffects.update(time, values);
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
	public boolean removeEffect(IMappable callingObject,
			Collection<Object> effectIDs) {
		// TODO Auto-generated method stub
		return false;
	}
}
