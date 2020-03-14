package rcxtools.share.tvm;

import java.awt.Choice;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import rcxtools.RCXDownload;
import rcxtools.filebrowser.FileBrowser;
import rcxtools.share.gui.Colors;
import rcxtools.share.gui.StatusBar;

/**
 * List of selected java files in RCXDownload.
 */
public class LeJOSFiles {

	private static final String lstFileName = "RCXDownload.lst";
	private RCXDownload owner;
	private Vector fileVector;
	private static final int MAX_ENTRIES = 20;
	private String fileDir = "";
	private String fileName = "";
	private Choice choice;

	private static StatusBar status;

	public LeJOSFiles(RCXDownload owner) {
		this.owner = owner;

		status = owner.getStatusBar();
	}

	public void open() {
		/*
		FileDialog fileDialog = new FileDialog (owner, "Open...", FileDialog.LOAD);
		FilenameFilter ffJava = new JavaFileIsOk();
		fileDialog.setFilenameFilter(ffJava);
		
		if (!fileVector.isEmpty()) {
		  String actFileName = fileVector.elementAt(choice.getSelectedIndex()).toString();
		  fileDialog.setDirectory(actFileName.substring(0,actFileName.lastIndexOf(File.separator)));
		}
		//fileDialog.setMode(FileDialog.LOAD);
		fileDialog.setBackground(color.bColor);
		fileDialog.show ();
		*/
		String currFileDir = System.getProperty("user.dir");
		if (!fileVector.isEmpty()) {
			try {
				currFileDir = (new File(fileVector
						.elementAt(choice.getSelectedIndex())
						.toString()))
						.getParent();
			} catch (IndexOutOfBoundsException ioobe) {
				currFileDir = System.getProperty("user.dir");
			}
		}
		//System.out.println("Current directory: "+currFileDir);
		FileBrowser fileDialog = new FileBrowser(owner, currFileDir, false);
		fileDialog.setBackgroundColor(Colors.bgColor);
		fileDialog.setForegroundColor(Colors.bgFColor);
		fileDialog.setButtonColor(Colors.bColor);
		fileDialog.setVisible(true);
		String name = fileDialog.getFile();

		if ((name == null) || (!name.endsWith(".java")))
			return;
		fileDir = (new File(name)).getParent();
		fileName = name;
		//System.out.println("File: "+fileDir+" , "+fileName);
		//status.setText("file: " + fileName);

		if (!fileVector.contains(name)) {
			if (fileVector.isEmpty()) {
				choice.removeAll();
			}
			choice.insert((new File(name)).getName(), 0);
			fileVector.insertElementAt(fileName, 0);
			status.setText("File: " + fileName);
			if (choice.getItemCount() > MAX_ENTRIES) {
				choice.remove(choice.getItemCount() - 1);
				fileVector.removeElementAt(fileVector.size() - 1);
			}
			save();
		} else
			choice.select(fileVector.indexOf(fileName));

	}

	public void save() {

		BufferedWriter f;
		String s;

		try {
			f = new BufferedWriter(new FileWriter(lstFileName));

			for (Enumeration el = fileVector.elements();el.hasMoreElements();) {
				s = (String) el.nextElement();
				f.write(s);
				f.newLine();
			}
			f.close();
		} catch (IOException e) {
			System.out.println(
				"An error occured while saving list of selected files in "
					+ lstFileName);
			status.setText(
				"An error occured while saving list of selected files in "
					+ lstFileName);
		}

	}
	public void load() {
		BufferedReader f;
		String line;
		//choice.removeAll();
		choice = new Choice();
		fileVector = new Vector();

		try {
			f = new BufferedReader(new FileReader(lstFileName));
			while ((line = f.readLine()) != null) {
				choice.add((new File(line)).getName());
				fileVector.addElement(line);
				System.out.println(lstFileName + ": " + line);
			}
			f.close();
		} catch (IOException e) {
			System.out.println(
				"An error occured while reading list of selected files from "
					+ lstFileName);
			status.setText(
				"An error occured while reading list of selected files from "
					+ lstFileName);
		}

		if (!fileVector.isEmpty()) {
			fileName = fileVector.elementAt(0).toString();
			fileDir =
				(new File(fileVector.elementAt(0).toString())).getParent();
			choice.select(fileVector.indexOf(fileName));
		} else
			choice.add("                           ");

	}
	public void reset() {
		fileDir = "";
		fileName = "";
		choice.removeAll(); // = new Choice();
		fileVector.removeAllElements(); // = new Vector();
		save();
	}
	
	public Choice getChoice() {
		return choice;
	}
	public Vector getFileVector() {
		return fileVector;
	}
	public String getFileName() {
		return fileName;
	}
	public String getFileDir() {
		return fileDir;
	}
	/*
	  class JavaFileIsOk implements FilenameFilter   // Wird auf Win32-Platformen nicht unterstuetzt!!!
	                                                 // mehr dazu unter http://www.artis.uni-oldenburg.de/Books/Java-FAQ/faq_d.html
	  {
	      public void JavaFileIsOk() {
	      }
	
	
	      public boolean accept(File dir, String name) {
	        File file = new File(dir,name);
	        if(file.isDirectory()) {
	            return true;
	        } else if (file.isFile() && (name.endsWith(".java"))) {
	            return true;
	        } else {
	            return false;
	        }
	      }
	  }
	*/
}