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

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Location;
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

	private Object3D object3D;

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
		object3D = new Object3D( lo );
	}

	/**
	 * @return
	 */
	public Location3D getLocation3D( )
	{
		return object3D.getLocation3D()[0];
	}




	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		Image3DRenderEvent ire = new Image3DRenderEvent( source );

		if ( object3D != null )
		{
			ire.object3D = new Object3D( object3D );
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

	public Object3D getObject3D( )
	{
		return object3D;
	}

	public void prepare2D( double xOffset, double yOffset )
	{
		Location[] points = object3D.getPoints2D( xOffset, yOffset );
		setLocation( points[ 0 ] );
		 
	}

}
