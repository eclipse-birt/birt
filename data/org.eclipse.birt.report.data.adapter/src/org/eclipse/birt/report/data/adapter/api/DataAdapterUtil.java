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

import java.io.File;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.timefunction.TimePeriodType;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.icu.util.ULocale;

/**
 * This class implement some utility methods that can be used by the consumer of Data Engine.
 */

public class DataAdapterUtil
{
	private static Map<String, String> aggrAdapterMap = new HashMap<String, String>( );
	private static Map<String, Integer> filterOptMap = new HashMap<String, Integer>( );

	static
	{
		registerAggregationFunction( );
		registerFilterOperator( );
	}

	/**
	 * register model aggregation function names with build-in function names	
	 */
	private static void registerAggregationFunction( )
	{
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
	 * register model filter operator with dte's IConditionalExpression operator
	 */
	private static void registerFilterOperator( )
	{
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_EQ,
				new Integer( IConditionalExpression.OP_EQ ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NE,
				new Integer( IConditionalExpression.OP_NE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_LT,
				new Integer( IConditionalExpression.OP_LT ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_LE,
				new Integer( IConditionalExpression.OP_LE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_GE,
				new Integer( IConditionalExpression.OP_GE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_GT,
				new Integer( IConditionalExpression.OP_GT ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN,
				new Integer( IConditionalExpression.OP_BETWEEN ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN,
				new Integer( IConditionalExpression.OP_NOT_BETWEEN ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NULL,
				new Integer( IConditionalExpression.OP_NULL ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL,
				new Integer( IConditionalExpression.OP_NOT_NULL ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_TRUE,
				new Integer( IConditionalExpression.OP_TRUE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_FALSE,
				new Integer( IConditionalExpression.OP_FALSE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_LIKE,
				new Integer( IConditionalExpression.OP_LIKE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_TOP_N,
				new Integer( IConditionalExpression.OP_TOP_N ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N,
				new Integer( IConditionalExpression.OP_BOTTOM_N ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT,
				new Integer( IConditionalExpression.OP_TOP_PERCENT ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT,
				new Integer( IConditionalExpression.OP_BOTTOM_PERCENT ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_MATCH,
				new Integer( IConditionalExpression.OP_MATCH ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE,
				new Integer( IConditionalExpression.OP_NOT_LIKE ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH,
				new Integer( IConditionalExpression.OP_NOT_MATCH ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_IN,
				new Integer( IConditionalExpression.OP_IN ) );
		filterOptMap.put( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN,
				Integer.valueOf( IConditionalExpression.OP_NOT_IN ) );
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
	 * @throws AdapterException 
	 */
	public static void registerDataObject( ScriptContext context,
			ILinkedResult source ) throws AdapterException
	{
		try
		{
			Scriptable targetScope = ( (IDataScriptEngine) context.getScriptEngine( IDataScriptEngine.ENGINE_NAME ) ).getJSScope( context );
			int type = ( (ILinkedResult) source ).getCurrentResultType( );
			if ( type == ILinkedResult.TYPE_TABLE )
			{
				targetScope.put( "row",
						targetScope,
						new JSResultIteratorObject( (ILinkedResult) source,
								targetScope ) );
				targetScope.put( "data",
						targetScope,
						new JSResultIteratorObject( (ILinkedResult) source,
								targetScope ) );
			}
			else if ( type == ILinkedResult.TYPE_CUBE && source.getCurrentResult( )!= null )
			{
				Scriptable scope = ( (ICubeCursor) source.getCurrentResult( ) ).getScope( );
				targetScope.put( "data", targetScope, scope.get( "data", scope ) );
				if( scope.get( "data", scope ) != null && scope.get( "data", scope ) instanceof Scriptable )
					targetScope.put( "row", targetScope, scope.get( "row", scope ) );
				targetScope.put( "dimension",
						targetScope,
						scope.get( "dimension", scope ) );
				targetScope.put( "measure", targetScope, scope.get( "measure",
						scope ) );
			}
		}
		catch ( BirtException e )
		{
			throw new AdapterException( e.getErrorCode( ), e );
		}
	}
	
	/**
	 * 
	 * @param context
	 * @throws AdapterException
	 */
	public static void unRegisterDataObject( ScriptContext context ) throws AdapterException
	{
		Scriptable targetScope;
		try
		{
			if( context!= null )
			{
				targetScope = ( (IDataScriptEngine) context.getScriptEngine( IDataScriptEngine.ENGINE_NAME ) ).getJSScope( context );
				targetScope.delete( "row" );
				targetScope.delete( "dimension" );
				targetScope.delete( "measure" );
				targetScope.delete( "data" );				
			}
		}
		catch ( BirtException e )
		{
			throw new AdapterException( e.getErrorCode( ), e );
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
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB ) )
			return DataType.BLOB_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT ) )
			return DataType.JAVA_OBJECT_TYPE;
		return DataType.UNKNOWN_TYPE;
	}
	
	/**
	 * @param type: a Data Engine data type
	 * @return Data Engine data types compatible with <code>type</code>, including <code>type</code> itself
	 * @throws exception if <code>DataType.ANY_TYPE</code>, <code>DataType.UNKNOWN_TYPE</code> or other unsupported int value is passed into
	 */
	public static int[] getCompatibleDataTypes( int type ) throws AdapterException
	{
		switch ( type )
		{
			case DataType.BOOLEAN_TYPE:
				return new int[]{ DataType.BOOLEAN_TYPE };
			case DataType.INTEGER_TYPE:
				return new int[]{ DataType.INTEGER_TYPE };
			case DataType.DOUBLE_TYPE:
				return new int[]{ DataType.DOUBLE_TYPE, DataType.INTEGER_TYPE };
			case DataType.DECIMAL_TYPE:
				return new int[]{ DataType.DECIMAL_TYPE, DataType.DOUBLE_TYPE, DataType.INTEGER_TYPE };
			case DataType.STRING_TYPE:
				return new int[]{ DataType.STRING_TYPE, DataType.BOOLEAN_TYPE,
						DataType.DECIMAL_TYPE, DataType.DOUBLE_TYPE, DataType.INTEGER_TYPE, 
						DataType.DATE_TYPE, DataType.BLOB_TYPE, DataType.BINARY_TYPE, 
						DataType.SQL_DATE_TYPE, DataType.SQL_TIME_TYPE, DataType.JAVA_OBJECT_TYPE };
			case DataType.DATE_TYPE:
				return new int[]{ DataType.DATE_TYPE, DataType.SQL_DATE_TYPE, DataType.SQL_TIME_TYPE };
			case DataType.BLOB_TYPE:
				return new int[]{ DataType.BLOB_TYPE };
			case DataType.BINARY_TYPE:
				return new int[]{ DataType.BINARY_TYPE };
			case DataType.SQL_DATE_TYPE:
				return new int[]{ DataType.SQL_DATE_TYPE, DataType.DATE_TYPE };
			case DataType.SQL_TIME_TYPE:
				return new int[]{ DataType.SQL_TIME_TYPE, DataType.DATE_TYPE };
			case DataType.JAVA_OBJECT_TYPE:
				return new int[]{ DataType.JAVA_OBJECT_TYPE, DataType.STRING_TYPE, DataType.BOOLEAN_TYPE,
						DataType.DECIMAL_TYPE, DataType.DOUBLE_TYPE, DataType.INTEGER_TYPE, 
						DataType.DATE_TYPE, DataType.BLOB_TYPE,
						DataType.SQL_DATE_TYPE, DataType.SQL_TIME_TYPE };
			default:
				throw new AdapterException( ResourceConstants.INVALID_DATA_TYPE, type );
		}
	}
	
    /**
     * Adapter dte's function name to model function name
     * @param apiDataType
     * @return
     */
    public static String adapterToModelDataType( int apiDataType )
    {
        if ( apiDataType == DataType.INTEGER_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
        else if ( apiDataType == DataType.STRING_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
        else if ( apiDataType == DataType.DATE_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME;
        else if ( apiDataType == DataType.DECIMAL_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL;
        else if ( apiDataType == DataType.DOUBLE_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT;
        else if ( apiDataType == DataType.SQL_DATE_TYPE)
            return DesignChoiceConstants.COLUMN_DATA_TYPE_DATE;
        else if ( apiDataType == DataType.SQL_TIME_TYPE)
            return DesignChoiceConstants.COLUMN_DATA_TYPE_TIME;
        else if ( apiDataType == DataType.BOOLEAN_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN;
        else if ( apiDataType == DataType.BLOB_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB;
        else if ( apiDataType == DataType.JAVA_OBJECT_TYPE )
            return DesignChoiceConstants.COLUMN_DATA_TYPE_JAVA_OBJECT;

        return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY;
    }
	
    /**
     * Adapter dte's data type to model data type
     * 
     * @param funcName
     *            DTE function name
     * @return model function name
     */
    public static String toModelAggregationType( String funcName )
    {
        for ( Map.Entry<String, String> funcEntry : aggrAdapterMap.entrySet( ) )
        {
            if ( funcEntry.getValue( ).equals( funcName ) )
            {
                return funcEntry.getKey( );
            }
        }
        return funcName;
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
		Object o = aggrAdapterMap.get( modelAggrType );
		return o == null ? modelAggrType : o.toString( );
	}
	
	/**
	 * This method is used to adapter model filter's operator into dte's
	 * operator
	 * 
	 * @param modelOpr
	 * @return
	 */
	public static int adaptModelFilterOperator( String modelOpr )
	{
		Integer operator = (Integer) filterOptMap.get( modelOpr );
		if ( operator != null )
			return ( (Integer) operator ).intValue( );
		else
			return IConditionalExpression.OP_NONE;
	}
	
	/**
	 * Get the rollup aggregation name. If the function is TOTAL_AVE_FUNC,
	 * TOTAL_COUNT_FUNC, or TOTAL_COUNT_DISTINCT_FUNC, return TOTAL_SUM_FUNC as
	 * measure function name
	 * 
	 * @param functionName
	 * @return
	 */
	public static String getRollUpAggregationName( String functionName )
	{
		if ( functionName == null || functionName.trim( ).length( ) == 0 )
			return functionName;
		String func = functionName;
		try
		{
			func = adaptModelAggregationType( functionName );
		}
		catch ( AdapterException e )
		{
			// do nothing
		}
		if ( func.equals( IBuildInAggregation.TOTAL_AVE_FUNC ) ||
				func.equals( IBuildInAggregation.TOTAL_COUNT_FUNC ) ||
				func.equals( IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC ) )
			return IBuildInAggregation.TOTAL_SUM_FUNC;
		else
			return func;
	}
	
	/**
	 * This method is used to adapter model sort direction into dte's direction.
	 * 
	 * @param dir
	 * @return
	 */
	public static int adaptModelSortDirection( String modelSortDir )
	{
		return DesignChoiceConstants.SORT_DIRECTION_DESC.equals( modelSortDir )
				? ISortDefinition.SORT_DESC : ISortDefinition.SORT_ASC;
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static int modelDataTypeToCoreDataType( String type )
	{
		int typeNum = DataType.ANY_TYPE;
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
			typeNum = DataType.STRING_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
			typeNum = DataType.DOUBLE_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
			typeNum = DataType.DECIMAL_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
			typeNum = DataType.DATE_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
			typeNum = DataType.SQL_DATE_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
			typeNum = DataType.SQL_TIME_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
			typeNum = DataType.BOOLEAN_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
			typeNum = DataType.INTEGER_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_JAVA_OBJECT.equals( type ) )
			typeNum = DataType.JAVA_OBJECT_TYPE;
		return typeNum;
	}
	
	/**
	 * 
	 * @param dteDTName
	 * @return
	 */
	public static String coreDataTypeToModelDataType( String dteDTName )
	{
		if ( dteDTName == null )
			return null;

		String modelDataType = DesignChoiceConstants.PARAM_TYPE_ANY;

		if ( dteDTName.equals( DataType.INTEGER_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_INTEGER;
		else if ( dteDTName.equals( DataType.DOUBLE_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_FLOAT;
		else if ( dteDTName.equals( DataType.DECIMAL_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_DECIMAL;
		else if ( dteDTName.equals( DataType.STRING_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_STRING;
		else if ( dteDTName.equals( DataType.DATE_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_DATETIME;
		else if ( dteDTName.equals( DataType.BOOLEAN_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_BOOLEAN;
		else if ( dteDTName.equals( DataType.SQL_DATE_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_DATE;
		else if ( dteDTName.equals( DataType.SQL_TIME_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_TIME;
		else if ( dteDTName.equals( DataType.OBJECT_TYPE_NAME ) )
			modelDataType = DesignChoiceConstants.PARAM_TYPE_JAVA_OBJECT;
		return modelDataType;
	}
	
	/**
	 * get dte's TimePeriodType enum.
	 * @param type
	 * @return
	 */
	public static TimePeriodType toTimePeriodType( String type )
	{
		if ( type.equals( TimePeriodType.YEAR.toString( ) ) )
		{
			return TimePeriodType.YEAR;
		}
		else if ( type.equals( TimePeriodType.QUARTER.toString( ) ) )
		{
			return TimePeriodType.QUARTER;
		}
		else if ( type.equals( TimePeriodType.MONTH.toString( ) ) )
		{
			return TimePeriodType.MONTH;
		}
		else if ( type.equals( TimePeriodType.WEEK.toString( ) ) )
		{
			return TimePeriodType.WEEK;
		}
		else if ( type.equals( TimePeriodType.DAY.toString( ) ) )
		{
			return TimePeriodType.DAY;
		}
		else if ( type.equals( "Year to Date" ))
		{
			return TimePeriodType.YEAR;
		}
		else if ( type.equals( "Quarter to Date" ))
		{
			return TimePeriodType.QUARTER;
		}
		else if ( type.equals( "Month to Date" ))
		{
			return TimePeriodType.MONTH;
		}
		else if ( type.equals( "Week to Date" ))
		{
			return TimePeriodType.WEEK;
		}
		return null;
	}

	/**
	 * to model's time type
	 * @param type
	 * @return
	 */
	public static String toModelTimeType( TimePeriodType type )
	{
		if ( TimePeriodType.YEAR.equals( type ) )
		{
			return DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR;
		}
		else if ( TimePeriodType.QUARTER.equals( type ) )
		{
			return DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER;
		}
		else if ( TimePeriodType.MONTH.equals( type ) )
		{
			return DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH;
		}
		else if ( TimePeriodType.WEEK.equals( type ) )
		{
			return DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR;
		}
		else if ( TimePeriodType.DAY.equals( type ) )
		{
			return DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR;
		}
		return null;
	}

	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String adaptArgumentName( String name )
	{
		return "Data Field".equals( name )
				? org.eclipse.birt.data.aggregation.impl.Constants.EXPRESSION_NAME
				: name;
	}
	
	/**
	 * Get the parameter value from .rptconfig file if it does exist
	 * 
	 * @return Object[] the parameter value
	 */
	public static Object getParamValueFromConfigFile( ScalarParameterHandle paramHandle )
	{
		ModuleHandle designModule = paramHandle.getModuleHandle( );
		String designFileName = designModule.getFileName( );
		// replace the file extension
		// maybe the report is provided as a stream
		// then the config file cannot be found
		int index = designFileName.lastIndexOf( '.' );
		if ( index < 0 )
		{
			return null;
		}
		String reportConfigName = designFileName.substring( 0, index + 1 )
				+ "rptconfig";
		final File file = new File( reportConfigName );
		
		
		if ( AccessController.doPrivileged( new PrivilegedAction<Boolean>()
				{
			  public Boolean run()
			  {
			    return file.exists();
			  }
			}))
		{
			String paraName = paramHandle.getName( );
			ScalarParameterHandle parameterHandle = (ScalarParameterHandle) designModule.findParameter( paraName );
			paraName = paraName + "_" + parameterHandle.getID( );
			SessionHandle sessionHandle = new DesignEngine( null ).newSessionHandle( ULocale.US );
			ReportDesignHandle rdHandle = null;
			// Open report config file
			try
			{
				rdHandle = sessionHandle.openDesign( reportConfigName );
			}
			catch ( DesignFileException e )
			{
				return null;
			}
			// handle config vars
			if ( rdHandle != null )
			{
				List values = new ArrayList( );
				Iterator configVars = rdHandle.configVariablesIterator( );
				while ( configVars != null && configVars.hasNext( ) )
				{
					ConfigVariableHandle configVar = (ConfigVariableHandle) configVars.next( );
					if ( configVar != null )
					{
						String varName = prepareConfigVarName( configVar.getName( ) );
						Object varValue = configVar.getValue( );
						if ( varName == null || varValue == null )
						{
							continue;
						}
						if ( varName.equals( paraName ) )
						{
							String value = (String) varValue;
							// if the value actually is in String type, convert
							// it by adding quotation marks
							values.add( value );
							// return value;
						}
						if ( isNullValue( varName, (String) varValue, paraName ) )
						{
							if ( !parameterHandle.getParamType( )
									.equals( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE ) )
							{
								return null;
							}
							return new Object[0];
						}
					}
				}
				if( values.size( ) > 0 )
				{
					if ( parameterHandle.getParamType( )
							.equals( DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE ) )
					{
						try
						{
							return DataTypeUtil.convert( 
									values.get( 0 ), DataAdapterUtil.modelDataTypeToCoreDataType( parameterHandle.getDataType() ) );
						}
						catch ( BirtException e )
						{
							return null;
						}
					}
					
					try {
						Object[] reValues = new Object[values.size()];
						for (int i = 0; i < reValues.length; i++)
						{
							reValues[i] = DataTypeUtil.convert( 
									values.get(i), DataAdapterUtil.modelDataTypeToCoreDataType(parameterHandle.getDataType()));
						}
						return reValues;
					}
					catch (BirtException e)
					{
						return null;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Delete the last "_" part
	 * 
	 * @param name
	 * @return String
	 */
	private static String prepareConfigVarName( String name )
	{
		int index = name.lastIndexOf( "_" ); //$NON-NLS-1$
		return name.substring( 0, index );
	}

	/**
	 * Checks whether the parameter value is null in the rptconfig file
	 * 
	 * @param varName
	 * @param varValue
	 * @param newParaName
	 * @return
	 */
	private static boolean isNullValue( String varName, String varValue,
			String newParaName )
	{
		return varName.toLowerCase( ).startsWith( "__isnull" )
				&& varValue.equals( newParaName );
	}
	
	private static class JSResultIteratorObject extends ScriptableObject
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 684728008759347940L;
		private ILinkedResult it;
		private IResultIterator currentIterator;
		private Scriptable scope;
		
		JSResultIteratorObject( ILinkedResult it, Scriptable scope )
		{
			this.it = it;
			this.scope = scope;
			if ( it.getCurrentResultType( ) == ILinkedResult.TYPE_TABLE )
				this.currentIterator = (IResultIterator) it.getCurrentResult( );
		}
		
		public String getClassName( )
		{
			return "JSResultIteratorObject";
		}

		/*
		 * @see org.mozilla.javascript.Scriptable#get(java.lang.String, org.mozilla.javascript.Scriptable)
		 */
		public Object get( String arg0, Scriptable scope )
		{
			try
			{
				if ( this.currentIterator == null )
					return null;

				if ( "__rownum".equalsIgnoreCase( arg0 ) ||
						"0".equalsIgnoreCase( arg0 ) )
				{
					return Integer.valueOf( this.currentIterator.getRowIndex( ) );
				}
				if ( "_outer".equalsIgnoreCase( arg0 ) )
				{
					return new JSResultIteratorObject( it.getParent( ), this.scope );
				}
				return JavascriptEvalUtil.convertToJavascriptValue( this.currentIterator.getValue( arg0 ), this.scope );
			}
			catch ( BirtException e )
			{
				return null;
			}
		}

		/*
		 * @see org.mozilla.javascript.Scriptable#get(int, org.mozilla.javascript.Scriptable)
		 */
		public Object get( int index, Scriptable start )
		{
			return this.get( String.valueOf( index ), start );
		}
	}
}
