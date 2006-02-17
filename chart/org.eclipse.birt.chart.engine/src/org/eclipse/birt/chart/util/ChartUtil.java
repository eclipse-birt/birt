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

package org.eclipse.birt.chart.util;

import java.util.Iterator;

import org.eclipse.birt.chart.computation.Point;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.internal.computations.Polygon;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.component.Label;

/**
 * Utility class for Charts.
 */

public class ChartUtil
{

	/**
	 * Precision for chart rendering. Increase this to avoid unnecessary
	 * precision check.
	 */
	private static final double EPS = 1E-9;

	/**
	 * Returns if the given color definition is totally transparent. e.g.
	 * transparency==0.
	 * 
	 * @param cdef
	 * @return
	 */
	public static final boolean isColorTransparent( ColorDefinition cdef )
	{
		return cdef == null
				|| ( cdef.isSetTransparency( ) && cdef.getTransparency( ) == 0 );
	}

	/**
	 * Returns if the given label defines a shadow.
	 * 
	 * @param la
	 * @return
	 */
	public static final boolean isShadowDefined( Label la )
	{
		return !isColorTransparent( la.getShadowColor( ) );
	}

	/**
	 * Returns if the given two double values are equal within a small
	 * precision, e.g. EPS=1E-10.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static final boolean mathEqual( double v1, double v2 )
	{
		return Math.abs( v1 - v2 ) < EPS;
	}

	/**
	 * Returns if the given left double value is less than the given right value
	 * within a small precision, e.g. EPS=1E-10.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static final boolean mathLT( double lv, double rv )
	{
		return ( rv - lv ) > EPS;
	}

	/**
	 * Returns if the given left double value is greater than the given right
	 * value within a small precision, e.g. EPS=1E-10.
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static final boolean mathGT( double lv, double rv )
	{
		return ( lv - rv ) > EPS;
	}

	/**
	 * Convert pixel value to points.
	 * 
	 * @param idsSWT
	 * @param dOriginalHeight
	 * @return
	 */
	public static final double convertPixelsToPoints(
			final IDisplayServer idsSWT, double dOriginalHeight )
	{
		return ( dOriginalHeight * 72d ) / idsSWT.getDpiResolution( );
	}

	/**
	 * Returns the quadrant (1-4) for given angle in degree. Specially, -1 means
	 * Zero degree. -2 means 90 degree, -3 means 180 degree, -4 means 270
	 * degree.
	 * 
	 * @param dAngle
	 * @return
	 */
	public static final int getQuadrant( double dAngle )
	{
		dAngle = dAngle - ( ( (int) dAngle ) / 360 ) * 360;

		if ( dAngle < 0 )
		{
			dAngle += 360;
		}
		if ( dAngle == 0 )
		{
			return -1;
		}
		if ( dAngle == 90 )
		{
			return -2;
		}
		if ( dAngle == 180 )
		{
			return -3;
		}
		if ( dAngle == 270 )
		{
			return -4;
		}
		if ( dAngle >= 0 && dAngle < 90 )
		{
			return 1;
		}
		if ( dAngle > 90 && dAngle < 180 )
		{
			return 2;
		}
		if ( dAngle > 180 && dAngle < 270 )
		{
			return 3;
		}
		else
		{
			return 4;
		}
	}

	/**
	 * Returns if two polygons intersect.
	 * 
	 * @param pg1
	 * @param pg2
	 * @return
	 */
	public static boolean intersects( Polygon pg1, Polygon pg2 )
	{
		// TODO this is just a simple implementation for clip test. May only
		// works for convex polygon.

		boolean started = false;
		boolean diff = false, oldDiff = false;

		for ( Iterator itr = pg2.getPoints( ).iterator( ); itr.hasNext( ); )
		{
			Point pt = (Point) itr.next( );

			diff = pg1.contains( pt );

			if ( !started )
			{
				started = true;
				oldDiff = diff;
			}

			if ( diff != oldDiff )
			{
				return true;
			}

			oldDiff = diff;
		}

		if ( diff )
		{
			return true;
		}

		if ( pg1.getPoints( ).size( ) > 0 )
		{
			return pg2.contains( pg1.getPoint( 0 ) );
		}

		return false;
	}

	/**
	 * Merges two fonts to the original one from a source. The original one can
	 * not be null. Only consider inheritable properties.
	 * 
	 * @param original
	 * @param source
	 * @return
	 */
	public static void mergeFont( FontDefinition original, FontDefinition source )
	{
		if ( source != null )
		{
			if ( original.getAlignment( ) == null )
			{
				original.setAlignment( source.getAlignment( ) );
			}
			else if ( !original.getAlignment( ).isSetHorizontalAlignment( )
					&& source.getAlignment( ) != null )
			{
				original.getAlignment( )
						.setHorizontalAlignment( source.getAlignment( )
								.getHorizontalAlignment( ) );
			}
			if ( original.getName( ) == null )
			{
				original.setName( source.getName( ) );
			}
			if ( !original.isSetBold( ) )
			{
				original.setBold( source.isBold( ) );
			}
			if ( !original.isSetItalic( ) )
			{
				original.setItalic( source.isItalic( ) );
			}
			if ( !original.isSetRotation( ) )
			{
				original.setRotation( source.getRotation( ) );
			}
			if ( !original.isSetSize( ) )
			{
				original.setSize( source.getSize( ) );
			}
			if ( !original.isSetWordWrap( ) )
			{
				original.setWordWrap( source.isWordWrap( ) );
			}
			if ( !original.isSetUnderline( ) )
			{
				original.setUnderline( source.isUnderline( ) );
			}
			if ( !original.isSetStrikethrough( ) )
			{
				original.setStrikethrough( source.isStrikethrough( ) );
			}
		}
	}

	/**
	 * Returns the string representation for given object. null for null object.
	 * 
	 * @param value
	 * @return
	 */
	public static String stringValue( Object value )
	{
		if ( value == null )
		{
			return null;
		}

		return String.valueOf( value );
	}
}
