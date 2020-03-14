package rcxtools.share.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DownloadDialog extends Dialog implements ActionListener {
	boolean result;
	private Frame owner;
	private Button buttonOK;
	private Button buttonYES;
	private Button buttonNO;

	public DownloadDialog(Frame pOwner, String pTitle,
						  String pLblText, boolean pYesNo) {
		super(pOwner, pTitle, true);
		this.owner = pOwner;
		//Fenster
		//setBackground(Color.lightGray);
		//setBackground(new Color(220,220,220));
		//setBackground(color.bgColor);
		Colors.add(this, 2, -1);
		setFont(new Font("Dialog", Font.PLAIN, 14));
		setLayout(new BorderLayout());

		Panel labelPanel = new Panel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc;
		labelPanel.setLayout(gbl);

		//Title-Label

		Label lineLabel;

		int i = 0;
		String line;
		int width = 40;
		do {
			if (pLblText.indexOf(" ") >= width) {
				line = pLblText.substring(0, pLblText.indexOf(" "));
				pLblText = pLblText.substring(pLblText.indexOf(" ")+1,pLblText.length());
			} else if (pLblText.indexOf(" ") == -1) {
				line = pLblText;
				pLblText = "";
			} else {
				if (pLblText.length() > width) {
					line = pLblText.substring(
						0,pLblText.substring(0, width).lastIndexOf(" "));
					pLblText = pLblText.substring(
					pLblText.substring(0, width).lastIndexOf(" ") + 1,
					pLblText.length());
				} else {
					line = pLblText.substring(0, pLblText.length());
					pLblText = "";
				}
			}
			//System.out.println(line);
			//System.out.println(lblText);
			lineLabel = new Label(line, Label.LEFT);
			gbc = makegbc(0, i, 0, 1);
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbl.setConstraints(lineLabel, gbc);
			labelPanel.add(lineLabel);
			Colors.add(lineLabel, 2, 3);
			i++;
		} while (!pLblText.equals(""));

		add(labelPanel, "Center");
		//Buttons
		Panel panel = new Panel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

		if (pYesNo) {
			buttonYES = new Button("  Yes  ");
			buttonYES.setBackground(Colors.bColor);
			buttonYES.addActionListener(this);
			panel.add(buttonYES);
			//panel.add(new Label("    "));
			buttonNO = new Button(" No ");
			buttonNO.setBackground(Colors.bColor);
			buttonNO.addActionListener(this);
			panel.add(buttonNO);
			buttonYES.requestFocus();
		} else {
			buttonOK = new Button("    OK    ");
			buttonOK.setBackground(Colors.bColor);
			buttonOK.addActionListener(this);
			panel.add(buttonOK);
			buttonOK.requestFocus();
		}
		add("South", panel);

		pack();
		setResizable(false);
	}

	public void setPos() {
		Point parloc = owner.getLocation();
		Dimension frame = owner.getSize();
		Dimension dialog = getSize();
		setLocation(
			(int) (parloc.x + (frame.width - dialog.width) / 2),
			(int) (parloc.y + (frame.height - dialog.height) / 2));
	}

	public void actionPerformed(ActionEvent event) {
		result = event.getActionCommand().equals("  Yes  ");
		setVisible(false);
		//dispose();
	}

	public boolean getResult() {
		return result;
	}
	private GridBagConstraints makegbc(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(1, 10, 1, 10);
		return gbc;
	}
}