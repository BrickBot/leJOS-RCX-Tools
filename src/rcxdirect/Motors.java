package rcxdirect;

/**
 * Abstraction for motors. To synchronize
 * all motors use method <code>controlMotors</code>.
 * To set each motor's power, use
 * <p>
 * Example:<p>
 * <code><pre>
 *   Motors.control(1, 3, 1, 7, 7, 7);
 * </pre></code>
 */
public class Motors extends DirectSend {

	public static final int FORWARD		= 1;
	public static final int BACKWARD	= 2;
	public static final int STOP		= 3;
	public static final int FLOAT		= 4;

	private Motors() {
	}

	/**
	 * Sets motor direction for each motor to a <i>value between 1 and 4</i>
	 * (1=forward, 2=backward, 3=stop, 4=float)
	 * and motor power to a <i>value between 0 and 7</i>
	 * @param aMode mode 1=forward, 2=backward, 3=stop, 4=float
	 * @param bMode mode 1=forward, 2=backward, 3=stop, 4=float
	 * @param cMode mode 1=forward, 2=backward, 3=stop, 4=float
	 * @param aPower power value in the range [0-7].
	 * @param bPower power value in the range [0-7].
	 * @param cPower power value in the range [0-7].
	 */
	public static void control(
		int aMode,
		int bMode,
		int cMode,
		int aPower,
		int bPower,
		int cPower) {
		String cntrl_cmd = "05 " + aMode + "" + aPower
						 + " "   + bMode + "" + bPower
						 + " "   + cMode + "" + cPower + " 00 00";

		int result = sendToRCX(cntrl_cmd, "motors");
	}

}
