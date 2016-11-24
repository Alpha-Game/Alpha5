package match;

import java.util.HashMap;
import java.util.Map;

import constants.states.MatchEffectStateType;
import joe.classes.geometry2D.Vector2D;
import joe.game.base.effect.IStateType;

public class MatchState extends AbstractMatchObjectState {
	private static class MatchCurrentState {
		private double speed;
		private double gravity;
		private double gravity_angle;
		
		public MatchCurrentState(double speed, double gravity, double gravity_angle) {
			this.speed = speed;
			this.gravity = gravity;
			this.gravity_angle = gravity_angle;
		}
	}
	
	private MatchCurrentState fLastState;
	private MatchCurrentState fCurrentState;
	
	private double fBaseSpeed;
	private Vector2D fBaseGravity;
	
	public MatchState() {
		super();
		fBaseSpeed = 1.0;
		fBaseGravity = new Vector2D(0.0, 40.0);
		
		fLastState = new MatchCurrentState(
				fBaseSpeed, 
				fBaseGravity.getMagnitude(), 
				fBaseGravity.getAngle()
		);
		fCurrentState = new MatchCurrentState(
				fBaseSpeed, 
				fBaseGravity.getMagnitude(), 
				fBaseGravity.getAngle()
		);
	}
	
	protected <V> V getValue(IStateType<V> type, MatchCurrentState state) {
		if (type == null) {
			return null;
		} else if (MatchEffectStateType.Gravity.toString().equals(type.toString())) {
			return type.getType().cast(state.gravity);
		} else if (MatchEffectStateType.Gravity_Angle.toString().equals(type.toString())) {
			return type.getType().cast(state.gravity_angle);
		} else if (MatchEffectStateType.Gravity_X.toString().equals(type.toString())) {
			return type.getType().cast(Vector2D.getValueX(state.gravity, state.gravity_angle));
		} else if (MatchEffectStateType.Gravity_Y.toString().equals(type.toString())) {
			return type.getType().cast(Vector2D.getValueY(state.gravity, state.gravity_angle));
		} else if (MatchEffectStateType.Speed.toString().equals(type.toString())) {
			return type.getType().cast(state.speed);
		}
		return null;
	}
	
	public <V> V getLastValue(IStateType<V> type) {
		return getValue(type, fLastState);
	}
	
	public <V> V getValue(IStateType<V> type) {
		return getValue(type, fCurrentState);
	}

	public void update(long time) {
		// Get base values		
		Map<String, Double> values = new HashMap<String, Double>();
		values.put(MatchEffectStateType.Speed.toString(), fBaseSpeed);
		values.put(MatchEffectStateType.Gravity.toString(), fBaseGravity.getMagnitude());
		values.put(MatchEffectStateType.Gravity_Angle.toString(), fBaseGravity.getAngle());
		
		// Apply Effects
		update(time, values);
		
		// Set next state values		
		fLastState = fCurrentState;
		fCurrentState = new MatchCurrentState(
				values.get(MatchEffectStateType.Speed.toString()),
				values.get(MatchEffectStateType.Gravity.toString()),
				values.get(MatchEffectStateType.Gravity_Angle.toString())
		);
	}
}
