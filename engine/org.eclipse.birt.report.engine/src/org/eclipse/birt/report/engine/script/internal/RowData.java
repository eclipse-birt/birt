/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * A class representing expression results. Can be used to get values of
 * expressions defined on a report item. Implements lazy lookup; values are not
 * evaluated until they are requested.
 * 
 * Some processing is done to the expressions to make it easier for the user.
 * Example: It is ok to write row[CUSTOMERNAME] even though the correct
 * expression would be row["CUSTOMERNAME"]
 * 
 */

public class RowData implements IRowData
{
	private IResultIterator rsIterator;

	private List valueExpressions;

	private static final Pattern rowWithIndex = Pattern.compile(
			"(row\\[\\d+\\])", Pattern.CASE_INSENSITIVE );

	private static final Pattern rowWithWord = Pattern.compile(
			"(row\\[\\w+\\])", Pattern.CASE_INSENSITIVE );

	public RowData( IResultIterator rsIterator, List valueExpressions )
	{
		this.rsIterator = rsIterator;
		this.valueExpressions = valueExpressions;
	}

	/**
	 * Get the value of the provided expression. The expression must be defined
	 * on the report item. Some processing is done to the expression to make
	 * thing easier. It is ok to for an expression to contain things like
	 * row[CUSTOMERNAME] for example (will be replaced with row["CUSTOMENAME"]).
	 * row[123] will be kept as row[123] (index lookup). The regex used is to
	 * find things to replace is: row\\[\\w+\\], Pattern.CASE_INSENSITIVE minus
	 * row\\[\\d+\\], Pattern.CASE_INSENSITIVE.
	 * 
	 * @param expression
	 * @return the evaluated value of the provided expression
	 * @throws ScriptException
	 */
	public Object getExpressionValue( String expression )
			throws ScriptException
	{
		expression = process( expression );
		return eval( expression );
	}

	public Object getExpressionValue( int index ) throws ScriptException
	{
		if ( index <= valueExpressions.size( ) )
		{
			IBaseExpression expr = ( IBaseExpression ) valueExpressions
					.get( index - 1 );
			if (expr == null)
				return null;
			try
			{
				return rsIterator.getValue( expr );
			} catch ( BirtException e )
			{
				throw new ScriptException( e.getLocalizedMessage( ) );
			}
		}
		return null;
	}

	// Process the expression (replace row[something] with row["something"])
	private String process( String expression )
	{
		if ( expression == null )
			return null;
		expression = expression.trim( );
		// Replace row[something] with row["something"]
		Matcher mWord = rowWithWord.matcher( expression );
		StringBuffer sb = new StringBuffer( );
		while ( mWord.find( ) )
		{
			String group = mWord.group( 1 );
			// TODO: This could probably be merged into the main pattern
			Matcher mIndex = rowWithIndex.matcher( group );
			// Don't replace row[123] with row["123"] (index)
			if ( !mIndex.matches( ) )
			{
				group = group.replaceAll( "\\[", "[\"" );
				group = group.replaceAll( "\\]", "\"]" );
			}
			mWord.appendReplacement( sb, group );
		}
		mWord.appendTail( sb );
		return sb.toString( );
	}

	private Object eval( String expression ) throws ScriptException
	{
		if ( valueExpressions == null )
			return null;
		Iterator exprIt = valueExpressions.iterator( );
		while ( exprIt.hasNext( ) )
		{
			IBaseExpression expr = ( IBaseExpression ) exprIt.next( );
			if ( !( expr instanceof IScriptExpression ) )
				continue;
			String rowExpr = ( ( IScriptExpression ) expr ).getText( );
			if ( expression.equals( rowExpr ) )
			{
				try
				{
					return rsIterator.getValue( expr );
				} catch ( BirtException e )
				{
					throw new ScriptException( e.getLocalizedMessage( ) );
				}
			}
		}
		return null;
	}

	public int getExpressionCount( )
	{
		if ( valueExpressions == null )
			return 0;
		return valueExpressions.size( );
	}

}
