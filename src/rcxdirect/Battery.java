package rcxdirect;

/**
 * Provides access to Battery.
 */
public class Battery extends DirectSend {
	
	public Battery() {
	}
	
	/**
	 * @return Battery voltage.
	 */
	public static int getVoltageInternal() {
		String battery_cmd = "30";
		int result = sendToRCX(battery_cmd, "battery");
		return result;
	}

	/**
	 * @return Battery voltage in mV.
	 */
	public static int getVoltageMilliVolt() {
		/*
		 * calculation from LEGO firmware
		 */
		return Battery.getVoltageInternal() * 43988 / 1560;
	}

	/**
	 * @return Battery voltage in Volt.
	 */
	public static float getVoltage() {
		return (float) (Battery.getVoltageMilliVolt() * 0.001);
	}
}
