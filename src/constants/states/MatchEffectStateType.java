package constants.states;

public class MatchEffectStateType<V> extends AbstractStateType<V> {
	public static final MatchEffectStateType<Double> Time = new MatchEffectStateType<Double>("Time", Double.class, true, false, true, true, true);
	public static final MatchEffectStateType<Double> Speed = new MatchEffectStateType<Double>("Speed", Double.class, true, false, true, true, true);
	public static final MatchEffectStateType<Double> Gravity = new MatchEffectStateType<Double>("Gravity", Double.class, true, false, false, false, false);
	public static final MatchEffectStateType<Double> Gravity_Angle = new MatchEffectStateType<Double>("Gravity_Angle", Double.class, true, false, false, false, false);
	public static final MatchEffectStateType<Double> Gravity_X = new MatchEffectStateType<Double>("Gravity_X", Double.class, true, false, true, true, true);
	public static final MatchEffectStateType<Double> Gravity_Y = new MatchEffectStateType<Double>("Gravity_Y", Double.class, true, false, true, true, true);
	
	private MatchEffectStateType(String identifier, Class<V> type, boolean canGet, boolean canSet, boolean canAddInstantEffect, boolean canAddConstantEffect, boolean canAddTimedEffect) {
		super(identifier, type, canGet, canSet, canAddInstantEffect, canAddConstantEffect, canAddTimedEffect);
	}
}
