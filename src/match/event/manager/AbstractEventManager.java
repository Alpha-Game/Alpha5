package match.event.manager;

import java.util.Map;

import constants.MatchEventConstants;
import joe.classes.bundle.Values;
import joe.game.platformer.condition.IMatchCondition;
import joe.game.platformer.event.IMatchEvent;
import joe.game.platformer.match.IMatchManager;

public abstract class AbstractEventManager implements IMatchEvent {
	private final String fIdentifier;
	private IMatchCondition fCondition;

	public AbstractEventManager(Map<String, Object> settings) {
		fIdentifier = Values.getStringValue(settings.get(MatchEventConstants.Settings_Identifier), null);
		fCondition = Values.getValue(settings.get(MatchEventConstants.Settings_Condtion), null, IMatchCondition.class); // TODO: condition
	}

	@Override
	public String getIdentifier() {
		return fIdentifier;
	}
	
	@Override
	public IMatchCondition getCondition() {
		return fCondition;
	}
	
	protected abstract void performEvent(IMatchManager manager);

	@Override
	public void update(IMatchManager manager) {
		IMatchCondition condition = getCondition();
		if (condition == null || condition.isTrue(manager)) {
			performEvent(manager);
		}
	}
}
