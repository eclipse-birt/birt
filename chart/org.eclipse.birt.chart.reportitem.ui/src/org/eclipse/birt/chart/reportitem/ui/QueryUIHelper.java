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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;

public final class QueryUIHelper
{

	private static final String BASE_SERIES = Messages.getString( "QueryHelper.Text.CategroySeries" ); //$NON-NLS-1$

	private static final String ORTHOGONAL_SERIES = Messages.getString( "QueryHelper.Text.ValueSeries" ); //$NON-NLS-1$

	private static final String X_SERIES = Messages.getString( "QueryHelper.Text.XSeries" ); //$NON-NLS-1$

	private static final String Y_SERIES = Messages.getString( "QueryHelper.Text.YSeries" ); //$NON-NLS-1$

	public static final String[] CAST_STRING_ARRAY = new String[0];

	/**
	 * 
	 * @param cm
	 */
	public final SeriesQueries[] getSeriesQueryDefinitions( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			return getSeriesQueryDefinitions( (ChartWithAxes) cm );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			return getSeriesQueryDefinitions( (ChartWithoutAxes) cm );
		}
		return null;
	}

	/**
	 * 
	 * @param cwa
	 */
	final SeriesQueries[] getSeriesQueryDefinitions( ChartWithAxes cwa )
	{
		final ArrayList<SeriesQueries> alSeriesQueries = new ArrayList<SeriesQueries>( 4 );
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			return alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
		}

		// DON'T CARE ABOUT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES
		// DEFINITION
		SeriesDefinition sd = elSD.get( 0 ); // ONLY ONE MUST EXIST

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList<Query> elBaseSeries = seBase.getDataDefinition( );
		int[] bDataIndex = getValidationIndex( seBase );
		Query[] qua = new Query[bDataIndex.length];
		SeriesQueries sqd = new SeriesQueries( X_SERIES, qua );
		for ( int i = 0; i < bDataIndex.length; i++ )
		{
			if ( i < elBaseSeries.size( ) )
			{
				qua[i] = elBaseSeries.get( bDataIndex[i] );
			}
			else
			{
				qua[i] = QueryImpl.create( "" ); //$NON-NLS-1$
				elBaseSeries.add( qua[i] );
			}
		}
		alSeriesQueries.add( sqd );

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES'
		// QUERIES
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		for ( int j = 0; j < axaOrthogonal.length; j++ )
		{
			elSD = axaOrthogonal[j].getSeriesDefinitions( ); // DON'T CARE
			// ABOUT
			// SERIES
			// DEFINITION
			// QUERIES
			for ( int k = 0; k < elSD.size( ); k++ )
			{
				sd = elSD.get( k );
				Series seOrthogonal = sd.getDesignTimeSeries( );
				EList<Query> elOrthogonalSeries = seOrthogonal.getDataDefinition( );
				int[] oDataIndex = getValidationIndex( seOrthogonal );
				qua = new Query[oDataIndex.length];
				sqd = new SeriesQueries( Y_SERIES, qua );
				for ( int i = 0; i < oDataIndex.length; i++ )
				{
					if ( oDataIndex[i] < elOrthogonalSeries.size( ) )
					{
						qua[i] = elOrthogonalSeries.get( oDataIndex[i] );
					}
					else
					{
						qua[i] = QueryImpl.create( "" ); //$NON-NLS-1$
						elOrthogonalSeries.add( qua[i] );
					}					
				}
				alSeriesQueries.add( sqd );
			}
		}
		return alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
	}

	/**
	 * 
	 * @param cwoa
	 */
	final SeriesQueries[] getSeriesQueryDefinitions( ChartWithoutAxes cwoa )
	{
		final ArrayList<SeriesQueries> alSeriesQueries = new ArrayList<SeriesQueries>( 4 );
		EList<SeriesDefinition> elSD = cwoa.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			return alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
		}

		// DON'T CARE ABOUT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES
		// DEFINITION
		SeriesDefinition sd = elSD.get( 0 );

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList<Query> elBaseSeries = seBase.getDataDefinition( );
		int[] bDataIndex = getValidationIndex( seBase );
		Query[] qua = new Query[bDataIndex.length];
		SeriesQueries sqd = new SeriesQueries( BASE_SERIES, qua );
		for ( int i = 0; i < bDataIndex.length; i++ )
		{
			if ( i < elBaseSeries.size( ) )
			{
				qua[i] = elBaseSeries.get( bDataIndex[i] );
			}
			else
			{
				qua[i] = QueryImpl.create( "" ); //$NON-NLS-1$
				elBaseSeries.add( qua[i] );
			}
		}
		alSeriesQueries.add( sqd );

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		elSD = sd.getSeriesDefinitions( ); // ALL ORTHOGONAL SERIES DEFINITIONS
		for ( int k = 0; k < elSD.size( ); k++ )
		{
			sd = elSD.get( k );
			Series seOrthogonal = sd.getDesignTimeSeries( );
			EList<Query> elOrthogonalSeries = seOrthogonal.getDataDefinition( );
			int[] oDataIndex = getValidationIndex( seOrthogonal );
			qua = new Query[oDataIndex.length];
			sqd = new SeriesQueries( ORTHOGONAL_SERIES, qua );
			for ( int i = 0; i < oDataIndex.length; i++ )
			{
				if ( oDataIndex[i] < elOrthogonalSeries.size( ) )
				{
					qua[i] = elOrthogonalSeries.get( oDataIndex[i] );
				}
				else
				{
					qua[i] = QueryImpl.create( "" ); //$NON-NLS-1$
					elOrthogonalSeries.add( qua[i] );
				}	
			}
			alSeriesQueries.add( sqd );
		}
		return alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
	}

	/**
	 * SeriesQueries
	 */
	public static final class SeriesQueries
	{

		/**
		 * 
		 */
		private final String sSeriesType;

		/**
		 * 
		 */
		private final Query[] qua;

		/**
		 * 
		 * @param sSeriesType
		 * @param qua
		 */
		SeriesQueries( String sSeriesType, Query[] qua )
		{
			this.sSeriesType = sSeriesType;
			this.qua = qua;
		}

		public Collection<String> validate( )
		{
			ArrayList<String> al = null;
			if ( qua.length == 0 )
			{
				al = new ArrayList<String>( qua.length );
				al.add( Messages.getString( "SeriesQueries.NoDataDefinitionFor", sSeriesType ) ); //$NON-NLS-1$
			}
			else
			{
				Object seriesName = ( (Series) qua[0].eContainer( ) ).getSeriesIdentifier( );
				String nameExt = ""; //$NON-NLS-1$
				if ( seriesName != null && seriesName.toString( ).length( ) > 0 )
				{
					nameExt = "(" + seriesName.toString( ) + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				}

				for ( int i = 0; i < qua.length; i++ )
				{
					if ( !qua[i].isDefined( ) )
					{
						
						if ( al == null )
						{
							al = new ArrayList<String>( qua.length );
						}
						al.add( Messages.getString( "SeriesQueries.dataDefnUndefined", //$NON-NLS-1$
								sSeriesType + nameExt ) ); 
					}
				}
			}

			return al;
		}
	}

	private int[] getValidationIndex( Series series )
	{
		return ChartUIUtil.getSeriesUIProvider( series )
				.validationIndex( series );
	}

	/**
	 * Returns query definitions of chart.
	 * 
	 * @param cm
	 * @return query definition
	 * @see ChartUIConstants#QUERY_CATEGORY
	 * @see ChartUIConstants#QUERY_OPTIONAL
	 * @see ChartUIConstants#QUERY_VALUE
	 * @since 2.3
	 */
	public static Map<String, Query[]> getQueryDefinitionsMap( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			return getQueryDefinitionsMap( (ChartWithAxes) cm );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			return getQueryDefinitionsMap( (ChartWithoutAxes) cm );
		}
		return Collections.emptyMap( );
	}

	/**
	 * Returns query definitions of axes chart.
	 * 
	 * @param cm
	 * @return
	 * @since 2.3
	 */
	static Map<String, Query[]> getQueryDefinitionsMap( ChartWithAxes cwa )
	{
		Map<String, Query[]> queryMap = new HashMap<String, Query[]>( );

		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions( );

		SeriesDefinition sd = elSD.get( 0 ); // ONLY ONE MUST

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList<Query> elBaseSeries = seBase.getDataDefinition( );
		Query categoryQuery = elBaseSeries.get( 0 ); // Only first.
		if ( categoryQuery != null )
		{
			queryMap.put( ChartUIConstants.QUERY_CATEGORY, new Query[]{
				categoryQuery
			} );
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES'
		// QUERIES
		Series seOrthogonal;
		EList<Query> elOrthogonalSeries;
		List<Query> yOptionQueryList = new ArrayList<Query>( );
		List<Query> valueQueryList = new ArrayList<Query>( );

		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		for ( Axis axis : axaOrthogonal )
		{
			elSD = axis.getSeriesDefinitions( );
			for ( SeriesDefinition sdef : elSD )
			{
				Query yOptionQuery = sdef.getQuery( );
				if ( yOptionQuery != null )
				{
					yOptionQueryList.add( yOptionQuery );
				}

				seOrthogonal = sdef.getDesignTimeSeries( );
				elOrthogonalSeries = seOrthogonal.getDataDefinition( );
				for ( Query q : elOrthogonalSeries )
				{
					if ( q != null )
					{
						valueQueryList.add( q );
					}
				}
			}
		}

		if ( yOptionQueryList.size( ) > 0 )
		{
			Query[] q = new Query[]{};
			queryMap.put( ChartUIConstants.QUERY_OPTIONAL,
					yOptionQueryList.toArray( q ) );
		}

		if ( valueQueryList.size( ) > 0 )
		{
			Query[] q = new Query[]{};
			queryMap.put( ChartUIConstants.QUERY_VALUE,
					valueQueryList.toArray( q ) );
		}

		return queryMap;
	}

	/**
	 * Returns query definitions of non-axes chart.
	 * 
	 * @param cm
	 * @return
	 */
	static Map<String, Query[]> getQueryDefinitionsMap( ChartWithoutAxes cwoa )
	{
		Map<String, Query[]> queryMap = new HashMap<String, Query[]>( );

		EList<SeriesDefinition> elSD = cwoa.getSeriesDefinitions( );

		SeriesDefinition sd = elSD.get( 0 );

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList<Query> elBaseSeries = seBase.getDataDefinition( );
		Query categoryQuery = elBaseSeries.get( 0 );
		if ( categoryQuery != null )
		{
			queryMap.put( ChartUIConstants.QUERY_CATEGORY, new Query[]{
				categoryQuery
			} );
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES

		List<Query> yOptionQueryList = new ArrayList<Query>( );
		List<Query> valueQueryList = new ArrayList<Query>( );

		Series seOrthogonal;
		EList<Query> elOrthogonalSeries;
		elSD = sd.getSeriesDefinitions( ); // ALL ORTHOGONAL SERIES DEFINITIONS
		for ( SeriesDefinition sdef : elSD )
		{

			Query yOptionQuery = sdef.getQuery( );
			if ( yOptionQuery != null )
			{
				yOptionQueryList.add( yOptionQuery );
			}

			seOrthogonal = sdef.getDesignTimeSeries( );
			elOrthogonalSeries = seOrthogonal.getDataDefinition( );
			for ( Query q : elOrthogonalSeries )
			{
				if ( q != null )
				{
					valueQueryList.add( q );
				}
			}

		}

		if ( yOptionQueryList.size( ) > 0 )
		{
			Query[] q = new Query[]{};
			queryMap.put( ChartUIConstants.QUERY_OPTIONAL,
					yOptionQueryList.toArray( q ) );
		}

		if ( valueQueryList.size( ) > 0 )
		{
			Query[] q = new Query[]{};
			queryMap.put( ChartUIConstants.QUERY_VALUE,
					valueQueryList.toArray( q ) );
		}

		return queryMap;
	}
}