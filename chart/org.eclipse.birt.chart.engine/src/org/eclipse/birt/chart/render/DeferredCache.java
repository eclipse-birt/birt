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

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.I3DRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;

/**
 * This class implements deferred rendering capability for chart.
 */
public final class DeferredCache
{
	public static final int FLUSH_PLANE = 1;
	public static final int FLUSH_LINE = 2;
	public static final int FLUSH_MARKER = 4;
	public static final int FLUSH_LABLE = 8;
	public static final int FLUSH_3D = 16;

	private final IDeviceRenderer idr;

	private final ArrayList alPlanes = new ArrayList( 16 );

	private final ArrayList alLines = new ArrayList( 16 );

	private final ArrayList alMarkers = new ArrayList( 16 );

	private final ArrayList alLabels = new ArrayList( 16 );

	private List al3D = new ArrayList( 16 );

	private final boolean bTransposed;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/factory" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public DeferredCache( IDeviceRenderer idr, Chart c )
	{
		this.idr = idr;
		bTransposed = ( c instanceof ChartWithAxes && ( (ChartWithAxes) c ).isTransposed( ) );
	}

	/**
	 * Addes rendering event to cache.
	 * 
	 * @param pre
	 *            As of now, supported types are RectanguleRenderEvent and
	 *            PolygonRenderEvent
	 */
	public final void addPlane( PrimitiveRenderEvent pre, int iInstruction )
	{
		try
		{
			if ( pre instanceof I3DRenderEvent )
			{
				al3D.add( new WrappedInstruction( this,
						pre.copy( ),
						iInstruction ) );
			}
			else
			{
				alPlanes.add( new WrappedInstruction( this,
						pre.copy( ),
						iInstruction ) );
			}
		}
		catch ( ChartException ufex )
		{
			logger.log( ufex );
		}
	}

	/**
	 * Adds wrapped rendering event to cache. Never use this for 3D rendering
	 * event.
	 */
	public final void addModel( WrappedInstruction wi )
	{
		alPlanes.add( wi );
	}

	/**
	 * Adds line rendering event to cache.
	 */
	public final void addLine( LineRenderEvent lre )
	{
		if ( lre instanceof I3DRenderEvent )
		{
			if ( lre.getLineAttributes( ) != null
					&& lre.getLineAttributes( ).isSetVisible( )
					&& lre.getLineAttributes( ).isVisible( ) )
			{
				al3D.add( lre.copy( ) );
			}
		}
		else
		{
			alLines.add( lre.copy( ) );
		}
	}

	/**
	 * Adds text rendering event to cache.
	 */
	public final void addLabel( TextRenderEvent tre )
	{
		if ( tre instanceof I3DRenderEvent )
		{
			al3D.add( tre.copy( ) );
		}
		else
		{
			alLabels.add( tre.copy( ) );
		}
	}

	/**
	 * Adds marker rendering event to cache.
	 */
	public final void addMarker( PrimitiveRenderEvent pre, int iInstruction )
	{
		try
		{
			if ( pre instanceof I3DRenderEvent )
			{
				al3D.add( new WrappedInstruction( this,
						pre.copy( ),
						iInstruction ) );
			}
			else
			{
				alMarkers.add( new WrappedInstruction( this,
						pre.copy( ),
						iInstruction ) );
			}
		}
		catch ( ChartException ufex )
		{
			logger.log( ufex );
		}
	}
	
	/**
	 * Flush the cache, perform all pending rendering tasks.
	 */
	public final void flush( ) throws ChartException
	{
		flushOptions( FLUSH_3D
				| FLUSH_LABLE | FLUSH_LINE | FLUSH_MARKER | FLUSH_PLANE );
	}

	/**
	 * Flush the cache of specified types.
	 * 
	 * @param options
	 *            types
	 * @see #FLUSH_3D
	 * @see #FLUSH_LABLE
	 * @see #FLUSH_LINE
	 * @see #FLUSH_MARKER
	 * @see #FLUSH_PLANE
	 * @since 2.2
	 */
	public final void flushOptions( int options ) throws ChartException
	{
		WrappedInstruction wi;

		// FLUSH PLANES
		if ( ( options & FLUSH_PLANE ) == FLUSH_PLANE )
		{
			Collections.sort( alPlanes ); // SORT ON Z-ORDER
			for ( int i = 0; i < alPlanes.size( ); i++ )
			{
				wi = (WrappedInstruction) alPlanes.get( i );
				if ( wi.isModel( ) )
				{
					ArrayList al = wi.getModel( );
					for ( int j = 0; j < al.size( ); j++ )
					{
						PrimitiveRenderEvent pre = (PrimitiveRenderEvent) al.get( j );
						pre.fill( idr );
						pre.draw( idr );
					}
				}
				else
				{
					wi.getEvent( ).iObjIndex = i + 1;
					switch ( wi.getInstruction( ) )
					{
						case PrimitiveRenderEvent.FILL
								| PrimitiveRenderEvent.DRAW :
							wi.getEvent( ).fill( idr );
							wi.getEvent( ).draw( idr );
							break;
						case PrimitiveRenderEvent.FILL :
							wi.getEvent( ).fill( idr );
							break;
						case PrimitiveRenderEvent.DRAW :
							wi.getEvent( ).draw( idr );
							break;
					}
				}
			}
			alPlanes.clear( );
		}

		// FLUSH LINES (WITHOUT SORTING)
		if ( ( options & FLUSH_LINE ) == FLUSH_LINE )
		{
			for ( int i = 0; i < alLines.size( ); i++ )
			{
				LineRenderEvent lre = (LineRenderEvent) alLines.get( i );
				lre.draw( idr );
			}
			alLines.clear( );
		}

		// FLUSH MARKERS (WITHOUT SORTING)
		if ( ( options & FLUSH_MARKER ) == FLUSH_MARKER )
		{
			for ( int i = 0; i < alMarkers.size( ); i++ )
			{
				wi = (WrappedInstruction) alMarkers.get( i );
				switch ( wi.getInstruction( ) )
				{
					case PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW :
						wi.getEvent( ).fill( idr );
						wi.getEvent( ).draw( idr );
						break;
					case PrimitiveRenderEvent.FILL :
						wi.getEvent( ).fill( idr );
						break;
					case PrimitiveRenderEvent.DRAW :
						wi.getEvent( ).draw( idr );
						break;
				}
			}
			alMarkers.clear( );
		}

		// FLUSH TEXT (WITHOUT SORTING)
		if ( ( options & FLUSH_LABLE ) == FLUSH_LABLE )
		{
			for ( int i = 0; i < alLabels.size( ); i++ )
			{
				TextRenderEvent tre = (TextRenderEvent) alLabels.get( i );
				tre.draw( idr );
			}
			alLabels.clear( );
		}

		// FLUSH 3D events (already z-sorted)
		if ( ( options & FLUSH_3D ) == FLUSH_3D )
		{
			for ( int i = 0; i < al3D.size( ); i++ )
			{
				Object obj = al3D.get( i );

				if ( obj instanceof WrappedInstruction )
				{
					wi = (WrappedInstruction) obj;

					if ( wi.isModel( ) )
					{
						ArrayList al = wi.getModel( );
						for ( int j = 0; j < al.size( ); j++ )
						{
							PrimitiveRenderEvent pre = (PrimitiveRenderEvent) al.get( j );
							pre.fill( idr );
							pre.draw( idr );
						}
					}
					else
					{
						wi.getEvent( ).iObjIndex = i + 1;
						switch ( wi.getInstruction( ) )
						{
							case PrimitiveRenderEvent.FILL
									| PrimitiveRenderEvent.DRAW :
								wi.getEvent( ).fill( idr );
								wi.getEvent( ).draw( idr );
								break;
							case PrimitiveRenderEvent.FILL :
								wi.getEvent( ).fill( idr );
								break;
							case PrimitiveRenderEvent.DRAW :
								wi.getEvent( ).draw( idr );
								break;
						}
					}
				}
				else if ( obj instanceof LineRenderEvent )
				{
					( (LineRenderEvent) obj ).draw( idr );
				}
				else if ( obj instanceof TextRenderEvent )
				{
					( (TextRenderEvent) obj ).draw( idr );
				}
			}
			al3D.clear( );
		}
	}
	

	/**
	 * Pre-process all the 3D rendering events. This must be called before
	 * {@link #flush()}.
	 */
	public void process3DEvent( Engine3D engine, double xOffset, double yOffset )
	{
		al3D = engine.processEvent( al3D, xOffset, yOffset );
	}

	/**
	 * @return Returns if current rendering context is transposed.
	 */
	public boolean isTransposed( )
	{
		return bTransposed;
	}

}