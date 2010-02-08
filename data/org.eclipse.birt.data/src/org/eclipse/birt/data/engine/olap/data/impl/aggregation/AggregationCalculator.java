
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

import org.eclipse.birt.data.engine.aggregation.AggregationUtil;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * The AggregationCalculator class calculates values for its associated
 * Aggregation.
 */

public class AggregationCalculator
{
	AggregationDefinition aggregation;
	private Accumulator[] accumulators;
	private int levelCount;
	private int[] measureIndexes;
	private MeasureInfo[] measureInfos;
	private IDiskArray result = null;
	private IAggregationResultRow currentResultObj = null;
	private int[] parameterColIndex;
	private FacttableRow facttableRow;
	
	private static Logger logger = Logger.getLogger( AggregationCalculator.class.getName( ) );

	/**
	 * 
	 * @param aggregationDef
	 * @param facttableRowIterator
	 * @throws DataException 
	 */
	AggregationCalculator( AggregationDefinition aggregationDef, 
			DimColumn[] parameterColNames, //the parameter sequence corresponding with <code>Row4Aggregation.getParameterValues()</code>  
			IDataSet4Aggregation.MetaInfo metaInfo,
			ICubeDimensionReader cubeDimensionReader ) throws IOException, DataException
	{
		Object[] params = {
				aggregationDef, parameterColNames, metaInfo
		};
		logger.entering( AggregationCalculator.class.getName( ),
				"AggregationCalculator",
				params );
		this.aggregation = aggregationDef;
		AggregationFunctionDefinition[] aggregationFunction = aggregationDef.getAggregationFunctions( );
		if(aggregationDef.getLevels( )==null)
			this.levelCount = 0;
		else
			this.levelCount = aggregationDef.getLevels( ).length;
		if ( aggregationFunction != null )
		{
			this.accumulators = new Accumulator[aggregationFunction.length];
			this.measureIndexes = new int[aggregationFunction.length];
			this.parameterColIndex = new int[aggregationFunction.length];
				
			for ( int i = 0; i < aggregationFunction.length; i++ )
			{
				IAggrFunction aggregation = AggregationManager.getInstance( )
						.getAggregation( aggregationFunction[i].getFunctionName( ) );
				if (aggregation == null)
				{
					throw new DataException(
							DataResourceHandle.getInstance( ).getMessage( ResourceConstants.UNSUPPORTED_FUNCTION ) 
							+ aggregationFunction[i].getFunctionName( ));
				}
				if ( AggregationUtil.needDataField( aggregation ) )
				{
					this.parameterColIndex[i] = find( parameterColNames,
							aggregationFunction[i].getParaCol( ) );
				}
				else
				{
					this.parameterColIndex[i] = -1;
				}
				this.accumulators[i] = aggregation.newAccumulator( );
				this.accumulators[i].start( );
				final String measureName = aggregationFunction[i].getMeasureName( );
				this.measureIndexes[i] = metaInfo.getMeasureIndex( measureName );
	
				if ( this.measureIndexes[i] == -1 && measureName != null )
				{
					throw new DataException( ResourceConstants.MEASURE_NAME_NOT_FOUND,
							measureName );
				}
			}
		}
		result = new BufferedStructureArray( AggregationResultRow.getCreator( ), Constants.LIST_BUFFER_SIZE );
		measureInfos = metaInfo.getMeasureInfos( );
		facttableRow = new FacttableRow( measureInfos, cubeDimensionReader, metaInfo );
		logger.exiting( AggregationCalculator.class.getName( ),
				"AggregationCalculator" );
	}
	
	/**
	 * 
	 * @param colArray
	 * @param col
	 * @return
	 */
	private static int find( DimColumn[] colArray, DimColumn col )
	{
		if( colArray == null || col == null )
		{
			return -1;
		}
		for ( int i = 0; i < colArray.length; i++ )
		{
			if ( col.equals( colArray[i] ) )
			{
				return i;
			}
		}
		return -1;
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
						if ( !getFilterResult( row, i ) )
						{
							continue;
						}
						accumulators[i].onRow( getAccumulatorParameter( row, i ) );
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
	 * @param row
	 * @param functionNo
	 * @return
	 * @throws DataException 
	 */
	private boolean getFilterResult( Row4Aggregation row, int functionNo )
			throws DataException
	{
		facttableRow.setDimPos( row.getDimPos( ) );
		facttableRow.setMeasure( row.getMeasures( ) );
		IJSFacttableFilterEvalHelper filterEvalHelper = ( aggregation.getAggregationFunctions( )[functionNo] ).getFilterEvalHelper( );
		if ( filterEvalHelper == null )
		{
			return true;
		}
		else
		{
			return filterEvalHelper.evaluateFilter( facttableRow );
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
		/*else
			result.add( new AggregationResultRow( ) );*/
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
				if ( !getFilterResult( row, i ) )
				{
					continue;
				}
				accumulators[i].onRow( getAccumulatorParameter( row, i ) );
			}
		}
	}
	
	private Object[] getAccumulatorParameter( Row4Aggregation row, int funcIndex )
	{
		Object[] parameters = null;
		if( parameterColIndex[funcIndex] == -1 )
		{
			parameters = new Object[1];
			if( measureIndexes[funcIndex] < 0 )
			{
				return null;
			}
			else
			{
				parameters[0] = row.getMeasures()[measureIndexes[funcIndex]];
			}
		}
		else
		{
			parameters = new Object[2];
			if( measureIndexes[funcIndex] < 0 )
			{
				parameters[0] = null;
			}
			else
			{
				parameters[0] = row.getMeasures()[measureIndexes[funcIndex]];
			}
			parameters[1] = row.getParameterValues( )[parameterColIndex[funcIndex]];
		}
		return parameters;
	}
	
	/**
	 * 
	 * @param key1
	 * @param key2
	 * @return
	 */
	private int compare( Object[] key1, Object[] key2 )
	{
		for ( int i = 0; i < aggregation.getLevels( ).length; i++ )
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
