package rcxtools.share.tvm;

import java.awt.TextArea;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import rcxtools.RCXDownload;
import rcxtools.RCXTool;
import rcxtools.share.gui.ButtonUtil;
import rcxtools.share.gui.DownloadDialog;
import rcxtools.share.gui.StatusBar;

/**
 * Provides methods for lejosc, lejos, lejosrun,
 * lejosfirmdl and autostart.
 */

public class Invoke {
	private static RCXTool owner;
	private static DownloadDialog dlg;
	private static StatusBar status;
	private static ButtonUtil button;
	private static TextArea textBox;
	private boolean killed = false;

	private static String[] envpLink = new String[2];
	private static String[] envpExec = new String[3];
	private static String[] envpComp = new String[2];
	private static String[] envpStart= new String[3];
	private static LeJOSOptions props;
	private static Process proc, link, comp, starter;
	private static DownloadThread downlThread;
	//private SimFrame owner;

	private boolean autostart = false;

	public Invoke(RCXTool pOwner) {
		fetchComponents(pOwner);
	}
	
	public void fetchComponents(RCXTool pOwner) {
		owner = pOwner;
		status = owner.getStatusBar();
		props = owner.getProps();
		button = new ButtonUtil(owner);
		if (owner.getClassName().endsWith("RCXDownload"))
			textBox = ((RCXDownload)owner).getTextBox();
	}

	public void stop() {
		killed = true;
		releaseComponents();
		if (proc != null)
			proc.destroy();
	}
	/*
	public void starte() {
		autostart = props.autostart();
		this.compile();
		this.download();
		if (autostart) this.starteAmpel();
	} */

	public void compile(String pName) throws Exception {
		
		button.disable();
		File aFile = new File(pName);
		//String pTvmComp =
		//	props.lejosBin() + "lejosc "
		//		+ ((props.javaVer().equals("JDK1.4")) ? "-target 1.1 " : "")
		//		+ "\"" + aFile.getCanonicalPath() + "\"";
		
		String pTvmComp =
				props.lejosBin() + File.separatorChar + "lejosc " + 
				((props.javaVer().equals("1.1")) ? "-target 1.1 " : "") +
				((props.javaSrc().equals("1.2")) ? "-source 1.2 " : "") +
				props.quot() + pName + props.quot();
				//props.quot() + aFile.getCanonicalPath() + props.quot();
		// evtl. noch "lejosc -target 1.1 " fuer aeltere SDKs
		envpComp[0] = "PATH=" + props.lejosBin() + File.pathSeparatorChar +
								props.javaBin();
		envpComp[1] = "CLASSPATH=" + aFile.getParent() + File.separatorChar;

		try {
			System.out.println("Compiling: lejosc " +
				((props.javaVer().equals("1.1")) ? "-target 1.1 " : "") +
				((props.javaSrc().equals("1.2")) ? "-source 1.2 " : "") +
				aFile.getName() + " ...");
			status.setText("Compiling " + aFile.getName());
			
			comp = Runtime.getRuntime().exec(pTvmComp, envpComp);
			
			downlThread = new DownloadThread(comp, owner, pName, false);
			downlThread.start();

			int returnVal = downlThread.hasFinished(); //p.waitFor();

			downlThread.stop();

			if (returnVal != 0) {

				StringBuffer errBuff = downlThread.getErrBuff();
				if (errBuff.length() > 0 &&
					!errBuff.toString().startsWith(pName))
					printSettings(pTvmComp, envpComp);
				throw new Exception(errBuff.toString());
			}
		} catch (IOException e) {
			releaseComponents();
			//if (e.getMessage().length() > 0) printSettings(pTvmComp, envpComp);
			throw new Exception(e.toString());
		} catch (InterruptedException e) {
			releaseComponents();
			throw new Exception(e.toString());
		} finally {
			releaseComponents();
		}
		/*
		} catch (Exception e) {
			System.out.println("---------------------------------------------------");
			System.out.println(e.getMessage());
			if (e.getMessage().toString().indexOf("error") >=0) {
				Vector errInt = downlThread.getErrInterpret();
				for (Enumeration en = errInt.elements(); en.hasMoreElements(); ) {
					System.out.println((String)en.nextElement().toString());
				}
				System.exit(0);
			}
		} */
	}

	public void download(String pName) throws Exception {
		
		button.disable();

		File aFile = new File(pName);

		String shortFileName =
			aFile.getName().substring(0, aFile.getName().lastIndexOf("."));
		String dFileName = pName.substring(0, pName.lastIndexOf("."));


		File cFile = new File(dFileName + ".class");
		if (!cFile.exists()) {
			dlg =
				new DownloadDialog(
					owner,
					"RCX-Downloadmanager",
					"There is no compiled class " + shortFileName + ".class",
					false);
			dlg.setPos();
			dlg.setVisible(true);
			releaseComponents();
			throw new Exception("No Class-File " + dFileName + ".class");
		}
		//File binFile = new File(dFileName + ".bin");
		//boolean binFileDeleted = false;

		String pTvmLink = props.lejosBin()  + File.separatorChar +
				"lejos " + shortFileName + 
				" -o " + props.quot() + dFileName + ".bin" + props.quot();

		envpLink[0] = "CLASSPATH=" + aFile.getParent();
		envpLink[1] = "PATH=" + props.javaBin() + File.pathSeparator +
								props.lejosBin();

		String pTvmExec = props.lejosBin() + File.separatorChar + "lejosrun " + 
			((props.lejosRun().equals("fast")) ? "-f " : "") +
			props.quot() + dFileName + ".bin" + props.quot();
		
		envpExec[0] = "RCXTTY=" + props.rcxTTY();
		envpExec[1] = "CLASSPATH=" + aFile.getParent();
		envpExec[2] = "PATH=" + props.javaBin() + File.pathSeparator +
								props.lejosBin();

		System.out.println("Environment variable settings - Linker: ");
		for (int i = 0; i < envpLink.length; i++)
			System.out.println("   " + envpLink[i]);
		System.out.println("Environment variable settings - Download: ");
		for (int i = 0; i < envpExec.length; i++)
			System.out.println("   " + envpExec[i]);
		
		/**********************************************************
		 * Linking                                                *
		 **********************************************************/
		try {
			File tmp = new File(dFileName + ".bin");
			tmp.deleteOnExit();
			
			System.out.println("Linking    : lejos " + shortFileName + " -o " + 
				shortFileName + ".bin ...");
			
			link = Runtime.getRuntime().exec(pTvmLink, envpLink);
			
			try {
				int linkStatus = link.waitFor();

				if (!killed) {
					if (linkStatus == 0) {
						status.setText("Finished linking.");
						status.stop();
						//System.out.println("Finished linking.");
					} else {
						System.out.println("Linking failed.");

						String errLink = readErrors(true, link);
						if (errLink.length() <= 0)
							errLink = readErrors(false, link);
						dlg = new DownloadDialog(
								owner,
								owner.getClassName().endsWith("RCXDownload")?
								"RCX-Downloadmanager":"RCX-Direct-Mode",
								errLink, false);
						dlg.setPos();
						dlg.setVisible(true);
						autostart = false;
						throw new Exception(errLink.toString());
					}
				} else {
					status.setText("Download interrupted.");
					autostart = false;
				}
				status.stop();

			} // -> try
			catch (Exception e) {
				System.out.println("Linker: " + e.getMessage());
				System.out.println("-------------------------------------------");
				autostart = false;
				throw new Exception(e.toString());
			}
		} catch (Exception e) {
			releaseComponents();
			//System.out.println("Linker2: " + e.toString());
			autostart = false;
			throw new Exception(e.toString());
			//System.exit(0);
		}
	
		/**********************************************************
		 * Download                                               *
		 **********************************************************/
		try {
			System.out.println(
				"Downloading: lejosrun "+
				((props.lejosRun().equals("fast")) ? "-f " : "") +
				shortFileName+".bin ...");

			proc = Runtime.getRuntime().exec(pTvmExec, envpExec);

			try {
				downlThread =
					new DownloadThread(proc, owner, shortFileName, true);
				downlThread.start();

				int returnVal = downlThread.hasFinished(); //p.waitFor();

				downlThread.stop();

				if (returnVal != 0) {

					StringBuffer errBuff = downlThread.getErrBuff();
					dlg = new DownloadDialog(
							owner,
							owner.getClassName().endsWith("RCXDownload")?
							"RCX-Downloadmanager":"RCX-Direct-Mode",
							errBuff.toString(), false);
					dlg.setPos();
					dlg.setVisible(true);
					autostart = false;
					throw new Exception(errBuff.toString());
				} else {
					if (owner.getClassName().endsWith("RCXDownload")) {
						textBox.append("Program succesfully downloaded.");
						textBox.append("\n");
					}
				}
			} // -> try
			catch (IOException e) {
				System.out.println("IOException: " + e.toString());
				autostart = false;
				throw new Exception(e.toString());
			} catch (InterruptedException e) {
				status.setText("Download was interrupted.");
				autostart = false;
				System.out.println("Download was interrupted.");
				System.out.println("InterruptedException: " + e.toString());
			} finally {
				releaseComponents();
				status.stop();
			}
		} // -> try
		catch (Exception e) {
			//System.out.println("Download2 " + e.getMessage());
			releaseComponents();
			autostart = false;
			throw new Exception(e.toString());
		}
		if (autostart) startProgram();
		releaseComponents();
	}

	public void downloadFirmware() throws Exception {

		button.disable(); //(true);
		
		String[] envpExec = { "RCXTTY=" + props.rcxTTY() };
		System.out.println("environment variable settings - Firmware: ");
		for (int i = 0; i < envpExec.length; i++)
			System.out.println("   " + envpExec[i]);
			
		String pTvmExec =
			props.lejosBin() + File.separatorChar
				+ "lejosfirmdl"
				+ ((props.lejosFirmdl().equals("fast")) ? " -f" : "");

		try {
			System.out.println("Downloading firmware (" + pTvmExec + ") ...");
			//status.setText("Downloading firmware ...");
			proc = Runtime.getRuntime().exec(pTvmExec, envpExec); //(pParams);

			try {

				downlThread =
					new DownloadThread(proc, owner, "firmware", true);
					//new DownloadThread_ALT(proc, status, progressDlg, "firmware");
				//rcxFirmWThread = new RCXDownloadThread(proc, status, "firmware");
				downlThread.start();

				int returnVal = downlThread.hasFinished(); //p.waitFor();

				downlThread.stop();

				if (returnVal != 0) {

					StringBuffer errBuff = downlThread.getErrBuff();
					//System.out.println("RCXInvokeTVM: "+ errBuff.toString());

					//String errFirmware = readErrors(false, proc);
					//if (errFirmware.length() <= 0)
					//   errFirmware = readErrors(true, proc);
					dlg =
						new DownloadDialog(owner, "RCX-Downloadmanager",
							errBuff.toString(), false);
					dlg.setPos();
					dlg.setVisible(true);
					throw new Exception(errBuff.toString());
				}

			} // -> try
			catch (IOException e) {
				System.out.println("IOException: " + e.toString());
				throw new Exception(e.toString());
			} catch (InterruptedException e) {
				status.setText("Firmware-Download was interrupted.");
				System.out.println("Firmware-Download was interrupted.");
				System.out.println("InterruptedException: " + e.toString());
			} finally {
				button.enable();
				status.stop();
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			releaseComponents();
			status.stop();
			throw new Exception(e.toString());
		}
	}

	public void startProgram() {

		//byte[] receivedMessage = null;
		StringBuffer isBuff = null;
		try {
			Thread.sleep(500);
		}
		catch (InterruptedException ie) { }

		envpStart[0] = "PATH=" + props.jreBin() +
						File.pathSeparatorChar + props.lejosBin();
		envpStart[1] = "CLASSPATH=" + props.rcxToolsJar() + 
						File.pathSeparatorChar + props.lejosRcxComm();
		envpStart[2] = "LD_LIBRARY_PATH=" + props.lejosBin();
	
		//byte [] startsequence = {(byte) 0xd2,0x02,0x00};
		String pTvmStart = "java rcxdirect.TowerOperation " +
			props.rcxTTY() + " 1 d2 02 00";

		try {
			System.out.println("----------------------------------------");
			System.out.println("Sending start signal: d2 02 00 ...");
			
			//System.out.println(envpStart[0]);
			//System.out.println(envpStart[1]);
			//System.out.println(pTvmStart);

			starter = Runtime.getRuntime().exec(pTvmStart, envpStart);

			//int returnVal = starter.waitFor();
			starter.waitFor();
			//if (returnVal != 0) {    // fprintf(stderr,...) also Fehlermeldung

			isBuff = getStream(starter.getInputStream());

			throw new Exception(isBuff.toString());

		} catch (Exception e) {
			/*
			int startStatus = starter.waitFor();

			if (startStatus == 0) {

				status.setText("Program succesfully started.");
				status.stop();
			}
			else {
				dlg = new DownloadDialog(owner,
					owner.getClassName().endsWith("RCXDownload")?
					"RCX-Downloadmanager":"RCX-Direct-Mode",
					"I can't start the program.\n"+
					"Please press the \"RUN\"-Button on your RCX.",
					false);
				dlg.setPos();
				dlg.setVisible(true);
				status.setText(
					"Press \"RUN\" to start direct RCX communication.");
				status.stop();

				
				String errStart = readErrors(true, starter);
				if (errStart.length() <= 0)
					errStart = readErrors(false, starter);
				throw new Exception(errStart.toString());
			}
			*/
			if (isBuff != null && isBuff.toString().length() > 0) {
				String inLine = isBuff.toString();
				if (!inLine.trim().equals("no response from rcx"))
					System.out.println("Tower received: "+inLine.trim());

				//receivedMessage = RCXMath.stringToByteArray(inLine.trim());
				// Kekoa Proudfoot (RCX Opcode Reference):
				// A return value of 0 indicates success,
				// a return value of 1 indicates that there is
				// 		insufficient memory for a task of the
				//		specified size,
				// and a return value of 2 indicates that the
				//		task index was invalid. 
			} else {
				try {
					isBuff = getStream(starter.getErrorStream());
					//System.out.println(isBuff.toString());
					String inLine = isBuff.toString();
					System.out.print(inLine);
					System.out.println("Check the following settings:");
					System.out.println(envpStart[0]);
					System.out.println(envpStart[1]);
					System.out.println("Prompt: "+pTvmStart);
					dlg = new DownloadDialog(owner,
						owner.getClassName().endsWith("RCXDownload")?
						"RCX-Downloadmanager":"RCX-Direct-Mode",
						"I can't start the program.\n"+
						"Please press the \"RUN\"-Button on your RCX.",
						false);
					dlg.setPos();
					dlg.setVisible(true);
					status.setText(
						"Press \"RUN\" to start direct RCX communication.");
					status.stop();

				} catch (IOException ioe) {
					System.out.println("Error occured while sending start signal.");
				}
			}
		} finally {
			
		}
	}

	public void releaseComponents() {
		button.enable();
		//if (progressDlg.isVisible()) progressDlg.hide();
	}
	
	public void setAutostart(boolean pStart) {
		autostart = pStart;
	}

	private String readErrors(boolean getInput, Process pProc) {
	
		BufferedReader err;
		if (getInput)
			err = new BufferedReader(new InputStreamReader(pProc.getInputStream()));
		else
			err = new BufferedReader(new InputStreamReader(pProc.getErrorStream()));
		StringBuffer errBuff = new StringBuffer();
		String line;
		try {
			while ((line = err.readLine()) != null) {
				errBuff.append(line + " ");
			}
		} catch (Exception e) {
			System.out.println("readErrors: " + e);
		} finally {
			return errBuff.toString();
		}
	}
	
	public static StringBuffer getStream(InputStream stream)
		throws IOException {
		BufferedReader is = null;
		is = new BufferedReader(new InputStreamReader(stream));

		StringBuffer isBuff = new StringBuffer();
		String line;
		while ((line = is.readLine()) != null) {
			isBuff.append(line + '\n');
		}
		return isBuff;
	}
	
	private void printSettings(String pExec, String[] pEnv) {

		System.out.println("Check the following settings:");
		for (int i=0; i<pEnv.length; i++)
			System.out.println(envpStart[i]);
		System.out.println("Prompt: "+pExec);
	}
}