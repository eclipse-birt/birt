/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.emf.common.util.EList;

/**
 * The class defines basic functions for creating base query.
 * 
 * @since BIRT 2.3
 */
public abstract class AbstractChartBaseQueryGenerator
{

	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/** The handle of report item handle. */
	protected ExtendedItemHandle fReportItemHandle;

	/** Current chart handle. */
	protected Chart fChartModel;

	/** The set stores created binding names. */
	protected Set fNameSet = new HashSet( );

	/**
	 * Constructor of the class.
	 * 
	 * @param chart
	 * @param handle
	 */
	public AbstractChartBaseQueryGenerator( ExtendedItemHandle handle, Chart cm )
	{
		fChartModel = cm;
		fReportItemHandle = handle;
	}

	/**
	 * Create base query definition.
	 * 
	 * @param parent
	 * @return
	 */
	public abstract IDataQueryDefinition createBaseQuery( IDataQueryDefinition parent );
	
	/**
	 * Create base query definition.
	 * 
	 * @param columns
	 * @return
	 * @throws DataException
	 */
	public abstract IDataQueryDefinition createBaseQuery( List columns ) throws DataException;
	
	/**
	 * Add aggregate bindings of value series for grouping case.
	 * 
	 * @param query
	 * @param seriesDefinitions
	 * @param baseGroupDef
	 * @param valueExprMap
	 * @param baseSD
	 * @throws DataException
	 */
	protected void addValueSeriesAggregateBindingForGrouping(
			BaseQueryDefinition query, EList seriesDefinitions,
			GroupDefinition baseGroupDef, Map valueExprMap,
			SeriesDefinition baseSD ) throws DataException
	{
		for ( Iterator iter = seriesDefinitions.iterator( ); iter.hasNext( ); )
		{
			SeriesDefinition orthSD = (SeriesDefinition) iter.next( );
			
			String expr = ( (Query) orthSD.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );
			if ( expr != null && !"".equals( expr ) ) //$NON-NLS-1$
			{
				String aggName = getAggFunExpr( orthSD, baseSD );
				if ( aggName!= null && !"".equals( aggName ) ) //$NON-NLS-1$
				{
					// Get a unique name.
					String name = generateUniqueBindingName( expr );
					
					
					Binding colBinding = new Binding( name );

					colBinding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
					colBinding.setExpression( new ScriptExpression( expr ) );
					if ( baseGroupDef != null )
					{
						colBinding.addAggregateOn( baseGroupDef.getName( ) );
					}

					// Set aggregate parameters.
					try
					{
						colBinding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( aggName ) );

						IAggregateFunction aFunc = PluginSettings.instance( )
								.getAggregateFunction( aggName );
						if ( aFunc.getParametersCount( ) > 0 )
						{
							Object[] parameters = getAggFunParameters( orthSD,
									baseSD );

							for ( int i = 0; i < parameters.length &&
									i < aFunc.getParametersCount( ); i++ )
							{
								String param = (String) parameters[i];
								colBinding.addArgument( new ScriptExpression( param ) );
							}
						}
					}
					catch ( ChartException e )
					{
						logger.log( e );
					}

					String newExpr = getExpressionForEvaluator( name );

					( (Query) orthSD.getDesignTimeSeries( )
							.getDataDefinition( )
							.get( 0 ) ).setDefinition( newExpr );

					query.addBinding( colBinding );

					valueExprMap.put( expr, newExpr );
				}
			}
		}
	}

	/**
	 * Generate a unique binding name.
	 * 
	 * @param expr
	 * @return
	 */
	protected String generateUniqueBindingName( String expr )
	{
		String name = StructureFactory.newComputedColumn( fReportItemHandle,
				expr.replaceAll( "\"", "" ) ) //$NON-NLS-1$ //$NON-NLS-2$
				.getName( );
		if ( fNameSet.contains( name ) )
		{
			name = name + fNameSet.size( );
			return generateUniqueBindingName( name );
		}

		fNameSet.add( name );
		return name;
	}

	/**
	 * Generate grouping bindings and add into query definition.
	 * 
	 * @param query
	 * @throws DataException
	 */
	protected void generateGroupBindings( BaseQueryDefinition query )
			throws DataException
	{
		// 1. Get first base and orthogonal series definition to get
		// grouping definition.
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		if ( fChartModel instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) fChartModel;
			baseSD = (SeriesDefinition) cwa.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );

			orthAxisArray = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true );
			orthSD = (SeriesDefinition) ( (Axis) orthAxisArray[0] ).getSeriesDefinitions( )
					.get( 0 );
		}
		else if ( fChartModel instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) fChartModel;
			baseSD = (SeriesDefinition) cwoa.getSeriesDefinitions( ).get( 0 );
			orthSD = (SeriesDefinition) baseSD.getSeriesDefinitions( ).get( 0 );
		}

		// 2. Add grouping.
		// 2.1 Add Y optional grouping.
		GroupDefinition yGroupingDefinition = createOrthogonalGroupingDefinition( orthSD );
		if ( yGroupingDefinition != null )
		{
			query.addGroup( yGroupingDefinition );

			// If the SortKey of Y grouping isn't Y grouping expression, add
			// new
			// sort definition on the group.
			// If base grouping is set, the value series should be
			// aggregate.
			if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) &&
					orthSD.isSetSorting( ) &&
					orthSD.getSortKey( ) != null )
			{
				String sortKey = orthSD.getSortKey( ).getDefinition( );
				String yGroupingExpr = orthSD.getQuery( ).getDefinition( );

				// Add additional sort on the grouping.
				if ( sortKey != null && !yGroupingExpr.equals( sortKey ) )
				{
					// If the SortKey does't equal Y grouping expression, we
					// must create new sort definition and calculate
					// aggregate on the grouping and sort by the SortKey.
					String name = generateUniqueBindingName( sortKey );
					Binding binding = new Binding( name );
					query.addBinding( binding );

					binding.setExpression( new ScriptExpression( sortKey ) );
					binding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
					binding.addAggregateOn( yGroupingDefinition.getName( ) );
					String aggFunc = getAggFunExpr( sortKey,
							baseSD,
							orthAxisArray );
					binding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( aggFunc ) );

					SortDefinition sortDefinition = new SortDefinition( );
					sortDefinition.setColumn( binding.getBindingName( ) );
					sortDefinition.setExpression( ExpressionUtil.createRowExpression( binding.getBindingName( ) ) );
					sortDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( orthSD.getSorting( ) ) );
					yGroupingDefinition.addSort( sortDefinition );
				}
			}
		}

		// 2.2 Add base grouping.
		GroupDefinition baseGroupDefinition = createBaseGroupingDefinition( baseSD );
		if ( baseGroupDefinition != null )
		{
			query.addGroup( baseGroupDefinition );
		}

		// 3. Add binding for value series aggregate.
		GroupDefinition innerGroupDef = null;
		if ( query.getGroups( ) != null && query.getGroups( ).size( ) > 0 )
		{
			innerGroupDef = (GroupDefinition) query.getGroups( )
					.get( query.getGroups( ).size( ) - 1 );
		}

		Map valueExprMap = new HashMap( );
		// Add aggregates.
		if ( fChartModel instanceof ChartWithAxes )
		{
			for ( int i = 0; i < orthAxisArray.length; i++ )
			{
				addValueSeriesAggregateBindingForGrouping( query,
						( (Axis) orthAxisArray[i] ).getSeriesDefinitions( ),
						baseGroupDefinition,
						valueExprMap,
						baseSD );
			}
		}
		else if ( fChartModel instanceof ChartWithoutAxes )
		{
			addValueSeriesAggregateBindingForGrouping( query,
					baseSD.getSeriesDefinitions( ),
					baseGroupDefinition,
					valueExprMap,
					baseSD );
		}

		// 4. Binding sort on base series.
		String baseSortExpr = getValidSortExpr( baseSD );
		if ( baseSD.isSetSorting( ) && baseSortExpr != null )
		{
			if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) )
			{
				// If base series set group, add sort on group definition.
				String baseExpr = ( (Query) baseSD.getDesignTimeSeries( )
						.getDataDefinition( )
						.get( 0 ) ).getDefinition( );
				if ( baseExpr.equals( getValidSortExpr( baseSD ) ) )
				{
					baseGroupDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( baseSD.getSorting( ) ) );
				}
				else
				{
					SortDefinition sd = new SortDefinition( );
					sd.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( baseSD.getSorting( ) ) );

					String newValueSeriesExpr = (String) valueExprMap.get( baseSortExpr );
					if ( newValueSeriesExpr != null )
					{
						// Use new expression instead of old.
						baseSD.getSortKey( ).setDefinition( newValueSeriesExpr );

						sd.setExpression( newValueSeriesExpr );
					}
					else
					{
						sd.setExpression( baseSortExpr );
					}

					baseGroupDefinition.addSort( sd );
				}
			}
			else
			{
				// If base series doesn't set group, directly add sort on
				// query definition.
				SortDefinition sd = new SortDefinition( );
				sd.setExpression( baseSortExpr );
				sd.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( baseSD.getSorting( ) ) );
				query.addSort( sd );
			}
		}
	}

	/**
	 * Create Y grouping definition.
	 * 
	 * @param orthSD
	 * @return
	 */
	private GroupDefinition createOrthogonalGroupingDefinition(
			SeriesDefinition orthSD )
	{

		if ( ChartReportItemUtil.isYGroupingDefined( orthSD ) )
		{
			DataType dataType = null;
			GroupingUnitType groupUnit = null;
			double groupIntervalRange = 0; // Default value is 0.

			String yGroupExpr = orthSD.getQuery( ).getDefinition( );

			if ( orthSD.getGrouping( ) != null &&
					orthSD.getGrouping( ).isEnabled( ) )
			{
				dataType = orthSD.getGrouping( ).getGroupType( );
				groupUnit = orthSD.getGrouping( ).getGroupingUnit( );
				groupIntervalRange = orthSD.getGrouping( )
						.getGroupingInterval( );
			}

			String name = generateUniqueBindingName( yGroupExpr );

			GroupDefinition yGroupDefinition = new GroupDefinition( name );

			yGroupDefinition.setKeyExpression( yGroupExpr );

			yGroupDefinition.setInterval( ChartReportItemUtil.convertToDtEGroupUnit( dataType,
					groupUnit,
					groupIntervalRange ) );
			yGroupDefinition.setIntervalRange( ChartReportItemUtil.convertToDtEIntervalRange( dataType,
					groupUnit,
					groupIntervalRange ) );
			if ( orthSD.isSetSorting( ) )
			{
				yGroupDefinition.setSortDirection( ChartReportItemUtil.convertToDtESortDirection( orthSD.getSorting( ) ) );
			}

			return yGroupDefinition;
		}

		return null;
	}

	/**
	 * Create base grouping definition.
	 * 
	 * @param baseSD
	 * @return
	 */
	private GroupDefinition createBaseGroupingDefinition(
			SeriesDefinition baseSD )
	{
		DataType dataType;
		GroupingUnitType groupUnit;
		double groupIntervalRange;
		if ( ChartReportItemUtil.isBaseGroupingDefined( baseSD ) )
		{
			dataType = baseSD.getGrouping( ).getGroupType( );
			groupUnit = baseSD.getGrouping( ).getGroupingUnit( );
			groupIntervalRange = baseSD.getGrouping( ).getGroupingInterval( );
			if ( groupIntervalRange < 0 )
			{
				groupIntervalRange = 0;
			}

			String baseExpr = ( (Query) baseSD.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );

			String name = generateUniqueBindingName( baseExpr );

			GroupDefinition baseGroupDefinition = new GroupDefinition( name );

			baseGroupDefinition.setKeyExpression( baseExpr );
			baseGroupDefinition.setInterval( ChartReportItemUtil.convertToDtEGroupUnit( dataType,
					groupUnit,
					groupIntervalRange ) );
			baseGroupDefinition.setIntervalRange( ChartReportItemUtil.convertToDtEIntervalRange( dataType,
					groupUnit,
					groupIntervalRange ) );

			return baseGroupDefinition;
		}
		return null;
	}

	/**
	 * Returns aggregation function expression.
	 * 
	 * @param orthSD
	 * @param baseSD
	 * @return
	 */
	protected String getAggFunExpr( SeriesDefinition orthSD,
			SeriesDefinition baseSD )
	{

		String strBaseAggExp = null;
		if ( baseSD.getGrouping( ) != null &&
				baseSD.getGrouping( ).isSetEnabled( ) &&
				baseSD.getGrouping( ).isEnabled( ) )
		{
			strBaseAggExp = baseSD.getGrouping( ).getAggregateExpression( );
			return getAggFuncExpr( orthSD, strBaseAggExp );
		}
		
		return orthSD.getGrouping( ).getAggregateExpression( );
	}

	protected Object[] getAggFunParameters( SeriesDefinition orthSD,
			SeriesDefinition baseSD )
	{
		if ( baseSD.getGrouping( ) != null &&
				baseSD.getGrouping( ).isSetEnabled( ) &&
				baseSD.getGrouping( ).isEnabled( ) )
		{
			SeriesGrouping grouping = orthSD.getGrouping( );
			if ( grouping.isSetEnabled( ) && grouping.isEnabled( ) )
			{
					// Set own group
					return grouping.getAggregateParameters( ).toArray( );
			}
			
			return baseSD.getGrouping( ).getAggregateParameters( ).toArray( );
		}
		else
		{
			return orthSD.getGrouping( ).getAggregateParameters( ).toArray( );
		}
	}
	
	/**
	 * Get aggregation function string of sort key related with value series.
	 * 
	 * @param sortKey
	 * @param baseSD
	 * @param orthAxisArray
	 * @return
	 */
	protected String getAggFunExpr( String sortKey, SeriesDefinition baseSD,
			Object[] orthAxisArray )
	{
		String baseAggFunExpr = null;
		if ( baseSD.getGrouping( ) != null &&
				baseSD.getGrouping( ).isSetEnabled( ) &&
				baseSD.getGrouping( ).isEnabled( ) )
		{
			baseAggFunExpr = baseSD.getGrouping( ).getAggregateExpression( );
		}

		String aggFunction = null;

		if ( fChartModel instanceof ChartWithAxes )
		{
			for ( int i = 0; i < orthAxisArray.length; i++ )
			{
				EList sds = ( (Axis) orthAxisArray[i] ).getSeriesDefinitions( );
				for ( Iterator iter = sds.iterator( ); iter.hasNext( ); )
				{
					SeriesDefinition sd = (SeriesDefinition) iter.next( );
					if ( sd.getDesignTimeSeries( ).getDataDefinition( ) != null &&
							sd.getDesignTimeSeries( )
									.getDataDefinition( )
									.get( 0 ) != null )
					{
						Query q = (Query) sd.getDesignTimeSeries( )
								.getDataDefinition( )
								.get( 0 );
						if ( sortKey.equals( q.getDefinition( ) ) )
						{
							aggFunction = getAggFuncExpr( sd, baseAggFunExpr );
							break;
						}
					}
				}
			}
		}
		else if ( fChartModel instanceof ChartWithoutAxes )
		{

			for ( Iterator iter = baseSD.getSeriesDefinitions( ).iterator( ); iter.hasNext( ); )
			{
				SeriesDefinition sd = (SeriesDefinition) iter.next( );
				if ( sd.getDesignTimeSeries( ).getDataDefinition( ) != null &&
						sd.getDesignTimeSeries( ).getDataDefinition( ).get( 0 ) != null )
				{
					Query q = (Query) sd.getDesignTimeSeries( )
							.getDataDefinition( )
							.get( 0 );
					if ( sortKey.equals( q.getDefinition( ) ) )
					{
						aggFunction = sd.getGrouping( )
								.getAggregateExpression( );
						break;
					}
				}
			}

		}

		if ( aggFunction == null || "".equals( aggFunction ) ) { //$NON-NLS-1$
			return baseAggFunExpr;
		}

		return aggFunction;
	}

	/**
	 * Get valid sort expression from series definition.
	 * 
	 * @param sd
	 * @return
	 */
	protected String getValidSortExpr( SeriesDefinition sd )
	{
		if ( !sd.isSetSorting( ) )
		{
			return null;
		}

		String sortExpr = null;
		if ( sd.getSortKey( ) != null &&
				sd.getSortKey( ).getDefinition( ) != null )
		{
			sortExpr = sd.getSortKey( ).getDefinition( );
		}
		else
		{
			sortExpr = ( (Query) sd.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 ) ).getDefinition( );
		}
		if ( "".equals( sortExpr ) ) //$NON-NLS-1$
		{
			sortExpr = null;
		}

		return sortExpr;
	}

	/**
	 * Gets the aggregation function expression
	 * 
	 * @param orthoSD
	 * @param strBaseAggExp
	 */
	public static String getAggFuncExpr( SeriesDefinition orthoSD,
			String strBaseAggExp )
	{
		String strOrthoAgg = null;
		SeriesGrouping grouping = orthoSD.getGrouping( );
		// Only if base series has enabled grouping
		if ( strBaseAggExp != null )
		{
			// Set own group
			strOrthoAgg = grouping.getAggregateExpression( );

			// Set base group
			if ( strOrthoAgg == null || "".equals( strOrthoAgg ) ) //$NON-NLS-1$
			{
				strOrthoAgg = strBaseAggExp;
			}
		}
		return strOrthoAgg;
	}
	
	/**
	 * @param expression
	 * @return
	 */
	protected String getExpressionForEvaluator( String expression )
	{
		return expression;
	}
}
