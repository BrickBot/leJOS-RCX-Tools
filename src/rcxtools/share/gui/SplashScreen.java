package rcxtools.share.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import rcxtools.RCXTool;

/**
 * Splash screen
 *
 */
public class SplashScreen extends Thread {

	private SplashWindow sw;
	private Image splashIm;
	private boolean clicked = false;

	public SplashScreen(RCXTool pOwner) {

		MediaTracker mt = new MediaTracker(pOwner);
		URL url = getClass().getResource("images/rcxtools.jpg");
		splashIm = pOwner.getToolkit().getImage(url);
		//splashIm = Toolkit.getDefaultToolkit().getImage("rcxtools/share/gui/images/RCXTools.jpg");
		mt.addImage(splashIm, 0);
		try {
			//Warten, bis das Image vollständig geladen ist,
			//damit getWidth() und getHeight() funktionieren
			mt.waitForID(0);
		} catch (InterruptedException ie) {
		}

		sw = new SplashWindow(pOwner, splashIm);
		start();
	}
	public void run() {
		try {
			Thread.sleep(2500);
		} catch (InterruptedException ie) { }
		if (!sw.disappears()) sw.disappear();
	}

}

class SplashWindow extends Window {
	private Image splashIm;
	private RCXTool parent;
	//private int imgwidth;
	private final int width = 320;
	private final int height = 256;
	private final Font defaultFont = new Font("Dialog", Font.PLAIN, 10);
	private final Font bigFont = new Font("Dialog", Font.BOLD, 12);
	private final String clickMe = "don't show me again.";
	private Rectangle clickRect	= new Rectangle(0, 0, 0, 0);
	private Dimension clickBox	= new Dimension(0, 0);
	private boolean clicked  = false;
	private boolean pressed  = false;
	private boolean disappear = false;
	private boolean neverAgain= false;
	private FontMetrics fm;
	private Image offscreenImg; // the offscreen images
	private Graphics oG;		// the offscreen images graphic context

	SplashWindow(RCXTool parent, Image splashIm) {
		super(parent);
		this.parent = parent;
		this.splashIm = splashIm;
		//imgwidth = splashIm.getWidth(this);
		setSize(width, height);

		/* Add the mouse listener */
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				pressed = false;
				if (clickRect.contains(evt.getPoint())) {
					clicked = !clicked;
					repaint();
				}
			}
			public void mousePressed(MouseEvent evt) {
				pressed = true;
				if (clickRect.contains(evt.getPoint())) {
					neverAgain = true;
					repaint();
				}
				//while (evt.getID() == MouseEvent.MOUSE_PRESSED)
				//	try { Thread.sleep(100);
				//	} catch (InterruptedException ie) {}
			}
				
			public void mouseReleased(MouseEvent evt) {
				pressed = false;
			}
		});
		/*
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved (MouseEvent evt) {
				//if (pressed) {
				
					setLocation(evt.getX()-getSize().width/2,
							evt.getY()-getSize().height/2);
					//repaint();
					System.out.println(evt.getX()+" "+evt.getY());
				//}
			}			
		}); */

		/* Center the window */
		Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle winDim = getBounds();
		setLocation(
			(screenDim.width - winDim.width) / 2,
			(screenDim.height - winDim.height) / 2);
		setVisible(true);
	}

	public void paint(Graphics g) {
		if (splashIm != null) {
			

			if(offscreenImg == null)
				offscreenImg = createImage(width, height);
				
			fm = this.getGraphics().getFontMetrics(defaultFont);
			oG = offscreenImg.getGraphics();
			oG.drawImage(splashIm, 0, 0, this);
			oG.setColor(Color.gray);
			oG.draw3DRect(0, 0, width - 1, height - 1, true);
			/*
			drawShadow(oG, bigFont, parent.getClass().getName(), 10, 16);
			drawShadow(oG, defaultFont, "(Copyright \u00a9 Tim Rinkens)",10,26);
			drawShadow(oG, bigFont, "leJOS", 10, 40);
			drawShadow(oG, defaultFont, "is copyright \u00a9 Jose Solorzano",10,50);
			*/
			//cG.drawImage(splashIm,(int)Math.round((width-imgwidth)/2.),120,this);
			//drawCentered(cG, bigFont, parent.getClass().getName(),30);
			//drawCentered(cG, defaultFont, "(Copyright \u00a9 Tim Rinkens)",50);
			//drawCentered(cG, bigFont, "for leJOS",80);
			//drawCentered(cG, defaultFont, "(leJOS is copyright \u00a9 2000 Jose Solorzano)",100);
			if (!disappear) {

				clickBox = new Dimension(
					fm.getAscent() + fm.getDescent()-4,
					fm.getAscent() + fm.getDescent()-4);
				clickRect = new Rectangle(
					width - fm.stringWidth(clickMe) - clickBox.width - 8,
					height - fm.getAscent() - fm.getDescent() - 4,
					fm.stringWidth(clickMe) + clickBox.width + 4,
					fm.getAscent() + fm.getDescent()); //160,238,156,14
					
					//oG = cG.create();
					drawClickRect(oG);
			}
			g.drawImage(offscreenImg, 0, 0, this.getWidth(), this.getHeight(),this);	
			//cG = G.create();

			if (clicked && !disappear) disappear();
		}
	}
	
	public void update() {
		this.repaint();
		//update(getGraphics());
	}
	
	public void disappear() {
		if (neverAgain)
			parent.getProps().writeSplashScr();   // --> REMEMEBER SPLASHSCREEN OFF <--
		
		while (pressed)  {
			try { Thread.sleep(300);} 
			catch (InterruptedException e) { }
			
		}
		disappear = true;
		new DisappearThread(this).start();
		//dispose();
	}
	
	private void drawClickRect(Graphics pG) {
		pG.setColor(new Color(112,112,112));
		pG.draw3DRect(clickRect.x, clickRect.y,
					  clickRect.width, clickRect.height, !pressed);
		pG.draw3DRect(clickRect.x+2, clickRect.y+2,
					 clickBox.width, clickBox.height, false);
		pG.setFont(defaultFont);
		pG.drawString(clickMe,
					  clickRect.x + clickRect.width - fm.stringWidth(clickMe),
					  clickRect.y + fm.getAscent());
		if (clicked) {
			pG.drawLine(clickRect.x + 2,
						clickRect.y + 2,
						clickRect.x + 2 + clickBox.width,
						clickRect.y + 2 + clickBox.height);
			pG.drawLine(clickRect.x + 2 + clickBox.width,
						clickRect.y + 2,
						clickRect.x + 2,
						clickRect.y + 2 + clickBox.height);
		}
	}
	
	public boolean clicked() {
		return clicked;
	}
	
	public boolean pressed() {
		return pressed();
	}
	
	public boolean disappears() {
		return disappear;
	}
	/*
	public void drawShadow(Graphics g, Font font, String text,
			int posX, int posY) {
		g.setFont(font);
		g.setColor(Color.lightGray);
		g.drawString(text, posX + 1, posY + 1);
		g.setColor(Color.black);
		g.drawString(text, posX, posY);
	} */
	/*
	public void drawCentered(Graphics g, Font font, String text, int posY) {
	  int posX = (int)Math.round((width-g.getFontMetrics(font).stringWidth(text)) / 2.0);
	  drawShadow(g, font, text, posX, posY);
	} */
}
class DisappearThread extends Thread {
	
	SplashWindow splWin;
	
	public DisappearThread(SplashWindow pSplWin) {
		splWin = pSplWin;
	}
	public void run() {
		try {
			sleep(600);
			Dimension size = splWin.getSize();
			//Point loc = splWin.getLocation();
			Rectangle rect = splWin.getBounds();
			int step = 60;
			
			for (int i=(int)size.getWidth(); i>step ; i-=step) {
				rect.width  = rect.width-step;
				rect.height = rect.height - (int)Math.floor((double)step/5.0*4.0);
				rect.x = rect.x + step/2;
				rect.y = rect.y + step/2;
				//System.out.println(rect);
				
				splWin.setSize(rect.width, rect.height);
				splWin.setLocation(rect.x, rect.y);
				sleep(60);
			}
			splWin.dispose();
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}
}