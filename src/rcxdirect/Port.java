package rcxdirect;

public class Port {
	private static String RCXPortName = "";

	private Port() {
	}

	/**
	 * @return the string representing the serial port that
	 * will be used to transfer data to the RCX.
	 */
	public static String getName() {
		return RCXPortName;
	}

	/**
	 * Sets the serial port that will be used to transfer data
	 * to the RCX, i.e. <i>COM1</i>, <i>/dev/ttyS0</i>.
	 * @param aPortName A string representing the serial port.
	 */
	public static void setName(String aPortName) {
		RCXPortName = aPortName;
	}

}
