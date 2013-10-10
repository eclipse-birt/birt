/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.query.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeOperation;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.LevelDefiniton;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.MeasureDefinition;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.util.CubeAggrDefn;
import org.eclipse.birt.data.engine.olap.util.CubeAggrDefnOnMeasure;
import org.eclipse.birt.data.engine.olap.util.CubeRunningNestAggrDefn;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;
/**
 * Utility class
 *
 */
public class CubeQueryDefinitionUtil
{

	/**
	 * Populate all aggregation member in CubeQueryDefinition. For initial
	 * implementation: we only consider IMeasureDefintion we will take into
	 * consider to handle the aggregation definition in binding expression;
	 * 
	 * @param queryDefn
	 * @param measureMapping 
	 * @return
	 * @throws DataException 
	 */
	static CalculatedMember[] getCalculatedMembers(
			ICubeQueryDefinition queryDefn, Scriptable scope, Map measureMapping, ScriptContext cx ) throws DataException
	{
		CubeAggrDefn[] cubeAggrs = OlapExpressionUtil.getAggrDefns( queryDefn.getBindings( ) );

		populateMeasureFromBinding( queryDefn, cx );
		populateMeasureFromFilter( queryDefn, cx );
		populateMeasureFromSort( queryDefn, cx );

		CalculatedMember[] calculatedMembers1 = extractMeasure( queryDefn,
				measureMapping );

		int startRsId = calculatedMembers1.length == 0 ? 0 : 1;

		CalculatedMember[] calculatedMembers2 = createCalculatedMembersByAggrOnList( startRsId, calculatedMembers1, 
				cubeAggrs,
				scope,
				cx );

		return uniteCalculatedMember( calculatedMembers1, calculatedMembers2 );
	}

	private static CalculatedMember[] extractMeasure(
			ICubeQueryDefinition queryDefn, Map measureMapping )
			throws DataException
	{
		List measureList = queryDefn.getMeasures( );

		if ( measureList == null )
			return new CalculatedMember[0];
		List measureAggrOns = populateMeasureAggrOns( queryDefn );

		List unreferencedMeasures = getUnreferencedMeasures( queryDefn,
				measureList,
				measureMapping,
				measureAggrOns );
		CalculatedMember[] calculatedMembers1 = new CalculatedMember[unreferencedMeasures.size( )];
		int index = 0;

		Iterator measureIter = unreferencedMeasures.iterator( );
		while ( measureIter.hasNext( ) )
		{
			MeasureDefinition measureDefn = (MeasureDefinition) measureIter.next( );
			String innerName = OlapExpressionUtil.createMeasureCalculateMemeberName( measureDefn.getName( ) );
			measureMapping.put( measureDefn.getName( ), innerName );
			// all the measures will consume one result set, and the default
			// rsID is 0. If no unreferenced measures are found, the
			// bindings' start index of rsID will be 0
			calculatedMembers1[index] = new CalculatedMember( new CubeAggrDefnOnMeasure( innerName,
					measureDefn.getName( ),
					measureAggrOns,
					adaptAggrFunction( measureDefn ),
					null,
					null,
					null ),
					0 );
			index++;
		}
		return calculatedMembers1;
	}
	
	private static CalculatedMember[] createCalculatedMembersByAggrOnList(
			int startRsId, CalculatedMember[] calculatedMembers1, CubeAggrDefn[] cubeAggrs, Scriptable scope,
			ScriptContext cx ) throws DataException
	{
		if ( cubeAggrs == null )
		{
			return new CalculatedMember[0];
		}
		
		List<CalculatedMember> withDistinctRsIds = new ArrayList<CalculatedMember>( );
		if( calculatedMembers1!= null && calculatedMembers1.length>0 )
		{
			withDistinctRsIds.add( calculatedMembers1[0] );
		}

		assert startRsId >= 0;

		int preparedRsId = startRsId;
		CalculatedMember[] result = new CalculatedMember[cubeAggrs.length];
		int index = 0;
		for ( CubeAggrDefn cubeAggrDefn : cubeAggrs )
		{
			int id = getResultSetIndex( withDistinctRsIds,
					cubeAggrDefn.getAggrLevelsInAggregationResult( ) );
			if ( id == -1 )
			{
				result[index] = new CalculatedMember( cubeAggrDefn,
						preparedRsId );
				withDistinctRsIds.add( result[index] );
				preparedRsId++;
			}
			else
			{
				result[index] = new CalculatedMember( cubeAggrDefn, id );
			}

			if ( cubeAggrDefn.getFilter( ) != null )
			{
				IJSFacttableFilterEvalHelper filterEvalHelper = new JSFacttableFilterEvalHelper( scope, cx,
						new FilterDefinition( cubeAggrDefn.getFilter( ) ) , null, null );
				result[index].setFilterEvalHelper( filterEvalHelper );
			}
			index++;
		}
		return result;
	}

	public static boolean isRunnnigAggr( CubeAggrDefn cubeAggr ) throws DataException
	{
		return isRunnnigAggr( cubeAggr.getAggrName( ) );

	}
	
	public static boolean isRunnnigAggr( String aggrFunc ) throws DataException
	{
		IAggrFunction af = AggregationManager.getInstance( ).getAggregation( aggrFunc );
		return af != null && af.getType( ) == IAggrFunction.RUNNING_AGGR;
	}
	
	public static List<DrillOnDimensionHierarchy> flatternDrillFilter(
			IEdgeDefinition edge )
	{
		if ( edge == null || edge.getDrillFilter( ).isEmpty( ) )
			return Collections.EMPTY_LIST;

		List<IDimensionDefinition> dimensionList = edge.getDimensions( );
		List<DrillOnDimensionHierarchy> drillOnDimension = new ArrayList<DrillOnDimensionHierarchy>( );
		for ( int i = 0; i < dimensionList.size( ); i++ )
		{
			IDimensionDefinition dimension = dimensionList.get( i );
			IEdgeDrillFilter[] drill = edge.getDrillFilter( dimension );
			if ( drill.length > 0 )
				drillOnDimension.add( new DrillOnDimensionHierarchy( dimension,
						drill ) );
		}
		return drillOnDimension;
	}
		
	public static CalculatedMember[] addCalculatedMembers (CubeAggrDefn[] cubeAggrs, AggregationRegisterTable manager,
			Scriptable scope, ScriptContext cx ) throws DataException
	{
		if (cubeAggrs == null)
		{
			return new CalculatedMember[0];
		}
		CalculatedMember[] result = new CalculatedMember[cubeAggrs.length];
		int newRsId = manager.getBasedRsIndex( ) + 1;
		CalculatedMember newCm = null;
		int index = 0;
		for (CubeAggrDefn cubeAggrDefn : cubeAggrs)
		{
			int id = -1;
			if ( !isRunnnigAggr( cubeAggrDefn ) ) //Currently, no aggregation compose step for running type aggregation
			{
				for ( CalculatedMember cm : manager.getCalculatedMembers( ) )
				{
					if ( cm.getCubeAggrDefn( ).getAggrLevelsInAggregationResult( ).equals( cubeAggrDefn.getAggrLevelsInAggregationResult( ) ))
					{
						id = cm.getRsID( );
						break;
					}
				}
			}
			if ( id == -1 )
			{
				newCm = new CalculatedMember( cubeAggrDefn,
						newRsId );
				newRsId++;
			}
			else
			{
				newCm = new CalculatedMember( cubeAggrDefn,
						id );
			}

			if ( cubeAggrDefn.getFilter( ) != null )
			{
				IJSFacttableFilterEvalHelper filterEvalHelper = new JSFacttableFilterEvalHelper( scope, cx,
						new FilterDefinition( cubeAggrDefn.getFilter( ) ), null, null );
				newCm.setFilterEvalHelper( filterEvalHelper );
			}
			manager.addCalculatedMembersFromCubeOperation( new CalculatedMember[]{newCm} );
			result[index++] = newCm;
		}
		return result;
	}
	
	public static CalculatedMember[] uniteCalculatedMember(CalculatedMember[] array1, CalculatedMember[] array2)
	{
		assert array1 != null && array2 != null;
		int size = array1.length + array2.length;
		CalculatedMember[] result = new CalculatedMember[size];
		System.arraycopy( array1, 0, result, 0, array1.length );
		System.arraycopy( array2, 0, result, array1.length, array2.length );
		return result;
	}
	
	public static AggregationDefinition[] createAggregationDefinitons(CalculatedMember[] calculatedMembers,
			ICubeQueryDefinition query, Scriptable scope, ScriptContext cx) throws DataException
	{
		if (calculatedMembers == null)
		{
			return new AggregationDefinition[0];
		}
		List<AggregationDefinition> result = new ArrayList<AggregationDefinition>();
		Set<Integer> rsIDSet = new HashSet<Integer>( );
		for ( int i = 0; i < calculatedMembers.length; i++ )
		{
			if ( rsIDSet.contains( Integer.valueOf( calculatedMembers[i].getRsID( ) )))
			{
				continue;
			}
			List<CalculatedMember> list = getCalculatedMemberWithSameRSId( calculatedMembers, i );
			AggregationFunctionDefinition[] funcitons = new AggregationFunctionDefinition[list.size( )];
			for ( int index = 0; index < list.size( ); index++ )
			{
				String[] dimInfo = ( (CalculatedMember) list.get( index ) ).getCubeAggrDefn( ).getFirstArgumentInfo( );
				String dimName = null;
				String levelName = null;
				String attributeName = null;
				DimLevel dimLevel = null;
				if ( dimInfo != null && dimInfo.length == 3 )
				{
					dimName = dimInfo[0];
					levelName = dimInfo[1];
					attributeName = dimInfo[2];
					dimLevel = new DimLevel( dimName, levelName );
				}
				funcitons[index] = new AggregationFunctionDefinition( list.get( index ).getCubeAggrDefn( ).getName( ),
						list.get( index ).getCubeAggrDefn( ).getMeasure( ),
						dimLevel,
						attributeName,
						list.get( index ).getCubeAggrDefn( ).getAggrName( ),
						list.get( index ).getFilterEvalHelper( ) );
				
				ITimeFunction timeFunction = list.get( index ).getCubeAggrDefn( ).getTimeFunction( );
				if (timeFunction != null)
				{
					if( containsTimeDimension( query,list.get( index ).getCubeAggrDefn( ) ) )
					{
						funcitons[index].setTimeFunction( list.get( index ).getCubeAggrDefn( ).getTimeFunction( ) );					
					}
					else
					{
						funcitons[index].setTimeFunctionFilter( list.get( index ).getCubeAggrDefn( ).getTimeFunction( ) );					
					}
				}
				
				CubeAggrDefn cad = ( (CalculatedMember) list.get( index ) ).getCubeAggrDefn( );
				if ( cad instanceof CubeRunningNestAggrDefn )
				{
					//Special process to pass aggregation arguments for running type aggregation
					CubeRunningNestAggrDefn crnad = (CubeRunningNestAggrDefn) cad;
					if ( crnad.getNotLevelArguments( ) != null && !crnad.getNotLevelArguments( ).isEmpty( ))
					{
						IScriptExpression se = crnad.getNotLevelArguments( ).get( 0 );
						Object argumentValue = ScriptEvalUtil.evalExpr( se,
								cx.newContext( scope ),
								ScriptExpression.defaultID,
								0 );
						funcitons[index].setParaValue( argumentValue );
					}
				}
			}

			DimLevel[] levels = new DimLevel[calculatedMembers[i].getCubeAggrDefn( ).getAggrLevelsInDefinition( )
					.size( )];
			int[] sortType = new int[levels.length];
			for ( int index = 0; index < levels.length; index++ )
			{
				Object obj = calculatedMembers[i].getCubeAggrDefn( ).getAggrLevelsInDefinition( )
						.get( index );
				levels[index] = (DimLevel) obj;
				sortType[index] = getSortDirection( levels[index], query );
			}

			rsIDSet.add( Integer.valueOf(calculatedMembers[i].getRsID( ) ) );
			result.add(new AggregationDefinition( levels,
						sortType,
						funcitons ));
		}
		return result.toArray( new AggregationDefinition[0] );
	}
	
	private static boolean containsTimeDimension( ICubeQueryDefinition query, CubeAggrDefn aggrDefn ) throws DataException
 {
		ITimeFunction timeFunction = aggrDefn.getTimeFunction( );
		List onlevels = aggrDefn.getAggrLevelsInAggregationResult( );

		if ( query.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) != null )
		{
			IEdgeDefinition definition = query.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
			List<IDimensionDefinition> dimension = definition.getDimensions( );
			for ( int i = 0; i < dimension.size( ); i++ )
			{
				if ( dimension.get( i )
						.getName( )
						.equals( timeFunction.getTimeDimension( ) ) )
				{

					List<ILevelDefinition> levels = dimension.get( i )
							.getHierarchy( )
							.get( 0 )
							.getLevels( );
					for ( int j = 0; j < levels.size( ); j++ )
					{
						if ( onlevels.contains( new DimLevel( levels.get( j ) ) ) )
							return true;
					}
					return false;
				}
			}
		}
		if ( query.getEdge( ICubeQueryDefinition.ROW_EDGE ) != null )
		{
			IEdgeDefinition definition = query.getEdge( ICubeQueryDefinition.ROW_EDGE );
			List<IDimensionDefinition> dimension = definition.getDimensions( );
			for ( int i = 0; i < dimension.size( ); i++ )
			{
				if ( dimension.get( i )
						.getName( )
						.equals( timeFunction.getTimeDimension( ) ) )
				{
					List<ILevelDefinition> levels = dimension.get( i )
							.getHierarchy( )
							.get( 0 )
							.getLevels( );
					for ( int j = 0; j < levels.size( ); j++ )
					{
						if ( onlevels.contains( new DimLevel( levels.get( j ) ) ) )
							return true;
					}
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param levelDefn
	 * @param query
	 * @return
	 * @throws DataException 
	 */
	public static int getSortDirection( DimLevel level, ICubeQueryDefinition query ) throws DataException
	{
		if ( query.getSorts( ) != null && !query.getSorts( ).isEmpty( ) )
		{
			for ( int i = 0; i < query.getSorts( ).size( ); i++ )
			{
				ISortDefinition sortDfn = ( (ISortDefinition) query.getSorts( )
						.get( i ) );
				DimLevel info = null;
				if( sortDfn.getExpression( ) == null )
				{
					String colName = sortDfn.getColumn( );
					info = getDimLevelByBindingName( colName, query.getBindings( ) );
					if ( level.equals( info ) )
					{
						return sortDfn.getSortDirection( );
					}
				}
				else
				{
					String expr = null;
					expr = sortDfn.getExpression( ).getText( );
					info = getDimLevel( expr, query.getBindings( ) );

					if ( level.equals( info ) )
					{
						return sortDfn.getSortDirection( );
					}
				}	
			}
		}
		return IDimensionSortDefn.SORT_UNDEFINED;
	}
	
	/**
	 * Get dim level from an expression.
	 * @param expr
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	private static DimLevel getDimLevel( String expr, List bindings ) throws DataException
	{
		String bindingName = OlapExpressionUtil.getBindingName( expr );
		DimLevel dimLevel = getDimLevelByBindingName( bindingName, bindings );
		if( dimLevel != null )
			return dimLevel;
		if ( OlapExpressionUtil.isReferenceToDimLevel( expr ) == false )
			return null;
		else 
			return OlapExpressionUtil.getTargetDimLevel( expr );
	}

	private static DimLevel getDimLevelByBindingName( String bindingName, List bindings ) throws DataException
	{
		if( bindingName != null )
		{
			for( int j = 0; j < bindings.size( ); j++ )
			{
				IBinding binding = (IBinding)bindings.get( j );
				if( binding.getBindingName( ).equals( bindingName ))
				{
					if (! (binding.getExpression( ) instanceof IScriptExpression))
						return null;
					return getDimLevel( ((IScriptExpression)binding.getExpression( )).getText( ), bindings );
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param calMember
	 * @param index
	 * @return
	 */
	private static List<CalculatedMember> getCalculatedMemberWithSameRSId( CalculatedMember[] calMember,
			int index )
	{
		CalculatedMember member = calMember[index];
		List<CalculatedMember> list = new ArrayList<CalculatedMember>( );
		list.add( member );

		for ( int i = index + 1; i < calMember.length; i++ )
		{
			if ( calMember[i].getRsID( ) == member.getRsID( ) )
				list.add( calMember[i] );
		}
		return list;
	}
	
	/**
	 * 
	 * @param queryDefn
	 * @param levelExpr
	 * @param type
	 * @return
	 * @throws DataException
	 */
	static int getLevelIndex( ICubeQueryDefinition queryDefn, String levelExpr,
			int type ) throws DataException
	{
		int index = -1;
		DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel( levelExpr );
		IEdgeDefinition edgeDefn = queryDefn.getEdge( type );
		Iterator dimIter = edgeDefn.getDimensions( ).iterator( );
		while ( dimIter.hasNext( ) )
		{
			IDimensionDefinition dimDefn = (IDimensionDefinition) dimIter.next( );
			Iterator hierarchyIter = dimDefn.getHierarchy( ).iterator( );
			while ( hierarchyIter.hasNext( ) )
			{
				IHierarchyDefinition hierarchyDefn = (IHierarchyDefinition) hierarchyIter.next( );
				for ( int i = 0; i < hierarchyDefn.getLevels( ).size( ); i++ )
				{
					index++;
					ILevelDefinition levelDefn = (ILevelDefinition) hierarchyDefn.getLevels( )
							.get( i );
					if ( dimDefn.getName( )
							.equals( dimLevel.getDimensionName( ) ) &&
							levelDefn.getName( )
									.equals( dimLevel.getLevelName( ) ) )
					{
						return index;
					}
				}
			}
		}
		return -1;
	}
	
	/**
	 * used for backward capability.
	 * 
	 * @param measureDefn
	 * @return
	 */
	private static String adaptAggrFunction( MeasureDefinition measureDefn )
	{
		return measureDefn.getAggrFunction( ) == null
				? "SUM"
				: measureDefn.getAggrFunction( );
	}

	/**
	 * 
	 * @param queryDefn
	 * @param measureList
	 * @param measureMapping 
	 * @param measureAggrOns 
	 * @return
	 * @throws DataException 
	 */
	private static List getUnreferencedMeasures(
			ICubeQueryDefinition queryDefn, List measureList,
			Map measureMapping, List measureAggrOns ) throws DataException
	{
		List result = new ArrayList( );
		List bindings = queryDefn.getBindings( );
		for ( Iterator i = measureList.iterator( ); i.hasNext( ); )
		{
			MeasureDefinition measure = (MeasureDefinition) i.next( );
			if( measure.getAggrFunction() == null || measure.getAggrFunction().trim().length()==0 )
			{
				continue;
			}
			IBinding referenceBinding = getMeasureDirectReferenceBinding( measure,
					bindings,
					measureAggrOns );
			if ( referenceBinding != null )
			{
				measureMapping.put( measure.getName( ),
						referenceBinding.getBindingName( ) );
			}
			else
			{
				result.add( measure );
			}
		}
		return result;
	}

	/**
	 * get the binding that directly reference to the specified measure.
	 * 
	 * @param measure
	 * @param bindings
	 * @param measureAggrOns
	 * @return
	 * @throws DataException
	 */
	private static IBinding getMeasureDirectReferenceBinding( MeasureDefinition measure,
			List bindings, List measureAggrOns ) throws DataException
	{
		
		for ( Iterator i = bindings.iterator( ); i.hasNext( ); )
		{
			IBinding binding = (IBinding) i.next( );
			if ( binding.getAggregatOns( ).size( ) == measureAggrOns.size( ) )
			{
				String aggrFunction = adaptAggrFunction( measure );
				String funcName = binding.getAggrFunction( );
				if ( aggrFunction.equals( funcName ) )
				{
					IBaseExpression expression = binding.getExpression( );
					if ( expression instanceof IScriptExpression )
					{
						IScriptExpression expr = (IScriptExpression) expression;
						String measureName = OlapExpressionUtil.getMeasure( expr.getText( ) );
						if ( measure.getName( ).equals( measureName ) )
						{
							int t = 0;
							for ( ; t < measureAggrOns.size( ); t++ )
							{
								DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel( binding.getAggregatOns( )
										.get( t )
										.toString( ) );

								if ( !dimLevel.equals( measureAggrOns.get( t ) ) )
								{
									break;
								}
							}
							if ( t == measureAggrOns.size( ) )
								return binding;
						}
					}
				}
			}
		}
		return null;
	}

	/**
	 * To populate the relational measures from Sort list of queryDefn
	 * 
	 * @param queryDefn
	 */
	private static void populateMeasureFromSort( ICubeQueryDefinition queryDefn, ScriptContext cx )
			throws DataException
	{
		for ( int i = 0; i < queryDefn.getSorts( ).size( ); i++ )
		{
			createRelationalMeasures( queryDefn,
					(IBaseExpression) ( (ISortDefinition) queryDefn.getSorts( )
							.get( i ) ).getExpression( ), cx );
		}
	}

	/**
	 * To populate the relational measures from filter list of queryDefn
	 * 
	 * @param queryDefn
	 */
	private static void populateMeasureFromFilter(
			ICubeQueryDefinition queryDefn, ScriptContext cx ) throws DataException
	{
		for ( int i = 0; i < queryDefn.getFilters( ).size( ); i++ )
		{
			createRelationalMeasures( queryDefn,
					(IBaseExpression) ( (IFilterDefinition) queryDefn.getFilters( )
							.get( i ) ).getExpression( ), cx );
		}
	}

	/**
	 * To populate the relational measures from binding list of queryDefn
	 * 
	 * @param queryDefn
	 */
	private static void populateMeasureFromBinding(
			ICubeQueryDefinition queryDefn, ScriptContext cx ) throws DataException
	{
		for ( int i = 0; i < queryDefn.getBindings( ).size( ); i++ )
		{
			List measureList = createRelationalMeasures( queryDefn,
					(IBaseExpression) ( (IBinding) queryDefn.getBindings( )
							.get( i ) ).getExpression( ), cx );
			String functionName = ( (IBinding) queryDefn.getBindings( ).get( i ) ).getAggrFunction( );
			if( functionName != null )
			{
				for( int j = 0; j < measureList.size( ); j++ )
				{
					( ( IMeasureDefinition ) measureList.get( j ) ).setAggrFunction( functionName );
				}
			}
		}
	}

	/**
	 * Get the drilled target level for the drill operation.
	 * 
	 * @param drill
	 * @return
	 */
	public static List<DimLevel> getDrilledTargetLevels( IEdgeDrillFilter drill )
	{
		IHierarchyDefinition hierarchy = drill.getTargetHierarchy( );
		List levels = hierarchy.getLevels( );
		List<DimLevel> targetLevels = new ArrayList<DimLevel>( );
		for ( int j = 0; j < levels.size( ); j++ )
		{
			ILevelDefinition levelDefn = (ILevelDefinition) levels.get( j );
			targetLevels.add( new DimLevel( hierarchy.getDimension( ).getName( ),
					levelDefn.getName( ) ) );
			if ( levelDefn.getName( ).equals( drill.getTargetLevelName( ) ) )
			{
				break;
			}
		}
		return targetLevels;
	}

	/**
	 * To create all the rational measures for CubeQueryDefinition according to the expression
	 * 
	 * @param queryDefn, expression
	 * @return List
	 * @throws DataException 
	 */
	private static List createRelationalMeasures(
			ICubeQueryDefinition queryDefn, IBaseExpression expression, ScriptContext cx )
			throws DataException
	{
		List<IMeasureDefinition> measures = new ArrayList<IMeasureDefinition>( );
		List<IScriptExpression> exprTextList = getExprTextList( expression );
		for ( int i = 0; i < exprTextList.size( ); i++ )
		{
			IScriptExpression exprText = (IScriptExpression) exprTextList.get( i );
			String measureName = OlapExpressionCompiler.getReferencedScriptObject( exprText,
					ScriptConstants.MEASURE_SCRIPTABLE  );
			if ( measureName != null && measureName.trim( ).length( ) > 0 )
			{
				//if current measure list doesn't contain this measure, then add it to the list
				List existMeasures = queryDefn.getMeasures( );
				boolean exist = false;
				for ( int j = 0; j < existMeasures.size( ); j++ )
				{
					if ( ( (IMeasureDefinition) existMeasures.get( j ) ).getName( )
							.equals( measureName ) )
					{
						exist = true;
						break;
					}
				}				
				if ( !exist )
				{
					measures.add( queryDefn.createMeasure( measureName ) );
				}
			}
		}
		return measures;
	}
	
	/**
	 * To get all the sub expressions' text list of the given expression
	 * 
	 * @param queryDefn, expression
	 * @return List
	 */
	private static List<IScriptExpression> getExprTextList( IBaseExpression expression )
	{
		List<IScriptExpression> textList = new ArrayList<IScriptExpression>( );
		if ( expression instanceof IScriptExpression )
		{
			textList.add( (IScriptExpression) expression );
		}
		else if ( expression instanceof IExpressionCollection )
		{
			List exprList = (List) ( (IExpressionCollection) expression ).getExpressions( );
			for ( int i = 0; i < exprList.size( ); i++ )
			{
				IBaseExpression baseExpr = (IBaseExpression) exprList.get( i );
				textList.addAll( getExprTextList( baseExpr ) );
			}
		}
		else if ( expression instanceof IConditionalExpression )
		{
			textList.add( ( (IScriptExpression) ( (IConditionalExpression) expression ).getExpression( ) ) );
			textList.addAll( getExprTextList( ( (IConditionalExpression) expression ).getOperand1( ) ) );
			textList.addAll( getExprTextList( ( (IConditionalExpression) expression ).getOperand2( ) ) );
		}
		return textList;
	}
	
	/**
	 * Populate the list of measure aggregation ons.
	 * @param queryDefn
	 * @return
	 */
	public static List populateMeasureAggrOns( ICubeQueryDefinition queryDefn )
	{
		List levelList = new ArrayList( );
		ILevelDefinition[] rowLevels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) );
		ILevelDefinition[] columnLevels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) );
		ILevelDefinition[] pageLevels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.PAGE_EDGE ) );

		for ( int i = 0; i < rowLevels.length; i++ )
		{
			levelList.add( new DimLevel( rowLevels[i] ) );
		}
		for ( int i = 0; i < columnLevels.length; i++ )
		{
			levelList.add( new DimLevel( columnLevels[i] ) );
		}
		for ( int i = 0; i < pageLevels.length; i++ )
		{
			levelList.add( new DimLevel( pageLevels[i] ) );
		}
		return levelList;
	}
	
	private static List getReferenceDimLevelOnEdge(
			ICubeQueryDefinition queryDefn, int type )
	{
		List levelList = new ArrayList( );
		ILevelDefinition[] rowLevels = getLevelsOnEdge( queryDefn.getEdge( type ) );

		for ( int i = 0; i < rowLevels.length; i++ )
		{
			levelList.add( new DimLevel( rowLevels[i] ) );
		}
		return levelList;
	}
	
	/**
	 * 
	 * @param aggrList
	 * @param levelList
	 * @return
	 */
	private static int getResultSetIndex( List aggrList, List levelList )
	{
		for ( int i = 0; i < aggrList.size( ); i++ )
		{
			CalculatedMember member = (CalculatedMember) aggrList.get( i );
			if ( member.getCubeAggrDefn( ).getAggrLevelsInAggregationResult( ).equals( levelList ) )
			{
				return member.getRsID( );
			}
		}		
		return -1;
	}
	
	/**
	 * get all ILevelDefinition from certain IEdgeDefinition
	 * 
	 * @param edgeDefn
	 * @return
	 */
	public static ILevelDefinition[] getLevelsOnEdge( IEdgeDefinition edgeDefn )
	{
		if ( edgeDefn == null )
			return new ILevelDefinition[0];

		List levelList = new ArrayList( );
		Iterator dimIter = edgeDefn.getDimensions( ).iterator( );
		while ( dimIter.hasNext( ) )
		{
			IDimensionDefinition dimDefn = (IDimensionDefinition) dimIter.next( );
			Iterator hierarchyIter = dimDefn.getHierarchy( ).iterator( );
			while ( hierarchyIter.hasNext( ) )
			{
				IHierarchyDefinition hierarchyDefn = (IHierarchyDefinition) hierarchyIter.next( );
				levelList.addAll( hierarchyDefn.getLevels( ) );
			}
		}

		ILevelDefinition[] levelDefn = new LevelDefiniton[levelList.size( )];
		for ( int i = 0; i < levelList.size( ); i++ )
		{
			levelDefn[i] = (ILevelDefinition) levelList.get( i );
		}

		return levelDefn;
	}

	/**
	 * Get related level's info for all measure.
	 * 
	 * @param queryDefn
	 * @param measureMapping 
	 * @return
	 * @throws DataException 
	 */
	public static Map getRelationWithMeasure( ICubeQueryDefinition queryDefn, Map measureMapping,
			CubeAggrDefn[] aggrsFromCubeOperations ) throws DataException
	{
		Map measureRelationMap = new HashMap( );
		List pageLevelList = new ArrayList( );
		List rowLevelList = new ArrayList( );
		List columnLevelList = new ArrayList( );

		if( queryDefn.getEdge( ICubeQueryDefinition.PAGE_EDGE )!= null )
		{
			ILevelDefinition[] levels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.PAGE_EDGE ) );
			for ( int i = 0; i < levels.length; i++ )
			{
				pageLevelList.add( new DimLevel( levels[i] ) );
			}
		}
		if ( queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) != null )
		{
			ILevelDefinition[] levels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) );
			for ( int i = 0; i < levels.length; i++ )
			{
				columnLevelList.add( new DimLevel( levels[i] ) );
			}
		}

		if ( queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) != null )
		{
			ILevelDefinition[] levels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) );
			for ( int i = 0; i < levels.length; i++ )
			{
				rowLevelList.add( new DimLevel( levels[i] ) );
			}
		}
		
		if ( queryDefn.getMeasures( ) != null
				&& !queryDefn.getMeasures( ).isEmpty( ) )
		{
			Iterator measureIter = queryDefn.getMeasures( ).iterator( );
			while ( measureIter.hasNext( ) )
			{
				IMeasureDefinition measure = (MeasureDefinition) measureIter.next( );
				measureRelationMap.put( measureMapping.get( measure.getName( ) ),
						new Relationship( rowLevelList, columnLevelList, pageLevelList ) );
			}
		}
		List orignalBindings = queryDefn.getBindings( );
		List newBindings =  getNewBindingsFromCubeOperations(queryDefn);
		CubeAggrDefn[] cubeAggrs1 = OlapExpressionUtil.getAggrDefns( orignalBindings );
		CubeAggrDefn[] cubeAggrs = new CubeAggrDefn[cubeAggrs1.length + aggrsFromCubeOperations.length];
		System.arraycopy( cubeAggrs1, 0, cubeAggrs, 0, cubeAggrs1.length );
		System.arraycopy( aggrsFromCubeOperations, 0, cubeAggrs, cubeAggrs1.length, aggrsFromCubeOperations.length );
		 if ( cubeAggrs != null && cubeAggrs.length > 0 )
		{
			for ( int i = 0; i < cubeAggrs.length; i++ )
			{
				if ( cubeAggrs[i].getAggrName( ) == null )
					continue;
				List aggrOns = cubeAggrs[i].getAggrLevelsInAggregationResult( );
				List usedLevelOnRow = new ArrayList( );
				List usedLevelOnColumn = new ArrayList( );
				List usedLevelOnPage = new ArrayList( );
				for ( int j = 0; j < aggrOns.size( ); j++ )
				{
					if ( pageLevelList.contains( aggrOns.get( j ) ) )
					{
						usedLevelOnPage.add( aggrOns.get( j ) );
					}
					if ( rowLevelList.contains( aggrOns.get( j ) ) )
					{
						usedLevelOnRow.add( aggrOns.get( j ) );
					}
					else if ( columnLevelList.contains( aggrOns.get( j ) ) )
					{
						usedLevelOnColumn.add( aggrOns.get( j ) );
					}
				}
				measureRelationMap.put( cubeAggrs[i].getName( ),
						new Relationship( usedLevelOnRow, usedLevelOnColumn, usedLevelOnPage ) );
			}
		}
		return measureRelationMap;
	}
	
	public static List<IBinding> getNewBindingsFromCubeOperations(ICubeQueryDefinition cubeQueryDefn)
	{
		List<IBinding> list = new ArrayList<IBinding>();
		for (ICubeOperation co : cubeQueryDefn.getCubeOperations( ))
		{
			IBinding[] newBindings = co.getNewBindings( );
			list.addAll( Arrays.asList( newBindings ) );
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static List<IBinding> getAllBindings(ICubeQueryDefinition cubeQueryDefn)
	{
		List<IBinding> result = new ArrayList();
		result.addAll( cubeQueryDefn.getBindings( ) );
		result.addAll( getNewBindingsFromCubeOperations(cubeQueryDefn)  );
		return result;
	}
	
	public static DimLevel[] getAggregationLevels( String bindingName, ICubeQueryDefinition cubeQuery ) throws DataException
	{
		List bindingList = getAllBindings( cubeQuery );		
		for ( Iterator it = bindingList.iterator( ); it.hasNext( ); )
		{
			IBinding binding = (IBinding) it.next( );
			if ( binding.getBindingName( ).equals( bindingName ) )
			{
				return getAggregationOnsForBinding( binding, cubeQuery );
			}
		}
		return new DimLevel[0];
	}

	public static DimLevel[] getAggregationOnsForBinding( IBinding binding, ICubeQueryDefinition cubeQuery )
			throws DataException
	{
		List aggrs = binding.getAggregatOns( );
		if ( aggrs.size( ) == 0 )
		{
			if ( OlapExpressionCompiler.getReferencedScriptObject( binding.getExpression( ),
					ScriptConstants.DIMENSION_SCRIPTABLE ) != null )
				return null;

			IBinding directReferenceBinding = OlapExpressionUtil.getDirectMeasureBinding( binding, cubeQuery.getBindings( ) );
			if ( directReferenceBinding != null && directReferenceBinding!= binding )
			{
				return getAggregationLevels( directReferenceBinding.getBindingName( ), cubeQuery );
			}
			// get all level names in the query definition
			return (DimLevel[])( CubeQueryDefinitionUtil.populateMeasureAggrOns( cubeQuery ).toArray( new DimLevel[0] ));
		}
		else
		{
			DimLevel[] levels = new DimLevel[aggrs.size( )];
			for ( int i = 0; i < aggrs.size( ); i++ )
			{
				levels[i] = OlapExpressionUtil.getTargetDimLevel( aggrs.get( i )
						.toString( ) );
			}
			return levels;
		}
	}

	/**
	 * 
	 * @param levelList
	 * @param edge
	 */
	private void populateDimLevel( List levelList, IEdgeDefinition edge )
	{
		if( edge == null )
			return;
		List rowDims = edge.getDimensions( );
		for ( Iterator i = rowDims.iterator( ); i.hasNext( ); )
		{
			IDimensionDefinition dim = (IDimensionDefinition) i.next( );
			IHierarchyDefinition hirarchy = (IHierarchyDefinition) dim.getHierarchy( )
					.get( 0 );
			for ( Iterator j = hirarchy.getLevels( ).iterator( ); j.hasNext( ); )
			{
				ILevelDefinition level = (ILevelDefinition) j.next( );
				levelList.add( new DimLevel( dim.getName( ),
						level.getName( ) ) );
			}
		}
	}
}
