
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.FactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * The AggregationCalculator class calculates values for its associated
 * Aggregation.
 */

public class AggregationCalculator
{
	AggregationDefinition aggregation;
	private Accumulator[] accumulators;
	private int levelCount;
	private int[] measureIndex;
	private IDiskArray result = null;
	private AggregationResultRow currentResultObj = null;
	
	/**
	 * 
	 * @param aggregation
	 * @param facttableRowIterator
	 * @throws DataException 
	 */
	AggregationCalculator( AggregationDefinition aggregation, FactTableRowIterator facttableRowIterator ) throws DataException
	{
		this.aggregation = aggregation;
		AggregationFunctionDefinition[] aggregationFunction = aggregation.getAggregationFunctions( );
		if(aggregation.getLevelNames( )==null)
			this.levelCount = 0;
		else
			this.levelCount = aggregation.getLevelNames( ).length;
		if ( aggregationFunction != null )
		{
			this.accumulators = new Accumulator[aggregationFunction.length];
			this.measureIndex = new int[aggregationFunction.length];

			for ( int i = 0; i < aggregationFunction.length; i++ )
			{
				this.accumulators[i] = BuiltInAggregationFactory.getInstance( )
						.getAggregation( aggregationFunction[i].getFunctionName( ) )
						.newAccumulator( );
				this.measureIndex[i] = facttableRowIterator.getMeasureIndex( aggregationFunction[i].getMeasureName( ) );
				if ( this.measureIndex[i] == -1 )
				{
					throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
							aggregationFunction[i].getMeasureName( ) );
				}
			}
		}
		result = new BufferedStructureArray( AggregationResultRow.getCreator( ), Constants.LIST_BUFFER_SIZE );
	}
	
	/**
	 * 
	 * @param row
	 * @throws IOException
	 * @throws DataException
	 */
	void onRow( Row4Aggregation row ) throws IOException, DataException
	{
		if ( currentResultObj == null )
		{
			newAggregationResultRow( row );
		}
		else
		{
			if ( currentResultObj.levelMembers == null
					|| compare( row.levelMembers, currentResultObj.levelMembers ) == 0 )
			{
				if ( accumulators != null )
				{
					for ( int i = 0; i < accumulators.length; i++ )
					{
						accumulators[i].onRow( new Object[]{
							row.measures[measureIndex[i]]
						} );
					}
				}
			}
			else
			{
				if ( accumulators != null )
				{
					currentResultObj.aggregationValues = new Object[accumulators.length];
					for ( int i = 0; i < accumulators.length; i++ )
					{
						accumulators[i].finish( );
						currentResultObj.aggregationValues[i] = accumulators[i].getValue( );
						accumulators[i].start( );
					}
				}
				result.add( currentResultObj );
				newAggregationResultRow( row );
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	IDiskArray getResult( ) throws IOException, DataException
	{
		if ( currentResultObj != null && accumulators != null )
		{
			currentResultObj.aggregationValues = new Object[accumulators.length];
			for ( int i = 0; i < accumulators.length; i++ )
			{
				accumulators[i].finish( );
				currentResultObj.aggregationValues[i] = accumulators[i].getValue( );
				accumulators[i].start( );
			}
		}
		result.add( currentResultObj );
		return this.result;
	}
	
	/**
	 * 
	 * @param row
	 * @throws DataException
	 */
	private void newAggregationResultRow( Row4Aggregation row ) throws DataException
	{
		currentResultObj = new AggregationResultRow( );
		if ( levelCount > 0 )
		{
			currentResultObj.levelMembers = new Member[levelCount];
			System.arraycopy( row.levelMembers,
					0,
					currentResultObj.levelMembers,
					0,
					currentResultObj.levelMembers.length );
		}
		if ( accumulators != null )
		{
			for ( int i = 0; i < accumulators.length; i++ )
			{
				accumulators[i].onRow( new Object[]{
					row.measures[measureIndex[i]]
				} );
			}
		}
	}
	
	/**
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	private static int compare( Object[] key1, Object[] key2 )
	{
		for ( int i = 0; i < key1.length; i++ )
		{
			int result = ( (Comparable) key1[i] ).compareTo( key2[i] );
			if ( result < 0 )
			{
				return result;
			}
			else if ( result > 0 )
			{
				return result;
			}
		}
		return 0;
	}
}
