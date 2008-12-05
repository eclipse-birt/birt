/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

public class HyperlinkInfo
{

	private int type;

	private String url, bookmark;

	private String tooltip;

	public static int BOOKMARK = 0;

	public static int HYPERLINK = 1;

	public static int DRILL = 2;

	public HyperlinkInfo( int type, String url, String toolTip )
	{
		this( type, url, null, toolTip );
	}

	public HyperlinkInfo( int type, String url, String bookmark, String toolTip )
	{
		this.type = type;
		this.url = url;
		this.bookmark = bookmark;
		this.tooltip = toolTip;
	}

	public String getUrl( )
	{
		return this.url;
	}

	public String getBookmark( )
	{
		return this.bookmark;
	}

	public String getTooltip( )
	{
		return this.tooltip;
	}

	public int getType( )
	{
		return this.type;
	}
}