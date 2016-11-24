package menu.main;

public class MainMenuState {
	private Boolean currentSelectionSynchronized;
	private Integer currentSelection;
	
	public MainMenuState() {
		currentSelectionSynchronized = false;
	}
	
	public int getCurrentSelection() {
		synchronized(currentSelectionSynchronized) {
			if (currentSelection == null) {
				return 0;
			}
			return currentSelection;
		}
	}
	
	public void setCurrentSelection(int selection) {
		synchronized(currentSelectionSynchronized) {
			currentSelection = selection;
		}
	}
}
