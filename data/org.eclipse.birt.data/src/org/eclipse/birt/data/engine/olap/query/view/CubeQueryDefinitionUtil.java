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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.aggregation.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.impl.query.LevelDefiniton;
import org.eclipse.birt.data.engine.olap.impl.query.MeasureDefinition;
import org.eclipse.birt.data.engine.olap.util.ICubeAggrDefn;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IJSMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * Utility class
 *
 */
class CubeQueryDefinitionUtil
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
			ICubeQueryDefinition queryDefn, Scriptable scope, Map measureMapping ) throws DataException
	{
		List measureList = queryDefn.getMeasures( );
		ICubeAggrDefn[] cubeAggrs = OlapExpressionUtil.getAggrDefns( queryDefn.getBindings( ) );
		
		List cubeAggrBindingList = new ArrayList();
		for( int i=0; i< cubeAggrs.length; i++ )
		{
			if( cubeAggrs[i].getAggrName( ) != null )
				cubeAggrBindingList.add( cubeAggrs[i] );
		}
		
		populateMeasureFromBinding( queryDefn );
		populateMeasureFromFilter( queryDefn );
		populateMeasureFromSort( queryDefn );

		if ( measureList == null )
			return new CalculatedMember[0];
		List measureAggrOns = populateMeasureAggrOns( queryDefn );
		
		List unreferencedMeasures = getUnreferencedMeasures( queryDefn,
				measureList,
				measureMapping,
				measureAggrOns );
		CalculatedMember[] calculatedMember = new CalculatedMember[unreferencedMeasures.size( )
				+ cubeAggrBindingList.size( )];
		int index = 0;
		
		List calculatedMemberList = new ArrayList();
		if ( !unreferencedMeasures.isEmpty( ) )
		{
			Iterator measureIter = unreferencedMeasures.iterator( );
			while ( measureIter.hasNext( ) )
			{
				MeasureDefinition measureDefn = (MeasureDefinition) measureIter.next( );
				String innerName = OlapExpressionUtil.createMeasureCalculateMemeberName( measureDefn.getName( ) );
				measureMapping.put( measureDefn.getName( ), innerName );
				// all the measures will consume one result set, and the default
				// rsID is 0. If no unreferenced measures are found, the
				// bindings' start index of rsID will be 0
				calculatedMember[index] = new CalculatedMember( innerName,
						measureDefn.getName( ),
						measureAggrOns,
						adaptAggrFunction( measureDefn ),
						0 );
				calculatedMemberList.add( calculatedMember[index] );
				index++;
			}
		}
		
		if ( !cubeAggrBindingList.isEmpty( ) )
		{
			int rsID = index > 0 ? 1 : 0;
			for ( int i = 0; i < cubeAggrBindingList.size( ); i++ )
			{
				int id = getResultSetIndex( calculatedMemberList,
						( (ICubeAggrDefn) cubeAggrBindingList.get( i ) ).getAggrLevels( ) );
				if ( id == -1 )
				{
					calculatedMember[index] = new CalculatedMember( (ICubeAggrDefn) cubeAggrBindingList.get( i ),
							rsID );
					calculatedMemberList.add( calculatedMember[index] );
					rsID++;
				}
				else
				{
					calculatedMember[index] = new CalculatedMember( (ICubeAggrDefn) cubeAggrBindingList.get( i ),
							id );
				}

				if ( ( (ICubeAggrDefn) cubeAggrBindingList.get( i ) ).getFilter( ) != null )
				{
					IJSMeasureFilterEvalHelper filterEvalHelper = new JSMeasureFilterEvalHelper( scope,
							new FilterDefinition( ( (ICubeAggrDefn) cubeAggrBindingList.get( i ) ).getFilter( ) ) );
					calculatedMember[index].setFilterEvalHelper( filterEvalHelper );
				}
				index++;
			}
		}

		return calculatedMember;
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
				? IBuildInAggregation.TOTAL_SUM_FUNC
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
	private static void populateMeasureFromSort( ICubeQueryDefinition queryDefn )
			throws DataException
	{
		for ( int i = 0; i < queryDefn.getSorts( ).size( ); i++ )
		{
			createRelationalMeasures( queryDefn,
					(IBaseExpression) ( (ISortDefinition) queryDefn.getSorts( )
							.get( i ) ).getExpression( ) );
		}
	}

	/**
	 * To populate the relational measures from filter list of queryDefn
	 * 
	 * @param queryDefn
	 */
	private static void populateMeasureFromFilter(
			ICubeQueryDefinition queryDefn ) throws DataException
	{
		for ( int i = 0; i < queryDefn.getFilters( ).size( ); i++ )
		{
			createRelationalMeasures( queryDefn,
					(IBaseExpression) ( (IFilterDefinition) queryDefn.getFilters( )
							.get( i ) ).getExpression( ) );
		}
	}

	/**
	 * To populate the relational measures from binding list of queryDefn
	 * 
	 * @param queryDefn
	 */
	private static void populateMeasureFromBinding(
			ICubeQueryDefinition queryDefn ) throws DataException
	{
		for ( int i = 0; i < queryDefn.getBindings( ).size( ); i++ )
		{
			createRelationalMeasures( queryDefn,
					(IBaseExpression) ( (IBinding) queryDefn.getBindings( )
							.get( i ) ).getExpression( ) );
		}
	}

	/**
	 * To create all the rational measures for CubeQueryDefinition according to the expression
	 * 
	 * @param queryDefn, expression
	 * @return List
	 * @throws DataException 
	 */
	private static List createRelationalMeasures(
			ICubeQueryDefinition queryDefn, IBaseExpression expression )
			throws DataException
	{
		List measures = new ArrayList( );
		List exprTextList = getExprTextList( expression );
		for ( int i = 0; i < exprTextList.size( ); i++ )
		{
			String exprText = (String) exprTextList.get( i );
			String measureName = OlapExpressionCompiler.getReferencedScriptObject( exprText,
					ScriptConstants.MEASURE_SCRIPTABLE );
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
	private static List getExprTextList( IBaseExpression expression )
	{
		List textList = new ArrayList( );
		if ( expression instanceof IScriptExpression )
		{
			textList.add( ( (IScriptExpression) expression ).getText( ) );
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
			textList.add( ( (IScriptExpression) ( (IConditionalExpression) expression ).getExpression( ) ).getText( ) );
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

		for ( int i = 0; i < rowLevels.length; i++ )
		{
			levelList.add( new DimLevel( rowLevels[i] ) );
		}
		for ( int i = 0; i < columnLevels.length; i++ )
		{
			levelList.add( new DimLevel( columnLevels[i] ) );
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
			if ( member.getAggrOnList( ).equals( levelList ) )
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
	static ILevelDefinition[] getLevelsOnEdge( IEdgeDefinition edgeDefn )
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
	public static Map getRelationWithMeasure( ICubeQueryDefinition queryDefn, Map measureMapping ) throws DataException
	{
		Map measureRelationMap = new HashMap( );
		List rowLevelList = new ArrayList( );
		List columnLevelList = new ArrayList( );

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
						new RelationShip( rowLevelList, columnLevelList ) );
			}
		}
		ICubeAggrDefn[] cubeAggrs = OlapExpressionUtil.getAggrDefns( queryDefn.getBindings( ) );
		
		 if ( cubeAggrs != null && cubeAggrs.length > 0 )
		{
			for ( int i = 0; i < cubeAggrs.length; i++ )
			{
				if ( cubeAggrs[i].getAggrName( ) == null )
					continue;
				List aggrOns = cubeAggrs[i].getAggrLevels( );
				List usedLevelOnRow = new ArrayList( );
				List usedLevelOnColumn = new ArrayList( );
				for ( int j = 0; j < aggrOns.size( ); j++ )
				{
					if ( rowLevelList.contains( aggrOns.get( j ) ) )
						usedLevelOnRow.add( aggrOns.get( j ) );
					else if ( columnLevelList.contains( aggrOns.get( j ) ) )
						usedLevelOnColumn.add( aggrOns.get( j ) );
				}

				measureRelationMap.put( cubeAggrs[i].getName( ),
						new RelationShip( usedLevelOnRow, usedLevelOnColumn ) );
			}
		}
		return measureRelationMap;
	}
}
