/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.ibm.icu.util.Calendar;

/**
 * An internal class used for data binding, runtime series generating.
 */
public class DataProcessor
{

	private final RunTimeContext rtc;
	private final IActionEvaluator iae;

	/**
	 * 
	 * To collect aggregation expressions and queries of each series. 
	 * 
	 */
	private static class AggregationExpressionHelper
	{

		private List aggregationExpsList = new ArrayList( 3 );
		private List querysList = new ArrayList( 3 );

		//Group query for base aggregation
		private List baseQueryList = new ArrayList( 3 );

		public void addAggregation( String exp, List querys )
		{
			for ( int i = 0; i < querys.size( ); i++ )
			{
				aggregationExpsList.add( exp );
				querysList.add( querys.get( i ) );
			}
		}

		public String[] getAggregations( )
		{
			return (String[]) aggregationExpsList.toArray( new String[aggregationExpsList.size( )] );
		}

		public String[] getDataDefinitions( )
		{
			return (String[]) querysList.toArray( new String[querysList.size( )] );
		}
		
		public List getDataDefinitionsForBaseGrouping()
		{
			return  baseQueryList;
		}
		
		public void dispose( )
		{
			aggregationExpsList.clear( );
			querysList.clear( );
		}

		public boolean isEmpty( )
		{
			return aggregationExpsList.isEmpty( ) || querysList.isEmpty( );
		}

		/**
		 * 
		 * @param elSD
		 *            orthogonal series definitions list
		 * @param lhmLookup
		 */
		public void addSeriesDefinitions( EList elSD,
				GroupingLookupHelper lhmLookup ) throws ChartException
		{		
			for ( int k = 0; k < elSD.size( ); k++ )
			{
				SeriesDefinition sdOrthogonal = (SeriesDefinition) elSD.get( k );
				Series series = sdOrthogonal.getDesignTimeSeries( );
				List qlist = ChartEngine.instance( )
						.getDataSetProcessor( series.getClass( ) )
						.getDataDefinitionsForGrouping( series );
				
				List slist = new ArrayList(1);
				for ( int i = 0; i < qlist.size( ); i++ )
				{
					slist.add( ((Query)qlist.get( i )).getDefinition( ) );
				}

				String strOrtAgg = lhmLookup.getOrthogonalAggregationExpression( sdOrthogonal );
				if ( strOrtAgg != null )
				{
					// cache orthogonal series grouping
					addAggregation( strOrtAgg, slist );
				}
				else
				{
					// If no orthogonal grouping, use base grouping
					baseQueryList.addAll( slist );
				}
			}
		}
	}

	/**
	 * The constructor.
	 * 
	 * @param rtc
	 */
	public DataProcessor( RunTimeContext rtc, IActionEvaluator iae )
	{
		this.rtc = rtc;
		this.iae = iae;
	}

	/**
	 * Returns all valid trigger expressions from series, the variables or
	 * parameters in trigger expressions will be replaced.
	 */
	public static String[] getSeriesTriggerExpressions( Series se,
			IActionEvaluator iae, SeriesDefinition baseSD,
			SeriesDefinition orthoSD )
	{
		ArrayList rt = new ArrayList( );

		if ( se != null && iae != null )
		{
			for ( Iterator itr = se.getTriggers( ).iterator( ); itr.hasNext( ); )
			{
				Trigger tg = (Trigger) itr.next( );

				String[] expra = iae.getActionExpressions( tg.getAction( ),
						StructureSource.createSeries( se ) );
				
				if ( expra != null && expra.length > 0 )
				{
					for ( int i = 0; i < expra.length; i++ )
					{
						String expr = expra[i];
						if ( baseSD != null && orthoSD != null )
						{
							expr = ChartVariableHelper.parseChartVariables( expr,
								se,
								baseSD,
								orthoSD );
						}
						if ( expr != null
								&& expr.trim( ).length( ) > 0
								&& !rt.contains( expr ) )
						{
							rt.add( expr );
						}
					}
				}
			}
		}

		if ( rt.size( ) > 0 )
		{
			return (String[]) rt.toArray( new String[rt.size( )] );
		}
		return null;
	}

	/**
	 * Returns the design time's trigger expressions.
	 * 
	 * @param se
	 * @param iae
	 * @return
	 * @since 2.5
	 */
	public static String[] getDesignTimeStringsSeriesTriggerExpressions(
			Series se, IActionEvaluator iae )
	{
		return getSeriesTriggerExpressions( se, iae, null, null );
	}

	private GroupKey[] findGroupKeys( Chart cm, GroupingLookupHelper lhmLookup )
	{
		if ( cm instanceof ChartWithAxes )
		{
			return findGroupKeys( (ChartWithAxes) cm, lhmLookup );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			return findGroupKeys( (ChartWithoutAxes) cm, lhmLookup );
		}
		return null;
	}

	private GroupKey[] findGroupKeys( ChartWithoutAxes cwoa,
			GroupingLookupHelper lhmLookup )
	{
		final List alKeys = new ArrayList( 4 );
		EList elSD = cwoa.getSeriesDefinitions( );

		// Find all orthogonal group keys in model
		SeriesDefinition sd = (SeriesDefinition) elSD.get( 0 );
		elSD = sd.getSeriesDefinitions( );

		Query qOrthogonalSeriesDefinition;
		String sExpression;

		for ( int i = 0; i < elSD.size( ); i++ )
		{
			sd = (SeriesDefinition) elSD.get( i );
			qOrthogonalSeriesDefinition = sd.getQuery( );
			if ( qOrthogonalSeriesDefinition == null )
			{
				continue;
			}

			sExpression = qOrthogonalSeriesDefinition.getDefinition( );
			if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
			{
				GroupKey sortKey = new GroupKey( sExpression, sd.isSetSorting( )
						? sd.getSorting( ) : null );

				if ( !alKeys.contains( sortKey ) )
				{
					sortKey.setKeyIndex( lhmLookup.findIndex( sExpression,
							lhmLookup.getOrthogonalAggregationExpression( sd ) ) );
					alKeys.add( sortKey );
				}
			}
		}

		return (GroupKey[]) alKeys.toArray( new GroupKey[alKeys.size( )] );
	}

	private GroupKey[] findGroupKeys( ChartWithAxes cwa,
			GroupingLookupHelper lhmLookup )
	{
		final ArrayList alKeys = new ArrayList( 4 );

		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];

		// Find all orthogonal group keys in model
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		EList elSD;
		SeriesDefinition sd;
		Query qOrthogonalSeriesDefinition;
		String sExpression;

		for ( int i = 0; i < axaOrthogonal.length; i++ )
		{
			elSD = axaOrthogonal[i].getSeriesDefinitions( );

			for ( int j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				qOrthogonalSeriesDefinition = sd.getQuery( );
				if ( qOrthogonalSeriesDefinition == null )
				{
					continue;
				}

				sExpression = qOrthogonalSeriesDefinition.getDefinition( );
				if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
				{
					GroupKey sortKey = new GroupKey( sExpression,
							sd.isSetSorting( ) ? sd.getSorting( ) : null );
					if ( !alKeys.contains( sortKey ) )
					{
						sortKey.setKeyIndex( lhmLookup.findIndex( sExpression,
								lhmLookup.getOrthogonalAggregationExpression( sd ) ) );
						alKeys.add( sortKey );
					}
				}
			}
		}

		return (GroupKey[]) alKeys.toArray( new GroupKey[alKeys.size( )] );
	}

	/**
	 * Uses IDataRowExpressionEvaluator to create a ResultSetWrapper
	 * 
	 * @return A wrapper of the chart resultset
	 * 
	 * @throws ChartException
	 */
	protected ResultSetWrapper mapToChartResultSet(
			IDataRowExpressionEvaluator idre, Chart cm ) throws ChartException
	{
		ResultSetWrapper rsw = null;
		
		// 1. Collect all used data expressions and grouping expressions
		GroupingLookupHelper lhmLookup = new GroupingLookupHelper( cm,
				iae,
				rtc,
				idre );
		
		// 2. WALK THROUGH RESULTS
		List liResultSet = null;
		List<String> co = null;
		
		// If current is sharing query, use original expressions. Else the value
		// series expression will be transformed to a unique name which include
		// aggregate information to ensure getting correct data when chart
		// evaluates expression.
		// Also if it isn't a grouped evaluator, it still get original
		// expressions and chart will do group by itself.
		co = lhmLookup.getExpressions( );
		
		try
		{
			liResultSet = evaluateRowSet( idre, co.toArray( ) );
		}
		catch ( RuntimeException e )
		{
			throw new ChartException( ChartEnginePlugin.ID, ChartException.GENERATION, e);
		}

		// Prepare orthogonal grouping keys
		final GroupKey[] orthogonalGroupKeys = findGroupKeys( cm, lhmLookup );
		
		if ( idre instanceof IGroupedDataRowExpressionEvaluator
				&& ( (IGroupedDataRowExpressionEvaluator) idre ).getGroupBreaks( 0 ) != null )
		{
			int[] groupBreaks = new int[]{};
			if ( orthogonalGroupKeys != null && orthogonalGroupKeys.length > 0 )
			{
				// If the orthogonal grouping of chart is set, it should be the 0th
				// index.
				groupBreaks = ( (IGroupedDataRowExpressionEvaluator) idre ).getGroupBreaks( 0 );
			}

			// Format data time for grouped case.
			formatBaseSeriesData( cm, lhmLookup, liResultSet );
			
			// 3. Create result set wrapper for grouping case.
			rsw = new ResultSetWrapper( lhmLookup,
					liResultSet,
					orthogonalGroupKeys,
					groupBreaks );
		}
		else
		{
			// 3. Create result set wrapper
			rsw = new ResultSetWrapper( lhmLookup,
					liResultSet,
					orthogonalGroupKeys );

			// 4. Check if base grouping is set.
			SeriesDefinition sdBase = null;
			SeriesDefinition sdValue = null;
			boolean bBaseGrouping = false;

			// TODO ??do we need processing trigger expr too?
			// search all orthogonal series data definitions for base grouping
			AggregationExpressionHelper aggHelper = new AggregationExpressionHelper( );
			if ( cm instanceof ChartWithAxes )
			{
				ChartWithAxes cwa = (ChartWithAxes) cm;
				Axis[] axaBase = cwa.getBaseAxes( );
				Axis[] axaOrthogonal = null;

				// EACH BASE AXIS
				for ( int j = 0; j < axaBase.length; j++ )
				{
					sdBase = axaBase[j].getSeriesDefinitions( ).get( 0 );
					axaOrthogonal = cwa.getOrthogonalAxes( axaBase[j], true );
					bBaseGrouping = rsw.getRowCount( ) > 0 &&
							sdBase.getGrouping( ) != null &&
							sdBase.getGrouping( ).isEnabled( );

					// EACH ORTHOGONAL AXIS
					for ( int i = 0; i < axaOrthogonal.length; i++ )
					{
						// EACH ORTHOGONAL SERIES
						aggHelper.addSeriesDefinitions( axaOrthogonal[i].getSeriesDefinitions( ),
								lhmLookup );
					}
				}

				sdValue = cwa.getOrthogonalAxes( axaBase[0], true )[0].getSeriesDefinitions( )
						.get( 0 );

			}
			else if ( cm instanceof ChartWithoutAxes )
			{
				ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
				sdBase = cwoa.getSeriesDefinitions( )
						.get( 0 );
				bBaseGrouping = rsw.getRowCount( ) > 0 &&
						sdBase.getGrouping( ) != null &&
						sdBase.getGrouping( ).isEnabled( );

				// EACH ORTHOGONAL SERIES
				aggHelper.addSeriesDefinitions( sdBase.getSeriesDefinitions( ),
						lhmLookup );
				sdValue = sdBase.getSeriesDefinitions( ).get( 0 );
			}

			// 4.1. If base grouping is set???
			if ( bBaseGrouping &&
					aggHelper.getDataDefinitionsForBaseGrouping( ).size( ) > 0 )
			{
				// cache base series grouping
				aggHelper.addAggregation( sdBase.getGrouping( )
						.getAggregateExpression( ),
						aggHelper.getDataDefinitionsForBaseGrouping( ) );
			}

			// 5. apply sorting and grouping of chart.
			String[] aggregationExp = aggHelper.getAggregations( );
			String[] saExpressionKeys = aggHelper.getDataDefinitions( );
			if ( idre instanceof IGroupedDataRowExpressionEvaluator )
			{
				if ( ( (IGroupedDataRowExpressionEvaluator) idre ).needOptionalGrouping( ) )
				{
					rsw.applyValueSeriesGroupingNSorting( sdValue,
							aggregationExp,
							saExpressionKeys );
				}
				if ( ( (IGroupedDataRowExpressionEvaluator) idre ).needCategoryGrouping( ) )
				{
					rsw.applyBaseSeriesSortingAndGrouping( sdBase,
							aggregationExp,
							saExpressionKeys );
				}
			}
			else
			{
				rsw.applyValueSeriesGroupingNSorting( sdValue,
						aggregationExp,
						saExpressionKeys );
				rsw.applyBaseSeriesSortingAndGrouping( sdBase,
						aggregationExp,
						saExpressionKeys );
			}
			aggHelper.dispose( );
		}

		return rsw;
	}
	
	/**
	 * Fills the model chart runtime series with the data
	 * 
	 * @throws ChartException
	 */
	public void generateRuntimeSeries( IDataRowExpressionEvaluator idre,
			Chart cm ) throws ChartException
	{
//		if ( idre instanceof IChartCubeResultSet )
//		{
//			generateRuntimeSeries( (IChartCubeResultSet) idre, cm );
//		}
//		else
		// {
			ResultSetWrapper rsw = mapToChartResultSet( idre, cm );
			generateRuntimeSeries( cm, rsw );
//		}
	}

	private void generateRuntimeSeries( ChartWithoutAxes cwoa,
			ResultSetWrapper rsw ) throws ChartException
	{
		final int iGroupCount = rsw.getGroupCount( );

		// POPULATE THE BASE RUNTIME SERIES
		EList elSD = cwoa.getSeriesDefinitions( );
		final SeriesDefinition sdBase = (SeriesDefinition) elSD.get( 0 );
		final SortOption baseSorting = sdBase.isSetSorting( )
				? sdBase.getSorting( ) : null;
		final Series seBaseDesignSeries = sdBase.getDesignTimeSeries( );
		final Series seBaseRuntimeSeries = (Series) EcoreUtil.copy( seBaseDesignSeries );
		sdBase.getSeries( ).add( seBaseRuntimeSeries );

		int iOrthogonalSeriesDefinitionCount = 0;
		int iBaseColumnIndex = 0;
		SeriesDefinition sd;
		Query qy;
		String sExpression;

		EList dda = sdBase.getDesignTimeSeries( ).getDataDefinition( );
		if ( dda.size( ) > 0 )
		{
			List columns = rsw.getLookupHelper( ).getExpressions( );
			iBaseColumnIndex = columns.indexOf( ( (Query) dda.get( 0 ) ).getDefinition( ) );
			if ( iBaseColumnIndex == -1 )
			{
				iBaseColumnIndex = 0;
			}
		}

		elSD = sdBase.getSeriesDefinitions( );
		for ( int j = 0; j < elSD.size( ); j++ )
		{
			sd = (SeriesDefinition) elSD.get( j );
			qy = sd.getQuery( );
			if ( qy == null )
			{
				continue;
			}
			sExpression = qy.getDefinition( );
			if ( sExpression == null || sExpression.length( ) == 0 )
			{
				continue;
			}
			iOrthogonalSeriesDefinitionCount++;
		}

		if ( iOrthogonalSeriesDefinitionCount < 1 )
		{
			fillSeriesDataSet( cwoa,
					seBaseRuntimeSeries,
					rsw.getSubset( iBaseColumnIndex ) );

			// POPULATE ONE ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;
			elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH ORTHOGONAL
			// SERIES DEFINITION
			{
				sdOrthogonal = (SeriesDefinition) elSD.get( j );
				seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );
				seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

				// Retrieve trigger expressions.
				String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
						iae,
						sdBase,
						sdOrthogonal );
				String aggExp = rsw.getLookupHelper( )
						.getOrthogonalAggregationExpression( sdOrthogonal );
				fillSeriesDataSet( cwoa,
						seOrthogonalRuntimeSeries,
						rsw.getSubset( rsw.getLookupHelper( )
								.getValueSeriesExprBuilder( )
								.buildExpr( seOrthogonalDesignSeries.getDataDefinition( ),
										sdOrthogonal,
										sdBase ),
								aggExp ),
						getDesignTimeStringsSeriesTriggerExpressions( seOrthogonalDesignSeries,
								iae ), // Just use trigger expression as
						// the key.
						rsw.getSubset( triggerExprs, aggExp ) );
				seOrthogonalRuntimeSeries.setSeriesIdentifier( seOrthogonalDesignSeries.getSeriesIdentifier( ) );
				sdOrthogonal.getSeries( ).add( seOrthogonalRuntimeSeries );
			}
		}
		else
		{
			// compute all base values.
			Object[] oa = rsw.getMergedGroupingBaseValues( iBaseColumnIndex,
					baseSorting );

			List baseValues = (List) oa[0];
			List idxList = (List) oa[1];
			final int maxCount = baseValues.size( );

			// populate base series dataset.
			// use max-count group values as the base values.
			Object[] baseData = populateSeriesDataSet( seBaseRuntimeSeries,
					new ResultSetDataSet( baseValues,
							rsw.getColumnDataType( iBaseColumnIndex ) ) );

			// POPULATE ALL ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;

			List orthogonalDataList = new ArrayList( );

			elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ )
			{
				sdOrthogonal = (SeriesDefinition) elSD.get( j );
				seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );

				// Retrieve trigger expressions.
				String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
						iae,
						sdBase,
						sdOrthogonal );
				String aggExp = rsw.getLookupHelper( )
						.getOrthogonalAggregationExpression( sdOrthogonal );
				for ( int k = 0; k < iGroupCount; k++ )
				{
					seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

					Object[] odata = populateSeriesDataSet( seOrthogonalRuntimeSeries,
							rsw.getSubset( k,
									seOrthogonalDesignSeries.getDataDefinition( ),
									aggExp ),
							rsw.getSubset( k, triggerExprs, aggExp ) );

					odata[3] = Integer.valueOf( rsw.getGroupRowCount( k ) );
					odata[4] = Integer.valueOf( k );
					odata[5] = triggerExprs;

					orthogonalDataList.add( odata );
				}
			}

			Object[] orthogonalData = orthogonalDataList.toArray( );

			// try correct all invalid datasets.
			for ( int i = 0; i < orthogonalData.length; i++ )
			{
				DataSet ds = (DataSet) ( (Object[]) orthogonalData[i] )[0];
				DataSet[] userDs = (DataSet[]) ( (Object[]) orthogonalData[i] )[6];
				int groupIndex = ( (Integer) ( (Object[]) orthogonalData[i] )[4] ).intValue( );

				ds = adjustDataSet( ds,
						maxCount,
						(List) idxList.get( groupIndex ),
						userDs );

				( (Object[]) orthogonalData[i] )[3] = ds;
			}

			// Fill all runtime series dataset after validation.
			// Fill the base series.
			fillSeriesDataSet( (IDataSetProcessor) baseData[1],
					seBaseRuntimeSeries,
					(DataSet) baseData[0] );

			int odx = 0;

			// Fill ALL ORTHOGONAL SERIES
			elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH
			// ORTHOGONAL
			// SERIES DEFINITION
			{
				sdOrthogonal = (SeriesDefinition) elSD.get( j );
				String aggExp = rsw.getLookupHelper( )
						.getOrthogonalAggregationExpression( sdOrthogonal );
				
				for ( int k = 0; k < iGroupCount; k++ ) // FOR
				// EACH
				// ORTHOGONAL
				// RUNTIME SERIES
				{
					Object[] odata = (Object[]) orthogonalData[odx];
					seOrthogonalRuntimeSeries = (Series) odata[2];
					fillSeriesDataSet( (IDataSetProcessor) odata[1],
							seOrthogonalRuntimeSeries,
							(DataSet) odata[0],
							(String[]) odata[5],
							(DataSet[]) odata[6] );

					qy = sdOrthogonal.getQuery( );
					sExpression = ( qy == null ) ? IConstants.UNDEFINED_STRING
							: qy.getDefinition( );
					if ( sExpression == null )
						sExpression = IConstants.UNDEFINED_STRING;
					// TODO format the group key.
					seOrthogonalRuntimeSeries.setSeriesIdentifier( rsw.getGroupKey( k,
							sExpression,
							aggExp ) );
					sdOrthogonal.getSeries( ).add( seOrthogonalRuntimeSeries );

					odx++;
				}
			}
		}
	}

	private void generateRuntimeSeries( ChartWithAxes cwa, ResultSetWrapper rsw )
			throws ChartException
	{
		final int iGroupCount = rsw.getGroupCount( );

		// POPULATE THE BASE RUNTIME SERIES
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions( );
		final SeriesDefinition sdBase = elSD.get( 0 );
		final SortOption baseSorting = sdBase.isSetSorting( )
				? sdBase.getSorting( ) : null;
		final Series seBaseDesignSeries = sdBase.getDesignTimeSeries( );
		final Series seBaseRuntimeSeries = (Series) EcoreUtil.copy( seBaseDesignSeries );
		sdBase.getSeries( ).add( seBaseRuntimeSeries );

		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		int iOrthogonalSeriesDefinitionCount = 0;
		int iBaseColumnIndex = 0;
		SeriesDefinition sd;
		Query qy;
		String sExpression;

		// Get column index of base series.
		EList<Query> dda = sdBase.getDesignTimeSeries( ).getDataDefinition( );
		if ( dda.size( ) > 0 )
		{
			List<String> columns = rsw.getLookupHelper( ).getExpressions( );
			iBaseColumnIndex = columns.indexOf( dda.get( 0 ).getDefinition( ) );
			if ( iBaseColumnIndex == -1 )
			{
				iBaseColumnIndex = 0;
			}
		}

		// Get optional Y series grouping expression.
		for ( int i = 0; i < axaOrthogonal.length; i++ )
		{
			elSD = axaOrthogonal[i].getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ )
			{
				sd = elSD.get( j );
				qy = sd.getQuery( );
				if ( qy == null )
				{
					continue;
				}
				sExpression = qy.getDefinition( ); // This is the optional Y series grouping expression.
				if ( sExpression == null || sExpression.length( ) == 0 )
				{
					continue;
				}
				iOrthogonalSeriesDefinitionCount++;
			}
		}

		// Generate runtime series and put data into series.
		if ( iOrthogonalSeriesDefinitionCount < 1 ) // "< 1" means that optional Y series grouping isn't be defined.
		{
			// 1. Add values of base series.
			fillSeriesDataSet( cwa,
					seBaseRuntimeSeries,
					rsw.getSubset( iBaseColumnIndex ) );

			// POPULATE ONE ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;
			
			// 2. Add values of value series. 
			for ( int i = 0; i < axaOrthogonal.length; i++ ) // FOR EACH AXIS
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH
				// ORTHOGONAL
				// SERIES DEFINITION
				{
					sdOrthogonal = elSD.get( j );
					seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );
					seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

					// Retrieve trigger expressions.
					String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
							iae,
							sdBase,
							sdOrthogonal );
					String aggExp = rsw.getLookupHelper( )
							.getOrthogonalAggregationExpression( sdOrthogonal );
					// Add trigger to user datasets
					fillSeriesDataSet( cwa,
							seOrthogonalRuntimeSeries,
							rsw.getSubset( rsw.getLookupHelper( )
									.getValueSeriesExprBuilder( )
									.buildExpr( seOrthogonalDesignSeries.getDataDefinition( ),
											sdOrthogonal,
											sdBase ),
									aggExp ),
							getDesignTimeStringsSeriesTriggerExpressions( seOrthogonalDesignSeries,
									iae ), // Just use trigger expression as
							// the key.
							rsw.getSubset( triggerExprs, aggExp ) );
					seOrthogonalRuntimeSeries.setSeriesIdentifier( seOrthogonalDesignSeries.getSeriesIdentifier( ) );
					sdOrthogonal.getSeries( ).add( seOrthogonalRuntimeSeries );
				}
			}
		}
		else
		{
			// compute all base values.
			Object[] oa = rsw.getMergedGroupingBaseValues( iBaseColumnIndex,
					baseSorting );

			List baseValues = (List) oa[0];
			List idxList = (List) oa[1];
			final int maxCount = baseValues.size( );

			// populate base series dataset.
			// use max-count group values as the base values.
			Object[] baseData = populateSeriesDataSet( seBaseRuntimeSeries,
					new ResultSetDataSet( baseValues,
							rsw.getColumnDataType( iBaseColumnIndex ) ) );

			// POPULATE ALL ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;

			List orthogonalDataList = new ArrayList( );

			for ( int i = 0; i < axaOrthogonal.length; i++ ) // FOR EACH AXIS
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ )
				{
					sdOrthogonal = elSD.get( j );
					seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );

					// Retrieve trigger expressions.
					String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
							iae,
							sdBase,
							sdOrthogonal );
					String aggExp = rsw.getLookupHelper( )
							.getOrthogonalAggregationExpression( sdOrthogonal );
					for ( int k = 0; k < iGroupCount; k++ )
					{
						seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

						Object[] odata = populateSeriesDataSet( seOrthogonalRuntimeSeries,
								rsw.getSubset( k,
										rsw.getLookupHelper( )
												.getValueSeriesExprBuilder( )
												.buildExpr( seOrthogonalDesignSeries.getDataDefinition( ),
														sdOrthogonal,
														sdBase ),
										aggExp ),
								rsw.getSubset( k, triggerExprs, aggExp ) );

						odata[3] = Integer.valueOf( rsw.getGroupRowCount( k ) );
						odata[4] = Integer.valueOf( k );
						odata[5] = triggerExprs;

						orthogonalDataList.add( odata );
					}
				}
			}

			Object[] orthogonalData = orthogonalDataList.toArray( );

			// try correct all invalid datasets.
			for ( int i = 0; i < orthogonalData.length; i++ )
			{
				DataSet ds = (DataSet) ( (Object[]) orthogonalData[i] )[0];
				DataSet[] userDs = (DataSet[]) ( (Object[]) orthogonalData[i] )[6];
				int groupIndex = ( (Integer) ( (Object[]) orthogonalData[i] )[4] ).intValue( );

				ds = adjustDataSet( ds,
						maxCount,
						(List) idxList.get( groupIndex ),
						userDs );

				( (Object[]) orthogonalData[i] )[3] = ds;
			}

			// Fill all runtime series dataset after validation.
			// Fill the base series.
			fillSeriesDataSet( (IDataSetProcessor) baseData[1],
					seBaseRuntimeSeries,
					(DataSet) baseData[0] );

			int odx = 0;

			// Fill ALL ORTHOGONAL SERIES
			for ( int i = 0; i < axaOrthogonal.length; i++ ) // FOR EACH AXIS
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH
				// ORTHOGONAL
				// SERIES DEFINITION
				{
					sdOrthogonal = elSD.get( j );
					String aggExp = rsw.getLookupHelper( )
							.getOrthogonalAggregationExpression( sdOrthogonal );
					for ( int k = 0; k < iGroupCount; k++ ) // FOR
					// EACH
					// ORTHOGONAL
					// RUNTIME SERIES
					{
						Object[] odata = (Object[]) orthogonalData[odx];
						seOrthogonalRuntimeSeries = (Series) odata[2];
						fillSeriesDataSet( (IDataSetProcessor) odata[1],
								seOrthogonalRuntimeSeries,
								(DataSet) odata[0],
								(String[]) odata[5],
								(DataSet[]) odata[6] );

						qy = sdOrthogonal.getQuery( );
						sExpression = ( qy == null )
								? IConstants.UNDEFINED_STRING
								: qy.getDefinition( );
						if ( sExpression == null )
							sExpression = IConstants.UNDEFINED_STRING;
						// TODO format the group key.
						seOrthogonalRuntimeSeries.setSeriesIdentifier( rsw.getGroupKey( k,
								sExpression,
								aggExp ) );
						sdOrthogonal.getSeries( )
								.add( seOrthogonalRuntimeSeries );

						odx++;
					}
				}
			}
		}
	}

	private DataSet adjustDataSet( DataSet ds, int maxcount, List indexMap,
			DataSet[] userDs )
	{
		DataSet dataSet = adjustEachDataSet( ds, indexMap );

		if ( userDs != null && userDs.length > 0 )
		{
			for ( int i = 0; i < userDs.length; i++ )
			{
				DataSet usds = adjustEachDataSet( userDs[i], indexMap );
				userDs[i] = usds;
			}
		}

		return dataSet;
	}

	private DataSet adjustEachDataSet( DataSet ds, List indexMap )
	{
		Collection co;
		double[] da;
		Double[] dda;
		long[] la;
		Calendar[] ca;
		String[] sa;
		Object[] oa;

		int[] indexArray = new int[indexMap.size( )];

		for ( int i = 0; i < indexArray.length; i++ )
		{
			indexArray[i] = ( (Integer) indexMap.get( i ) ).intValue( );
		}

		Object oContent = ds.getValues( );
		if ( ds instanceof NumberDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof double[] )
			{
				da = (double[]) oContent;

				double[] doubleBuffer = new double[indexArray.length];
				Arrays.fill( doubleBuffer, Double.NaN );
				for ( int i = 0; i < da.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						doubleBuffer[idx] = da[i];
					}
				}
				ds.setValues( doubleBuffer );
			}
			else if ( oContent instanceof Double[] )
			{
				dda = (Double[]) oContent;

				Double[] doubleBuffer = new Double[indexArray.length];
				for ( int i = 0; i < dda.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						doubleBuffer[idx] = dda[i];
					}
				}
				ds.setValues( doubleBuffer );
			}
			else if ( oContent instanceof Number )
			{
				da = new double[]{
					( (Number) oContent ).doubleValue( )
				};

				double[] doubleBuffer = new double[indexArray.length];
				Arrays.fill( doubleBuffer, Double.NaN );
				int idx = indexArray[0];
				if ( idx != -1 )
				{
					doubleBuffer[idx] = da[0];
				}
				ds.setValues( doubleBuffer );
			}
		}
		else if ( ds instanceof DateTimeDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof long[] )
			{
				la = (long[]) oContent;

				double[] longBuffer = new double[indexArray.length];
				Arrays.fill( longBuffer, Double.NaN );
				for ( int i = 0; i < la.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						longBuffer[idx] = la[i];
					}
				}
				ds.setValues( longBuffer );
			}
			else if ( oContent instanceof Calendar[] )
			{
				ca = (Calendar[]) oContent;

				Calendar[] calendarBuffer = new Calendar[indexArray.length];
				for ( int i = 0; i < ca.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						calendarBuffer[idx] = ca[i];
					}
				}
				ds.setValues( calendarBuffer );
			}

		}
		else if ( ds instanceof TextDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof String[] )
			{
				sa = (String[]) oContent;

				String[] stringBuffer = new String[indexArray.length];
				for ( int i = 0; i < sa.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						stringBuffer[idx] = sa[i];
					}
				}
				ds.setValues( stringBuffer );
			}
		}
		else
		{
			// for other anonymous types
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof Object[] )
			{
				oa = (Object[]) oContent;

				Object[] objectBuffer = new Object[indexArray.length];
				for ( int i = 0; i < oa.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						objectBuffer[idx] = oa[i];
					}
				}
				ds.setValues( objectBuffer );
			}
		}

		return ds;
	}

	/**
	 * Populates the runtime dataset.
	 * 
	 * @param seRuntime
	 * @param rsds
	 * @return the returned object array contains [DataSet, IDataSetProcessor,
	 *         RuntimeSeries, GroupRowCount, GroupIndex, UserKeys,
	 *         UserDataSets].
	 * @throws ChartException
	 */
	private Object[] populateSeriesDataSet( Series seRuntime,
			IResultSetDataSet rsds ) throws ChartException
	{
		return populateSeriesDataSet( seRuntime, rsds, null );
	}

	/**
	 * Populates the runtime dataset.
	 * 
	 * @param seRuntime
	 * @param rsds
	 * @return the returned object array contains [DataSet, IDataSetProcessor,
	 *         RuntimeSeries, GroupRowCount, GroupIndex, UserKeys,
	 *         UserDataSets].
	 * @throws ChartException
	 */
	private Object[] populateSeriesDataSet( Series seRuntime,
			IResultSetDataSet rsds, IResultSetDataSet userRsds )
			throws ChartException
	{
		IDataSetProcessor idsp = null;
		try
		{
			idsp = PluginSettings.instance( )
					.getDataSetProcessor( seRuntime.getClass( ) );
		}
		catch ( ChartException pex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					pex );
		}

		DataSet ds = null;

		ds = idsp.populate( rsds, null );

		DataSet[] usds = null;
		if ( userRsds != null )
		{
			// process user dataset.
			UserDataSetProcessor tdsp = new UserDataSetProcessor( );
			usds = tdsp.populate( userRsds );
		}

		return new Object[]{
				ds, idsp, seRuntime, null, null, null, usds
		};
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( IDataSetProcessor idsp, Series seRuntime,
			DataSet ds ) throws ChartException
	{
		fillSeriesDataSet( idsp, seRuntime, ds, null, null );
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( IDataSetProcessor idsp, Series seRuntime,
			DataSet ds, String[] userKeys, DataSet[] userDs )
			throws ChartException
	{
		final ScriptHandler sh = rtc.getScriptHandler( );

		ScriptHandler.callFunction( sh,
				ScriptHandler.BEFORE_DATA_SET_FILLED,
				seRuntime,
				idsp,
				rtc.getScriptContext( ) );

		seRuntime.setDataSet( ds );

		if ( userDs != null && userKeys != null )
		{
			// process user dataset.
			for ( int i = 0; i < Math.min( userDs.length, userKeys.length ); i++ )
			{
				seRuntime.setDataSet( userKeys[i], userDs[i] );
			}
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.AFTER_DATA_SET_FILLED,
				seRuntime,
				ds,
				rtc.getScriptContext( ) );
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( Chart cm, Series seRuntime,
			IResultSetDataSet rsds ) throws ChartException
	{
		fillSeriesDataSet( cm, seRuntime, rsds, null, null );
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( Chart cm, Series seRuntime,
			IResultSetDataSet rsds, String[] userKeys,
			IResultSetDataSet userRsds ) throws ChartException
	{
		final ScriptHandler sh = rtc.getScriptHandler( );
		IDataSetProcessor idsp = null;
		try
		{
			idsp = PluginSettings.instance( )
					.getDataSetProcessor( seRuntime.getClass( ) );
		}
		catch ( ChartException pex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					pex );
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.BEFORE_DATA_SET_FILLED,
				seRuntime,
				idsp,
				rtc.getScriptContext( ) );

		DataSet ds = null;
		ds = idsp.populate( rsds, null );

		seRuntime.setDataSet( ds );

		if ( userRsds != null && userKeys != null )
		{
			// process user dataset.
			UserDataSetProcessor tdsp = new UserDataSetProcessor( );
			DataSet[] usds = tdsp.populate( userRsds );

			for ( int i = 0; i < Math.min( usds.length, userKeys.length ); i++ )
			{
				seRuntime.setDataSet( userKeys[i], usds[i] );
			}
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.AFTER_DATA_SET_FILLED,
				seRuntime,
				ds,
				rtc.getScriptContext( ) );
	}

	/**
	 * Evaluate data for all expressions, include base series, optional Y series grouping and value series.
	 * 
	 * @param idre
	 * @param columns
	 * @param areValueSeries
	 * @return
	 * @since 2.3
	 */
	public List<Object[]> evaluateRowSet( IDataRowExpressionEvaluator idre,
			final Object[] columns )
	{
		List<Object[]> liResultSet = new ArrayList<Object[]>( );
		final int iColumnCount = columns.length;
		Object[] oaTuple;
		final int MAX_ROW_COUNT = ChartUtil.getSupportedMaxRowCount( rtc );
		if ( idre.first( ) )
		{
			int count = 0;
			do
			{
				// If max row limitation is used
				if ( MAX_ROW_COUNT > 0 && count++ >= MAX_ROW_COUNT )
				{
					// Do not throw exceptions to stop rendering, but get the
					// first rows to render chart
					break;
				}

				oaTuple = new Object[iColumnCount];
				for ( int i = 0; i < columns.length; i++ )
				{
					oaTuple[i] = idre.evaluate( (String) columns[i] );
				}
				liResultSet.add( oaTuple );
			} while ( idre.next( ) );
		}

		// !Don't close evaluator here, let creator close it.
		// idre.close( );

		return liResultSet;
	}

	private void generateRuntimeSeries( Chart cm, ResultSetWrapper rsw )
			throws ChartException
	{
		cm.clearSections( IConstants.RUN_TIME );

		if ( cm instanceof ChartWithAxes )
		{
			generateRuntimeSeries( (ChartWithAxes) cm, rsw );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			generateRuntimeSeries( (ChartWithoutAxes) cm, rsw );
		}
	}

//	/**
//	 * Generate runtime series with grouped multi-dimensional result set.
//	 * 
//	 * @param chartCubeResultSet
//	 * @param chart
//	 */
//	private void generateRuntimeSeries( IChartCubeResultSet chartCubeResultSet,
//			Chart chart )
//	{
//		// TODO Auto-generated method stub
//	}

	/**
	 * Format base series data. Now it is only used to format datetime data,
	 * format date for different grouping unit.
	 * 
	 * @param cm
	 * @param lhmLookup
	 * @param rowSet
	 */
	public void formatBaseSeriesData( Chart cm, GroupingLookupHelper lhmLookup, List rowSet )
	{
		SeriesDefinition sdBase = null;

		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			Axis[] axaBase = cwa.getBaseAxes( );

			// EACH BASE AXIS
			for ( int j = 0; j < axaBase.length; j++ )
			{
				sdBase = (SeriesDefinition) axaBase[j].getSeriesDefinitions( )
						.get( 0 );
			}
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			sdBase = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
		}
		final SeriesGrouping sg = sdBase.getGrouping( );
		if ( sg == null || !sg.isEnabled( ) )
		{
			return;
		}

		final Series seBaseDesignTime = sdBase.getDesignTimeSeries( );
		final Query q = (Query) seBaseDesignTime.getDataDefinition( ).get( 0 );
		final int iBaseColumnIndex = lhmLookup.findIndexOfBaseSeries( q.getDefinition( ) );

		final DataType dtGrouping = sg.getGroupType( );
		if ( dtGrouping == DataType.DATE_TIME_LITERAL )
		{
			int cunit = GroupingUtil.groupingUnit2CDateUnit( sg.getGroupingUnit( ) );
			CDateTime baseReference = null;
			for ( Iterator iter = rowSet.iterator( ); iter.hasNext( ); )
			{
				Object[] oaTuple = (Object[]) iter.next( );
				Object obj = oaTuple[iBaseColumnIndex];

				// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
				if ( obj instanceof CDateTime )
				{
					baseReference = (CDateTime) obj;
				}
				else if ( obj instanceof Calendar )
				{
					baseReference = new CDateTime( (Calendar) obj );
				}
				else if ( obj instanceof Date )
				{
					baseReference = new CDateTime( (Date) obj );
				}
				else
				{
					// set as the smallest Date.
					baseReference = new CDateTime( 0 );
				}
				
				baseReference.clearBelow( cunit );
				
				oaTuple[iBaseColumnIndex] = baseReference;
			}
		}
	}
}
