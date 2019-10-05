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

package org.eclipse.birt.core.data;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;

/**
 * This class help to manipulate expressions.
 * 
 */
public final class ExpressionUtil
{

	/** prefix for row */
	public static final String ROW_INDICATOR = "row";

	/** prefix for dataset row */
	public static final String DATASET_ROW_INDICATOR = "dataSetRow";
	
	/** prefix for parameter */
	public static final String PARAMETER_INDICATOR = "params";//$NON-NLS-1$
	
	/** prefix for measure */
	public static final String MEASURE_INDICATOR = "measure";
	
	/** prefix for dimension */
	public static final String DIMENSION_INDICATOR = "dimension";
	
	/** prefix for column bindings in Cube */
	public static final String DATA_INDICATOR = "data";
	
	private static String PREFIX = "COLUMN_";
	
	public static String EXPRESSION_VALUE_SUFFIX = ".value";
	
	private static int suffix = 0;

	/** prefix for variable */
	public static final String VARIABLE_INDICATOR = "vars";

	/**
	 * Return a row expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createRowExpression( String rowName )
	{
		return createJSRowExpression( rowName );
	}

	/**
	 * Return a dataSetRow expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createDataSetRowExpression( String rowName )
	{
		return createJSDataSetRowExpression( rowName );
	}
	
	/**
	 * Return a JS row expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createJSRowExpression( String rowName )
	{
		return ROW_INDICATOR + createJSExprComponent( rowName );
	}

	/**
	 * Return a JS measure expression text according to given measure name.
	 * 
	 * @param measureName
	 * @return
	 */
	public static String createJSMeasureExpression( String measureName )
	{
		return MEASURE_INDICATOR + createJSExprComponent( measureName );
	}

	/**
	 * Return a JS data expression text according to given data name.
	 * @param dataName
	 * @return
	 */
	public static String createJSDataExpression( String dataName )
	{
		return DATA_INDICATOR + createJSExprComponent( dataName );
	}
	
	/**
	 * Return a JS dimension expression text according to given dimension and
	 * measure name.By default it is reference to "ID" attribute of that level.
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @return
	 */
	public static String createJSDimensionExpression( String dimensionName,
			String levelName )
	{
		return DIMENSION_INDICATOR
				+ createJSExprComponent( dimensionName )
				+ createJSExprComponent( levelName );
	}
	
	/**
	 * Return a JS dimension expression text according to given dimension and
	 * measure name.
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public static String createJSDimensionExpression( String dimensionName, String levelName, String attributeName )
	{
		return DIMENSION_INDICATOR
			+ createJSExprComponent( dimensionName )
			+ createJSExprComponent( levelName )
			+ createJSExprComponent( attributeName );
	}
	/**
	 * Return a JS dataSetRow expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createJSDataSetRowExpression( String rowName )
	{
		return DATASET_ROW_INDICATOR + createJSExprComponent( rowName );
	}
	
	/**
	 * Return a JS parameter expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createJSParameterExpression( String parameterName )
	{
		return PARAMETER_INDICATOR + createJSExprComponent( parameterName );
	}

	/**
	 * Return a JavaScript parameter value according to an expression.
	 * 
	 * @param expression
	 * @return
	 */
	public static String createJSParameterValueExpression( String parameterName )
	{
		return createJSParameterExpression( parameterName )
				+ EXPRESSION_VALUE_SUFFIX;
	}

	private static String createJSExprComponent( String value )
	{
		return "[\""
		+ ( value == null
				? ""
				: JavascriptEvalUtil.transformToJsConstants( value.trim( ) ) )
		+ "\"]";
	}
	
	/**
	 * Return a row expression text according to given row index, which is
	 * 1-based.
	 * 
	 * @param index
	 * @return
	 * @deprecated
	 */
	public static String createRowExpression( int index )
	{
		return ROW_INDICATOR + "[" + index + "]";
	}

	/**
	 * Extract all column expression info
	 * 
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static List<IColumnBinding> extractColumnExpressions( String oldExpression )
			throws BirtException
	{
		return extractColumnExpressions( oldExpression, ExpressionUtil.ROW_INDICATOR );
	}
	
	/**
	 * Get the simplest column binding name. Such as row["col1"] pattern, we
	 * will return the column name "col1"
	 * 
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static String getColumnBindingName( String oldExpression )
			throws BirtException
	{
		List<IColumnBinding> columnsLists = extractColumnExpressions( oldExpression, ExpressionUtil.ROW_INDICATOR );
		if ( columnsLists.size( ) != 1
				|| !ExpressionParserUtility.isDirectColumnRef( oldExpression,
						ExpressionUtil.ROW_INDICATOR ) )
			return null;
		return columnsLists.get( 0 ).getResultSetColumnName( );
	}

	/**
	 * Get the simplest column binding name. Such as dataSetRow["col1"] pattern, we
	 * will return the column name "col1"
	 * 
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static String getColumnName( String oldExpression )
			throws BirtException
	{
		List<IColumnBinding> columnsLists = extractColumnExpressions( oldExpression, ExpressionUtil.DATASET_ROW_INDICATOR );
		if ( columnsLists.size( ) != 1
				|| !ExpressionParserUtility.isDirectColumnRef( oldExpression,
						ExpressionUtil.DATASET_ROW_INDICATOR ) )
			return null;
		return columnsLists.get( 0 ).getResultSetColumnName( );
	}
	
	/**
	 * Extract all column expression info
	 * 
	 * @param oldExpression
	 * @param mode
	 *            if true, it means to compile the "row" expression.else extract
	 *            "dataSetRow" expression
	 * @return
	 * @throws BirtException
	 * @deprecated use <code>extractColumnExpressions( String, String )</code> instead
	 */
	@Deprecated
	public static List<IColumnBinding> extractColumnExpressions( String oldExpression,
			boolean mode ) throws BirtException
	{
		String indicator = ( mode ? ExpressionUtil.ROW_INDICATOR : ExpressionUtil.DATASET_ROW_INDICATOR );
		return extractColumnExpressions( oldExpression, indicator);
	}
	
	/**
	 * Extract all column expression info
	 * 
	 * @param oldExpression
	 * @param mode
	 *            if true, it means to compile the "row" expression.else extract
	 *            "dataSetRow" expression
	 * @return
	 * @throws BirtException
	 */
	public static List<IColumnBinding> extractColumnExpressions( String oldExpression,
			String indicator ) throws BirtException
	{
		if ( oldExpression == null || oldExpression.trim( ).length( ) == 0 )
			return Collections.emptyList( );

		try
		{
			return ExpressionParserUtility.compileColumnExpression( new ExpressionParserUtility( ),
					oldExpression,
					indicator );			
		}
		catch( Exception e )
		{
			return Collections.emptyList( );
		}
	}

	/**
     * whethter the expression has aggregation 
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static boolean hasAggregation( String expression )
	{
		if ( expression == null )
			return false;

		try
		{
			return ExpressionParserUtility.hasAggregation( expression,
					ExpressionUtil.ROW_INDICATOR )
					|| ExpressionParserUtility.hasAggregation( expression,
							ExpressionUtil.DATASET_ROW_INDICATOR );
		}
		catch ( BirtException e )
		{
			return false;
		}
	}
	
	/**
	 * Return an IColumnBinding instance according to given oldExpression.
	 * 
	 * @param oldExpression
	 * @return
	 */
	public static IColumnBinding getColumnBinding( String oldExpression )
	{
		suffix++;
		return new ColumnBinding( PREFIX + suffix,
				ExpressionUtil.toNewExpression( oldExpression ) );
	}

	/**
	 * Translate the old expression with "row" as indicator to new expression
	 * using "dataSetRow" as indicator.
	 * 
	 * @param oldExpression
	 * @return
	 */
	public static String toNewExpression( String oldExpression )
	{
		if ( oldExpression == null )
			return null;

		char[] chars = oldExpression.toCharArray( );

		// 5 is the minium length of expression that can cantain a row
		// expression
		if ( chars.length < 5 )
			return oldExpression;
		else
		{
			ParseIndicator status = new ParseIndicator( 0,
					0,
					false,
					false,
					true,
					true );

			for ( int i = 0; i < chars.length; i++ )
			{
				status = getParseIndicator( chars,
						i,
						status.omitNextQuote( ),
						status.getCandidateKey1( ),
						status.getCandidateKey2( ) );

				i = status.getNewIndex( );
				if ( i >= status.getRetrieveSize( ) + 3 )
				{
					if ( status.isCandidateKey( )
							&& chars[i - status.getRetrieveSize( ) - 3] == 'r'
							&& chars[i - status.getRetrieveSize( ) - 2] == 'o'
							&& chars[i - status.getRetrieveSize( ) - 1] == 'w' )
					{
						if ( i - status.getRetrieveSize( ) - 4 <= 0
								|| isValidProceeding( chars[i
										- status.getRetrieveSize( ) - 4] ) )
						{
							if ( chars[i] == ' '
									|| chars[i] == '.' || chars[i] == '[' )
							{
								String firstPart = oldExpression.substring( 0,
										i - status.getRetrieveSize( ) - 3 );
								String secondPart = toNewExpression( oldExpression.substring( i
										- status.getRetrieveSize( ) ) );
								String newExpression = firstPart
										+ "dataSetRow" + secondPart;
								return newExpression;
							}
						}
					}
				}
			}

		}

		return oldExpression;
	}

	/**
	 * Translate the old expression with "rows" as parent query indicator to new expression
	 * using "row._outer" as parent query indicator.
	 * 
	 * @param oldExpression
	 * @param isParameterBinding
	 * @return
	 */
	public static String updateParentQueryReferenceExpression( String oldExpression, boolean isParameterBinding )
	{
		if ( oldExpression == null )
			return null;

		char[] chars = oldExpression.toCharArray( );

		// 7 is the minium length of expression that can cantain a row
		// expression
		if ( chars.length < 7 )
			return oldExpression;
		else
		{
			ParseIndicator status = new ParseIndicator( 0,
					0,
					false,
					false,
					true,
					true );

			for ( int i = 0; i < chars.length; i++ )
			{
				status = getParseIndicator( chars,
						i,
						status.omitNextQuote( ),
						status.getCandidateKey1( ),
						status.getCandidateKey2( ) );

				i = status.getNewIndex( );
				if ( i >= status.getRetrieveSize( ) + 4 )
				{
					if ( status.isCandidateKey( )
							&& chars[i - status.getRetrieveSize( ) - 4] == 'r'
							&& chars[i - status.getRetrieveSize( ) - 3] == 'o'
							&& chars[i - status.getRetrieveSize( ) - 2] == 'w'
							&& chars[i - status.getRetrieveSize( ) - 1] == 's')
					{
						if ( i - status.getRetrieveSize( ) - 5 <= 0
								|| isValidProceeding( chars[i
										- status.getRetrieveSize( ) - 5] ) )
						{
							if ( chars[i] == ' '
									|| chars[i] == '.' || chars[i] == '[' )
							{
								int start = i;
								int end = 1;
								//end is the offset of "[n]" in "rows[n]".
								do
								{
									i++;
									end++;
								}while( i < chars.length && chars[i]!=']');
								
								String firstPart = oldExpression.substring( 0,
										start - status.getRetrieveSize( ) - 4 );
								String secondPart = updateParentQueryReferenceExpression( oldExpression.substring( start
										- status.getRetrieveSize( ) + end), isParameterBinding );
								String newExpression = firstPart
										+ (isParameterBinding?"row":"row._outer") + secondPart;
								return newExpression;
							}
						}
					}
				}
			}
	
		}
	
		return oldExpression;
	}
	
	/**
	 * whether the exression is report paramter reference.The pattern should
	 * like params["aa"].if yes, return true. else return false;
	 * 
	 * @param expression
	 */
	public static boolean isScalarParamReference( String expression )
	{
		final String PARAM_PATTERN = "params\\[\".+\\\"]";
		Pattern pattern = Pattern.compile( PARAM_PATTERN );
		Matcher matcher = pattern.matcher( expression );
		return matcher.matches( );
	}
	
	/**
	 * This method is used to provide information necessary for next step parsing.
	 * 
	 * @param chars
	 * @param i
	 * @param omitNextQuote
	 * @param candidateKey1
	 * @param candidateKey2
	 * @return
	 */
	private static ParseIndicator getParseIndicator( char[] chars, int i,
			boolean omitNextQuote, boolean candidateKey1, boolean candidateKey2 )
	{
		int retrieveSize = 0;

		if ( chars[i] == '/' )
		{
			if ( i > 0 && chars[i - 1] == '/' )
			{
				retrieveSize++;
				while ( i < chars.length - 2 )
				{
					i++;
					retrieveSize++;
					if ( chars[i] == '\n' )
					{
						break;
					}
				}
				retrieveSize++;
				i++;
			}
		}
		else if ( chars[i] == '*' )
		{
			if ( i > 0 && chars[i - 1] == '/' )
			{
				i++;
				retrieveSize = retrieveSize + 2;
				while ( i < chars.length - 2 )
				{
					i++;
					retrieveSize++;
					if ( chars[i - 1] == '*' && chars[i] == '/' )
					{
						break;
					}
				}
				retrieveSize++;
				i++;
			}
		}

		if ( ( !omitNextQuote ) && chars[i] == '"' )
		{
			candidateKey1 = !candidateKey1;
			if ( candidateKey1 )
				candidateKey2 = true;
		}
		if ( ( !omitNextQuote ) && chars[i] == '\'' )
		{
			candidateKey2 = !candidateKey2;
			if ( candidateKey2 )
				candidateKey1 = true;
		}
		if ( chars[i] == '\\' )
			omitNextQuote = true;
		else
			omitNextQuote = false;

		return new ParseIndicator( retrieveSize,
				i,
				candidateKey1,
				omitNextQuote,
				candidateKey1,
				candidateKey2 );
	}

	/**
	 * Test whether the char immediately before the candidate "row" key is
	 * valid.
	 * 
	 * @param operator
	 * @return
	 */
	private static boolean isValidProceeding( char operator )
	{
		if ( ( operator >= 'A' && operator <= 'Z' )
				|| ( operator >= 'a' && operator <= 'z' ) || operator == '_' )
			return false;

		return true;
	}
	
	/**
	 * 
	 * @param jointColumName
	 * @return
	 */
	public static String[] getSourceDataSetNames( String jointColumName )
	{
		assert jointColumName != null;

		String[] result = new String[2];
		if ( jointColumName.indexOf( "::" ) != -1 )
		{
			String[] splited = jointColumName.split( "::" );

			result[0] = splited[0];
			if ( result[0].endsWith( "1" ) || result[0].endsWith( "2" ) )
				result[1] = result[0].substring( 0, result[0].length( ) - 1 );
		}

		return result;
	}
	
	/**
	 * Gets the data set name with the given full name. The full name may 
	 * contain the library namespace.
	 * 
	 * <p>
	 * For example,
	 * <ul>
	 * <li>"dataSet1" is extracted from "new_library.dataSet1"
	 * </ul>
	 * 
	 * @param fullDataSetName
	 *            the data set
	 * @return the name
	 */
	
	public static String getDataSetNameWithoutPrefix(String fullDataSetName)
	{
		if (fullDataSetName == null)
			return null;
							
		String dataSetName = fullDataSetName;			
		String temp[] = fullDataSetName.split( "\\Q.\\E" );  //$NON-NLS-1$
		if( temp.length >= 2 )
		{
			dataSetName = temp[1].trim( );
		}

		return dataSetName;
	}
	
	/**
	 * 
	 * @param expr
	 * @return
	 * @throws CoreException
	 */
	public static Set<IDimLevel> getReferencedDimLevel( String expr ) throws CoreException
	{
		return OlapExpressionCompiler.getReferencedDimLevel( expr );
	}
	
	/**
	 * 
	 * @param expr
	 * 
	 * @deprecated replaced by getAllReferencedMeasures
	 * 
	 * @return get the first measure name in this expression
	 * @throws CoreException
	 */
	public static String getReferencedMeasure( String expr )
			throws CoreException
	{
		Set<String> names = OlapExpressionCompiler.getReferencedMeasure( expr );
		return names.isEmpty( )? null : names.iterator( ).next( );
	}
	
	/**
	 * 
	 * @param expr
	 * @return get the referenced measure name in this expression
	 * @throws CoreException
	 */
	public static Set<String> getAllReferencedMeasures( String expr ) throws CoreException
	{
		return OlapExpressionCompiler.getReferencedMeasure( expr );
	}
	
	/**
	 * 
	 * @param expr
	 * @param paramOldName
	 * @param paramNewName
	 * @return
	 */
	public static String replaceParameterName( String expr,
			String paramOldName, String paramNewName )
	{
		if ( expr == null || paramOldName == null || paramNewName == null )
			return expr;

		expr = expr.replaceAll( "\\Qparams[\"\\E" + paramOldName + "\\Q\"]\\E",
				"params[\"" + paramNewName + "\"]" );
		expr = expr.replaceAll( "\\Qparams.\\E" + paramOldName, "params."
				+ paramNewName );
		return expr;
	}
	
	/**
	 * Generate a Javascript constant expression by user input string and target BIRT data type.
	 * @param input user input string.
	 * @param dataType target BIRT data type. available values defined in {@code org.eclipse.birt.core.data.DataType}
	 * @return generated Javascript constant expression text
	 * @throws BIRTException
	 */
	public static String generateConstantExpr( String input, int dataType ) throws BirtException
	{
		if ( input == null )
		{
			return null;
		}
		if ( dataType == DataType.DECIMAL_TYPE )
		{
			return "new java.math.BigDecimal(\"" + input + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		if ( dataType == DataType.STRING_TYPE
				|| dataType == DataType.DATE_TYPE
				|| dataType == DataType.SQL_DATE_TYPE
				|| dataType == DataType.SQL_TIME_TYPE
				|| dataType == DataType.JAVA_OBJECT_TYPE )
		{
			return JavascriptEvalUtil.transformToJsExpression( input );
		}
		return input;
	}
}

/**
 * A utility class for internal use only.
 * 
 */
class ParseIndicator
{

	private int retrieveSize;
	private int newIndex;
	private boolean isCandidateKey;
	private boolean omitNextQuote;
	private boolean candidateKey1;
	private boolean candidateKey2;

	ParseIndicator( int retrieveSize, int newIndex, boolean isCandidateKey,
			boolean omitNextQuote, boolean candidateKey1, boolean candidateKey2 )
	{
		this.retrieveSize = retrieveSize;
		this.newIndex = newIndex;
		this.isCandidateKey = isCandidateKey;
		this.omitNextQuote = omitNextQuote;
		this.candidateKey1 = candidateKey1;
		this.candidateKey2 = candidateKey2;
	}

	public int getRetrieveSize( )
	{
		return this.retrieveSize;
	}

	public int getNewIndex( )
	{
		return this.newIndex;
	}

	public boolean isCandidateKey( )
	{
		return this.isCandidateKey;
	}

	public boolean omitNextQuote( )
	{
		return this.omitNextQuote;
	}

	public boolean getCandidateKey1( )
	{
		return this.candidateKey1;
	}

	public boolean getCandidateKey2( )
	{
		return this.candidateKey2;
	}
}

class ColumnBinding implements IColumnBinding
{

	private String columnName;
	private String expression;
	private int level;

	ColumnBinding( String columnName, String expression )
	{
		this.columnName = columnName;
		this.expression = expression;
		this.level = 0;
	}
	
	ColumnBinding( String columnName, String expression, int level )
	{
		this.columnName = columnName;
		this.expression = expression;
		this.level = level;
	}
	
	public String getResultSetColumnName( )
	{
		return this.columnName;
	}

	public String getBoundExpression( )
	{
		return this.expression;
	}

	public int getOuterLevel( )
	{
		return level;
	}

}