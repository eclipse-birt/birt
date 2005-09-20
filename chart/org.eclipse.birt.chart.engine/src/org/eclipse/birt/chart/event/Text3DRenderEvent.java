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
import org.eclipse.birt.chart.model.attribute.Bounds;
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

	private Location3D loc3d;

	private Vector center;

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
		this.loc3d = loc;
	}

	/**
	 * @return
	 */
	public Location3D getLocation3D( )
	{
		return loc3d;
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

	public Vector getCenter( )
	{
		if ( center != null )
		{
			return center;
		}

		if ( loc3d == null )
		{
			return null;
		}

		return new Vector( loc3d );
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
		if ( loc3d != null )
		{
			tre.setLocation3D( (Location3D) EcoreUtil.copy( loc3d ) );
		}
		if ( _taBlock != null )
		{
			tre.setBlockAlignment( (TextAlignment) EcoreUtil.copy( _taBlock ) );
		}
		return tre;
	}
}
