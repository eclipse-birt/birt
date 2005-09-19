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
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Line3DRenderEvent
 */
public final class Line3DRenderEvent extends LineRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = 33812052466380930L;

	private Location3D s3d, e3d;

	/**
	 * @param oSource
	 */
	public Line3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * @param start
	 */
	public void setStart3D( Location3D start )
	{
		s3d = start;
	}

	/**
	 * @return
	 */
	public Location3D getStart3D( )
	{
		return s3d;
	}

	/**
	 * @param start
	 */
	public void setEnd3D( Location3D end )
	{
		e3d = end;
	}

	/**
	 * @return
	 */
	public Location3D getEnd3D( )
	{
		return e3d;
	}

	/**
	 * Returns center of gravity of line
	 * 
	 * @return
	 */
	public Vector getCenter( )
	{
		if ( s3d == null || e3d == null )
		{
			return null;
		}

		return new Vector( ( s3d.getX( ) + e3d.getX( ) ) / 2,
				( s3d.getY( ) + e3d.getY( ) ) / 2,
				( s3d.getZ( ) + e3d.getZ( ) ) / 2,
				true );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		Line3DRenderEvent lre = new Line3DRenderEvent( source );
		if ( lia != null )
		{
			lre.setLineAttributes( (LineAttributes) EcoreUtil.copy( lia ) );
		}

		if ( s3d != null )
		{
			lre.setStart3D( (Location3D) EcoreUtil.copy( s3d ) );
		}

		if ( e3d != null )
		{
			lre.setEnd3D( (Location3D) EcoreUtil.copy( e3d ) );
		}
		return lre;
	}
}
