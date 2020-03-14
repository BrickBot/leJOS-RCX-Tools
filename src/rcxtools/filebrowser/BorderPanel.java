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
/* $Id: BorderPanel.java,v 1.1 1998/11/13 01:16:18 tkormann Exp $ */
/* Author: Thierry.Kormann@sophia.inria.fr */

//package fr.dyade.koala.lyptus.widgets;
package rcxtools.filebrowser;

import java.awt.*;


public class BorderPanel extends Panel {

	String title;
	FontMetrics fm;
	Color fgColor = Color.black;
	Color bgColor = Color.gray;
	int htitle;
	int wtitle;

	public BorderPanel(String title) {
		super();
		this.title = title;
	}

	public BorderPanel(LayoutManager layout) {
		this(layout, null);
	}

	public BorderPanel(LayoutManager layout, String title) {
		super(layout);
		this.title = title;
	}

	public void addNotify() {
		super.addNotify();
		//fm = Toolkit.getDefaultToolkit().getFontMetrics(getFont());
		fm = this.getGraphics().getFontMetrics(getFont());
		htitle = fm.getAscent() + fm.getDescent();
		wtitle = fm.stringWidth(title);
	}

	public void setFont(Font font) {
		super.setFont(font);
		//fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
		fm = super.getFontMetrics(font);
	}
	public void setForegroundColor(Color color) {
		fgColor = color;
	}
	public void setBackgroundColor(Color color) {
		bgColor = color;
	}

	public Insets getInsets() {
		return new Insets(4 + htitle, 4, 4, 4);
	}

	public void paint(Graphics g) {
		super.paint(g);
		Dimension dim = getSize();
		int incy = fm.getAscent() / 2;
		g.setColor(Color.white);
		g.drawRect(1, incy + 1, dim.width - 2, dim.height - 2 - incy);
		g.setColor(bgColor);
		g.drawRect(0, incy, dim.width - 2, dim.height - 2 - incy);
		g.setColor(getBackground());
		g.fillRect(10 - 2, 0, wtitle + 4, htitle);
		g.setColor(fgColor);
		g.drawString(title, 10, fm.getAscent());
	}

}
