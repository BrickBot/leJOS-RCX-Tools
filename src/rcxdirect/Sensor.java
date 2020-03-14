package rcxdirect;

/**
 * Abstraction for a sensor. There are three Sensor instances
 * available: Sensor.S1, Sensor.S2 and Sensor.S3. They correspond
 * to sensor inputs labeled 1, 2 and 3 in the RCX.
 */

public class Sensor extends DirectSend {

	private int iSensorId;

	/**
	 * Sensor labeled 1 on RCX.
	 */
	public static final Sensor S1 = new Sensor(0);
	/**
	 * Sensor labeled 1 on RCX.
	 */
	public static final Sensor S2 = new Sensor(1);
	/**
	 * Sensor labeled 1 on RCX.
	 */
	public static final Sensor S3 = new Sensor(2);

	/**
	 * Reads the canonical value of the sensor.
	 */
	public final int readValue() {
		return readSensorValue(iSensorId, 3);
	}

	/**
	 * Reads the raw value of the sensor.
	 */
	public final int readRawValue() {
		//System.out.println("RCXDirectSensor: readRawValue - aId = "+iSensorId);
		return readSensorValue(iSensorId, 0);
	}

	/**
	 * Reads the boolean value of the sensor.
	 */
	public final boolean readBooleanValue() {
		int result = readSensorValue(iSensorId, 1);
		return (result > 0);
	}

	private Sensor(int aId) {
		iSensorId = aId;
	}

	private int readSensorValue(int s, int state) {
		// 0x00 = RAW, 0x20 = BOOL, 0x80 = PERCENT.
		String sensor_cmd = "32 0" + s + " 0" + state;
		int result = sendToRCX(sensor_cmd, "sensor");

		//System.out.println("readSensorValue: " + result + ".");
		//return lastSensorResult();
		return result;
	}
}