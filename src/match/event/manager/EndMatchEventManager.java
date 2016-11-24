package match.event.manager;

import java.util.Map;

import joe.game.platformer.match.IMatchManager;

public class EndMatchEventManager extends AbstractEventManager {

	public EndMatchEventManager(Map<String, Object> settings) {
		super(settings);
	}

	@Override
	protected void performEvent(IMatchManager manager) {
		manager.getManager().setManager(manager.getPreviousManager());
	}
}
