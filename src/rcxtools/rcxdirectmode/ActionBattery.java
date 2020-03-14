package rcxtools.rcxdirectmode;

import java.awt.event.ActionEvent;

import rcxdirect.Battery;
import rcxtools.RCXDirectMode;
import rcxtools.share.gui.DownloadDialog;


public class ActionBattery extends Action {

	public static DownloadDialog dialog;
	
	public ActionBattery(RCXDirectMode pOwner) {
		super(pOwner);
	}
	
	public void go(ActionEvent evt) {

		status.showPacket("battery packet");
		int value = Battery.getVoltageInternal();
		status.showPacket("battery packet", Battery.getTimeoutStatic(),
			((value >= 0) ? true : false));
		if (value >= 0) {
			float milliVolt = (float) (value * 43988.0 / 1560.0);

			dialog = new DownloadDialog(owner, "RCX-DirectMode",
					"Battery power: " + value + " (= " + milliVolt + " mV)", false);
			dialog.setPos();
			dialog.setVisible(true);
		}
	}

}