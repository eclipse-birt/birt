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

package org.eclipse.birt.report.data.adapter.api;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.aggregation.IBuildInAggregation;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This class implement some utility methods that can be used by the consumer of Data Engine.
 */

public class DataAdapterUtil
{
	private static Map aggrAdapterMap = new HashMap();
	static
	{// map model aggregation function names to build-in function names
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_SUM,
				IBuildInAggregation.TOTAL_SUM_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT,
				IBuildInAggregation.TOTAL_COUNT_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_MIN,
				IBuildInAggregation.TOTAL_MIN_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_MAX,
				IBuildInAggregation.TOTAL_MAX_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE,
				IBuildInAggregation.TOTAL_AVE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG,
				IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV,
				IBuildInAggregation.TOTAL_STDDEV_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST,
				IBuildInAggregation.TOTAL_FIRST_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_LAST,
				IBuildInAggregation.TOTAL_LAST_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_MODE,
				IBuildInAggregation.TOTAL_MODE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE,
				IBuildInAggregation.TOTAL_MOVINGAVE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN,
				IBuildInAggregation.TOTAL_MEDIAN_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE,
				IBuildInAggregation.TOTAL_VARIANCE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM,
				IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_IRR,
				IBuildInAggregation.TOTAL_IRR_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR,
				IBuildInAggregation.TOTAL_MIRR_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_NPV,
				IBuildInAggregation.TOTAL_NPV_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV,
				IBuildInAggregation.TOTAL_RUNNINGNPV_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT,
				IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT,
				IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N,
				IBuildInAggregation.TOTAL_TOP_N_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N,
				IBuildInAggregation.TOTAL_BOTTOM_N_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT,
				IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT,
				IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK,
				IBuildInAggregation.TOTAL_PERCENT_RANK_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE,
				IBuildInAggregation.TOTAL_PERCENTILE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE,
				IBuildInAggregation.TOTAL_QUARTILE_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM,
				IBuildInAggregation.TOTAL_PERCENTSUM_FUNC );
		aggrAdapterMap.put( DesignChoiceConstants.AGGREGATION_FUNCTION_RANK,
				IBuildInAggregation.TOTAL_RANK_FUNC );
	}
	/**
	 * This method is used to register the Java Script Objects which are defined in the scope of
	 * source ResultSet ( might be IResultSet or CubeCursor ) to target scope. One possible client
	 * of this method is Report Engine. A classic use case is that instead of register its own "row" object 
	 * the Report Engine can simply call this method with proper argument so that the "row" object
	 * registered in IResultIterator's scope, that is, JSResultSetRow, can be accessed by engine using
	 * engine scope. 
	 *   
	 * @param targetScope
	 * @param source
	 */
	public static void registerJSObject( Scriptable targetScope,
			ILinkedResult source )
	{
		int type = ( (ILinkedResult) source ).getCurrentResultType( );
		if ( type == ILinkedResult.TYPE_TABLE )
		{
			targetScope.put( "row", targetScope, new JSResultIteratorObject(
					(ILinkedResult) source ) );
		}
		else if ( type == ILinkedResult.TYPE_CUBE )
		{
			Scriptable scope = ( (ICubeCursor) source.getCurrentResult( ) )
					.getScope( );
			targetScope.put( "data", targetScope, scope.get( "data", scope ) );
			targetScope.put( "dimension", targetScope, scope.get( "dimension",
					scope ) );
			targetScope.put( "measure", targetScope, scope.get( "measure",
					scope ) );
		}
	}
	
	/**
	 * Adapts a Model data type (string) to Data Engine data type constant
	 * (integer) on column
	 */
	public static int adaptModelDataType( String modelDataType )
	{
		if ( modelDataType == null )
			return DataType.UNKNOWN_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY ) )
			return DataType.ANY_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER ) )
			return DataType.INTEGER_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING ) )
			return DataType.STRING_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME ) )
			return DataType.DATE_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL ) )
			return DataType.DECIMAL_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT ) )
			return DataType.DOUBLE_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME ) )
			return DataType.SQL_TIME_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE ) )
			return DataType.SQL_DATE_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN ) )
			return DataType.BOOLEAN_TYPE;
		return DataType.UNKNOWN_TYPE;
	}
	
	/**
	 * 
	 * @param modelAggrType
	 * @return
	 * @throws AdapterException
	 */
	public static String adaptModelAggregationType( String modelAggrType )
			throws AdapterException
	{
		return (String) aggrAdapterMap.get( modelAggrType );
	}
	
	
	private static class JSResultIteratorObject extends ScriptableObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 684728008759347940L;
		private ILinkedResult it;
		private IResultIterator currentIterator;
		
		JSResultIteratorObject( ILinkedResult it )
		{
			this.it = it;
			if ( it.getCurrentResultType( ) == ILinkedResult.TYPE_TABLE )
				this.currentIterator = ( IResultIterator ) it.getCurrentResult( );
		}
		public String getClassName( )
		{
			return "JSResultIteratorObject";
		}
		
		public Object get( String arg0, Scriptable scope )
		{
			try
			{
				if ( this.currentIterator == null )
					return null;
				
				if( "__rownum".equalsIgnoreCase( arg0 )||"0".equalsIgnoreCase( arg0 ))
				{
					return new Integer( this.currentIterator.getRowIndex( ) );
				}
				if( "_outer".equalsIgnoreCase( arg0 ))
				{
					return new JSResultIteratorObject( it.getParent( ));
				}
				return this.currentIterator.getValue( arg0 );
			}
			catch ( BirtException e )
			{
				return null;
			}
		}
		
	}
}
