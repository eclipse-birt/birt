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

package org.eclipse.birt.report.designer.util;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.widgets.Display;

/**
 * Converts measure from inch to pixel
 */

public class MetricUtility
{

	/**
	 * the horizontal and vertical DPI
	 */

	public static org.eclipse.swt.graphics.Point dpi = Display.getDefault( )
			.getDPI( );

	/**
	 * Transforms the inch to pixel
	 * 
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 * @return a new point
	 */

	public static Point inchToPixel( double x, double y )
	{
		int xpixel = (int) ( inchToPixel( x ) );
		int ypixel = (int) ( inchToPixel( y ) );
		return new Point( xpixel, ypixel );
	}

	/**
	 * Transforms the inch to pixel
	 * 
	 * @param x
	 * @return pixel value
	 */
	public static double inchToPixel( double x )
	{
		return ( x * dpi.x );
	}

	/**
	 * Transforms the pixel to inch
	 * 
	 * @param x
	 * @return pixel value
	 */
	public static double pixelToPixelInch( int x )
	{
		return ( ( (double) x ) / dpi.x );
	}

}