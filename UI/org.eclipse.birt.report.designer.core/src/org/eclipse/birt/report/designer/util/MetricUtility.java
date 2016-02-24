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

import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
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

	public static final org.eclipse.swt.graphics.Point dpi = Display.getDefault( )
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

	/**
	 * Update dimension according to pixel size and unit
	 * 
	 * @param dim
	 *            dimension to update
	 * @param pixelSize
	 *            pixel size
	 * @throws SemanticException
	 */
	public static void updateDimension( DimensionHandle dim, double pixelSize )
			throws SemanticException
	{
		updateDimension( dim, pixelSize, dim.getUnits( ) );
	}

	/**
	 * Update dimension according to the pixel size and keep the existing unit.
	 * If unit is not set, use default unit.
	 * 
	 * @param dim
	 *            dimension to update
	 * @param pixelSize
	 *            pixel size
	 * @param targetUnit
	 * @throws SemanticException
	 */
	public static void updateDimension( DimensionHandle dim, double pixelSize,
			String targetUnit ) throws SemanticException
	{
		if ( targetUnit == null )
		{
			targetUnit = dim.getDefaultUnit( );
		}
		// Do not convert for pixel unit
		if ( DesignChoiceConstants.UNITS_PX.equals( targetUnit ) )
		{
			dim.setValue( new DimensionValue( pixelSize,
					DesignChoiceConstants.UNITS_PX ) );
		}
		else if ( DimensionUtil.isAbsoluteUnit( targetUnit ) )
		{
			// Keep the unit if it's absolute unit
			double inchSize = MetricUtility.pixelToPixelInch( (int) pixelSize );
			dim.setValue( DimensionUtil.convertTo( inchSize,
					DesignChoiceConstants.UNITS_IN,
					targetUnit ) );
		}
		else
		{
			// otherwise use inch
			double inchSize = MetricUtility.pixelToPixelInch( (int) pixelSize );
			dim.setValue( new DimensionValue( inchSize,
					DesignChoiceConstants.UNITS_IN ) );
		}
	}
}