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

package org.eclipse.birt.chart.event;

/**
 * StructureType
 */
public class StructureType
{

	/**
	 * Indicates an Unknown structure type.
	 */
	public static final StructureType UNKNOWN = new StructureType( "unknown" ); //$NON-NLS-1$

	/**
	 * Indicates a Plot structure type.
	 */
	public static final StructureType PLOT = new StructureType( "plot" ); //$NON-NLS-1$

	/**
	 * Indicates a Title structure type.
	 */
	public static final StructureType TITLE = new StructureType( "title" ); //$NON-NLS-1$

	/**
	 * Indicates a Legend structure type.
	 */
	public static final StructureType LEGEND = new StructureType( "legend" ); //$NON-NLS-1$

	/**
	 * Indicates a Legend Title structure type.
	 */
	public static final StructureType LEGEND_TITLE = new StructureType( "legendTitle" ); //$NON-NLS-1$

	/**
	 * Indicates a Legend Entry structure type.
	 */
	public static final StructureType LEGEND_ENTRY = new StructureType( "legendEntry" ); //$NON-NLS-1$

	/**
	 * Indicates an Axis structure type.
	 */
	public static final StructureType AXIS = new StructureType( "axis" ); //$NON-NLS-1$

	/**
	 * Indicates an Axis Title structure type.
	 */
	public static final StructureType AXIS_TITLE = new StructureType( "axisTitle" ); //$NON-NLS-1$

	/**
	 * Indicates an Axis Label structure type.
	 */
	public static final StructureType AXIS_LABEL = new StructureType( "axisLabel" ); //$NON-NLS-1$

	/**
	 * Indicates a Series structure type.
	 */
	public static final StructureType SERIES = new StructureType( "series" ); //$NON-NLS-1$

	/**
	 * Indicates a Series Title structure type.
	 */
	public static final StructureType SERIES_TITLE = new StructureType( "seriesTitle" ); //$NON-NLS-1$

	/**
	 * Indicates a Series Element structure type.
	 */
	public static final StructureType SERIES_ELEMENT = new StructureType( "seriesElement" ); //$NON-NLS-1$

	/**
	 * Indicates a Series Marker structure type.
	 */
	public static final StructureType SERIES_MARKER = new StructureType( "seriesMarker" ); //$NON-NLS-1$

	/**
	 * Indicates a Series DataPoint structure type.
	 */
	public static final StructureType SERIES_DATA_POINT = new StructureType( "seriesDataPoint" ); //$NON-NLS-1$

	/**
	 * Indicates a Series Fitting Curve structure type.
	 */
	public static final StructureType SERIES_FITTING_CURVE = new StructureType( "seriesFittingCurve" ); //$NON-NLS-1$

	/**
	 * Indicates a Marker Line structure type.
	 */
	public static final StructureType MARKER_LINE = new StructureType( "markerLine" ); //$NON-NLS-1$

	/**
	 * Indicates a Marker Range structure type.
	 */
	public static final StructureType MARKER_RANGE = new StructureType( "markerRange" ); //$NON-NLS-1$

	private String type;

	/**
	 * Prevent from instantiating.
	 * 
	 * @param type
	 */
	private StructureType( String type )
	{
		this.type = type;
	}

	/**
	 * Returns the type string.
	 * 
	 * @return
	 */
	public final String getTypeString( )
	{
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString( )
	{
		return "StructureType[" + type + "]"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
