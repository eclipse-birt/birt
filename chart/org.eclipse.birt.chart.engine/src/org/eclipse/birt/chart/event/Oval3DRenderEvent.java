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

import org.eclipse.birt.chart.computation.Vector;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Oval3DRenderEvent
 */
public class Oval3DRenderEvent extends OvalRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = 3249838045689532033L;

	private Location3D[] points;

	private Vector center;

	/**
	 * @param oSource
	 */
	public Oval3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * @param loa
	 */
	public void setLocation3D( Location3D[] loa )
	{
		points = loa;
	}

	/**
	 * @return
	 */
	public Location3D[] getLocation3D( )
	{
		return points;
	}

	/**
	 * @param va
	 */
	public void updateCenter( Vector[] va )
	{
		if ( va == null || va.length == 0 )
		{
			return;
		}

		double m = va.length;
		double xs = 0, ys = 0, zs = 0;

		for ( int i = 0; i < m; i++ )
		{
			xs += va[i].get( 0 );
			ys += va[i].get( 1 );
			zs += va[i].get( 2 );
		}

		center = new Vector( xs / m, ys / m, zs / m, true );
	}

	/**
	 * Returns center of gravity of oval
	 * 
	 * @return
	 */
	public Vector getCenter( )
	{
		if ( center != null )
		{
			return center;
		}

		if ( points == null || points.length == 0 )
		{
			return null;
		}

		double m = points.length;
		double xs = 0, ys = 0, zs = 0;

		for ( int i = 0; i < m; i++ )
		{
			xs += points[i].getX( );
			ys += points[i].getY( );
			zs += points[i].getZ( );
		}

		return new Vector( xs / m, ys / m, zs / m, true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		final Oval3DRenderEvent ore = new Oval3DRenderEvent( source );
		if ( points != null )
		{
			final Location3D[] loa = new Location3D[this.points.length];
			for ( int i = 0; i < loa.length; i++ )
			{
				loa[i] = (Location3D) EcoreUtil.copy( points[i] );
			}
			ore.points = loa;
		}

		if ( _lia != null )
		{
			ore.setOutline( (LineAttributes) EcoreUtil.copy( _lia ) );
		}

		if ( _ifBackground != null )
		{
			ore.setBackground( (Fill) EcoreUtil.copy( _ifBackground ) );
		}
		return ore;
	}

}
