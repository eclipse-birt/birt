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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
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
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
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
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
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
						levelMapping,
						modelAdapter );
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
						levelMapping,
						modelAdapter );
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
								.getValue( ),
								ExpressionLocation.CUBE ) );
						binding.setDataType( DataAdapterUtil.adaptModelDataType( column.getDataType( ) ) );

						if ( column.getFilterExpression( ) != null )
						{
							binding.setFilter( modelAdapter.adaptExpression( (Expression) column.getExpressionProperty( ComputedColumn.FILTER_MEMBER )
									.getValue( ),
									ExpressionLocation.CUBE ) );
						}

						for ( Iterator argItr = column.argumentsIterator( ); argItr.hasNext( ); )
						{
							AggregationArgumentHandle aah = (AggregationArgumentHandle) argItr.next( );
							if ( aah.getValue( ) != null )
							{
								binding.addArgument( aah.getName( ),
										modelAdapter.adaptExpression( (Expression) aah.getExpressionProperty( AggregationArgument.VALUE_MEMBER )
												.getValue( ),
												ExpressionLocation.CUBE ) );
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
			Map<LevelHandle, ILevelDefinition> levelMapping,
			IModelAdapter modelAdapter ) throws BirtException
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
						&& mirrorLevel.getFullName( )
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

					if ( mvh != null && mvh.getLevel( ) != null )
					{
						addDrillDefinition( edge, mvh, levelMapping );
					}
				}

				addEdgeMemberFilter( cubeQuery,
						modelAdapter,
						members,
						levelMapping );
			}
		}
	}

	private static void addEdgeMemberFilter( ICubeQueryDefinition cubeQuery,
			IModelAdapter modelAdapter, List<MemberValueHandle> members,
			Map<LevelHandle, ILevelDefinition> levelMapping )
			throws BirtException
	{
		List<List<IScriptExpression>> allTargetLevels = new ArrayList<List<IScriptExpression>>( );
		List<List<List<IScriptExpression>>> allMemberValues = new ArrayList<List<List<IScriptExpression>>>( );
		List<List<List<Boolean>>> allMemberFlags = new ArrayList<List<List<Boolean>>>( );
		int[] op = new int[]{
			0
		};

		for ( MemberValueHandle mvh : members )
		{
			if ( mvh != null && mvh.getLevel( ) == null )
			{
				List<IScriptExpression> targetLevels = new ArrayList<IScriptExpression>( );
				List<List<IScriptExpression>> memberValues = new ArrayList<List<IScriptExpression>>( );
				List<List<Boolean>> memberFlags = new ArrayList<List<Boolean>>( );

				traverseMemberFilter( targetLevels,
						op,
						memberValues,
						memberFlags,
						mvh,
						levelMapping,
						modelAdapter,
						1,
						new int[]{
							1
						} );

				allTargetLevels.add( targetLevels );
				allMemberValues.add( memberValues );
				allMemberFlags.add( memberFlags );
			}
		}

		List<IScriptExpression> mergedTargetLevels = new ArrayList<IScriptExpression>( );
		Collection<Collection<IScriptExpression>> mergedMemberValues = new ArrayList<Collection<IScriptExpression>>( );

		int maxLen = 0;

		// determine maximum size of all effective buckets
		for ( int i = 0; i < allMemberValues.size( ); i++ )
		{
			List<List<IScriptExpression>> memberValues = allMemberValues.get( i );

			for ( int j = 0; j < memberValues.size( ); j++ )
			{
				List<IScriptExpression> bucket = memberValues.get( j );

				List<Boolean> flagBucket = allMemberFlags.get( i ).get( j );

				for ( Boolean mark : flagBucket )
				{
					if ( mark != null && mark.booleanValue( ) )
					{
						if ( bucket.size( ) > maxLen )
						{
							maxLen = bucket.size( );
						}

						mergedMemberValues.add( bucket );

						break;
					}
				}
			}
		}

		// normalize merged member values
		for ( Collection<IScriptExpression> bucket : mergedMemberValues )
		{
			int gap = maxLen - bucket.size( );

			while ( gap > 0 )
			{
				// fill with placeholder
				bucket.add( null );

				gap--;
			}
		}

		// merge target levels to maixmum length
		for ( List<IScriptExpression> targetLevels : allTargetLevels )
		{
			if ( mergedTargetLevels.size( ) < maxLen
					&& targetLevels.size( ) > mergedTargetLevels.size( ) )
			{
				mergedTargetLevels.addAll( targetLevels.subList( mergedTargetLevels.size( ),
						Math.min( targetLevels.size( ), maxLen ) ) );
			}
		}

		if ( mergedMemberValues.size( ) > 0 )
		{
			IFilterDefinition memberFilter = getCubeElementFactory( ).creatLevelMemberFilterDefinition( mergedTargetLevels,
					op[0],
					mergedMemberValues );

			cubeQuery.addFilter( memberFilter );
		}
	}

	/**
	 * !!!Note depth and pos is 1-based for this method.
	 */
	private static void traverseMemberFilter(
			List<IScriptExpression> targetLevels, int[] op,
			List<List<IScriptExpression>> memberValues,
			List<List<Boolean>> memberFlags, MemberValueHandle member,
			Map<LevelHandle, ILevelDefinition> levelMapping,
			IModelAdapter modelAdapter, int depth, int[] pos )
			throws BirtException
	{
		LevelHandle targetLevel = member.getLevel( );

		if ( targetLevel == null )
		{
			// this must be the pseudo root level, which only denotes a filter.
			depth--;
		}
		else
		{
			// process non-root level member value

			String targetDataType = targetLevel.getDataType( );
			int dteDataType = DataAdapterUtil.adaptModelDataType( targetDataType );

			if ( depth > targetLevels.size( ) )
			{
				ILevelDefinition targetLevelDef = levelMapping.get( targetLevel );

				IDimensionDefinition targetDimDef = targetLevelDef.getHierarchy( )
						.getDimension( );

				targetLevels.add( modelAdapter.adaptJSExpression( ExpressionUtil.createJSDimensionExpression( targetDimDef.getName( ),
						targetLevelDef.getName( ) ),
						targetDataType ) );
			}

			String val = member.getValue( );

			// TODO check null val?
			matrixAdd( memberValues,
					memberFlags,
					depth,
					pos,
					modelAdapter.adaptJSExpression( ExpressionUtil.generateConstantExpr( val,
							dteDataType ),
							targetDataType ),
					false );
		}

		Iterator<FilterConditionHandle> filters = member.filtersIterator( );

		if ( filters == null || !filters.hasNext( ) )
		{
			return;
		}

		// TODO only can support first filter and OP_IN/OP_NOT_IN for now
		FilterConditionHandle fch = filters.next( );

		int dop = DataAdapterUtil.adaptModelFilterOperator( fch.getOperator( ) );

		assert dop == IConditionalExpression.OP_IN
				|| dop == IConditionalExpression.OP_NOT_IN
				|| dop == IConditionalExpression.OP_NONE;

		if ( op[0] == 0 )
		{
			// use the first op encountered
			op[0] = dop;
		}
		else if ( op[0] != dop )
		{
			// TODO throw exception?
		}

		// TODO only check value1 for now
		List<Expression> val1list = fch.getValue1ExpressionList( )
				.getListValue( );

		if ( val1list != null && val1list.size( ) > 0 )
		{
			if ( depth + 1 > targetLevels.size( ) )
			{
				targetLevels.add( modelAdapter.adaptExpression( (Expression) fch.getExpressionProperty( FilterCondition.EXPR_MEMBER )
						.getValue( ),
						ExpressionLocation.CUBE ) );
			}

			for ( Expression expr : val1list )
			{
				matrixAdd( memberValues,
						memberFlags,
						depth + 1,
						pos,
						modelAdapter.adaptExpression( expr,
								ExpressionLocation.CUBE ),
						true );
			}
		}

		// keep processing child members
		List children = member.getContents( IMemberValueModel.MEMBER_VALUES_PROP );

		if ( children != null )
		{
			for ( int i = 0; i < children.size( ); i++ )
			{
				MemberValueHandle child = (MemberValueHandle) children.get( i );

				if ( child != null )
				{
					traverseMemberFilter( targetLevels,
							op,
							memberValues,
							memberFlags,
							child,
							levelMapping,
							modelAdapter,
							depth + 1,
							pos );
				}
			}
		}
	}

	private static void matrixAdd( List<List<IScriptExpression>> values,
			List<List<Boolean>> flags, int depth, int[] pos,
			IScriptExpression val, boolean mark )
	{
		List<IScriptExpression> bucket;
		List<Boolean> flagBucket;

		if ( pos[0] > values.size( ) )
		{
			bucket = new ArrayList<IScriptExpression>( );
			values.add( bucket );

			flagBucket = new ArrayList<Boolean>( );
			flags.add( flagBucket );

			if ( pos[0] > 1 )
			{
				List<IScriptExpression> lastBucket = values.get( pos[0] - 2 );
				List<Boolean> lastFlagBucket = flags.get( pos[0] - 2 );

				for ( int i = 1; i < depth; i++ )
				{
					// copy the shared bucket values
					bucket.add( lastBucket.get( i - 1 ) );
					// also copy the flags
					flagBucket.add( lastFlagBucket.get( i - 1 ) );
				}
			}

			pos[0]++;
		}
		else
		{
			bucket = values.get( pos[0] - 1 );
			flagBucket = flags.get( pos[0] - 1 );
		}

		if ( depth > bucket.size( ) )
		{
			bucket.add( val );
			flagBucket.add( Boolean.valueOf( mark ) );
		}
		else
		{
			bucket.set( depth - 1, val );
			flagBucket.set( depth - 1, Boolean.valueOf( mark ) );
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

	/**
	 * !!!Note depth is 0-based for this method.
	 */
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
							.getValue( ),
							ExpressionLocation.CUBE ),
							levelMapping.get( lv.getCubeLevel( ) ),
							qualifyLevels,
							qualifyValues,
							DataAdapterUtil.adaptModelSortDirection( sortKey.getDirection( ) ) );

					sortDef.setSortLocale( sortKey.getLocale( ) );
					sortDef.setSortStrength( sortKey.getStrength( ) );

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
						List<ScriptExpression> vals = null;

						List<Expression> val1list = filterCon.getValue1ExpressionList( )
								.getListValue( );

						if ( val1list != null )
						{
							vals = new ArrayList<ScriptExpression>( );

							for ( Expression expr : val1list )
							{
								vals.add( modelAdapter.adaptExpression( expr,
										ExpressionLocation.CUBE ) );
							}
						}

						filterCondExpr = new ConditionalExpression( modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.EXPR_PROP )
								.getValue( ),
								ExpressionLocation.CUBE ),
								DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
								vals );
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
								.getValue( ),
								ExpressionLocation.CUBE ),
								DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
								modelAdapter.adaptExpression( value1,
										ExpressionLocation.CUBE ),
								modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.VALUE2_PROP )
										.getValue( ),
										ExpressionLocation.CUBE ) );
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
					List<ScriptExpression> vals = null;

					List<Expression> val1list = filterCon.getValue1ExpressionList( )
							.getListValue( );

					if ( val1list != null )
					{
						vals = new ArrayList<ScriptExpression>( );

						for ( Expression expr : val1list )
						{
							vals.add( modelAdapter.adaptExpression( expr,
									ExpressionLocation.CUBE ) );
						}
					}

					filterCondExpr = new ConditionalExpression( modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.EXPR_PROP )
							.getValue( ),
							ExpressionLocation.CUBE ),
							DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
							vals );
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
							.getValue( ),
							ExpressionLocation.CUBE ),
							DataAdapterUtil.adaptModelFilterOperator( filterCon.getOperator( ) ),
							modelAdapter.adaptExpression( value1,
									ExpressionLocation.CUBE ),
							modelAdapter.adaptExpression( (Expression) filterCon.getExpressionProperty( IFilterConditionElementModel.VALUE2_PROP )
									.getValue( ),
									ExpressionLocation.CUBE ) );
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
