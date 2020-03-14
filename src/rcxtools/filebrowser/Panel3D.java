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
/* Copyright (c) 1998 by Groupe Bull. All Rights Reserved */
/* $Id: Panel3D.java,v 1.3 1998/09/30 13:21:57 tkormann Exp $ */
/* Author: Thierry.Kormann@sophia.inria.fr  */

//package fr.dyade.koala.lyptus.widgets;
package rcxtools.filebrowser;

import java.awt.*;
//import fr.dyade.koala.graphics.DoubleBuffering;

/**
 *
 * @author Thierry.Kormann@sophia.inria.fr 
 */
public class Panel3D extends Panel {

	boolean raised;
	boolean doubleBufferize;
	Insets insets = new Insets(2, 2, 2, 2);

	public Panel3D() {
		this(true, false);
	}

	public Panel3D(boolean raised) {
		this(raised, false);
	}

	public Panel3D(boolean raised, boolean doubleBufferize) {
		super();
		setLayout(new BorderLayout());
		this.raised = raised;
		this.doubleBufferize = doubleBufferize;
		setBackground(Color.lightGray);
	}

	public void paint(Graphics g) {
		Dimension dim = getSize();
		g.setColor(getBackground());
		g.draw3DRect(0, 0, dim.width - 1, dim.height - 1, raised);
		super.paint(g);
	}

	public Insets getInsets() {
		return insets;
	}

	public void setInsets(Insets insets) {
		this.insets = insets;
	}

	public void update(Graphics g) {
		if (doubleBufferize) {
			DoubleBuffering.update(this, g);
		} else {
			super.update(g);
		}
	}

}
