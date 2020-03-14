package rcxtools.rcxdownload;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Adapter class for receiving component events from
 * RCXDownload.
 */


public class RepaintAdapter extends ComponentAdapter {

	private Frame owner;
	private Dimension minSize;

	public RepaintAdapter(Frame pOwner) {
		this.owner = pOwner;
		this.minSize = owner.getSize();
	}

	public void componentMoved(ComponentEvent event) {
		//System.out.println("RCXDownlRepaintAdapter: Component moved.");
		//event.getComponent().repaint();
	}

	public void componentResized(ComponentEvent event) {
		//System.out.println("RCXDownlRepaintAdapter: Component resized.");
		Dimension newSize = owner.getSize();

		if (newSize.width < minSize.width)
			owner.setSize(minSize.width, newSize.height);
		if (newSize.height < minSize.height)
			owner.setSize(newSize.width, minSize.height);
	}
}