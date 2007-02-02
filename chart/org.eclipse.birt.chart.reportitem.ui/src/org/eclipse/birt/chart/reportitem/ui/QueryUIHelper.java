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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartUIExtensionsImpl;
import org.eclipse.emf.common.util.EList;

public final class QueryUIHelper
{

	private static final String BASE_SERIES = Messages.getString( "QueryHelper.Text.CategroySeries" ); //$NON-NLS-1$

	private static final String ORTHOGONAL_SERIES = Messages.getString( "QueryHelper.Text.ValueSeries" ); //$NON-NLS-1$

	private static final String X_SERIES = Messages.getString( "QueryHelper.Text.XSeries" ); //$NON-NLS-1$

	private static final String Y_SERIES = Messages.getString( "QueryHelper.Text.YSeries" ); //$NON-NLS-1$

	public static final String[] CAST_STRING_ARRAY = new String[0];

	private transient Hashtable htSeriesAttributeUIProviders = null;

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
		final ArrayList alSeriesQueries = new ArrayList( 4 );
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];
		EList elSD = axPrimaryBase.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			return (SeriesQueries[]) alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
		}

		// DON'T CARE ABOUT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES
		// DEFINITION
		SeriesDefinition sd = (SeriesDefinition) elSD.get( 0 ); // ONLY ONE MUST
		// EXIST

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList elBaseSeries = seBase.getDataDefinition( );
		int[] bDataIndex = getValidationIndex( seBase );
		Query[] qua = new Query[bDataIndex.length];
		SeriesQueries sqd = new SeriesQueries( X_SERIES, qua );
		for ( int i = 0; i < bDataIndex.length; i++ )
		{
			if ( i < elBaseSeries.size( ) )
			{
				qua[i] = (Query) elBaseSeries.get( bDataIndex[i] );
			}
			else
			{
				qua[i] = QueryImpl.create( "" ); //$NON-NLS-1$
			}
		}
		alSeriesQueries.add( sqd );

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES'
		// QUERIES
		Series seOrthogonal;
		EList elOrthogonalSeries;
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
				sd = (SeriesDefinition) elSD.get( k );
				seOrthogonal = sd.getDesignTimeSeries( );
				elOrthogonalSeries = seOrthogonal.getDataDefinition( );
				int[] oDataIndex = getValidationIndex( seOrthogonal );
				qua = new Query[oDataIndex.length];
				sqd = new SeriesQueries( Y_SERIES, qua );
				for ( int i = 0; i < oDataIndex.length; i++ )
				{
					qua[i] = (Query) elOrthogonalSeries.get( oDataIndex[i] );
				}
				alSeriesQueries.add( sqd );
			}
		}
		return (SeriesQueries[]) alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
	}

	/**
	 * 
	 * @param cwoa
	 */
	final SeriesQueries[] getSeriesQueryDefinitions( ChartWithoutAxes cwoa )
	{
		final ArrayList alSeriesQueries = new ArrayList( 4 );
		EList elSD = cwoa.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			return (SeriesQueries[]) alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
		}

		// DON'T CARE ABOUT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES
		// DEFINITION
		SeriesDefinition sd = (SeriesDefinition) elSD.get( 0 );

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList elBaseSeries = seBase.getDataDefinition( );
		int[] bDataIndex = getValidationIndex( seBase );
		Query[] qua = new Query[bDataIndex.length];
		SeriesQueries sqd = new SeriesQueries( BASE_SERIES, qua );
		for ( int i = 0; i < bDataIndex.length; i++ )
		{
			if ( i < elBaseSeries.size( ) )
			{
				qua[i] = (Query) elBaseSeries.get( bDataIndex[i] );
			}
			else
			{
				qua[i] = QueryImpl.create( "" ); //$NON-NLS-1$
			}
		}
		alSeriesQueries.add( sqd );

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		Series seOrthogonal;
		EList elOrthogonalSeries;
		elSD = sd.getSeriesDefinitions( ); // ALL ORTHOGONAL SERIES DEFINITIONS
		for ( int k = 0; k < elSD.size( ); k++ )
		{
			sd = (SeriesDefinition) elSD.get( k );
			seOrthogonal = sd.getDesignTimeSeries( );
			elOrthogonalSeries = seOrthogonal.getDataDefinition( );
			int[] oDataIndex = getValidationIndex( seOrthogonal );
			qua = new Query[oDataIndex.length];
			sqd = new SeriesQueries( ORTHOGONAL_SERIES, qua );
			for ( int i = 0; i < oDataIndex.length; i++ )
			{
				qua[i] = (Query) elOrthogonalSeries.get( oDataIndex[i] );
			}
			alSeriesQueries.add( sqd );
		}
		return (SeriesQueries[]) alSeriesQueries.toArray( new SeriesQueries[alSeriesQueries.size( )] );
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

		public Collection validate( )
		{
			ArrayList al = null;
			for ( int i = 0; i < qua.length; i++ )
			{
				if ( !qua[i].isDefined( ) )
				{
					if ( al == null )
					{
						al = new ArrayList( qua.length );
					}
					al.add( Messages.getString( "SeriesQueries.dataDefnUndefined", sSeriesType ) ); //$NON-NLS-1$
				}
			}
			if ( qua.length == 0 )
			{
				al = new ArrayList( qua.length );
				al.add( Messages.getString( "SeriesQueries.NoDataDefinitionFor", sSeriesType ) ); //$NON-NLS-1$
			}
			return al;
		}
	}

	private void getSeriesAttributeUIProviders( )
	{
		// Get collection of registered UI Providers
		Collection cRegisteredEntries = ChartUIExtensionsImpl.instance( )
				.getSeriesUIComponents( );
		Iterator iterEntries = cRegisteredEntries.iterator( );
		while ( iterEntries.hasNext( ) )
		{
			ISeriesUIProvider provider = (ISeriesUIProvider) iterEntries.next( );
			String sSeries = provider.getSeriesClass( );
			htSeriesAttributeUIProviders.put( sSeries, provider );
		}
	}

	private int[] getValidationIndex( Series series )
	{
		if ( this.htSeriesAttributeUIProviders == null )
		{
			htSeriesAttributeUIProviders = new Hashtable( );
			getSeriesAttributeUIProviders( );
		}
		return ( (ISeriesUIProvider) htSeriesAttributeUIProviders.get( series.getClass( )
				.getName( ) ) ).validationIndex( series );
	}

}