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

//package fr.dyade.koala.beans;
package rcxtools.filebrowser;

import java.awt.*;
import java.awt.image.*;
import java.net.URL;

import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * This class enable Image loading and painting. Images that are created from a
 * URL or filename are preloaded using MediaTracker to monitor the loaded state
 * of the image.
 * <P>From the swing package (ImageIcon.java)
 * @version 1.16 10/06/97
 * @author Jeff Dinkins 
 */
public class ImageManager implements Serializable {

	transient Image image;
	ImageObserver imageObserver;
	String description = null;

	protected static Component component;
	protected static MediaTracker tracker;

	int width = -1;
	int height = -1;

	static {
		component = new Component() {
		}; // needed by MediaTracker
		tracker = new MediaTracker(component);
	}

	/**
	 * Creates an ImageManager from the specified file. The image will
	 * be preloaded by using MediaTracker to monitor the loading state
	 * of the image.
	 * @param filename the name of the file containing the image
	 * @param description a brief textual description of the image
	 */
	public ImageManager(String filename, String description) {
		image = Toolkit.getDefaultToolkit().getImage(filename);
		this.description = description;
		loadImage(image);
	}

	/**
	 * Creates an ImageManager from the specified file. The image will
	 * be preloaded by using MediaTracker to monitor the loading state
	 * of the image.
	 */
	public ImageManager(String filename) {
		image = Toolkit.getDefaultToolkit().getImage(filename);
		description = filename;
		loadImage(image);
	}

	/**
	 * Creates an ImageManager from the specified URL. The image will
	 * be preloaded by using MediaTracker to monitor the loaded state
	 * of the image.
	 * @param URL the URL for the image
	 * @param description a brief textual description of the image
	 */
	public ImageManager(URL location, String description) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		this.description = description;
		loadImage(image);
	}

	/**
	 * Creates an ImageManager from the specified URL. The image will
	 * be preloaded by using MediaTracker to monitor the loaded state
	 * of the image.
	 */
	public ImageManager(URL location) {
		image = Toolkit.getDefaultToolkit().getImage(location);
		description = location.toExternalForm();
		loadImage(image);
	}

	/**
	 * Creates an ImageManager from the image. 
	 * @param image the image
	 * @param description a brief textual description of the image
	 */
	public ImageManager(Image image, String description) {
		this.image = image;
		this.description = description;
		loadImage(image);
	}

	/**
	 * Creates an ImageManager from the image. 
	 */
	public ImageManager(Image image) {
		this.image = image;
		Object o = image.getProperty("comment", imageObserver);
		if (o instanceof String) {
			description = (String) o;
		}
		loadImage(image);
	}

	/**
	 * Creates an uninitialized image icon.
	 */
	public ImageManager() {
	}

	/**
	 * Wait for the image to load
	 */
	protected void loadImage(Image image) {
		synchronized (tracker) {
			tracker.addImage(image, 0);
			try {
				tracker.waitForID(0);
			} catch (InterruptedException e) {
				System.out.println("INTERRUPTED while loading Image");
			}

			width = image.getWidth(imageObserver);
			height = image.getHeight(imageObserver);
		}
	}

	/**
	 * Returns the Icon's Image
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Set the image displayed by this icon.
	 */
	public void setImage(Image image) {
		this.image = image;
		loadImage(image);
	}

	/**
	 * Get the description of the image.  This is meant to be a brief
	 * textual description of the object.  For example, it might be
	 * presented to a blind user to give an indication of the purpose
	 * of the image.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set the description of the image.  This is meant to be a brief
	 * textual description of the object.  For example, it might be
	 * presented to a blind user to give an indication of the purpose
	 * of the image.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Paints the Icon
	 */
	public synchronized void paintIcon(Graphics g, int x, int y) {
		g.drawImage(image, x, y, imageObserver);
	}

	/**
	 * Paints the Icon
	 * @deprecated
	 */
	public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
		g.drawImage(image, x, y, imageObserver);
	}

	/**
	 * Paints the Icon
	 */
	public synchronized void paintIcon(
		Graphics g, int x, int y, int w, int h, Color bgcolor) {
		g.drawImage(image, x, y, w, h, bgcolor, imageObserver);
	}

	/**
	 * Paints the Icon
	 */
	public synchronized void paintIcon(
		Graphics g, int x, int y, int w, int h, int sx, int sy, int sw, int sh) {
		g.drawImage(image, x, y, w, h, sx, sy, sw, sh, imageObserver);
	}

	/**
	 * Paints the Icon
	 */
	public synchronized void paintIcon(
		Graphics g, int x, int y, int w, int h, int sx, int sy, int sw, int sh,
		Color bgcolor) {
		g.drawImage(image, x, y, w, h, sx, sy, sw, sh, bgcolor, imageObserver);
	}

	/**
	 * Get the width of the Icon
	 */
	public int getIconWidth() {
		return width;
	}

	/**
	 * Get the height of the Icon
	 */
	public int getIconHeight() {
		return height;
	}

	/** 
	 * Set the image observer for the image.  Set this
	 * property if the ImageManager contains an animated GIF.
	 * For example:
	 * <pre>
	 *     icon = new ImageManager(...)
	 *     button.setImage(icon);
	 *     icon.setImageObserver(button);
	 * </pre>
	 */
	public void setImageObserver(ImageObserver observer) {
		imageObserver = observer;
	}

	/**
	 *  Return the umage observer for the image 
	 */
	public ImageObserver getImageObserver() {
		return imageObserver;
	}

	/**
	 * Returns true if the specified URL locates a valid image, false otherwise.
	 * @param URL the URL to check
	 */
	public static boolean isValidImage(URL url) {
		if (url == null) {
			return false;
		}
		Component comp = new Component() {
		};
		Image img = comp.getToolkit().getImage(url);
		MediaTracker tracker = new MediaTracker(comp);
		tracker.addImage(img, 0);
		try {
			tracker.waitForID(0);
		} catch (InterruptedException e) {
			return false;
		}
		return !tracker.isErrorID(0);
	}

	private void readObject(ObjectInputStream s)
		throws ClassNotFoundException, IOException {
		s.defaultReadObject();

		int w = s.readInt();
		int h = s.readInt();
		int[] pixels = (int[]) (s.readObject());

		Toolkit tk = Toolkit.getDefaultToolkit();
		ColorModel cm = ColorModel.getRGBdefault();
		image = tk.createImage(new MemoryImageSource(w, h, cm, pixels, 0, w));
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();

		int w = getIconWidth();
		int h = getIconHeight();
		int[] pixels = new int[w * h];

		try {
			PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
			pg.grabPixels();
			if ((pg.getStatus() & ImageObserver.ABORT) != 0) {
				throw new IOException("failed to load image contents");
			}
		} catch (InterruptedException e) {
			throw new IOException("image load interrupted");
		}

		s.writeInt(w);
		s.writeInt(h);
		s.writeObject(pixels);
	}
}
