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
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.emf.common.util.EList;

/**
 * Query helper for cube query definition
 */

class ChartCubeQueryHelper
{

	private final ExtendedItemHandle handle;
	private final Chart cm;

	/**
	 * Maps for registered query expressions.<br>
	 * Key: query expression, value: column binding name
	 */
	private Map registeredBindings = new HashMap( );

	/**
	 * Maps for registered sort keys.<br>
	 * Key: sort key query expression, value: ILevelDefinition
	 */
	private Map registeredLevels = new HashMap( );

	private String rowEdgeDimension;
	/**
	 * Maps for hierarchy definitions.<br>
	 * Key: dimension name, value: IHierarchyDefinition
	 */
	private Map mapHieDef = new HashMap( );

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

		List sdList = getAllSeriesDefinitions( cm );
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			SeriesDefinition sd = (SeriesDefinition) sdList.get( i );
			List queryList = sd.getDesignTimeSeries( ).getDataDefinition( );
			for ( int j = 0; j < queryList.size( ); j++ )
			{
				Query query = (Query) queryList.get( j );
				// Add measures or dimensions for data definition
				bindSeriesQuery( sd, query, cubeQuery );
			}

			// Add measures or dimensions for optional grouping
			bindSeriesQuery( sd, sd.getQuery( ), cubeQuery );

			// Sort key query may be modified in the next method, so get it
			// first
			String sortKeyQuery = sd.getSortKey( ).getDefinition( );
			// Add measures or dimensions for data definition
			bindSeriesQuery( sd, sd.getSortKey( ), cubeQuery );

			// Add sorting on dimension
			if ( sd.isSetSorting( )
					&& sortKeyQuery != null && sortKeyQuery.length( ) > 0
					&& registeredLevels.containsKey( sortKeyQuery ) )
			{
				ICubeSortDefinition sortDef = ChartReportItemUtil.getCubeElementFactory( )
						.createCubeSortDefinition( sortKeyQuery,
								(ILevelDefinition) registeredLevels.get( sortKeyQuery ),
								null,
								null,
								sd.getSorting( ) == SortOption.ASCENDING_LITERAL
										? ISortDefinition.SORT_ASC
										: ISortDefinition.SORT_DESC );
				cubeQuery.addSort( sortDef );
			}

			// TODO Add sorting on measures
			// TODO Add filter
		}

		return cubeQuery;
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
			String name = (String) registeredBindings.get( expr );
			if ( name == null )
			{
				// Get a unique name.
				name = StructureFactory.newComputedColumn( handle,
						expr.replaceAll( "\"", "" ) ) //$NON-NLS-1$ //$NON-NLS-2$
						.getName( );
				registeredBindings.put( expr, name );

				Binding colBinding = new Binding( name );
				colBinding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );
				colBinding.setExpression( new ScriptExpression( expr ) );
				// colBinding.addAggregateOn( innerGroupDef.getName( ) );
				// colBinding.setAggrFunction(
				// ChartReportItemUtil.convertToDtEAggFunction(
				// sd.getGrouping( )
				// .getAggregateExpression( ) ) );

				// Add binding to query definition
				cubeQuery.addBinding( colBinding );

				String measure = getMeasure( expr );
				if ( measure != null )
				{
					// Add measure
					IMeasureDefinition mDef = cubeQuery.createMeasure( measure );
					// Set aggregation type from base series definition
					// grouping
					mDef.setAggrFunction( getBaseAggregationType( cm ) );
				}

				if ( isReferenceToDimLevel( expr ) )
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
						mapHieDef.put( dimensionName, hieDef );
					}
					else
					{
						hieDef = (IHierarchyDefinition) mapHieDef.get( dimensionName );
					}

					// Create level
					ILevelDefinition level = hieDef.createLevel( levels[1] );
					registeredLevels.put( expr, level );
					// columnLevelNameList.add( "Group3/Job" );
				}
			}

			// Replace query expression in chart runtime model with binding name
			String newExpr = ExpressionUtil.createJSDataExpression( name );
			query.setDefinition( newExpr );
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

	static String getBaseAggregationType( Chart cm )
	{
		String aggExp = getBaseSeriesDefinition( cm ).getGrouping( )
				.getAggregateExpression( )
				.toLowerCase( );
		// DTE will ignore average function during mapping
		// aggExp = DataAdapterUtil.getRollUpAggregationName( aggExp );
		try
		{
			aggExp = DataAdapterUtil.adaptModelAggregationType( aggExp );
		}
		catch ( AdapterException e )
		{
			// Set aggregation type as sum if no match
			aggExp = "SUM"; //$NON-NLS-1$
		}
		return aggExp;
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
		if ( expr == null )
			return null;
		if ( !expr.matches( "\\Qdata[\"\\E.*\\Q\"]\\E" ) ) //$NON-NLS-1$
			return null;
		return expr.replaceFirst( "\\Qdata[\"\\E", "" ) //$NON-NLS-1$ //$NON-NLS-2$
				.replaceFirst( "\\Q\"]\\E", "" ); //$NON-NLS-1$ //$NON-NLS-2$
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
