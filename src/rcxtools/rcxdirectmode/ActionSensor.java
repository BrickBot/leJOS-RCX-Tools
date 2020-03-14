package rcxtools.rcxdirectmode;

import java.awt.event.ActionEvent;

import rcxdirect.Sensor;
import rcxtools.RCXDirectMode;



public class ActionSensor extends Action {
	
	private Sensor[] sensors = { Sensor.S1, Sensor.S2, Sensor.S3 };
	private Sensor sensor = sensors[0];
	
	public ActionSensor(RCXDirectMode pOwner) {
		super(pOwner);
	}
	
	public void go(ActionEvent evt) {

		int cmd_int = Integer.parseInt(evt.getActionCommand());
		//String cmd_state =
		//	cbg_s[cmd_int].getSelectedCheckbox().getLabel();
		int value = -1;

		//System.out.println("sensorButton: " + cmd + ", " + cmd_state);
		status.showPacket("sensor packet");

		sensor = sensors[cmd_int];

		if (owner.compareSensorMode(cmd_int, 0))//cmd_state == owner.cbg_s_label[0])
			value = sensor.readValue();
		else if (owner.compareSensorMode(cmd_int, 1))
			value = sensor.readRawValue();
		else if (owner.compareSensorMode(cmd_int, 2))
			value = ((sensor.readBooleanValue()) ? 1 : 0);

		if (sensor.lastResult()) {
			status.showPacket(
				"sensor packet (Sensor " + (cmd_int + 1) + ")",
				sensor.getTimeout(),
				true);
			if (!owner.compareSensorMode(cmd_int, 2))
			owner.setSensorLabel(cmd_int,
					" " + (cmd_int + 1) + ": " + value);
			else {
				owner.setSensorLabel(cmd_int,
					" " + (cmd_int + 1) + ": " + ((value != 0) ? "true" : "false"));
			}

		} else {
			status.showPacket(
				"sensor packet (Sensor " + (cmd_int + 1) + ")",
				sensor.getTimeout(),
				false);
			owner.setSensorLabel(cmd_int, " " + (cmd_int + 1) + ": -");
		}
	}

}