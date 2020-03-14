package rcxtools.rcxdownload;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rcxtools.RCXDownload;
import rcxtools.share.gui.Colors;


public class ResetDialog extends Dialog implements ActionListener {

	private boolean result;

	private RCXDownload owner;

	public ResetDialog(RCXDownload pOwner) {
		super(pOwner, true);
		this.owner = pOwner;

		//Fenster

		//setBackground(color.bgColor);
		Colors.add(this, 2, -1);
		setTitle("Reset");

		setLayout(new BorderLayout());

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc;
		setLayout(gbl);

		//Title-Label
		gbc = makegbc(0, 0, 0, 1);
		gbc.insets = new Insets(1, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_title =
			new Label(
				"Reset the list of java sources or clear the message area.",
				Label.LEFT);
		//lb_title.setForeground(color.lbColor1);
		lb_title.setFont(new Font("Dialog", Font.PLAIN, 10));
		gbl.setConstraints(lb_title, gbc);
		Colors.add(lb_title, 2, 3);
		add(lb_title);

		//Label
		gbc = makegbc(0, 1, 1, 1);
		gbc.insets = new Insets(10, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_Lst = new Label("Java Sources", Label.LEFT);
		lb_Lst.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_Lst, gbc);
		Colors.add(lb_Lst, 2, 3);
		add(lb_Lst);

		//Button
		gbc = makegbc(1, 1, 1, 1);
		gbc.insets = new Insets(10, 1, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		//gbc.insets = new Insets(5, 5, 5, 5);
		Button btn_Lst = new Button(" Reset ");
		btn_Lst.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				lstButtonActionPerformed();
			}
		});
		btn_Lst.setBackground(Colors.bColor);
		btn_Lst.setFont(new Font("DialogInput", Font.BOLD, 10));
		//btn_Lst.addActionListener(this);
		gbl.setConstraints(btn_Lst, gbc);
		add(btn_Lst);

		//Label
		gbc = makegbc(0, 2, 1, 1);
		gbc.insets = new Insets(10, 5, 1, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		Label lb_Msg = new Label("Message Area", Label.LEFT);
		lb_Msg.setFont(new Font("Dialog", Font.PLAIN, 12));
		gbl.setConstraints(lb_Msg, gbc);
		Colors.add(lb_Msg, 2, 3);
		add(lb_Msg);

		//Button
		gbc = makegbc(1, 2, 1, 1);
		gbc.insets = new Insets(10, 1, 1, 1);
		gbc.anchor = GridBagConstraints.WEST;
		//gbc.insets = new Insets(5, 5, 5, 5);
		Button btn_Msg = new Button(" Reset ");
		btn_Msg.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				msgButtonActionPerformed();
			}
		});
		btn_Msg.setBackground(Colors.bColor);
		btn_Msg.setFont(new Font("DialogInput", Font.BOLD, 10));

		gbl.setConstraints(btn_Msg, gbc);
		add(btn_Msg);

		Button btn_Ok = new Button("     OK     ");
		btn_Ok.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				doneButtonActionPerformed();
			}
		});
		btn_Ok.setBackground(Colors.bColor);
		btn_Ok.setFont(new Font("Dialog", Font.PLAIN, 12));

		//Button-Panel
		gbc = makegbc(1, 3, 1, 1);
		gbc.insets = new Insets(20, 1, 10, 1);
		gbc.anchor = GridBagConstraints.WEST;

		gbl.setConstraints(btn_Ok, gbc);
		add(btn_Ok);

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm();
			}
		});

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
	/*
	public void update()
	{
	
	}
	*/

	public void doneButtonActionPerformed() {
		result = true; //event.getActionCommand().equals("     OK     ");
		setVisible(false);

	}

	public void msgButtonActionPerformed() {
		owner.resetMessages();
	}

	public void lstButtonActionPerformed() {
		owner.resetList();
	}

	public void actionPerformed(ActionEvent event) {

	}

	public boolean getResult() {
		return result;
	}

	public void exitForm() { //WindowEvent evt) { //FIRST:event_exitForm
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
}