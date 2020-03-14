import rcxdirect.*;

/**
 * The package 'rcxdirect' can be used to test the functionality
 * of sensors and motors. You can also write some programs to
 * control the RCX via PC.
 *
 * Download the program for direct communication to the RCX 
 * (Click 'Download Direct-RCX' in RCXDirectMode).
 * Place RCXDirectTest.java into the same directory as
 * rcxtools.jar and RCXTools.properties.
 * Compile with
 *    javac -classpath rcxtools.jar RCXDirectTest.java
 * Run with
 *    java -classpath .;rcxtools.jar RCXDirectTest
 */

public class RCXDirectTest {
	public static void main(String[] args) {
		Port.setName("usb"); // or "COM1" (Windows), "/dev/ttyS0" (Linux)

		System.out.println("Battery power: " + Battery.getVoltageMilliVolt());

		while (true) {
			System.out.println("----------------------------------------");
			System.out.println(
				"Sensor.S2.readValue = " + Sensor.S2.readValue());
			delay(600);
			Motor.A.forward();
			Motor.C.forward();
			delay(600);
			Motor.A.stop();
			Motor.C.stop();
			Sound.beep();
			Motors.control(
				Motors.BACKWARD,
				Motors.STOP,
				Motors.BACKWARD,
				Motor.A.getPower(),
				Motor.B.getPower(),
				Motor.C.getPower());
			delay(600);
			Motors.control(
				Motors.STOP,
				Motors.STOP,
				Motors.STOP,
				Motor.A.getPower(),
				Motor.B.getPower(),
				Motor.C.getPower());
		}
	}

	public static void delay(int t) {
		try {
			Thread.sleep(t);
		} catch (InterruptedException ie) {
		}
	}
}
