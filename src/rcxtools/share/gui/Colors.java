package rcxtools.share.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.util.Enumeration;
import java.util.Hashtable;

import rcxtools.RCXTool;


public class Colors {
	private static int currentScheme = 0;
	private static Hashtable compTable = new Hashtable();
	private static Color[][] colors = 
		{ {	new Color(220, 220, 220),
			new Color(180, 180, 180),
			new Color(  0, 186, 186),
			new Color(  0,   0,   0),
			new Color(  0, 120, 120),
			new Color(255, 100, 155),
			new Color(  0, 222, 222),
			new Color(250, 227,  51),
			new Color(  0,   0,   0),
			new Color(255, 255, 255),
			new Color(  0,   0,   0) },

		  { new Color(220, 220, 220),	// bColor
			new Color(180, 180, 180),	// bColor1
			new Color(137, 137, 200),
			//Color(125,125,181);  		// bgColor
			new Color(255, 255, 255),	// bgFColor
			new Color(100, 100, 145),	// bgColor1
			new Color(170, 170, 246),	// buttonColor
			new Color(170, 170, 246),	// scrollColor
			new Color(175, 175, 255),	// lbcolor1
			new Color(  0,   0,   0),	// lbFColor1
			new Color(255, 255, 255),	// lbColor2
			new Color( 38,  38,  48) },	// lbFColor2

		  { SystemColor.control,				// bColor
			SystemColor.controlHighlight,		// bColor1
			SystemColor.activeCaptionBorder,	// bgColor
			SystemColor.textText,				// bgFColor
			SystemColor.controlShadow,			// bgColor1
			SystemColor.controlShadow,			// buttonColor
			SystemColor.scrollbar,				// scrollColor
			SystemColor.activeCaption,			// lbcolor1
			SystemColor.activeCaptionText,		// lbFColor1
			SystemColor.inactiveCaption,		// lbColor2
			SystemColor.inactiveCaptionText } };// lbFColor2
	
	public static Color bColor;
	public static Color bColor1;
	public static Color bgColor;
	public static Color bgFColor;
	public static Color bgColor1;
	public static Color buttonColor;
	public static Color scrollColor;
	public static Color lbColor1;
	public static Color lbFColor1;
	public static Color lbColor2;
	public static Color lbFColor2;

	public static void init(RCXTool pOwner) {
		
		setScheme(Integer.valueOf(pOwner.getProps().Colors()).intValue());
	}

	public static void update() {

		int[] bgfg; // = new int[2];

		for (Enumeration el = compTable.keys(); el.hasMoreElements();) {
			Component comp = (Component) el.nextElement();
			bgfg = (int[]) compTable.get(comp);
			if (bgfg[0] >= 0)
				comp.setBackground(colors[currentScheme][bgfg[0]]);
			if (bgfg[1] >= 0)
				comp.setForeground(colors[currentScheme][bgfg[1]]);
		}
		//System.out.println("Colors updated "+currentScheme);
	}

	public static void add(Component comp, int bgColor, int fgColor) {

		int[] bgfg = { bgColor, fgColor };

		compTable.put(comp, bgfg);
		if (bgfg[0] >= 0)
			comp.setBackground(colors[currentScheme][bgfg[0]]);
		if (bgfg[1] >= 0)
			comp.setForeground(colors[currentScheme][bgfg[1]]);
	}

	public static void setScheme(int scheme) {
		currentScheme = scheme;

		bColor 		= colors[currentScheme][0];
		bColor1		= colors[currentScheme][1];
		bgColor		= colors[currentScheme][2];
		bgFColor	= colors[currentScheme][3];
		bgColor1	= colors[currentScheme][4];
		buttonColor	= colors[currentScheme][5];
		scrollColor	= colors[currentScheme][6];
		lbColor1	= colors[currentScheme][7];
		lbFColor1	= colors[currentScheme][8];
		lbColor2	= colors[currentScheme][9];
		lbFColor2	= colors[currentScheme][10];
	}

}