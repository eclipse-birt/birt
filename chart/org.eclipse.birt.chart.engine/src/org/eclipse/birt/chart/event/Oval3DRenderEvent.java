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
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Oval3DRenderEvent
 */
public class Oval3DRenderEvent extends OvalRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = 3249838045689532033L;



	private Object3D object3D;

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
		object3D = new Object3D( loa );
	}

	/**
	 * @return
	 */
	public Location3D[] getLocation3D( )
	{
		return object3D.getLocation3D( );
	}

	


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		final Oval3DRenderEvent ore = new Oval3DRenderEvent( source );
		if ( object3D != null )
		{
			ore.object3D = new Object3D( object3D );
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

	public Object3D getObject3D( )
	{
		return object3D;
	}
	public void prepare2D( double xOffset, double yOffset )
	{
		Location[] points = object3D.getPoints2D( xOffset, yOffset );
		setBounds( BoundsImpl.create( points[0].getX( ),
				points[0].getY( ),
				points[2].getX( ) - points[1].getX( ),
				points[0].getY( ) - points[1].getY( ) ) );
	}
}
