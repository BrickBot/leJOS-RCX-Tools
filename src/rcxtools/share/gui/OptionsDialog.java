package rcxtools.share.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import rcxtools.RCXTool;
import rcxtools.filebrowser.FileBrowser;
import rcxtools.share.tvm.LeJOSOptions;

/**
 * Dialog for environment variable settings, colors etc.
 * @see <a href="../tvm/LeJOSOptions.html">rcxtools.share.tvm.LeJOSOptions</a>
 */
public class OptionsDialog extends Dialog implements ActionListener {
	private boolean result;

	private RCXTool owner;
	private LeJOSOptions props;


	public static TextField tf_leJOS = new TextField("", 24);
	public static TextField tf_Java = new TextField("", 24);
	//public static Choice    ch_leJOSC = new Choice();
	public static Checkbox ch_JavaVer = new Checkbox("lejosc -target 1.1");
	public static Checkbox ch_JavaSrc = new Checkbox("lejosc -source 1.2");
	public static Choice ch_RCXTTY = new Choice();
	public static Choice ch_Colors = new Choice();
	public static Choice ch_leJRun = new Choice();
	public static Choice ch_leJFirmdl = new Choice();
	private static String str_leJOS;
	private static String str_Java;
	private static String str_JavaVer;
	private static String str_JavaSrc;
	//private static String   str_leJOSC;
	private static String str_RCXTTY;
	private static String str_leJRun;
	private static String str_leJFirmdl;
	private static String str_Colors;

	public OptionsDialog(RCXTool pOwner) {
		super(pOwner, true);
		this.owner = pOwner;
		props = owner.getProps();

		//Fenster
		Colors.add(this, 2, -1);
		setTitle("Preferences");
		//setFont(new Font("Dialog", Font.BOLD, 14));
		setLayout(new BorderLayout());

		//Panel textPanel = new Panel(new FlowLayout(FlowLayout.LEFT));
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc;
		setLayout(gbl);

		//Title-Label
		gbc = makegbc(0, 0, 0, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_title =
			new Label(
				"Preferences for RCXDownload and RCXDirectMode",
				Label.LEFT);
		//lb_title.setForeground(color.lbColor1);
		lb_title.setFont(new Font("Dialog", Font.PLAIN, 10));
		gbl.setConstraints(lb_title, gbc);
		Colors.add(lb_title, 2, 3);
		add(lb_title);

		//Label leJOS Path
		gbc = makegbc(0, 1, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_leJOS = new Label("Path to leJOS", Label.LEFT);
		lb_leJOS.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_leJOS, gbc);
		Colors.add(lb_leJOS, 2, 3);
		add(lb_leJOS);
		//Textfield leJOS Path
		gbc = makegbc(1, 1, 1, 1);
		//gbc.weightx = 100;
		//gbc.weighty = 0.0;
		tf_leJOS.setFont(new Font("DialogInput", Font.BOLD, 10));
		gbl.setConstraints(tf_leJOS, gbc);
		Colors.add(tf_leJOS, 0, -1);
		add(tf_leJOS);
		//Button leJOS Directory
		gbc = makegbc(2, 1, 1, 1);
		gbc.insets = new Insets(5, 5, 5, 5);
		Button btn_leJOS = new Button("Choose Directory");
		btn_leJOS.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dirButtonActionPerformed(tf_leJOS);
			}
		});
		Colors.add(btn_leJOS, 0, -1);
		btn_leJOS.setFont(new Font("DialogInput", Font.BOLD, 10));
		btn_leJOS.addActionListener(this);
		gbl.setConstraints(btn_leJOS, gbc);
		add(btn_leJOS);

		//Label Java Path
		gbc = makegbc(0, 2, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_Java = new Label("Path to JDK", Label.LEFT);
		lb_Java.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_Java, gbc);
		Colors.add(lb_Java, 2, 3);
		add(lb_Java);
		//Textfield Java Path
		gbc = makegbc(1, 2, 1, 1);
		//gbc.weightx = 100;
		//gbc.weighty = 0.0;
		tf_Java.setFont(new Font("DialogInput", Font.BOLD, 10));
		gbl.setConstraints(tf_Java, gbc);
		Colors.add(tf_Java, 0, -1);
		add(tf_Java);
		//Button Java Directory
		gbc = makegbc(2, 2, 1, 1);
		gbc.insets = new Insets(5, 5, 5, 5);
		Button btn_Java = new Button("Choose Directory");
		btn_Java.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dirButtonActionPerformed(tf_Java);
			}
		});
		Colors.add(btn_Java, 0, -1);
		btn_Java.setFont(new Font("DialogInput", Font.BOLD, 10));
		btn_Java.addActionListener(this);
		gbl.setConstraints(btn_Java, gbc);
		add(btn_Java);
		/*
		//Label lejosc command
		gbc = makegbc(0, 3, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_leJOSC = new Label("Compile with",Label.LEFT);
		lb_leJOSC.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_leJOSC, gbc);
		color.add(lb_leJOSC, 2, 3);
		add(lb_leJOSC);
		//Textfield lejosc command
		gbc = makegbc(1, 3, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		ch_leJOSC.add("lejosc  (jdk >1.1)");
		ch_leJOSC.add("lejosc1 (jdk 1.1)");
		ch_leJOSC.setFont(new Font("DialogInput", Font.BOLD, 10));
		
		gbl.setConstraints(ch_leJOSC, gbc);
		color.add(ch_leJOSC, 0, -1);
		add(ch_leJOSC);
		*/

		//Label Java Version
		gbc = makegbc(0, 3, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_JavaVer = new Label("Target version", Label.LEFT);
		lb_JavaVer.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_JavaVer, gbc);
		Colors.add(lb_JavaVer, 2, 3);
		add(lb_JavaVer);
		//Checkbox Java Version
		gbc = makegbc(1, 3, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(ch_JavaVer, gbc);
		ch_JavaVer.setFont(new Font("DialogInput", Font.BOLD, 10));
		Colors.add(ch_JavaVer, 2, 3);
		add(ch_JavaVer);
		
		//Label Java Source
		gbc = makegbc(0, 4, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_JavaSrc = new Label("Source version", Label.LEFT);
		lb_JavaSrc.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_JavaSrc, gbc);
		Colors.add(lb_JavaSrc, 2, 3);
		add(lb_JavaSrc);
		//Checkbox Java Version
		
		gbc = makegbc(1, 4, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbl.setConstraints(ch_JavaSrc, gbc);
		ch_JavaSrc.setFont(new Font("DialogInput", Font.BOLD, 10));
		Colors.add(ch_JavaSrc, 2, 3);
		add(ch_JavaSrc);

		//Label Com-Port
		gbc = makegbc(0, 5, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_RCXTTY = new Label("COM-Port", Label.LEFT);
		lb_RCXTTY.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_RCXTTY, gbc);
		Colors.add(lb_RCXTTY, 2, 3);
		add(lb_RCXTTY);
		//Choice Com-Port
		gbc = makegbc(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		//ch_RCXTTY.addItemListener(this);
		//if (System.getProperty("os.name").equals("Linux")) {
		String s = System.getProperties().getProperty("os.name").toUpperCase();
		if (s.indexOf("WINDOW") >= 0) {
			ch_RCXTTY.add("COM1");
			ch_RCXTTY.add("COM2");
			ch_RCXTTY.add("COM3");
			ch_RCXTTY.add("COM4");
		} else {
			ch_RCXTTY.add("/dev/ttyS0");
			ch_RCXTTY.add("/dev/ttyS1");
			ch_RCXTTY.add("/dev/ttyS2");
			ch_RCXTTY.add("/dev/ttyS3");
		}
		ch_RCXTTY.add("usb");
		//if (str_RCXTTY == "COM2") ch_RCXTTY.select(1);
		gbl.setConstraints(ch_RCXTTY, gbc);
		ch_RCXTTY.setFont(new Font("DialogInput", Font.BOLD, 10));
		Colors.add(ch_RCXTTY, 0, -1);
		add(ch_RCXTTY);

		//Label lejosrun
		gbc = makegbc(0, 6, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_leJRun = new Label("Program download", Label.LEFT);
		lb_leJRun.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_leJRun, gbc);
		Colors.add(lb_leJRun, 2, 3);
		add(lb_leJRun);
		//Choice lejosrun
		gbc = makegbc(1, 6, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;

		ch_leJRun.add("slow");
		ch_leJRun.add("fast");

		gbl.setConstraints(ch_leJRun, gbc);
		ch_leJRun.setFont(new Font("DialogInput", Font.BOLD, 10));
		Colors.add(ch_leJRun, 0, -1);
		add(ch_leJRun);

		//Label lejosfirmdl
		gbc = makegbc(0, 7, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_leJFirmdl = new Label("Firmware download", Label.LEFT);
		lb_leJFirmdl.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_leJFirmdl, gbc);
		Colors.add(lb_leJFirmdl, 2, 3);
		add(lb_leJFirmdl);
		//Choice lejosfirmdl
		gbc = makegbc(1, 7, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;

		ch_leJFirmdl.add("slow");
		ch_leJFirmdl.add("fast");

		gbl.setConstraints(ch_leJFirmdl, gbc);
		ch_leJFirmdl.setFont(new Font("DialogInput", Font.BOLD, 10));
		Colors.add(ch_leJFirmdl, 0, -1);
		add(ch_leJFirmdl);

		//Label Colors
		gbc = makegbc(0, 8, 1, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_Colors = new Label("Colors", Label.LEFT);
		lb_Colors.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_Colors, gbc);
		Colors.add(lb_Colors, 2, 3);
		add(lb_Colors);
		//Choice Colors
		gbc = makegbc(1, 8, 1, 1);
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		ch_Colors.add("Normal");
		ch_Colors.add("Mauve");
		ch_Colors.add("System");
		gbl.setConstraints(ch_Colors, gbc);
		ch_Colors.setFont(new Font("DialogInput", Font.BOLD, 10));
		ch_Colors.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				colorItemChanged();
			}
		});
		Colors.add(ch_Colors, 0, -1);
		add(ch_Colors);

		Panel buttonPanel =
			new Panel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		Button buttonAccept = new Button("  Accept  ");
		buttonAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doneButtonActionPerformed(evt);
			}
		});
		//buttonAccept.setBackground(color.bColor);
		Colors.add(buttonAccept, 0, -1);
		buttonAccept.setFont(new Font("Dialog", Font.PLAIN, 12));
		buttonAccept.addActionListener(this);
		buttonPanel.add(buttonAccept);

		Button buttonDenie = new Button(" Denie ");
		buttonDenie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				doneButtonActionPerformed(evt);
			}
		});
		//buttonDenie.setBackground(color.bColor);
		Colors.add(buttonDenie, 0, -1);
		buttonDenie.setFont(new Font("Dialog", Font.PLAIN, 12));
		buttonDenie.addActionListener(this);
		buttonPanel.add(buttonDenie);
		Colors.add(buttonPanel, 2, -1);

		//Button-Panel
		gbc = makegbc(0, 9, 0, 1);
		gbc.insets = new Insets(10, 1, 1, 1);
		//gbc.weightx = 100;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(buttonPanel, gbc);
		add(buttonPanel);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				exitForm();
			}
		});

		pack();
		setResizable(false);
		update();
	}

	public void setPos() {
		Point parloc = owner.getLocation();
		Dimension frame = owner.getSize();
		Dimension dialog = getSize();
		setLocation(
			(int) (parloc.x + (frame.width - dialog.width) / 2),
			(int) (parloc.y + (frame.height - dialog.height) / 2));
		update();
	}
	
	public void update() {
	
		if (props.fileExists()) {
			str_leJOS		= props.lejosPath();
			str_Java		= props.javaPath();
			str_JavaVer		= props.javaVer();
			str_JavaSrc		= props.javaSrc();
			//str_leJOSC	= props();
			str_RCXTTY		= props.rcxTTY();
			str_leJRun		= props.lejosRun();
			str_leJFirmdl	= props.lejosFirmdl();
			str_Colors		= props.Colors();
			if (str_leJOS != "") {
				tf_leJOS.setText(str_leJOS + File.separatorChar);
				if ((new File(str_leJOS,
					"lib" + File.separator + "classes.jar"))
					.exists())
					tf_leJOS.setForeground(Color.black);
				else
					tf_leJOS.setForeground(Color.red);
			} else {
				tf_leJOS.setText("");
			}
			if (str_Java != "") {
				tf_Java.setText(str_Java + File.separatorChar);
			} else {
				tf_Java.setText("");
			}
			
			if (str_JavaVer != "") {
				ch_JavaVer.setState(str_JavaVer.equals("1.1"));
			} else {
				ch_JavaVer.setState(false);
			}
			
			if (str_JavaSrc != "") {
				ch_JavaSrc.setState(str_JavaSrc.equals("1.2"));
			} else {
				ch_JavaSrc.setState(false);
			}
			//else                 { tf_Java.setText(java_HOME); }
			//if (str_leJOSC != "") { ch_leJOSC.select((str_leJOSC.equals("lejosc"))?0:1); }
			//else                  { ch_leJOSC.select(0); }
			if (str_RCXTTY.equals("")) {
				ch_RCXTTY.select(0);
			} else {
				ch_RCXTTY.select(str_RCXTTY);
			}
			if (str_leJRun.equals("")) {
				ch_leJRun.select(0);
			} else {
				ch_leJRun.select(str_leJRun);
			}
			if (str_leJFirmdl.equals("")) {
				ch_leJFirmdl.select(0);
			} else {
				ch_leJFirmdl.select(str_leJFirmdl);
			}
			if (str_Colors.equals("")) {
				ch_Colors.select(0);
			} else
				ch_Colors.select(Integer.valueOf(str_Colors).intValue());

			//System.out.println(""+str_leJOS);
			//System.out.println(""+str_Java);
			//System.out.println(""+str_RCXTTY);
		}
	}

	public void colorItemChanged() {

		Colors.setScheme(ch_Colors.getSelectedIndex());
		Colors.update();
	}

	public void doneButtonActionPerformed(ActionEvent event) {
		result = event.getActionCommand().equals("  Accept  ");

		setVisible(false);

	}
	public void dirButtonActionPerformed(TextField tf_path) {
		String path = "";
		//System.out.println("tf "+tf_path.equals(tf_leJOS)); //getClass().getName());
		if (tf_path.equals(tf_leJOS)) {
			path = getPathDialog("leJOS");
			if (!path.equals("")) {
				tf_path.setText(path);

				File file =
					new File(path, "lib" + File.separator + "classes.jar");
				boolean res = file.exists();

				if (res) {
					tf_path.setForeground(Color.black);
				} else {
					tf_path.setForeground(Color.red);
				}

			}
		} else {
			path = getPathDialog("JDK");
			if (!path.equals("")) {
				tf_path.setText(path);
			}
		}
		//System.out.println("directory-path: "+path);
	}
	
	public void actionPerformed(ActionEvent event) {
	}
	
	public String getPathDialog(String title) {
		//FileDialog fd = new FileDialog(owner, "Choose path to "+title, FileDialog.LOAD);
		//FilenameFilter ffAll = new AcceptAll();
		//fd.setFilenameFilter(ffAll);
		//fd.setFile("choose directory");
		FileBrowser fd =
			new FileBrowser(owner, System.getProperty("user.dir"), true);
		fd.setBackgroundColor(Colors.bgColor);
		fd.setForegroundColor(Colors.bgFColor);
		fd.setButtonColor(Colors.bColor);
		fd.setVisible(true);
		//String fileName = fd.getFile();
		String directory = fd.getDirectory();

		//System.out.println("directory: "+ directory);
		if ((directory != null)) {
			return (directory);
		} else
			return ("");
	}
	public boolean getResult() {
		if (result)
			LeJOSOptions.write(
				tf_leJOS.getText(),
				tf_Java.getText(),
				((ch_JavaVer.getState()) ? "1.1" : "none"),
				((ch_JavaSrc.getState()) ? "1.2" : "none"),
				//(ch_leJOSC.getSelectedIndex()==1)?"lejosc1":"lejosc",
				ch_RCXTTY.getSelectedItem(),
				ch_leJRun.getSelectedItem(),
				ch_leJFirmdl.getSelectedItem(),
				"" + ch_Colors.getSelectedIndex());
		
		Colors.setScheme(Integer.valueOf(props.Colors()).intValue());
		Colors.update();
		return result;
	}
	
	public void exitForm() { //(WindowEvent evt) { //FIRST:event_exitForm
		//System.exit (0);
		result = false;
		setVisible(false);
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
	/*
	class AcceptAll implements FilenameFilter   // Wird auf Win32-Platformen nicht unterstuetzt!!!
	                                            // mehr dazu unter http://www.artis.uni-oldenburg.de/Books/Java-FAQ/faq_d.html
	{
	   public void AcceptAll() {
	   }
	
	
	   public boolean accept(File dir, String name) {
	     File file = new File(dir,name);
	     //System.out.println("dir: "+ dir.toString() + " / "+ name);
	     if(file.isDirectory()) {
	         return true;
	     } else if (file.isFile()) { // && (name.endsWith(".java"))) {
	         return true;
	     } else {
	         return false;
	     }
	   }
	}
	*/
}