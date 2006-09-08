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

package org.eclipse.birt.report.engine.script.internal.element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IHighLightRule;
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;

public class Row extends ReportElement implements IRow
{

	public Row( RowHandle row )
	{
		super( row );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getHeight()
	 */

	public String getHeight( )
	{
		DimensionHandle height = ( (RowHandle) handle ).getHeight( );
		return ( height == null ? null : height.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getBookmark()
	 */

	public String getBookmark( )
	{
		return ( (RowHandle) handle ).getBookmark( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#setBookmark(java.lang.String)
	 */

	public void setBookmark( String value ) throws ScriptException
	{
		try
		{
			( (RowHandle) handle ).setBookmark( value );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * Add HighLightRule
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	public void addHighLightRule( IHighLightRule rule ) throws ScriptException
	{
		if ( rule == null )
			return;

		PropertyHandle propHandle = handle
				.getPropertyHandle( Style.HIGHLIGHT_RULES_PROP );
		try
		{
			propHandle.addItem( rule.getStructure( ) );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

	public IHighLightRule[] getHighLightRule( )
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( Style.HIGHLIGHT_RULES_PROP );
		Iterator iterator = propHandle.iterator( );

		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			HighlightRuleHandle ruleHandle = (HighlightRuleHandle) iterator
					.next( );
			HighLightRuleImpl h = new HighLightRuleImpl( ruleHandle );
			rList.add( h );
			++count;
		}

		return (IHighLightRule[]) rList.toArray( new IHighLightRule[count] );
	}

	public void removeHighLightRules( ) throws ScriptException
	{

		PropertyHandle propHandle = handle
				.getPropertyHandle( Style.HIGHLIGHT_RULES_PROP );
		List structureList = new ArrayList( );
		Iterator iterator = propHandle.iterator( );

		while ( iterator.hasNext( ) )
		{
			HighlightRuleHandle highHandle = (HighlightRuleHandle) iterator
					.next( );
			structureList.add( highHandle );
		}
		try
		{
			propHandle.removeItems( structureList );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}
}
