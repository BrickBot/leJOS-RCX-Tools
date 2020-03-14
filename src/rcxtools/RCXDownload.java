package rcxtools;
/**
 * file:   RCXDownload.java
 * @author Tim Rinkens <tau@uni-paderborn.de>
 * date:   2003-11-24
 *
 * The RCXDownload application is a visual interface for leJOS, a Java
 * Runtime for programming the Lego RCX written by Jose Solorzano.
 * RCXDownload automatically sets the JDK-, leJOS- and ClassPaths,
 * compiles the chosen Java-Source, shows the compiler messages
 * and is able to link and load both, the compiled classes and
 * the leJOS-firmware.
 *
 * RCXDownload v1.5. Copyright 2003 by Tim Rinkens. All Rights Reserved.
 * leJOS, Copyright (c) 2000 Jose H. Solorzano. All rights reserved.
 *
 * RCXDownload is unsupported, experimental software. Use at your own risk.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose and without fee is hereby granted,
 * provided that any copy or derivative of this software or documentation
 * retains the name "Tim Rinkens" and "Jose Solorzano" and also retains
 * this condition and the following disclaimer:
 *
 * This software is FREE FOR COMMERCIAL AND NON-COMMERCIAL USE,
 * provided the following condition is met.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 * IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * CopyrightVersion 1.0
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Vector;

import rcxtools.rcxdownload.RepaintAdapter;
import rcxtools.rcxdownload.ResetDialog;
//import rcxtools.share.gui.ImageUtil;
import rcxtools.share.gui.*;
import rcxtools.share.tvm.*;

public class RCXDownload extends RCXTool {

	/** Initializes the Form */
	public RCXDownload() {
		//new SplashScreen(this);
		super();

		firmwareDlg =
			new DownloadDialog(
				this, "RCX-Downloadmanager",
				"Would you like to download the leJOS-Firmware?", true);
		//exitDlg =
		//	new DownloadDialog(
		//		this, "RCX-Downloadmanager",
		//		"Would you really want to quit RCX-Downloadmanager?", true);


		resetDlg = new ResetDialog(this);

		addComponentListener(new RepaintAdapter(this));

		System.out.println(" Ready!");
	}



	/** This method is called from within the constructor to
	 * initialize the form.
	 */
	public void initComponents() {

		setTitle("RCX-Downloadmanager");

		setLayout(new BorderLayout(10, 10));
		//setBackground(bColor); //(Color.white);

		//status = new StatusBar(this);
		statusLabel = new Label (" Status: ");
		status = new StatusBar(statusLabel);
		
		mainPanel = new Panel();
		mainPanel.setLayout(new GridBagLayout());
		//(new java.awt.FlowLayout (2, 5, 5));
		mainPanel.setBackground(Color.white);

		titlePanel = new Panel(new GridBagLayout());
		//packetPanel = new Panel(new java.awt.GridLayout(1,1,0,0));
		//packetLabel = new Label("(...)");
		/*
		titleLabel = new Label[5];
		titleLabel[0] = new Label ("RCX-Download", Label.CENTER);
		titleLabel[0].setFont(bigBoldFont);
		titleLabel[1] = new Label ("(Copyright \u00a9 Tim Rinkens)", Label.CENTER);
		titleLabel[1].setFont(smallFont);
		titleLabel[2] = new Label ("for leJOS", Label.CENTER);
		titleLabel[2].setFont(bigFont);
		titleLabel[3] = new Label ("(leJOS is copyright", Label.CENTER);
		titleLabel[3].setFont(smallFont);
		titleLabel[4] = new Label ("(c) 2000 Jose Solorzano)", Label.CENTER);
		titleLabel[4].setFont(smallFont); */
		addComponent(titlePanel,new ImageUtil("usbtower.jpg"),-1, -1,0,0,1,1,5,20,0,20,2);
		addComponent(titlePanel, rcxPanel = new LCDCardPanel("RCX-Download"),	-1,-1,1,0,1,1,5,0,0,0,2);
		addComponent(titlePanel,new ImageUtil("rcx.jpg"),    -1, -1,2,0,1,1,5,20,0,20,2);

		Panel buttonBorder	= new Panel(new GridBagLayout());
		Panel buttonBorder1	= new Panel(new GridBagLayout());
		Panel[] panelBorder	= new Panel[3];
		for (int k = 0; k < 3; k++)
			panelBorder[k] = new Panel(new GridBagLayout());

		Panel topPanel		= new Panel(new GridBagLayout());
		Panel topPanel1		= new Panel(new GridBagLayout());
		Panel textBoxPanel	= new Panel(new GridBagLayout());
		Panel bottomPanel	= new Panel(new GridBagLayout());
		Panel bottomPanel1	= new Panel(new GridBagLayout());
		//Panel bottomPanel1 = new Panel(new GridBagLayout());

		openButton = new Button("Open");
		//openButton.setToolTipText("Click here to open a *.java-File");
		openButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				tvmFiles.open();
				setChoice(tvmFiles.getChoice());
				fileVector = tvmFiles.getFileVector();
				fileName = tvmFiles.getFileName();
				fileDir = tvmFiles.getFileDir();
				//openButtonActionPerformed (evt);
			}
		});

		tvmFiles = new LeJOSFiles(this);
		tvmFiles.load();
		choice = tvmFiles.getChoice();
		fileVector = tvmFiles.getFileVector();
		fileName = tvmFiles.getFileName();
		fileDir = tvmFiles.getFileDir();

		//choice.add(blankItem);
		choice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				//System.out.println(""+choice.getSelectedIndex());
				if (!fileVector.isEmpty()) {
					fileName = fileVector.elementAt(getChoice().getSelectedIndex()).toString();
					status.setText("File: " + fileName);
				}
				//openButtonActionPerformed (evt);
			}
		});

		downlFirmwareButton = new Button("Download Firmware");
		downlFirmwareButton
			.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String errFirmware = "";
				firmwareDlg.setPos();
				firmwareDlg.setVisible(true);
				if (firmwareDlg.getResult()) {
					getTextBox().setText("");
					//String err = "";
					try {
						//progressDlg.setPos();
						//progressDlg.show();
						invTVM.downloadFirmware();
					} catch (Exception e) {
						errFirmware = e.toString(); //err = e.getMessage();
						//System.out.println("RCXDownload.java: " + err); //e.getMessage());
						//textBox.append(e.toString());
					} finally {
						//progressDlg.hide();
						if (errFirmware.length() > 0) {
							//textBox.append("Download Firmware: Transfer Data Failed\n");
							//textBox.append("Perhaps the battery-power of the tower is low.\n");
							status.setText("There is something wrong!");
						} else
						getTextBox().append("Firmware succesfully replaced.\n");
					}
				}
				//downlFirmwareButtonActionPerformed (evt);
			}
		});

		preferencesButton = new Button("Preferences");
		preferencesButton.setFont(smallFont);
		preferencesButton
			.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				optionsDlg.setPos();
				optionsDlg.setVisible(true);
				if (optionsDlg.getResult()) {
					status.setText("Wrote preferences.");
					status.stop();
				}
			}
		});
		compileButton = new Button(" Compile ");
		compileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!fileVector.isEmpty())
					compileButtonActionPerformed();
			}
		});
		downloadButton = new Button(" Download ");
		//downloadButton.setToolTipText("Click here to download the program to the RCX");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				String errDownload = "";
				if (!fileVector.isEmpty())
					try {
						//progressDlg.setPos();
						//progressDlg.show();
						invTVM.setAutostart(startChbx.getState());

						invTVM.download(
							fileVector
								.elementAt(getChoice().getSelectedIndex())
								.toString());
						
					} catch (Exception e) {
						errDownload = e.getMessage();
						System.out.println(errDownload);

					} finally {
						//progressDlg.hide();
						//progressDlg.setVisible(false);
					}
			}
		});
		
		startChbx = new Checkbox("Autostart");
		startChbx.setFont(smallFont);
		/*
		cancelButton = new Button ("Cancel Download");
		cancelButton.addActionListener (new java.awt.event.ActionListener () {
		     public void actionPerformed (java.awt.event.ActionEvent evt) {
		       invTVM.stop();
		       //cancelButtonActionPerformed (evt);
		     }
		   }
		);
		*/
		resetButton = new Button("Reset");
		resetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				resetDlg.setPos();
				resetDlg.setVisible(true);
				if (resetDlg.getResult()) {
					//if ((resetDlg.getMessagesReset()) && (!textBox.getText().equals(""))) textBox.setText("");

				}

			}
		});

		exitButton = new Button("   Exit   ");
		//exitButton.setToolTipText("Quit the RCX-Download-Manager");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				//exitDlg.setPos();
				//exitDlg.setVisible(true);
				//if (exitDlg.getResult()) {
				invTVM.stop();
				System.exit(0);
				//}
			}
		});

		textBox =
			new TextArea("Welcome to the RCX-Download-Manager!\n", 18, 40);
		textBox.setFont(textBoxFont);
		textBox.setEditable(false);
		//textBox.setColumns(20);
		
		//addComponent(titlePanel,titleLabel[0],-1, -1, 0,0,1,2,0,0,0,0,2);
		//addComponent(titlePanel,titleLabel[1],-1, -1, 0,2,1,1,0,0,0,0,2);
		//addComponent(titlePanel,new ImageUtil("lcdjava.jpg"),-1, -1,1,0,1,4,5,20,0,30,2);
		//addComponent(titlePanel,titleLabel[2],-1, -1, 2,0,1,1,0,0,0,0,2);
		//addComponent(titlePanel,titleLabel[3],-1, -1, 2,1,1,1,0,0,0,0,2);
		//addComponent(titlePanel,titleLabel[4],-1, -1, 2,2,1,1,0,0,0,0,2);
		
		/*
		Label titleLabel_1 = new Label("RCXDownload");
		titleLabel_1.setFont(bigBoldFont);
		Label titleLabel_2 = new Label("for leJOS");
		titleLabel_2.setFont(bigFont);
		addComponent(titlePanel,titleLabel_1, -1, -1,  1,0,1,1,5,0,0,0,2);
		addComponent(titlePanel,titleLabel_2, -1, -1,  1,1,1,1,5,0,0,0,2);
		
		//((CardLayout)rcxPanel.getLayout()).show(rcxPanel,"LCD");
		addComponent(titlePanel,new ImageUtil("irtower.jpg"),-1, -1,0,0,1,2,5,0,0,30,2);
		addComponent(titlePanel,new ImageUtil("rcx.jpg"),    -1, -1,2,0,1,2,5,30,0,0,2);
		*/
		
		addComponent(topPanel1,openButton,    0, -1, 0,0,1,1,5,8,5,8,3); // 3
		addComponent(topPanel1,choice,        0, -1, 1,0,1,1,5,8,5,8,1); // 1
		addComponent(topPanel1,compileButton, 0, -1, 2,0,1,1,5,8,5,8,3); // 3
		addComponent(topPanel1,downloadButton,0, -1, 3,0,1,1,5,8,5,8,3); // 3
		addComponent(topPanel1,startChbx,	  2, -1, 4,0,1,1,5,8,5,8,3); // 3
		//addComponent(topPanel,cancelButton,  c.bColor,4,0,1,1,5,8,5,8,3); // 3
		addComponent(topPanel,topPanel1,      2, -1, 0,0,1,1,0,0,0,0,1);
		//addComponent(topPanel,cancelButton,   0,  -1, 1,0,1,1,5,8,5,8,4);
		
		addComponent(textBoxPanel,textBox,    0, -1, 0,0,1,1,5,8,5,8,5);
		
		addComponent(bottomPanel1,downlFirmwareButton, 0, -1, 0,0,1,1,5,8,5,8,1);
		addComponent(bottomPanel1,resetButton,         0, -1, 1,0,1,1,5,8,5,8,1);
		addComponent(bottomPanel1,preferencesButton,   0, -1, 2,0,1,1,5,8,5,8,2);
		
		addComponent(bottomPanel,bottomPanel1,    2, -1, 0,0,1,1,0,0,0,0,1);
		addComponent(bottomPanel,exitButton,      0,  -1, 1,0,1,1,5,8,5,8,4);
		
		
		addComponent(panelBorder[0],topPanel,     2, -1, 0,0,1,1,2,2,2,2,0);
		addComponent(panelBorder[1],textBoxPanel, 2, -1, 0,0,1,1,2,2,2,2,5);
		addComponent(panelBorder[2],bottomPanel,  2, -1, 0,0,1,1,2,2,2,2,0);
		
		addComponent(buttonBorder1,panelBorder[0], 0, -1, 0,0,1,1,10,10,0,10,0);
		addComponent(buttonBorder1,panelBorder[1], 0, -1, 0,1,1,1,10,10,0,10,5);
		addComponent(buttonBorder1,panelBorder[2], 0, -1, 0,2,1,1,10,10,10,10,0);
		
		addComponent(buttonBorder,buttonBorder1,   4, -1, 0,0,1,1,2,2,2,2,5);
		
		statusPanel = new Panel(new BorderLayout(0,0));
		
		//statusLabel.setForeground(c.lbFColor1);
		statusLabel.setAlignment(Label.LEFT);
		statusPanel.add(statusLabel,"Center");
		Colors.add(statusLabel, 7, 8);
		
		addComponent(mainPanel,titlePanel,  -1, -1, 0,0,1,1,0,0,0,0,0);
		addComponent(mainPanel,buttonBorder, 1,  -1, 0,1,1,1,5,10,0,10,5);
		addComponent(mainPanel,statusPanel,  7,   8,    0,2,1,1,5,0,0,0,0);

		add(mainPanel, "Center");
	
		invTVM = new Invoke(this);

		pack();
		//setResizable(false);
		setResizable(true);
	
		cVector.addElement(openButton);
		cVector.addElement(choice);
		cVector.addElement(compileButton);
		cVector.addElement(downloadButton);
		cVector.addElement(startChbx);
		cVector.addElement(downlFirmwareButton);
		cVector.addElement(resetButton);
		cVector.addElement(preferencesButton);
		//c.update();
	
	} //END:initComponents

	/*
	  private void openButtonActionPerformed (java.awt.event.ActionEvent evt) {//FIRST:event_openButtonActionPerformed
	
	    tvmFiles.open();
	    choice = tvmFiles.choice;
	    fileVector = tvmFiles.fileVector;
	    fileName = tvmFiles.fileName;
	    fileDir  = tvmFiles.fileDir;
	
	  }//LAST:event_openButtonActionPerformed
	*/

	public void compileButtonActionPerformed() {

		//fileName = fileVector.elementAt(choice.getSelectedIndex()).toString();
		String fileNameJava =
			fileName.substring(
				fileName.lastIndexOf(File.separator) + 1,
				fileName.length());
		String err = "";
		try {
			invTVM.compile(fileName);

		} catch (Exception e) {
			err = e.getMessage();
			System.out.println(err); //e.getMessage());
			textBox.append("\n");

			if (err.length() > 0) {
				printErrors(err, fileNameJava);
				status.setText("There is something wrong!");
			}
			//else {
			//textBox.append(fileNameJava + " is ready for download.\n");
			//status.setText("Succesfully compiled!");
			//status.stop();
			//}
		}
		if (err.length() == 0) {
			textBox.append(fileNameJava + " is ready for download.\n");
			status.setText("Succesfully compiled!");
			status.stop();
		}

	}
	/*
	  private void downlFirmwareButtonActionPerformed (java.awt.event.ActionEvent evt) {
	
	    firmwareDlg.setVisible(true);
	    //Auf das Schlieﬂen des Dialogs warten...
	    if (firmwareDlg.getResult()) {
	        invTVM.downloadFirmware();
	    }
	  }
	
	  private void downloadButtonActionPerformed (java.awt.event.ActionEvent evt) {
	    //status.setText("Downloading " + (String)fileVector.elementAt(choice.getSelectedIndex()) + " ...");
	    //invokeTvm (choice.getSelectedItem());
	    invTVM.download (fileVector.elementAt(choice.getSelectedIndex()).toString());
	      //(String)fileVector.elementAt(choice.getSelectedIndex()));
	  }
	  private void cancelButtonActionPerformed (java.awt.event.ActionEvent evt) {
	
	    //killed = true;
	    //if (proc != null) proc.destroy();
	    invTVM.stop();
	  }
	
	  private void exitButtonActionPerformed (java.awt.event.ActionEvent evt) {//FIRST:event_exitButtonActionPerformed
	
	    //if (proc!=null) proc.destroy();
	    invTVM.stop();
	    System.exit (0);
	  }
	*/
	private void printErrors(String err, String fileNameJava) {
		textBox.append("------- Compiler says -------\n");
		//textBox.append(err);
		//System.out.println("err.length() " + err.length());
		//System.out.println("fileName.length() " + fileName.length());
		//System.out.println("err.length()-fileName.length(): "+ (err.length()-fileName.length()));
		int k = 0;
		//System.out.println("fileName: " + fileName);
		try {

			if (err.startsWith(fileName))
				while (k < err.length()) {
					if ((k < (err.length() - fileName.length() - 1))
						&& (err
							.substring(k, k + fileName.length())
							.startsWith(fileName))) {
						textBox.append(fileNameJava + " -> line ");
						k += fileName.length() + 1;
						textBox.append(
							err.substring(k, err.indexOf(":", k)) + "\n");
						k = err.indexOf(":", k) + 1;
					} else {
						//System.out.println(k + ": " + err.substring(k,k+1));
						textBox.append(err.substring(k, k + 1));
						k++;
					}
				} else
				textBox.append(err + "\n");

		} catch (Exception e) { //(IndexOutOfBoundsException e) {
			System.out.println(e.toString());
		}

	}
	public void resetMessages() {
		if (textBox.getText().equals(""))
			status.setText("Message area is already clean.");
		else {
			textBox.setText("");
			status.setText("Message area is clean now.");
		}
		status.stop();
	}
	public void resetList() {
		if (fileVector.isEmpty())
			status.setText("List of java sources is empty.");
		else {
			tvmFiles.reset();
			choice.removeAll();
			choice = tvmFiles.getChoice();
			fileVector = tvmFiles.getFileVector();
			fileName = tvmFiles.getFileName();
			fileDir = tvmFiles.getFileDir();
			status.setText("List of java sources is empty now.");
		}
		status.stop();
	}
	public void disableButtons() { //boolean all) {

		openButton.setEnabled(false);
		choice.setEnabled(false);
		downlFirmwareButton.setEnabled(false);
		compileButton.setEnabled(false);
		downloadButton.setEnabled(false);
		//if (all) cancelButton.setEnabled(false);
	}
	public void enableButtons() {
		openButton.setEnabled(true);
		choice.setEnabled(true);
		downlFirmwareButton.setEnabled(true);
		compileButton.setEnabled(true);
		downloadButton.setEnabled(true);
		//cancelButton.setEnabled(true);
	}
	
	public TextArea getTextBox() {
		return textBox;
	}
	public Choice getChoice() {
		return choice;
	}
	public void setChoice(Choice pChoice) {
		choice = pChoice;
	}
	
	// Variables declaration
	
	public final static Dimension hpad10 = new Dimension(10, 1);
	public final static Dimension hpad40 = new Dimension(40, 1);
	public final static Dimension vpad10 = new Dimension(1, 10);
	public final static Dimension vpad40 = new Dimension(1, 40);

	public LeJOSFiles tvmFiles;

	public static DownloadDialog firmwareDlg;
	//private static DownloadDialog exitDlg;
	public static ResetDialog resetDlg;
	
	private static Panel	mainPanel;
	private static Panel	titlePanel;
	//private static Label[]	titleLabel;
	
	private static Button	openButton;
	private static Choice	choice;
	private static Button	downlFirmwareButton;
	private static Button	preferencesButton;
	private static Button	compileButton;
	private static Button	downloadButton;
	private static Checkbox startChbx;
	//private static Button cancelButton;
	//private static Button aboutButton;
	private static Button	resetButton;
	private static TextArea	textBox;
	
	public static Vector fileVector;
	public static String fileDir = "";
	public static String fileName = "";
	
	//protected final int SIZE = 1<<15; // 32k - the size of the RCX memory
	//private static final int MAX_ENTRIES = 10;
	//public String blankItem = "                    ";
	
	// End of variables declaration//END:variables
}