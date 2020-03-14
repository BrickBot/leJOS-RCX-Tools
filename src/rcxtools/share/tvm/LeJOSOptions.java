package rcxtools.share.tvm;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Environment variable settings for compile, download and 
 * autostart processes.
 */
public class LeJOSOptions {

	/**
	 * DEVELOPER=
	 * true:  class files are assumed to be in the "src/bin" directory.
	 * false: class files are assumed to be in the "rcxtools.jar" archive.
	 * 
	 * For both, RCXReceiver is assumed to be in the "RCXTools/rcxdirect"
	 * directory.
	 */
	private static final	boolean DEVELOPER	= false;	// development or final release
	
	private static Properties optionProps	= new Properties();
	private static final	String userDir		= System.getProperties().getProperty("user.dir");
	private static final	String optFileName	= "RCXTools.properties";
	public  static final	String rcxToolsJar =
		(DEVELOPER)?
			(userDir + File.separatorChar + "bin") :
			(userDir + File.separatorChar + "rcxtools.jar");
	public 	static final	String rcxReceiver =
		((DEVELOPER)?
			(userDir.substring(0,userDir.lastIndexOf(File.separatorChar))
				+ File.separatorChar + "RCXTools") :
			(userDir))
			+ File.separatorChar + "rcxdirect"
			+ File.separatorChar + "RCXReceiver.java";

	private static boolean	optFileExists	= true;
	private static String	javaPath		= "",
							javaVer			= "",
							javaSrc			= "";
	private static String	lejosPath		= "",
							lejosRun		= "",
							lejosFirmdl		= "";
	private static String	rcxTTY			= "";
	private static String	Colors			= "";
	private static String	splashscr		= "";

	/*
	private static String[]	envpLink = new String[2];
	private static String[]	envpExec = new String[3];
	private static String[]	envpComp = new String[2];
	private static String[]	envpStart= new String[2];
	*/
	private static boolean	autostart= false;
	private static final String	OSName =
		System.getProperties().getProperty("os.name").toUpperCase();
	// Linux OS
	public static final int UNIX		= 0;
	// Windows OS
	public static final int WINDOWS	= 1;
	// Mac OS
	public static final int MAC		= 2;
	
	public static final int OS =
		(OSName.indexOf("WINDOW") >= 0)? WINDOWS : 
		((OSName.indexOf("MAC") >= 0)  ? MAC : UNIX);

	//public LeJOSOptions() {
	//	load();
	//}

	public void load() {
		System.out.println(userDir.substring(0,userDir.lastIndexOf(File.separatorChar)) );
		/*
		String s = System.getProperties().getProperty("os.name").toUpperCase();
		if (s.indexOf("WINDOW") >= 0) {
			OS = WINDOWS;
		} else if (s.indexOf("MAC") >= 0) {
			OS = MAC;
		} */
		
		try {

			FileInputStream optLoader = new FileInputStream(
				userDir + File.separatorChar + optFileName);

			optionProps.load(optLoader);
			lejosPath	= optionProps.getProperty("lejosPath");
			javaPath	= optionProps.getProperty("javaHome");
			javaVer		= optionProps.getProperty("jcTarget");
			javaSrc		= optionProps.getProperty("jcSource");
			//leJOS_C	= optionProps.getProperty("leJOSComp");
			rcxTTY= optionProps.getProperty("RCXTTY");
			lejosRun	= optionProps.getProperty("lejosrun");
			lejosFirmdl	= optionProps.getProperty("lejosfirmdl");
			Colors		= optionProps.getProperty("Colors");
			splashscr	= optionProps.getProperty("splashscreen");

		} catch (FileNotFoundException e) {
			System.out.println("File " + optFileName + " not found.");
			optFileExists = false;
		} catch (IOException e) {
			System.out.println("Error occured while reading " + optFileName);
		}
	}
	
	public boolean loaded() {

		return !(   (lejosPath()	== "")
				 || (javaPath()		== "")
				 || (javaVer()		== "")
				 || (javaSrc()		== "")
				 || (rcxTTY()		== "")
				 || (Colors()		== "") );
	}

	public static void write(
		String env_leJOS, String env_Java, String env_JavaVer, String env_JavaSrc,
		String env_RCXTTY, String env_leJRun, String env_leJFirm,
		String env_Colors) {

		File outFile = new File(userDir
			+ File.separatorChar + optFileName);
		System.out.println("File \"" + optFileName + "\" " + ((outFile.exists())
					? "exists."	: "doesn't exist. I'll create it."));

		try {

			FileOutputStream optionSaver = new FileOutputStream(
				userDir + File.separatorChar + optFileName);
			System.out.println("Save Property-File: "+
				userDir + File.separatorChar + optFileName);

			optionProps.put("lejosPath", trimPath(env_leJOS));
			optionProps.put("javaHome", trimPath(env_Java));
			optionProps.put("jcTarget", env_JavaVer);
			optionProps.put("jcSource", env_JavaSrc);
			//optionProps.put("leJOSComp", env_leJOSC);
			optionProps.put("RCXTTY", env_RCXTTY);
			optionProps.put("lejosrun", env_leJRun);
			optionProps.put("lejosfirmdl", env_leJFirm);
			optionProps.put("Colors", env_Colors);
			/*
			if (env_leJOSC.equals("lejosc1"))
			  optionProps.save(optionSaver, "RCXTools Options"); // Deprecated!
			else */
			javaPath		= trimPath(env_Java);
			javaVer			= env_JavaVer;
			javaSrc			= env_JavaSrc;
			lejosPath		= trimPath(env_leJOS);
			lejosRun		= env_leJRun;
			lejosFirmdl		= env_leJFirm;
			rcxTTY			= env_RCXTTY;
			Colors			= env_Colors;
			optionProps.store(optionSaver, "RCXTools Options");

		} catch (IOException e) {
			System.out.println(
				"Error occured while creating \"" + optFileName + "\".");
		}
	}
	
	public void writeSplashScr() {

		try {

			FileOutputStream optionSaver = new FileOutputStream(
				userDir + File.separatorChar + optFileName);
			System.out.println("Save Property-File: "+
				userDir	+ File.separatorChar + optFileName);

			optionProps.put("splashscreen", "false");
			optionProps.store(optionSaver, "RCXTools Options");

		} catch (IOException e) {
			System.out.println(
				"Error occured while writing splashscreen state to \"" + 
					optFileName + "\".");
		}
	}

	public String lejosPath() {

		if ((lejosPath == null) || lejosPath.equals(""))
			System.out.println("\"" + optFileName + "\": lejosPath not found.");

		return ((lejosPath == null) ? "" : lejosPath);
	}

	public String lejosBin() {
		return lejosPath().concat(File.separatorChar + "bin");
	}
	
	public String lejosClasses() {
		return lejosPath() + File.separatorChar + "lib" +
				File.separatorChar + "classes.jar";
	}

	public String lejosRcxComm() {
		return lejosPath() + File.separatorChar + "lib" +
				File.separatorChar + "pcrcxcomm.jar";
	}
	
	public String lejosRun() {

		if ((lejosRun == null) || lejosRun.equals(""))
			lejosRun = "slow";

		return lejosRun;
	}
	
	public String lejosFirmdl() {

		if ((lejosFirmdl == null) || lejosFirmdl.equals(""))
			lejosFirmdl = "slow";

		return lejosFirmdl;
	}

	public String javaPath() {

		if ((javaPath == null) || javaPath.equals(""))
			System.out.println("\"" + optFileName + "\": javaHome not found.");

		return ((javaPath == null) ? "" : javaPath);
	}
	
	public String javaBin() {
		return javaPath + File.separatorChar + "bin";
	}
	
	public String jreBin() {
		//System.out.println("getting java.library.path");
		return System.getProperty("java.library.path");
	}

	public String javaVer() {

		if ((javaVer == null) || javaVer.equals(""))
			System.out.println(
				"\"" + optFileName + "\": jcTarget not found.");

		return ((javaVer == null) ? "" : javaVer);
	}
	
	public String javaSrc() {

		if ((javaSrc == null) || javaSrc.equals(""))
			System.out.println(
				"\"" + optFileName + "\": jcSource not found.");

		return ((javaSrc == null) ? "" : javaSrc);
	}

	public String rcxTTY() {

		if ((rcxTTY == null) || rcxTTY.equals("")) 
			System.out.println("\"" + optFileName + "\": RCXTTY not found.");

		return ((rcxTTY == null) ? "" : rcxTTY);
	}

	public boolean splashscr() {

		//if ((splashscr == null) || splashscr.equals("")) 
		//	System.out.println("\"" + optFileName + "\": Splashscreen state not found.");

		return (splashscr == null || !splashscr.equals("false"));
	}
	
	public String Colors() {

		if ((Colors == null) || Colors.equals(""))
			Colors = "0";

		return Colors;
	}
	
	public String userDir() {
		return userDir;
	}
	
	public final String rcxToolsJar() {
		return rcxToolsJar;
	}

	/*
	public String[] envpLink() {
		return envpLink;
	}
	public String[] envpExec() {
		return envpExec;
	}
	public String[] envpComp() {
		return envpComp;
	}
	public String[] envpStart() {
		return envpStart;
	} */
	
	public boolean autostart() {
		return autostart;
	}
	public void setAutostart(boolean pAuto) {
		autostart = pAuto;
	}
	
	public int getOS() {
		return OS;
	}
	
	public String quot() {
		return (OS == UNIX)?"":"\"";
	}

	public boolean fileExists() {

		File optFile = new File(userDir()
			+ File.separatorChar + optFileName);
		//if (!optFile.exists()) {
		//	System.out.println("File \"" + optFileName + "\" does not exist.");

		return optFile.exists();

	}
	public static String trimPath(String path) {
		if (path != null) {
			if ((!path.equals("")) &&
				((path.charAt(path.length() - 1) + "").equals(File.separator + "")))
				//System.out.println("bingo 2");
				path = path.substring(0, path.length() - 1);
		}
		return (path != null) ? path : "";
	}
}