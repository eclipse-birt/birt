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
 *	This class help to manipulate expressions. 
 *
 */
public final class ExpressionUtil
{
	/** prefix for row */
	private static final String ROW_INDICATOR = "row";
	
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
	 * Return a row expression text according to given row index, which
	 * is 1-based.
	 * 
	 * @param index
	 * @return
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
	 * Translate the old expression with "row" as indicator to new expression
	 * using "dataSetRow" as indicator in xml file.
	 * 
	 * NOTE: "&quot;" will not be parse as '"'
	 * 
	 * @param xmlString
	 * @return
	 */
	public static List extractColumnExpressionsFromXML( String xmlString )
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
					while ( i < chars.length )
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
								addCandidateColumnToList( xmlString, result, i, j );
							}
						}
					}
				}
			}
		}

		return result;
	}

	/**
	 * @param xmlString
	 * @param result
	 * @param i
	 * @param j
	 */
	private static void addCandidateColumnToList( String xmlString, List result, int i, int j )
	{
		Object candidate = xmlString.substring( j,i ).trim( );
		if ( candidate.toString( ).startsWith( "\"" )
				&& candidate.toString( ).endsWith( "\"" ) )
			candidate = candidate.toString( ).substring( 1,	candidate.toString( ).length( ) - 1 ); 
		else
		{
			try{
				candidate = new Integer( candidate.toString( ) );
			}catch (Exception e)
			{
				candidate = null;
			}
		}
		if( candidate!= null )
			result.add( candidate );
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
		if ( (operator >= 'A' && operator <= 'Z')
			 || (operator >='a' && operator <= 'z')
			 || operator == '_')
			return false;
		
		return true;
	}
	
	
}
