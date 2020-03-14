package rcxdirect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import rcxtools.share.tvm.LeJOSOptions;


public class DirectSend {

	private static Process sendProc;
	private static final String optFileName = "RCXTools.properties";
	private static String userDir = System.getProperty("user.dir");
	//private static String javaBin = "";
	private static String jreBin =
		System.getProperty("java.library.path");
	
	private static final String rcxtoolsJar  = LeJOSOptions.rcxToolsJar;

	private static String lejosPath		= "";
	private static String lejosBin		= "";
	private static String lejosRcxComm	= "";
	private static String[] envpSend	= new String[3];

	private static Properties optionProps = new Properties();
	
	private static final String OSName =
		System.getProperties().getProperty("os.name").toUpperCase();
	/*
	// Linux OS
	private static final int UNIX		= 0;
	// Windows OS
	private static final int WINDOWS	= 1;
	// Mac OS
	private static final int MAC		= 2;
	private static final int OS =
		(OSName.indexOf("WINDOW") >= 0)?	WINDOWS : 
		((OSName.indexOf("MAC") >= 0)  ? MAC : UNIX);
	*/
	private static StringBuffer isBuff		= null;
	private static byte[] receivedMessage	= null;
	//private static final byte[] expectedHeader	= { 85, -1, 0 }; // bytes[55 FF 00]
	//private static byte[] receivedBody		= null;

	private static int success = -1;
	private static final int MAX = 99;
	private static double currTimeout = 0.0;
	
	private static boolean initialized = false;
	
	public static void init() {

		//StringBuffer isBuff = null;
		receivedMessage = null;
		//expectedHeader = null;
		
		try {
			File opt = new File(userDir+File.separatorChar+optFileName);
			FileInputStream optLoader = new FileInputStream(opt);
			optionProps.load(optLoader);
			lejosPath = optionProps.getProperty("lejosPath");
			lejosBin = lejosPath + File.separatorChar + "bin";
		} catch (FileNotFoundException e) {
			System.out.println("Can't find property file "+optFileName);
			//e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error while reading property file "+optFileName);
			//e.printStackTrace();
		}
		
		//if (LeJOSOptions.OS == LeJOSOptions.UNIX) {
		//	envpSend = new String[3];
		//	envpSend[2] = "LD_LIBRARY_PATH=" + lejosBin;
		//}
		System.out.println("jreBin: " + jreBin);
		System.out.println("lejosBin: " + lejosBin);
		System.out.println("rcxtoolsJar: " + rcxtoolsJar);
		
		lejosRcxComm = lejosPath + File.separatorChar + "lib" + File.separatorChar +
			"pcrcxcomm.jar";
		envpSend[0] = "PATH=" + jreBin + 
						File.pathSeparatorChar + lejosBin;
		
		envpSend[1] = "CLASSPATH=" + rcxtoolsJar + 
						//File.pathSeparatorChar + lejosPath + File.separatorChar +
						//	"lib" + File.separatorChar + "jtools.jar" +
						File.pathSeparatorChar + lejosRcxComm;
		envpSend[2] = "LD_LIBRARY_PATH=" + lejosBin;
		
		initialized = true;
	}
	
	public static int sendToRCX(String pCmd, String pState) {
		
		if (!initialized) init();
		
		long t0, t1; // Zeit (ms)
		Date d;
		currTimeout = 0.0;
		d = new Date();
		t0 = d.getTime();
		isBuff = null;
		byte[] receivedMessage = null;
		
		String pTvmSend = "java rcxdirect.TowerOperation " +
			Port.getName() +
			((pState.equals("sensor") || pState.equals("battery"))?" 3 ":" 1 ") +
			pCmd;
			//((pState.equals("sensor")||pState.equals("battery"))?3:1);

		//System.out.println(envpSend[0]);
		//System.out.println(envpSend[1]);
		//System.out.println(pTvmSend);

		Process p = null;

		try {
			System.out.println("----------------------------------------");
			System.out.println("Sending to RCX: " + pCmd + " ...");
			
			sendProc = Runtime.getRuntime().exec(pTvmSend, envpSend);

			//int returnVal = p.waitFor();
			sendProc.waitFor();
			//if (returnVal != 0) {    // fprintf(stderr,...) also Fehlermeldung

			isBuff = getStream(sendProc.getInputStream());

			throw new Exception(isBuff.toString());

		} catch (Exception e) {
			
			if (isBuff != null && isBuff.toString().length() > 0) {
				String inLine = isBuff.toString();
				System.out.println("Tower received: "+inLine.trim());

				receivedMessage = RCXMath.stringToByteArray(inLine.trim());
				/*
				receivedBody = searchBody(receivedMessage);
				
				System.out.println((receivedBody==null)?
					"Invalid response.":
					"RCX sent back: "
					.concat("(")
					.concat(RCXMath.byteArrayToString(expectedHeader))
					.concat(") ")
					.concat(RCXMath.byteArrayToString(receivedBody)));
						//+ inLine.substring(8, inLine.length() - 2));
				*/
			} else {
				try {
					isBuff = getStream(sendProc.getErrorStream());
					//System.out.println(isBuff.toString());
					String inLine = isBuff.toString();
					System.out.print(inLine);
					System.out.println("Check the following settings:");
					for (int i=0;i<envpSend.length;i++)
						System.out.println(envpSend[i]);
					System.out.println("Prompt: "+pTvmSend);

				} catch (IOException ioe) {
					System.out.println("Error occured while sending.");
				}
			}
		} finally {
			
			d = new Date();
			t1 = d.getTime();
			currTimeout = t1 - t0;
			success = compareResult(receivedMessage, pCmd, pState);
			
		}

		return success;
	}
	/*
	public static byte[] searchBody(byte[] pMessage) {
		
		//System.out.println(RCXMath.byteArrayToString(pMessage));
		//pMessage = RCXMath.stringToByteArray("55 ff 00 ae 51 ae 51 00 00 00");
		byte[] body = new byte[pMessage.length-3];
		
		int i=0;
		while ( i < pMessage.length &&
				!(pMessage[i]					  == expectedHeader[0] &&
				  pMessage[(i+1)%pMessage.length] == expectedHeader[1] &&
				  pMessage[(i+2)%pMessage.length] == expectedHeader[2]))
			i++;
	
		if (i == pMessage.length) return null;
		//System.out.println("Header starts at "+i);
		for (int k=0; k<pMessage.length-3;k++) {
			body[k] = pMessage[(i+3+k)%pMessage.length];
			//System.out.println((i+3+k)%pMessage.length);
		}
		return body;
	}
	*/
	private static int compareResult(byte[] aReply, String aCMD, String aSt) {
		int succ = -1;

		if ((aReply != null)
			&& (aReply[0] == ~(RCXMath.toByte(aCMD.substring(0, 2))))) {
			// check header byte

			succ = 0;

			if (aSt.equals("sensor") || aSt.equals("battery")) {
				succ += Integer.valueOf(aReply[1] + "").intValue();

				if (succ < 0) {
					System.out.println(
						"DirectConnect: " + aSt + " packet: invalid number");
					succ = -1;

				} else {
					succ += Integer.valueOf(aReply[2] + "").intValue() * MAX;
					System.out.println(
						"DirectConnect: " + aSt + " value: " + succ);
				}
			}

		}
		System.out.println("DirectConnect: "
				+ aSt + " packet: " + ((succ >= 0) ? "ok" : "failed"));
		return succ;
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

	public double getTimeout() {
		return currTimeout;
	}
	
	public static double getTimeoutStatic() {
		return currTimeout;
	}

	public boolean lastResult() {
		return (success >= 0);
	}

	public static boolean lastResultStatic() {
		return (success >= 0);
	}
}