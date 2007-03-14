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

import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.api.query.impl.LevelDefiniton;
import org.eclipse.birt.data.engine.olap.api.query.impl.MeasureDefinition;

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
	 * @return
	 */
	static CalculatedMember[] getCalculatedMembers(
			ICubeQueryDefinition queryDefn )
	{
		List measureList = queryDefn.getMeasures( );
		// List bindingList = queryDefn.getBindings( );

		if ( measureList == null )
			return new CalculatedMember[0];

		CalculatedMember[] calculatedMember = new CalculatedMember[measureList.size( )];
		int index = 0;
		if ( !measureList.isEmpty( ) )
		{
			List levelNameList = new ArrayList( );
			ILevelDefinition[] rowLevels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) );
			ILevelDefinition[] columnLevels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) );

			for ( int i = 0; i < rowLevels.length; i++ )
			{
				levelNameList.add( rowLevels[i].getName( ) );
			}
			for ( int i = 0; i < columnLevels.length; i++ )
			{
				levelNameList.add( columnLevels[i].getName( ) );
			}

			Iterator measureIter = measureList.iterator( );
			while ( measureIter.hasNext( ) )
			{
				MeasureDefinition measureDefn = (MeasureDefinition) measureIter.next( );

				calculatedMember[index] = new CalculatedMember( measureDefn.getName( ),
						levelNameList,
						BuiltInAggregationFactory.TOTAL_SUM_FUNC,
						true );
				index++;
			}
		}

		// TODO
		// if ( queryDefn.getBindings( ) != null
		// && !queryDefn.getBindings( ).isEmpty( ) )
		// {
		// IBinding binding;
		//
		// for ( int i = 0; i < bindingList.size( ); i++ )
		// {
		// binding = (IBinding) bindingList.get( i );
		// calculatedMember[index] = new CalculatedMember(
		// binding.getBindingName( ),
		// binding.getExpression( ),
		// binding.getAggregatOns( ),
		// binding.getAggrFunction( ),
		// false );
		// index++;
		// }
		// }
		return calculatedMember;
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
	 * @return
	 */
	public static Map getRelationWithMeasure( ICubeQueryDefinition queryDefn )
	{
		Map measureRelationMap = new HashMap( );
		List rowLevelList = new ArrayList( );
		List columnLevelList = new ArrayList( );

		if ( queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) != null )
		{
			ILevelDefinition[] levels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.COLUMN_EDGE ) );
			for ( int i = 0; i < levels.length; i++ )
			{
				columnLevelList.add( levels[i].getName( ) );
			}
		}

		if ( queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) != null )
		{
			ILevelDefinition[] levels = getLevelsOnEdge( queryDefn.getEdge( ICubeQueryDefinition.ROW_EDGE ) );
			for ( int i = 0; i < levels.length; i++ )
			{
				rowLevelList.add( levels[i].getName( ) );
			}
		}

		if ( queryDefn.getMeasures( ) != null
				&& !queryDefn.getMeasures( ).isEmpty( ) )
		{
			Iterator measureIter = queryDefn.getMeasures( ).iterator( );
			while ( measureIter.hasNext( ) )
			{
				IMeasureDefinition measure = (MeasureDefinition) measureIter.next( );
				measureRelationMap.put( measure.getName( ),
						new RelationShip( rowLevelList, columnLevelList ) );
			}
		}
		// TODO
		// if ( queryDefn.getBindings( ) != null
		// && !queryDefn.getBindings( ).isEmpty( ) )
		// {
		// Iterator measureIter = queryDefn.getBindings( ).iterator( );
		// IBinding binding;
		// List usedLevelOnRow, usedLevelOnColumn;
		// while ( measureIter.hasNext( ) )
		// {
		// binding = (IBinding) measureIter.next( );
		// List aggrOns = binding.getAggregatOns( );
		// usedLevelOnRow = new ArrayList( );
		// usedLevelOnColumn = new ArrayList( );
		// for ( int i = 0; i < aggrOns.size( ); i++ )
		// {
		// if ( rowLevelList.contains( aggrOns.get( i ) ) )
		// usedLevelOnRow.add( aggrOns.get( i ) );
		// else if ( columnLevelList.contains( aggrOns.get( i ) ) )
		// usedLevelOnColumn.add( aggrOns.get( i ) );
		// }
		//
		// measureRelationMap.put( binding.getBindingName( ),
		// new RelationShip( usedLevelOnRow, usedLevelOnColumn ) );
		//			}
		//		}
		return measureRelationMap;
	}
}
