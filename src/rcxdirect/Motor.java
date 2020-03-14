package rcxdirect;

/**
 * Abstraction for a motor. Three instances of <code>Motor</code>
 * are available: <code>Motor.A</code>, <code>Motor.B</code>
 * and <code>Motor.C</code>. To control each motor use
 * methods <code>forward, backward, reverseDirection, stop</code>
 * and <code>flt</code>. To set each motor's power, use
 * <code>setPower</code>.
 * <p>
 * Example:<p>
 * <code><pre>
 *   Motor.A.setPower(1);
 *   Motor.C.setPower(7);
 *   Motor.A.forward();
 *   Motor.C.forward();
 *   Thread.sleep (1000);
 *   Motor.A.stop();
 *   Motor.C.stop();
 * </pre></code>
 */
public class Motor extends DirectSend {
	private int iId;
	private short iMode = 4;
	private short iPower = 3;

	/**
	 * Motor A.
	 */
	public static final Motor A = new Motor(0);
	/**
	 * Motor B.
	 */
	public static final Motor B = new Motor(1);
	/**
	 * Motor C.
	 */
	public static final Motor C = new Motor(2);

	public Motor(int aId) {
		iId = aId;
	}

	/**
	 * Sets motor power to a <i>value between 0 and 7</i>.
	 * @param aPower A value in the range [0-7].
	 */
	public final void setPower(int aPower) {
		iPower = (short) aPower;
		controlMotorPower(iId, aPower);
		//controlMotors (1, 2, 1, aPower, aPower, aPower);
	}
	/**
	 * Returns the current motor power.
	 */
	public final int getPower() {
		return iPower;
	}

	/**
	 * Causes motor to rotate forward.
	 */
	public final void forward() {
		iMode = 1;
		controlMotor(iId, 1);
	}

	/**
	 * Causes motor to rotate backwards.
	 */
	public final void backward() {
		iMode = 2;
		controlMotor(iId, 2);
	}

	/**
	 * Causes motor to stop, pretty much
	 * instantaneously. In other words, the
	 * motor doesn't just stop; it will resist
	 * any further motion.
	 */
	public final void stop() {
		iMode = 3;
		//controlMotor (iId, 1);
		controlMotor(iId, 3);
	}

	/**
	 * Causes motor to float. The motor will lose all power,
	 * but this is not the same as stopping. Use this
	 * method if you don't want your robot to trip in
	 * abrupt turns.
	 */
	public final void flt() {
		iMode = 4;
		controlMotor(iId, 4);
	}

	public void controlMotor(int aMotor, int aState) {
		//String motor_cmd = "21 " + aMotor + ""+(aState * 4);
		String motor_cmd = "21 " + aMotor + "" + aState;
		//System.out.println(motor_cmd);
		// state: --> 2:backward, 3:stop, 1:forward
		//int result = sendToRCX(motor_cmd, "motor");
		sendToRCX(motor_cmd, "motor");
		//byte[] bytePacket = { 21, RCXMath.toByte(aMotor+""+aState) };
		//int result = sendToRCX(bytePacket, "motor");
	}
	public void controlMotorPower(int aMotor, int aValue) {
		String power_cmd = "13 0" + aMotor + " 0" + aValue + " 00";
		int result = sendToRCX(power_cmd, "motorPower");
	}

}
