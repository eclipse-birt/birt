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

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

/**
 * This is the basic rendering event for each concrete rendering event to
 * extend.
 */
public abstract class PrimitiveRenderEvent extends ChartEvent implements
		Comparable
{

	/**
	 * A constant indicats a Drawing operation.
	 */
	public static final int DRAW = 1;

	/**
	 * A constant indicats a Filling operation.
	 */
	public static final int FILL = 2;

	/**
	 * An index value used internally. Note this is public only for
	 * cross-package internal access.
	 */
	public int iObjIndex = 0;

	private double dDepth = 0;

	/**
	 * Creates a Primitive Render Event from a source object. The source can be
	 * of any type. Inside the chart engine, it is a StructureSource object
	 * 
	 * @param oSource
	 *            The Source Object
	 * @see StructureSource
	 */
	public PrimitiveRenderEvent( Object oSource )
	{
		super( oSource );
	}

	/**
	 * Returns the mimimum bounds required to contain the rendering area for
	 * current event.
	 * 
	 * @return
	 * @throws ChartException
	 *             if not implemented by concrete class
	 */
	public Bounds getBounds( ) throws ChartException
	{
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.UNSUPPORTED_FEATURE,
				"exception.unsupported.bounds", //$NON-NLS-1$ 
				new Object[]{
					this
				},
				Messages.getResourceBundle( ) );
	}

	/**
	 * @return A copy of this primitive rendering instruction implemented by
	 *         subclasses
	 * 
	 * @throws UnsupportedFeatureException
	 */
	public PrimitiveRenderEvent copy( ) throws ChartException
	{
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.UNSUPPORTED_FEATURE,
				"exception.unsupported.copy", //$NON-NLS-1$ 
				new Object[]{
					this
				},
				Messages.getResourceBundle( ) );
	}

	/**
	 * Compare two bounds regularly.
	 * 
	 * @param bo1
	 * @param bo2
	 * @return
	 */
	public static final int compareRegular( Bounds bo1, Bounds bo2 )
	{
		final double dMinX1 = bo1.getLeft( );
		final double dMinX2 = bo2.getLeft( );
		double dDiff = dMinX1 - dMinX2;
		if ( dDiff != 0 )
		{
			return ( dDiff < 0 ) ? IConstants.LESS : IConstants.MORE;
		}
		else
		{
			final double dMaxX1 = bo1.getLeft( ) + bo1.getWidth( );
			final double dMaxX2 = bo2.getLeft( ) + bo2.getWidth( );
			dDiff = dMaxX1 - dMaxX2;
			if ( dDiff != 0 )
			{
				return ( dDiff < 0 ) ? IConstants.LESS : IConstants.MORE;
			}
			else
			{
				final double dMinY1 = bo1.getTop( );
				final double dMinY2 = bo2.getTop( );
				dDiff = dMinY1 - dMinY2;
				if ( dDiff != 0 )
				{
					return ( dDiff < 0 ) ? IConstants.MORE : IConstants.LESS;
				}
				else
				{
					final double dMaxY1 = bo1.getTop( ) + bo1.getHeight( );
					final double dMaxY2 = bo2.getTop( ) + bo2.getHeight( );
					dDiff = dMaxY1 - dMaxY2;
					if ( dDiff != 0 )
					{
						return ( dDiff < 0 ) ? IConstants.MORE
								: IConstants.LESS;
					}
					else
					{
						return IConstants.EQUAL;
					}
				}
			}
		}
	}

	/**
	 * Compare two bounds in transposed way.
	 * 
	 * @param bo1
	 * @param bo2
	 * @return
	 */
	public static final int compareTransposed( Bounds bo1, Bounds bo2 )
	{
		final double dMinY1 = bo1.getTop( );
		final double dMinY2 = bo2.getTop( );
		double dDiff = dMinY1 - dMinY2;
		if ( dDiff != 0 )
		{
			return ( dDiff < 0 ) ? IConstants.MORE : IConstants.LESS;
		}
		else
		{
			final double dMaxY1 = bo1.getTop( ) + bo1.getHeight( );
			final double dMaxY2 = bo2.getTop( ) + bo2.getHeight( );
			dDiff = dMaxY1 - dMaxY2;
			if ( dDiff != 0 )
			{
				return ( dDiff < 0 ) ? IConstants.MORE : IConstants.LESS;
			}
			else
			{
				final double dMinX1 = bo1.getLeft( );
				final double dMinX2 = bo2.getLeft( );
				dDiff = dMinX1 - dMinX2;
				if ( dDiff != 0 )
				{
					return ( dDiff < 0 ) ? IConstants.LESS : IConstants.MORE;
				}
				else
				{
					final double dMaxX1 = bo1.getLeft( ) + bo1.getWidth( );
					final double dMaxX2 = bo2.getLeft( ) + bo2.getWidth( );
					dDiff = dMaxX1 - dMaxX2;
					if ( dDiff != 0 )
					{
						return ( dDiff < 0 ) ? IConstants.LESS
								: IConstants.MORE;
					}
					else
					{
						return IConstants.EQUAL;
					}
				}
			}
		}
	}

	/**
	 * Compares two primitives in terms of Z-order rendering
	 */
	public int compareTo( Object o )
	{
		PrimitiveRenderEvent pre = null;
		if ( o instanceof WrappedInstruction )
		{
			pre = ( (WrappedInstruction) o ).getEvent( );
		}
		else if ( o instanceof PrimitiveRenderEvent )
		{
			pre = (PrimitiveRenderEvent) o;
		}
		else
		{
			throw new RuntimeException( new ChartException( ChartEnginePlugin.ID,
					ChartException.UNSUPPORTED_FEATURE,
					"exception.unsupported.comparison", //$NON-NLS-1$ 
					new Object[]{
						o
					},
					Messages.getResourceBundle( ) ) );
		}
		/*
		 * if (dDepth != pre.dDepth) { return (dDepth > pre.dDepth) ?
		 * IConstants.MORE : IConstants.LESS; }
		 */

		Bounds bo = null, boPre = null;
		try
		{
			bo = getBounds( );
			boPre = pre.getBounds( );
		}
		catch ( ChartException ufex )
		{
			throw new RuntimeException( ufex );
		}
		return compareRegular( bo, boPre );
	}

	/**
	 * Causes this instruction to 'draw' itself on the device renderer
	 * 
	 * @param idr
	 * @throws UnsupportedFeatureException
	 */
	public void draw( IDeviceRenderer idr ) throws ChartException
	{
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.UNSUPPORTED_FEATURE,
				"exception.unsupported.internal.draw", //$NON-NLS-1$ 
				new Object[]{
					this
				},
				Messages.getResourceBundle( ) );
	}

	/**
	 * Causes this instruction to 'fill' itself on the device renderer
	 * 
	 * @param idr
	 * @throws UnsupportedFeatureException
	 */
	public void fill( IDeviceRenderer idr ) throws ChartException
	{
		throw new ChartException( ChartEnginePlugin.ID,
				ChartException.UNSUPPORTED_FEATURE,
				"exception.unsupported.internal.fill", //$NON-NLS-1$ 
				new Object[]{
					this
				},
				Messages.getResourceBundle( ) );
	}

	/**
	 * Sets the depth of current event.
	 * 
	 * @param dDepth
	 */
	public final void setDepth( double dDepth )
	{
		this.dDepth = dDepth;
	}

	/**
	 * @return Returns the depth of current event.
	 */
	public final double getDepth( )
	{
		return dDepth;
	}
}