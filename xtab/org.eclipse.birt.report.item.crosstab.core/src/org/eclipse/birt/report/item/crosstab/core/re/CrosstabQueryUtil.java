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
import org.eclipse.birt.data.engine.olap.api.query.ICubeElementFactory;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.birt.report.model.elements.interfaces.ISortElementModel;

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

		List<String> rowLevelNameList = new ArrayList<String>( );
		List<String> columnLevelNameList = new ArrayList<String>( );

		List<LevelViewHandle> levelViewList = new ArrayList<LevelViewHandle>( );
		Map<LevelHandle, ILevelDefinition> levelMapping = new HashMap<LevelHandle, ILevelDefinition>( );

		DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );

		try
		{
			IModelAdapter modelAdapter = session.getModelAdaptor( );

			if ( needMeasure )
			{
				// add measure definitions
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
					addFactTableOrMeasureFilter( mv.filtersIterator( ),
							cubeQuery,
							modelAdapter );
				}
			}

			// add row edge
			if ( needRowDimension
					&& crosstabItem.getDimensionCount( ROW_AXIS_TYPE ) > 0 )
			{
				addEdgeDefinition( cubeQuery,
						crosstabItem,
						ROW_AXIS_TYPE,
						rowLevelNameList,
						levelViewList,
						levelMapping );
			}

			// add column edge
			if ( needColumnDimension
					&& crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE ) > 0 )
			{
				addEdgeDefinition( cubeQuery,
						crosstabItem,
						COLUMN_AXIS_TYPE,
						columnLevelNameList,
						levelViewList,
						levelMapping );
			}

			// add fact table filters on Crosstab
			addFactTableOrMeasureFilter( crosstabItem.filtersIterator( ),
					cubeQuery,
					modelAdapter );

			// add sorting/filter
			if ( needSorting )
			{
				addLevelSorting( levelViewList,
						levelMapping,
						cubeQuery,
						modelAdapter );
			}

			if ( needFilter )
			{
				addLevelFilter( levelViewList,
						levelMapping,
						cubeQuery,
						modelAdapter );
			}

			if ( needBinding )
			{
				// add column binding
				Iterator bindingItr = ( (ExtendedItemHandle) crosstabItem.getModelHandle( ) ).columnBindingsIterator( );
				ModuleHandle module = ( (ExtendedItemHandle) crosstabItem.getModelHandle( ) ).getModuleHandle( );

				if ( bindingItr != null )
				{
					Map<String, String> cache = new HashMap<String, String>( );

					while ( bindingItr.hasNext( ) )
					{
						ComputedColumnHandle column = (ComputedColumnHandle) bindingItr.next( );

						Binding binding = new Binding( column.getName( ) );
						binding.setAggrFunction( column.getAggregateFunction( ) == null ? null
								: DataAdapterUtil.adaptModelAggregationType( column.getAggregateFunction( ) ) );
						binding.setExpression( modelAdapter.adaptExpression( (Expression) column.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER )
								.getValue( ) ) );
						binding.setDataType( DataAdapterUtil.adaptModelDataType( column.getDataType( ) ) );

						if ( column.getFilterExpression( ) != null )
						{
							binding.setFilter( modelAdapter.adaptExpression( (Expression) column.getExpressionProperty( ComputedColumn.FILTER_MEMBER )
									.getValue( ) ) );
						}

						for ( Iterator argItr = column.argumentsIterator( ); argItr.hasNext( ); )
						{
							AggregationArgumentHandle aah = (AggregationArgumentHandle) argItr.next( );
							if ( aah.getValue( ) != null )
							{
								binding.addArgument( modelAdapter.adaptExpression( (Expression) aah.getExpressionProperty( AggregationArgument.VALUE_MEMBER )
										.getValue( ) ) );
							}
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
		}
		finally
		{
			session.shutdown( );
		}

		return cubeQuery;
	}

	private static void addEdgeDefinition( ICubeQueryDefinition cubeQuery,
			CrosstabReportItemHandle crosstabItem, int axis,
			List<String> levelNameList, List<LevelViewHandle> levelViewList,
			Map<LevelHandle, ILevelDefinition> levelMapping )
			throws BirtException
	{
		// TODO check visibility?

		IEdgeDefinition edge = cubeQuery.createEdge( axis == COLUMN_AXIS_TYPE ? ICubeQueryDefinition.COLUMN_EDGE
				: ICubeQueryDefinition.ROW_EDGE );

		LevelHandle mirrorLevel = crosstabItem.getCrosstabView( axis )
				.getMirroredStartingLevel( );

		for ( int i = 0; i < crosstabItem.getDimensionCount( axis ); i++ )
		{
			DimensionViewHandle dv = crosstabItem.getDimension( axis, i );

			if ( dv.getCubeDimension( ) == null )
			{
				if ( axis == COLUMN_AXIS_TYPE )
				{
					throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.dimension.column", //$NON-NLS-1$
							dv.getCubeDimensionName( ) ) );
				}
				else
				{
					throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.dimension.row", //$NON-NLS-1$
							dv.getCubeDimensionName( ) ) );
				}
			}

			IDimensionDefinition dimDef = edge.createDimension( dv.getCubeDimension( )
					.getName( ) );

			IHierarchyDefinition hieDef = dimDef.createHierarchy( dv.getCubeDimension( )
					.getDefaultHierarchy( )
					.getName( ) );

			for ( int j = 0; j < dv.getLevelCount( ); j++ )
			{
				LevelViewHandle lv = dv.getLevel( j );

				if ( lv.getCubeLevel( ) == null )
				{
					if ( axis == COLUMN_AXIS_TYPE )
					{
						throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.level.column", //$NON-NLS-1$
								lv.getCubeLevelName( ) ) );
					}
					else
					{
						throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.level.row", //$NON-NLS-1$
								lv.getCubeLevelName( ) ) );
					}
				}

				ILevelDefinition levelDef = hieDef.createLevel( lv.getCubeLevel( )
						.getName( ) );

				levelNameList.add( lv.getCubeLevel( ).getFullName( ) );

				if ( mirrorLevel != null
						&& mirrorLevel.getQualifiedName( )
								.equals( lv.getCubeLevelName( ) ) )
				{
					edge.setMirrorStartingLevel( levelDef );
				}

				levelViewList.add( lv );
				levelMapping.put( lv.getCubeLevel( ), levelDef );
			}
		}

		// check drill definitions
		CrosstabViewHandle view = crosstabItem.getCrosstabView( axis );

		if ( view != null )
		{
			List members = view.getMembers( );

			if ( members != null && members.size( ) > 0 )
			{
				for ( int i = 0; i < members.size( ); i++ )
				{
					MemberValueHandle mvh = (MemberValueHandle) members.get( i );

					if ( mvh != null )
					{
						addDrillDefinition( edge, mvh, levelMapping );
					}
				}
			}
		}
	}

	private static void addDrillDefinition( IEdgeDefinition edge,
			MemberValueHandle member,
			Map<LevelHandle, ILevelDefinition> levelMapping )
	{
		IHierarchyDefinition targetHierarchy = null;
		String targetLevelName = null;
		List<List<Object>> values = new ArrayList<List<Object>>( );

		// the bucket to record output parameters
		Object[] output = new Object[]{
				targetLevelName, targetHierarchy
		};

		traverseDrillMember( output, member, levelMapping, values, 0 );

		targetLevelName = (String) output[0];
		targetHierarchy = (IHierarchyDefinition) output[1];

		IEdgeDrillFilter drillDef = edge.createDrillFilter( null );

		drillDef.setTargetHierarchy( targetHierarchy );
		drillDef.setTargetLevelName( targetLevelName );

		List<Object[]> tuples = new ArrayList<Object[]>( );

		for ( int i = 0; i < values.size( ); i++ )
		{
			List<Object> vals = values.get( i );
			if ( vals == null || vals.size( ) == 0 )
			{
				tuples.add( null );
			}
			else
			{
				tuples.add( vals.toArray( new Object[vals.size( )] ) );
			}
		}

		drillDef.setTuple( tuples );
	}

	private static void traverseDrillMember( Object[] output,
			MemberValueHandle member,
			Map<LevelHandle, ILevelDefinition> levelMapping,
			List<List<Object>> values, int depth )
	{
		LevelHandle targetLevel = member.getLevel( );

		if ( targetLevel == null )
		{
			return;
		}

		// record the tuple values
		while ( depth >= values.size( ) )
		{
			values.add( new ArrayList<Object>( ) );
		}

		Object val = member.getValue( );

		// only add non-null values
		if ( val != null )
		{
			List<Object> vals = values.get( depth );

			vals.add( val );
		}

		ILevelDefinition targetLevelDef = levelMapping.get( targetLevel );

		// update the target level name
		output[0] = targetLevel.getName( );

		if ( targetLevelDef != null )
		{
			// record the last seen hierarchy
			output[1] = targetLevelDef.getHierarchy( );
		}

		// keep process child members
		List children = member.getContents( IMemberValueModel.MEMBER_VALUES_PROP );

		if ( children != null )
		{
			for ( int i = 0; i < children.size( ); i++ )
			{
				MemberValueHandle child = (MemberValueHandle) children.get( i );

				if ( child != null )
				{
					traverseDrillMember( output,
							child,
							levelMapping,
							values,
							depth + 1 );
				}
			}
		}
	}

	/**
	 * Recursively add all member values and associated levels to the given
	 * list.
	 */
	private static void addMembers(
			Map<LevelHandle, ILevelDefinition> levelMapping,
			List<ILevelDefinition> levels, List<Object> values,
			MemberValueHandle member )
	{
		if ( member != null )
		{
			ILevelDefinition levelDef = levelMapping.get( member.getLevel( ) );

			if ( levelDef != null )
			{
				levels.add( levelDef );
				values.add( member.getValue( ) );

				if ( member.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) > 0 )
				{
					// only use first member here
					addMembers( levelMapping,
							levels,
							values,
							(MemberValueHandle) member.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
									0 ) );
				}
			}
		}
	}

	private static void addLevelSorting( List<LevelViewHandle> levelViews,
			Map<LevelHandle, ILevelDefinition> levelMapping,
			ICubeQueryDefinition cubeQuery, IModelAdapter modelAdapter )
			throws BirtException
	{
		List<ILevelDefinition> levels = new ArrayList<ILevelDefinition>( );
		List<Object> values = new ArrayList<Object>( );

		for ( Iterator<LevelViewHandle> itr = levelViews.iterator( ); itr.hasNext( ); )
		{
			LevelViewHandle lv = itr.next( );

			Iterator sortItr = lv.sortsIterator( );

			if ( sortItr != null )
			{
				while ( sortItr.hasNext( ) )
				{
					SortElementHandle sortKey = (SortElementHandle) sortItr.next( );

					// clean up first
					levels.clear( );
					values.clear( );

					addMembers( levelMapping,
							levels,
							values,
							sortKey.getMember( ) );

					ILevelDefinition[] qualifyLevels = null;
					Object[] qualifyValues = null;

					if ( levels.size( ) > 0 )
					{
						qualifyLevels = levels.toArray( new ILevelDefinition[levels.size( )] );
						qualifyValues = values.toArray( new Object[values.size( )] );
					}

					ICubeSortDefinition sortDef = getCubeElementFactory( ).createCubeSortDefinition( modelAdapter.adaptExpression( (Expression) sortKey.getExpressionProperty( ISortElementModel.KEY_PROP )
							.getValue( ) ),
							levelMapping.get( lv.getCubeLevel( ) ),
							qualifyLevels,
							qualifyValues,
							DataAdapterUtil.adaptModelSortDirection( sortKey.getDirection( ) ) );

					cubeQuery.addSort( sortDef );
				}
			}
		}
	}

	private static void addLevelFilter( List<LevelViewHandle> levelViews,
			Map<LevelHandle, ILevelDefinition> levelMapping,
			ICubeQueryDefinition cubeQuery, IModelAdapter modelAdapter )
			throws BirtException
	{
		List<ILevelDefinition> levels = new ArrayList<ILevelDefinition>( );
		List<Object> values = new ArrayList<Object>( );

		for ( Iterator<LevelViewHandle> itr = levelViews.iterator( ); itr.hasNext( ); )
		{
			LevelViewHandle lv = itr.next( );

			Iterator filterItr = lv.filtersIterator( );

			if ( filterItr != null )
			{
				while ( filterItr.hasNext( ) )
				{
					FilterConditionElementHandle filterCon = (FilterConditionElementHandle) filterItr.next( );

					// clean up first
					levels.clear( );
					values.clear( );

					addMembers( levelMapping,
							levels,
							values,
							filterCon.getMember( ) );

					ILevelDefinition[] qualifyLevels = null;
					Object[] qualifyValues = null;

					if ( levels.size( ) > 0 )
					{
						qualifyLevels = levels.toArray( new ILevelDefinition[levels.size( )] );
						qualifyValues = values.toArray( new Object[values.size( )] );
					}

					ConditionalExpression filterCondExpr;

					if ( ModuleUtil.isListFilterValue( filterCon ) )
					{
						filterCondExpr = new ConditionalExpression( modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.EXPR_PROP )
								.getValue( ) ),
								DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
								filterCon.getValue1ExpressionList( )
										.getListValue( ) );
					}
					else
					{
						Expression value1 = null;

						List<Expression> val1list = filterCon.getValue1ExpressionList( )
								.getListValue( );

						if ( val1list != null && val1list.size( ) > 0 )
						{
							value1 = val1list.get( 0 );
						}

						filterCondExpr = new ConditionalExpression( modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.EXPR_PROP )
								.getValue( ) ),
								DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
								modelAdapter.adaptExpression( value1 ),
								modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.VALUE2_PROP )
										.getValue( ) ) );
					}

					ICubeFilterDefinition filterDef = getCubeElementFactory( ).creatCubeFilterDefinition( filterCondExpr,
							levelMapping.get( lv.getCubeLevel( ) ),
							qualifyLevels,
							qualifyValues );

					cubeQuery.addFilter( filterDef );
				}
			}
		}
	}

	private static void addFactTableOrMeasureFilter(
			Iterator<FilterConditionElementHandle> filters,
			ICubeQueryDefinition cubeQuery, IModelAdapter modelAdapter )
			throws BirtException
	{
		if ( filters != null )
		{
			while ( filters.hasNext( ) )
			{
				FilterConditionElementHandle filterCon = filters.next( );

				ConditionalExpression filterCondExpr;

				if ( ModuleUtil.isListFilterValue( filterCon ) )
				{
					filterCondExpr = new ConditionalExpression( modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.EXPR_PROP )
							.getValue( ) ),
							DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
							filterCon.getValue1ExpressionList( ).getListValue( ) );
				}
				else
				{
					Expression value1 = null;

					List<Expression> val1list = filterCon.getValue1ExpressionList( )
							.getListValue( );

					if ( val1list != null && val1list.size( ) > 0 )
					{
						value1 = val1list.get( 0 );
					}

					filterCondExpr = new ConditionalExpression( modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.EXPR_PROP )
							.getValue( ) ),
							DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
							modelAdapter.adaptExpression( value1 ),
							modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.VALUE2_PROP )
									.getValue( ) ) );
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
