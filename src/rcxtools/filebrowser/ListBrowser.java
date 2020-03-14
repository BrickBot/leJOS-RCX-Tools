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
/* $Id: ListBrowser.java,v 1.3 1999/02/17 16:10:03 tkormann Exp $ */
/* Author: Thierry.Kormann@sophia.inria.fr */

//package fr.dyade.koala.lyptus.widgets;
package rcxtools.filebrowser;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import java.net.URL;

//import fr.dyade.koala.beans.ImageManager;
//import fr.dyade.koala.graphics.DoubleBuffering;

/**
 * This class is a generic list browser.
 *
 * @author Thierry.Kormann@sophia.inria.fr 
 */
public class ListBrowser extends Panel implements AdjustmentListener {

	/** 
	 * This policy that lets just one node selected at the same time. 
	 */
	public static final int SINGLE = 0;
	/** 
	 * The policy that enables a multiple selection of nodes. 
	 */
	public static final int MULTIPLE = 1;

	/** The list entries. */
	protected Vector entries = new Vector();
	/** The selected list entries. */
	protected Vector selection = new Vector();

	Color selectColor = new Color(0, 0, 128);
	Color selectFontColor = Color.white;

	Scrollbar vscroll;
	int start;
	int incy = 16 + 4; // 16 is the size of the icon from java.beans.BeanInfo 
	FontMetrics fm;
	int selectionPolicy = SINGLE;
	ListBrowserHandler handler;

	/**
	 * Constructs a new <code>ListBrowser</code> with the specified handler.
	 * @param handler the list handler to manage actions
	 */
	public ListBrowser(ListBrowserHandler handler) {
		super(null);
		this.handler = handler;
		addMouseListener(new ListBrowserListener());
	}

	/**
	 * Sets the increment before each line to the specified value.
	 * @param inc the number of pixels before next line in the list
	 */
	public void setIncrement(int inc) {
		this.incy = inc;
	}

	/**
	 * Adds a new node with the specified url, name and client data.
	 * @param url the icon url
	 * @param surl the selected icon url
	 * @param name the entry name
	 * @param item the client data
	 */
	public void addNode(URL url, URL surl, String name, Object item) {
		entries.addElement(new ListNode(url, surl, name, item));
	}

	/**
	 * Adds a new node with the specified image, name and client data.
	 * @param image the icon
	 * @param simage the selected icon
	 * @param name the entry name
	 * @param item the client data
	 */
	public void addNode(Image image, Image simage, String name, Object item) {
		entries.addElement(new ListNode(image, simage, name, item));
	}

	public void addNotify() {
		super.addNotify();
		//fm = Toolkit.getDefaultToolkit().getFontMetrics(getFont());
		fm = this.getGraphics().getFontMetrics(getFont());
	}

	/**
	 * Sets the vertical scroll of this browser to the specified scroll bar.
	 * The Browser becomes an AdjustmentListener of this scrollbar.
	 * @param vscroll the scrollbar
	 */
	public void setVerticalScrollbar(Scrollbar vscroll) {
		this.vscroll = vscroll;
		vscroll.addAdjustmentListener(this);
	}

	public void setFont(Font font) {
		super.setFont(font);
		//fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
		fm = super.getFontMetrics(font);
	}

	public void adjustmentValueChanged(AdjustmentEvent evt) {
		if (evt.getSource() == vscroll) {
			start = evt.getValue();
		}
		repaint();
	}

	public void update(Graphics pg) {
		DoubleBuffering.update(this, pg);
	}

	public void paint(Graphics g) {
		updateScrollbar();
		Dimension dim = getSize();
		int y = 0;
		g.setFont(getFont());
		for (int i = start; i < entries.size() && y < dim.height - incy; ++i) {
			ListNode entry = (ListNode) entries.elementAt(i);
			if (entry.selected) {
				g.setColor(selectColor);
				g.fillRect(0, y + 1, dim.width - 1, incy);
				if (entry.smanager != null) {
					entry.smanager.paintIcon(g, 0, y + 2);
				}
				g.setColor(selectFontColor);
			} else {
				if (entry.imanager != null) {
					entry.imanager.paintIcon(g, 0, y + 2);
				}
				g.setColor(getForeground());
			}
			g.drawString(entry.name, 22, 2 + y + fm.getAscent());
			y += incy;
		}
	}

	/**
	 * Removes the specified node.
	 * @param node the node to remove
	 */
	public void removeNode(ListNode node) {
		entries.removeElement(node);
	}

	/**
	 * Removes all nodes.
	 */
	public void removeAllNodes() {
		entries.removeAllElements();
		selection.removeAllElements();
		start = 0;
		vscroll.setValue(0);
	}

	// ------------------------------------------------------------------------
	// selection
	// ------------------------------------------------------------------------

	/**
	 * Gets the number of selected node in this browser.
	 */
	public int getSelectedNodeCount() {
		return selection.size();
	}

	/**
	 * Gets the selected node at the specified index.
	 * @param index the index of the selected node
	 */
	public ListNode getSelectedNode(int index) {
		return (ListNode) selection.elementAt(index);
	}

	/**
	 * Unselects all selected component.
	 */
	public void unselectAll() {
		for (int i = 0; i < selection.size(); ++i) {
			((ListNode) selection.elementAt(i)).selected = false;
		}
		selection.removeAllElements();
	}

	/**
	 * Sets the selection policy.
	 * @param policy: SINGLE or MULTIPLE
	 */
	public void setSelectionPolicy(int policy) {
		selectionPolicy = policy;
	}

	/**
	 * Gets the selection policy.
	 */
	public int getSelectionPolicy() {
		return selectionPolicy;
	}

	/**
	 * Sets the color of a selected node to the specified color.
	 * @param color the color used to paint a selected node
	 */
	public void setSelectionFontColor(Color color) {
		this.selectFontColor = color;
	}

	/**
	 * Sets the background color of a selected node to the specified color.
	 * @param color the color used to paint the background of a selected node
	 */
	public void setSelectionBackgroudColor(Color color) {
		this.selectColor = color;
	}

	/**
	 * Sets the specified node selected or not, depending on the specified
	 * parameter.
	 * @param node the node
	 * @param state if true, the node is selected, false implies the node is
	 * unselected
	 */
	public void setSelected(ListNode node, boolean state) {
		if (node == null) {
			return;
		}
		if (selectionPolicy == SINGLE) {
			unselectAll();
		}
		if (node.selected && !state) {
			selection.removeElement(node);
		} else if (!node.selected && state) {
			selection.addElement(node);
		}
		node.selected = state;
	}

	private void updateScrollbar() {
		if (vscroll == null) {
			return;
		}
		int n = Math.max(1, getSize().height / incy);
		vscroll.setMaximum(entries.size());
		vscroll.setVisibleAmount(n);
		vscroll.setBlockIncrement(n);
		//vscroll.setPageIncrement(n);
	}

	private int getEntryAt(int x, int y) {
		int index = start + (y / incy);
		return (index < entries.size()) ? index : -1;
	}

	private class ListBrowserListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			int index = getEntryAt(e.getX(), e.getY());
			if (index < 0) {
				handler.notifyOutside(ListBrowser.this, e);
			} else {
				ListNode entry = (ListNode) entries.elementAt(index);
				if (e.getClickCount() == 2) {
					handler.notifyExecute(ListBrowser.this, entry);
				} else {
					handler.notifySelect(ListBrowser.this, entry, e);
				}
			}
		}
	}
}
