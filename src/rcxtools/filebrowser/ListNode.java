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
/* $Id: ListNode.java,v 1.2 1998/11/13 01:16:18 tkormann Exp $ */
/* Author: Thierry.Kormann@sophia.inria.fr */

//package fr.dyade.koala.lyptus.widgets;
package rcxtools.filebrowser;

import java.awt.*;
import java.net.URL;

//import fr.dyade.koala.beans.ImageManager;

/**
 * The representation of a list entry of a ListBrowser. An ListNode is used
 * internally by the ListBrowser to store informations related to an entry.
 *
 * @author Thierry.Kormann@sophia.inria.fr 
 */
public class ListNode {

	ImageManager imanager;
	ImageManager smanager;
	String name;
	Object item;
	boolean selected = false;
	URL url;
	URL surl;

	/**
	 * Constructs a new <code>ListNode</code>.
	 * @param url the icon url
	 * @param surl the selected icon url
	 * @param name the entry name
	 * @param item the client data
	 */
	public ListNode(URL url, URL surl, String name, Object item) {
		this.url = url;
		if (url != null) {
			this.imanager = new ImageManager(url);
		}
		this.surl = surl;
		if (surl != null) {
			this.smanager = new ImageManager(surl);
		}
		this.name = name;
		this.item = item;
	}

	/**
	 * Constructs a new <code>ListNode</code> with the specified image, name
	 * and item.
	 * @param image the icon
	 * @param simage the selected icon
	 * @param name the entry name
	 * @param item the client data 
	 */
	public ListNode(Image image, Image simage, String name, Object item) {
		if (image != null) {
			this.imanager = new ImageManager(image);
		}
		if (simage != null) {
			this.imanager = new ImageManager(simage);
		}
		this.name = name;
		this.item = item;
	}

	/**
	 * Returns true if this node is selected, false otherwise.
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Gets the URL of this IconNode.
	 */
	public URL getIconURL() {
		return url;
	}

	/**
	 * Gets the item of this node.
	 */
	public Object getItem() {
		return item;
	}

	/**
	 * Gets the Image of this icon.
	 */
	public Image getIconImage() {
		return imanager.getImage();
	}
}
