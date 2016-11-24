package match.layer;

import java.util.HashMap;
import java.util.Map;

import constants.LayerConstants;
import constants.states.MatchEffectStateType;
import match.AbstractMatchObjectState;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Vector2D;
import joe.game.base.effect.IStateType;
import joe.game.twodimension.platformer.layer.ILayerManager;

public class LayerState extends AbstractMatchObjectState {
	private static class LayerCurrentState {
		private double speed;
		private double gravity;
		private double gravity_angle;
		
		public LayerCurrentState(double speed, double gravity, double gravity_angle) {
			this.speed = speed;
			this.gravity = gravity;
			this.gravity_angle = gravity_angle;
		}
	}
	
	private ILayerManager fLayer;
	
	private LayerCurrentState fLastState;
	private LayerCurrentState fCurrentState;
	
	private double fBaseSpeedMultiplier;
	private Vector2D fBaseGravityMultiplier;
	
	public LayerState(ILayerManager layer, Map<String, Object> settings) {
		super();
		fLayer = layer;
		
		fBaseSpeedMultiplier = Values.getDoubleValue(settings.get(LayerConstants.Settings_BaseSpeedMultiplier), 1.0);
		fBaseGravityMultiplier = Values.getValue(settings.get(LayerConstants.Settings_BaseGravityMultiplier), Vector2D.fromMagnitude(1, 0), Vector2D.class);
		
		fLastState = new LayerCurrentState(
				fLayer.getMatchManager().getLastValue(MatchEffectStateType.Speed) * fBaseSpeedMultiplier,
				fLayer.getMatchManager().getLastValue(MatchEffectStateType.Gravity) * fBaseGravityMultiplier.getMagnitude(),
				fLayer.getMatchManager().getLastValue(MatchEffectStateType.Gravity_Angle) + fBaseGravityMultiplier.getAngle()
		);
		fCurrentState = new LayerCurrentState(
				fLayer.getMatchManager().getValue(MatchEffectStateType.Speed) * fBaseSpeedMultiplier, 
				fLayer.getMatchManager().getValue(MatchEffectStateType.Gravity) * fBaseGravityMultiplier.getMagnitude(), 
				fLayer.getMatchManager().getValue(MatchEffectStateType.Gravity_Angle) + fBaseGravityMultiplier.getAngle()
		);
	}
	
	protected double getValue(IEffectStateType type, LayerCurrentState state) {
		if (type == null) {
			return Double.NaN;
		} else if (LayerEffectStateType.Gravity.toString().equals(type.toString())) {
			return state.gravity;
		} else if (LayerEffectStateType.Gravity_Angle.toString().equals(type.toString())) {
			return state.gravity_angle;
		} else if (LayerEffectStateType.Gravity_X.toString().equals(type.toString())) {
			return Vector2D.getValueX(state.gravity, state.gravity_angle);
		} else if (LayerEffectStateType.Gravity_Y.toString().equals(type.toString())) {
			return Vector2D.getValueY(state.gravity, state.gravity_angle);
		} else if (LayerEffectStateType.Speed.toString().equals(type.toString())) {
			return state.speed;
		}
		return Double.NaN;
	}
	
	public double getLastValue(IEffectStateType type) {
		return getValue(type, fLastState);
	}
	
	public double getValue(IStateType type) {
		return getValue(type, fCurrentState);
	}
	
	public void update(long time) {
		double gravity_x = fLayer.getMatchManager().getValue(MatchEffectStateType.Gravity_X);
		double gravity_y = fLayer.getMatchManager().getValue(MatchEffectStateType.Gravity_Y);
		
		
		// Get base values		
		Map<String, Double> values = new HashMap<String, Double>();
		values.put(LayerEffectStateType.Speed.toString(), fBaseSpeedMultiplier * fLayer.getMatchManager().getValue(MatchEffectStateType.Speed));
		values.put(LayerEffectStateType.Gravity.toString(), fBaseGravityMultiplier.getMagnitude() + Vector2D.getMagnitude(gravity_x, gravity_y));
		values.put(LayerEffectStateType.Gravity_Angle.toString(), fBaseGravityMultiplier.getAngle() + Vector2D.getAngle(gravity_x, gravity_y));
		
		// Apply Effects
		update(time, values);
		

		// Set next state values		
		fLastState = fCurrentState;
		fCurrentState = new LayerCurrentState(
				values.get(LayerEffectStateType.Speed.toString()),
				values.get(LayerEffectStateType.Gravity.toString()),
				values.get(LayerEffectStateType.Gravity_Angle.toString())
		);
	}
}
