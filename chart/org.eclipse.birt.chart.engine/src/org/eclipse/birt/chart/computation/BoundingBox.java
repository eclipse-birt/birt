/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.computation;

/**
 * This class ...
 * 
 * @author Actuate Corporation
 */
public final class BoundingBox
{

	private double dX, dY;

	private double dWidth, dHeight;

	private double dHotPoint;

	public BoundingBox( int _iLabelLocation, double _dX, double _dY,
			double _dWidth, double _dHeight, double _dHotPoint )
	{
		dX = _dX;
		dY = _dY;
		dWidth = _dWidth;
		dHeight = _dHeight;
		dHotPoint = _dHotPoint;
	}

	public final double getHotPoint( )
	{
		return dHotPoint;
	}

	public final double getTop( )
	{
		return dY;
	}

	public final double getLeft( )
	{
		return dX;
	}

	public final double getWidth( )
	{
		return dWidth;
	}

	public final double getHeight( )
	{
		return dHeight;
	}

	public final void setLeft( double _dX )
	{
		dX = _dX;
	}

	public final void setTop( double _dY )
	{
		dY = _dY;
	}

	public final void scale( double dScale )
	{
		dX *= dScale;
		dY *= dScale;
		dWidth *= dScale;
		dHeight *= dScale;
		dHotPoint *= dScale;
	}
}