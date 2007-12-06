
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
package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.aggregation.AggregationFactory;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IMeasureDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.script.ScriptConstants;

/**
 * 
 */

public class OlapQueryUtil
{
	/**
	 * Valid bindings, return a list of invalid binding.
	 * 
	 * @param queryDefn
	 * @param suppressException
	 * @return
	 * @throws DataException
	 */
	public static List validateBinding( ICubeQueryDefinition queryDefn, boolean suppressException ) throws DataException
	{
		List result = new ArrayList();
		Set validMeasures = new HashSet();
		for( int i = 0; i < queryDefn.getMeasures( ).size( ); i++ )
		{
			IMeasureDefinition measure = (IMeasureDefinition) queryDefn.getMeasures( ).get( i );
			validMeasures.add( measure.getName( ) );
		}
		
		Set validDimLevels = new HashSet();

		populateLevel( queryDefn, validDimLevels, ICubeQueryDefinition.COLUMN_EDGE );
		populateLevel( queryDefn, validDimLevels, ICubeQueryDefinition.ROW_EDGE );
		
		for( int i = 0; i < queryDefn.getBindings( ).size( ); i++ )
		{
			boolean isValid = true;
			IBinding binding = (IBinding)queryDefn.getBindings( ).get(i);
			
			if ( binding.getAggrFunction( ) != null &&
					binding.getExpression( ) instanceof IScriptExpression )
			{
				String expr = ( (IScriptExpression) binding.getExpression( ) ).getText( );
				if ( expr == null &&
						( AggregationFactory.getInstance( )
								.getAggrInfo( binding.getAggrFunction( ) ) != null && !AggregationFactory.getInstance( )
								.getAggrInfo( binding.getAggrFunction( ) )
								.needDataField( ) ) )
					continue;
			}
			
			Set levels = OlapExpressionCompiler.getReferencedDimLevel( binding.getExpression( ), queryDefn.getBindings( ) );
			if( ! validDimLevels.containsAll( levels ))
			{	
				isValid = false;
				if( !suppressException )
					throw new DataException( ResourceConstants.INVALID_BINDING_REFER_TO_INEXIST_DIMENSION,
						binding.getBindingName( ) );
			}
			
			String measureName = OlapExpressionCompiler.getReferencedScriptObject( binding.getExpression( ),
					ScriptConstants.MEASURE_SCRIPTABLE );
			if ( measureName != null && !validMeasures.contains( measureName ) )
			{
				isValid = false;
				if( !suppressException )
				throw new DataException( ResourceConstants.INVALID_BINDING_REFER_TO_INEXIST_MEASURE,
						binding.getBindingName( ) );
			}

			if ( ( binding.getAggregatOns( ).size( ) > 0
					&& binding.getAggrFunction( ) == null ) )
			{
				isValid = false;
				if( !suppressException )
				throw new DataException( ResourceConstants.INVALID_BINDING_MISSING_AGGR_FUNC,
						binding.getBindingName( ) );
			}
			
			if( !isValid )
				result.add( binding );
		}
	
		return result;
	}

	/**
	 * 
	 * @param validDimLevels
	 * @param edgeType
	 */
	private static void populateLevel( ICubeQueryDefinition queryDefn, Set validDimLevels, int edgeType )
	{
		if ( queryDefn.getEdge( edgeType ) == null )
			return;
		for( int i = 0; i < queryDefn.getEdge( edgeType ).getDimensions( ).size( ); i++ )
		{
			for( int j = 0; j < getHierarchy( queryDefn, edgeType, i ).getLevels( ).size( );j++)
			{
				ILevelDefinition level = (ILevelDefinition)getHierarchy( queryDefn, edgeType, i ).getLevels( ).get( j );
				validDimLevels.add( new DimLevel( getDimension( queryDefn, edgeType, i ).getName( ), level.getName( )) );
			}
		}
	}

	/**
	 * 
	 * @param edgeType
	 * @param i
	 * @return
	 */
	private static IHierarchyDefinition getHierarchy( ICubeQueryDefinition queryDefn, int edgeType, int i )
	{
		return ((IHierarchyDefinition)(getDimension( queryDefn, edgeType, i )).getHierarchy( ).get( 0 ));
	}

	/**
	 * 
	 * @param edgeType
	 * @param i
	 * @return
	 */
	private static IDimensionDefinition getDimension( ICubeQueryDefinition queryDefn, int edgeType, int i )
	{
		return (IDimensionDefinition)queryDefn.getEdge( edgeType ).getDimensions( ).get( i );
	}
}
