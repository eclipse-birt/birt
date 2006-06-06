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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * This class help to manipulate expressions.
 * 
 */
public final class ExpressionUtil
{

	/** prefix for row */
	private static final String ROW_INDICATOR = "row";

	/** prefix for dataset row */
	private static final String DATASET_ROW_INDICATOR = "dataSetRow";

	private static String PREFIX = "COLUMN_";
	
	private static int suffix = 0;

	/**
	 * Return a row expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createRowExpression( String rowName )
	{
		return ROW_INDICATOR + "[\"" + rowName + "\"]";
	}

	/**
	 * Return a dataSetRow expression text according to given row name.
	 * 
	 * @param rowName
	 * @return
	 */
	public static String createDataSetRowExpression( String rowName )
	{
		return DATASET_ROW_INDICATOR + "[\"" + rowName + "\"]";
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
     * extract all column expression info excluding outer_level > 0
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static List extractColumnExpressions( String oldExpression )
			throws BirtException
	{
		if ( oldExpression == null )
			return new ArrayList( );

		List exprList = ExpressionParserUtility.compileColumnExpression( oldExpression );
		for ( int i = 0; i < exprList.size( ); )
		{
			IColumnBinding info = (IColumnBinding) exprList.get( i );
			if ( info.getOuterLevel( ) != 0 )
			{
				exprList.remove( i );
			}
			else
				i++;
		}
		return exprList;
	}
	
	/**
     * extract all column expression info, including outer_level >0 
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static List extractColumnInfo( String oldExpression )
			throws BirtException
	{
		if ( oldExpression == null )
			return new ArrayList( );

		return ExpressionParserUtility.compileColumnExpression( oldExpression );
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
			return ExpressionParserUtility.hasAggregation( expression );
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