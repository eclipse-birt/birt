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

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.emf.common.util.EList;

/**
 * Query helper for cube query definition
 */

class ChartCubeQueryHelper
{

	private final ExtendedItemHandle handle;
	private final Chart cm;

	/**
	 * Maps for registered column bindings.<br>
	 * Key: binding query expression, value: Binding
	 */
	private Map registeredBindings = new HashMap( );
	/**
	 * Maps for registered queries.<br>
	 * Key: binding name, value: raw query expression
	 */
	private Map registeredQueries = new HashMap( );

	/**
	 * Maps for registered level definitions.<br>
	 * Key: Binding name of query, value: ILevelDefinition
	 */
	private Map registeredLevels = new HashMap( );

	/**
	 * Maps for registered level handles.<br>
	 * Key: LevelHandle, value: ILevelDefinition
	 */
	private Map registeredLevelHandles = new HashMap( );

	private String rowEdgeDimension;

	public ChartCubeQueryHelper( ExtendedItemHandle handle, Chart cm )
	{
		this.handle = handle;
		this.cm = cm;
	}

	public ICubeQueryDefinition createCubeQuery( IDataQueryDefinition parent )
			throws BirtException
	{
		CubeHandle cubeHandle = handle.getCube( );
		ICubeQueryDefinition cubeQuery = ChartReportItemUtil.getCubeElementFactory( )
				.createCubeQuery( cubeHandle.getQualifiedName( ) );

		// Add column bindings from handle
		initBindings( );

		List sdList = getAllSeriesDefinitions( cm );

		// Add measures and dimensions
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = (SeriesDefinition) sdList.get( i );
			List queryList = sd.getDesignTimeSeries( ).getDataDefinition( );
			for ( int j = 0; j < queryList.size( ); j++ )
			{
				Query query = (Query) queryList.get( j );
				// Add measures or dimensions for data definition, and update
				// query expression
				bindSeriesQuery( sd, query, cubeQuery );
			}

			// Add measures or dimensions for optional grouping, and update
			// query expression
			bindSeriesQuery( sd, sd.getQuery( ), cubeQuery );
		}

		// Add sorting
		// Sorting must be added after measures and dimensions, since sort
		// key references to measures or dimensions
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = (SeriesDefinition) sdList.get( i );
			addSorting( cubeQuery, sd, i );
		}

		// Add filter
		addCubeFilter( cubeQuery );

		return cubeQuery;
	}

	private void initBindings( ) throws BirtException
	{
		for ( Iterator bindings = handle.getColumnBindings( ).iterator( ); bindings.hasNext( ); )
		{
			ComputedColumnHandle column = (ComputedColumnHandle) bindings.next( );
			// Create new binding
			Binding binding = new Binding( column.getName( ) );
			binding.setDataType( DataAdapterUtil.adaptModelDataType( column.getDataType( ) ) );
			binding.setExpression( new ScriptExpression( column.getExpression( ) ) );
			if ( column.getAggregateOn( ) != null )
			{
				binding.addAggregateOn( column.getAggregateOn( ) );
				binding.setAggrFunction( column.getAggregateFunction( ) == null
						? null
						: DataAdapterUtil.adaptModelAggregationType( column.getAggregateFunction( ) ) );
			}

			// Add binding query expression here
			registeredBindings.put( ExpressionUtil.createJSDataExpression( column.getName( ) ),
					binding );
			// Add raw query expression here
			registeredQueries.put( binding.getBindingName( ),
					column.getExpression( ) );

			// Do not add every binding to cube query, since it may be not used.
			// The binding will be added when found in chart.
		}
	}

	private void addSorting( ICubeQueryDefinition cubeQuery,
			SeriesDefinition sd, int i ) throws BirtException
	{
		// Sort key may be modified in the next method, so get it first
		String sortKey = sd.getSortKey( ).getDefinition( );
		// Update query expression
		bindSeriesQuery( sd, sd.getSortKey( ), cubeQuery );

		if ( sd.isSetSorting( ) && sortKey != null && sortKey.length( ) > 0 )
		{
			String sortKeyBinding = getBindingName( sd.getSortKey( )
					.getDefinition( ) );
			if ( registeredLevels.containsKey( sortKeyBinding ) )
			{
				// Add sorting on dimension
				ICubeSortDefinition sortDef = ChartReportItemUtil.getCubeElementFactory( )
						.createCubeSortDefinition( sortKey,
								(ILevelDefinition) registeredLevels.get( sortKeyBinding ),
								null,
								null,
								sd.getSorting( ) == SortOption.ASCENDING_LITERAL
										? ISortDefinition.SORT_ASC
										: ISortDefinition.SORT_DESC );
				cubeQuery.addSort( sortDef );
			}
			else if ( getMeasure( sortKey ) != null )
			{
				// Add sorting on measures
				Query targetQuery = i > 0 ? sd.getQuery( )
						: (Query) sd.getDesignTimeSeries( )
								.getDataDefinition( )
								.get( 0 );
				String aggFun = i == 0 ? sd.getGrouping( )
						.getAggregateExpression( )
						: AbstractChartBaseQueryGenerator.getAggFuncExpr( sd,
								getBaseSeriesDefinition( cm ).getGrouping( )
										.getAggregateExpression( ) );
				String targetBindingName = getBindingName( targetQuery.getDefinition( ) );

				// Find measure binding
				Binding measureBinding = (Binding) registeredBindings.get( sortKey );
				// Create new total binding on measure
				Binding aggBinding = new Binding( measureBinding.getBindingName( )
						+ targetBindingName );
				aggBinding.setDataType( measureBinding.getDataType( ) );
				aggBinding.setExpression( measureBinding.getExpression( ) );
				aggBinding.addAggregateOn( (String) registeredQueries.get( targetBindingName ) );
				aggBinding.setAggrFunction( aggFun );
				cubeQuery.addBinding( aggBinding );

				ICubeSortDefinition sortDef = ChartReportItemUtil.getCubeElementFactory( )
						.createCubeSortDefinition( ExpressionUtil.createJSDataExpression( aggBinding.getBindingName( ) ),
								(ILevelDefinition) registeredLevels.get( targetBindingName ),
								null,
								null,
								sd.getSorting( ) == SortOption.ASCENDING_LITERAL
										? ISortDefinition.SORT_ASC
										: ISortDefinition.SORT_DESC );
				cubeQuery.addSort( sortDef );
			}
		}
	}

	/**
	 * Adds measure or row/column edge according to query expression. Besides,
	 * generates column bindings, replace them in chart queries and add them in
	 * query definition.
	 */
	private void bindSeriesQuery( SeriesDefinition sd, Query query,
			ICubeQueryDefinition cubeQuery ) throws BirtException
	{
		String expr = query.getDefinition( );
		if ( expr != null && expr.length( ) > 0 )
		{
			boolean bBindingExp = isBinding( expr );
			Binding colBinding = (Binding) registeredBindings.get( expr );
			if ( bBindingExp || colBinding == null )
			{
				String bindingName = null;
				if ( colBinding == null )
				{
					// Get a unique name.
					bindingName = StructureFactory.newComputedColumn( handle,
							expr.replaceAll( "\"", "" ) ) //$NON-NLS-1$ //$NON-NLS-2$
							.getName( );
					colBinding = new Binding( bindingName );
					colBinding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
					colBinding.setExpression( new ScriptExpression( expr ) );

					registeredBindings.put( expr, colBinding );
					registeredQueries.put( bindingName, expr );
				}
				else
				{
					bindingName = colBinding.getBindingName( );
					// Convert binding expression like data[] to raw expression
					// like dimension[] or measure[]
					expr = (String) registeredQueries.get( bindingName );
				}

				// Add binding to query definition
				cubeQuery.addBinding( colBinding );

				String measure = getMeasure( expr );
				if ( measure != null )
				{
					// Add measure
					IMeasureDefinition mDef = cubeQuery.createMeasure( measure );
					// Set aggregation type from base series definition
					// grouping
					String aggFun = adaptAggregationType( getBaseSeriesDefinition( cm ).getGrouping( )
							.getAggregateExpression( ) );
					mDef.setAggrFunction( aggFun );
				}
				else if ( isReferenceToDimLevel( expr ) )
				{
					// Add row/column edge
					String[] levels = getTargetLevel( expr );
					String dimensionName = levels[0];
					final int edgeType = getEdgeType( dimensionName );
					IEdgeDefinition edge = cubeQuery.getEdge( edgeType );
					IHierarchyDefinition hieDef = null;
					if ( edge == null )
					{
						// Only create one edge/dimension/hierarchy in one
						// direction
						edge = cubeQuery.createEdge( edgeType );
						IDimensionDefinition dimDef = edge.createDimension( dimensionName );
						hieDef = dimDef.createHierarchy( handle.getCube( )
								.getDimension( dimDef.getName( ) )
								.getDefaultHierarchy( )
								.getQualifiedName( ) );
					}
					else
					{
						hieDef = (IHierarchyDefinition) ( (IDimensionDefinition) edge.getDimensions( )
								.get( 0 ) ).getHierarchy( ).get( 0 );
					}

					// Create level
					ILevelDefinition levelDef = hieDef.createLevel( levels[1] );
					registeredLevels.put( bindingName, levelDef );

					LevelHandle levelHandle = handle.getCube( )
							.getDimension( levelDef.getHierarchy( )
									.getDimension( )
									.getName( ) )
							.getDefaultHierarchy( )
							.getLevel( levelDef.getName( ) );
					registeredLevelHandles.put( levelHandle, levelDef );
				}
			}

			if ( !bBindingExp )
			{
				// If expression is not binding, replace query expression in
				// chart runtime model with binding name
				String newExpr = ExpressionUtil.createJSDataExpression( colBinding.getBindingName( ) );
				query.setDefinition( newExpr );
			}
		}
	}

	private void addCubeFilter( ICubeQueryDefinition cubeQuery )
			throws BirtException
	{
		List levels = new ArrayList( );
		List values = new ArrayList( );

		Iterator filterItr = ChartReportItemUtil.getChartReportItemFromHandle( handle )
				.getCubeFiltersIterator( );
		while ( filterItr.hasNext( ) )
		{
			FilterConditionElementHandle filterCon = (FilterConditionElementHandle) filterItr.next( );

			// clean up first
			levels.clear( );
			values.clear( );

			addMembers( levels, values, filterCon.getMember( ) );

			ILevelDefinition[] qualifyLevels = null;
			Object[] qualifyValues = null;

			if ( levels.size( ) > 0 )
			{
				qualifyLevels = (ILevelDefinition[]) levels.toArray( new ILevelDefinition[levels.size( )] );
				qualifyValues = values.toArray( new Object[values.size( )] );
			}

			ConditionalExpression filterCondExpr;

			if ( ModuleUtil.isListFilterValue( filterCon ) )
			{
				filterCondExpr = new ConditionalExpression( filterCon.getExpr( ),
						DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
						filterCon.getValue1List( ) );
			}
			else
			{
				filterCondExpr = new ConditionalExpression( filterCon.getExpr( ),
						DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
						filterCon.getValue1( ),
						filterCon.getValue2( ) );
			}

			ICubeFilterDefinition filterDef = ChartReportItemUtil.getCubeElementFactory( )
					.creatCubeFilterDefinition( filterCondExpr,
							(ILevelDefinition) registeredLevelHandles.get( filterCon.getMember( )
									.getLevel( ) ),
							qualifyLevels,
							qualifyValues );

			cubeQuery.addFilter( filterDef );

		}
	}

	/**
	 * Recursively add all member values and associated levels to the given
	 * list.
	 */
	private void addMembers( List levels, List values, MemberValueHandle member )
	{
		if ( member != null )
		{
			Object levelDef = registeredLevelHandles.get( member.getLevel( ) );

			if ( levelDef != null )
			{
				levels.add( levelDef );
				values.add( member.getValue( ) );

				if ( member.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) > 0 )
				{
					// only use first member here
					addMembers( levels,
							values,
							(MemberValueHandle) member.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
									0 ) );
				}
			}
		}
	}

	private int getEdgeType( String dimensionName )
	{
		if ( this.rowEdgeDimension == null )
		{
			this.rowEdgeDimension = dimensionName;
			return ICubeQueryDefinition.ROW_EDGE;
		}
		return this.rowEdgeDimension.equals( dimensionName )
				? ICubeQueryDefinition.ROW_EDGE
				: ICubeQueryDefinition.COLUMN_EDGE;
	}

	static String adaptAggregationType( String aggregatonExpr )
	{
		// DTE will ignore average function during mapping
		// aggExp = DataAdapterUtil.getRollUpAggregationName( aggExp );
		try
		{
			return DataAdapterUtil.adaptModelAggregationType( aggregatonExpr.toLowerCase( ) );
		}
		catch ( AdapterException e )
		{
			// Set aggregation type as sum if no match
			return "SUM"; //$NON-NLS-1$
		}
	}

	static SeriesDefinition getBaseSeriesDefinition( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			Axis xAxis = (Axis) ( ( (ChartWithAxes) cm ).getAxes( ).get( 0 ) );
			return (SeriesDefinition) xAxis.getSeriesDefinitions( ).get( 0 );
		}

		return (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
				.get( 0 );

	}

	static List getAllSeriesDefinitions( Chart chart )
	{
		List seriesList = new ArrayList( );
		if ( chart instanceof ChartWithAxes )
		{
			Axis xAxis = (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 );
			// Add base series definitions
			seriesList.addAll( xAxis.getSeriesDefinitions( ) );
			EList axisList = xAxis.getAssociatedAxes( );
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				// Add value series definitions
				seriesList.addAll( ( (Axis) axisList.get( i ) ).getSeriesDefinitions( ) );
			}
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			SeriesDefinition sdBase = (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 );
			seriesList.add( sdBase );
			seriesList.addAll( sdBase.getSeriesDefinitions( ) );
		}
		return seriesList;
	}

	/**
	 * This method is to get the measure name that referenced by a measure
	 * reference expression.
	 * 
	 * @param expr
	 * @return
	 */
	static String getMeasure( String expr ) throws DataException
	{
		if ( expr == null || !expr.matches( "\\Qmeasure[\"\\E.*\\Q\"]\\E" ) ) //$NON-NLS-1$
		{
			return null;
		}
		return expr.replaceFirst( "\\Qmeasure[\"\\E", "" ) //$NON-NLS-1$ //$NON-NLS-2$
				.replaceFirst( "\\Q\"]\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$

	}

	/**
	 * Return the binding name of data["binding"]
	 * 
	 * @param expr
	 */
	public static String getBindingName( String expr )
	{
		if ( !isBinding( expr ) )
			return null;
		return expr.replaceFirst( "\\Qdata[\"\\E", "" ) //$NON-NLS-1$ //$NON-NLS-2$
				.replaceFirst( "\\Q\"]\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	static boolean isBinding( String expr )
	{
		if ( expr == null )
			return false;
		return expr.matches( "\\Qdata[\"\\E.*\\Q\"]\\E" ); //$NON-NLS-1$
	}

	static boolean isReferenceToDimLevel( String expr )
	{
		if ( expr == null )
			return false;
		return expr.matches( "\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E" ); //$NON-NLS-1$
	}

	/**
	 * This method is used to get the level name that reference by a level
	 * reference expression of following format:
	 * dimension["dimensionName"]["levelName"].
	 * 
	 * String[0] dimensionName; String[1] levelName;
	 * 
	 * @param expr
	 * @return String[]
	 */
	static String[] getTargetLevel( String expr )
	{
		// TODO enhance me.
		if ( expr == null )
			return null;
		if ( !expr.matches( "\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E" ) ) //$NON-NLS-1$
			return null;

		expr = expr.replaceFirst( "\\Qdimension\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$
		String[] result = expr.split( "\\Q\"][\"\\E" ); //$NON-NLS-1$
		result[0] = result[0].replaceAll( "\\Q[\"\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$
		result[1] = result[1].replaceAll( "\\Q\"]\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$
		return result;
	}

}
