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
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.chart.computation.Engine3D;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.I3DRenderEvent;
import org.eclipse.birt.chart.event.IRenderInstruction;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.MarkerInstruction;
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
	public static final int FLUSH_MARKER = 2 << 1;
	public static final int FLUSH_LABLE = 2 << 2;
	public static final int FLUSH_3D = 2 << 3;
	public static final int FLUSH_PLANE_SHADOW = 2 << 4;
	public static final int FLUSH_ALL = ( 2 << 5 ) - 1;

	private final IDeviceRenderer idr;

	private final ArrayList<WrappedInstruction> alPlanes = new ArrayList<WrappedInstruction>( 16 );

	private Comparator<?> cpPlanes = null;

	private final ArrayList<LineRenderEvent> alLines = new ArrayList<LineRenderEvent>( 16 );

	private final ArrayList<MarkerInstruction> alMarkers = new ArrayList<MarkerInstruction>( 16 );

	private final ArrayList<TextRenderEvent> alLabels = new ArrayList<TextRenderEvent>( 16 );

	private final ArrayList<WrappedInstruction> alPlaneShadows = new ArrayList<WrappedInstruction>( 4 );

	private Comparator<?> cpPlaneShadows = null;

	public List al3D = new ArrayList( 16 );

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
	 * Adds rendering Plane event to cache.
	 * 
	 * @param pre
	 *            As of now, supported types are RectanguleRenderEvent and
	 *            PolygonRenderEvent
	 */
	public final Object addPlane( PrimitiveRenderEvent pre, int iInstruction )
	{
		return addPlane( pre, iInstruction, 0 );
	}

	public final Object addPlane( PrimitiveRenderEvent pre, int iInstruction,
			int zorder_hint )
	{
		Object obj = null;
		try
		{
			WrappedInstruction wi;
			
			if ( pre instanceof I3DRenderEvent )
			{
				wi = new WrappedInstruction( this,
						pre.copy( ),
						iInstruction,
						zorder_hint );
				al3D.add( wi );
			}
			else
			{
				wi = new WrappedInstruction( this,
						pre.copy( ),
						iInstruction,
						zorder_hint );
				alPlanes.add( wi );
			}
			
			obj = wi;
		}
		catch ( ChartException ufex )
		{
			logger.log( ufex );
		}
		
		return obj;
	}

	/**
	 * Adds rendering Plane event to cache. This Plane is usually a shadow or
	 * depth, and will be in the lower z-order
	 * 
	 * @param pre
	 *            As of now, supported types are RectanguleRenderEvent and
	 *            PolygonRenderEvent
	 */
	public final void addPlaneShadow( PrimitiveRenderEvent pre, int iInstruction )
	{
		addPlaneShadow( pre, iInstruction, 0 );
	}

	public final void addPlaneShadow( PrimitiveRenderEvent pre,
			int iInstruction, int zorder_hint )
	{
		try
		{
			if ( pre instanceof I3DRenderEvent )
			{
				al3D.add( new WrappedInstruction( this,
						pre.copy( ),
						iInstruction,
						zorder_hint ) );
			}
			else
			{
				alPlaneShadows.add( new WrappedInstruction( this,
						pre.copy( ),
						iInstruction,
						zorder_hint ) );
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
					&& lre.getLineAttributes( ).isVisible( ) )
			{
				PrimitiveRenderEvent lre1 = lre.copy( );
				al3D.add( lre1 );
			}
		}
		else
		{
			alLines.add( (LineRenderEvent) lre.copy( ) );
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
			alLabels.add( (TextRenderEvent) tre.copy( ) );
		}
	}

	/**
	 * Adds marker rendering event to cache.
	 */
	public final void addMarker( PrimitiveRenderEvent pre, int iInstruction,
			double iMarkerSize )
	{
		try
		{
			if ( pre instanceof I3DRenderEvent )
			{
				al3D.add( new MarkerInstruction( this,
						pre.copy( ),
						iInstruction,
						iMarkerSize ) );
			}
			else
			{
				alMarkers.add( new MarkerInstruction( this,
						pre.copy( ),
						iInstruction,
						iMarkerSize ) );
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
		flushOptions( FLUSH_ALL );
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
		// FLUSH PLANE SHADOWS
		if ( ( options & FLUSH_PLANE_SHADOW ) == FLUSH_PLANE_SHADOW )
		{
			flushPlaneShadows( idr, alPlaneShadows, cpPlaneShadows );
		}

		// FLUSH PLANES
		if ( ( options & FLUSH_PLANE ) == FLUSH_PLANE )
		{
			flushPlanes( idr, alPlanes, cpPlanes );
		}

		// FLUSH LINES (WITHOUT SORTING)
		if ( ( options & FLUSH_LINE ) == FLUSH_LINE )
		{
			flushLines( idr, alLines );
		}

		// FLUSH MARKERS (WITHOUT SORTING)
		if ( ( options & FLUSH_MARKER ) == FLUSH_MARKER )
		{
			flushMarkers( idr, alMarkers );
		}

		// FLUSH TEXT (WITHOUT SORTING)
		if ( ( options & FLUSH_LABLE ) == FLUSH_LABLE )
		{
			flushLabels( idr, alLabels );
		}

		// FLUSH 3D events (already z-sorted)
		if ( ( options & FLUSH_3D ) == FLUSH_3D )
		{
			flush3D( idr, al3D );
		}
	}

	/**
	 * Flush cached 3D elements.
	 * 
	 * @param _idr
	 * @param alBlocks
	 * @throws ChartException
	 * @since 2.2.1
	 */
	static void flush3D( IDeviceRenderer _idr, List alBlocks )
			throws ChartException
	{
		if ( _idr == null || alBlocks == null )
		{
			return;
		}

		IRenderInstruction wi;
		for ( int i = 0; i < alBlocks.size( ); i++ )
		{
			Object obj = alBlocks.get( i );

			if ( obj instanceof IRenderInstruction )
			{
				wi = (WrappedInstruction) obj;

				if ( wi.isModel( ) )
				{
					List al = wi.getModel( );
					for ( int j = 0; j < al.size( ); j++ )
					{
						PrimitiveRenderEvent pre = (PrimitiveRenderEvent) al.get( j );
						pre.fill( _idr );
						pre.draw( _idr );
					}
				}
				else
				{
					wi.getEvent( ).iObjIndex = i + 1;
					switch ( wi.getInstruction( ) )
					{
						case PrimitiveRenderEvent.FILL
								| PrimitiveRenderEvent.DRAW :
							wi.getEvent( ).fill( _idr );
							wi.getEvent( ).draw( _idr );
							break;
						case PrimitiveRenderEvent.FILL :
							wi.getEvent( ).fill( _idr );
							break;
						case PrimitiveRenderEvent.DRAW :
							wi.getEvent( ).draw( _idr );
							break;
					}
				}
			}
			else if ( obj instanceof LineRenderEvent )
			{
				( (LineRenderEvent) obj ).draw( _idr );
			}
			else if ( obj instanceof TextRenderEvent )
			{
				( (TextRenderEvent) obj ).draw( _idr );
			}
		}
		alBlocks.clear( );
	}

	/**
	 * Flush cached labels.
	 * 
	 * @param _idr
	 * @param labels
	 * @throws ChartException
	 * @since 2.2.1
	 */
	static void flushLabels( IDeviceRenderer _idr, List labels )
			throws ChartException
	{
		if ( _idr == null || labels == null )
		{
			return;
		}

		for ( int i = 0; i < labels.size( ); i++ )
		{
			TextRenderEvent tre = (TextRenderEvent) labels.get( i );
			tre.draw( _idr );
		}
		labels.clear( );
	}

	/**
	 * Flush cached markers.
	 * 
	 * @param _idr
	 * @param markers
	 * @throws ChartException
	 * @since 2.2.1
	 */
	static void flushMarkers( IDeviceRenderer _idr, List markers )
			throws ChartException
	{
		if ( _idr == null || markers == null )
		{
			return;
		}

		IRenderInstruction wi;
		// SORT ON Marker Size
		Collections.sort( markers );
		for ( int i = 0; i < markers.size( ); i++ )
		{
			wi = (IRenderInstruction) markers.get( i );
			switch ( wi.getInstruction( ) )
			{
				case PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW :
					wi.getEvent( ).fill( _idr );
					wi.getEvent( ).draw( _idr );
					break;
				case PrimitiveRenderEvent.FILL :
					wi.getEvent( ).fill( _idr );
					break;
				case PrimitiveRenderEvent.DRAW :
					wi.getEvent( ).draw( _idr );
					break;
			}
		}
		markers.clear( );
	}

	/**
	 * Flush cached lines.
	 * 
	 * @param _idr
	 * @param lines
	 * @throws ChartException
	 * @since 2.2.1
	 */
	static void flushLines( IDeviceRenderer _idr, List lines )
			throws ChartException
	{
		if ( _idr == null || lines == null )
		{
			return;
		}

		for ( int i = 0; i < lines.size( ); i++ )
		{
			LineRenderEvent lre = (LineRenderEvent) lines.get( i );
			lre.draw( _idr );
		}
		lines.clear( );
	}

	/**
	 * Flush cached planes.
	 * 
	 * @param _idr
	 * @param planes
	 * @throws ChartException
	 * @since 2.2.1
	 */
	static void flushPlanes( IDeviceRenderer _idr, List planes, Comparator cp )
			throws ChartException
	{
		if ( _idr == null || planes == null )
		{
			return;
		}

		IRenderInstruction wi;

		// SORT ON Z-ORDER
		if ( cp != null )
		{
			Collections.sort( planes, cp );
		}
		else
		{
			Collections.sort( planes );
		}

		for ( int i = 0; i < planes.size( ); i++ )
		{
			wi = (IRenderInstruction) planes.get( i );
			if ( wi.isModel( ) )
			{
				List al = wi.getModel( );
				for ( int j = 0; j < al.size( ); j++ )
				{
					PrimitiveRenderEvent pre = (PrimitiveRenderEvent) al.get( j );
					pre.fill( _idr );
					pre.draw( _idr );
				}
			}
			else
			{
				wi.getEvent( ).iObjIndex = i + 1;
				switch ( wi.getInstruction( ) )
				{
					case PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW :
						wi.getEvent( ).fill( _idr );
						wi.getEvent( ).draw( _idr );
						break;
					case PrimitiveRenderEvent.FILL :
						wi.getEvent( ).fill( _idr );
						break;
					case PrimitiveRenderEvent.DRAW :
						wi.getEvent( ).draw( _idr );
						break;
				}
			}
		}
		planes.clear( );
	}

	/**
	 * Flush cached plane shadows.
	 * 
	 * @param _idr
	 * @param planeShadows
	 * @throws ChartException
	 * @since 2.2.1
	 */
	static void flushPlaneShadows( IDeviceRenderer _idr, List planeShadows,
			Comparator cp ) throws ChartException
	{
		if ( _idr == null || planeShadows == null )
		{
			return;
		}

		IRenderInstruction wi;

		// SORT ON Z-ORDER
		if ( cp != null )
		{
			Collections.sort( planeShadows, cp );
		}
		else
		{
			Collections.sort( planeShadows );
		}

		for ( int i = 0; i < planeShadows.size( ); i++ )
		{
			wi = (IRenderInstruction) planeShadows.get( i );
			if ( wi.isModel( ) )
			{
				List al = wi.getModel( );
				for ( int j = 0; j < al.size( ); j++ )
				{
					PrimitiveRenderEvent pre = (PrimitiveRenderEvent) al.get( j );
					pre.fill( _idr );
					pre.draw( _idr );
				}
			}
			else
			{
				wi.getEvent( ).iObjIndex = i + 1;
				switch ( wi.getInstruction( ) )
				{
					case PrimitiveRenderEvent.FILL | PrimitiveRenderEvent.DRAW :
						wi.getEvent( ).fill( _idr );
						wi.getEvent( ).draw( _idr );
						break;
					case PrimitiveRenderEvent.FILL :
						wi.getEvent( ).fill( _idr );
						break;
					case PrimitiveRenderEvent.DRAW :
						wi.getEvent( ).draw( _idr );
						break;
				}
			}
		}
		planeShadows.clear( );
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

	/**
	 * Returns all cached markers.
	 * 
	 * @return all cached markers.
	 */
	public List getAllMarkers( )
	{
		return alMarkers;
	}

	/**
	 * Returns all cached labels.
	 * 
	 * @return all cached labels.
	 */
	public List getAllLabels( )
	{
		return alLabels;
	}

	/*
	 * set the z-order comparator for plane shadows
	 */
	public void setPlaneShadowsComparator( Comparator<?> cp )
	{
		this.cpPlaneShadows = cp;
	}

	/*
	 * set the z-order comparator for plane shadows
	 */
	public void setPlanesComparator( Comparator<?> cp )
	{
		this.cpPlanes = cp;
	}
}
