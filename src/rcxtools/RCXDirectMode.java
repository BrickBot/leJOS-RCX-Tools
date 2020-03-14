package rcxtools;
/**
 * file:   RCXDirectMode.java
 * @author Tim Rinkens <tau@uni-paderborn.de>
 * date:   2003-11-24
 *
 * The RCXDirectMode application is a visual interface for leJOS, a Java
 * Runtime for programming the Lego RCX written by Jose Solorzano.
 * RCXDirectMode directly controls the RCX-Brick. Its transparent GUI
 * covers the complicated communication with the RCX.
 *
 * RCXDirectMode v1.5. Copyright 2003 by Tim Rinkens. All Rights Reserved.
 * leJOS, Copyright (c) 2000 Jose H. Solorzano. All rights reserved.
 *
 * RCXDirectMode is unsupported, experimental software. Use at your own risk.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose and without fee is hereby granted,
 * provided that any copy or derivative of this software or documentation
 * retains the names "Tim Rinkens" and "Jose Solorzano" and also retains
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
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import rcxdirect.Port;
import rcxtools.rcxdirectmode.*;
import rcxtools.share.gui.*;
import rcxtools.share.tvm.Invoke;
import rcxtools.share.tvm.LeJOSOptions;


public class RCXDirectMode extends RCXTool {

	/** Initializes the Form */
	public RCXDirectMode() {
		
		super();
		Port.setName(getProps().rcxTTY());
		//DirectSend.setSendPath(
		//	System.getProperty("user.dir") + File.separator + "rcxdirect");
		
		System.out.println(" Ready!");
	}


	/** This method is called from within the constructor to
	 * initialize the form.
	 */
	public void initComponents() {

		setTitle("RCX-Direct-Mode");

		setLayout(new BorderLayout(10, 10));
		//setBackground(bColor); //(Color.white);

		mainPanel = new Panel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBackground(Color.white);

		titlePanel = new Panel(new GridBagLayout());
		/*
		    titleLabel = new Label[4];
		    //packetPanel = new Panel(new java.awt.GridLayout(1,1,0,0));
		    //packetLabel = new Label("(...)");
		    titleLabel[0] = new Label ("RCX-Direct-Mode", Label.CENTER);
		    titleLabel[0].setFont(bigBoldFont);
		    titleLabel[1] = new Label ("(Copyright \u00a9 Tim Rinkens)", Label.CENTER);
		    titleLabel[1].setFont(smallFont);
		    titleLabel[2] = new Label ("for leJOS", Label.CENTER);
		    titleLabel[2].setFont(bigFont);
		    titleLabel[3] = new Label ("(leJOS is copyright (c) 2000 Jose Solorzano)", Label.CENTER);
		    titleLabel[3].setFont(smallFont);
		
		
		    addComponent(titlePanel,titleLabel[0],Color.white,  1,0,1,1,5,0,0,0,2);
		    addComponent(titlePanel,titleLabel[1],Color.white,  1,1,1,1,0,0,0,0,2);
		    addComponent(titlePanel,titleLabel[2],Color.white,  1,2,1,1,0,0,0,0,2);
		    addComponent(titlePanel,titleLabel[3],Color.white,  1,3,1,1,0,0,0,0,2);
		*/
		addComponent(titlePanel, rcxPanel = new LCDCardPanel("RCX-Direct-Mode"),	-1,-1,1,0,1,1,5,0,0,0,2);

		//((CardLayout)rcxPanel.getLayout()).show(rcxPanel,"LCD");

		//addComponent(titlePanel,new ImageUtil("irtower.jpg"),-1, -1,0,0,1,1,5,0,0,30,2);
		//addComponent(titlePanel,new ImageUtil("rcx.jpg"),    -1, -1,2,0,1,1,5,30,0,0,2);
		addComponent(titlePanel,new ImageUtil("motor.jpg"),-1, -1,0,0,1,1,5,0,0,20,2);
		addComponent(titlePanel,new ImageUtil("sensors.jpg"),    -1, -1,2,0,1,1,5,20,0,0,2);

		buttonBorder = new Panel(new GridBagLayout());
		buttonBorder1 = new Panel(new GridBagLayout());
		panelBorder = new Panel[4];

		for (int k = 0; k < 4; k++)
			panelBorder[k] = new Panel(new GridBagLayout());

		motorPanel = new Panel(new GridBagLayout());

		sensorPanel = new Panel(new GridBagLayout());

		soundPanel = new Panel(new GridBagLayout());

		bottomPanel = new Panel(new GridBagLayout());
		Panel bottomPanel1 = new Panel(new GridBagLayout());
		motorButton = new Button[3][3];
		motor = new int[3];
		bt_m_panel = new Panel[3];
		cbg_s = new CheckboxGroup[3];
		cbg_s_panel = new Panel[3];
		sensorLabel = new Label[3];

		for (int k = 0; k < 3; k++) {

			bt_m_panel[k] = new Panel(new GridLayout(3, 1));

			motorButton[k][0] = new Button("Forward");
			motorButton[k][0].setFont(defaultFont);
			Colors.add(motorButton[k][0], 0, -1);
			motorButton[k][1] = new Button("Stop");
			motorButton[k][1].setFont(defaultFont);
			Colors.add(motorButton[k][1], 5, -1);
			motorButton[k][2] = new Button("Backward");
			motorButton[k][2].setFont(defaultFont);
			Colors.add(motorButton[k][2], 0, -1);
			motor[k] = 3;
			sensorLabel[k] = new Label(" " + (k + 1) + ": -");

			cbg_s_panel[k] = new Panel(new GridLayout(3, 1));
			cbg_s[k] = new CheckboxGroup();
		}
		
		vsb = new Scrollbar[3];
		vsb_label = new Label[3];
		cbg_s_chbox = new Checkbox[3][3];
		sensorButton = new Button[3];
		
		for (int k = 0; k < 3; k++) {
			vsb[k] = new Scrollbar(Scrollbar.VERTICAL, 3, 1, 0, 8);
			vsb[k].addAdjustmentListener(new AdjustmentListener() {
				public void adjustmentValueChanged(AdjustmentEvent evt) {

					motorPowerAction.go(evt);
				}
			});

			vsb_label[k] =
				new Label("" + (vsb[k].getMaximum() - vsb[k].getValue() - 1));

			for (int i = 0; i < 3; i++) {

				motorButton[k][i].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {

						motorAction.go(evt);
					}
				});
				switch (i) {
					case 0 :
						motorButton[k][i].setActionCommand(k + " " + 1);
						break;
					case 1 :
						motorButton[k][i].setActionCommand(k + " " + 3);
						break;
					case 2 :
						motorButton[k][i].setActionCommand(k + " " + 2);
						break;
				}
				//motorButton[k][i].setActionCommand(k + " " + i);
				bt_m_panel[k].add(motorButton[k][i]);
				cVector.addElement(motorButton[k][i]);

				cbg_s_chbox[k][i] = new Checkbox(
					cbg_s_label[i], cbg_s[k], ((i == 0) ? true : false));
				//cbg_s_chbox[k][i] = new Checkbox(cbg_s_label[i],cbg_s[k],false);
				//cbg_s_chbox[k][i] = new Checkbox(cbg_s_label[i],cbg_s[k],false);
				Colors.add(cbg_s_chbox[k][i], 2, 3);
				cbg_s_panel[k].add(cbg_s_chbox[k][i]);
			}

			//cbg_s_panel[k].add();
			//cbg_s_panel[k].add();
			//cbg_s_panel[k].add();

			sensorButton[k] = new Button("Value");
			sensorButton[k].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					sensorAction.go(evt);
				}
			});
			sensorButton[k].setActionCommand("" + k);
		}

		sound_panel = new Panel(new GridLayout(3, 2, 3, 3));
		soundButton = new Button[6];
		soundButton[0] = new Button("Blip");
		soundButton[1] = new Button("Beep beep");
		soundButton[2] = new Button("Downward tones");
		soundButton[3] = new Button("Upward tones");
		soundButton[4] = new Button("Low buzz");
		soundButton[5] = new Button("Fast upward tones");
		for (int k = 0; k < 6; k++) {
			soundButton[k].setFont(smallFont);
			Colors.add(soundButton[k], 0, -1);
			sound_panel.add(soundButton[k]);
			// ------------>    Tiny-VM-usage: Sound.systemBeep (true, 5);
			soundButton[k].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					soundAction.go(evt);
				}
			});
			soundButton[k].setActionCommand("" + k);
			cVector.addElement(soundButton[k]);
		}

		stopButton = new Button("Stop all");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				stopAction.go(evt);
			}
		});
		batteryButton = new Button("Battery-Power");
		batteryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				batteryAction.go(evt);
			}
		});
		downloadButton = new Button("Download Direct-RCX");
		downloadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				downloadButtonActionPerformed();
			}
		});
		preferencesButton = new Button("Preferences");
		preferencesButton.setFont(smallFont);
		preferencesButton
			.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				preferencesButtonActionPerformed();
			}
		});
		exitButton = new Button("    Exit    ");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exitButtonActionPerformed();
			}
		});

		Label lb_motors = new Label(" Motors");
		char[] lb_mot_chars = { 'A', 'B', 'C' };
		Label lb_sensors = new Label(" Sensors");
		//lb_sensors.setForeground(c.lbFColor1);
		Label lb_sound = new Label(" Sound");
		//lb_sound.setForeground(c.lbFColor1);

		addComponent(motorPanel, lb_motors, 7, 8, 0, 0, 3, 1, 5, 8, 0, 8, 0);
		//addComponent(motorPanel,new Label(" A"),     color.lbColor2,0,1,1,1,10,8,0,8,0);
		//addComponent(motorPanel,new Label(" B"),     color.lbColor2,1,1,1,1,10,8,0,8,0);
		//addComponent(motorPanel,new Label(" C"),     color.lbColor2,2,1,1,1,10,8,0,8,0);
		for (int k = 0; k < 3; k++) {
			lb_motors = new Label(" " + lb_mot_chars[k]);

			addComponent(motorPanel,lb_motors,9,8,k,1,1,1,10,8,0,8,0);

			addComponent(motorPanel, vsb_label[k], 2,3, k,2,1,1,5,8,0,8,2);
			addComponent(motorPanel, vsb[k],       6,-1,k,3,1,2,2,8,4,8,3);
			addComponent(motorPanel, bt_m_panel[k],2,-1,k,5,1,1,5,8,0,8,0);
		}

		addComponent(motorPanel, stopButton,  0, -1, 0, 6, 3, 1, 10, 8, 5, 8, 0);

		addComponent(sensorPanel, lb_sensors, 7, 8, 0, 0, 3, 1, 5, 8, 0, 8, 0);

		for (int k = 0; k < 3; k++) {
			addComponent(sensorPanel,sensorLabel[k],9,8,k,1,1,1,10,8,0 ,8,0);
			addComponent(sensorPanel,cbg_s_panel[k],2,3,k,2,1,1, 5,8,10,8,0);
			
			addComponent(sensorPanel,sensorButton[k],0, -1, k, 3, 1, 1, 5, 8, 5, 8, 0);
		}
		addComponent(soundPanel, lb_sound, 7, 8, 0, 0, 3, 1, 5, 8, 0, 8, 0);
		addComponent(soundPanel, sound_panel, 2, -1, 0, 1, 3, 1, 10, 8, 5, 8, 0);
		
		addComponent(bottomPanel1,downloadButton,0, -1, 0, 0, 1, 1, 5, 8, 5, 8, 1);
		addComponent(bottomPanel1, batteryButton, 0, -1, 1, 0, 1, 1, 5, 8, 5, 8, 1);
		addComponent(bottomPanel1,preferencesButton,0, -1, 2, 0, 1, 1, 5, 8, 5, 8, 2);

		addComponent(bottomPanel, bottomPanel1, 2, -1, 0, 0, 1, 1, 0, 0, 0, 0, 1);
		addComponent(bottomPanel, exitButton, 0, -1, 1, 0, 1, 1, 5, 8, 5, 8, 4);

		addComponent(panelBorder[0], motorPanel, 2, -1, 0, 0, 1, 1, 2, 2, 2, 2, 3);
		addComponent(panelBorder[1], sensorPanel, 2, -1, 0, 0, 1, 1, 2, 2, 2, 2, 0);
		addComponent(panelBorder[2], soundPanel, 2, -1, 0, 0, 1, 1, 2, 2, 2, 2, 0);
		addComponent(panelBorder[3], bottomPanel, 2, -1, 0, 0, 2, 1, 2, 2, 2, 2, 0);
		addComponent(buttonBorder1,panelBorder[0],0, -1, 0, 1, 1, 2, 10, 10, 0, 0, 3);
		addComponent(buttonBorder1,panelBorder[1],0, -1, 1, 1, 1, 1, 10, 10, 0, 10, 0);
		addComponent(buttonBorder1,panelBorder[2],0, -1, 1, 2, 1, 1, 10, 10, 0, 10, 0);
		addComponent(buttonBorder1,panelBorder[3],0, -1, 0, 3, 2, 1, 10, 10, 10, 10, 0);

		addComponent(buttonBorder, buttonBorder1, 4, -1, 0, 0, 1, 1, 2, 2, 2, 2, 0);

		statusPanel = new Panel(new BorderLayout(0, 0));
		statusLabel = new Label(" Status: ");
		statusLabel.setAlignment(Label.LEFT);
		Colors.add(statusLabel, 7, 8);

		statusPanel.add(statusLabel, "Center");

		addComponent(mainPanel, titlePanel, -1, -1, 0, 1, 1, 1, 0, 0, 0, 0, 2);
		addComponent(mainPanel, buttonBorder, 1, -1, 0, 2, 1, 1, 5, 10, 0, 10, 0);

		addComponent(mainPanel, statusPanel, 7, 8, 0, 3, 1, 1, 5, 0, 0, 0, 0);

		add(mainPanel, "Center");

		status = new StatusBar(statusLabel);

		motorAction		= new ActionMotor(this);
		motorPowerAction= new ActionMotorPower(this);
		stopAction		= new ActionStop(this);
		sensorAction	= new ActionSensor(this);
		soundAction		= new ActionSound(this);
		batteryAction	= new ActionBattery(this);

		invTVM = new Invoke(this);

		pack();
		setResizable(false);

	} //END:initComponents

	public void downloadButtonActionPerformed() {

		//System.out.println("downloadButton");
		String errDownload = "";
		try {
			//progressDlg.setPos();
			//progressDlg.show();
			invTVM.setAutostart(true);
			invTVM.download(fileName);

		} catch (Exception e) {
			errDownload = e.getMessage();
			//status.setText("Problem occured while RCX-Receiver-download.");
			//System.out.println ("Problem occured while RCX-Receiver-download.");
			System.out.println("Downloading RCXReceiver: " + errDownload);
		} finally {
			//progressDlg.hide();
		}

	}

	public void preferencesButtonActionPerformed() {
		optionsDlg.setPos();
		optionsDlg.setVisible(true);
		if (optionsDlg.getResult()) {
			status.setText("Wrote preferences.");
			status.stop();
			Port.setName(getProps().rcxTTY());
		}
	}

	public void exitButtonActionPerformed() {
		//FIRST:event_exitButtonActionPerformed

		//if (proc!=null) proc.destroy();
		System.exit(0);
	}

//	private void disableButtons() {
//
//		for (Enumeration el = cVector.elements(); el.hasMoreElements();) {
//			Component comp = (Component) el.nextElement();
//			comp.setEnabled(false);
//		}
//		/*
//		    stopButton.setEnabled(false);
//		    for (int k=0; k<3; k++) {
//		        vsb[k].setEnabled(false);
//		        bt_m_panel[k].setEnabled(false);
//		        cbg_s_panel[k].setEnabled(false);
//		    }
//		*/
//	}

//	private void enableButtons() {
//		stopButton.setEnabled(true);
//		for (int k = 0; k < 3; k++) {
//			vsb[k].setEnabled(true);
//			bt_m_panel[k].setEnabled(true);
//			cbg_s_panel[k].setEnabled(true);
//		}
//
//	}

	public String[] getMotorLabel() {
		return motorLabel;
	}
	
	//public Label[] getSensorLabel() {
	//	return sensorLabel;
	//}
	public Button[][] getMotorButton() {
		return motorButton;
	}
	
	public void setMotorBtnColor(int pMot, int pState, Color pColor) {
		motorButton[pMot][pState].setBackground(pColor);
	}
	
	public void setMotorState(int pMot, int pState) {
		motor[pMot] = pState;
	}
	
	public void setSensorLabel(int pIndex, String pText) {
		sensorLabel[pIndex].setText(pText);
	}

	public boolean compareSensorMode(int pCBIndex, int pIndex) {
		return cbg_s[pCBIndex].getSelectedCheckbox().getLabel()
				.equals(cbg_s_label[pIndex]);		
	}
	
	public Scrollbar getScrollbar(int pIndex) {
		return vsb[pIndex];
	}
	
	public void setScrollBarLabel(int pIndex, String pText) {
		vsb_label[pIndex].setText(pText);
	}

	// Variables declaration
	private static Panel	mainPanel;
	private static Panel	titlePanel;
	//private static Label[]	titleLabel;
	private static Panel	buttonBorder;
	private static Panel	buttonBorder1;
	private static Panel[]	panelBorder;		// = new Panel[4];
	private static Panel	motorPanel;
	private static Panel	sensorPanel;
	private static Panel	soundPanel;
	private static Panel	bottomPanel;
	private static Scrollbar[]		vsb;		// = new Scrollbar[3];
	private static Label[]			vsb_label;	// = new Label[3];
	private static Panel[]	bt_m_panel;			// = new Panel[3];
	private static final String[] motorLabel = { "A", "B", "C" };
	private static Button[][] motorButton;		// = new Button[3][3];
	private int[]  motor;						// = new int[3];

	
	private static Label[]		sensorLabel;	// = new Label[3];
	private static CheckboxGroup[] cbg_s;		// = new CheckboxGroup[3];
	private static Checkbox[][] cbg_s_chbox;	// = new Checkbox[3][3];
	private static Panel[]		cbg_s_panel;	// = new Panel[3];
	private static final String[] cbg_s_label = { "percent", "raw", "boolean" };
	private static Button[]		sensorButton;	// = new Button[3];
	private static Button[]		soundButton;
	private static Panel		sound_panel;

	private static Button stopButton;
	private static Button batteryButton;
	private static Button downloadButton;
	private static Button preferencesButton;

	public static ActionMotor		motorAction;
	public static ActionMotorPower	motorPowerAction;
	public static ActionSound		soundAction;
	public static ActionStop		stopAction;
	public static ActionBattery		batteryAction;
	public static ActionSensor		sensorAction;

	//private String fileDir = "";
	private String fileName = LeJOSOptions.rcxReceiver;

	// End of variables declaration//END:variables
}
