package match.effect;

import joe.game.platformer.effect.IEffect;
import joe.game.platformer.effect.IEffectOperatorType;
import joe.game.platformer.effect.IEffectStateType;

public class Effect implements IEffect {
	private IEffectStateType fEffectType;
	private IEffectOperatorType fOperatorType;
	private double fValue;
	private Long fDuration;
	
	public Effect(IEffectStateType effectType, IEffectOperatorType operatorType, double value, Long duration) {
		fEffectType = effectType;
		fOperatorType = operatorType;
		fValue = value;
		fDuration = duration;
	}
	
	@Override
	public IEffectStateType getStateType() {
		return fEffectType;
	}
	
	@Override
	public IEffectOperatorType getOperatorType() {
		return fOperatorType;
	}
	
	@Override
	public double getValue() {
		return fValue;
	}
	
	@Override
	public Long getDuration() {
		return fDuration;
	}
	
	@Override
	public double computeNewValue(IEffectStateType type, double value) {
		if (fEffectType != null && fEffectType.toString().equals(type.toString())) {
			return computeNewValue(fOperatorType, value);
		}
		return value;
	}
	
	protected double computeNewValue(IEffectOperatorType operator, double value) {
		if (EffectOperatorType.Value.equals(operator)) {
			return fValue;
		} else if (EffectOperatorType.Addition.equals(operator)) {
			return value + fValue;
		} else if (EffectOperatorType.Multiplcation.equals(operator)) {
			return value * fValue;
		} else {
			return value;
		}
	}
}
