/*
 * The contents of this file are subject to the Dyade Public License,
 * as defined by the file DYADE_PUBLIC_LICENSE.TXT
 *
 * You may not use this file except in compliance with the License. You may
 * obtain a copy of the License on the Dyade web site (www.dyade.fr).
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License for
 * the specific terms governing rights and limitations under the License.
 *
 * The Original Code is Koala Graphics, including the java package
 * fr.dyade.koala, released July 10, 2000.
 *
 * The Initial Developer of the Original Code is Dyade. The Original Code and
 * portions created by Dyade are Copyright Bull and Copyright INRIA.
 * All Rights Reserved.
 */
/* Copyright (c) 1998 by Groupe Bull. All Rights Reserved */
/* $Id: FileBrowser.java,v 1.2 1999/01/08 16:33:56 tkormann Exp $ */
/* Author: Thierry.Kormann@sophia.inria.fr */

//package fr.dyade.koala.lyptus.widgets;
package rcxtools.filebrowser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;

/**
 * This class is file dialog.
 *
 * @author Thierry.Kormann@sophia.inria.fr
 */
public class FileBrowser extends Dialog implements FilenameFilter {

	// The linux OS
	static final int UNIX = 0;
	// The Windows OS
	static final int WINDOWS = 1;
	// The Mac OS
	static final int MAC = 2;

	//static Class c = FileBrowser.class;
	//static URL dir  = c.getResource("images/dirsmall.gif");
	//static URL odir = c.getResource("images/diropenedsmall.gif");
	//static URL file = c.getResource("images/filesmall.gif");
	Image dir;// = getToolkit().getImage("images/dirsmall.gif");
	Image odir;// = getToolkit().getImage("images/diropenedsmall.gif");
	Image file;// = getToolkit().getImage("images/filesmall.gif");
	static Font smallfont = new Font("Helvetica", Font.PLAIN, 10);

	static int os = UNIX;

	ListPanel dbrowser;
	ListPanel fbrowser;

	Frame owner;
	Panel actions;
	Panel exit;
	BorderPanel panelExt;
	Label labelExt;
	Choice directories;
	Choice extensions;
	Label selection;
	TextField field;
	Choice disk; // for windows system only !
	Button bOK;
	Button bCancel;
	File current;
	String currentExt;
	boolean dirOnly;
	String fSelected;
	//String dSelected = null;

	// optimisation rulez
	File[] files;

	static {
		String s = System.getProperties().getProperty("os.name").toUpperCase();
		if (s.indexOf("WINDOW") >= 0) {
			FileBrowser.os = WINDOWS;
		} else if (s.indexOf("MAC") >= 0) {
			FileBrowser.os = MAC;
		}
		//System.out.println("os.name: "+s);
	}

	public FileBrowser(Frame pOwner, String root, boolean dirOnly) {
		//super("File Browser");
		super(pOwner, "File Browser", true);
		this.owner = pOwner;
		
		URL url;
		url  = owner.getClass().getResource("share/gui/images/dirsmall.gif");
		dir  = owner.getToolkit().getImage(url);
		url  = owner.getClass().getResource("share/gui/images/diropenedsmall.gif");
		odir = owner.getToolkit().getImage(url);
		url  = owner.getClass().getResource("share/gui/images/filesmall.gif");
		file = owner.getToolkit().getImage(url);
		
		this.dirOnly = dirOnly;
		new WindowCloser(this);

		Label label;
		Panel panel;
		//Button b;

		setLayout(new GridBagLayout());
		setBackground(Color.lightGray);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(4, 4, 4, 4);
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;

		// WARNING : take care of the OS ! Windows has DISK (A:, C: ...)
		// Creates the directory choice
		panel = new Panel(new FlowLayout());
		if (os == WINDOWS) {
			panel.add(disk = new Choice());
			// Adds the 26 disks
			for (int i = 0; i < 26; ++i) {
				disk.add(((char) (((int) 'A') + i)) + ":\\");
			}
			disk.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					scanDirectory(new File((String) e.getItem()));
					dbrowser.browser.repaint();
					fbrowser.browser.repaint();
				}
			});
			disk.select(root.charAt(0) + ":\\");
			disk.setFont(smallfont);
		}
		panel.add(directories = new Choice());
		directories.addItemListener(new ItemListener() {
			String item = "";
			public void itemStateChanged(ItemEvent e) {			// <------- critical for linux <---
				//System.out.println((String) directories.getSelectedItem()+ "  " +
				//	(String)e.getItem());
				if (!item.equals((String) directories.getSelectedItem())) {
				//if (!item.equals((String) e.getItem())) {
					//item = (String) directories.getSelectedItem();
					item = (String) e.getItem();
					scanDirectory(new File(item));
					dbrowser.browser.repaint();
					fbrowser.browser.repaint();
				}
			}
		});
		directories.setFont(smallfont);
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH; //HORIZONTAL;
		setConstraintsCoords(constraints, 0, 1, 2, 1);
		add(panel, constraints);

		// Creates the directory and file list browser
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;
		constraints.fill = GridBagConstraints.BOTH;
		setConstraintsCoords(constraints, 0, 2, 1, 1);
		add(
			dbrowser = new ListPanel("Directories", new DirListHandler()),
			constraints);

		fbrowser = new ListPanel("Files", new FileListHandler());
		extensions = new Choice();
		if (!dirOnly) {
			setConstraintsCoords(constraints, 1, 2, 1, 1);
			add(fbrowser, constraints);

			// Creates the extension part
			constraints.weightx = 1.0;
			constraints.weighty = 0;
			constraints.anchor = GridBagConstraints.NORTHWEST;
			constraints.fill = GridBagConstraints.HORIZONTAL;
			panelExt =
				new BorderPanel(
					new FlowLayout(FlowLayout.LEFT),
					"Extensions Filter");
			panelExt.setFont(smallfont);
			panelExt.add(labelExt = new Label("Determine file type :"));
			labelExt.setFont(smallfont);
			panelExt.add(extensions);
			extensions.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (!currentExt.equals(e.getItem())) {
						currentExt = extensions.getSelectedItem();
						updateEntries();
						dbrowser.browser.repaint();
						fbrowser.browser.repaint();
					}
				}
			});
			extensions.setFont(smallfont);
			setConstraintsCoords(constraints, 0, 3, 2, 1);
			add(panelExt, constraints);
		} else
			this.setTitle("Directory Browser");
		// Creates the selection part
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.weightx = 1.0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(0, 4, 0, 4);
		setConstraintsCoords(constraints, 0, 4, 2, 1);
		add(selection = new Label(), constraints);
		selection.setFont(smallfont);
		setConstraintsCoords(constraints, 0, 5, 2, 1);
		field = new TextField();
		if (!dirOnly)
			add(field, constraints);
		field.setFont(smallfont);

		// Creates the exit part
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.weightx = 0;
		constraints.weighty = 0;
		constraints.fill = GridBagConstraints.NONE;
		setConstraintsCoords(constraints, 0, 6, 2, 1);
		panel = new Panel();
		panel.add(bOK = new Button("  OK  "));
		//bOK.addActionListener(this);
		bOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doneButtonActionPerformed(e);
				//fSelected = getSelectedFile().getPath();
				//dispose();
				//setVisible(false);
			}
		});
		bOK.setFont(smallfont);
		panel.add(bCancel = new Button("Cancel"));
		//bCancel.addActionListener(this);
		bCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doneButtonActionPerformed(e);
				//dispose();
				//fSelected = null;
				//setVisible(false);
			}
		});
		bCancel.setFont(smallfont);
		add(panel, constraints);

		setSize((dirOnly) ? 400 : 540, 400);
		setPos();
		// Initializes
		directories.add(new File(root).getAbsolutePath());
		extensions.add("java");
		extensions.add("Automatic");
		selection.setText("Selection: " + new File(root).getAbsolutePath());
		extensions.select(0);
		currentExt = extensions.getSelectedItem();
		scanDirectory(new File(new File(root).getAbsolutePath()));
	}

	public void addExtension(String ext) {
		extensions.add(ext.toUpperCase());
	}

	public boolean accept(File dir, String name) {
		return (
			(name.charAt(0) != '.')
				&& (new File(dir, name).isDirectory() || checkExtension(name)));
	}

	// Checks for the extension of the specified file
	private boolean checkExtension(String name) {
		if (!dirOnly) {
			if (extensions.getSelectedItem().equals("Automatic")) {
				return true;
			} else {
				return name.toLowerCase().endsWith(
					extensions.getSelectedItem());
			}
		} else
			return false;
	}

	// WARNING : take care of the OS !
	private void computeDirectories(String dir) {
		directories.removeAll();
		int index = dir.length();
		directories.add(dir);
		while (--index > 0) {
			if (os == WINDOWS && index < 3) {
				break;
			}
			if (dir.charAt(index) == File.separatorChar) {
				directories.add(dir.substring(0, index));
			}
		}
		if (os == WINDOWS) {
			directories.add(dir.substring(0, 3));
		} else {
			directories.add(dir.substring(0, 1));
		}
		directories.setSize(directories.getPreferredSize());
		directories.getParent().doLayout();
	}

	// Parses the specified directory
	public void scanDirectory(File fd) {
		if (!fd.exists()) {
			new ErrorDialog(
				this,
				"The file \"" + fd.getName() + "\" does not exist !")
				.setVisible(true); //.show();
			return;
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		current = fd;
		selection.setText("Selection: " + current.getAbsolutePath());

		computeDirectories(fd.getAbsolutePath());
		String[] entries = fd.list();
		if (entries != null) {
			Arrays.sort(entries);
			files = new File[entries.length];
			for (int i = 0; i < files.length; ++i) {
				files[i] = new File(current.getPath(), entries[i]);
			}
			updateEntries();
		}
	}

	public void updateEntries() {
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		fbrowser.browser.removeAllNodes();
		dbrowser.browser.removeAllNodes();
		dbrowser.browser.addNode(odir, odir, "." + File.separatorChar, current);
		if (current.getParent() != null) {
			dbrowser.browser.addNode(
				dir,
				dir,
				".." + File.separatorChar,
				new File(current.getParent()));
		}
		for (int i = 0; i < files.length; ++i) {
			File f = files[i]; //new File(current.getPath(), entries[i]);
			if (!accept(current, f.getName())) {
				continue;
			}
			if (f.isDirectory()) {
				dbrowser.browser.addNode(dir, dir, f.getName(), f);
			} else if (accept(current, f.getName())) {
				fbrowser.browser.addNode(file, file, f.getName(), f);
			}
		}
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void setConstraintsCoords(GridBagConstraints constraints,
			int x, int y, int width, int height) {
		constraints.gridx = x;
		constraints.gridy = y;
		constraints.gridwidth = width;
		constraints.gridheight = height;
	}
	public void setPos() {
		Point parloc = owner.getLocation();
		Dimension frame = owner.getSize();
		Dimension dialog = getSize();
		setLocation(
			(int) (parloc.x + (frame.width - dialog.width) / 2),
			(int) (parloc.y + (frame.height - dialog.height) / 2));
		//update();
	}
	public void setBackgroundColor(Color bgc) {
		this.setBackground(bgc);
		selection.setBackground(bgc);
		if (panelExt != null) {
			panelExt.setBackgroundColor(bgc);
			labelExt.setBackground(bgc);
		}
	}
	public void setForegroundColor(Color fgc) {
		selection.setForeground(fgc);
		if (panelExt != null) {
			panelExt.setForegroundColor(fgc);
			labelExt.setForeground(fgc);
		}
	}
	public void setButtonColor(Color bc) {
		bOK.setBackground(bc);
		bCancel.setBackground(bc);
		directories.setBackground(bc);
		if (extensions != null)
			extensions.setBackground(bc);
		if (disk != null)
			disk.setBackground(bc);
		field.setBackground(bc);
		dbrowser.vscroll.setBackground(bc);
		fbrowser.vscroll.setBackground(bc);
	}

	public void doneButtonActionPerformed(ActionEvent event) {
		//result = event.getActionCommand().equals("  Accept  ");
		if (event.getSource().equals(bOK)) {
			if (getSelectedFile() != null) {
				fSelected = getSelectedFile().getPath();
				dispose();
			}
		} else {
			fSelected = null;
			dispose();
		}
		//setVisible(false);
	}

	// Gets the selected node, file has a higher priority than directory
	private File getSelectedFile() {
		if (fbrowser.browser.getSelectedNodeCount() > 0) {
			return (File) fbrowser.browser.getSelectedNode(0).getItem();
		} else if (dbrowser.browser.getSelectedNodeCount() > 0) {
			return (File) dbrowser.browser.getSelectedNode(0).getItem();
		}
		return null;
	}
	public String getFile() {

		return fSelected;
	}
	public String getDirectory() {
		//((File) node.getItem()).getAbsolutePath();
		//System.out.println(fSelected);
		return fSelected;
	}
	/*
	    public static void main(String[] args) {
		//FileBrowser browser = new FileBrowser(args[0]);
	        FileBrowser browser =
	           new FileBrowser(System.getProperty("user.dir"), false);
		for (int i=1; i < args.length; ++i) {
		    browser.addExtension(args[i]);
		}
		browser.show();
	        String select = browser.getFile();
	        System.out.println(select);//browser.getDirectory());
	    }
	*/

	/*
	 * -----------------------------------------------------------------------
	 * This class is a panel with a title and a list browser.
	 * -----------------------------------------------------------------------
	 */
	private class ListPanel extends Panel {

		ListBrowser browser;
		Scrollbar vscroll;

		public ListPanel(String title, ListBrowserHandler handler) {
			super(new BorderLayout());

			Panel3D p = new Panel3D(false, false);
			Label label;
			p.add("North", label = new Label("  " + title) {
				public void paint(Graphics g) {
					super.paint(g);
					Dimension dim = getSize();
					g.setColor(Color.black);
					g.drawRect(1, 1, dim.width - 2, dim.height - 2);
					g.setColor(Color.lightGray);
					g.draw3DRect(2, 2, dim.width - 4, dim.height - 4, true);
				}
			});
			//label.setBackground(Color.lightGray);
			label.setBackground(SystemColor.control);
			label.setForeground(Color.black);
			p.add("Center", browser = new ListBrowser(handler));
			browser.setIncrement(14);
			p.setInsets(new Insets(1, 1, 1, 1));
			add("Center", p);

			add("East", vscroll = new Scrollbar());
			browser.setVerticalScrollbar(vscroll);
			browser.setBackground(new Color(240, 240, 245));
			setFont(smallfont);
		}

	}

	/*
	 * -----------------------------------------------------------------------
	 * The handler for the ListBrowser dedicated to the directories.
	 * -----------------------------------------------------------------------
	 */
	private class DirListHandler implements ListBrowserHandler {

		public void notifyOutside(ListBrowser browser, MouseEvent e) {
			// nothing to do
		}

		public void notifySelect(
			ListBrowser browser,
			ListNode node,
			MouseEvent e) {
			browser.setSelected(node, !node.isSelected());
			if (node.isSelected()) {
				String s =
					((File) node.getItem()).getName() + File.separatorChar;
				field.setText(s);
			} else {
				field.setText("");
			}
			if (dirOnly)
				selection.setText(
					"Selection: " + ((File) node.getItem()).getAbsolutePath());

			browser.repaint();
		}

		public void notifyExecute(ListBrowser browser, ListNode node) {
			scanDirectory((File) node.getItem());
			field.setText("");
			dbrowser.browser.repaint();
			fbrowser.browser.repaint();
		}
	}

	/*
	 * -----------------------------------------------------------------------
	 * The handler for the ListBrowser dedicated to the files.
	 * -----------------------------------------------------------------------
	 */
	private class FileListHandler implements ListBrowserHandler {

		public void notifyOutside(ListBrowser browser, MouseEvent e) {
			// nothing to do
		}

		public void notifySelect(
			ListBrowser browser,
			ListNode node,
			MouseEvent e) {
			browser.setSelected(node, !node.isSelected());
			if (node.isSelected()) {
				String s = ((File) node.getItem()).getName();
				field.setText(s);
			} else {
				field.setText("");
			}
			browser.repaint();
		}

		public void notifyExecute(ListBrowser browser, ListNode node) {
		}
	}

	/*
	 * -----------------------------------------------------------------------
	 * The error Dialog
	 * -----------------------------------------------------------------------
	 */
	private class ErrorDialog extends Dialog {

		public ErrorDialog(Dialog parent, String mes) {

			super(parent, "Error", true);
			//new fr.dyade.koala.util.WindowCloser(this);
			new WindowCloser(this);

			Label label;
			add("Center", label = new Label(mes));
			label.setFont(smallfont);
			Button b;
			add("South", b = new Button("Cancel"));
			b.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			b.setFont(smallfont);
			setSize(200, 120);
			Point parloc = parent.getLocation();
			Dimension frame = parent.getSize();
			Dimension dialog = getSize();
			setLocation(
				(int) (parloc.x + (frame.width - dialog.width) / 2),
				(int) (parloc.y + (frame.height - dialog.height) / 2));
			setBackground(parent.getBackground());
		}
	}
}
