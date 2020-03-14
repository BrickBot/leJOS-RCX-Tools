package rcxtools.share.gui;

import java.awt.Component;
import java.util.Enumeration;
import java.util.Vector;

import rcxtools.RCXTool;

public class ButtonUtil {

	private RCXTool owner;
	private Vector cVector = new Vector();
	//private Button cancelButton;

	public ButtonUtil(RCXTool pOwner) {
		//System.out.println("The class of " + owner +
		//                              " is " + owner.getClass().getName());
		this.owner = pOwner;
		this.cVector = pOwner.getComponentList();
		//this.cancelButton = owner.cancelButton;
	}

	public void disable() {

		for (Enumeration el = cVector.elements(); el.hasMoreElements();) {
			Component comp = (Component) el.nextElement();
			comp.setEnabled(false);
		}
		//if ((all) && (owner.getClass().getName().equals("RCXDownload")))
		//   cancelButton.setEnabled(false);

	}
	public void enable() {

		for (Enumeration el = cVector.elements(); el.hasMoreElements();) {
			Component comp = (Component) el.nextElement();
			comp.setEnabled(true);
		}
		//if (owner.getClass().getName().equals("RCXDownload"))
		//   cancelButton.setEnabled(true);
	}

}