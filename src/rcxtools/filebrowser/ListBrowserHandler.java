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
/* $Id: ListBrowserHandler.java,v 1.1 1998/11/12 10:19:01 tkormann Exp $ */
/* Author: Thierry.Kormann@sophia.inria.fr */

//package fr.dyade.koala.lyptus.widgets;
package rcxtools.filebrowser;

import java.awt.event.MouseEvent;

/**
 *
 * @author Thierry.Kormann@sophia.inria.fr 
 */
public interface ListBrowserHandler {

	/**
	 * Notifies that a mouse click has been done and targets no node.
	 * @param browser the NodeBrowser sending the notification.
	 */
	public void notifyOutside(ListBrowser browser, MouseEvent e);

	/**
	 * Notifies that a node has to be selected.
	 * @param browser the NodeBrowser sending the notification.
	 * @param node the selected node
	 * @param e the mouse event that targets the selected node
	 */
	public void notifySelect(ListBrowser browser, ListNode node, MouseEvent e);

	/**
	 * Notifies that a node has to be executed.
	 * @param browser the NodeBrowser sending the notification.
	 * @param node the node to execute
	 */
	public void notifyExecute(ListBrowser browser, ListNode node);
}
