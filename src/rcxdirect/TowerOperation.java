package rcxdirect;

import josx.rcxcomm.Tower;

import java.util.Date;
/**
 * @author Tim Rinkens
 *
 * TowerOperation is a replacement for send.c
 * in previous versions.
 * 
 */
public class TowerOperation {
	
	private static final int TIMEOUT = 1000;  // max. delay for reply from RCX
	private static Tower tower = new Tower();
	private static TowerObserver observer = new TowerObserver(tower, TIMEOUT);
	
	/**
	 * Constructor
	 */
	public TowerOperation(String rcxTTY, int replyLength, byte[] sendBytes) {
		/*
		byte[] recBytes = new byte[replyLength];
		tower.open(rcxTTY);
		tower.send(sendBytes, sendBytes.length);
		int reply = tower.receive(recBytes);
		
		if (reply > 0)
			System.out.println("" +RCXMath.byteArrayToString(recBytes));
		else
			System.out.println("" + tower.strerror(tower.getError()));
		tower.close();
		*/
		//byte[] recBytes = new byte[replyLength];
		tower.open(rcxTTY);
		tower.send(sendBytes, sendBytes.length);
		observer.start();
		int reply = observer.hasFinished(replyLength);	
	}
	
	public static void main(String[] args) {
		
		//System.out.println("TowerOperation -> main");

		if (args.length > 2) {
			byte[] bytes = new byte[args.length-2];
			for (int i=2; i<args.length;i++)
				bytes[i-2] = RCXMath.toByte(args[i]); 
			
			new TowerOperation(args[0], RCXMath.toInt(args[1]), bytes);
		} else {
			System.out.println(
				"Usage: java rcxdirect.TowerEmploy [usb|COM1..COM4] [reply length] [bytes]");
		}
	}	
}
class TowerObserver implements Runnable {
	
	private static Thread observerThread = null;
	private static long maxDelay;
	private static byte[] recBytes;
	private static int reply;
	private static Tower observedTower;

	/**
	 * Constructor
	 */
	public TowerObserver(Tower pTower, int pDelay) {
		observedTower = pTower;
		maxDelay = pDelay;
	}

	public int hasFinished(int pReplyLength) {
		recBytes = new byte[pReplyLength];
		reply = observedTower.receive(recBytes);
		return reply;
	}
	
	public byte[] getReceivedBytes() {
		return recBytes;
	}

	public void start() {
		reply = -1;
		if (observerThread == null) {
			observerThread = new Thread(this);
			observerThread.start();
			//returnVal = -1;
		}
	}

	public void stop() {
		if (observerThread != null && observerThread.isAlive())
			observerThread.interrupt(); //stop();
		observerThread = null;
	}

	public void run() {
		long timeout = new Date().getTime() + maxDelay;
		while (timeout > new Date().getTime() &&
				reply == -1) {
			Thread.yield();
		}
		stop();
		if (reply >= 0) {
			recBytes  = getReceivedBytes();
			System.out.println("" +RCXMath.byteArrayToString(recBytes));
		}
		else
			System.out.println("no response from rcx");
			//System.out.println("" + observedTower.strerror(observedTower.getError()));
		observedTower.close();	
	}
}