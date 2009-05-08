/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IStyle;
import org.w3c.dom.css.CSSValue;

public class BackgroundImageInfo extends AreaConstants
{



	protected int xOffset;
	protected int yOffset;
	protected int repeatedMode;
	protected int height;
	protected int width;


	protected String url;

	public BackgroundImageInfo( String url, int repeatedMode, int xOffset,
			int yOffset, int height, int width)
	{
		this.url = url;
		this.repeatedMode = repeatedMode;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.width = width;
		this.height = height;
	}

	public BackgroundImageInfo( BackgroundImageInfo bgi )
	{
		this.url = bgi.url;
		this.repeatedMode = bgi.repeatedMode;
		this.xOffset = bgi.xOffset;
		this.yOffset = bgi.yOffset;
		this.width = bgi.width;
		this.height = bgi.height;
	}

	public BackgroundImageInfo( String url, CSSValue mode, int xOffset,
			int yOffset, int height, int width)
	{
		this( url, repeatMap.get( mode ), xOffset, yOffset, height, width );
	}

	public BackgroundImageInfo( String url, int height, int width )
	{
		this( url, 0, 0, 0, height, width );
	}

	public int getXOffset( )
	{
		return xOffset;
	}

	public void setYOffset( int y )
	{
		this.yOffset = y;
	}
	

	public void setXOffset( int x )
	{
		this.xOffset = x;
	}

	public int getYOffset( )
	{
		return yOffset;
	}

	
	public int getHeight( )
	{
		return height;
	}

	
	public void setHeight( int height )
	{
		this.height = height;
	}

	
	public int getWidth( )
	{
		return width;
	}

	
	public void setWidth( int width )
	{
		this.width = width;
	}

	public int getRepeatedMode( )
	{
		return repeatedMode;
	}

	public String getUrl( )
	{
		return url;
	}
}
