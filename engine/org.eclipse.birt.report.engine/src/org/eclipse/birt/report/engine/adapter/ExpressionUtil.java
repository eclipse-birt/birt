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

package org.eclipse.birt.report.engine.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

/**
 * This class help to manipulate expressions.
 * 
 */
public final class ExpressionUtil
{
	private static String TOTAL_PREFIX = "TOTAL_COLUMN_";
	private static int totalColumnSuffix = 0;

	/**
	 * 
	 * @param exprs
	 * @return
	 */
	public static ITotalExprBindings prepareTotalExpressions( List exprs )
	{
		
		TotalExprBinding result = new TotalExprBinding();
		List l = new ArrayList( );
		for ( int i = 0; i < exprs.size( ); i++ )
		{
			
			Object key = exprs.get( i );
			
			result.addColumnBindings( l );
			if ( key instanceof String )
			{
				String expr = key == null ? null : key.toString( );
				String newExpr = prepareTotalExpression( expr, l );
				result.addColumnBindings( l );
				result.addNewExpression( newExpr );
			}
			else if ( key instanceof IConditionalExpression )
			{
				addConditionalExprBindings( result, key, l );
			}
		}
		return result;
	}

	/**
	 * @param result
	 * @param key
	 */
	private static void addConditionalExprBindings( TotalExprBinding result, Object key, List bindings )
	{
		IConditionalExpression ce = (IConditionalExpression) key;

		String expr = ce.getExpression( ) == null ? null
				: ce.getExpression( ).getText( );
		String operand1 = ce.getOperand1( ) == null ? null
				: ce.getOperand1( ).getText( );
		String operand2 = ce.getOperand2( ) == null ? null
				: ce.getOperand2( ).getText( );

		String newExpr = prepareTotalExpression( expr, bindings );
		String newOperand1 = prepareTotalExpression( operand1,
				bindings );
		String newOperand2 = prepareTotalExpression( operand2,
				bindings );

		ConditionalExpression newCondExpr = new ConditionalExpression( newExpr == null
				? null : new ScriptExpression( newExpr ),
				ce.getOperator( ),
				newOperand1 == null ? null
						: new ScriptExpression( newOperand1 ),
				newOperand2 == null ? null
						: new ScriptExpression( newOperand2 ) );

		String bindingName = TOTAL_PREFIX + totalColumnSuffix;
		totalColumnSuffix++;

		ColumnBinding columnBinding = new ColumnBinding( bindingName,
				newCondExpr );

		List allColumnBindings = new ArrayList( );

		allColumnBindings.add( columnBinding );
		
		result.addColumnBindings( allColumnBindings );
		
		result.addNewExpression( "row[\""
						+ JavascriptEvalUtil.transformToJsConstants( bindingName )
						+ "\"]" );
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

							boolean shouldAddToList = true;
							for( int j = 0; j < columnBindings.size( ); j++ )
							{
								IBaseExpression expression = ((IColumnBinding)columnBindings.get( j )).getBoundExpression( );
								if( expression instanceof IScriptExpression )
								{
									if( oldExpression.equals( ((IScriptExpression) expression).getText( ) ))
									{
										shouldAddToList = false;
										name = ((IColumnBinding)columnBindings.get( j )).getResultSetColumnName( );
										break;
									}
								}
							}
							if ( shouldAddToList )
							{
								name = TOTAL_PREFIX + totalColumnSuffix;
								totalColumnSuffix++;
								columnBindings.add( new ColumnBinding( name, expr ) );
							}
							
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
 */
class TotalExprBinding implements ITotalExprBindings
{

	/**
	 * 
	 */
	private List newExprs;
	private List columnBindings;

	TotalExprBinding()
	{
		this.newExprs = new ArrayList();
		this.columnBindings = new ArrayList();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.data.ITotalExprBindings#getNewExpression()
	 */
	public List getNewExpression( )
	{
		return this.newExprs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.data.ITotalExprBindings#getColumnBindings()
	 */
	public IColumnBinding[] getColumnBindings( )
	{
		IColumnBinding[] result = new ColumnBinding[columnBindings.size()];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = (IColumnBinding) columnBindings.get( i );
		}
		return result;
	}
	
	public void addNewExpression( String expr )
	{
		this.newExprs.add( expr );
	}

	public void addColumnBindings( List columnBindingList )
	{
		for( int i = 0; i < columnBindingList.size( ); i++ )
		{
			if( !this.columnBindings.contains( columnBindingList.get( i ) ))
			{
				this.columnBindings.add( columnBindingList.get( i ) );
			}
		}
	}
	public List getNewExpression( Object key )
	{
		// TODO Auto-generated method stub
		return null;
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
	private IBaseExpression expression;

	ColumnBinding( String name, IBaseExpression expression )
	{
		this.columnName = name;
		this.expression = expression;
	}

	ColumnBinding( String name, String expression )
	{
		this.columnName = name;
		this.expression = new ScriptExpression( expression );
	}
	
	public String getResultSetColumnName( )
	{
		return this.columnName;
	}

	public IBaseExpression getBoundExpression( )
	{
		return this.expression;
	}

}