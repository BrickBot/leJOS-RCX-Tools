package rcxtools.share.gui;

import java.awt.*;

/**
 * ProgressDialog indicates the progress of the download
 * operation.
 * @see <a href="ProgressBar.html">ProgressBar</a> 
 */
public class ProgressDialog
	extends Dialog //implements ActionListener //,WindowListener,KeyListener //,Runnable
{
	private boolean result;
	private Frame owner;
	//private static Colors color = new Colors();
	private static ProgressBar progressBar;
	private static Panel progressPanel;
	//private static Button buttonCancel;
	//private static RCXProgressThread progressThread = null;

	public ProgressDialog(Frame pOwner) //, boolean Cancel)
	{
		super(pOwner, "Downloading ...", false);
		this.owner = pOwner;
		//boolean Cancel = true;

		setFont(new Font("Dialog", Font.PLAIN, 14));
		setLayout(new BorderLayout(20, 20));

		progressPanel = new Panel();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc;
		progressPanel.setLayout(gbl);

		progressBar =
			new ProgressBar(260, 20, Color.lightGray, Color.blue, Color.white);
		// Create a progress bar

		gbc = makegbc(0, 0, 0, 1);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbl.setConstraints(progressBar, gbc);
		Colors.add(progressBar, -1, 4);
		progressPanel.add(progressBar);
		/*
		      //Buttons
		      Panel panel = new Panel();
		
		      if (Cancel)
		      {
		        //panel.setLayout(new FlowLayout(FlowLayout.CENTER,20,20));
		        buttonCancel = new Button("  Cancel  ");
		        buttonCancel.setBackground(color.bColor);
		        buttonCancel.addActionListener(this);
		        gbc = makegbc(0, 1, 0, 1);
		        gbc.fill = GridBagConstraints.NONE;
		        gbl.setConstraints(buttonCancel, gbc);
		        color.add(panel, 2, 3);
		        progressPanel.add(buttonCancel);
		        buttonCancel.requestFocus();
		        //add("South", panel);
		      }
		*/

		add(progressPanel, "Center");
		pack();
		Colors.add(this, 2, -1);
		setResizable(false);
		this.reset();
		//this.start();

	}
	/*
	   public void start()
	   {
	      progressThread = new RCXProgressThread(this);
	      progressThread.start();
	   }
	*/
	public void setPos() {
		Point parloc = owner.getLocation();
		Dimension frame = owner.getSize();
		Dimension dialog = getSize();
		setLocation(
			(int) (parloc.x + (frame.width - dialog.width) / 2),
			(int) (parloc.y + (frame.height - dialog.height) / 2));
	}

	public void setForeground(Color progressForeground) {

		progressPanel.setForeground(progressForeground);
	}
	public void setBackground(Color progressBackground) {

		progressPanel.setBackground(progressBackground);
	}
	/*
	   public void actionPerformed(ActionEvent event)
	   {
	      result = event.getActionCommand().equals("  Cancel  ");
	      //progressThread.stop();
	      setVisible(false);
	      //dispose();
	   }
	*/
	public void processEvent(AWTEvent event) {
		if (event.getID() == Event.WINDOW_DESTROY) {
			//progressThread.stop();
			setVisible(false);
		}
		super.processEvent(event);
	}

	public boolean getResult() {
		return result;
	}

	public synchronized void update(float percentage) {
		progressBar.updateBar(percentage);
		progressBar.paintNow();
	}
	public void reset() {
		progressBar.updateBar((float) 0.0);
		progressBar.paintNow();
	}

	private GridBagConstraints makegbc(int x, int y, int width, int height) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.insets = new Insets(20, 10, 20, 10);
		return gbc;
	}
}