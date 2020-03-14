package rcxtools;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import rcxtools.share.gui.Colors;
import rcxtools.share.gui.IconUtil;
import rcxtools.share.gui.LCDCardPanel;
import rcxtools.share.gui.OptionsDialog;
import rcxtools.share.gui.SplashScreen;
import rcxtools.share.gui.StatusBar;
import rcxtools.share.tvm.Invoke;
import rcxtools.share.tvm.LeJOSOptions;

/**
 * @author Tim
 *
 * Abstraction of RCXDownload or RCXDirectMode
 * 
 */
public abstract class RCXTool extends Frame {

	public static final Font defaultFont	= new Font("Dialog", Font.PLAIN, 12);
	public static final Font smallFont		= new Font("Dialog", Font.PLAIN, 10);
	public static final Font boldFont		= new Font("Dialog", Font.PLAIN, 12);
	public static final Font bigFont		= new Font("Dialog", Font.PLAIN, 16);
	public static final Font bigBoldFont	= new Font("Dialog", Font.PLAIN, 20);
	
	// labelFont in RCXDirectMode:
	public static final Font labelFont		= new Font("Monospaced", Font.PLAIN, 10);
	// textBoxFont in RCXDownload:
	public static final Font textBoxFont	= new Font("Monospaced", Font.PLAIN, 12);
	private static final String[] CompList	= { "button", "scrollbar", "checkbox" };
	public  static LCDCardPanel rcxPanel;
	
	public static Vector cVector;
	public static Invoke invTVM;
	public static OptionsDialog optionsDlg;
	//public static DownloadDialog dlg;

	public static Button exitButton;
	public static Panel statusPanel;
	public static Label statusLabel;
	public static StatusBar status;
	
	private static String toolClassName;
	private static LeJOSOptions props;
	
	
	
	public RCXTool() {
		
		//java.lang.reflect.Field[] usedObjects =	this.getClass().getDeclaredFields();
		//toolClassName = usedObjects[0].getDeclaringClass().getName().toString();
		toolClassName = this.getClass().getName();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exitForm();
			}
		});
		props = new LeJOSOptions();
		props.load();
		
		Colors.init(this);
		cVector = new Vector();
		initComponents();
		new IconUtil(this);
		
		Dimension screen = getToolkit().getScreenSize();
		Dimension frame = getSize();
		setLocation(
			(int) ((screen.width - frame.width) / 2),
			(int) ((screen.height - frame.height) / 2));
		
		optionsDlg = new OptionsDialog(this);

		if (!props.loaded()) {

			optionsDlg.setPos();
			optionsDlg.setVisible(true);
			if (optionsDlg.getResult()) {
				status.setText("Wrote preferences.");
				status.stop();
				//Port.setName(LeJOSOptions.RCXTTY());
			}
		}
		if (props.splashscr())
			new SplashScreen(this);
		
		//progressDlg = new ProgressDialog(this);
		//rcxPanel = new RCXCardPanel();
		new Thread() {
			public void run() {
				try {
					sleep(2000);
				} catch (Exception e) {
				}
				((CardLayout) rcxPanel.getLayout()).show(rcxPanel, "LCD");
			}
		}
		.start();
	}
	
	public abstract void initComponents();

	// Komponente zum Panel hinzufügen:
	// comp .... Komponente
	// color ... Hinter-/Vordergrundfarbe
	// x ....... Spalte
	// y ....... Zeile
	// w ....... Breite
	// top usw.. Abstände zu den benachbarten Komponenten

	public void addComponent(
		Panel p, Component comp, int bgColor, int fgColor,
		int x,int y,int w,int h,int top,int left,int bottom,int right,int fill) {
		GridBagConstraints cb = new GridBagConstraints();
		cb.gridx = x;
		cb.gridy = y;
		cb.gridwidth = w;
		cb.gridheight = h;
		cb.weightx = 1;
		cb.weighty = 0;
		cb.anchor = GridBagConstraints.NORTHWEST;
		switch (fill) {
			case 0 :
				cb.fill = GridBagConstraints.HORIZONTAL; //BOTH;
				cb.anchor = GridBagConstraints.CENTER;
				break;
			case 1 :
				cb.fill = GridBagConstraints.NONE;
				break;
			case 2 :
				cb.fill = GridBagConstraints.NONE; //BOTH;
				cb.anchor = GridBagConstraints.CENTER;
				break;
			case 3 :
				cb.fill = GridBagConstraints.VERTICAL;
				cb.anchor = GridBagConstraints.CENTER;
				cb.weighty = 1;
				break;
			case 4 :
				cb.fill = GridBagConstraints.VERTICAL;
				cb.anchor = GridBagConstraints.NORTHEAST;
				break;
			case 5 :
				cb.fill = GridBagConstraints.BOTH;
				cb.anchor = GridBagConstraints.CENTER;
				cb.weighty = 1;
				break;
		}

		//cb.anchor = GridBagConstraints.CENTER;
		//cb.weightx = 1; cb.weighty = 1;
		cb.insets = new Insets(top, left, bottom, right);
		((GridBagLayout) p.getLayout()).setConstraints(comp, cb);
		//comp.setBackground(bgColor);
		Colors.add(comp, bgColor, fgColor);
		p.add(comp);
		// RCXDirectMode:
		if (toolClassName.endsWith("RCXDirectMode") && fill != 4)
			for (int k = 0; k < CompList.length; k++)
				if (comp.getName().startsWith(CompList[k]))
					cVector.addElement(comp);
		
		//System.out.println(this.getTitle());
		System.out.print("|");
	}
	
	public Vector getComponentList() {
		return cVector;
	}

	public StatusBar getStatusBar() {
		return status;
	}
	
	public String getClassName() {
		return toolClassName;
	}
	
	public LeJOSOptions getProps() {
		return props;
	}

	/** Exit the Application */
	public void exitForm() { //WindowEvent evt) { //FIRST:event_exitForm
		System.exit(0);
	} //LAST:event_exitForm
}
