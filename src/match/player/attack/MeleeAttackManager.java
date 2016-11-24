package match.player.attack;

import java.util.Map;

import match.player.PlayerEffectStateType;
import joe.classes.geometry2D.Rectangle2D;
import joe.game.layout.implementation.LayoutCalculator;
import joe.game.platformer.layer.ILayerManager;
import joe.game.platformer.match.ICollidableObject;
import joe.game.platformer.match.IMoveableObject;
import joe.game.platformer.physics.PhysicsCalculator;
import joe.game.platformer.player.IPlayerManager;

public class MeleeAttackManager implements ICollidableObject {
	private IPlayerManager fPlayer;
	
	public MeleeAttackManager(Map<String, Object> settings) {
	}
	
	public void onAttack(IPlayerManager currnetPlayer) {
		fPlayer = currnetPlayer;
		
		ILayerManager layer = currnetPlayer.getLayer();
		for(IPlayerManager player : layer.getPlayers()) {
			if (player != fPlayer) {
				if (canCollide(player) && player.canCollide(this)) {
					if (isCollision(player) && player.isCollision(this)) {
						onCollision(player);
					}
				}
			}
		}
	}
	
	protected void onCollision(ICollidableObject object) {
		IPlayerManager player = (IPlayerManager)object;
		player.setValue(this, PlayerEffectStateType.Health, player.getValue(PlayerEffectStateType.Health) - 1.0);
	}

	@Override
	public boolean canCollide(ICollidableObject object) {
		if (object instanceof IPlayerManager) {
			return true;
		}
		return false;
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

	@Override
	public Rectangle2D getCollisionRectangle() {
		double y = fPlayer.getValue(PlayerEffectStateType.Y_Top);
		double x = fPlayer.getValue(PlayerEffectStateType.X_Right);
		return LayoutCalculator.createBox(x, x + 30, y, y + 30);
	}
}
