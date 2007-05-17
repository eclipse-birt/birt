
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
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.impl.facttable.IFactTableRowIterator;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

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
	private IAggregationResultRow currentResultObj = null;
	
	private static Logger logger = Logger.getLogger( AggregationCalculator.class.getName( ) );

	/**
	 * 
	 * @param aggregation
	 * @param facttableRowIterator
	 * @throws DataException 
	 */
	AggregationCalculator( AggregationDefinition aggregation, IFactTableRowIterator facttableRowIterator ) throws DataException
	{
		Object[] params = {
				aggregation, facttableRowIterator
		};
		logger.entering( AggregationCalculator.class.getName( ),
				"AggregationCalculator",
				params );
		this.aggregation = aggregation;
		AggregationFunctionDefinition[] aggregationFunction = aggregation.getAggregationFunctions( );
		if(aggregation.getLevels( )==null)
			this.levelCount = 0;
		else
			this.levelCount = aggregation.getLevels( ).length;
		if ( aggregationFunction != null )
		{
			this.accumulators = new Accumulator[aggregationFunction.length];
			this.measureIndex = new int[aggregationFunction.length];

			for ( int i = 0; i < aggregationFunction.length; i++ )
			{
				this.accumulators[i] = BuiltInAggregationFactory.getInstance( )
						.getAggregation( aggregationFunction[i].getFunctionName( ) )
						.newAccumulator( );
				this.accumulators[i].start( );
				this.measureIndex[i] = facttableRowIterator.getMeasureIndex( aggregationFunction[i].getMeasureName( ) );
				if ( this.measureIndex[i] == -1 )
				{
					throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
							aggregationFunction[i].getMeasureName( ) );
				}
			}
		}
		result = new BufferedStructureArray( AggregationResultRow.getCreator( ), Constants.LIST_BUFFER_SIZE );
		logger.exiting( AggregationCalculator.class.getName( ),
				"AggregationCalculator" );
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
			if ( currentResultObj.getLevelMembers() == null
					|| compare( row.getLevelMembers(), currentResultObj.getLevelMembers() ) == 0 )
			{
				if ( accumulators != null )
				{
					for ( int i = 0; i < accumulators.length; i++ )
					{
						accumulators[i].onRow( new Object[]{
							row.getMeasures()[measureIndex[i]]
						} );
					}
				}
			}
			else
			{
				if ( accumulators != null )
				{
					currentResultObj.setAggregationValues( new Object[accumulators.length] );
					for ( int i = 0; i < accumulators.length; i++ )
					{
						accumulators[i].finish( );
						currentResultObj.getAggregationValues()[i] = accumulators[i].getValue( );
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
			currentResultObj.setAggregationValues( new Object[accumulators.length] );
			for ( int i = 0; i < accumulators.length; i++ )
			{
				accumulators[i].finish( );
				currentResultObj.getAggregationValues()[i] = accumulators[i].getValue( );
				accumulators[i].start( );
			}
		}
		if ( currentResultObj != null )
			result.add( currentResultObj );
		else
			result.add( new AggregationResultRow( ) );
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
			currentResultObj.setLevelMembers( new Member[levelCount] );
			System.arraycopy( row.getLevelMembers(),
					0,
					currentResultObj.getLevelMembers(),
					0,
					currentResultObj.getLevelMembers().length );
		}
		if ( accumulators != null )
		{
			for ( int i = 0; i < accumulators.length; i++ )
			{
				accumulators[i].onRow( new Object[]{
					row.getMeasures()[measureIndex[i]]
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
