/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.cursor.DrilledAggregateResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;


public class DrillOperationExecutor
{

	public IAggregationResultSet[] execute(
			IAggregationResultSet[] aggregationRsFromCube,
			ICubeQueryDefinition iCubeQueryDefinition ) throws DataException,
			IOException
	{
		IEdgeDefinition columnEdge = iCubeQueryDefinition.getEdge( ICubeQueryDefinition.COLUMN_EDGE );
		IEdgeDefinition rowEdge = iCubeQueryDefinition.getEdge( ICubeQueryDefinition.ROW_EDGE );
		List<IEdgeDrillFilter[]> columnDrill = CubeQueryDefinitionUtil.flatternDrillFilter( columnEdge );
		List<IEdgeDrillFilter[]> rowDrill = CubeQueryDefinitionUtil.flatternDrillFilter( rowEdge );
		List<IEdgeDrillFilter[]> combinedDrill = new ArrayList<IEdgeDrillFilter[]>( );
		combinedDrill.addAll( rowDrill );
		combinedDrill.addAll( columnDrill );

		int index = 0;
		if ( columnEdge != null )
		{
			if ( !columnEdge.getDrillFilter( ).isEmpty( ) )
			{
				IAggregationResultSet rs = populateResultSet( aggregationRsFromCube[index],
						columnDrill );
				aggregationRsFromCube[index] = rs;
			}
			index++;
		}
		if ( rowEdge != null )
		{
			if( !rowEdge.getDrillFilter( ).isEmpty( ) )
			{
				IAggregationResultSet rs = populateResultSet( aggregationRsFromCube[index],
						rowDrill );
				aggregationRsFromCube[index] = rs;				
			}
			index++;
		}

		for ( int i = index; i < aggregationRsFromCube.length; i++ )
		{
			if ( !combinedDrill.isEmpty( ) )
			{
				IAggregationResultSet rs = populateResultSet( aggregationRsFromCube[i],
						combinedDrill );
				aggregationRsFromCube[i] = rs;
			}
		}
		return aggregationRsFromCube;
	}

	private IAggregationResultSet populateResultSet(
			IAggregationResultSet aggregationRsFromCube,
			List<IEdgeDrillFilter[]> drillFilters ) throws IOException, DataException
	{
		if ( aggregationRsFromCube.getAllLevels( ) == null
				|| aggregationRsFromCube.getAllLevels( ).length == 0
				|| aggregationRsFromCube.length( ) == 0 )
			return aggregationRsFromCube;
		DrilledAggregateResultSet rs = new DrilledAggregateResultSet( aggregationRsFromCube,
				drillFilters );
		return rs;
	}
}
