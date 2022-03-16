/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

	@Override
	public String getDisplayName() {
		return displayName;
	}

	@Override
	public String getBookmark() {
		return bookmark;
	}

	@Override
	public String getElementType() {
		return elementType;
	}

	@Override
	public int getBookmarkType() {
		return bookmarkType;
	}
}
