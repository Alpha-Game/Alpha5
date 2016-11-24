package main;

public abstract class Manager extends joe.game.manager.Manager {

	protected Manager(GameManager manager) {
		super(manager);
	}
	
	public GameManager getManager() {
		return (GameManager)super.getManager();
	}
	
	public Manager getPreviousManager() {
		return (Manager)super.getPreviousManager();
	}
}
