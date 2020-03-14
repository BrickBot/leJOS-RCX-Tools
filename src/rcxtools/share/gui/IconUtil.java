package rcxtools.share.gui;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Frame;
import java.net.URL;


public class IconUtil extends Frame {

	public IconUtil(Frame pOwner) {
		//this.owner = owner;

		try {
			//Image img = getToolkit().getImage("images/rcxicon.jpg");
			URL url = pOwner.getClass().getResource("share/gui/images/rcxicon.jpg");
			Image img = pOwner.getToolkit().getImage(url);
			//rcxicon.gif");
			MediaTracker mt = new MediaTracker(this);

			mt.addImage(img, 0);
			try {
				//Warten, bis das Image vollständig geladen ist,
				mt.waitForAll();
			} catch (InterruptedException e) {
				//nothing
			}
			pOwner.setIconImage(img);
		} catch (Exception e) {
			System.out.println("assignIcon: Can't find RCX-Icon.");
		}

	}

}