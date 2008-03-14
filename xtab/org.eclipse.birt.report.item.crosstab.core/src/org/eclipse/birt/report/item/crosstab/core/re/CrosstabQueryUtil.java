/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.olap.api.query.ICubeElementFactory;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;

/**
 * CrosstabQueryUtil
 */
public class CrosstabQueryUtil implements ICrosstabConstants
{

	private static ICubeElementFactory factory = null;

	private CrosstabQueryUtil( )
	{
	}

	public synchronized static ICubeElementFactory getCubeElementFactory( )
			throws BirtException
	{
		if ( factory != null )
		{
			return factory;
		}

		try
		{
			Class cls = Class.forName( ICubeElementFactory.CUBE_ELEMENT_FACTORY_CLASS_NAME );
			factory = (ICubeElementFactory) cls.newInstance( );
		}
		catch ( Exception e )
		{
			throw new CrosstabException( e );
		}
		return factory;
	}

	public static ICubeQueryDefinition createCubeQuery(
			CrosstabReportItemHandle crosstabItem,
			IDataQueryDefinition parentQuery, boolean needMeasure,
			boolean needRowDimension, boolean needColumnDimension,
			boolean needBinding, boolean needSorting, boolean needFilter )
			throws BirtException
	{
		ICubeQueryDefinition cubeQuery = getCubeElementFactory( ).createCubeQuery( crosstabItem.getCubeName( ) );

		List rowLevelNameList = new ArrayList( );
		List columnLevelNameList = new ArrayList( );

		List levelViewList = new ArrayList( );
		Map levelMap = new HashMap( );

		if ( needMeasure )
		{
			// add measure
			for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
			{
				// TODO check visibility?
				MeasureViewHandle mv = crosstabItem.getMeasure( i );

				if ( mv instanceof ComputedMeasureViewHandle )
				{
					continue;
				}

				if ( mv.getCubeMeasure( ) == null )
				{
					throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.measure", //$NON-NLS-1$
							mv.getCubeMeasureName( ) ) );
				}

				IMeasureDefinition mDef = cubeQuery.createMeasure( mv.getCubeMeasure( )
						.getName( ) );
				mDef.setAggrFunction( mv.getCubeMeasure( ).getFunction( ) == null ? null
						: DataAdapterUtil.getRollUpAggregationName( mv.getCubeMeasure( )
								.getFunction( ) ) );

				// add measure filters
				Iterator mfitr = mv.filtersIterator( );

				if ( mfitr != null )
				{
					while ( mfitr.hasNext( ) )
					{
						FilterConditionElementHandle filterCon = (FilterConditionElementHandle) mfitr.next( );

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

						ICubeFilterDefinition filterDef = getCubeElementFactory( ).creatCubeFilterDefinition( filterCondExpr,
								null,
								null,
								null );

						cubeQuery.addFilter( filterDef );
					}
				}
			}
		}

		// add row edge
		if ( needRowDimension
				&& crosstabItem.getDimensionCount( ROW_AXIS_TYPE ) > 0 )
		{
			// TODO check visibility?
			IEdgeDefinition rowEdge = cubeQuery.createEdge( ICubeQueryDefinition.ROW_EDGE );

			LevelHandle mirrorLevel = crosstabItem.getCrosstabView( ROW_AXIS_TYPE )
					.getMirroredStartingLevel( );

			for ( int i = 0; i < crosstabItem.getDimensionCount( ROW_AXIS_TYPE ); i++ )
			{
				DimensionViewHandle dv = crosstabItem.getDimension( ROW_AXIS_TYPE,
						i );

				if ( dv.getCubeDimension( ) == null )
				{
					throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.dimension.row", //$NON-NLS-1$
							dv.getCubeDimensionName( ) ) );
				}

				IDimensionDefinition dimDef = rowEdge.createDimension( dv.getCubeDimension( )
						.getName( ) );

				IHierarchyDefinition hieDef = dimDef.createHierarchy( dv.getCubeDimension( )
						.getDefaultHierarchy( )
						.getName( ) );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					LevelViewHandle lv = dv.getLevel( j );

					if ( lv.getCubeLevel( ) == null )
					{
						throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.level.row", //$NON-NLS-1$
								lv.getCubeLevelName( ) ) );
					}

					ILevelDefinition levelDef = hieDef.createLevel( lv.getCubeLevel( )
							.getName( ) );

					rowLevelNameList.add( lv.getCubeLevel( ).getFullName( ) );

					if ( mirrorLevel != null
							&& mirrorLevel.getQualifiedName( )
									.equals( lv.getCubeLevelName( ) ) )
					{
						rowEdge.setMirrorStartingLevel( levelDef );
					}

					levelViewList.add( lv );
					levelMap.put( lv.getCubeLevel( ), levelDef );
				}
			}

		}

		// add column edge
		if ( needColumnDimension
				&& crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE ) > 0 )
		{
			// TODO check visibility?
			IEdgeDefinition columnEdge = cubeQuery.createEdge( ICubeQueryDefinition.COLUMN_EDGE );

			LevelHandle mirrorLevel = crosstabItem.getCrosstabView( COLUMN_AXIS_TYPE )
					.getMirroredStartingLevel( );

			for ( int i = 0; i < crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE ); i++ )
			{
				DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
						i );

				if ( dv.getCubeDimension( ) == null )
				{
					throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.dimension.column", //$NON-NLS-1$
							dv.getCubeDimensionName( ) ) );
				}

				IDimensionDefinition dimDef = columnEdge.createDimension( dv.getCubeDimension( )
						.getName( ) );

				IHierarchyDefinition hieDef = dimDef.createHierarchy( dv.getCubeDimension( )
						.getDefaultHierarchy( )
						.getName( ) );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					LevelViewHandle lv = dv.getLevel( j );

					if ( lv.getCubeLevel( ) == null )
					{
						throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.level.column", //$NON-NLS-1$
								lv.getCubeLevelName( ) ) );
					}

					ILevelDefinition levelDef = hieDef.createLevel( lv.getCubeLevel( )
							.getName( ) );

					columnLevelNameList.add( lv.getCubeLevel( ).getFullName( ) );

					if ( mirrorLevel != null
							&& mirrorLevel.getQualifiedName( )
									.equals( lv.getCubeLevelName( ) ) )
					{
						columnEdge.setMirrorStartingLevel( levelDef );
					}

					levelViewList.add( lv );
					levelMap.put( lv.getCubeLevel( ), levelDef );
				}
			}

		}

		// add sorting/filter
		if ( needSorting )
		{
			addLevelSorting( levelViewList, levelMap, cubeQuery );
		}

		if ( needFilter )
		{
			addLevelFilter( levelViewList, levelMap, cubeQuery );
		}

		if ( needBinding )
		{
			// add column binding
			Iterator bindingItr = ( (ExtendedItemHandle) crosstabItem.getModelHandle( ) ).columnBindingsIterator( );
			ModuleHandle module = ( (ExtendedItemHandle) crosstabItem.getModelHandle( ) ).getModuleHandle( );

			if ( bindingItr != null )
			{
				Map cache = new HashMap( );

				while ( bindingItr.hasNext( ) )
				{
					ComputedColumnHandle column = (ComputedColumnHandle) bindingItr.next( );

					Binding binding = new Binding( column.getName( ) );
					binding.setAggrFunction( column.getAggregateFunction( ) == null ? null
							: DataAdapterUtil.adaptModelAggregationType( column.getAggregateFunction( ) ) );
					binding.setExpression( column.getExpression( ) == null ? null
							: new ScriptExpression( column.getExpression( ) ) );
					binding.setDataType( DataAdapterUtil.adaptModelDataType( column.getDataType( ) ) );

					if ( column.getFilterExpression( ) != null )
					{
						binding.setFilter( new ScriptExpression( column.getFilterExpression( ) ) );
					}

					for ( Iterator argItr = column.argumentsIterator( ); argItr.hasNext( ); )
					{
						AggregationArgumentHandle aah = (AggregationArgumentHandle) argItr.next( );

						binding.addArgument( new ScriptExpression( aah.getValue( ) ) );
					}

					List aggrList = column.getAggregateOnList( );

					if ( aggrList != null )
					{
						for ( Iterator aggrItr = aggrList.iterator( ); aggrItr.hasNext( ); )
						{
							String baseLevel = (String) aggrItr.next( );

							CrosstabUtil.addHierachyAggregateOn( module,
									binding,
									baseLevel,
									rowLevelNameList,
									columnLevelNameList,
									cache );
						}
					}

					cubeQuery.addBinding( binding );
				}
			}
		}

		return cubeQuery;
	}

	/**
	 * Recursively add all member values and associated levels to the given
	 * list.
	 */
	private static void addMembers( Map levelMap, List levels, List values,
			MemberValueHandle member )
	{
		if ( member != null )
		{
			Object levelDef = levelMap.get( member.getLevel( ) );

			if ( levelDef != null )
			{
				levels.add( levelDef );
				values.add( member.getValue( ) );

				if ( member.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) > 0 )
				{
					// only use first member here
					addMembers( levelMap,
							levels,
							values,
							(MemberValueHandle) member.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
									0 ) );
				}
			}
		}
	}

	private static void addLevelSorting( List levelViews, Map levelMap,
			ICubeQueryDefinition cubeQuery ) throws BirtException
	{
		List levels = new ArrayList( );
		List values = new ArrayList( );

		for ( Iterator itr = levelViews.iterator( ); itr.hasNext( ); )
		{
			LevelViewHandle lv = (LevelViewHandle) itr.next( );

			Iterator sortItr = lv.sortsIterator( );

			if ( sortItr != null )
			{
				while ( sortItr.hasNext( ) )
				{
					SortElementHandle sortKey = (SortElementHandle) sortItr.next( );

					// clean up first
					levels.clear( );
					values.clear( );

					addMembers( levelMap, levels, values, sortKey.getMember( ) );

					ILevelDefinition[] qualifyLevels = null;
					Object[] qualifyValues = null;

					if ( levels.size( ) > 0 )
					{
						qualifyLevels = (ILevelDefinition[]) levels.toArray( new ILevelDefinition[levels.size( )] );
						qualifyValues = values.toArray( new Object[values.size( )] );
					}

					ICubeSortDefinition sortDef = getCubeElementFactory( ).createCubeSortDefinition( sortKey.getKey( ),
							(ILevelDefinition) levelMap.get( lv.getCubeLevel( ) ),
							qualifyLevels,
							qualifyValues,
							DataAdapterUtil.adaptModelSortDirection( sortKey.getDirection( ) ) );

					cubeQuery.addSort( sortDef );
				}
			}
		}
	}

	private static void addLevelFilter( List levelViews, Map levelMap,
			ICubeQueryDefinition cubeQuery ) throws BirtException
	{
		List levels = new ArrayList( );
		List values = new ArrayList( );

		for ( Iterator itr = levelViews.iterator( ); itr.hasNext( ); )
		{
			LevelViewHandle lv = (LevelViewHandle) itr.next( );

			Iterator filterItr = lv.filtersIterator( );

			if ( filterItr != null )
			{
				while ( filterItr.hasNext( ) )
				{
					FilterConditionElementHandle filterCon = (FilterConditionElementHandle) filterItr.next( );

					// clean up first
					levels.clear( );
					values.clear( );

					addMembers( levelMap, levels, values, filterCon.getMember( ) );

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

					ICubeFilterDefinition filterDef = getCubeElementFactory( ).creatCubeFilterDefinition( filterCondExpr,
							(ILevelDefinition) levelMap.get( lv.getCubeLevel( ) ),
							qualifyLevels,
							qualifyValues );

					cubeQuery.addFilter( filterDef );
				}
			}
		}
	}
}
