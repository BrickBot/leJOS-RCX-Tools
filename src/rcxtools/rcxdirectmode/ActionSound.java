package rcxtools.rcxdirectmode;

import java.awt.event.ActionEvent;

import rcxdirect.Sound;
import rcxtools.RCXDirectMode;


public class ActionSound extends Action {

	public ActionSound(RCXDirectMode pOwner) {
		super(pOwner);
	}
	
	public void go(ActionEvent evt) {

		String cmd = evt.getActionCommand();

		status.showPacket("sound packet");
		Sound.systemSound(Integer.parseInt(cmd));
		boolean success = Sound.lastResultStatic();
		status.showPacket("sound packet", Sound.getTimeoutStatic(), success);
	}

}