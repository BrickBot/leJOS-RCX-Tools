package rcxtools.share.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.net.URL;


class LCDSegment extends Canvas {

	//private Image rcx_image; // = getImage(getCodeBase(), "images/lcdjava.gif");
	//private Image rcx_image = getToolkit().getImage("rcxtools/share/gui/images/lcd.jpg");
	private URL url = getClass().getResource("images/lcd.jpg");
	private Image rcx_image = getToolkit().getImage(url);
	//private Image rcx_image = Toolkit.getDefaultToolkit().getImage("rcxtools/share/gui/images/lcd.jpg");
	//protected Image  rcx_image = java.applet.Applet.getImage(java.applet.Applet.getCodeBase(), "images/lcdjava.gif");         // image (, that contains rects).

	private int digit;
	private String lcdText = "      ";
	private Dimension segmentSize = new Dimension(1 * 10, 1 * 18);
	private Dimension offDimension;
	private Image offImage;
	private Graphics offGraphics;
	//private boolean hasfocus;
	private int polysx[][] = {
		{ 1, 2, 8, 9, 8, 2},    //Segment 0
		{ 9,10,10, 9, 8, 8},    //Segment 1
		{ 9,10,10, 9, 8, 8},    //Segment 2
		{ 1, 2, 8, 9, 8, 2},    //Segment 3
		{ 1, 2, 2, 1, 0, 0},    //Segment 4
		{ 1, 2, 2, 1, 0, 0},    //Segment 5
		{ 1, 2, 8, 9, 8, 2},    //Segment 6
	};
	private int polysy[][] = {
		{ 1, 0, 0, 1, 2, 2},    //Segment 0
		{ 1, 2, 8, 9, 8, 2},    //Segment 1
		{ 9,10,16,17,16,10},    //Segment 2
		{17,16,16,17,18,18},    //Segment 3
		{ 9,10,16,17,16,10},    //Segment 4
		{ 1, 2, 8, 9, 8, 2},    //Segment 5
		{ 9, 8, 8, 9,10,10},    //Segment 6
	};
	private int digits[][] = {
		{1,1,1,1,1,1,0},         //Ziffer 0
		{0,1,1,0,0,0,0},         //Ziffer 1
		{1,1,0,1,1,0,1},         //Ziffer 2
		{1,1,1,1,0,0,1},         //Ziffer 3
		{0,1,1,0,0,1,1},         //Ziffer 4
		{1,0,1,1,0,1,1},         //Ziffer 5
		{1,0,1,1,1,1,1},         //Ziffer 6
		{1,1,1,0,0,0,0},         //Ziffer 7
		{1,1,1,1,1,1,1},         //Ziffer 8
		{1,1,1,1,0,1,1},         //Ziffer 9
		{1,1,1,0,1,1,1},         //Alphabet A=R
		{0,0,1,1,1,1,1},         //Alphabet B
		{1,0,0,1,1,1,0},         //Alphabet C
		{0,1,1,1,1,0,1},         //Alphabet D
		{1,0,0,1,1,1,1},         //Alphabet E
		{1,0,0,0,1,1,1},         //Alphabet F
		{1,0,1,1,1,1,0},         //Alphabet G
		{0,1,1,0,1,1,1},         //Alphabet H
		{0,1,1,0,0,0,0},         //Alphabet I
		{0,1,1,1,0,0,0},         //Alphabet J
		{0,1,1,0,1,1,1},         //Alphabet K
		{0,0,0,1,1,1,0},         //Alphabet L
		{1,1,1,0,1,1,0},         //Alphabet M=N
		{1,1,1,0,1,1,0},         //Alphabet N
		{1,1,1,1,1,1,0},         //Alphabet O
		{1,1,0,0,1,1,1},         //Alphabet P
		{1,1,1,0,0,1,1},         //Alphabet Q
		{0,0,0,0,1,0,1},         //Alphabet R
		{1,0,1,1,0,1,1},         //Alphabet S
		{1,1,1,0,0,0,0},         //Alphabet T
		{0,1,1,1,1,1,0},         //Alphabet U=V=W
		{0,1,1,1,1,1,0},         //Alphabet V
		{0,1,1,1,1,1,0},         //Alphabet W
		{0,1,1,0,1,1,1},         //Alphabet X
		{0,1,1,1,0,1,1},         //Alphabet Y
		{1,1,0,1,1,0,1},         //Alphabet Z
		{1,1,1,0,1,1,1},         //Alphabet a=A
		{0,0,1,1,1,1,1},         //Alphabet b=B
		{0,0,0,1,1,0,1},         //Alphabet c
		{0,1,1,1,1,0,1},         //Alphabet d=D
		{1,0,0,1,1,1,1},         //Alphabet e=E
		{1,0,0,0,1,1,1},         //Alphabet f=F
		{1,0,1,1,1,1,0},         //Alphabet g=G
		{0,0,1,0,1,1,1},         //Alphabet h
		{0,1,1,0,0,0,0},         //Alphabet i=I
		{0,1,1,1,0,0,0},         //Alphabet j=J
		{0,1,1,0,1,1,1},         //Alphabet k=K
		{0,0,0,1,1,1,0},         //Alphabet l=L
		{0,0,1,0,1,0,1},         //Alphabet m=n
		{0,0,1,0,1,0,1},         //Alphabet n
		{0,0,1,1,1,0,1},         //Alphabet o
		{1,1,0,0,1,1,1},         //Alphabet p=P
		{1,1,1,0,0,1,1},         //Alphabet q=Q
		{0,0,0,0,1,0,1},         //Alphabet r=R
		{1,0,1,1,0,1,1},         //Alphabet s=S
		{0,0,0,1,1,1,1},         //Alphabet t
		{0,0,1,1,1,0,0},         //Alphabet u=v=w
		{0,0,1,1,1,0,0},         //Alphabet v
		{0,0,1,1,1,0,0},         //Alphabet w
		{0,1,1,0,1,1,1},         //Alphabet x=X
		{0,1,1,1,0,1,1},         //Alphabet y=Y
		{1,1,0,1,1,0,1},         //Alphabet z=Z
		{0,0,0,0,0,0,0},         //Zeichen  Leer
		{0,0,0,1,0,0,0},         //Zeichen  _
		{0,0,0,0,0,0,1},         //Zeichen  -
		{0,1,0,0,1,0,1},         //Zeichen  /
		{0,0,0,1,0,0,1},         //Zeichen  = und :
		{0,0,0,0,1,0,0},         //Zeichen  .
		{1,0,0,1,1,1,0},         //Zeichen  (
		{1,1,1,1,0,0,0},         //Zeichen  )
		{0,1,0,0,0,1,0}          //Zeichen  "
	};

	public LCDSegment() {
		//this(0);
	}
	/*
	   public RCXSegment(int digit)
	 {
	      super();
	      this.digit = digit;
	      this.hasfocus = false;
	      enableEvents(AWTEvent.COMPONENT_EVENT_MASK);
	      enableEvents(AWTEvent.FOCUS_EVENT_MASK);
	      enableEvents(AWTEvent.MOUSE_EVENT_MASK);
	      enableEvents(AWTEvent.KEY_EVENT_MASK);
	   }
	*/
	public Dimension getPreferredSize() {
		return new Dimension(184, 74);
		//(1*10,1*18); //(1*15,1*27); //(1*10,1*18); //(5*10,5*18);
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(184, 74); //(1*10,1*18);
	}
	
	public boolean isFocusable() {
		return true;
	}
	
	public void paint(Graphics g) {
		update(g);
	}
	
	/**
	* (overload update to *not* erase the background before painting)
	*/
	public synchronized void update(Graphics g) {
		Dimension d = new Dimension(getBounds().width, getBounds().height);
		//Erzeugung eines unsichtbaren Graphikkontextes,
		//sofern kein brauchbarer existiert.
		if ((offGraphics == null)
			|| (d.width != offDimension.width)
			|| (d.height != offDimension.height)) {
			offDimension = d;
			offImage = createImage(d.width, d.height);
			offGraphics = offImage.getGraphics();
		}
		//Löschen des vorherigen Bildes:
		offGraphics.setColor(getBackground());
		offGraphics.fillRect(0, 0, d.width, d.height);
		offGraphics.drawImage(rcx_image, 0, 0, this); // paint the image
	
		int x = 49;
		int k = 0;
	
		while (k < lcdText.length()) {
			char key = ' ';
			try {
				key = lcdText.charAt(k);
			} catch (StringIndexOutOfBoundsException e) {
			} //System.out.println("RCXSegment: " + e.toString()); }
	
			int keyNum = java.lang.Character.getNumericValue(key);
			if (java.lang.Character.isLowerCase(key))
				keyNum += 26;
	
			//System.out.println("Numeric Value ("+key+"): "+ java.lang.Character.getNumericValue(key)+ " , keyNum: "+keyNum);
	
			if ((keyNum >= 0) && (keyNum <= 61)) {
				setValue(keyNum);
			}
			else if (key == ' ') setValue(62);
			else if (key == '_') setValue(63);
			else if (key == '-') setValue(64);
			else if (key == '/') setValue(65);
			else if (key == '=') setValue(66);
			else if (key == ':') setValue(66); // same as '='
			else if (key == '.') setValue(67);
			else if (key == ',') setValue(67);
			else if (key == '(') setValue(68);
			else if (key == ')') setValue(69);
			else if (key == '"') setValue(70);
			else setValue(62);
	
			paintSegment(offGraphics, x + k * (segmentSize.width + 4));
			k++;
		}
		g.drawImage(offImage, 0, 0, this);
	}
	
	public void paintSegment(Graphics g, int x) {
		Color lightgray = new Color(207, 207, 203);
		Color black = new Color(94, 94, 94);
		//Color yellow   = new Color(85,85,85);
	
		//g.translate(x,40);
		int y = 25;
		//dx und dy berechnen
		int dx = 1; //getSize().width / 10;
		int dy = 1; //getSize().height / 18;
		//Hintergrund
		//g.setColor(lightgray);
		//g.fillRect(x - 2, y - 2, segmentSize.width + 4, segmentSize.height + 4);
		//g.fillRect(x, y, segmentSize.width, segmentSize.height);

		//Segmente
		//if (hasfocus) {
		//   g.setColor(yellow);
		//} else {
		g.setColor(black);
		//}
		for (int i = 0; i < 7; ++i) { //alle Segmente
			if (digits[digit][i] == 1) {
				Polygon poly = new Polygon();
				for (int j = 0; j < 6; ++j) { //alle Eckpunkte
					poly.addPoint(x + dx * polysx[i][j], y + dy * polysy[i][j]);
				}
				g.fillPolygon(poly);
			}
		}
		//Trennlinien

		g.setColor(lightgray);
		g.drawLine(x, y, x + dx * 10, y + dy * 10);
		g.drawLine(x, y + 8 * dy, x + 10 * dx, y + 18 * dy);
		g.drawLine(x, y + 10 * dy, x + 10 * dx, y);
		g.drawLine(x, y + 18 * dy, x + 10 * dx, y + 8 * dy);
	}
	
	private void setValue(int value) {
		digit = value;
	
		//System.out.println("digits.length = "+ digits.length);
	}
	
	public void setText(String value) {
		lcdText = trim(value);
		repaint();
	}
	
	public String trim(String value) {
		String tmp = "";
		for (int i = 0; i < 5; i++) {
			if (i >= value.length())
				tmp = tmp.concat(" "); //break;
			else
				tmp = tmp.concat("" + value.charAt(i));
			if (i == 3)
				tmp = tmp.concat(" ");
		}
		return tmp;
	}
}
