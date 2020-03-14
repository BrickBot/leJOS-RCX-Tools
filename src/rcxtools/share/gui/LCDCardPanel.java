package rcxtools.share.gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LCDCardPanel extends Panel {

	public final Font smallFont		= new Font("Dialog", Font.PLAIN, 10);
	public final Font bigFont		= new Font("Dialog", Font.PLAIN, 16);
	public final Font bigBoldFont	= new Font("Dialog", Font.PLAIN, 18);
	public static LCDSegment segment	= new LCDSegment();
	public static TextField tf_TextLCD	= new TextField("HELLO", 5);
	public static String toolName;

	public LCDCardPanel(String pToolName) {
		
		toolName = pToolName;
		setLayout(new CardLayout());

		Panel titlePanel = new Panel(new GridLayout(4, 1));
		Label[] titleLabel = new Label[4];

		titleLabel[0] = new Label(toolName, Label.CENTER);
		titleLabel[0].setFont(bigBoldFont);
		titleLabel[1] = new Label("(Copyright \u00a9 Tim Rinkens)", Label.CENTER);
		titleLabel[1].setFont(smallFont);
		titleLabel[2] = new Label("for leJOS", Label.CENTER);
		titleLabel[2].setFont(bigFont);
		titleLabel[3] = new Label("(leJOS is copyright (c) 2000 Jose Solorzano)",
								Label.CENTER);
		titleLabel[3].setFont(smallFont);
		for (int k = 0; k < titleLabel.length; k++) {
			titlePanel.add(titleLabel[k]);
			titleLabel[k].addMouseListener(new RCXCardMouseListener(this));
		}
		add("Title", titlePanel);
		
		if (toolName.indexOf("Download") >= 0)
			add("LCD", createSolidLCD());
		else
			add("LCD", createLCD());
	}

	Panel createLCD() {

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc;
		Panel p = new Panel(gbl);

		//LCD Display
		gbc = makegbc(0, 0, 3, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.weighty = 0.0;
		gbl.setConstraints(segment, gbc);
		p.add(segment);

		//Label TextField
		Label lb_TextLCD = new Label("TextLCD.print"); //,Label.RIGHT);
		lb_TextLCD.setFont(new Font("DialogInput", Font.BOLD, 10));
		gbc = makegbc(0, 1, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL; //NONE;
		//gbc.weighty = 0.0;
		gbl.setConstraints(lb_TextLCD, gbc);
		p.add(lb_TextLCD);

		//TextField
		gbc = makegbc(1, 1, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL; //NONE;
		//gbc.weighty=1.0;
		//if (System.getProperty("os.name").toUpperCase().equals("LINUX")) {
		//	gbc.ipady = -14;
		//	gbc.ipadx = 10;
		//}
		tf_TextLCD.setFont(new Font("DialogInput", Font.BOLD, 10));
		gbl.setConstraints(tf_TextLCD, gbc);
		p.add(tf_TextLCD);
		//tf_TextLCD.setSize(50,30);

		//Label2 TextField
		Label lb_TextLCD2 = new Label("  ", Label.RIGHT);
		lb_TextLCD2.setFont(new Font("DialogInput", Font.BOLD, 10));
		gbc = makegbc(2, 1, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL; //NONE;
		//gbc.weighty = 0.0;
		gbl.setConstraints(lb_TextLCD2, gbc);
		p.add(lb_TextLCD2);

		segment.setText(tf_TextLCD.getText());

		tf_TextLCD.addTextListener(new java.awt.event.TextListener() {
			public void textValueChanged(java.awt.event.TextEvent event) {
				TextField tf = (TextField) event.getSource();
				//System.out.println("textValueChanged: "+tf.getText());
				if (tf.getText().length() > 5) {
					tf.setText(tf.getText().substring(1, 6));
					tf.setCaretPosition(5);
				} else
					segment.setText(tf.getText());
			}
		});
		segment.addMouseListener(new RCXCardMouseListener(this));
		return p;
	}
	
	Panel createSolidLCD() {
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc;
		Panel p = new Panel(gbl);

		//LCD Display
		gbc = makegbc(0, 0, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		ImageUtil lcdjava = new ImageUtil("lejos.jpg");
		gbl.setConstraints(lcdjava, gbc);
		p.add(lcdjava);
		lcdjava.addMouseListener(new RCXCardMouseListener(this));
		return p;
	}

	public Dimension getPreferredSize() {
		return (toolName.indexOf("Download") >= 0)?
			new Dimension(306, 74) : new Dimension(240, 100);
	}

	private GridBagConstraints makegbc(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(1, 1, 1, 1);
		return gbc;
	}

	public void flip(String label) {
		((CardLayout) this.getLayout()).show(this, label);
	}
}

class RCXCardMouseListener extends MouseAdapter {
	LCDCardPanel owner;
	String canvasName;

	public RCXCardMouseListener(LCDCardPanel pOwner) {
		this.owner = pOwner;
	}

	public void mouseClicked(MouseEvent mouseEvent) {
		canvasName = mouseEvent.getSource().getClass().getName();
		//System.out.println(canvasName);
		//if (canvasName.indexOf("ImageUtil")>=0 ||
		if (canvasName.indexOf("LCDSegment")>=0) {
			int x = mouseEvent.getX();
			int y = mouseEvent.getY();
			//System.out.println("Segment " + x + "," + y);
			if ((x > 11) && (x < 33) && (y > 43) && (y < 56))
				owner.flip("Title");
		}
		else if (canvasName.indexOf("ImageUtil")>=0)
			owner.flip("Title");
		else
			owner.flip("LCD");
	}

}