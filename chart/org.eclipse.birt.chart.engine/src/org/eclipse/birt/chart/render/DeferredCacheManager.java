/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ChartDimension;

/**
 * The class is used to manange runtime DeferredCache of series, it assures the
 * correct painting z-order of series for 2D case.
 * @since 2.2.1
 */
public final class DeferredCacheManager
{

	/** Handle of concrete device renderer, its type may be SWT, Swing or SVG... */
	private final IDeviceRenderer fDeviceRenderer;

	/** Handle of chart object. */
	private final Chart fChart;

		/** The first deferred cache, it will be executed before other  <code>DeferredCache</code> . */
	private DeferredCache fFirstDC;

	/** The last deferred cache, it will be executed after other  <code>DeferredCache</code> . */
	private DeferredCache fLastDC;
	
	/**
	 * The single deferred cache object for some chart whose element must be put
	 * into a signle <code>DeferredCache</code> object.
	 * <p>
	 * Currently the stacked bar series, bubble seires and area series must be
	 * put into single cache.
	 */
	private DeferredCache fSingleDC;

	/** The list stores painting z-order of deferred order for series. */
	private final List fDeferredCacheList = new ArrayList( );

	/**
	 * Constructor of the class.
	 * 
	 * @param idr
	 *            specified device renderer.
	 * @param chart
	 *            specified chart instance.
	 */
	public DeferredCacheManager( IDeviceRenderer idr, Chart chart )
	{
		this.fDeviceRenderer = idr;
		fChart = chart;
		fFirstDC = new DeferredCache( fDeviceRenderer, fChart );
		fLastDC = new DeferredCache( fDeviceRenderer, fChart );
	}

	/**
	 * Create <code>DeferredCache</code> instance for current series.
	 * 
	 * @param br
	 *            current renderer.
	 * @return instance of <code>DeferredCache</code>
	 */
	public DeferredCache createDeferredCache( BaseRenderer br )
	{
		if ( br != null &&
				( ChartDimension.THREE_DIMENSIONAL == fChart.getDimension( )
						.getValue( ) || br.getSeries( ).isSingleCache( ) ) )
		{
			return createSingleDeferredCache( );
		}
		else
		{
			return createDeferredCache( );
		}
	}

	/**
	 * Create new <code>DeferredCache</code> instance.
	 * 
	 * @return <code>DeferredCache</code> instance.
	 */
	DeferredCache createDeferredCache( )
	{
		DeferredCache dc = new DeferredCache( fDeviceRenderer, fChart );
		fDeferredCacheList.add( dc );
		return dc;
	}

	/**
	 * Create <code>DeferredCache</code> instance for signle case, the related
	 * <code>DeferredCache</code> will be only created once.
	 * 
	 * @return instance of <code>DeferredCache</code>.
	 */
	DeferredCache createSingleDeferredCache( )
	{
		if ( fSingleDC != null )
		{
			return fSingleDC;
		}

		fSingleDC = new DeferredCache( fDeviceRenderer, fChart );
		fDeferredCacheList.add( fSingleDC );

		return fSingleDC;
	}
	
	/**
	 * Flush all <code>DeferredCache</code> in the mananger.
	 * 
	 * @throws ChartException
	 */
	public void flushAll( ) throws ChartException
	{
		int options = DeferredCache.FLUSH_PLANE_SHADOW |
		DeferredCache.FLUSH_PLANE |
		DeferredCache.FLUSH_LINE |
		DeferredCache.FLUSH_3D;
		
		// Flush specified blocks.
		flushOptions( options );

		// Flush markers and labels.
		flushMarkersNLabels( );
		
		clearDC( );
	}

	/**
     * Flush specified blocks.
     *
	 * @param options
	 * @throws ChartException
	 */
	public void flushOptions(int options) throws ChartException
	{
		// 1. Flush first common blocks.
		fFirstDC.flushOptions( options  );

		// 2. Flush data points one by one.
		for ( java.util.Iterator iter = fDeferredCacheList.iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );
			if ( obj instanceof DeferredCache )
			{
				( (DeferredCache) obj ).flushOptions( options );
			}
		}

		// 3. flush last blocks.
		fLastDC.flushOptions( options );
	}
	
	/**
	 * Flush markers and lables in all caches.
	 * 
	 * @throws ChartException
	 */
	void flushMarkersNLabels( ) throws ChartException
	{
		List allMarkers = new ArrayList( );
		List allLabels = new ArrayList( );

		getMarkersNLabels( allMarkers, allLabels );

		DeferredCache.flushMarkers( fDeviceRenderer, allMarkers );

		DeferredCache.flushLabels( fDeviceRenderer, allLabels );
	}
	
	/**
	 * Get markers and labels from all caches.
	 * 
	 * @param allMarkers
	 * @param allLabels
	 */
	public void getMarkersNLabels( List allMarkers, List allLabels )
	{
		allMarkers.addAll( fFirstDC.getAllMarkers( ) );
		fFirstDC.getAllMarkers( ).clear( );
		allLabels.addAll( fFirstDC.getAllLabels( ) );
		fFirstDC.getAllLabels( ).clear( );

		for ( java.util.Iterator iter = fDeferredCacheList.iterator( ); iter.hasNext( ); )
		{
			Object obj = iter.next( );
			if ( obj instanceof DeferredCache )
			{
				allMarkers.addAll( ( (DeferredCache) obj ).getAllMarkers( ) );
				( (DeferredCache) obj ).getAllMarkers( ).clear( );
				allLabels.addAll( ( (DeferredCache) obj ).getAllLabels( ) );
				( (DeferredCache) obj ).getAllLabels( ).clear( );
			}
			else if ( obj instanceof List )
			{
				Collections.sort( (List) obj );
				for ( java.util.Iterator iter1 = ( (List) obj ).iterator( ); iter1.hasNext( ); )
				{
					DeferredCache dc = (DeferredCache) iter1.next( );
					allMarkers.addAll( dc.getAllMarkers( ) );
					dc.getAllMarkers( ).clear( );
					allLabels.addAll( dc.getAllLabels( ) );
					dc.getAllLabels( ).clear( );
				}
			}
		}

		allMarkers.addAll( fLastDC.getAllMarkers( ) );
		fLastDC.getAllMarkers( ).clear( );
		allLabels.addAll( fLastDC.getAllLabels( ) );
		fLastDC.getAllLabels( ).clear( );
	}
	
	/**
	 * Clear all <code>DeferredCache</code> instances.
	 */
	public void clearDC( )
	{
		fDeferredCacheList.clear( );

		fFirstDC = null;
		fLastDC = null;
		fSingleDC = null;
	}

	/**
	 * Returns first <code>DeferredCache</code> instance.
	 * 
	 * @return first <code>DeferredCache</code> instance.
	 */
	public DeferredCache getFirstDeferredCache( )
	{
		return fFirstDC;
	}

	/**
	 * Returns last <code>DeferredCache</code> instance.
	 * 
	 * @return last <code>DeferredCache</code> instance.
	 */
	public DeferredCache getLastDeferredCache( )
	{
		return fLastDC;
	}
}
