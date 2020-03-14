package rcxtools.share.tvm;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector;

import rcxtools.RCXTool;
import rcxtools.share.gui.ProgressDialog;

/**
 * Keeps a watch on compile and download processes.
 * @see <a href="Invoke.html">Invoke</a>
 */
public class DownloadThread implements Runnable {
	
	private static Thread rcxDownloadThread = null;

	private static StringBuffer errBuff = new StringBuffer();
	private static Process p = null;
	private static int returnVal = -1;
	private static RCXTool owner;
	private static ProgressDialog progrDlg;
	private static boolean isDownload = false;
	private static Vector errInterpet;
	private static String fileName;
	//private boolean frameFokus = true;

	/**
	 * Constructor
	 */
	public DownloadThread(Process pProc, RCXTool pOwner,
						  String pFileName,
						  boolean pIsDownload) {
		p = pProc;
		owner = pOwner;
		fileName = pFileName;
		//progrDlg = pProgrDlg;		
		isDownload = pIsDownload;
		errInterpet = new Vector();
		
		if (isDownload) {
			progrDlg = new ProgressDialog(owner);
			progrDlg.setPos();
			progrDlg.setVisible(true); //.show();
		}
	}

	public StringBuffer getErrBuff() {

		return errBuff;
	}

	public int hasFinished() {

		try {
			returnVal = p.waitFor();
		} catch (InterruptedException errIR) {
			System.out.println(errIR);
		} finally {
			return returnVal;
		}
	}

	public void start() {

		if (rcxDownloadThread == null) {
			rcxDownloadThread = new Thread(this);
			rcxDownloadThread.start();
			returnVal = -1;

		}
	}

	public void stop() {
		if (rcxDownloadThread != null && rcxDownloadThread.isAlive())
			rcxDownloadThread.interrupt(); //stop();

		rcxDownloadThread = null;
	}

	public void run() {
		
		if (isDownload) {
			downloadRun();
		}
		else {
			compileRun();
		}
	}
	
	public void downloadRun() {
		
		BufferedReader errRead =
			new BufferedReader(new InputStreamReader(p.getErrorStream()));

		BufferedReader inRead =
			new BufferedReader(new InputStreamReader(p.getInputStream()));

		errBuff = new StringBuffer();

		String line = "";
		String lineIn = "";

		try {
			// Get 'fprintf' in lejosrun:
			while ((line = errRead.readLine()) != null) {

				if (line.indexOf("%") != -1) {
					owner.getStatusBar().setText("downloading " +
						fileName + " : " + line);
					//status.setText(""+((float)(Float.parseFloat(line.substring(1,line.indexOf("%")))/100.0)));
					progrDlg.update(
						((float) (Float.parseFloat(
							line.substring(1, line.indexOf("%"))) / 100.0)));
					//owner.setzeStatus((float)(Float.parseFloat(line.substring(1,line.indexOf("%")))/100.0));
				} else {
					if (line.length() != 0)
						errBuff.append(line + " ");
				}
				Thread.yield();				
			}
			// Get 'printf' in lejosrun:
			while ((lineIn = inRead.readLine()) != null) {

				errBuff.append(lineIn);
			}
			if (isDownload) progrDlg.setVisible(false); //.hide();

		} catch (Exception e) {
			if (isDownload) progrDlg.setVisible(false); //.hide();
			//System.out.println("RCXDownloadThread: Exception " + e.toString());
		}
	}

	public void compileRun() {
		
		boolean errFound = false;
		BufferedReader errRead =
			new BufferedReader(new InputStreamReader(p.getErrorStream()));

		errBuff = new StringBuffer();
		errFound = false;
		String line;
	
		try {
			while ((line = errRead.readLine()) != null) {
				errBuff.append(line + '\n');
				//if (line.indexOf("package")>=0 && 
				//	line.indexOf("does not exist")>=0) {
				//	errInterpet.addElement("Wrong import suspected.");
				//}
				
				errFound = true;
				//System.out.println("ErrorStream: "+line);
			}
	
		} catch (Exception e) {
	
		}
	}
	
	public Vector getErrInterpret() {
		return errInterpet;
	}
}