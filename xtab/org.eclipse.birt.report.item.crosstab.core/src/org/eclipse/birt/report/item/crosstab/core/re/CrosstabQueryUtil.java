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
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
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
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.SortElementHandle;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
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

	/**
	 * @deprecated please use
	 *             {@link #createCubeQuery(CrosstabReportItemHandle, IDataQueryDefinition, IModelAdapter, boolean, boolean, boolean, boolean, boolean, boolean)}
	 */
	public static ICubeQueryDefinition createCubeQuery(
			CrosstabReportItemHandle crosstabItem,
			IDataQueryDefinition parentQuery, boolean needMeasure,
			boolean needRowDimension, boolean needColumnDimension,
			boolean needBinding, boolean needSorting, boolean needFilter )
			throws BirtException
	{
		DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );

		try
		{
			IModelAdapter modelAdapter = session.getModelAdaptor( );

			return createCubeQuery( crosstabItem,
					parentQuery,
					modelAdapter,
					needMeasure,
					needRowDimension,
					needColumnDimension,
					needBinding,
					needSorting,
					needFilter );
		}
		finally
		{
			session.shutdown( );
		}
	}

	public static ICubeQueryDefinition createCubeQuery(
			CrosstabReportItemHandle crosstabItem,
			IDataQueryDefinition parentQuery, IModelAdapter modelAdapter,
			boolean needMeasure, boolean needRowDimension,
			boolean needColumnDimension, boolean needBinding,
			boolean needSorting, boolean needFilter ) throws BirtException
	{
		ICubeQueryDefinition cubeQuery = getCubeElementFactory( ).createCubeQuery( crosstabItem.getCubeName( ) );
		cubeQuery.setID( String.valueOf(crosstabItem.getModelHandle( ).getID( )) );
		
		boolean isBoundToLinkedDataSet = CrosstabUtil.isBoundToLinkedDataSet( crosstabItem ) ;
		
		List<String> rowLevelNameList = new ArrayList<String>( );
		List<String> columnLevelNameList = new ArrayList<String>( );

		List<LevelViewHandle> levelViewList = new ArrayList<LevelViewHandle>( );
		Map<String, ILevelDefinition> levelMapping = new HashMap<String, ILevelDefinition>( );
		
		if ( needMeasure )
		{
			// add measure definitions
			for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
			{
				// TODO check visibility?
				MeasureViewHandle mv = crosstabItem.getMeasure( i );

				// add measure filters
				addFactTableOrMeasureFilter( mv.filtersIterator( ),
						cubeQuery,
						modelAdapter );

				if ( mv instanceof ComputedMeasureViewHandle 
						&& !CrosstabUtil.isLinkedDataModelMeasureView( mv ) )
				{
					continue;
				}

				if( isBoundToLinkedDataSet )
				{
					addLinkedDataModelMeasureDefinition( cubeQuery,
							crosstabItem,
							mv );
				}
				else
				{
					MeasureHandle mHandle = mv.getCubeMeasure( );
					if ( mHandle == null )
					{
						throw new CrosstabException( Messages.getString( "CrosstabQueryHelper.error.invalid.measure", //$NON-NLS-1$
								mv.getCubeMeasureName( ) ) );
					}
	
					if ( mHandle.isCalculated( ) )
					{
						cubeQuery.createDerivedMeasure( mHandle.getName( ),
								DataAdapterUtil.adaptModelDataType( mHandle.getDataType( ) ),
								modelAdapter.adaptExpression( (Expression) mHandle.getExpressionProperty( IMeasureModel.MEASURE_EXPRESSION_PROP )
										.getValue( ),
										ExpressionLocation.CUBE ) );
					}
					else
					{
						IMeasureDefinition mDef = cubeQuery.createMeasure( mHandle.getName( ) );
						mDef.setAggrFunction( DataAdapterUtil.getRollUpAggregationName( mHandle.getFunction( ) ) );
					}
				}
			}
		}

		// Crosstab binding expression map
		Map<String, String> exprMap = new HashMap<String, String>();
		if( isBoundToLinkedDataSet )
		{
			exprMap = CrosstabUtil.getBindingExpressMap( crosstabItem );
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
					exprMap,
					modelAdapter,
					isBoundToLinkedDataSet );
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
					exprMap,
					modelAdapter,
					isBoundToLinkedDataSet );
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

			if ( bindingItr != null )
			{
				Map<String, String> cache = new HashMap<String, String>( );

				while ( bindingItr.hasNext( ) )
				{
					ComputedColumnHandle column = (ComputedColumnHandle) bindingItr.next( );

					// now user dte model adpater to transform the binding
					IBinding binding = modelAdapter.adaptBinding( column,
							ExpressionLocation.CUBE );

					// still need add aggregateOn field
					List aggrList = column.getAggregateOnList( );

					if ( aggrList != null )
					{
						for ( Iterator aggrItr = aggrList.iterator( ); aggrItr.hasNext( ); )
						{
							String baseLevel = (String) aggrItr.next( );

							CrosstabUtil.addHierachyAggregateOn( crosstabItem,
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

	private static void addLinkedDataModelMeasureDefinition( ICubeQueryDefinition cubeQuery, 
			CrosstabReportItemHandle crosstabItem, 
			MeasureViewHandle mv )
	{
		String linkedColumnName = (mv instanceof ComputedMeasureViewHandle) ? null : mv.getCubeMeasureName( );
		CrosstabCellHandle cell = mv.getCell( );
		String measureBindingName = linkedColumnName;
		String aggrFunc = null;		
		if( cell != null )
		{
			List contents = cell.getContents( );
			for( Object obj : contents )
			{
				if( obj != null && obj instanceof DataItemHandle )
				{
					measureBindingName = ((DataItemHandle)obj).getResultSetColumn( );
					ComputedColumnHandle column = CrosstabUtil.getColumnHandle( crosstabItem, measureBindingName );
					aggrFunc = (column != null) ? column.getAggregateFunction( ) : null;
					if( CrosstabUtil.validateBinding( column, linkedColumnName ) )
					{
						break;
					}
				}
			}
		}
		
		if( measureBindingName != null )
		{
			IMeasureDefinition mDef = cubeQuery.createMeasure( measureBindingName );
			mDef.setAggrFunction( DataAdapterUtil.getRollUpAggregationName( aggrFunc ) );
		}
	}
	
	private static ILevelDefinition createLinkedDataModelLevelDefinition( IHierarchyDefinition hieDef, CrosstabReportItemHandle crosstabItem, 
			LevelViewHandle lv )
	{
		String levelBindingName = CrosstabUtil.getLevelBindingName( crosstabItem, lv, null, null );
		if( levelBindingName == null )
		{
			levelBindingName = CubeUtil.splitLevelName( lv.getCubeLevelName( ) )[1];
		}
		
		if( levelBindingName != null )
		{
			return hieDef.createLevel( levelBindingName ); 
		}
		
		return null;
	}
	
	private static void addEdgeDefinition( ICubeQueryDefinition cubeQuery,
			CrosstabReportItemHandle crosstabItem, int axis,
			List<String> levelNameList, List<LevelViewHandle> levelViewList,
			Map<String, ILevelDefinition> levelMapping, Map<String, String> exprMap,
			IModelAdapter modelAdapter, boolean isBoundToLinkedDataSet ) throws BirtException
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

				ILevelDefinition levelDef = null;
				if( isBoundToLinkedDataSet )
				{
					levelDef = createLinkedDataModelLevelDefinition( hieDef, crosstabItem, lv );
				}
				else
				{
					levelDef = hieDef.createLevel( lv.getCubeLevel( ).getName( ) );
				}
				
				levelNameList.add( lv.getCubeLevel( ).getFullName( ) );

				if ( mirrorLevel != null
						&& mirrorLevel.getQualifiedName( )
								.equals( lv.getCubeLevelName( ) ) )
				{
					edge.setMirrorStartingLevel( levelDef );
				}

				levelViewList.add( lv );
				levelMapping.put( lv.getCubeLevel( ).getFullName( ), levelDef );
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
					if( mvh == null )
					{
						continue;
					}
					
					LevelHandle lv = mvh.getLevel( );
					if( lv == null )
					{
						lv = crosstabItem.findLevelHandle( mvh.getCubeLevelName( ) );	
					}
					
					if ( lv != null )
					{
						addDrillDefinition( crosstabItem, edge, mvh, levelMapping );
					}
				}

				addEdgeMemberFilter( cubeQuery,
						crosstabItem,
						modelAdapter,
						members,
						levelMapping,
						exprMap );
			}
		}
	}

	private static void addEdgeMemberFilter( ICubeQueryDefinition cubeQuery, CrosstabReportItemHandle crosstabItem,
			IModelAdapter modelAdapter, List<MemberValueHandle> members,
			Map<String, ILevelDefinition> levelMapping, Map<String, String> exprMap )
			throws BirtException
	{
		List<List<IScriptExpression>> allTargetLevels = new ArrayList<List<IScriptExpression>>( );
		List<List<List<IScriptExpression>>> allMemberValues = new ArrayList<List<List<IScriptExpression>>>( );
		List<List<List<Boolean>>> allMemberFlags = new ArrayList<List<List<Boolean>>>( );
		int[] op = new int[]{
			0
		};

		Boolean[] updateAggFlag = new Boolean[]{
			null
		};

		for ( MemberValueHandle mvh : members )
		{
			if( mvh == null )
			{
				continue;
			}
			
			LevelHandle lv = mvh.getLevel( );
			if( lv == null )
			{
				lv = crosstabItem.findLevelHandle( mvh.getCubeLevelName( ) );	
			}
			
			if ( lv == null )
			{
				List<IScriptExpression> targetLevels = new ArrayList<IScriptExpression>( );
				List<List<IScriptExpression>> memberValues = new ArrayList<List<IScriptExpression>>( );
				List<List<Boolean>> memberFlags = new ArrayList<List<Boolean>>( );

				traverseMemberFilter( crosstabItem,
						targetLevels,
						op,
						updateAggFlag,
						memberValues,
						memberFlags,
						mvh,
						levelMapping,
						exprMap,
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

		// TODO data engine should provider way better API to avoid this crappy
		// logic.

		// merge all target levels into one single list
		List<String> keyList = new ArrayList<String>( );

		for ( int i = 0; i < allTargetLevels.size( ); i++ )
		{
			List<IScriptExpression> targetLevels = allTargetLevels.get( i );

			String startLevel = targetLevels.get( 0 ).getText( );

			int startIdx = keyList.indexOf( startLevel );

			if ( startIdx == -1 )
			{
				startIdx = keyList.size( );

				for ( IScriptExpression target : targetLevels )
				{
					keyList.add( target.getText( ) );

					mergedTargetLevels.add( target );
				}
			}

			List<List<IScriptExpression>> memberValues = allMemberValues.get( i );

			for ( int j = 0; j < memberValues.size( ); j++ )
			{
				List<IScriptExpression> bucket = memberValues.get( j );

				List<Boolean> flagBucket = allMemberFlags.get( i ).get( j );

				boolean effective = false;

				// check if this bucket is effective
				for ( Boolean mark : flagBucket )
				{
					if ( mark != null && mark.booleanValue( ) )
					{
						effective = true;
						break;
					}
				}

				if ( !effective )
				{
					continue;
				}

				if ( startIdx == 0 )
				{
					mergedMemberValues.add( bucket );
				}
				else
				{
					List<IScriptExpression> newBucket = new ArrayList<IScriptExpression>( );

					for ( int k = 0; k < startIdx; k++ )
					{
						// fill with placeholder
						newBucket.add( null );
					}

					newBucket.addAll( bucket );

					mergedMemberValues.add( newBucket );
				}
			}
		}

		int maxLen = keyList.size( );

		// normalize merged member values with same length
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

		if ( mergedMemberValues.size( ) > 0 )
		{
			IFilterDefinition memberFilter = getCubeElementFactory( ).creatLevelMemberFilterDefinition( mergedTargetLevels,
					op[0],
					mergedMemberValues );

			if ( updateAggFlag[0] != null )
			{
				memberFilter.setUpdateAggregation( updateAggFlag[0].booleanValue( ) );
			}

			cubeQuery.addFilter( memberFilter );
		}
	}

	/**
	 * !!!Note depth and pos is 1-based for this method.
	 */
	private static void traverseMemberFilter(CrosstabReportItemHandle crosstabItem,
			List<IScriptExpression> targetLevels, int[] op,
			Boolean[] updateAggFlag,
			List<List<IScriptExpression>> memberValues,
			List<List<Boolean>> memberFlags, MemberValueHandle member,
			Map<String, ILevelDefinition> levelMapping,
			Map<String, String> exprMap,
			IModelAdapter modelAdapter, int depth, int[] pos )
			throws BirtException
	{
		LevelHandle targetLevel = member.getLevel( );
		if( targetLevel == null )
		{
			targetLevel = crosstabItem.findLevelHandle( member.getCubeLevelName( ) );
		}
		
		if ( targetLevel == null )
		{
			// this must be the pseudo root level, which only denotes a filter.
			depth--;
		}
		else
		{
			boolean isLinkedDataModel = CrosstabUtil.isBoundToLinkedDataSet( crosstabItem ); 
					
			// process non-root level member value
			String targetDataType = targetLevel.getDataType( );
			int dteDataType = DataAdapterUtil.adaptModelDataType( targetDataType );

			if ( depth > targetLevels.size( ) )
			{
				ILevelDefinition targetLevelDef = levelMapping.get( targetLevel.getFullName( ) );

				IDimensionDefinition targetDimDef = targetLevelDef.getHierarchy( )
						.getDimension( );

				if( isLinkedDataModel )
				{
					DesignElementHandle deh = targetLevel.getContainer( ).getContainer( );
					String levelBindingName = targetLevelDef.getName( );
					String expr = exprMap.get( levelBindingName );
					if( expr == null )
					{
						expr = ExpressionUtil.createDataSetRowExpression( targetDimDef.getName( ) );
					}
					
					targetLevels.add( modelAdapter.adaptJSExpression( expr, targetDataType ) );
				}
				else
				{
					targetLevels.add( modelAdapter.adaptJSExpression( ExpressionUtil.createJSDimensionExpression( targetDimDef.getName( ),
							targetLevelDef.getName( ) ),
							targetDataType ) );
				}
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

		// !!!only check the flag on first filter for now though it's for entire
		// edge
		if ( updateAggFlag[0] == null )
		{
			boolean needUpdateAgg = fch.updateAggregation( );

			updateAggFlag[0] = Boolean.valueOf( needUpdateAgg );
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
					traverseMemberFilter( crosstabItem,
							targetLevels,
							op,
							updateAggFlag,
							memberValues,
							memberFlags,
							child,
							levelMapping,
							exprMap,
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

	private static void addDrillDefinition( CrosstabReportItemHandle crosstabItem, IEdgeDefinition edge,
			MemberValueHandle member,
			Map<String, ILevelDefinition> levelMapping )
	{
		IHierarchyDefinition targetHierarchy = null;
		String targetLevelName = null;
		List<List<Object>> values = new ArrayList<List<Object>>( );

		// the bucket to record output parameters
		Object[] output = new Object[]{
				targetLevelName, targetHierarchy
		};

		traverseDrillMember( crosstabItem, output, member, levelMapping, values, 0 );

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
	private static void traverseDrillMember( CrosstabReportItemHandle crosstabItem, Object[] output,
			MemberValueHandle member,
			Map<String, ILevelDefinition> levelMapping,
			List<List<Object>> values, int depth )
	{
		LevelHandle targetLevel = member.getLevel( );
		if( targetLevel == null )
		{
			targetLevel = crosstabItem.findLevelHandle( member.getCubeLevelName( ) );
		}
		
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

		// only add non-null values for normal cube
		// support null value for linked data model
		if ( val != null || CrosstabUtil.isBoundToLinkedDataSet( crosstabItem ) )
		{
			List<Object> vals = values.get( depth );

			vals.add( val );
		}

		ILevelDefinition targetLevelDef = levelMapping.get( targetLevel.getFullName( ) );

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
					traverseDrillMember( crosstabItem,
							output,
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
	private static void addMembers( Map<String, ILevelDefinition> levelMapping,
			List<ILevelDefinition> levels, List<Object> values,
			MemberValueHandle member )
	{
		if ( member != null )
		{			
			ILevelDefinition levelDef = levelMapping.get( member.getCubeLevelName( ) );
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
			Map<String, ILevelDefinition> levelMapping,
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
							levelMapping.get( lv.getCubeLevel( ).getFullName( ) ),
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
			Map<String, ILevelDefinition> levelMapping,
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
							levelMapping.get( lv.getCubeLevel( ).getFullName( ) ),
							qualifyLevels,
							qualifyValues,
							filterCon.updateAggregation( ) );

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
						null,
						filterCon.updateAggregation( ) );

				cubeQuery.addFilter( filterDef );
			}
		}
	}
}
