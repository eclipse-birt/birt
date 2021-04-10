/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.util.IBookmarkInfo;

public class BookmarkInfo implements IBookmarkInfo {

	private String displayName;
	private String bookmark;
	private String elementType;
	private int bookmarkType = IBookmarkInfo.CONSTANTS_TYPE;

	public BookmarkInfo(String bookmark, String displayName, String elementType) {
		this.displayName = displayName;
		this.bookmark = bookmark;
		this.elementType = elementType;
	}

	public BookmarkInfo(String bookmark, String displayName, String elementType, int bookmarkType) {
		this(bookmark, displayName, elementType);
		this.bookmarkType = bookmarkType;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getBookmark() {
		return bookmark;
	}

	public String getElementType() {
		return elementType;
	}

	public int getBookmarkType() {
		return bookmarkType;
	}
}
