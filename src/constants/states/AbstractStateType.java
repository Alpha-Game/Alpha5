package constants.states;

import joe.game.base.effect.IStateType;

public abstract class AbstractStateType<V> implements IStateType<V> {
	private final String fIdentifier;
	private final Class<V> fType;
	private final boolean fCanGet;
	private final boolean fCanSet;
	private final boolean fCanAddInstantEffect;
	private final boolean fCanAddConstantEffect;
	private final boolean fCanAddTimedEffect;
	
	protected AbstractStateType(String identifier, Class<V> type, boolean canGet, boolean canSet, boolean canAddInstantEffect, boolean canAddConstantEffect, boolean canAddTimedEffect) {
		fIdentifier = identifier;
		fType = type;
		fCanGet = canGet;
		fCanSet = canSet;
		fCanAddInstantEffect = canAddInstantEffect;
		fCanAddConstantEffect = canAddConstantEffect;
		fCanAddTimedEffect = canAddTimedEffect;
	}

	@Override
	public String getIdentifier() {
		return fIdentifier;
	}

	@Override
	public Class<V> getType() {
		return fType;
	}

	@Override
	public boolean canGet() {
		return fCanGet;
	}

	@Override
	public boolean canSet() {
		return fCanSet;
	}

	@Override
	public boolean canAddInstantEffect() {
		return fCanAddInstantEffect;
	}

	@Override
	public boolean canAddConstantEffect() {
		return fCanAddConstantEffect;
	}

	@Override
	public boolean canAddTimedEffect() {
		return fCanAddTimedEffect;
	}

}
