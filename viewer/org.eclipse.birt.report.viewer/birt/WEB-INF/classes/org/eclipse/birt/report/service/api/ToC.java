/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

import java.util.List;

/**
 * Representation of a TOC TODO: Javadoc
 * 
 */
public class ToC {

	private List children;

	private String id;

	private String displayName;

	private String bookmark;

	public ToC(String id, String displayName, String bookmark) {
		this.id = id;
		this.displayName = displayName;
		this.bookmark = bookmark;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
	}

	public String getID() {
		return id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getBookmark() {
		return bookmark;
	}

}
