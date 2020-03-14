package rcxtools.rcxdirectmode;

import java.awt.Button;
import java.awt.event.ActionEvent;

import rcxdirect.Motor;
import rcxdirect.Motors;
import rcxtools.RCXDirectMode;
import rcxtools.share.gui.Colors;


public class ActionStop extends ActionMotor {

	private Button[][] motorButton;


	public ActionStop(RCXDirectMode pOwner) {
		super(pOwner);
		motorButton = owner.getMotorButton();
	}


	public void go(ActionEvent evt) {

		boolean success;

		status.showPacket("motorStop packet (all Motors)");

		Motors.control(3, 3, 3,
			Motor.A.getPower(),
			Motor.B.getPower(),
			Motor.C.getPower());
		success = Motors.lastResultStatic();

		status.showPacket("motorStop packet (all Motors)",
			Motors.getTimeoutStatic(), success);
		
		for (int k = 0; k < 3; k++) {
			motorButton[k][0].setBackground(Colors.bColor);
			motorButton[k][1].setBackground(Colors.buttonColor);
			motorButton[k][2].setBackground(Colors.bColor);
		}
		/*
		for (int k=0; k<3; k++) {
		    if (owner.motor[k] != 1) {
		       owner.status.showPacket("motorStop packet (Motor " + motorLabel[k] + ")");
		       motor = motors[k];
		       motor.stop();
		       success = motor.lastResult();
		
		       owner.status.showPacket("motorStop packet (Motor " + motorLabel[k] + ")",
		                                motor.getTimeout(), success);
		
		    }
		    motorButton[k][0].setBackground(c.bColor);
		    motorButton[k][1].setBackground(c.buttonColor);
		    motorButton[k][2].setBackground(c.bColor);
		}
		*/

	}

}