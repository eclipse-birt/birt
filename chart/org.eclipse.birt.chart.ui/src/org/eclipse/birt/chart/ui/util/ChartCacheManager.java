/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Cache manager for chart series, sub-type and etc.
 */

public final class ChartCacheManager
{

	private static Map instances = new HashMap( 3 );

	private static String currentInstanceId = null;

	private static final String DEFAULT_INSTANCE = "default"; //$NON-NLS-1$

	private List cacheSeries = new ArrayList( 3 );

	private Map cacheCharts = new HashMap( );

	private static final String PREFIX_SUBTYPE = "s_"; //$NON-NLS-1$

	private static final String PREFIX_ORIENTATION = "o_"; //$NON-NLS-1$

	private ChartCacheManager( )
	{

	}

	/**
	 * Returns an instance. Must invoke <code>switchInstance(String)</code> at
	 * first to ensure returned instance is what you want, or return a default
	 * instance.
	 * 
	 * @return instance
	 */
	public static ChartCacheManager getInstance( )
	{
		if ( currentInstanceId == null )
		{
			switchInstance( DEFAULT_INSTANCE );
		}
		return (ChartCacheManager) instances.get( currentInstanceId );
	}

	/**
	 * Returns a specified instance.
	 * 
	 * @param instanceId
	 *            Instance id.
	 * @return instance
	 */
	public static ChartCacheManager getInstance( String instanceId )
	{
		switchInstance( instanceId );
		return getInstance( );
	}

	/**
	 * Switched the instance by passing id. If id is new, create an instance.
	 * 
	 * @param instanceId
	 *            Instance id.
	 */
	public static void switchInstance( String instanceId )
	{
		assert instanceId != null;
		currentInstanceId = instanceId;
		if ( !instances.containsKey( instanceId ) )
		{
			instances.put( instanceId, new ChartCacheManager( ) );
		}
	}

	/**
	 * Returns a cached series.
	 * 
	 * @param seriesClass
	 *            Class name of series
	 * @param seriesIndex
	 *            The series index in the all series definitions
	 * @return a cloned series instances of specified type. Returns null if not
	 *         found
	 */
	public Series findSeries( String seriesClass, int seriesIndex )
	{
		assert seriesIndex >= 0;
		while ( cacheSeries.size( ) <= seriesIndex )
		{
			cacheSeries.add( new HashMap( ) );
		}
		Map map = (Map) cacheSeries.get( seriesIndex );
		if ( !map.containsKey( seriesClass ) )
		{
			return null;
		}
		return (Series) EcoreUtil.copy( (Series) map.get( seriesClass ) );
	}

	/**
	 * Caches a list of series. Series instance will be cloned before being
	 * stored. If the series instance is existent, replace it with the latest.
	 * 
	 * @param seriesDefinitions
	 *            A list of series definitions. Series types can be different
	 *            from each other.
	 */
	public void cacheSeries( List seriesDefinitions )
	{
		for ( int i = 0; i < seriesDefinitions.size( ); i++ )
		{
			Series series = ( (SeriesDefinition) seriesDefinitions.get( i ) ).getDesignTimeSeries( );
			if ( cacheSeries.size( ) <= i )
			{
				cacheSeries.add( new HashMap( ) );
			}
			// Clone the series instance and save it
			( (Map) cacheSeries.get( i ) ).put( series.getClass( ).getName( ),
					EcoreUtil.copy( series ) );
		}

		// Remove redundant series instances.
		removeSeries( seriesDefinitions.size( ) );
	}

	/**
	 * Caches a series. Series instance will be cloned before being stored. If
	 * the series instance is existent, replace it with the latest.
	 * 
	 * @param seriesIndex
	 *            The series index in the all series definitions
	 * @param series
	 *            Series instance
	 */
	public void cacheSeries( int seriesIndex, Series series )
	{
		assert seriesIndex >= 0;
		while ( cacheSeries.size( ) <= seriesIndex )
		{
			cacheSeries.add( new HashMap( ) );
		}
		( (Map) cacheSeries.get( seriesIndex ) ).put( series.getClass( )
				.getName( ), EcoreUtil.copy( series ) );
	}

	/**
	 * Removes redundant series instances.
	 * 
	 * @param seriesSize
	 *            The series number of current chart model
	 */
	public void removeSeries( int seriesSize )
	{
		while ( cacheSeries.size( ) > seriesSize )
		{
			cacheSeries.remove( seriesSize );
		}
	}

	/**
	 * Clears current instance and related resources.
	 * 
	 */
	public void dispose( )
	{
		cacheSeries.clear( );
		cacheCharts.clear( );
		instances.remove( currentInstanceId );
		currentInstanceId = null;
	}

	/**
	 * Caches the latest selection of sub-type
	 * 
	 * @param chartType
	 *            Chart type
	 * @param subtype
	 *            Chart sub-type
	 */
	public void cacheSubtype( String chartType, String subtype )
	{
		cacheCharts.put( PREFIX_SUBTYPE + chartType, subtype );
	}

	/**
	 * Returns the latest selection of sub-type
	 * 
	 * @param chartType
	 *            Chart type
	 * @return the latest selection of sub-type. Returns null if not found
	 */
	public String findSubtype( String chartType )
	{
		return (String) cacheCharts.get( PREFIX_SUBTYPE + chartType );
	}

	/**
	 * Caches the latest selection of orientation.
	 * 
	 * @param chartType
	 *            Chart type
	 * @param orientation
	 *            Chart orientation
	 */
	public void cacheOrientation( String chartType, Orientation orientation )
	{
		cacheCharts.put( PREFIX_ORIENTATION + chartType, orientation );
	}

	/**
	 * Returns the latest selection of orientation.
	 * 
	 * @param chartType
	 *            Chart type
	 * @return the latest selection of orientation. Returns null if not found
	 */
	public Orientation findOrientation( String chartType )
	{
		return (Orientation) cacheCharts.get( PREFIX_ORIENTATION + chartType );
	}
}
