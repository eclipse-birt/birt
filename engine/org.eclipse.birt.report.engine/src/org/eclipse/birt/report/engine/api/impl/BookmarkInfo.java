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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.engine.api.IBookmarkInfo;

public class BookmarkInfo implements IBookmarkInfo
{

	private String displayName;
	private String bookmark;
	private String elementType;

	public BookmarkInfo( String bookmark, String displayName, String elementType )
	{
		this.displayName = displayName;
		this.bookmark = bookmark;
		this.elementType = elementType;
	}

	public String getDisplayName( )
	{
		return displayName;
	}

	public String getBookmark( )
	{
		return bookmark;
	}

	public String getElementType( )
	{
		return elementType;
	}
}
