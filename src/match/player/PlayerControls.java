package match.player;

import java.util.HashMap;
import java.util.Map;

import joe.game.platformer.player.Control;
import joe.game.platformer.player.Controls;
import joe.game.platformer.player.IPlayerManager;
import joe.input.ControllerAction;
import joe.input.IControllerAction;
import joe.input.IODeviceList;

public class PlayerControls {
	private Map<String, IControllerAction> fActionMap;
	private IPlayerManager fPlayerManager;
	
	public PlayerControls(IPlayerManager playerManager) {
		fActionMap = new HashMap<String, IControllerAction>();
		fPlayerManager = playerManager;
	}
	
	protected IODeviceList getIOReader() {
		return fPlayerManager.getMatchManager().getManager().getIOReader();
	}
	
	protected IControllerAction getController(String deviceID, String componentID) {
		if (deviceID == null) {
			if (componentID != null) {
				return new ControllerAction(getIOReader().getControllerMap().getControllerForComponentOnAllDevices(componentID));
			} else {
				return new ControllerAction(getIOReader().getControllerMap().getControllerForAllComponentsOnAllDevices());
			}
		} else {
			if (componentID != null) {
				return new ControllerAction(getIOReader().getControllerMap().getControllerForComponentOnDevice(deviceID, componentID));
			} else {
				return new ControllerAction(getIOReader().getControllerMap().getControllerForAllComponentsOnDevice(deviceID));
			}
		}
	}
	
	public void setCommand(String actionID, Controls controls) {
		if (controls != null) {
			for (Control control : controls.getControls()) {
				if (control.getDevice() != null || control.getComponent() != null) {
					// TODO make it multi controller setup
					fActionMap.put(actionID, getController(control.getDevice(), control.getComponent()));
				}
			}
		}
	}
	
	public IControllerAction getCommand(String actionID) {
		return fActionMap.get(actionID);
	}
}
