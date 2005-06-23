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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.IConstants;

/**
 * TransformationEvent
 */
public final class TransformationEvent extends PrimitiveRenderEvent
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -2322114654814388838L;

	public static final int UNDEFINED = IConstants.UNDEFINED;

	public static final int SCALE = 1;

	public static final int TRANSLATE = 2;

	public static final int ROTATE = 4;

	private int _iTransform = UNDEFINED;

	private double _dScale = 1.0;

	private double _dTranslateX = 0;

	private double _dTranslateY = 0;

	private double _dRotationInDegrees = 0;

	/**
	 * @param oSource
	 */
	public TransformationEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * 
	 * @param iTransform
	 */
	public final void setTransform( int iTransform )
	{
		_iTransform = iTransform;
	}

	/**
	 * 
	 * @return
	 */
	public final int getTransform( )
	{
		return _iTransform;
	}

	/**
	 * 
	 * @param dScale
	 */
	public final void setScale( double dScale )
	{
		_dScale = dScale;
	}

	/**
	 * 
	 * @return
	 */
	public final double getScale( )
	{
		return _dScale;
	}

	/**
	 * 
	 * @param dTranslateX
	 * @param dTranslateY
	 */
	public final void setTranslation( double dTranslateX, double dTranslateY )
	{
		_dTranslateX = dTranslateX;
		_dTranslateY = dTranslateY;
	}

	/**
	 * 
	 * @return
	 */
	public final double getTranslateX( )
	{
		return _dTranslateX;
	}

	/**
	 * 
	 * @return
	 */
	public final double getTranslateY( )
	{
		return _dTranslateY;
	}

	/**
	 * 
	 * @param dAngleInDegrees
	 */
	public final void setRotation( double dAngleInDegrees )
	{
		_dRotationInDegrees = dAngleInDegrees;
	}

	/**
	 * 
	 * @return
	 */
	public final double getRotation( )
	{
		return _dRotationInDegrees;
	}
}