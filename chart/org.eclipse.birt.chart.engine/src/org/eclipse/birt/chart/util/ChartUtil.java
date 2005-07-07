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

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
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
	private static final double EPS = 1E-10;

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

}
