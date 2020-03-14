package rcxtools.rcxdirectmode;

import rcxtools.RCXDirectMode;
import rcxtools.share.gui.StatusBar;

/**
 * @author Tim Rinkens
 *
 * Action prototype
 * 
 */
public class Action {

	public static RCXDirectMode owner;
	public static StatusBar status;
	
	public Action() {
	}
	
	public Action(RCXDirectMode pOwner) {
		owner  = pOwner;
		status = owner.getStatusBar();
	}
	
	/*
	public Action(StatusBar pBar, DownloadDialog pDialog) {
		status = pBar;
		dialog = pDialog;
	}
	*/
}
