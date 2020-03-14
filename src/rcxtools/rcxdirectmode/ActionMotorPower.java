package rcxtools.rcxdirectmode;

import java.awt.event.AdjustmentEvent;

import rcxtools.RCXDirectMode;

public class ActionMotorPower extends ActionMotor {

	public int[] motor_power = new int[3];

	public ActionMotorPower(RCXDirectMode pOwner) {
		super(pOwner);

		for (int k = 0; k < 3; k++)
			this.motor_power[k] = owner.getScrollbar(k).getValue();
	}

	public void go(AdjustmentEvent evt) {

		for (int k = 0; k < 3; k++) {
			if (motor_power[k] != owner.getScrollbar(k).getValue()) {
				owner.setScrollBarLabel(k, "" +
					(owner.getScrollbar(k).getMaximum()-
						owner.getScrollbar(k).getValue()- 1));
				status.showPacket("motorpower packet (Motor " + 
					motorLabel[k] + ")");

				motor = motors[k];
				motor.setPower(
					owner.getScrollbar(k).getMaximum()-
						owner.getScrollbar(k).getValue() - 1);
				boolean success = motor.lastResult();

				status.showPacket("motorpower packet (Motor " + 
					motorLabel[k] + ")", motor.getTimeout(), success);

				motor_power[k] = owner.getScrollbar(k).getValue();
			}
		}
	}
}