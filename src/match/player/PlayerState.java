package match.player;

import java.util.HashMap;
import java.util.Map;

import constants.PlayerConstants;
import constants.states.MatchEffectStateType;
import match.AbstractMatchObjectState;
import match.layer.LayerEffectStateType;
import joe.classes.bundle.Values;
import joe.classes.geometry2D.Dimension2D;
import joe.classes.geometry2D.Rectangle2D;
import joe.classes.geometry2D.Vector2D;
import joe.game.layout.implementation.LayoutCalculator;
import joe.game.platformer.effect.IEffectStateType;
import joe.game.platformer.layer.ILayerManager;
import joe.game.platformer.physics.PhysicalObject;
import joe.game.platformer.physics.PhysicsCalculator;
import joe.game.platformer.player.IPlayerManager;


public class PlayerState extends AbstractMatchObjectState {
	private static class PlayerCurrentState extends PhysicalObject {
		private PlayerState state;
		public ILayerManager layer;
		public double speed;
		public double health;
		public double maxHealth;
		
		public PlayerCurrentState(PlayerState state, ILayerManager layer, double x, double y, double width,	double height, double velocity_x, double velocity_y, double velocity_x_max, double velocity_y_max, double velocity_x_desired, double velocity_y_desired, double acceleration_x, double acceleration_y, double deccelartion_x, double deccelartion_y, double gravity_x, double gravity_y, double mass, double speed, double health, double maxHealth) {
			super(x, y, width, height, velocity_x, velocity_y, velocity_x_max, velocity_y_max, velocity_x_desired, velocity_y_desired, acceleration_x, acceleration_y, deccelartion_x, deccelartion_y, gravity_x, gravity_y, mass);
			this.state = state;
			this.layer = layer;
			this.speed = speed;
			this.health = health;
			this.maxHealth = maxHealth;
		}
		
		@Override
		protected PlayerCurrentState create(Double x, Double y, Double width, Double height, Double velocity_x, Double velocity_y, Double velocity_x_max, Double velocity_y_max, Double velocity_x_desired, Double velocity_y_desired, Double acceleration_x, Double acceleration_y, Double decceleration_x, Double decceleration_y, Double gravity_x, Double gravity_y, Double mass) {
			return new PlayerCurrentState(
					state,
					layer,
					x,
					y,
					width,
					height,
					velocity_x,
					velocity_y,
					velocity_x_max,
					velocity_y_max,
					velocity_x_desired,
					velocity_y_desired,
					acceleration_x,
					acceleration_y,
					decceleration_x,
					decceleration_y,
					gravity_x,
					gravity_y,
					mass,
					speed,
					health,
					maxHealth
			);
		}
		
		@Override
		public PlayerCurrentState clone() {
			return (PlayerCurrentState) super.clone();
		}
		
		@Override
		public PlayerCurrentState getNextState(long time) {
			PlayerCurrentState state = (PlayerCurrentState) super.getNextState((long)(speed * time));
			return state;
		}
	}	
	private Dimension2D fBaseDimensions;
	private Vector2D fBaseGravityMultiplier;
	private double fBaseMass;
	private double fBaseSpeedMultiplier;
	private Vector2D fBaseMaxVelocity;
	private Vector2D fBaseAcceleration;
	private Vector2D fBaseDecceleration;
	private double fBaseMaxHealth;
	
	private PlayerCurrentState fLastState;
	private PlayerCurrentState fCurrentState;
	
	private IPlayerManager fPlayerManager;
	
	
	public PlayerState(IPlayerManager playerManager, Map<String, Object> settings) {
		super();
		fPlayerManager = playerManager;	
		
		fBaseDimensions = (Dimension2D) Values.getValue(settings.get(PlayerConstants.Settings_BaseDimensions), LayoutCalculator.createDimension(0.0, 0.0), Dimension2D.class).clone();
		fBaseGravityMultiplier = Values.getValue(settings.get(PlayerConstants.Settings_BaseGravityMultiplier), Vector2D.fromMagnitude(1.0, 0.0), Vector2D.class).clone();
		fBaseMass = Values.getDoubleValue(settings.get(PlayerConstants.Settings_BaseMass), 1.0);
		fBaseSpeedMultiplier = Values.getDoubleValue(settings.get(PlayerConstants.Settings_BaseSpeedMultiplier), 1.0);
		fBaseMaxVelocity = Values.getValue(settings.get(PlayerConstants.Settings_BaseMaxVelocity), new Vector2D(0.0, 0.0), Vector2D.class).clone();
		fBaseAcceleration = Values.getValue(settings.get(PlayerConstants.Settings_BaseAcceleration), new Vector2D(0.0, 0.0), Vector2D.class).clone();
		fBaseDecceleration = Values.getValue(settings.get(PlayerConstants.Settings_BaseDecceleration), new Vector2D(0.0, 0.0), Vector2D.class).clone(); 
		fBaseMaxHealth = Values.getDoubleValue(settings.get(PlayerConstants.Settings_BaseMaxHealth), 1.0);
		
		fLastState = new PlayerCurrentState(this, null, 0, 0, fBaseDimensions.getWidth(), fBaseDimensions.getHeight(), 0.0, 0.0, fBaseMaxVelocity.getValueX(), fBaseMaxVelocity.getValueY(), 0.0, 0.0, fBaseAcceleration.getValueX(), fBaseAcceleration.getValueY(), fBaseDecceleration.getValueX(), fBaseDecceleration.getValueY(), fBaseGravityMultiplier.getValueX(), fBaseGravityMultiplier.getValueY(), fBaseMass, fBaseSpeedMultiplier, fBaseMaxHealth, fBaseMaxHealth);
		fCurrentState = fLastState.clone();
	}
	
	
	public Rectangle2D getLastRectangle() {
		if (fCurrentState.layer != null && fLastState.layer == null) {
			return getCurrentRectangle();
		}
		return LayoutCalculator.createRectangle(fLastState.x, fLastState.y, fLastState.width, fLastState.height);
	}
	
	public Rectangle2D getCurrentRectangle() {
		return LayoutCalculator.createRectangle(fCurrentState.x, fCurrentState.y, fCurrentState.width, fCurrentState.height);
	}
	
	protected IPlayerManager getPlayerManager() {
		return fPlayerManager;
	}
	
	protected double getValue(IEffectStateType type, PlayerCurrentState state) {
		if (type == null) {
			return Double.NaN;
		} else if (PlayerEffectStateType.Width.toString().equals(type.toString())) {
			return state.width;
		} else if (PlayerEffectStateType.Height.toString().equals(type.toString())) {
			return state.height;
		} else if (PlayerEffectStateType.X_Left.toString().equals(type.toString())) {
			return state.x;
		} else if (PlayerEffectStateType.X_Right.toString().equals(type.toString())) {
			return state.x + state.width;
		} else if (PlayerEffectStateType.Y_Top.toString().equals(type.toString())) {
			return state.y;
		} else if (PlayerEffectStateType.Y_Bottom.toString().equals(type.toString())) {
			return state.y + state.height;
		} else if (PlayerEffectStateType.Velocity_X.toString().equals(type.toString())) {
			return state.velocity_x;
		} else if (PlayerEffectStateType.Velocity_Y.toString().equals(type.toString())) {
			return state.velocity_y;
		} else if (PlayerEffectStateType.Velocity_X_Desired.toString().equals(type.toString())) {
			return state.velocity_x_desired;
		} else if (PlayerEffectStateType.Velocity_Y_Desired.toString().equals(type.toString())) {
			return state.velocity_y_desired;
		} else if (PlayerEffectStateType.Velocity_X_Max.toString().equals(type.toString())) {
			return state.velocity_x_max;
		} else if (PlayerEffectStateType.Velocity_Y_Max.toString().equals(type.toString())) {
			return state.velocity_y_max;
		} else if (PlayerEffectStateType.Acceleration_X.toString().equals(type.toString())) {
			return state.acceleration_x;
		} else if (PlayerEffectStateType.Acceleration_Y.toString().equals(type.toString())) {
			return state.acceleration_y;
		} else if (PlayerEffectStateType.Decceleration_X.toString().equals(type.toString())) {
			return state.decceleration_x;
		} else if (PlayerEffectStateType.Decceleration_Y.toString().equals(type.toString())) {
			return state.decceleration_y;
		} else if (PlayerEffectStateType.Speed.toString().equals(type.toString())) {
			return state.speed;
		} else if (PlayerEffectStateType.Gravity.toString().equals(type.toString())) {
			return PhysicsCalculator.getValue(state.gravity_x, state.gravity_y);
		} else if (PlayerEffectStateType.Gravity_Angle.toString().equals(type.toString())) {
			return PhysicsCalculator.getAngleInDegrees(state.gravity_x, state.gravity_y);
		} else if (PlayerEffectStateType.Gravity_X.toString().equals(type.toString())) {
			return state.gravity_x;
		} else if (PlayerEffectStateType.Gravity_Y.toString().equals(type.toString())) {
			return state.gravity_y;
		} else if (PlayerEffectStateType.Mass.toString().equals(type.toString())) {
			return state.mass;
		} else if (PlayerEffectStateType.MaxHealth.toString().equals(type.toString())) {
			return state.maxHealth;
		} else if (PlayerEffectStateType.Health.toString().equals(type.toString())) {
			return state.health;
		}
		return Double.NaN;
	}
	
	public void setLayer(ILayerManager layer) {
		fCurrentState.layer = layer;
	}
	
	public ILayerManager getLayer() {
		return fCurrentState.layer;
	}
	
	public double getLastValue(IEffectStateType type) {
		return getValue(type, fLastState);
	}
	
	public double getValue(IEffectStateType type) {
		return getValue(type, fCurrentState);
	}
	
	public boolean setValue(IEffectStateType type, double value) {
		if (type == null) {
			return false;
		} else if (PlayerEffectStateType.X_Left.toString().equals(type.toString())) {
			fCurrentState.x = value;
		} else if (PlayerEffectStateType.X_Right.toString().equals(type.toString())) {
			fCurrentState.x = value - fCurrentState.width;
		} else if (PlayerEffectStateType.Y_Top.toString().equals(type.toString())) {
			fCurrentState.y = value;
		} else if (PlayerEffectStateType.Y_Bottom.toString().equals(type.toString())) {
			fCurrentState.y = value - fCurrentState.height;
		}  else if (PlayerEffectStateType.Velocity_X.toString().equals(type.toString())) {
			fCurrentState.velocity_x = value;
		} else if (PlayerEffectStateType.Velocity_Y.toString().equals(type.toString())) {
			fCurrentState.velocity_y = value;
		} else if (PlayerEffectStateType.Velocity_X_Desired.toString().equals(type.toString())) {
			fCurrentState.velocity_x_desired = value;
		} else if (PlayerEffectStateType.Velocity_Y_Desired.toString().equals(type.toString())) {
			fCurrentState.velocity_y_desired = value;
		} else if (PlayerEffectStateType.Health.toString().equals(type.toString())) {
			fCurrentState.health = value;
		} else {
			return false;
		}
		return true;
	}
	
	public void update(long time) {
		ILayerManager layer = getPlayerManager().getLayer();
		
		// Get base values
		double gravity_x = (layer != null ? layer.getValue(LayerEffectStateType.Gravity_X) : getPlayerManager().getMatchManager().getValue(MatchEffectStateType.Gravity_X));
		double gravity_y = (layer != null ? layer.getValue(LayerEffectStateType.Gravity_Y) : getPlayerManager().getMatchManager().getValue(MatchEffectStateType.Gravity_Y));
		
		Map<String, Double> values = new HashMap<String, Double>();
		values.put(PlayerEffectStateType.Speed.toString(), fBaseSpeedMultiplier * (layer != null ? layer.getValue(LayerEffectStateType.Speed) : getPlayerManager().getMatchManager().getValue(MatchEffectStateType.Speed)));
		values.put(PlayerEffectStateType.Gravity.toString(), fBaseGravityMultiplier.getMagnitude() + Vector2D.getMagnitude(gravity_x, gravity_y));
		values.put(PlayerEffectStateType.Gravity_Angle.toString(), fBaseGravityMultiplier.getAngle() + Vector2D.getAngle(gravity_x, gravity_y));
		values.put(PlayerEffectStateType.Acceleration_X.toString(), fBaseAcceleration.getValueX());
		values.put(PlayerEffectStateType.Acceleration_Y.toString(), fBaseAcceleration.getValueY());
		values.put(PlayerEffectStateType.Decceleration_X.toString(), fBaseDecceleration.getValueX());
		values.put(PlayerEffectStateType.Decceleration_Y.toString(), fBaseDecceleration.getValueY());
		values.put(PlayerEffectStateType.Velocity_X_Max.toString(), fBaseMaxVelocity.getValueX());
		values.put(PlayerEffectStateType.Velocity_Y_Max.toString(), fBaseMaxVelocity.getValueY());
		values.put(PlayerEffectStateType.Width.toString(), fBaseDimensions.getWidth());
		values.put(PlayerEffectStateType.Height.toString(), fBaseDimensions.getHeight());
		values.put(PlayerEffectStateType.Mass.toString(), fBaseMass);
		values.put(PlayerEffectStateType.MaxHealth.toString(), fBaseMaxHealth);
		
		// Apply Effects
		update(time, values);
		
		double gravity = values.get(PlayerEffectStateType.Gravity.toString());
		double gravityAngle = values.get(PlayerEffectStateType.Gravity_Angle.toString());
		
		// Set next state values
		PlayerCurrentState nextState = fCurrentState.clone();
		nextState.speed = values.get(PlayerEffectStateType.Speed.toString());
		nextState.gravity_x = PhysicsCalculator.getValueX(gravity, gravityAngle);
		nextState.gravity_y = PhysicsCalculator.getValueY(gravity, gravityAngle);
		nextState.acceleration_x = values.get(PlayerEffectStateType.Acceleration_X.toString());
		nextState.acceleration_y = values.get(PlayerEffectStateType.Acceleration_Y.toString());
		nextState.decceleration_x = values.get(PlayerEffectStateType.Decceleration_X.toString());
		nextState.decceleration_y = values.get(PlayerEffectStateType.Decceleration_Y.toString());
		nextState.velocity_x_max = values.get(PlayerEffectStateType.Velocity_X_Max.toString());
		nextState.velocity_y_max = values.get(PlayerEffectStateType.Velocity_Y_Max.toString());
		nextState.width = values.get(PlayerEffectStateType.Width.toString());
		nextState.height = values.get(PlayerEffectStateType.Height.toString());
		nextState.mass = values.get(PlayerEffectStateType.Mass.toString());
		nextState.maxHealth = values.get(PlayerEffectStateType.MaxHealth.toString());
		if (nextState.health > nextState.maxHealth) {
			nextState.health = nextState.maxHealth;
		}		
		
		
		// Set next state
		nextState = nextState.getNextState(time);
		fLastState = fCurrentState;
		fCurrentState = nextState;
	}
}
