package rcxtools.rcxdirectmode;

import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

import rcxdirect.Motor;
import rcxtools.RCXDirectMode;
import rcxtools.share.gui.Colors;

public class ActionMotor extends Action {

	public static String[] motorLabel;
	public Motor[] motors = { Motor.A, Motor.B, Motor.C };
	public Motor motor = motors[0];
	private int[] states = { 1, 3, 2 };

	public ActionMotor(RCXDirectMode pOwner) {
		super(pOwner);
		motorLabel = owner.getMotorLabel();
	}

	public void go(ActionEvent evt) {

		StringTokenizer st = new StringTokenizer(evt.getActionCommand());
		String cmd_m = st.nextToken();
		int cmd_m_int = Integer.parseInt(cmd_m);
		int cmd_m_state_int = Integer.parseInt(st.nextToken());

		status.showPacket(
			"motor packet (Motor " + motorLabel[cmd_m_int] + ")");
		motor = motors[cmd_m_int];
		switch (cmd_m_state_int) {
			case 1 : motor.forward();	break;
			case 3 : motor.stop();		break;
			case 2 : motor.backward();	break;
			//default:
		}
		boolean success = motor.lastResult();
		// --> 2:backward, 3:stop, 1:forward
		//if (success) {   // Abfrage ist unsicher, da das RCX den Motor richtig anspricht, die Antwort jedoch
		// möglicherweise nicht ankommt.
		for (int k = 0; k < 3; k++)
			if (cmd_m_state_int != states[k])
				owner.setMotorBtnColor(cmd_m_int, k, Colors.bColor);
				//owner.motorButton[cmd_m_int][k].setBackground(Colors.bColor);
			else
				owner.setMotorBtnColor(cmd_m_int, k, Colors.buttonColor);
				//owner.motorButton[cmd_m_int][k].setBackground(Colors.buttonColor);
		
		owner.setMotorState(cmd_m_int, cmd_m_state_int);
		//owner.motor[cmd_m_int] = cmd_m_state_int;
		//}
		status.showPacket("motor packet (Motor " + motorLabel[cmd_m_int] + ")",
			motor.getTimeout(),	success);

	}
}