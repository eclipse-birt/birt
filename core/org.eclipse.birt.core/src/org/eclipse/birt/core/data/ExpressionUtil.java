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

/**
 *	This class help to manipulate expressions. 
 *
 */
public final class ExpressionUtil
{
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
	 * Translate the old expression with "row" as indicator to new expression using
	 * "dataSetRow" as indicator.
	 * 
	 * @param oldExpression
	 * @return
	 */	
	public static String translateOldExpression( String oldExpression )
	{
		if( oldExpression == null )
			return null;
		char[] chars = oldExpression.toCharArray( );
		
		//5 is the minium length of expression that can cantain a row expression
		if( chars.length < 5)
			return oldExpression;
		else
		{
			boolean candidateKey = true;
			boolean omitNextQuote = false;
			int retrieveSize = 0;
			for( int i = 0; i < chars.length; i++ )
			{
				if( chars[i] == '/')
				{
					if( i > 0 && chars[i-1] == '/')
					{
						retrieveSize++;
						while( i < chars.length )
						{
							i++;
							retrieveSize++;
							if( chars[i]=='\n')
							{
								break;
							}	
						}
						retrieveSize++;
						i++;
					}
				}else if( chars[i] == '*'  )
				{
					if( i > 0 && chars[i-1] == '/' )
					{
						i++;
						retrieveSize = retrieveSize+2;
						while( i < chars.length )
						{
							i++;
							retrieveSize++;
							if( chars[i-1] == '*' && chars[i] == '/')
							{
								break;
							}
						}
						retrieveSize++;
						i++;
					}
				}
				
				if( (!omitNextQuote) && chars[i] == '"')
					candidateKey = !candidateKey;
				if( chars[i] == '\\')
					omitNextQuote = true;
				else
					omitNextQuote = false;
				if( i >= retrieveSize + 3 ){
					if ( candidateKey
							&& chars[i - retrieveSize - 3] == 'r'
							&& chars[i - retrieveSize - 2] == 'o'
							&& chars[i - retrieveSize - 1] == 'w' )
					{
						if( i-retrieveSize-4 <=0 || isValidProceeding(chars[i - retrieveSize - 4]))
						{
							if(chars[i] == ' '||chars[i] == '.'||chars[i]=='[')
							{
								String firstPart = oldExpression.substring( 0, i-retrieveSize-3 );
								String secondPart = translateOldExpression( oldExpression.substring( i-retrieveSize ));
								return firstPart+"dataSetRow"+ secondPart;
							}
						}
					}
				}
			}
		}
		return oldExpression;
	}
	
	//Test whether the char immediately before the candidate "row" key is valid.
	private static boolean isValidProceeding( char operator )
	{
		if ( (operator >= 'A' && operator <= 'Z')
			 || (operator >='a' && operator <= 'z')
			 || operator == '_')
			return false;
		
		return true;
	}
}
