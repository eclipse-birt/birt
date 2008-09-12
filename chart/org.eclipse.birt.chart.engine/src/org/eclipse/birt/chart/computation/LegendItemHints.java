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

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;

/**
 * LegendItemHints
 */
public class LegendItemHints implements IConstants
{

	private final Point location;
	private final double width;
	private double height;
	private final String text;
	private final String extraText;
	private final double extraHeight;
	private final int type;
	private final int categoryIndex;
	private final SeriesDefinition seriesDefinition;
	private final Series series;

	public LegendItemHints( int type, Point loc, double width, double height,
			String text, int categoryIndex, SeriesDefinition seriesDefinition,
			Series series )
	{
		this( type,
				loc,
				width,
				height,
				text,
				0,
				null,
				categoryIndex,
				seriesDefinition,
				series );
	}

	public LegendItemHints( int type, Point loc, double width, double height,
			String text, double extraHeight, String extraText )
	{
		// not using the category index when color by series.
		this( type,
				loc,
				width,
				height,
				text,
				extraHeight,
				extraText,
				0,
				null,
				null );
	}

	// for group name
	public LegendItemHints( int type, Point loc, double width, double height,
			String text )
	{
		this( type, loc, width, height, text, 0, null, 0, null, null );
	}

	public LegendItemHints( int type, Point loc, double width, double height,
			String text, double extraHeight, String extraText,
			int categoryIndex, SeriesDefinition seriesDefinition, Series series )
	{
		this.type = type;
		this.location = loc;
		this.width = width;
		this.height = height;
		this.text = text;
		this.extraText = extraText;
		this.extraHeight = extraHeight;
		this.categoryIndex = categoryIndex;
		this.series = series;
		this.seriesDefinition = seriesDefinition;
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

	
	/**
	 * @return Returns the series.
	 */
	public Series getSeries( )
	{
		return series;
	}

	
	/**
	 * @return Returns the seriesDefinition.
	 */
	public SeriesDefinition getSeriesDefinition( )
	{
		return seriesDefinition;
	}

	
	/**
	 * @param height
	 *            The height to set.
	 */
	void setHeight( double height )
	{
		this.height = height;
	}

}
