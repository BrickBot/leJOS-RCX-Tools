package rcxtools.share.tvm;

import java.awt.Label;

/**
 * StatusThread is used to delete status messages after
 * a specified number of milliseconds. 
 */

public class StatusThread implements Runnable {
	
	private Thread statusThread = null;

	private static Label statusLabel;
	private int millisec;

	public StatusThread(Label pLabel) {
		statusLabel = pLabel;
	}

	public void start(int ms) {

		this.millisec = ms;
		if (statusThread == null) {
			statusThread = new Thread(this);
			statusThread.start();
		}
	}

	public void stop() {
		if (statusThread != null && statusThread.isAlive())
			statusThread.interrupt(); //stop();
		statusThread = null;
	}

	public void run() {
		while (Thread.currentThread() == statusThread) {

			try {
				//statusThread.sleep(millisec);
				Thread.sleep(millisec);
				statusLabel.setText(" Status:");
			} catch (InterruptedException e) {
			}
		}
	}
}