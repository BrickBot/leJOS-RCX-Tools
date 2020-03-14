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

/**
 * Utility class to catch window close events on a target
 * window and actually dispose the window.
 */

//package fr.dyade.koala.util;
package rcxtools.filebrowser;

import java.awt.*;
import java.awt.event.*;

public class WindowCloser implements WindowListener {

	/**
	 * Create an adaptor to listen for window closing events
	 * on the given window and actually perform the close.
	 */

	public WindowCloser(Window w) {
		this(w, false);
	}

	/**
	 * Create an adaptor to listen for window closing events
	 * on the given window and actually perform the close.
	 * If "exitOnClose" is true we do a System.exit on close.
	 */

	public WindowCloser(Window w, boolean exitOnClose) {
		this.exitOnClose = exitOnClose;
		w.addWindowListener(this);
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		if (exitOnClose) {
			System.exit(0);
		}
		e.getWindow().dispose();
	}

	public void windowClosed(WindowEvent e) {
		if (exitOnClose) {
			System.exit(0);
		}
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	private boolean exitOnClose;
}
