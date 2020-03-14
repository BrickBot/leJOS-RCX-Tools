package rcxtools.share.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

/**
* The progress bar component is a lightweight component that can
* be easily incorporated into most Java programs. A progress bar
* is made up of a rectangle boarder, background color, a foreground
* color, and a percentage displayed in the center of the rectangle.
* The foreground color expands or contracts to fill the rectangle
* depending on the percentage passed in the updatebar method (for
* example if 50% is passed the progress bar will show the left half
* as the foreground color and the right half as the background color).
* The string percentage displayed inside the progress bar will change
* colors when the foreground color of the progress bar paints over it.
* @version     1.0, 31 Jan 1998
* @author      Lou Schiano
*/
public class ProgressBar extends Canvas {

	private int progressWidth; // width of the progress bar
	private int progressHeight; // height of the progress bar
	private float percentage;
	// the percentage property used to updated the foreground
	private Image offscreenImg; // the offscreen image
	private Graphics offscreenG; // the offscreen images graphic context
	private Color progressColor = Color.red; // the default foreground color
	private Color progressBackground = Color.white;
	// the default background color

	/**
	 * Creates a progress bar using the passed width and height.
	 */
	public ProgressBar(int progrWidth, int progrHeight) {

		Font f = new Font("Dialog", Font.BOLD, 15);
		setFont(f);

		this.progressWidth = progrWidth;
		this.progressHeight = progrHeight;
		this.setSize(progressWidth, progressHeight);
	}

	/**
	* Creates a progress bar using the passed width, height, canvas color,
	* progress bar foreground color, and progress bar background color.
	*/
	public ProgressBar(int progrWidth, int progrHeight,
		Color canvasColor, Color progrColor, Color progrBackground) {

		Font f = new Font("Dialog", Font.BOLD, 15);
		setFont(f);

		this.progressWidth = progrWidth;
		this.progressHeight = progrHeight;
		this.progressColor = progrColor;
		this.progressBackground = progrBackground;
		this.setSize(progressWidth, progressHeight);

		setBackground(canvasColor);

	}

	/**
	* This method is called when another component wants to update the progress bar. A percentage in the form
	* of a float between 0 and 1 is accepted, then the progress bars repaint method is called to paint the
	* progress bar on its Canvas. The foreground of the progress bar will be repainted, plus the float will
	* be displayed in the center of the progress bar. Never pass this method a float greater than 1.
	*/
	public void updateBar(float percentage) {

		this.percentage = percentage;
		repaint();

	}

	/**
	* Sets the background color of the canvas the progress bar is drawn on.
	*/
	public void setCanvasColor(Color color) {

		setBackground(color);
	}

	/**
	* Sets the foreground color of the progress bar.
	*/
	public void setProgressColor(Color progressColor) {

		this.progressColor = progressColor;
	}

	/**
	* Sets the background color of the progress bar.
	*/
	public void setBackGroundColor(Color progressBackground) {

		this.progressBackground = progressBackground;
	}

	public void setForeground(Color progressForeground) {

		setProgressColor(progressForeground);
	}
	public void setBackground(Color progressBackground) {

		//setBackGroundColor(progressBackground);
	}
	/**
	* Paints the progress bar on the canvas. The string percentage displayed inside the progress bar will change colors
	* when the foreground color of the progress bar paints over it. This is accomplished by first painting the
	* the string percentage in the same color as the foreground, then setting the clipping rectange to the same size
	* as the foreground and repainting the percentage in the background color in the same spot. When the forground
	* is not overlaying the string percentage, the second string percentage will not paint because the clipping rectangle
	* does not cover that area.
	*/
	public synchronized void paint(Graphics g) {

		int width = 0;
		int height = 0;
		int inset = 4;

		if (offscreenImg == null) offscreenImg =
				createImage(progressWidth - inset, progressHeight - inset);
		// Create offscreen image for double buffering.

		offscreenG = offscreenImg.getGraphics();
		// Get the offscreen images graphic context.

		width = offscreenImg.getWidth(this);
		// Get the width of the offscreen image.
		height = offscreenImg.getHeight(this);
		// Get the height of the offscreen image.

		offscreenG.setColor(progressBackground); // Change color to background.
		offscreenG.fillRect(0, 0, width, height);
		// Erase all with background color.

		offscreenG.setColor(progressColor); // Change color to foreground.
		offscreenG.fillRect(0, 0, (int) (width * percentage), height);
		// Paint foreground.
		offscreenG.drawString(Integer.toString((int) (percentage * 100)) + "%",
			width / 2 - 8, height / 2 + 5);
		// Paint string percentage.

		offscreenG.clipRect(0, 0, (int) (width * percentage), height);
		// Set cliping rectangle same size as foreground.
		offscreenG.setColor(progressBackground); // Set color to background.
		offscreenG.drawString(Integer.toString((int) (percentage * 100)) + "%",
			width / 2 - 8, height / 2 + 5);
		// Paint string percentage.

		g.setColor(progressBackground);
		// Set the Canvas color to Progress bar background color.
		g.draw3DRect(getSize().width / 2 - progressWidth / 2, 0,
			progressWidth - 1, progressHeight - 1, false);
		// Draw a 3D rectangle.

		g.drawImage(offscreenImg, inset / 2, inset / 2, this);
		// Draw the offscreem image on the Canvas.

	}

	public void update(Graphics g) {

		paint(g);

	}

	public void paintNow() {
		update(getGraphics());
	}

} //end of progressBar class
