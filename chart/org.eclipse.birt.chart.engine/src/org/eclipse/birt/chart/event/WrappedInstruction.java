/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.render.DeferredCache;

import com.ibm.icu.util.ULocale;



/**
 * This class wraps different types of rendering events. It could stand for one
 * or a list of events.
 */
public final class WrappedInstruction implements IRenderInstruction
{

	private final DeferredCache dc;

	private final int iInstruction;

	private ArrayList alEvents = null;

	private PrimitiveRenderEvent pre = null;
	
	private long zorder = 0;

	/**
	 * The constructor.
	 */
	public WrappedInstruction( DeferredCache dc, ArrayList alEvents,
			int iInstruction, long zorder )
	{
		this.dc = dc;
		this.alEvents = alEvents;
		this.iInstruction = iInstruction;
		this.zorder = zorder;
	}

	public WrappedInstruction( DeferredCache dc, ArrayList alEvents,
			int iInstruction )
	{
		this( dc, alEvents, iInstruction, 0);
	}

	/**
	 * The constructor.
	 */
	public WrappedInstruction( DeferredCache dc, PrimitiveRenderEvent pre,
			int iInstruction, long zorder )
	{
		this.dc = dc;
		this.pre = pre;
		this.iInstruction = iInstruction;
		this.zorder = zorder;
	}

	public WrappedInstruction( DeferredCache dc, PrimitiveRenderEvent pre,
			int iInstruction )
	{
		this( dc, pre, iInstruction, 0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo( Object o )
	{
		Bounds bo = null;

		if ( o instanceof PrimitiveRenderEvent )
		{
			try
			{
				bo = ( (PrimitiveRenderEvent) o ).getBounds( );
			}
			catch ( ChartException e )
			{
				assert false;
				return -1;
			}
		}
		else if ( o instanceof IRenderInstruction )
		{
			bo = ( (IRenderInstruction) o ).getBounds( );
			
			if (o instanceof WrappedInstruction)
			{
				long zorder_that = ( (WrappedInstruction) o ).zorder;
				if (this.zorder < zorder_that)
				{
					return -1;
				}
				else if (this.zorder > zorder_that)
				{
					return 1;
				}
			}
		}

		
		return ( dc != null && dc.isTransposed( ) ) ? PrimitiveRenderEvent.compareTransposed( getBounds( ),
				bo )
				: PrimitiveRenderEvent.compareRegular( getBounds( ), bo );
	}

	/**
	 * Returns the associated event.
	 * 
	 * @return
	 */
	public final PrimitiveRenderEvent getEvent( )
	{
		return pre;
	}

	/**
	 * @return Returns the associated instruction. The value could be one of
	 *         these:
	 *         <ul>
	 *         <li>PrimitiveRenderEvent.DRAW
	 *         <li>PrimitiveRenderEvent.FILL
	 *         </ul>
	 */
	public final int getInstruction( )
	{
		return iInstruction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString( )
	{
		return Messages.getString( "wrapped.instruction.to.string", //$NON-NLS-1$ 
				new Object[]{
						super.toString( ),
						Boolean.valueOf( isModel( ) ),
						getBounds( )
				},
				ULocale.getDefault( ) );
	}

	/**
	 * @return Returns the mimimum bounds required to contain the rendering area
	 *         of associated rendering event.
	 */
	public final Bounds getBounds( )
	{
		if ( !isModel( ) )
		{
			try
			{
				return pre.getBounds( );
			}
			catch ( Exception ex )
			{
				ex.printStackTrace( );
			}
		}
		else
		{
			Bounds bo = null;
			for ( int i = 0; i < alEvents.size( ); i++ )
			{
				try
				{
					if ( i == 0 )
					{
						bo = BoundsImpl.copyInstance( ( (PrimitiveRenderEvent) alEvents.get( i ) ).getBounds( ) );
					}
					else
					{
						( (BoundsImpl) bo ).max( ( (PrimitiveRenderEvent) alEvents.get( i ) ).getBounds( ) );
					}
				}
				catch ( Exception ex )
				{
					ex.printStackTrace( );
				}
			}
			return bo;
		}
		return null;
	}

	/**
	 * @return Returns if wraps multiple events currently.
	 */
	public boolean isModel( )
	{
		return pre == null;
	}

	/**
	 * @return Returns list of events currently wraps.
	 */
	public List getModel( )
	{
		return alEvents;
	}

	
	public long getZOrder( )
	{
		return zorder;
	}

	
	public void setZOrder( int zorder )
	{
		this.zorder = zorder;
	}
	
	public static Comparator<?> getDefaultComarator( )
	{
		return new WIComparator( );
	}

	private static class WIComparator implements Comparator<Object>
	{

		private long getZOrder( Object o )
		{
			if ( o instanceof WrappedInstruction )
			{
				return ( (WrappedInstruction) o ).getZOrder( );
			}
			else
			{
				return 0;
			}
		}

		public int compare( Object o1, Object o2 )
		{
			return Long.valueOf( getZOrder( o1 ) ).compareTo( getZOrder( o2 ) );
		}
	}

}

