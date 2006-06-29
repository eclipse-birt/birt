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

import java.util.Iterator;

import org.eclipse.birt.chart.computation.Object3D;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A rendering event type for rendering 3D Area object.
 */
public class Area3DRenderEvent extends AreaRenderEvent implements
		I3DRenderEvent
{

	private static final long serialVersionUID = -308233971777301084L;

	/**
	 * The constructor.
	 */
	public Area3DRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#getObject3D()
	 */
	public Object3D getObject3D( ) throws ChartException
	{
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.UNSUPPORTED_FEATURE,
				"exception.unsupported.area3d.getOjbect3D", //$NON-NLS-1$ 
				new Object[]{
					this
				},
				Messages.getResourceBundle( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy( ) throws ChartException
	{
		Area3DRenderEvent are = new Area3DRenderEvent( source );

		if ( fill != null )
		{
			are.setBackground( (Fill) EcoreUtil.copy( fill ) );
		}

		if ( lia != null )
		{
			are.setOutline( LineAttributesImpl.copyInstance( lia ) );
		}

		for ( Iterator itr = alLinesAndArcs.iterator( ); itr.hasNext( ); )
		{
			are.add( ( (PrimitiveRenderEvent) itr.next( ) ).copy( ) );
		}

		return are;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.I3DRenderEvent#prepare2D(double,
	 *      double)
	 */
	public void prepare2D( double xOffset, double yOffset )
	{
		for ( int i = 0; i < getElementCount( ); i++ )
		{
			PrimitiveRenderEvent pre = getElement( i );

			if ( pre instanceof I3DRenderEvent )
			{
				( (I3DRenderEvent) pre ).prepare2D( xOffset, yOffset );
			}
		}
	}

}
