/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

/**
 * LegendItemHints
 */
public class LegendItemHints implements IConstants
{

	private final Point location;
	private final double width;
	private final double height;
	private final String text;
	private final String extraText;
	private final double extraHeight;
	private final int type;
	private final int categoryIndex;

	public LegendItemHints( int type, Point loc, double width, double height,
			String text, int categoryIndex )
	{
		this( type, loc, width, height, text, 0, null, categoryIndex );
	}

	public LegendItemHints( int type, Point loc, double width, double height,
			String text, double extraHeight, String extraText )
	{
		// not using the category index when color by series.
		this( type, loc, width, height, text, extraHeight, extraText, 0 );
	}

	public LegendItemHints( int type, Point loc, double width, double height,
			String text, double extraHeight, String extraText, int categoryIndex )
	{
		this.type = type;
		this.location = loc;
		this.width = width;
		this.height = height;
		this.text = text;
		this.extraText = extraText;
		this.extraHeight = extraHeight;
		this.categoryIndex = categoryIndex;
	}

	public int getType( )
	{
		return type;
	}

	/**
	 * This location is relative to the legend bound area (not include the
	 * legend title).
	 * 
	 * @return
	 */
	public Point getLocation( )
	{
		return location;
	}

	public double getWidth( )
	{
		return width;
	}

	public double getHeight( )
	{
		return height;
	}

	public double getLeft( )
	{
		if ( location != null )
		{
			return location.getX( );
		}

		return 0;
	}

	public double getTop( )
	{
		if ( location != null )
		{
			return location.getY( );
		}

		return 0;
	}

	public String getText( )
	{
		return text;
	}

	public String getExtraText( )
	{
		return extraText;
	}

	public double getExtraHeight( )
	{
		return extraHeight;
	}

	public int getCategoryIndex( )
	{
		return categoryIndex;
	}
}
