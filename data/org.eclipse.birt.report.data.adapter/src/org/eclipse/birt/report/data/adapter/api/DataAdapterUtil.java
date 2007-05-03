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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.aggregation.IBuildInAggregation;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This class implement some utility methods that can be used by the consumer of Data Engine.
 */

public class DataAdapterUtil
{
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
	public static String adaptModelAggregationType( String modelAggrType ) throws AdapterException
	{
		if( DesignChoiceConstants.MEASURE_FUNCTION_AVERAGE.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_AVE_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_COUNT.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_COUNT_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_COUNTDISTINCT.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_FIRST.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_FIRST_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_IRR.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_IRR_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_LAST.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_LAST_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_MAX.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_MAX_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_MEDIAN.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_MEDIAN_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_MIN.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_MIN_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_MIRR.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_MIRR_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_MODE.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_MODE_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_MOVINGAVE.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_MOVINGAVE_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_NPV.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_NPV_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_RUNNINGCOUNT.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_RUNNINGNPV.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_RUNNINGNPV_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_RUNNINGSUM.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_STDDEV.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_STDDEV_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_SUM.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_SUM_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_VARIANCE.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_VARIANCE_FUNC;
		if( DesignChoiceConstants.MEASURE_FUNCTION_WEIGHTEDAVG.equals( modelAggrType ) )
			return IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC;
		
		throw new AdapterException( ResourceConstants.INVALID_AGGREGATION_NAME, modelAggrType );
	}
	private static class JSResultIteratorObject extends ScriptableObject
	{
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
