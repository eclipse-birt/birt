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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

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

	private IResultSet rset;
	private ArrayList bindingNames = new ArrayList( );

	private static final Pattern rowWithIndex = Pattern.compile( "(row\\[\\d+\\])",
			Pattern.CASE_INSENSITIVE );

	private static final Pattern rowWithWord = Pattern.compile( "(row\\[\\w+\\])",
			Pattern.CASE_INSENSITIVE );

	public RowData( IResultSet rset, ReportItemHandle element )
	{
		this.rset = rset;
		// intialize the bindings and bindingNames
		if ( element != null )
		{
			addColumnBindings( element.columnBindingsIterator( ) );

			if ( element instanceof ListingHandle )
			{
				// add the bindings in the group
				ListingHandle list = (ListingHandle) element;
				Iterator groupIter = list.getGroups( ).iterator( );
				while ( groupIter.hasNext( ) )
				{
					GroupHandle group = (GroupHandle) groupIter.next( );
					addColumnBindings( group.columnBindingsIterator( ) );
				}
			}
		}
	}

	private void addColumnBindings( Iterator bindingIter )
	{
		if ( bindingIter != null )
		{
			while ( bindingIter.hasNext( ) )
			{
				ComputedColumnHandle binding = (ComputedColumnHandle) bindingIter.next( );
				bindingNames.add( binding.getName( ) );
			}
		}
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
	 * @deprecated
	 * @param expression
	 * @return the evaluated value of the provided expression
	 * @throws ScriptException
	 */
	public Object getExpressionValue( String expression )
			throws ScriptException
	{
		expression = process( expression );
		return rset.evaluate( expression );
	}

	/**
	 * @deprecated
	 */
	public Object getExpressionValue( int index ) throws ScriptException
	{
		String name = getColumnName( index );
		if ( name != null )
		{
			return getColumnValue( name );
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

	public int getExpressionCount( )
	{
		return getColumnCount( );
	}

	public Object getColumnValue( String name ) throws ScriptException
	{
		try
		{
			return rset.getValue( name );
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
		return null;
	}

	/**
	 * get column value by index
	 * index start from 0
	 */
	public Object getColumnValue( int index ) throws ScriptException
	{
		String name = getColumnName( index );
		if ( name != null )
		{
			return getColumnValue( name );
		}
		return null;
	}

	/**
	 * get column name by index
	 * index start from 0
	 */
	public String getColumnName( int index )
	{
		if ( index < bindingNames.size( ) )
		{
			return (String) bindingNames.get( index );
		}
		return null;
	}

	public int getColumnCount( )
	{
		return bindingNames.size( );
	}

}
