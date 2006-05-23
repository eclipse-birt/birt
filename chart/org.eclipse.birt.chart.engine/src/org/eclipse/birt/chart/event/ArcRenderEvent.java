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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * ArcRenderEvent
 */
public class ArcRenderEvent extends PrimitiveRenderEvent
{

	private static final long serialVersionUID = -8516218845415390970L;

	protected Location loTopLeft = null;

	protected double dWidth;

	protected double dHeight;

	protected double dStartInDegrees;

	protected double dExtentInDegrees;

	protected double dInnerRadius;

	protected double dOuterRadius;

	protected LineAttributes outline;

	protected Fill ifBackground = null;

	protected int iStyle = SECTOR;

	public static final int OPEN = 1;

	public static final int CLOSED = 2;

	public static final int SECTOR = 3;

	/**
	 * @param oSource
	 */
	public ArcRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#reset()
	 */
	public void reset( )
	{
		loTopLeft = null;
		dWidth = 0;
		dHeight = 0;
		dStartInDegrees = 0;
		dExtentInDegrees = 0;
		dInnerRadius = 0;
		dOuterRadius = 0;
		outline = null;
		ifBackground = null;
		iStyle = SECTOR;
	}

	/**
	 * @return Returns the arc style.
	 */
	public final int getStyle( )
	{
		return iStyle;
	}

	/**
	 * @param style
	 *            The arc style to set.
	 */
	public final void setStyle( int style )
	{
		iStyle = style;
	}

	/**
	 * @return Returns the top left co-ordinates of the bounding elliptical box
	 *         for the arc
	 */
	public final Location getTopLeft( )
	{
		return loTopLeft;
	}

	/**
	 * @param loTopLeft
	 *            The top left co-ordinates of the bounding elliptical box for
	 *            the arc
	 */
	public final void setTopLeft( Location loTopLeft )
	{
		this.loTopLeft = loTopLeft;
	}

	/**
	 * @return Returns the end arc angle.
	 */
	public final double getAngleExtent( )
	{
		return dExtentInDegrees;
	}

	/**
	 * 
	 * @param angleExtent The angle extent
	 * @since 2.1
	 */
	
	public final void setAngleExtent( double angleExtent )
	{
		this.dExtentInDegrees = angleExtent;
	}
	/**
	 * @param endAngle
	 *            The angle extent
	 * @deprecated in 2.1. Use setAngleExtent instead
	 * @see #setAngleExtent(double)
	 */
	public final void setEndAngle( double endAngle )
	{
		this.dExtentInDegrees = endAngle;
	}

	/**
	 * @return Returns the background.
	 */
	public final Fill getBackground( )
	{
		return ifBackground;
	}

	/**
	 * @param ifBackground
	 *            The background to set.
	 */
	public final void setBackground( Fill ifBackground )
	{
		this.ifBackground = ifBackground;
	}

	/**
	 * @return Returns the width.
	 */
	public double getWidth( )
	{
		return dWidth;
	}

	/**
	 * @param radius
	 *            The width to set.
	 */
	public void setWidth( double width )
	{
		this.dWidth = width;
	}

	/**
	 * @return Returns the height.
	 */
	public double getHeight( )
	{
		return dHeight;
	}

	/**
	 * @param radius
	 *            The height to set.
	 */
	public void setHeight( double height )
	{
		this.dHeight = height;
	}

	/**
	 * @return Returns the startAngle.
	 */
	public final double getStartAngle( )
	{
		return dStartInDegrees;
	}

	/**
	 * @param startAngle
	 *            The startAngle to set.
	 */
	public final void setStartAngle( double startAngle )
	{
		this.dStartInDegrees = startAngle;
	}

	/**
	 * 
	 * @param bo
	 */
	public final void setBounds( Bounds bo )
	{
		setTopLeft( LocationImpl.create( bo.getLeft( ), bo.getTop( ) ) );
		setWidth( bo.getWidth( ) );
		setHeight( bo.getHeight( ) );
	}

	/**
	 * 
	 * @return
	 */
	public Bounds getEllipseBounds( )
	{
		return BoundsImpl.create( loTopLeft.getX( ),
				loTopLeft.getY( ),
				dWidth,
				dHeight );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#getBounds()
	 */
	public Bounds getBounds( )
	{
		// use full bounds temporarialy
		return getEllipseBounds( );
		
		//TODO calculate the actual bounds.

		// double dMinY = -1, dMinX = -1, dMaxY = -1, dMaxX = -1;
		//
		// final double dStart = getStartAngle( );
		// final double dEnd = dStart + getAngleExtent( );
		// final int iQStart = getQuadrant( dStart );
		// final int iQEnd = getQuadrant( dEnd );
		//
		// double dXCosTheta = getWidth( )
		// / 2
		// * Math.cos( Math.toRadians( -dStart ) );
		// double dYSinTheta = getHeight( )
		// / 2
		// * Math.sin( Math.toRadians( -dStart ) );
		// double dX1 = ( loTopLeft.getX( ) + getWidth( ) / 2 ) + dXCosTheta;
		// double dY1 = ( loTopLeft.getY( ) + getHeight( ) / 2 ) + dYSinTheta;
		// dXCosTheta = getWidth( ) / 2 * Math.cos( Math.toRadians( -dEnd ) );
		// dYSinTheta = getHeight( ) / 2 * Math.sin( Math.toRadians( -dEnd ) );
		// double dX2 = loTopLeft.getX( ) + getWidth( ) / 2 + dXCosTheta;
		// double dY2 = loTopLeft.getY( ) + getHeight( ) / 2 + dYSinTheta;
		//
		// int iQMin = Math.min( iQStart, iQEnd );
		// int iQMax = Math.max( iQStart, iQEnd );
		//
		// // TEST QUADRANTS
		// for ( int i = iQMin; i < iQMax; i++ )
		// {
		// if ( i == 1 )
		// {
		// dMinY = loTopLeft.getY( );
		// }
		// else if ( i == 2 )
		// {
		// dMinX = loTopLeft.getX( );
		// }
		// else if ( i == 3 )
		// {
		// dMaxY = loTopLeft.getY( ) + getHeight( );
		// }
		// }
		//
		// dMaxX = Math.max( dX1, dX2 ); // MAX-X NEEDS TO BE DEFINED
		//
		// if ( dMinY != -1 )
		// {
		// dMinY = Math.min( dMinY, Math.min( dY1, dY2 ) );
		// }
		// else
		// // IF UNDEFINED DUE TO QUADRANT-1 SPAN
		// {
		// dMinY = Math.min( dY1, dY2 );
		// }
		//
		// if ( dMinX != -1 )
		// {
		// dMinX = Math.min( dMinX, Math.min( dX1, dX2 ) );
		// }
		// else
		// // IF UNDEFINED DUE TO QUADRANT-2 SPAN
		// {
		// dMinX = Math.min( dX1, dX2 );
		// }
		//
		// if ( dMaxY != -1 )
		// {
		// dMaxY = Math.max( dMaxY, Math.min( dY1, dY2 ) );
		// }
		// else
		// // IF UNDEFINED DUE TO QUADRANT-3 SPAN
		// {
		// dMaxY = Math.max( dY1, dY2 );
		// }
		//
		// if ( getStyle( ) == SECTOR ) // ALSO INCLUDE THE ARC CIRCLE CENTER
		// {
		// final double dCenterX = loTopLeft.getX( ) + dWidth / 2;
		// final double dCenterY = loTopLeft.getY( ) + dHeight / 2;
		// dMinX = Math.min( dCenterX, dMinX );
		// dMaxX = Math.max( dCenterX, dMaxX );
		// dMinY = Math.min( dCenterY, dMinY );
		// dMaxY = Math.max( dCenterY, dMaxY );
		// }
		// return BoundsImpl.create( dMinX, dMinY, dMaxX - dMinX, dMaxY - dMinY
		// );
	}

	// /**
	// *
	// * @param dAngle
	// * @return
	// */
	// private static final int getQuadrant( double dAngle )
	// {
	// if ( dAngle < 0 )
	// {
	// dAngle = 360 + dAngle;
	// }
	// if ( dAngle >= 0 && dAngle < 90 )
	// return 1;
	// if ( dAngle >= 90 && dAngle < 180 )
	// return 2;
	// if ( dAngle >= 180 && dAngle < 270 )
	// return 3;
	// else
	// return 4;
	// }

	/**
	 * @return Returns the outline.
	 */
	public final LineAttributes getOutline( )
	{
		return outline;
	}

	/**
	 * @param outline
	 *            The outline to set.
	 */
	public final void setOutline( LineAttributes outline )
	{
		this.outline = outline;
	}

	public double getInnerRadius( )
	{
		return dInnerRadius;
	}

	public void setInnerRadius( double innerRadius )
	{
		dInnerRadius = innerRadius;
	}

	public double getOuterRadius( )
	{
		return dOuterRadius;
	}

	public void setOuterRadius( double outerRadius )
	{
		dOuterRadius = outerRadius;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( ) throws ChartException
	{
		ArcRenderEvent are = new ArcRenderEvent( (StructureSource)source );
		if ( outline != null )
		{
			are.setOutline( LineAttributesImpl.copyInstance( outline ) );
		}

		if ( ifBackground != null )
		{
			are.setBackground( (Fill) EcoreUtil.copy( ifBackground ) );
		}

		if ( loTopLeft != null )
		{
			are.setTopLeft( LocationImpl.copyInstance( loTopLeft ) );
		}

		are.setStyle( iStyle );
		are.setWidth( dWidth );
		are.setHeight( dHeight );
		are.setStartAngle( dStartInDegrees );
		are.setEndAngle( dExtentInDegrees );
		are.setInnerRadius( dInnerRadius );
		are.setOuterRadius( dOuterRadius );

		return are;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart.device.IDeviceRenderer)
	 */
	public void draw( IDeviceRenderer idr ) throws ChartException
	{
		idr.drawArc( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart.device.IDeviceRenderer)
	 */
	public void fill( IDeviceRenderer idr ) throws ChartException
	{
		idr.fillArc( this );
	}
}
