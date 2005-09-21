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
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Image3DRenderEvent
 */
public final class Image3DRenderEvent extends ImageRenderEvent implements
		I3DRenderEvent
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = -5027476689319210090L;

	private Location3D lo3d;

	private Vector center;

	/**
	 * @param oSource
	 */
	public Image3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * @param lo
	 */
	public void setLocation3D( Location3D lo )
	{
		this.lo3d = lo;
	}

	/**
	 * @return
	 */
	public Location3D getLocation3D( )
	{
		return lo3d;
	}

	/**
	 * @param va
	 */
	public void updateCenter( Vector[] va )
	{
		if ( va == null || va.length < 1 )
		{
			return;
		}

		center = new Vector( va[0].get( 0 ),
				va[0].get( 1 ),
				va[0].get( 2 ),
				true );
	}

	/**
	 * @return
	 */
	public Vector getCenter( )
	{
		if ( center != null )
		{
			return center;
		}

		if ( lo3d == null )
		{
			return null;
		}

		return new Vector( lo3d );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		Image3DRenderEvent ire = new Image3DRenderEvent( source );

		if ( lo3d != null )
		{
			ire.setLocation3D( (Location3D) EcoreUtil.copy( lo3d ) );
		}

		if ( img != null )
		{
			ire.setImage( (Image) EcoreUtil.copy( img ) );
		}

		ire.setPosition( pos );
		ire.setWidth( width );
		ire.setHeight( height );
		ire.setStretch( stretch );

		return ire;
	}

}
