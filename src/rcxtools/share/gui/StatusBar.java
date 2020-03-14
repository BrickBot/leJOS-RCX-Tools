package rcxtools.share.gui;

import java.awt.Label;

import rcxtools.share.tvm.StatusThread;

/**
 * Reports status messages for a few seconds.
 * @see <a href="../tvm/StatusThread.html">rcxtools.share.tvm.StatusThread</a>
 */
public class StatusBar {

	private static StatusThread statusThread = null;
	private static Label statusLabel;
	//private static RCXProgressDialog progressDlg;

	public StatusBar(Label pLabel) {
		statusLabel = pLabel;
	}
	/*
	public StatusBar(RCXDirectMode owner) {
		this.statusLabel = owner.statusLabel;
		//this.progressDlg = owner.progressDlg;
	}
	*/

	public void showPacket(String msg) {

		if (statusThread != null)
			statusThread.stop();
		//statusLabel.setBackground(new java.awt.Color(255,50,50));
		statusLabel.setText(" Status: sending " + msg + " ...");

	}
	public void showPacket(String msg, double timeout, boolean result) {

		statusLabel.setText(" Status: receiving " + msg + " " 
			+ ((result) ? "at " : "in ") + new Double(timeout).intValue() + " ms" + ": "
			+ ((result) ? "ok" : "failed"));
		//statusLabel.setBackground(RCXColors.buttonColor); //(new java.awt.Color(255,0,0));
		//statusLabel.notify();

		statusThread = new StatusThread(statusLabel);
		statusThread.start(4000);
		//statusLabel.setBackground(RCXColors.lbColor1); //(new java.awt.Color(250,227,51));
	}

	public synchronized void setText(String msg) {

		if (statusThread != null) statusThread.stop();
		statusLabel.setText(" Status: " + msg);
		//statusThread = new RCXStatusThread(statusLabel);
		//statusThread.start(3000);
	}
	/*
	public synchronized void setProgress(float percentage) {
	
	   progressDlg.update(percentage);
	   progressDlg.repaint();
	}
	*/
	public void stop() {

		statusThread = new StatusThread(statusLabel);
		statusThread.start(4000);
	}
}