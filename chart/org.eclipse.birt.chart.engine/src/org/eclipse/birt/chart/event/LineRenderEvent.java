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
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * LineRenderEvent
 */
public final class LineRenderEvent extends PrimitiveRenderEvent
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 7216549683820618553L;

	private Location loStart;

	private Location loEnd;

	private LineAttributes lia;

	/**
	 * @param oSource
	 */
	public LineRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * 
	 * @param _loStart
	 */
	public final void setStart( Location _loStart )
	{
		loStart = _loStart;
	}

	/**
	 * 
	 * @return
	 */
	public final Location getStart( )
	{
		return loStart;
	}

	/**
	 * 
	 * @param _loEnd
	 */
	public final void setEnd( Location _loEnd )
	{
		loEnd = _loEnd;
	}

	/**
	 * 
	 * @return
	 */
	public final Location getEnd( )
	{
		return loEnd;
	}

	/**
	 * 
	 * @param _lia
	 */
	public final void setLineAttributes( LineAttributes _lia )
	{
		lia = _lia;
	}

	/**
	 * 
	 * @return
	 */
	public final LineAttributes getLineAttributes( )
	{
		return lia;
	}

	/**
	 *  
	 */
	public final Bounds getBounds( )
	{
		final double dMinX = Math.min( loStart.getX( ), loEnd.getX( ) );
		final double dMaxX = Math.max( loStart.getX( ), loEnd.getX( ) );
		final double dMinY = Math.min( loStart.getY( ), loEnd.getY( ) );
		final double dMaxY = Math.max( loStart.getY( ), loEnd.getY( ) );
		return BoundsImpl.create( dMinX, dMinY, dMaxX - dMinX, dMaxY - dMinY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public final PrimitiveRenderEvent copy( )
	{
		LineRenderEvent lre = new LineRenderEvent( source );
		if ( lia != null )
		{
			lre.setLineAttributes( (LineAttributes) EcoreUtil.copy( lia ) );
		}

		if ( loStart != null )
		{
			lre.setStart( (Location) EcoreUtil.copy( loStart ) );
		}

		if ( loEnd != null )
		{
			lre.setEnd( (Location) EcoreUtil.copy( loEnd ) );
		}
		return lre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart.device.IDeviceRenderer)
	 */
	public final void draw( IDeviceRenderer idr ) throws ChartException
	{
		idr.drawLine( this );
	}
}