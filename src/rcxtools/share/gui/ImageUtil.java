package rcxtools.share.gui;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.net.URL;

public class ImageUtil extends Canvas {

	private Image img;
	//private Image offscreenImg;		// the offscreen image
	//private Graphics offscreenG;	// the offscreen images graphic context
	
	public ImageUtil(String pFName) {
		
		URL url = getClass().getResource("images/" + pFName);
		img = getToolkit().getImage(url);
		//img = getToolkit().getImage("rcxtools/share/gui/images/" + pFName);

		MediaTracker mt = new MediaTracker(this);

		mt.addImage(img, 0);
		try {
			//Warten, bis das Image vollständig geladen ist,
			//damit getWidth() und getHeight() funktionieren
			mt.waitForAll();
		} catch (InterruptedException e) {
			//nothing
		}
	}

	public void paint(Graphics g) {
		g.drawImage(img, 1, 1, this);
		/*
		if(offscreenImg == null)
			offscreenImg = createImage(img.getWidth(this), img.getHeight(this));
		offscreenG = offscreenImg.getGraphics();
		offscreenG.drawImage(img, 1, 1, this);
	
		g.drawImage(offscreenImg, 0, 0, this);
		*/
	}

	public Dimension getPreferredSize() {
		return new Dimension(img.getWidth(this), img.getHeight(this));
	}

	public Dimension getMinimumSize() {
		return new Dimension(img.getWidth(this), img.getHeight(this));
	}
}