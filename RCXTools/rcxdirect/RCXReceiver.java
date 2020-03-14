import josx.platform.rcx.*;

/**
 * This program listens for messages from your PC.
 * It accepts a few standard opcodes, such as
 * Set Motor On/Off (0x21). You should be able to
 * control it using the RCXDirectMode GUI.
 */
public class RCXReceiver implements Segment, SensorConstants {
	private static final int LENGTH = 6;
	private static final int MAX = 99;
	static int sensorSegment[] =
		{ SENSOR_1_VIEW, SENSOR_2_VIEW, SENSOR_3_VIEW };
	static int motorSegment[][] =
		{ { MOTOR_A_VIEW, MOTOR_B_VIEW, MOTOR_C_VIEW }, {
			MOTOR_A_FWD, MOTOR_B_FWD, MOTOR_C_FWD }, {
			MOTOR_A_REV, MOTOR_B_REV, MOTOR_C_REV }
	};

	private static final Sensor[] SENSOR = { Sensor.S1, Sensor.S2, Sensor.S3 };
	private static final Motor[] MOTOR = { Motor.A, Motor.B, Motor.C };
	private static RCXSensorListener sensorListener;
	private static int[] sensorValue = { 0, 0, 0 };
	private static int[] motorValue = new int[3];
	//private static final byte[] clPacket = {(byte)0x00,(byte)0x00,(byte)0x00 };
	private static byte[] recPacket;


	public static void readSensorValue(byte[] pPacket) {
		//for (int k=0; k<=2; k++) SENSOR[k].activate();
		// removed rPacket (mem leak hint by Tobias Laursen)
		int pFlagsSensor = pPacket[1] & 0xFF;
		int pFlagsMode = pPacket[2] & 0xFF;

		//rPacket[0] = (byte) (45 & 0xFF); //(178 & 0xFF);  // (byte)178 = b2  -> send message
		//rPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		
		LCD.clear();

		if ((pFlagsSensor >= 0) && (pFlagsSensor <= 2)) {
			LCD.setSegment(sensorSegment[pFlagsSensor]);
			int value = 0;
			//Sensor s = (Sensor) pDevice;

			if (pFlagsMode == 0) { // raw-values have to be converted
				//SENSOR[pFlagsMode].setTypeAndMode (SENSOR_TYPE_LIGHT, SENSOR_MODE_RAW); // works bad in alpha5
				value = SENSOR[pFlagsSensor].readRawValue();
				int z = value;
				int i = 0;
				while (z > MAX) {
					i++;
					z = z - MAX;
				} // z = z-100; z = z-100; z = z-55; }
				pPacket[1] = (byte) (z & 0xFF);
				pPacket[2] = (byte) (i & 0xFF);
			} else if (pFlagsMode == 3) { // percent values
				//SENSOR[pFlagsMode].setTypeAndMode (SENSOR_TYPE_LIGHT, SENSOR_MODE_PCT); // works bad in alpha5
				value = sensorValue[pFlagsSensor];
				pPacket[1] = (byte) (value & 0xFF);
				pPacket[2] = (byte)0x00;
			} else if (pFlagsMode == 1) { // boolean values
				//SENSOR[pFlagsMode].setTypeAndMode (SENSOR_TYPE_TOUCH, SENSOR_MODE_BOOL); // works bad in alpha5
				value = (SENSOR[pFlagsSensor].readBooleanValue() ? 1 : 0);
				//((sensorValue[pFlagsSensor] > 1000) ? 1:0);
				pPacket[1] = (byte) (value & 0xFF);
				pPacket[2] = (byte)0x00;
			}

			LCD.showNumber(value);
		}
		Serial.sendPacket(pPacket, 0, 3);
		
		LCD.refresh();
		Sound.beep();
	}

	public static void setMotorPower(byte[] pPacket) {
		int pFlagsMotor = pPacket[1] & 0xFF;
		int pFlagsPower = pPacket[2] & 0xFF;
		//pPacket[0] = (byte) (13 & 0xFF);
		pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		Serial.sendPacket(pPacket, 0, 1);
		LCD.clear();
		LCD.showNumber(pFlagsPower);

		if ((pFlagsMotor >= 0) && (pFlagsMotor <= 2)) {
			LCD.setSegment(motorSegment[0][pFlagsMotor]);
			MOTOR[pFlagsMotor].setPower(pFlagsPower);
			motorValue[pFlagsMotor] = pFlagsPower;
		} else
			Sound.beep();
		LCD.refresh();
	}

	public static void setMotorOnOff(byte[] pPacket) {
		int pFlagsMotor = pPacket[1] & 0xFF;
		//pPacket[0] = (byte) (21 & 0xFF);
		pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		Serial.sendPacket(pPacket, 0, 1);

		//int x = (pFlagsMotor - (pFlagsMotor % 10)) / 10;
		//int y = pFlagsMotor % 10;

		int y = (pFlagsMotor % 16);
		int x = (pFlagsMotor - y) / 16;
		int pFlags = x * 10 + y;
		int motor_n = (pFlags - (pFlags % 10)) / 10;
		int motor_cmd = pFlags % 10;

		LCD.clear();
		//LCD.showNumber (motor_n);
		//LCD.showProgramNumber (motor_cmd);

		if ((motor_n >= 0) && (motor_n <= 2)) {
			LCD.showNumber(motorValue[motor_n]);
			//if (motor_cmd == 8) {
			if (motor_cmd == 1) {
				LCD.setSegment(motorSegment[1][motor_n]);
				MOTOR[motor_n].forward();
			}
			//else if (motor_cmd == 0) { 
			else if (motor_cmd == 2) {
				LCD.setSegment(motorSegment[2][motor_n]);
				MOTOR[motor_n].backward();
			} else if (motor_cmd == 3) {
				LCD.setSegment(motorSegment[0][motor_n]);
				MOTOR[motor_n].stop();
			} else {
				LCD.setSegment(motorSegment[1][motor_n]);
				LCD.setSegment(motorSegment[2][motor_n]);
				MOTOR[motor_n].flt();
			}
		} else
			Sound.beep();
		//LCD.showNumber(motor_n*10 + motor_cmd);  // debugging
		LCD.refresh();
	}

	public static void controlMotors(byte[] pPacket) {
		int[] pFlagsMotor = new int[3];
		int[][] motor_cmd = new int[3][2];
		pFlagsMotor[0] = pPacket[1] & 0xFF;
		pFlagsMotor[1] = pPacket[2] & 0xFF;
		pFlagsMotor[2] = pPacket[3] & 0xFF;
		//pPacket[0] = (byte) (21 & 0xFF);
		pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		Serial.sendPacket(pPacket, 0, 1);
		int i, x, y, pFlags;

		for (i = 0; i < 3; i++) {
			y = (pFlagsMotor[i] % 16);
			x = (pFlagsMotor[i] - y) / 16;
			pFlags = x * 10 + y;
			motor_cmd[i][0] = (pFlags - (pFlags % 10)) / 10;
			motor_cmd[i][1] = pFlags % 10;
		}
		LCD.clear();

		for (i = 0; i < 3; i++) {

			if ((motor_cmd[i][0] >= 1) && (motor_cmd[i][0] <= 4)) {

				if (motor_cmd[i][0] == 1) {
					LCD.setSegment(motorSegment[1][i]);
					MOTOR[i].forward();
				}
				//else if (motor_cmd == 0) { 
				else if (motor_cmd[i][0] == 2) {
					LCD.setSegment(motorSegment[2][i]);
					MOTOR[i].backward();
				} else if (motor_cmd[i][0] == 3) {
					LCD.setSegment(motorSegment[0][i]);
					MOTOR[i].stop();
				} else {
					LCD.setSegment(motorSegment[1][i]);
					LCD.setSegment(motorSegment[2][i]);
					MOTOR[i].flt();
				}
			} else
				Sound.beep();

			MOTOR[i].setPower(motor_cmd[i][1]);
			motorValue[i] = motor_cmd[i][1];
		}
		//LCD.showNumber(motor_n*10 + motor_cmd);  // debugging
		LCD.showNumber(
			motorValue[0] * 100 + motorValue[1] * 10 + motorValue[2]);
		//LCD.setNumber(0x3001, motorValue[0], 0x3002);
		//LCD.setNumber(0x3001, motorValue[1], 0x3004);
		//LCD.showProgramNumber(motorValue[2]);
		LCD.refresh();
	}

	public static void readBatteryValue(byte[] pPacket) {
		//byte[] rPacket = new byte[3];
		//rPacket[0] = (byte) (45 & 0xFF);
		pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		int value = Battery.getVoltageInternal();

		int z = value;
		int i = 0;
		while (z > MAX) {
			i++;
			z = z - MAX;
		} //z = z-100; z = z-100; z = z-55; }
		pPacket[1] = (byte) (z & 0xFF);
		pPacket[2] = (byte) (i & 0xFF);

		Serial.sendPacket(pPacket, 0, 3);
		//LCD.refresh();
		LCD.showNumber(value);
		Sound.beep();
	}
	
	public static void playSound(byte[] pPacket) {
		int pFlags = pPacket[1] & 0xFF;
		//pPacket[0] = (byte) (51 & 0xFF);
		pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
		Serial.sendPacket(pPacket, 0, 1);
		LCD.showNumber(pFlags);
		Sound.systemSound(true, pFlags);
	}

	// 0d3c - void rom_power_off
	//public static void powerOff() {
	//	pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
	//	Serial.sendPacket(pPacket, 0, 1);
	//	ROM.call ((short) 0x0d3c);
	//}

	public static void executePacket(byte[] pPacket) {
		int pOpCode = pPacket[0] & 0xF7;

		if (pOpCode == 0x10) {
			// Alive?
			LCD.showNumber(0000);
			pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
			Serial.sendPacket(pPacket, 0, 1);
			Sound.beep();
		} else if (pOpCode == 0x32) {
			readSensorValue(pPacket);
		} else if (pOpCode == 0x21) {
			setMotorOnOff(pPacket);
		} else if (pOpCode == 0x13) {
			setMotorPower(pPacket);
		} else if (pOpCode == 0x05) {
			controlMotors(pPacket);
		} else if (pOpCode == 0x51) {
			playSound(pPacket);
		} else if (pOpCode == 0x30) {
			readBatteryValue(pPacket);
		//} else if (pOpCode == 0x60) {
		//	powerOff(pPacket);
		} else {
			pPacket[0] = (byte) ~(pPacket[0] & 0xFF);
			Serial.sendPacket(pPacket, 0, pPacket.length);
			Sound.beep();
		}
		//pPacket = clPacket;
	}

	public static void main(String[] arg) {
		//LCD.showNumber (1111);
		LCD.clearSegment(WALKING);
		TextLCD.print("JAVA");
		LCD.refresh();
		Sound.playTone(1500, 20);
		
		// SensorListener is needed to get reasonable values.
		sensorListener = new RCXSensorListener(sensorValue);
		for (int k = 0; k <= 2; k++) {
			SENSOR[k].activate();
			SENSOR[k].addSensorListener(sensorListener);
			SENSOR[k].setTypeAndMode(SENSOR_TYPE_LIGHT, SENSOR_MODE_PCT);
			motorValue[k] = 4;
		}

		//byte[] pPacket = new byte[LENGTH];
		//pPacket = new byte[LENGTH];
		recPacket = new byte[LENGTH];
		// moved out of the loop (mem leak hint by David Hill)

		for (;;) {

			if (Serial.isPacketAvailable()) {

				Serial.readPacket(recPacket);

				executePacket(recPacket);
			}

		}
	}
}
