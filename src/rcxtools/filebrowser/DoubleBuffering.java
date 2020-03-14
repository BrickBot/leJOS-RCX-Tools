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
/* Copyright (c) 1997 by Groupe Bull. Read the COPYRIGHT file. */
/* $Id: DoubleBuffering.java,v 2.6 1999/03/10 14:28:17 phk Exp $ */
/* Author: Jean-Michel.Leon@sophia.inria.fr */

//package fr.dyade.koala.graphics;
package rcxtools.filebrowser;

import java.awt.*;

/**
 *  A smart implementation of double buffering that shares one offscreen image.
 *  Set the property "DBdebug" to true to disable double buffering and
 *  debug drawings. 
 * <P>Double buffering works only with native container (Panel, Canvas, etc.).
 * To enable double buffering on a Panel, create a Panel like this:
 <pre>
 Panel panel = new Panel () {
      public void update(Graphics g) {
	DoubleBuffering.update(this, g);
      }
    };
 </pre>
 *
 *
 * @author Philippe.Kaplan@sophia.inria.fr
 * @version 1.1
 */
public class DoubleBuffering {

	static transient Image image = null;
	static transient Component root = null;
	static int width = 0;
	static int height = 0;

	/**
	 * The debug flag. Automatically set on init whether DBdebug property is 
	 * true. Default is false.
	 */
	static public boolean debug = false;
	static {
		if (System.getSecurityManager() == null
			&& // don't raise an exception
			System.getProperty("DBdebug","false").equals("true")) {
			debug = true;
		}
	}

	// needed by serialization (although i can't explain exactly why...)
	/**
	 * Discard the offscreen image.
	 */
	static void resetImage() {
		root = null;
		width = 0;
		height = 0;
		image = null;
	}

	static Image getImage(Component c, int w, int h) {
		//if ((width < w)	|| (height < h)	|| root.getPeer() == null) {
		if ((width < w)	|| (height < h)	|| !root.isDisplayable()) { // image is no longer available
			image = c.createImage(w, h);
			width = w;
			height = h;
			root = c;
		}
		return image;
	}

	/**
	 *  Call area.paint() on an offscreen copy of Graphics g.
	 *  Example of use in a Component:
	<pre>
	    public void update(Graphics g) {
	      DoubleBuffering.update(this, g);
	    }
	</pre>
	 */
	static public void update(Component area, Graphics g) {
		if (!debug) {
			Graphics offgc;
			Dimension d = area.getSize();
			Image offscreen = null;

			Rectangle r = g.getClipBounds();

			// create the offscreen buffer and associated Graphics
			offscreen = getImage(area, d.width, d.height);
			offgc = offscreen.getGraphics();
			if (r != null) {
				offgc.clipRect(r.x, r.y, r.width, r.height);
			}

			// clear the exposed area
			offgc.setColor(area.getBackground());
			offgc.fillRect(0, 0, d.width, d.height);

			offgc.setColor(area.getForeground());
			// do normal redraw
			area.paint(offgc);

			// transfer offscreen to window
			g.drawImage(offscreen, 0, 0, area);
		} else {
			Dimension d = area.getSize();
			g.setColor(toggle ? Color.lightGray : Color.gray);
			toggle = !toggle;
			g.fillRect(0, 0, d.width, d.height);
			g.setColor(area.getForeground());
			area.paint(g);
		}
	}

	static boolean toggle = true;
}
