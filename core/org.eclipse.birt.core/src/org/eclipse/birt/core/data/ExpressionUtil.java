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
	private static String TOTAL_PREFIX = "TOTAL_COLUMN_";
	private static int totalColumnSuffix = 0;
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
	 * @param oldExpression
	 * @return
	 * @throws BirtException
	 */
	public static List extractColumnExpressions( String oldExpression )
			throws BirtException
	{
		if ( oldExpression == null )
			return new ArrayList( );

		return ExpressionParserUtility.compileColumnExpression( oldExpression );
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
	 * Translate the old expression with "row" as indicator to new expression
	 * using "dataSetRow" as indicator in xml file.
	 * 
	 * NOTE: "&quot;" will not be parse as '"'
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List extractColumnNamesFromXML( String xmlString )
	{
		List result = new ArrayList( );
		if ( xmlString == null )
			return null;

		char[] chars = xmlString.toCharArray( );

		int retrieveSize = 0;
		for ( int i = 0; i < chars.length; i++ )
		{
			retrieveSize = 0;
			if ( chars[i] == '-' )
			{
				if ( i > 1 && chars[i - 1] == '!' && chars[i - 2] == '<' )
				{
					i++;
					retrieveSize = retrieveSize + 3;
					while ( i < chars.length - 1 )
					{
						i++;
						retrieveSize++;
						if ( chars[i - 2] == '-'
								&& chars[i - 1] == '-' && chars[i] == '>' )
						{
							break;
						}
					}
					retrieveSize++;
					i++;
				}
			}

			if ( i >= retrieveSize + 3 )
			{
				if ( chars[i - retrieveSize - 3] == 'r'
						&& chars[i - retrieveSize - 2] == 'o'
						&& chars[i - retrieveSize - 1] == 'w' )
				{
					if ( i - retrieveSize - 4 <= 0
							|| isValidProceeding( chars[i - retrieveSize - 4] ) )
					{
						if ( chars[i] == '.' )
						{
							i++;
							int j = i;
							while ( chars[i] != ' '
									&& chars[i] != '<' && chars[i] != '>' )
							{
								i++;
								if ( i >= chars.length )
									return result;
							}
							result.add( xmlString.substring( j, i ) );
						}
						else if ( chars[i] == '[' )
						{
							i++;
							int j = i;
							boolean push = true;
							while ( chars[i] != ']' || chars[i - 1] == '\\' )
							{
								i++;
								if ( i >= chars.length )
									return result;
								if ( chars[i] == '<' || chars[i] == '>' )
								{
									push = false;
									break;
								}
							}
							if ( push )
							{
								addCandidateColumnToList( xmlString,
										result,
										i,
										j );
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * 
	 * @param exprs
	 * @return
	 */
	public static List prepareTotalExpressions( List exprs )
	{
		List result = new ArrayList( );
		for ( int i = 0; i < exprs.size( ); i++ )
		{
			List l = new ArrayList( );
			String expr = exprs.get( i ) == null ? null : exprs.get( i )
					.toString( );
			String newExpr = prepareTotalExpression( expr, l );
			TotalExprBinding teb = new TotalExprBinding( newExpr, l );
			result.add( teb );
		}
		return result;
	}

	/**
	 * Translate the old expression with "row" as indicator to new expression
	 * using "dataSetRow" as indicator.
	 * 
	 * @param oldExpression
	 * @return
	 */
	private static String prepareTotalExpression( String oldExpression,
			List columnBindings )
	{
		if ( oldExpression == null )
			return null;

		char[] chars = oldExpression.toCharArray( );

		// 5 is the minium length of expression that can cantain a row
		// expression
		if ( chars.length < 8 )
			return oldExpression;
		else
		{
			ParseIndicator indicator = new ParseIndicator( 0,
					0,
					false,
					false,
					true,
					true );
			for ( int i = 0; i < chars.length; i++ )
			{
				indicator = getParseIndicator( chars,
						i,
						indicator.omitNextQuote( ),
						indicator.getCandidateKey1( ),
						indicator.getCandidateKey2( ) );

				i = indicator.getNewIndex( );

				if ( i >= indicator.getRetrieveSize( ) + 6 )
				{
					if ( indicator.isCandidateKey( )
							&& chars[i - indicator.getRetrieveSize( ) - 6] == 'T'
							&& chars[i - indicator.getRetrieveSize( ) - 5] == 'o'
							&& chars[i - indicator.getRetrieveSize( ) - 4] == 't'
							&& chars[i - indicator.getRetrieveSize( ) - 3] == 'a'
							&& chars[i - indicator.getRetrieveSize( ) - 2] == 'l'
							&& chars[i - indicator.getRetrieveSize( ) - 1] == '.' )
					{
						if ( i - indicator.getRetrieveSize( ) - 7 <= 0
								|| isValidProceeding( chars[i
										- indicator.getRetrieveSize( ) - 7] ) )
						{
							String firstPart = oldExpression.substring( 0, i
									- indicator.getRetrieveSize( ) - 6 );

							int startIndex = i - indicator.getRetrieveSize( ) - 6;
							i = advanceToNextValidEncloser( chars, i );
							String secondPart = "";
							String name = "";
							String expr = "";

							if ( i < chars.length )
							{
								int endIndex = i + 1;

								expr = oldExpression.substring( startIndex,
										endIndex );

								secondPart = prepareTotalExpression( oldExpression.substring( i
										+ 1 - indicator.getRetrieveSize( ) ),
										columnBindings );
							}
							else
							{
								expr = oldExpression.substring( startIndex );
							}

							name = TOTAL_PREFIX + totalColumnSuffix;
							totalColumnSuffix++;
							columnBindings.add( new ColumnBinding( name, expr ) );

							String newExpression = firstPart
									+ "row[\"" + name + "\"]" + secondPart;

							return newExpression;
						}
					}
				}

			}

		}

		return oldExpression;
	}

	/**
	 * 
	 * @param chars
	 * @param i
	 * @return
	 */
	private static int advanceToNextValidEncloser( char[] chars, int i )
	{
		boolean isTotalConstants = true;
		int numberOfOpenBracket = 0;
		while ( i < chars.length )
		{
			ParseIndicator pid = getParseIndicator( chars, i, false, true,true);
			i = pid.getNewIndex( );
			if( pid.isCandidateKey( ))
			if ( chars[i] == '(' )
			{
				isTotalConstants = false;
				numberOfOpenBracket ++;
			}

			if ( isTotalConstants )
			{
				if ( !isValidProceeding( chars[i] ) )
					i++;
				else
					break;
			}
			else
			{
				if ( chars[i] != ')' )
					i++;
				else
				{
					if( chars[i] == ')')
					{
						numberOfOpenBracket--;
					}
					if( numberOfOpenBracket == 0)
					{
						break;
					}else
					{
						i++;
					}
				}
			}
		}

		if ( isTotalConstants )
			i--;
		return i;
	}

	/**
	 * @param xmlString
	 * @param result
	 * @param i
	 * @param j
	 */
	private static void addCandidateColumnToList( String xmlString,
			List result, int i, int j )
	{
		Object candidate = xmlString.substring( j, i ).trim( );
		if ( candidate.toString( ).startsWith( "\"" )
				&& candidate.toString( ).endsWith( "\"" ) )
			candidate = candidate.toString( ).substring( 1,
					candidate.toString( ).length( ) - 1 );
		else
		{
			try
			{
				candidate = new Integer( candidate.toString( ) );
			}
			catch ( Exception e )
			{
				candidate = null;
			}
		}
		if ( candidate != null )
			result.add( candidate );
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
 * 
 * @author lzhu
 * 
 */
class TotalExprBinding implements ITotalExprBindings
{

	/**
	 * 
	 */
	private String newExpr;
	private IColumnBinding[] columnBindings;

	/**
	 * 
	 * @param newExpr
	 * @param columnBindings
	 */
	TotalExprBinding( String newExpr, List columnBindings )
	{
		this.newExpr = newExpr;
		this.columnBindings = new IColumnBinding[columnBindings.size( )];
		for ( int i = 0; i < columnBindings.size( ); i++ )
		{
			this.columnBindings[i] = (IColumnBinding) columnBindings.get( i );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.data.ITotalExprBindings#getNewExpression()
	 */
	public String getNewExpression( )
	{
		return this.newExpr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.data.ITotalExprBindings#getColumnBindings()
	 */
	public IColumnBinding[] getColumnBindings( )
	{
		return this.columnBindings;
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

	ColumnBinding( String name, String expression )
	{
		this.columnName = name;
		this.expression = expression;
	}

	public String getResultSetColumnName( )
	{
		return this.columnName;
	}

	public String getBoundExpression( )
	{
		return this.expression;
	}

}