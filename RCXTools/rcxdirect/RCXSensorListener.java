import josx.platform.rcx.Sensor;
import josx.platform.rcx.SensorListener;

/**
 * @author T.R.
 *
 * RCXSensorListener
 * 
 */
public class RCXSensorListener implements SensorListener {
	
	int[] values;
	
	public RCXSensorListener(int[] pValues) {
		values = pValues;
	}

	public void stateChanged(Sensor src, int oldValue, int newValue) {
		//LCD.showNumber (newValue);
		//sensorValue[src.getId()] = newValue;
		values[src.getId()] = newValue;
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// ignore
		}
	}
}
