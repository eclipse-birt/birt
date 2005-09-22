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
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Text3DRenderEvent
 */
public final class Text3DRenderEvent extends TextRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = 3083777028665416663L;

	private Object3D object3D;

	/**
	 * @param oSource
	 */
	public Text3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * @param loc
	 */
	public void setLocation3D( Location3D loc )
	{
		this.object3D = new Object3D( loc );
	}

	/**
	 * @return
	 */
	public Location3D getLocation3D( )
	{
		return object3D.getLocation3D( )[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( )
	{
		Text3DRenderEvent tre = new Text3DRenderEvent( source );
		if ( _boBlock != null )
		{
			tre.setBlockBounds( (Bounds) EcoreUtil.copy( _boBlock ) );
		}
		tre.setAction( _iAction );
		tre.setTextPosition( _iTextPosition );
		if ( _la != null )
		{
			tre.setLabel( (Label) EcoreUtil.copy( _la ) );
		}
		if ( object3D != null )
		{
			tre.object3D = new Object3D( object3D );
		}
		if ( _taBlock != null )
		{
			tre.setBlockAlignment( (TextAlignment) EcoreUtil.copy( _taBlock ) );
		}
		return tre;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D( )
	{
		return object3D;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double, double)
	 */
	public void prepare2D( double xOffset, double yOffset )
	{
		Location[] points = object3D.getPoints2D( xOffset, yOffset );
		setLocation( points[0] );
	}
	
	public void reset( )
	{
		object3D.reset();
	}

}
